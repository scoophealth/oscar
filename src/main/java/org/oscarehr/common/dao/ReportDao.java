package org.oscarehr.common.dao;

import org.oscarehr.common.model.Report;
import org.springframework.stereotype.Repository;

@Repository
public class ReportDao extends AbstractDao<Report>{

	public ReportDao() {
		super(Report.class);
	}
}
