package it.devchallange.podgorny.dao.jdbc.impl;

import it.devchallange.podgorny.dao.jdbc.JDBCDAOService;
import it.devchallange.podgorny.dao.jdbc.utils.DBUtil;
import it.devchallange.podgorny.entity.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

public class ContentDAOJDBCImpl implements JDBCDAOService<Content> {
    @Autowired
    private DBUtil dbUtil;
    @Autowired
    @Qualifier("sqlGetAll")
    private String sqlGetAll;
    @Autowired
    @Qualifier("sqlFetchById")
    private String sqlFetchById;
    @Autowired
    @Qualifier("sqlFetchByEntityContent")
    private String sqlFetchByEntity;
    @Autowired
    @Qualifier("sqlInsertContent")
    private String sqlInsert;
    @Autowired
    @Qualifier("sqlDeleteAll")
    private String sqlDeleteAll;
    @Autowired
    @Qualifier("sqlDeleteByID")
    private String sqlDeleteByID;
    @Autowired
    @Qualifier("sqlDeleteByEntityContent")
    private String sqlDeleteByEntity;

    @PostConstruct
    private void init() {
        sqlGetAll = sqlGetAll.replace("$TABLE_NAME", "CONTENT");
        sqlFetchById = sqlFetchById.replace("$TABLE_NAME", "CONTENT");
        sqlFetchByEntity = sqlFetchByEntity.replace("$TABLE_NAME", "CONTENT");
        sqlInsert = sqlInsert.replace("$TABLE_NAME", "CONTENT");
        sqlDeleteAll = sqlDeleteAll.replace("$TABLE_NAME", "CONTENT");
        sqlDeleteByID = sqlDeleteByID.replace("$TABLE_NAME", "CONTENT");
        sqlDeleteByEntity = sqlDeleteByEntity.replace("$TABLE_NAME", "CONTENT");
    }

    public Content getEntityById(Long id) {
       return (Content) dbUtil.getEntityByID(DBUtil.entityType.CONTENT, sqlFetchById, id);
    }

    public Content getEntityByEntity(final Content content) {
       return (Content) dbUtil.getByEntity(content, sqlFetchByEntity, sqlFetchById);
    }

    public Set<Content> getAll() {
        return (Set<Content>) dbUtil.getAllEntity(DBUtil.entityType.CONTENT, sqlGetAll);
    }

    @Override
    public List<Content> getAllEntityRevision(Content content) {
        return null;
    }

    public boolean updateById(Long id, Content content) {
        return false;
    }

    public boolean insert(Content content) {
        return dbUtil.insertEntity(content, sqlInsert, sqlFetchByEntity, sqlFetchById);
    }

    @Override
    public long getInsertedEntityId(Content content) {
        return dbUtil.getInsertedEntityID(content);
    }

    public boolean insert(Set<Content> contentSet){
        boolean result = true;
        for (Content c : contentSet){
            if (!insert(c)){
                result = false;
                break;
            }
        }
        return result;
    }

    @Override
    public boolean delete(Set<Content> contentSet) {
        for (Content c: contentSet){
            if (!delete(c)) return false;
        }
        return true;
    }

    @Override
    public boolean delete(Content content) {
        return dbUtil.deleteEntityByEntity(sqlDeleteByEntity, content);
    }

    public boolean deleteById(Long id) {
        return dbUtil.deleteEntityById(sqlFetchById, id);
    }

    public boolean deleteAll() {
        return dbUtil.deleteAllEntity(sqlDeleteAll);
    }

    @Override
    public Content getEntityRevision(Content entity, int revision) {
        return null;
    }

    public String getSqlDeleteAll() {
        return sqlDeleteAll;
    }

    public String getSqlDeleteByID() {
        return sqlDeleteByID;
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

    public String getSqlDeleteByEntity() {
        return sqlDeleteByEntity;
    }

}
