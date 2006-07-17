

package org.caisi.PMmodule.dao;


import java.util.List;

import javax.sql.DataSource;

import org.caisi.PMmodule.model.Admission;
import org.caisi.PMmodule.model.Program;
import org.caisi.PMmodule.service.ProgramManager;

//###############################################################################

public interface AdmissionDao
{
	public Admission getAdmission(String programId, String demographicNo);
	
	public Admission getCurrentAdmission(String programId, String demographicNo);
	
	public List getAdmissions();

	public List getAdmissions(String demographicNo);
	
	public List getBedProgramAdmissionHistory(ProgramManager programMgr, String demographicNo);

	public List getServiceProgramAdmissionHistory(ProgramManager programMgr, String demographicNo); 
	
	public List getAdmissions(String demographicNo, int rowCountPass, int totalRowDisplay);

	public List getCurrentAdmissions(String demographicNo, int rowCountPass, int totalRowDisplay);

	public List getCurrentAdmissions(String demographicNo);

	public Admission getCurrentBedProgramAdmission(ProgramManager programMgr, String demographicNo);

	public List getCurrAdmissionRecordsOfABedProgram(ProgramManager programMgr, String bedProgramId);
	
	public List getCurrAdmissionRecordsOfAServiceProgram(ProgramManager programMgr, String serviceProgramId);

	public List getCurrentServiceProgramAdmission(ProgramManager programMgr, String demographicNo);

	public List getClientIdsFromProgramIds(String providerNo, List selectedProgramIds);
	
	public List getClientIdsFromProgramIdsNotEqual(Database_Service databaseService, DataSource dataSource, String providerNo, List selectedProgramIds);
	
	public boolean isProgramIdInAdmission(String programId);
	
	public boolean addAdmission(Admission admission, ProgramManager programMgr, Program program);

	public boolean dischargeAdmission(ProgramManager programMgr, String programId, Admission admission, String amId);

	public boolean updateAdmission(Admission admission);

	public boolean removeAdmission(String amId);

	public List getCurrentAdmissionsByProgramId(Long programId);
	
	public List getAllAdmissionsByProgramId(Long programId);
	
	public Admission getAdmission(Long id);
	
	public void saveAdmission(Admission admission);

	public List getAdmissionsInTeam(Long programId, Long teamId);
}

