package ru.iteco.training.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Repository
@ComponentScan("ru.iteco.training")
public class AbstractDao2<T extends Serializable> {
    private Class<T> clazz;

    final SessionFactory sessionFactory;

    @Autowired
    public AbstractDao2(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setClazz(final Class<T> clazzToSet) {
        clazz = clazzToSet;
    }

    public T get(final long id) {
        return (T) pushOperation((s) -> {
            return s.get(clazz, id); //---;
        });
    }

    public List<T> getAll() {
        return (List<T>) pushOperation((s) -> {
            return s.createQuery("from " + clazz.getName()).list(); //---;
        });
    }

    public void save(final T entity) {
        pushOperation(session -> {
            session.persist(entity);
        });
    }

    public void delete(final T entity) {
        pushOperation(session -> {
            session.delete(entity);
        });
    }

    public T update(final T entity) {
        return (T) pushOperation((s) -> {
            return s.merge(entity); //---;
        });
    }

    public void deleteById(final long id) {
        final T entity = get(id);
        delete(entity);
    }

    private void pushOperation(Consumer<Session> action) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            action.accept(session);
            session.getTransaction().commit();
        } catch (Exception ex) {
            System.err.println("Save operation error");
            ex.printStackTrace();
        }
    }

    private Object pushOperation(Function<Session, ?> action) {
        Object result = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            result = action.apply(session);
            session.getTransaction().commit();
            return result; //---
        } catch (Exception ex) {
            System.err.println("Save operation error");
            ex.printStackTrace();
        }
        return result; //---
    }
}
