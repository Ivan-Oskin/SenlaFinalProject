package com.oskin.ad_board.repository;

import com.oskin.ad_board.model.IIdentified;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


public abstract class AbstractCrudRepository<T extends IIdentified> implements CrudRepository<T> {
    private final Class<T> entityClass;
    private final Logger log;
    @PersistenceContext
    private EntityManager entityManager;

    public AbstractCrudRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.log = LoggerFactory.getLogger(getClass());
    }

    @Override
    public boolean create(T object) {
        try {
            log.info("start save");
            entityManager.persist(object);
            log.info("successful save");
            return true;
        } catch (Exception e) {
            log.error("error save, exception => {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<T> findById(int id) {
        try {
            log.info("start findById, id: {}", id);
            T object = entityManager.find(entityClass, id);
            log.info("successful findById, id: {}", id);
            return Optional.ofNullable(object);
        } catch (Exception e) {
            log.error("error findById, exception => {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            Optional<T> optional = findById(id);
            if (optional.isPresent()) {
                T object = optional.get();
                log.info("start delete id: {}", id);
                entityManager.remove(object);
                log.info("successful delete, id: {}", id);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("error delete byId, exception => {}", e.getMessage());
            return false;
        }
    }

    public boolean delete(T object) {
        try {
            log.info("start delete object");
            entityManager.remove(object);
            log.info("successful delete object");
                return true;
        } catch (Exception e) {
            log.error("error delete object, exception => {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(T object) {
        try {
            log.info("start update");
            entityManager.merge(object);
            log.info("successful update");
            return true;
        } catch (Exception e) {
            log.error("error update exception => {}", e.getMessage());
            return false;
        }
    }

    public T createAndGet(T object) {
        try {
            log.info("start saveAndGet");
            entityManager.persist(object);
            entityManager.flush();
            log.info("successful saveAndGet");
            return object;
        } catch (Exception e) {
            log.error("error saveAndGet, exception => {}", e.getMessage());
            return null;
        }
    }
}
