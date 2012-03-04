package org.oscarehr.common.dao;

import org.oscarehr.common.model.DesAnnualReviewPlan;
import org.springframework.stereotype.Repository;

@Repository
public class DesAnnualReviewPlanDao extends AbstractDao<DesAnnualReviewPlan>{

	public DesAnnualReviewPlanDao() {
		super(DesAnnualReviewPlan.class);
	}
}
