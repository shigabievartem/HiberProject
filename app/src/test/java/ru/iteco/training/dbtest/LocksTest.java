package ru.iteco.training.dbtest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iteco.training.dao.*;
import ru.iteco.training.objects.Attribute;
import ru.iteco.training.objects.AttributeType;
import ru.iteco.training.objects.City;
import ru.iteco.training.objects.Mayor;

import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;

/**
 * Тест работы блокировок. Следует обратить особое внимание на то, где и как используется Session.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AbstractDao.class)
//@Ignore
public class LocksTest {

    @Autowired
    CityDaoImpl cityDao;
    @Autowired
    MayorDaoImpl mayorDao;
    @Autowired
    AttributeDaoImpl attributeDao;
    @Autowired
    AttributeTypeDaoImpl attributeTypeDao;
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    DataSource dataSource;

    @Test
    public void testMayor() {
        int oldSize = mayorDao.getAll().size();

        Mayor mayor = new Mayor();
        mayor.setFio("Пупкин Василий Евгеньевич");
        mayorDao.save(mayor);

        Long idMayor = mayor.getId();

        Assert.assertNotNull(mayorDao.get(idMayor));
        Assert.assertEquals(oldSize + 1, mayorDao.getAll().size());

        mayor.setFio("new FIO");
        Mayor newMayor = (Mayor) mayorDao.update(mayor);

        Assert.assertEquals(mayor, newMayor);

        mayorDao.delete(mayor);

        Assert.assertNull(mayorDao.get(idMayor));
        Assert.assertEquals(oldSize, mayorDao.getAll().size());
    }

    @Test
//    @Transactional
    public void testCity() {
        long cityId = 2L;
        long mayorId = 3L;
        long attributeId = 4L;
        long attributeTypeId = 5L;
        City city = (City) cityDao.get(cityId);
        Assert.assertNotNull(city);
        Mayor cityMayor = city.getMayor();
        Collection<Attribute> cityAttributes = city.getAttributes();

        Mayor mayor = (Mayor) mayorDao.get(mayorId);

        Attribute attribute = (Attribute) attributeDao.get(attributeId);

        AttributeType attributeType = (AttributeType) attributeTypeDao.get(attributeTypeId);

        Assert.assertEquals(mayor, cityMayor);
        Assert.assertEquals(1, cityAttributes.size());
        Assert.assertTrue(cityAttributes.contains(attribute));

        Assert.assertEquals(attributeType, cityAttributes.iterator().next().getType());
    }

    @Test
//    @Transactional
    public void testAttribute() {
        AttributeType atrType = new AttributeType();
        atrType.setName("Test type");
        attributeTypeDao.save(atrType);

        Attribute attribute = new Attribute();
        attribute.setName("Test attribute");
        attribute.setType(atrType);
        attribute.setValue("Hello World");
        attributeDao.save(attribute);

        try {
            atrType = (AttributeType) attributeTypeDao.get(atrType.getId());
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            atrType = session.get(AttributeType.class, atrType);
            atrType.getAttributes().size();
            session.getTransaction().commit();
            session.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        attributeDao.delete(attribute);
        attributeTypeDao.delete(atrType);
    }

    @Test
//    @Transactional
    public void testPessimisticLock() throws InterruptedException {
        final String expectedCityName = "Новосибирск";
        final String cityNameToSetInMainThread = "Санкт-Петербург";
        final long waitTime = 2000;

        Mayor mayor = new Mayor();
        mayor.setFio("Test Mayor");
        MayorDaoImpl mayorDaoLocal = new MayorDaoImpl(sessionFactory);
        mayorDaoLocal.save(mayor);

        City city = new City();
        city.setName("testCity");
        city.setAttributes(Collections.emptyList());
        city.setMayor(mayor);
        CityDaoImpl cityDaoLocal = new CityDaoImpl(sessionFactory);
        cityDaoLocal.save(city);

        System.out.println(
                format("Начало теста. Id города %s, имя города '%s'", city.getId(), city.getName())
        );
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        City persistentCity = session.get(City.class, city.getId());
        session.lock(persistentCity, LockModeType.PESSIMISTIC_WRITE);
        CompletableFuture feature = CompletableFuture.runAsync(() -> {
                    // В отдельном потоке мы больше не можем использовать нашу session, потому что объект session
                    // не является потокобезопасным (читаем java-doc Session, 55 строка), и мы не можем гарантировать его валидность.
                    // Поэтому будем использовать DAO, который открывает новую сессию на каждый вызов метода.
                    System.out.println(
                            format("Пробуем изменить имя города в отдельном потоке (новое имя - '%s')", expectedCityName)
                    );
                    long startTime = System.currentTimeMillis();

                    try (Statement statement = dataSource.getConnection().createStatement()) {
                        statement.execute(format("update city set name = '%s' where city_id = %d", expectedCityName, persistentCity.getId()));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    long totalExcecutionTime = System.currentTimeMillis() - startTime;
                    System.out.println(
                            format("Транзакция выполнялась %s ms, транзакция висела в ожидании примерно %s ms",
                                    (totalExcecutionTime - waitTime),
                                    waitTime
                            )
                    );
                    System.out.println(format(
                            "Выполнение запроса в отдельном потоке завершилось, итоговое имя города - '%s'",
                            ((City) cityDaoLocal.get(persistentCity.getId())).getName()
                            )
                    );
                }
        );
        Thread.sleep(waitTime);
        persistentCity.setName(cityNameToSetInMainThread);
        session.update(persistentCity);
        session.flush();
        System.out.println(format("Теперь имя города '%s'", session.get(City.class, persistentCity.getId()).getName()));
        session.getTransaction().commit();
        feature.join();
        Assert.assertNotEquals(cityNameToSetInMainThread, ((City) cityDaoLocal.get(city.getId())).getName());
        Assert.assertEquals(expectedCityName, ((City) cityDaoLocal.get(city.getId())).getName());
        // Вопрос на засыпку. Почему не получлии OptimisticLockException? Ведь сущность была обновлена два раза.

    }

    //
    @Test(expected = OptimisticLockException.class)
    public void testOptimisticLock() throws InterruptedException {

        Mayor mayor = new Mayor();
        mayor.setFio("Test Mayor");
        MayorDaoImpl mayorDaoLocal = new MayorDaoImpl(sessionFactory);
        mayorDaoLocal.save(mayor);

        City city = new City();
        city.setName("testCity");
        city.setAttributes(Collections.emptyList());
        city.setMayor(mayor);
        CityDaoImpl cityDaoLocal = new CityDaoImpl(sessionFactory);
        cityDaoLocal.save(city);

//        System.out.println(
//                format("Начало теста. Id города %s, имя города '%s', версия %s", city.getId(), city.getName(), city.getVersion())
//        );

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        City persistentCity = session.get(City.class, city.getId());
        session.lock(persistentCity, LockModeType.OPTIMISTIC);
        System.out.println("Получили персистентный объект из базы, выполнили оптимистичную блокировку");
        CompletableFuture feature = CompletableFuture.runAsync(() -> {
                    System.out.println(
                            format("Меняем имя города в отдельном потоке, в отдельной сессии (новое имя '%s')", "Дубаи")
                    );
                    city.setName("Дубаи");
//                    System.out.println("Новая версия у city - " +
//                            ((City)cityDaoLocal.update(city)).getVersion() // Будет выполнено в новой сессии
//                    );
                }
        );
        feature.join();
        System.out.println("Основной поток подождал, пока выполнилось обновление сущности в базе");
        persistentCity.setName("Астана");
        System.out.println("Пробуем выставить новое имя города - 'Астана', обновление прошло успешно, " +
                "так как текущая сессия не знает об изменениях, сделанных в рамках другой сессии");
        session.update(persistentCity);
        try {
            session.flush();
        } catch (OptimisticLockException e) {
            System.out.println("При вызове session.flush() поймали optimisticLockException, так как " +
                    "версия у персистентного объекта отличается от той, что в базе");
            throw e;
        }
        session.getTransaction().commit();
    }

//    private static SessionFactory sessionFactory;
//
//    @BeforeClass
//    public static void init() {
//        sessionFactory = hibernateConfiguration.buildSessionFactory();
//    }
//
//    @Test
//    public void testPessimisticLock() throws InterruptedException {
//        final String excpectedCityName = "Новосибирск";
//        final String cityNameToSetInMainThread = "Санкт-Петербург";
//        final long waitTime = 2000;
//        CityDao cityDao = new CityDao(sessionFactory);
//        City city = cityDao.save(getDefaultCity());
//        System.out.println(
//                format("Начало теста. Id города %s, имя города '%s'", city.getId(), city.getName())
//        );
//        Session session = sessionFactory.openSession();
//        session.beginTransaction();
//        City persistentCity = session.get(City.class, city.getId());
//        session.lock(persistentCity, LockModeType.PESSIMISTIC_WRITE);
//        CompletableFuture feature = CompletableFuture.runAsync(() -> {
//                    // В отдельном потоке мы больше не можем использовать нашу session, потому что объект session
//                    // не является потокобезопасным (читаем java-doc Session, 55 строка), и мы не можем гарантировать его валидность.
//                    // Поэтому будем использовать DAO, который открывает новую сессию на каждый вызов метода.
//                    System.out.println(
//                            format("Пробуем изменить имя города в отдельном потоке (новое имя - '%s')", excpectedCityName)
//                    );
//                    long startTime = System.currentTimeMillis();
//                    executeSqlUsingJdbc(
//                            format("update city set name = '%s'where city_id = %d", excpectedCityName, persistentCity.getId())
//                    );
//                    long totalExcecutionTime = System.currentTimeMillis() - startTime;
//                    System.out.println(
//                            format("Транзакция выполнялась %s ms, транзакция висела в ожидании примерно %s ms",
//                                    (totalExcecutionTime - waitTime),
//                                    waitTime
//                            )
//                    );
//                    System.out.println(format(
//                            "Выполнение запроса в отдельном потоке завершилось, итоговое имя города - '%s'",
//                            cityDao.get(City.class, persistentCity.getId()).getName()
//                            )
//                    );
//                }
//        );
//        Thread.sleep(waitTime);
//        persistentCity.setName(cityNameToSetInMainThread);
//        session.update(persistentCity);
//        session.flush();
//        System.out.println(format("Теперь имя города '%s'", session.get(City.class, persistentCity.getId()).getName()));
//        session.getTransaction().commit();
//        feature.join();
//        Assert.assertNotEquals(cityNameToSetInMainThread, cityDao.get(city.getId()).getName());
//        Assert.assertEquals(excpectedCityName, cityDao.get(city.getId()).getName());
//        // Вопрос на засыпку. Почему не получлии OptimisticLockException? Ведь сущность была обновлена два раза.
//    }
//
//    @Test(expected = OptimisticLockException.class)
//    public void testOptimisticLock() throws InterruptedException {
//        CityDao cityDao = new CityDao(sessionFactory);
//        City city = cityDao.save(getDefaultCity());
//        System.out.println(
//                format("Начало теста. Id города %s, имя города '%s', версия %s", city.getId(), city.getName(), city.getVersion())
//        );
//        Session session = sessionFactory.openSession();
//        session.beginTransaction();
//        City persistentCity = session.get(City.class, city.getId());
//        session.lock(persistentCity, LockModeType.OPTIMISTIC);
//        System.out.println("Получили персистентный объект из базы, выполнили оптимистичную блокировку");
//        CompletableFuture feature = CompletableFuture.runAsync(() -> {
//                    System.out.println(
//                            format("Меняем имя города в отдельном потоке, в отдельной сессии (новое имя '%s')", "Дубаи")
//                    );
//                    city.setName("Дубаи");
//                    System.out.println("Новая версия у city - " +
//                            cityDao.update(city, city.getId()).getVersion() // Будет выполнено в новой сессии
//                    );
//                }
//        );
//        feature.join();
//        System.out.println("Основной поток подождал, пока выполнилось обновление сущности в базе");
//        persistentCity.setName("Астана");
//        System.out.println("Пробуем выставить новое имя города - 'Астана', обновление прошло успешно, " +
//                "так как текущая сессия не знает об изменениях, сделанных в рамках другой сессии");
//        session.update(persistentCity);
//        try {
//            session.flush();
//        } catch (OptimisticLockException e) {
//            System.out.println("При вызове session.flush() поймали optimisticLockException, так как " +
//                    "версия у персистентного объекта отличается от той, что в базе");
//            throw e;
//        }
//        session.getTransaction().commit();
//    }
//
//    private static void executeSqlUsingJdbc(String sql) {
//        Connection connection = null;
//        Statement statement = null;
//        Properties properties = new Properties();
//        properties.setProperty("user", DB_USERNAME);
//        properties.setProperty("password", DB_PASSWORD);
//        try {
//            connection = DriverManager.getConnection(DB_URL, properties);
//            statement = connection.createStatement();
//            statement.execute(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (connection != null) connection.close();
//                if (statement != null) statement.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private City getDefaultCity() {
//        return new City(null,
//                "Москва",
//                new Mayor("Иванов Иван Иванович", null),
//                null
//        );
//    }

}
