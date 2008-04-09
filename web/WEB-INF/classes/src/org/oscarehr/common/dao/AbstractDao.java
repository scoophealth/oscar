package org.oscarehr.common.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

@Transactional
abstract class AbstractDao {
    
    @PersistenceContext
    protected EntityManager entityManager = null;

    /**
     * aka update
     */
    public void merge(Object o) {
        entityManager.merge(o);
    }

    /**
     * aka create
     */
    public void persist(Object o) {
        entityManager.persist(o);
    }

    /**
     * You can only remove attached instances.
     */
    public void remove(Object o) {
        entityManager.remove(o);
    }

    /**
     * You can only refresh attached instances.
     */
    public void refresh(Object o) {
        entityManager.refresh(o);
    }

}
