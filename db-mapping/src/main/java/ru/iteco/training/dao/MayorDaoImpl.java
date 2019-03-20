package ru.iteco.training.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.iteco.training.objects.Mayor;

@Component
//@Transactional
@Qualifier("mayor")
public class MayorDaoImpl extends AbstractDao2 {
    public MayorDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(Mayor.class);
    }
}
