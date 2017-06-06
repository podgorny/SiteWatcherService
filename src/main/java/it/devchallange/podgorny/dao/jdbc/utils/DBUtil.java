package it.devchallange.podgorny.dao.jdbc.utils;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;
import it.devchallange.podgorny.entity.Article;
import it.devchallange.podgorny.entity.Category;
import it.devchallange.podgorny.entity.Content;
import it.devchallange.podgorny.entity.Entity;
import it.devchallange.podgorny.entity.Site;
import it.devchallange.podgorny.entity.Title;
import it.devchallange.podgorny.utils.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.sql.*;
import java.util.*;

@Controller
public class DBUtil {
    private static final Logger logger = Logger.getLogger(DBUtil.class);

    @Autowired
    @Qualifier("rootDS")
    private BoneCPDataSource rootDS;
    @Autowired
    @Qualifier("regularDS")
    private BoneCPDataSource regularDS;
    @Autowired
    private DBCache dbCache;
    private static boolean isDBInited = false;
    private @Resource(name = "initSQL") List<String> initSQL;
    private static Map<Entity, Long> entityIDs = new HashMap<>();

    public enum entityType {
        CATEGORY,
        ARTICLE,
        CONTENT,
        TITLE,
        SITE
    };


    public List<Article> getAllEntityRevisions(Entity entity, String sqlFetchEntityRevisions) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(regularDS);
        logger.info(((Article)entity).getURL());
        if (entity instanceof Article) {
            return jdbcTemplate.query(sqlFetchEntityRevisions, (resultSet, i) -> new Article(
                    resultSet.getLong(1), // article ID
                    new Site(resultSet.getLong(2), //site ID
                             resultSet.getString(3), //site NAME
                             resultSet.getString(4)), // site URL
                    resultSet.getString(5), // artile URL
                    new Category(resultSet.getLong(6), //  category ID
                            resultSet.getString(7)), // category NAME
                    new Title(resultSet.getLong(8), // title ID
                            resultSet.getString(9)), // title NAME
                    new Content(resultSet.getLong(10), // content ID
                            resultSet.getString(11), // content DATA
                            resultSet.getString(12), // content SHA256
                            resultSet.getString(13)), //content MD5
                    (long) resultSet.getRow(), // revision
                    resultSet.getDate(14)),
                    ((Article)entity).getURL()); // revision Date, ((Article)entity).getURL());
        }

        return null;
    }

    public Entity getEntityRevision(Entity entity, String sqlFetchArticleRevision, int revision) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(regularDS);
        if (entity instanceof Article){
            Article article = (Article) entity;
            return jdbcTemplate.queryForObject(sqlFetchArticleRevision, new Object[]{revision, article.getURL(), revision-1}, (resultSet, i) -> new Article(
                    resultSet.getLong(1), // article ID
                    new Site(resultSet.getLong(2), //site ID
                            resultSet.getString(3), //site NAME
                            resultSet.getString(4)), // site URL
                    resultSet.getString(5), // artile URL
                    new Category(resultSet.getLong(6), //  category ID
                            resultSet.getString(7)), // category NAME
                    new Title(resultSet.getLong(8), // title ID
                            resultSet.getString(9)), // title NAME
                    new Content(resultSet.getLong(10), // content ID
                            resultSet.getString(11), // content DATA
                            resultSet.getString(12), // content SHA256
                            resultSet.getString(13)), //content MD5
                    resultSet.getLong(14), // revision
                    resultSet.getDate(15))); // revision Date
        }
        return null;
    }

    public Entity getEntityByID(entityType entityType, String sqlFetchById, long id){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(regularDS);
        try {
            switch (entityType) {
                case SITE:
                    return jdbcTemplate.queryForObject(sqlFetchById, new Object[]{id}, (resultSet, i) -> new Site(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3)));
                case TITLE:
                    return jdbcTemplate.queryForObject(sqlFetchById, new Object[]{id}, (resultSet, i) -> new Title(resultSet.getLong(1), resultSet.getString(2)));
                case CATEGORY:
                    return jdbcTemplate.queryForObject(sqlFetchById, new Object[]{id}, (resultSet, i) -> new Category(resultSet.getLong(1), resultSet.getString(2)));
                case CONTENT:
                    return jdbcTemplate.queryForObject(sqlFetchById, new Object[]{id}, (resultSet, i) -> new Content(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4)));
                case ARTICLE:
                    return jdbcTemplate.queryForObject(sqlFetchById, new Object[]{id}, (resultSet, i) -> new Article(
                            resultSet.getLong(1), // article ID
                            new Site(resultSet.getLong(2), //site ID
                                    resultSet.getString(3), //site NAME
                                    resultSet.getString(4)), // site URL
                            resultSet.getString(5), // artile URL
                            new Category(resultSet.getLong(6), //  category ID
                                    resultSet.getString(7)), // category NAME
                            new Title(resultSet.getLong(8), // title ID
                                    resultSet.getString(9)), // title NAME
                            new Content(resultSet.getLong(10), // content ID
                                    resultSet.getString(11), // content DATA
                                    resultSet.getString(12), // content SHA256
                                    resultSet.getString(13)), //content MD5
                            resultSet.getLong(14), // revision
                            resultSet.getDate(15)) // revision Date

                    );

                default:
                    return null;
            }
        } catch (EmptyResultDataAccessException e){
            if (logger.isDebugEnabled()){
                logger.debug(e);
            }
            return null;
        }
    }

    public boolean updateByID(String sqlUpdate, Long id, Entity entity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(regularDS);
        if (entity instanceof Article){
            Article article = (Article) entity;
            jdbcTemplate.update(sqlUpdate, article.getCategory().getId(),
                                            article.getTitle().getId(),
                                            article.getContent().getId(),
                                            id);
            return true;
        }
        return false;
    }

    public Entity getByEntity(Entity entity, String sqlFetchByEntity, String sqlFetchByID) {
        Entity entityFromDB = null;
        JdbcTemplate jdbcTemplate;
        long id = 0;

        try{
            id = dbCache.getFromCache(entity);
        } catch (CacheNotEnabledException e) {
            if (logger.isTraceEnabled()){
                logger.trace(e);
            }
        }
        if (id > 0){
            logger.info("Readed from cache " + id);
            if (entity instanceof Title) entityFromDB = getEntityByID(entityType.TITLE, sqlFetchByID, id);
            else if (entity instanceof Content) entityFromDB = getEntityByID(entityType.CONTENT, sqlFetchByID, id);
            else if (entity instanceof Category) entityFromDB = getEntityByID(entityType.CATEGORY, sqlFetchByID, id);
            else if (entity instanceof Article) entityFromDB = getEntityByID(entityType.ARTICLE, sqlFetchByID, id);
            else if (entity instanceof Site) entityFromDB = getEntityByID(entityType.SITE, sqlFetchByID, id);
        } else {
            jdbcTemplate = new JdbcTemplate(regularDS);
            entityType entityType = null;
            if (entity instanceof Title) entityType = DBUtil.entityType.TITLE;
            else if (entity instanceof Category) entityType = DBUtil.entityType.CATEGORY;
            else if (entity instanceof Content) entityType = DBUtil.entityType.CONTENT;
            else if (entity instanceof Article) entityType = DBUtil.entityType.ARTICLE;
            else if (entity instanceof Site) entityType = DBUtil.entityType.SITE;

            try {
                switch (entityType) {
                    case SITE:
                        Site site = (Site) entity;
                        logger.info("GET BY ENTITY: " + site.getURLString());
                        entityFromDB = jdbcTemplate.queryForObject(sqlFetchByEntity, (resultSet, i) ->
                                new Site(resultSet.getLong(1),
                                        resultSet.getString(2),
                                        resultSet.getString(3)
                                ), site.getURLString()
                        );
                        break;
                    case TITLE:
                        Title title = (Title) entity;
                        entityFromDB = jdbcTemplate.queryForObject(sqlFetchByEntity, (resultSet, i) ->
                                new Title(resultSet.getLong(1),
                                        resultSet.getString(2)
                                ), title.getName()
                        );
                        break;
                    case CATEGORY:
                        Category category = (Category) entity;
                        entityFromDB = jdbcTemplate.queryForObject(sqlFetchByEntity, (resultSet, i) ->
                                new Category(resultSet.getLong(1),
                                        resultSet.getString(2)
                                ), category.getName()
                        );
                        break;
                    case CONTENT:
                        Content content = (Content) entity;
                        entityFromDB = jdbcTemplate.queryForObject(sqlFetchByEntity, new Object[]{content.getSha256Hex(), content.getMd5Hex()}, (resultSet, i) -> new Content(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4)));
                        break;
                    case ARTICLE:
                        Article article = (Article) entity;
                        entityFromDB = jdbcTemplate.queryForObject(sqlFetchByEntity, (resultSet, i) ->
                                        new Article(resultSet.getLong(1), // article ID
                                                new Site(resultSet.getLong(2), //site ID
                                                        resultSet.getString(3), //site NAME
                                                        resultSet.getString(4)), // site URL
                                                resultSet.getString(5), // artile URL
                                                new Category(resultSet.getLong(6), //  category ID
                                                        resultSet.getString(7)), // category NAME
                                                new Title(resultSet.getLong(8), // title ID
                                                        resultSet.getString(9)), // title NAME
                                                new Content(resultSet.getLong(10), // content ID
                                                        resultSet.getString(11), // content DATA
                                                        resultSet.getString(12), // content SHA256
                                                        resultSet.getString(13)), //content MD5
                                                resultSet.getLong(14), // revision
                                                resultSet.getDate(15)),
                                article.getURL()// revision Date, article.getURL()
                        );
                        logger.info("REVISION : " + article.getURL() + " / " + ((Article)entityFromDB).getCurrentRevision());
                        break;
                }
            } catch (EmptyResultDataAccessException e){
                if (logger.isDebugEnabled()){
                    logger.debug(e);
                }
                return null;
            }
        }
        if (logger.isTraceEnabled()) logger.trace("RETURNING: "  + entityFromDB);
        return entityFromDB;
    }
    public long getInsertedEntityID(Entity entity) {
        long id;
        try {
            id = dbCache.getFromCache(entity);
        } catch (CacheNotEnabledException e) {
            if (logger.isDebugEnabled()){
                logger.debug("Cache is disabaled. Getting ID from inserted entity");
            }
            synchronized (entityIDs) {
                id = entityIDs.get(entity);
                entityIDs.clear(); // prevent memory usage
            }
        }
        return id;
    }

    public Set<? extends Entity> getAllEntity(entityType entityType, String sql) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(regularDS);
        List<Entity> entityList = jdbcTemplate.query(sql, (resultSet, i) -> {
            switch (entityType){
                case SITE: return new Site(resultSet.getLong(1),  // ID
                                             resultSet.getString(2), //NAME
                                            resultSet.getString(3)); // URL
                case TITLE: return new Title(resultSet.getLong(1),  // ID
                                              resultSet.getString(2)); // NAME
                case CATEGORY: return new Category(resultSet.getLong(1),  // ID
                                                   resultSet.getString(2)); // NAME
                case CONTENT: return new Content(resultSet.getLong(1),  // ID
                        resultSet.getString(2),  // DATA (real text)
                        resultSet.getString(3), // SHA256HEX
                        resultSet.getString(4) // MD5HEX
                );
                case ARTICLE: return  new Article(resultSet.getLong(1), // article ID
                        new Site(resultSet.getLong(2), //site ID
                                resultSet.getString(3), //site NAME
                                resultSet.getString(4)), // site URL
                        resultSet.getString(5), // artile URL
                        new Category(resultSet.getLong(6), //  category ID
                                resultSet.getString(7)), // category NAME
                        new Title(resultSet.getLong(8), // title ID
                                resultSet.getString(9)), // title NAME
                        new Content(resultSet.getLong(10), // content ID
                                resultSet.getString(11), // content DATA
                                resultSet.getString(12), // content SHA256
                                resultSet.getString(13)), //content MD5
                        resultSet.getLong(14), // revision
                        resultSet.getDate(15));
                default: return null;
            }
        });
        Set<Entity> entitySet = new HashSet<>(entityList);
        insertIntoCache(entitySet);
        return entitySet;
    }

    public synchronized boolean insertEntity(Entity entity, String sqlInsert, String sqlFetchByEntity, String sqlFetchByID) {
        Throwable exception = null;
        JdbcTemplate jdbcTemplate = new JdbcTemplate(regularDS);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        boolean entityTypeDefined = false;
        try {
            if (!entityTypeDefined && entity instanceof Site) {
                entityTypeDefined = true;
                Site site = (Site) entity;

                if (logger.isDebugEnabled()) logger.debug("Inserting Site [" + sqlInsert + "]");
                jdbcTemplate.update(connection -> {
                            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert, new int[]{1});
                            if (logger.isDebugEnabled()) {
                                logger.debug("SQL PARAM 1 :" + site.getName());
                                logger.debug("SQL PARAM 2 :" + site.getURLString());
                            }
                            preparedStatement.setString(1, site.getName());
                            preparedStatement.setString(2, site.getURLString());
                            return preparedStatement;
                        },
                        keyHolder);
                if (logger.isDebugEnabled()) logger.debug("INSERTED " + site);
            }
            if (!entityTypeDefined && entity instanceof Title) {
                entityTypeDefined = true;
                Title title = ((Title) entity);

                if (logger.isDebugEnabled()) logger.debug("Inserting Title [" + sqlInsert + "]");
                jdbcTemplate.update(connection -> {
                            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert, new int[]{1});
                            if (logger.isDebugEnabled()) logger.debug("SQL PARAM 1 :" + title.getName());
                            preparedStatement.setString(1, title.getName());
                            return preparedStatement;
                        },
                        keyHolder);
            }
            if (!entityTypeDefined && entity instanceof Category) {
                entityTypeDefined = true;
                Category category = ((Category) entity);

                if (logger.isDebugEnabled()) logger.debug("Inserting Category [" + sqlInsert + "]");
                jdbcTemplate.update(connection -> {
                            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert, new int[]{1});
                            if (logger.isDebugEnabled()) logger.debug("SQL PARAM 1 :" + category.getName());
                            preparedStatement.setString(1, category.getName());
                            return preparedStatement;
                        },
                        keyHolder);
            }
            if (!entityTypeDefined && entity instanceof Content) {
                entityTypeDefined = true;
                Content content = ((Content) entity);
                String data = content.getData().length() > 20 ? content.getData().substring(0, 20) + "..." : content.getData();

                if (logger.isDebugEnabled()) logger.debug("Inserting Content [" + sqlInsert + "]");
                jdbcTemplate.update(connection -> {
                            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert, new int[]{1, 2, 3});
                            if (logger.isDebugEnabled()) {
                                logger.debug("SQL PARAM 1 (trimmed) :" + data);
                                logger.debug("SQL PARAM 2 :" + content.getSha256Hex());
                                logger.debug("SQL PARAM 3 :" + content.getMd5Hex());
                            }
                            preparedStatement.setString(1, content.getData());
                            preparedStatement.setString(2, content.getSha256Hex());
                            preparedStatement.setString(3, content.getMd5Hex());
                            return preparedStatement;
                        },
                        keyHolder);
            }
            if (!entityTypeDefined && entity instanceof Article){
                Article article = (Article) entity;
                jdbcTemplate.update(connection -> {
                            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert, new int[]{1, 2, 3, 4, 5});
                            if (logger.isDebugEnabled()) {
                                logger.debug("SQL PARAM 1 :" + article.getSite().getId());
                                logger.debug("SQL PARAM 2 :" + article.getURL());
                                logger.debug("SQL PARAM 3 :" + article.getCategory().getId());
                                logger.debug("SQL PARAM 4 :" + article.getTitle().getId());
                                logger.debug("SQL PARAM 5 :" + article.getContent().getId());
                            }

                            preparedStatement.setLong(1, article.getSite().getId());
                            preparedStatement.setString(2, article.getURL());
                            preparedStatement.setLong(3, article.getCategory().getId());
                            preparedStatement.setLong(4, article.getTitle().getId());
                            preparedStatement.setLong(5, article.getContent().getId());
                            return preparedStatement;
                        },
                        keyHolder);
            }
            if (logger.isDebugEnabled()) logger.debug("INSERTED");
        } catch (DataAccessException e) {
//             check if record already exists in DB
            if (!e.getRootCause().getClass().getCanonicalName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                logger.error(entity.getClass() + "\n" + e);
                throw e;
            }
            exception = e.getRootCause();
        }
        // keyHolder may not contains keys when record already exists in DB
        if (exception instanceof SQLIntegrityConstraintViolationException){
            Entity e = getByEntity(entity, sqlFetchByEntity, sqlFetchByID);
            insertIntoCache(e, e.getId());
            entityIDs.put(e, e.getId());
            entity.setId(e. getId());
            if (logger.isTraceEnabled()) logger.trace("RECEIVED FROM DB: " + e.getClass().getCanonicalName() + ", ID: " + e.getId());
        } else{
            if (keyHolder.getKey() == null){
                return false;
            }
            long id = Long.valueOf(keyHolder.getKey().toString());
            insertIntoCache(entity, id);
            entityIDs.put(entity, id);
            entity.setId(id);
        }
        return true;
    }

    public boolean insertArticleHistory(Article article, String sqlInsertNewRevision) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(regularDS);
        try {
            jdbcTemplate.update(sqlInsertNewRevision,
                    article.getId(),
                    article.getCategory().getId(),
                    article.getTitle().getId(),
                    article.getContent().getId());
        } catch (DataAccessException e){
            logger.error(e);
            return false;
        }
        return true;
    }
    public void initDB() throws SQLException{
        JdbcTemplate jdbcTemplate = new JdbcTemplate(rootDS);
        if (!isDBInited) {
            for (String s : initSQL) {
                try {
                    jdbcTemplate.execute(s);
                } catch (DataAccessException e) {
                    if (logger.isDebugEnabled() && (
                            Utils.isMatchedRegexp(e.toString(), ".*error code .(1007|1396|1826)..*")            // 1396 - User exists, 1007 - DB exists, 1826 - duplicate FK
                                    || Utils.isMatchedRegexp(e.toString(), ".*Table '.*' already exists$")            // Table X exists
                                    || Utils.isMatchedRegexp(e.toString(), ".*Duplicate key name 'ind.*(_id')?$")     // Index exists
                                    || Utils.isMatchedRegexp(e.toString(), ".*Can't write; duplicate key in table.*") // Foreign Key exists
                    )) {
                        logger.debug("Already executed: [" + s + "]");
                    } else {
                        throw e;
                    }
                }
            }
            isDBInited = true;
        }
    }

    public boolean deleteEntityById(String sqlDeleteByID, long id){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(regularDS);
        try {
            jdbcTemplate.update(sqlDeleteByID, id);
        }catch (DataAccessException e){
            logger.error(e);
            return false;
        }
        return true;
    }

    public boolean deleteAllEntity(String sqlDeleteAll) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(regularDS);
        try {
            jdbcTemplate.execute(sqlDeleteAll);
        }catch (DataAccessException e){
            logger.error(e);
            return false;
        }
        return true;
    }

    public <T extends Entity> boolean deleteEntityByEntity(String sqlDeleteByEntity, T entity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(regularDS);
        try {
            if (entity instanceof Category) jdbcTemplate.update(sqlDeleteByEntity, ((Category) entity).getName());
            else if (entity instanceof Content) jdbcTemplate.update(sqlDeleteByEntity, ((Content) entity).getSha256Hex(), ((Content) entity).getMd5Hex());
            else if (entity instanceof Title) jdbcTemplate.update(sqlDeleteByEntity, ((Title) entity).getName());
            else if (entity instanceof Article) jdbcTemplate.update(sqlDeleteByEntity, ((Article) entity).getURL());
            else if (entity instanceof Site) jdbcTemplate.update(sqlDeleteByEntity, ((Site) entity).getURLString());
        }catch (DataAccessException e){
            logger.error(e);
            return false;
        }
        return true;
    }

    private void insertIntoCache(Set<Entity> entityList) {
        try {
            dbCache.insertToCache(entityList);
        } catch (CacheNotEnabledException e) {
            if (logger.isTraceEnabled()){
                logger.trace(e);
            }
        }
    }

    private void insertIntoCache(Entity entity, long id) {
        try {
            dbCache.insertToCache(entity, id);
        } catch (CacheNotEnabledException e) {
            if (logger.isTraceEnabled()){
                logger.trace(e);
            }
        }
    }

    public void setLocalsDS(){
        BoneCPConfig rootConfig = rootDS.getConfig(),
                     regularConfig = regularDS.getConfig();
        rootConfig.setJdbcUrl(rootConfig.getJdbcUrl().replace("p_mysql", "localhost"));
        regularConfig.setJdbcUrl(regularConfig.getJdbcUrl().replace("p_mysql", "localhost"));
    }
}
