package it.devchallange.podgorny.dao;

import it.devchallange.podgorny.entity.Entity;

import java.util.List;
import java.util.Set;

public interface DAOService<T extends Entity> {

    T getEntityById(Long id);
    T getEntityByEntity(T t);
    Set<T> getAll();
    List<T> getAllEntityRevision(T t);

    boolean updateById(Long id, T t);
    boolean deleteById(Long id);

    boolean insert(T t);
    long getInsertedEntityId(T t) ;
    boolean insert(Set<T> setT);

    boolean delete(Set<T> entitySet);
    boolean delete(T entity);
    boolean deleteAll();

    T getEntityRevision(T entity, int revision);
}

