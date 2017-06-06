package it.devchallange.podgorny.dao.jdbc.impl;

import it.devchallange.podgorny.dao.jdbc.JDBCDAOService;
import it.devchallange.podgorny.dao.jdbc.utils.DBUtil;
import it.devchallange.podgorny.entity.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

public class SiteDAOJDBCImpl implements JDBCDAOService<Site> {
    @Autowired
    private DBUtil dbUtil;
    @Autowired
    @Qualifier("sqlGetAll")
    private String sqlGetAll;
    @Autowired
    @Qualifier("sqlFetchById")
    private String sqlFetchById;
    @Autowired
    @Qualifier("sqlFetchBySite")
    private String sqlFetchByEntity;
    @Autowired
    @Qualifier("sqlInsertSite")
    private String sqlInsert;
    @Autowired
    @Qualifier("sqlDeleteAll")
    private String sqlDeleteAll;
    @Autowired
    @Qualifier("sqlDeleteByID")
    private String sqlDeleteByID;
    @Autowired
    @Qualifier("sqlDeleteBySite")
    private String sqlDeleteByEntity;

    @PostConstruct
    private void init() {
        sqlGetAll = sqlGetAll.replace("$TABLE_NAME", "SITE");
        sqlFetchById = sqlFetchById.replace("$TABLE_NAME", "SITE");
        sqlFetchByEntity = sqlFetchByEntity.replace("$TABLE_NAME", "SITE");
        sqlInsert = sqlInsert.replace("$TABLE_NAME", "SITE");
        sqlDeleteAll = sqlDeleteAll.replace("$TABLE_NAME", "SITE");
        sqlDeleteByID = sqlDeleteByID.replace("$TABLE_NAME", "SITE");
        sqlDeleteByEntity = sqlDeleteByEntity.replace("$TABLE_NAME", "SITE");
    }

    public Site getEntityById(Long id) {
        return (Site) dbUtil.getEntityByID(DBUtil.entityType.SITE, sqlFetchById, id);
    }

    public Site getEntityByEntity(Site site) {
       return (Site) dbUtil.getByEntity(site, sqlFetchByEntity, sqlFetchById);
    }

    public Set<Site> getAll(){
        return (Set<Site>) dbUtil.getAllEntity(DBUtil.entityType.SITE, sqlGetAll);
    }

    @Override
    public List<Site> getAllEntityRevision(Site site) {
        return null;
    }

    public boolean updateById(Long id, Site site) {
        return false;
    }

    public boolean insert(Site site) {
        return dbUtil.insertEntity(site, sqlInsert, sqlFetchByEntity, sqlFetchById);
    }

    @Override
    public long getInsertedEntityId(Site site) {
        return dbUtil.getInsertedEntityID(site);
    }

    public boolean insert(Set<Site> siteSet){
        boolean result = true;
        for (Site c : siteSet){
            if (!insert(c)){
                result = false;
            }
        }
        return result;
    }

    public boolean delete(Set<Site> siteSet) {
        for (Site c : siteSet){
            if (!delete(c)) return false;
        }
        return true;
    }

    public boolean delete(Site entity) {
        return dbUtil.deleteEntityByEntity(sqlDeleteByEntity, entity);
    }

    public boolean deleteById(Long id) {
        return dbUtil.deleteEntityById(sqlFetchById, id);
    }

    public boolean deleteAll() {
        return dbUtil.deleteAllEntity(sqlDeleteAll);
    }

    @Override
    public Site getEntityRevision(Site entity, int revision) {
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
