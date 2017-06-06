package it.devchallange.podgorny.dao.jdbc;

import it.devchallange.podgorny.entity.Entity;

public interface ArticleJDBCDAOService<T extends Entity> extends JDBCDAOService<T> {
    String getSqlInertNewRevision();
    String getSqlUpdateArticle() ;
    String getSqlFetchArticleRevision();

}

