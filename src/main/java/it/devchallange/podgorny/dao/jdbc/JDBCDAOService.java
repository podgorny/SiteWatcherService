package it.devchallange.podgorny.dao.jdbc;

import it.devchallange.podgorny.dao.DAOService;
import it.devchallange.podgorny.entity.Entity;

import java.util.Set;

public interface JDBCDAOService<T extends Entity> extends DAOService<T> {

    String getSqlDeleteAll();

    String getSqlDeleteByID();

    String getSqlGetAll();

    String getSqlFetchById();

    String getSqlFetchByEntity();

    String getSqlInsert();

    String getSqlDeleteByEntity();
}

