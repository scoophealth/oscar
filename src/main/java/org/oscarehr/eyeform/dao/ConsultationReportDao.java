package org.oscarehr.eyeform.dao;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.eyeform.model.ConsultationReport;
import org.springframework.stereotype.Repository;

@Repository
public class ConsultationReportDao extends AbstractDao<ConsultationReport> {
	
	public ConsultationReportDao() {
		super(ConsultationReport.class);
	}

}
