package ru.iteco.training.configurations;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.iteco.training.utils.JavaUtils;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class SessionFactoryConfig {
    String connectionUrl;
    String driverName;
    String user;
    String password;
    String dialect;
    String hbm2ddlAuto;
    String showSql;

    public SessionFactoryConfig() {
        Properties property = JavaUtils.parsePropertyFile(this.getClass(), "/config.properties");

        connectionUrl = property.getProperty(Environment.URL);
        driverName = property.getProperty(Environment.DRIVER);
        user = property.getProperty(Environment.USER);
        password = property.getProperty(Environment.PASS);
        dialect = property.getProperty(Environment.DIALECT);
        hbm2ddlAuto = property.getProperty(Environment.HBM2DDL_AUTO);
        showSql = property.getProperty(Environment.SHOW_SQL);

        System.out.println("properties successfully read");
    }

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(connectionUrl);
        dataSource.setDriverClassName(driverName);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "sessionFactory")
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        Properties jpaProps = new Properties();
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        jpaProps.put(Environment.USER, user);
        jpaProps.put(Environment.PASS, password);
        jpaProps.put(Environment.DIALECT, dialect);
        jpaProps.put(Environment.HBM2DDL_AUTO, hbm2ddlAuto);
        jpaProps.put(Environment.SHOW_SQL, showSql);
//        jpaProps.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        sessionFactory.setHibernateProperties(jpaProps);
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("ru.iteco.training");
        return sessionFactory;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(
            SessionFactory sessionFactory) {

        HibernateTransactionManager txManager
                = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);

        return txManager;
    }


//    @Bean(name = "sessionFactory")
//    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
//    public SessionFactory getSessionFactory() {
//        StandardServiceRegistry registry = null;
//        SessionFactory sessionFactory = null;
//        try {
//
//            // Create registry builder
//            StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
//
//            // Hibernate settings equivalent to hibernate.cfg.xml's properties
//            Map<String, String> settings = new HashMap<>();
//            settings.put(Environment.DRIVER, "org.h2.Driver");
//            settings.put(Environment.URL, "jdbc:h2:~/test");
//            settings.put(Environment.USER, "sa");
//            settings.put(Environment.PASS, "");
//            settings.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
//            settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
//
//            // Apply settings
//            registryBuilder.applySettings(settings);
//
//            // Create registry
//            registry = registryBuilder.build();
//
//            // Create MetadataSources
//            MetadataSources sources = new MetadataSources(registry);
//
//            // Create Metadata
//            Metadata metadata = sources.getMetadataBuilder().build();
//
//            // Create SessionFactory
//            sessionFactory = metadata.getSessionFactoryBuilder().build();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (registry != null) {
//                StandardServiceRegistryBuilder.destroy(registry);
//            }
//        }
//        return sessionFactory;
//    }

}
