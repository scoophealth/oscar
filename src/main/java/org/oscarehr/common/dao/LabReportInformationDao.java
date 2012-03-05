package org.oscarehr.common.dao;

import org.oscarehr.common.model.LabReportInformation;
import org.springframework.stereotype.Repository;

@Repository
public class LabReportInformationDao extends AbstractDao<LabReportInformation>{

	public LabReportInformationDao() {
		super(LabReportInformation.class);
	}
}
