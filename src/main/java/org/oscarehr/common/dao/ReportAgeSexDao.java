package org.oscarehr.common.dao;

import org.oscarehr.common.model.ReportAgeSex;
import org.springframework.stereotype.Repository;

@Repository
public class ReportAgeSexDao extends AbstractDao<ReportAgeSex>{

	public ReportAgeSexDao() {
		super(ReportAgeSex.class);
	}
}
