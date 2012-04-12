package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.MeasurementGroupStyle;
import org.springframework.stereotype.Repository;

@Repository
public class MeasurementGroupStyleDao extends AbstractDao<MeasurementGroupStyle>{

	public MeasurementGroupStyleDao() {
		super(MeasurementGroupStyle.class);
	}

	   public List<MeasurementGroupStyle> findAll() {
	    	String sql = "select x from MeasurementGroupStyle x";
	    	Query query = entityManager.createQuery(sql);

	        @SuppressWarnings("unchecked")
	        List<MeasurementGroupStyle> results = query.getResultList();
	        return results;
	    }
}
