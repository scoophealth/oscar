package org.oscarehr.common.dao;

import org.oscarehr.common.model.ReportConfig;
import org.springframework.stereotype.Repository;

@Repository
public class ReportConfigDao extends AbstractDao<ReportConfig>{

	public ReportConfigDao() {
		super(ReportConfig.class);
	}
}
