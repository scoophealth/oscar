package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.DesAnnualReviewPlan;
import org.springframework.stereotype.Repository;

@Repository
public class DesAnnualReviewPlanDao extends AbstractDao<DesAnnualReviewPlan>{

	public DesAnnualReviewPlanDao() {
		super(DesAnnualReviewPlan.class);
	}

	public DesAnnualReviewPlan search(Integer formNo, Integer demographicNo) {

	    	String sqlCommand = "select x from DesAnnualReviewPlan x where x.formNo <= ? and x.demographicNo=? order by x.formNo DESC, x.desDate DESC, x.desTime DESC";

	        Query query = entityManager.createQuery(sqlCommand);
	        query.setParameter(1, formNo);
	        query.setParameter(2, demographicNo);

	        @SuppressWarnings("unchecked")
	        List<DesAnnualReviewPlan> results = query.getResultList();

	        if(results.size()>0 ){
	        	return results.get(0);
	        }
	        return null;
	    }
}
