package ru.iteco.training.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.iteco.training.objects.Attribute;

@Component
@Qualifier("attribute")
//@Transactional
public class AttributeDaoImpl extends AbstractDao2 {
    public AttributeDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(Attribute.class);
    }
}
