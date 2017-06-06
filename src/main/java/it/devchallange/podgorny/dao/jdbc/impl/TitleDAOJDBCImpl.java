package it.devchallange.podgorny.dao.jdbc.impl;

import it.devchallange.podgorny.dao.jdbc.JDBCDAOService;
import it.devchallange.podgorny.dao.jdbc.utils.DBUtil;
import it.devchallange.podgorny.entity.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

public class TitleDAOJDBCImpl implements JDBCDAOService<Title> {
    @Autowired
    private DBUtil dbUtil;
    @Autowired
    @Qualifier("sqlGetAll")
    private String sqlGetAll;
    @Autowired
    @Qualifier("sqlFetchById")
    private String sqlFetchById;
    @Autowired
    @Qualifier("sqlFetchByEntity")
    private String sqlFetchByEntity;
    @Autowired
    @Qualifier("sqlInsert")
    private String sqlInsert;
    @Autowired
    @Qualifier("sqlDeleteAll")
    private String sqlDeleteAll;
    @Autowired
    @Qualifier("sqlDeleteByID")
    private String sqlDeleteByID;
    @Autowired
    @Qualifier("sqlDeleteByEntity")
    private String sqlDeleteByEntity;

    @PostConstruct
    private void init() {
        sqlGetAll = sqlGetAll.replace("$TABLE_NAME", "TITLE");
        sqlFetchById = sqlFetchById.replace("$TABLE_NAME", "TITLE");
        sqlFetchByEntity = sqlFetchByEntity.replace("$TABLE_NAME", "TITLE");
        sqlInsert = sqlInsert.replace("$TABLE_NAME", "TITLE");
        sqlDeleteAll = sqlDeleteAll.replace("$TABLE_NAME", "TITLE");
        sqlDeleteByID = sqlDeleteByID.replace("$TABLE_NAME", "TITLE");
        sqlDeleteByEntity = sqlDeleteByEntity.replace("$TABLE_NAME", "TITLE");
    }

    public Title getEntityById(Long id) {
        return (Title) dbUtil.getEntityByID(DBUtil.entityType.TITLE, sqlFetchById, id);
    }

    public Title getEntityByEntity(Title title) {
       return (Title) dbUtil.getByEntity(title, sqlFetchByEntity, sqlFetchById);
    }

    public Set<Title> getAll(){
        return (Set<Title>) dbUtil.getAllEntity(DBUtil.entityType.TITLE, sqlGetAll);
    }

    @Override
    public List<Title> getAllEntityRevision(Title title) {
        return null;
    }

    public boolean updateById(Long id, Title title) {
        return false;
    }

    public boolean insert(Title title) {
        return dbUtil.insertEntity(title, sqlInsert, sqlFetchByEntity, sqlFetchById);
    }

    @Override
    public long getInsertedEntityId(Title title) {
        return dbUtil.getInsertedEntityID(title);
    }

    public boolean insert(Set<Title> titleSet){
        boolean result = true;
        for (Title c : titleSet){
            if (!insert(c)){
                result = false;
            }
        }
        return result;
    }

    public boolean delete(Set<Title> titleSet) {
        for (Title c : titleSet){
            if (!delete(c)) return false;
        }
        return true;
    }

    public boolean delete(Title entity) {
        return dbUtil.deleteEntityByEntity(sqlDeleteByEntity, entity);
    }

    public boolean deleteById(Long id) {
        return dbUtil.deleteEntityById(sqlFetchById, id);
    }

    public boolean deleteAll() {
        return dbUtil.deleteAllEntity(sqlDeleteAll);
    }

    @Override
    public Title getEntityRevision(Title entity, int revision) {
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
