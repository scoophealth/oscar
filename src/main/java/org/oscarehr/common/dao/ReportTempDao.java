package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.ReportTemp;
import org.springframework.stereotype.Repository;

@Repository
public class ReportTempDao extends AbstractDao<ReportTemp>{

	public ReportTempDao() {
		super(ReportTemp.class);
	}

	   public List<ReportTemp> findAll() {
	    	String sql = "select x from ReportTemp x";
	    	Query query = entityManager.createQuery(sql);

	        @SuppressWarnings("unchecked")
	        List<ReportTemp> results = query.getResultList();
	        return results;
	    }
}
