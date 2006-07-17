package org.caisi.casemgmt.dao;

import org.caisi.casemgmt.model.CaseManagementCPP;

public interface CaseManagementCPPDAO extends DAO {
	public CaseManagementCPP getCPP(String demographic_no);
	public void saveCPP(CaseManagementCPP cpp);
}
