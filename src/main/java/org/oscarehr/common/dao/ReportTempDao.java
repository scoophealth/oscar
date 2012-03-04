package org.oscarehr.common.dao;

import org.oscarehr.common.model.ReportTemp;
import org.springframework.stereotype.Repository;

@Repository
public class ReportTempDao extends AbstractDao<ReportTemp>{

	public ReportTempDao() {
		super(ReportTemp.class);
	}
}
