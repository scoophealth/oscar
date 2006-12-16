package org.oscarehr.casemgmt.dao;

import org.oscarehr.casemgmt.model.CaseManagementCPP;
import org.oscarehr.casemgmt.model.CaseManagementNote;

public interface EchartDAO
{
	public void saveCPPIntoEchart(CaseManagementCPP cpp, String providerNo);

	public void updateEchartOngoing(CaseManagementCPP cpp);

	public String saveEchart(CaseManagementNote note, CaseManagementCPP cpp,
			String userName, String lastStr);
}
