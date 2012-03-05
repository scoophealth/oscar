package org.oscarehr.common.dao;

import org.oscarehr.common.model.ReportByExamples;
import org.springframework.stereotype.Repository;

@Repository
public class ReportByExamplesDao extends AbstractDao<ReportByExamples>{

	public ReportByExamplesDao() {
		super(ReportByExamples.class);
	}
}
