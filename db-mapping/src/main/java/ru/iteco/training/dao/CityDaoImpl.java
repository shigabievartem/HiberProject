package ru.iteco.training.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.iteco.training.objects.City;

@Component
//@Transactional
public class CityDaoImpl extends AbstractDao2 {
    public CityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(City.class);
    }
}
