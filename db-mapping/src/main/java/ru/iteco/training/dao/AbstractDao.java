package ru.iteco.training.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Repository
@ComponentScan("ru.iteco.training")
@Transactional
public class AbstractDao<T extends Serializable> {
    private Class<T> clazz;

    final
    SessionFactory sessionFactory;

    @Autowired
    public AbstractDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setClazz(final Class<T> clazzToSet) {
        clazz = clazzToSet;
    }

    public T get(final long id) {
        return (T) getCurrentSession().get(clazz, id);
    }

    public List<T> getAll() {
        return sessionFactory.getCurrentSession().createQuery("from " + clazz.getName()).list();//---
    }

    public void save(final T entity) {
        getCurrentSession().persist(entity);
    }

    public T update(final T entity) {
        return (T) getCurrentSession().merge(entity);
    }

    public void delete(final T entity) {
        getCurrentSession().delete(entity);
    }

    public void deleteById(final long id) {
        final T entity = get(id);
        delete(entity);
    }

    protected final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
