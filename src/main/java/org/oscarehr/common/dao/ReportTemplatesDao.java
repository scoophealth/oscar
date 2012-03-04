package org.oscarehr.common.dao;

import org.oscarehr.common.model.ReportTemplates;
import org.springframework.stereotype.Repository;

@Repository
public class ReportTemplatesDao extends AbstractDao<ReportTemplates>{

	public ReportTemplatesDao() {
		super(ReportTemplates.class);
	}
}
