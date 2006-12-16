package org.oscarehr.casemgmt.dao;

import org.oscarehr.casemgmt.model.CaseManagementCPP;

public interface CaseManagementCPPDAO extends DAO {
	public CaseManagementCPP getCPP(String demographic_no);
	public void saveCPP(CaseManagementCPP cpp);
}
