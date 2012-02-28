package org.oscarehr.common.dao;

import org.oscarehr.common.model.BatchEligibility;
import org.springframework.stereotype.Repository;

@Repository
public class BatchEligibilityDao extends AbstractDao<BatchEligibility> {

	public BatchEligibilityDao() {
		super(BatchEligibility.class);
	}

}
