package org.oscarehr.common.dao;

import org.oscarehr.common.model.LabRequestReportLink;
import org.springframework.stereotype.Repository;

@Repository
public class LabRequestReportLinkDao extends AbstractDao<LabRequestReportLink>{

	public LabRequestReportLinkDao() {
		super(LabRequestReportLink.class);
	}

}
