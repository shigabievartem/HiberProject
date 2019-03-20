package ru.iteco.training.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.iteco.training.objects.AttributeType;

@Component
@Qualifier("attributeType")
//@Transactional
public class AttributeTypeDaoImpl extends AbstractDao2 {
    public AttributeTypeDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        setClazz(AttributeType.class);
    }
}
