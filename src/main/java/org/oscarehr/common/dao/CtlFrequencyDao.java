package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.CtlFrequency;
import org.springframework.stereotype.Repository;

@Repository
public class CtlFrequencyDao extends AbstractDao<CtlFrequency>{

	public CtlFrequencyDao() {
		super(CtlFrequency.class);
	}

	public List<CtlFrequency> findAll() {
	  	String sql = "select x from CtlFrequency x";
    	Query query = entityManager.createQuery(sql);

        @SuppressWarnings("unchecked")
        List<CtlFrequency> results = query.getResultList();
        return results;
	}

}
