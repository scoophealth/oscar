package org.oscarehr.common.dao;

import org.oscarehr.common.model.LabTestResults;
import org.springframework.stereotype.Repository;

@Repository
public class LabTestResultsDao extends AbstractDao<LabTestResults>{

	public LabTestResultsDao() {
		super(LabTestResults.class);
	}
}
