package org.caisi.casemgmt.dao;

import org.caisi.casemgmt.model.CaseManagementCPP;
import org.caisi.casemgmt.model.CaseManagementNote;
import org.caisi.model.CaisiRole;

public interface EchartDAO
{
	public void saveCPPIntoEchart(CaseManagementCPP cpp, String providerNo);

	public void updateEchartOngoing(CaseManagementCPP cpp);

	public String saveEchart(CaseManagementNote note, CaseManagementCPP cpp,
			String userName, String lastStr);
}
