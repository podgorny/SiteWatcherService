package it.devchallange.podgorny.dao.jdbc.impl;

import it.devchallange.podgorny.dao.jdbc.JDBCDAOService;
import it.devchallange.podgorny.dao.jdbc.utils.DBUtil;
import it.devchallange.podgorny.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

public class CategoryDAOJDBCImpl implements JDBCDAOService<Category> {

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
        sqlGetAll = sqlGetAll.replace("$TABLE_NAME", "CATEGORY");
        sqlFetchById = sqlFetchById.replace("$TABLE_NAME", "CATEGORY");
        sqlFetchByEntity = sqlFetchByEntity.replace("$TABLE_NAME", "CATEGORY");
        sqlInsert = sqlInsert.replace("$TABLE_NAME", "CATEGORY");
        sqlDeleteAll = sqlDeleteAll.replace("$TABLE_NAME", "CATEGORY");
        sqlDeleteByID = sqlDeleteByID.replace("$TABLE_NAME", "CATEGORY");
        sqlDeleteByEntity = sqlDeleteByEntity.replace("$TABLE_NAME", "CATEGORY");
    }
    public Category getEntityById(Long id) {
        return (Category) dbUtil.getEntityByID(DBUtil.entityType.CATEGORY, sqlFetchById, id);
    }
    public Category getEntityByEntity(Category category) {
       return (Category) dbUtil.getByEntity(category, sqlFetchByEntity, sqlFetchById);
    }

    public Set<Category> getAll(){
        return (Set<Category>) dbUtil.getAllEntity(DBUtil.entityType.CATEGORY, sqlGetAll);
    }

    @Override
    public List<Category> getAllEntityRevision(Category category) {
        return null;
    }

    public boolean updateById(Long id, Category category) {
        return false;
    }

    public boolean insert(Category category){
        return dbUtil.insertEntity(category, sqlInsert, sqlFetchByEntity, sqlFetchById);
    }

    @Override
    public long getInsertedEntityId(Category category) {
        return dbUtil.getInsertedEntityID(category);
    }

    public boolean insert(Set<Category> categorySet){
        boolean result = true;
        for (Category c : categorySet){
            if (!insert(c)){
                result = false;
            }
        }
        return result;
    }

    public boolean delete(Set<Category> categorySet) {
        for (Category c : categorySet){
            if (!delete(c)) return false;
        }
        return true;
    }

    public boolean delete(Category entity) {
        return dbUtil.deleteEntityByEntity(sqlDeleteByEntity, entity);
    }

    public boolean deleteById(Long id) {
        return dbUtil.deleteEntityById(sqlFetchById, id);
    }

    public boolean deleteAll() {
        return dbUtil.deleteAllEntity(sqlDeleteAll);
    }

    @Override
    public Category getEntityRevision(Category entity, int revision) {
        return null;
    }

    public String getSqlDeleteByEntity() {
        return sqlDeleteByEntity;
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
}
