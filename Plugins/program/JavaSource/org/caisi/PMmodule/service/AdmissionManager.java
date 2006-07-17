package org.caisi.PMmodule.service;

import java.util.List;

import javax.sql.DataSource;

import org.caisi.PMmodule.dao.Database_Service;
import org.caisi.PMmodule.exception.ProgramFullException;
import org.caisi.PMmodule.model.Admission;
import org.caisi.PMmodule.model.Program;


public interface AdmissionManager 
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
	
	public List getCurrentAdmissionsByProgramId(String programId);
	
	public List getAllAdmissionsByProgramId(String programId);
	
	public Admission getAdmission(Long id);
	
	public void saveAdmission(Admission admission);
	
	public void processAdmission(String demographicNo, String providerNo, Program program, ProgramManager programManager, String dischargeNotes, String admissionNotes) throws ProgramFullException;

	public void processInitialAdmission(String demographicNo, String providerNo, Program program, String admissionNotes) throws ProgramFullException;

}
