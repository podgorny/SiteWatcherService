package it.devchallange.podgorny.dao.jdbc.impl;

import it.devchallange.podgorny.dao.jdbc.ArticleJDBCDAOService;
import it.devchallange.podgorny.dao.jdbc.JDBCDAOService;
import it.devchallange.podgorny.dao.jdbc.utils.DBUtil;
import it.devchallange.podgorny.entity.Article;
import it.devchallange.podgorny.entity.Category;
import it.devchallange.podgorny.entity.Content;
import it.devchallange.podgorny.entity.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

public class ArticleDAOJDBCImpl implements ArticleJDBCDAOService<Article> {
    @Autowired
    private DBUtil dbUtil;
    @Autowired
    @Qualifier("contentDAOJDBC")
    private JDBCDAOService contentDAOJDBC;
    @Autowired
    @Qualifier("titleDAOJDBC")
    private JDBCDAOService titleDAOJDBC;
    @Autowired
    @Qualifier("categoryDAOJDBC")
    private JDBCDAOService categoryDAOJDBC;
    @Autowired
    @Qualifier("siteDAOJDBC")
    private JDBCDAOService siteDAOJDBC;
    @Autowired
    @Qualifier("sqlGetAllArticle")
    private String sqlGetAll;
    @Autowired
    @Qualifier("sqlFetchArticleById")
    private String sqlFetchById;
    @Autowired
    @Qualifier("sqlFetchByEntityArticle")
    private String sqlFetchByEntity;
    @Autowired
    @Qualifier("sqlInsertArticle")
    private String sqlInsert;
    @Autowired
    @Qualifier("sqlDeleteAll")
    private String sqlDeleteAll;
    @Autowired
    @Qualifier("sqlDeleteByID")
    private String sqlDeleteByID;
    @Autowired
    @Qualifier("sqlDeleteByEntityArticle")
    private String sqlDeleteByEntity;
    @Autowired
    @Qualifier("sqlInsertNewRevision")
    private String sqlInsertNewRevision;
    @Autowired
    @Qualifier("sqlUpdateArticle")
    private String sqlUpdateArticle;
    @Autowired
    @Qualifier("sqlFetchArticleRevision")
    private String sqlFetchArticleRevision;
    @Autowired
    @Qualifier("sqlFetchAllArticleRevisions")
    public String sqlFetchAllArticleRevisions;


    @PostConstruct
    private void init() {
        sqlGetAll = sqlGetAll.replace("$TABLE_NAME", "ARTICLE");
        sqlFetchById = sqlFetchById.replace("$TABLE_NAME", "ARTICLE");
        sqlFetchByEntity = sqlFetchByEntity.replace("$TABLE_NAME", "ARTICLE");
        sqlDeleteAll = sqlDeleteAll.replace("$TABLE_NAME", "ARTICLE");
        sqlDeleteByID = sqlDeleteByID.replace("$TABLE_NAME", "ARTICLE");
        sqlDeleteByEntity = sqlDeleteByEntity.replace("$TABLE_NAME", "ARTICLE");
    }

    public Article getEntityById(Long id) {
        return (Article) dbUtil.getEntityByID(DBUtil.entityType.ARTICLE, sqlFetchById, id);
    }

    public Article getEntityByEntity(Article article) {
       return (Article) dbUtil.getByEntity(article, sqlFetchByEntity, sqlFetchById);
    }

    public Set<Article> getAll(){

        return (Set<Article>) dbUtil.getAllEntity(DBUtil.entityType.ARTICLE, sqlGetAll);
    }

    @Override
    public List<Article> getAllEntityRevision(Article article) {
        return dbUtil.getAllEntityRevisions(article, sqlFetchAllArticleRevisions);
    }

    public boolean updateById(Long id, Article article) {
        Article articleFromDB = (Article) dbUtil.getEntityByID(DBUtil.entityType.ARTICLE, sqlFetchById, id);
        if (articleFromDB == null){
            return false;
        }
        if (!article.getCategory().equals(articleFromDB.getCategory()) ||
            !article.getTitle().equals(articleFromDB.getTitle()) ||
            !article.getContent().equals(articleFromDB.getContent())){
            //updating and insert to history
            Content content = article.getContent();
            Title title = article.getTitle();
            Category category = article.getCategory();
            return categoryDAOJDBC.insert(category) &&
                   contentDAOJDBC.insert(content) &&
                   titleDAOJDBC.insert(title) &&
                   dbUtil.updateByID(sqlUpdateArticle, id, article) &&
                   dbUtil.insertArticleHistory(article, sqlInsertNewRevision);
        }
        return true;
    }

    public boolean insert(Article article) {
        return dbUtil.insertEntity(article.getSite(), siteDAOJDBC.getSqlInsert(), siteDAOJDBC.getSqlFetchByEntity(), siteDAOJDBC.getSqlFetchById()) &&
                dbUtil.insertEntity(article.getCategory(), categoryDAOJDBC.getSqlInsert(), categoryDAOJDBC.getSqlFetchByEntity(), categoryDAOJDBC.getSqlFetchById()) &&
                dbUtil.insertEntity(article.getTitle(), titleDAOJDBC.getSqlInsert(), titleDAOJDBC.getSqlFetchByEntity(), titleDAOJDBC.getSqlFetchById()) &&
                dbUtil.insertEntity(article.getContent(), contentDAOJDBC.getSqlInsert(), contentDAOJDBC.getSqlFetchByEntity(), contentDAOJDBC.getSqlFetchById()) &&
                dbUtil.insertEntity(article, sqlInsert, sqlFetchByEntity, sqlFetchById) &&
                dbUtil.insertArticleHistory(article, sqlInsertNewRevision);
    }

    @Override
    public long getInsertedEntityId(Article article) {
        return dbUtil.getInsertedEntityID(article);
    }

    public boolean insert(Set<Article> articleSet){
        boolean result = true;
        for (Article c : articleSet){
            if (!insert(c)){
                result = false;
            }
        }
        return result;
    }

    public boolean delete(Set<Article> articleSet) {
        for (Article c : articleSet){
            if (!delete(c)) return false;
        }
        return true;
    }

    public boolean delete(Article entity) {
        return dbUtil.deleteEntityByEntity(sqlDeleteByEntity, entity);
    }

    public boolean deleteById(Long id) {
        return dbUtil.deleteEntityById(sqlFetchById, id);
    }

    public boolean deleteAll() {
        return dbUtil.deleteAllEntity(sqlDeleteAll);
    }

    @Override
    public Article getEntityRevision(Article entity, int revision) {
        return (Article) dbUtil.getEntityRevision(entity, sqlFetchArticleRevision, revision);
    }

    public String getSqlGetAll() {
        return sqlGetAll;
    }
    public String getSqlFetchById() {
        return sqlFetchById;
    }
    public String getSqlFetchByEntity() {
        return sqlFetchByEntity;
    }
    public String getSqlInsert() {
        return sqlInsert;
    }
    public String getSqlDeleteAll() {
        return sqlDeleteAll;
    }
    public String getSqlDeleteByID() {
        return sqlDeleteByID;
    }
    public String getSqlDeleteByEntity() {
        return sqlDeleteByEntity;
    }
    public String getSqlInertNewRevision() {
        return sqlInsertNewRevision;
    }
    public String getSqlUpdateArticle() {
        return sqlUpdateArticle;
    }
    public String getSqlFetchArticleRevision() {
        return sqlFetchArticleRevision;
    }
    public String getSqlFetchAllArticleRevisions() {
        return sqlFetchAllArticleRevisions;
    }
}
