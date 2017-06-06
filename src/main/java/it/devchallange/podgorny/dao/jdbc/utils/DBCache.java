package it.devchallange.podgorny.dao.jdbc.utils;

import it.devchallange.podgorny.entity.Entity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DBCache {
    private static final Logger logger = Logger.getLogger(DBCache.class);

    @Autowired
    @Qualifier("cacheEnabled")
    private boolean isEnabled;
    private Map<Entity, Long> entityMap;

    public DBCache() {
        entityMap = new HashMap<>();
    }

    public long getFromCache(Entity entity) throws CacheNotEnabledException {
        if (isEnabled){
            return entityMap.getOrDefault(entity, 0l);
        } else{
            throw new CacheNotEnabledException();
        }
    };

    public <T extends Entity> void insertToCache(Set<T> entitySet) throws CacheNotEnabledException {
        if(isEnabled) {
            for (Entity e : entitySet) {
                if (e.getId() > 0) {
                    entityMap.put(e, e.getId());
                }
            }
        } else{
            throw new CacheNotEnabledException();
        }
    }

    public <T extends  Entity> void insertToCache(T entity, PreparedStatement preparedStatement) throws CacheNotEnabledException {
        if(isEnabled) {
            if (preparedStatement == null && entity.getId() > 0) {
                entityMap.put(entity, entity.getId());
            } else {
                try {
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            entity.setId(generatedKeys.getLong(1));
                            if (logger.isTraceEnabled()) {
                                logger.trace("Insert to Cache " + entity);
                            }
                            entityMap.put(entity, entity.getId());
                        } else {
                            logger.error("No ID obtained for " + entity);
                        }
                    }
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        } else {
            throw new CacheNotEnabledException();
        }
    }

    public void enableCache(){
        if (!isEnabled){
            isEnabled = true;
            entityMap = new HashMap<>();
        }
    }

    public void disableCache(){
        if (isEnabled){
            isEnabled = false;
            entityMap.clear();
        }
    }

    public void insertToCache(Entity entity, long id) throws CacheNotEnabledException {
        if(isEnabled) {
            entityMap.put(entity, id);
        } else {
            throw new CacheNotEnabledException();
        }
    }
}
