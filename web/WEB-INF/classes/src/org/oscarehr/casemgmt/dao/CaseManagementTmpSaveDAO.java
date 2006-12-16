package org.oscarehr.casemgmt.dao;

import org.oscarehr.casemgmt.model.CaseManagementTmpSave;

public interface CaseManagementTmpSaveDAO {

	public void save(CaseManagementTmpSave obj);
	public void delete(String providerNo, Long demographicNo, Long programId);
	public CaseManagementTmpSave load(String providerNo, Long demographicNo, Long programId);
}
