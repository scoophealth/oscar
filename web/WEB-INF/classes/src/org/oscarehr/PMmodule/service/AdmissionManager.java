package org.oscarehr.PMmodule.service;

import java.util.Date;
import java.util.List;

import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.exception.AlreadyAdmittedException;
import org.oscarehr.PMmodule.exception.ProgramFullException;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.AdmissionSearchBean;
import org.oscarehr.PMmodule.model.Program;

public interface AdmissionManager  {
	
	public Admission getAdmission(String programId, String demographicNo);
	
	public Admission getCurrentAdmission(String programId, String demographicNo);
	
	public List getAdmissions();

	public List getAdmissions(String demographicNo);
		
	public List getCurrentAdmissions(String demographicNo);

	public Admission getCurrentBedProgramAdmission(String demographicNo);

	public List getCurrentServiceProgramAdmission(String demographicNo);
	
	public Admission getCurrentCommunityProgramAdmission(String demographicNo);
	
	public List getCurrentAdmissionsByProgramId(String programId);
	
	public Admission getAdmission(Long id);
	
	public void saveAdmission(Admission admission);
	
	public void processAdmission(String demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes) throws ProgramFullException,AdmissionException;

	public void processAdmission(String demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, Date admissionDate) throws ProgramFullException,AdmissionException;

	public void processAdmission(String demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, boolean tempAdmission) throws ProgramFullException, AdmissionException;

	public void processInitialAdmission(String demographicNo, String providerNo, Program program, String admissionNotes, Date admissionDate) throws ProgramFullException,AlreadyAdmittedException;

	public Admission getTemporaryAdmission(String demographicNo);
	
	public List getCurrentTemporaryProgramAdmission(String demographicNo);

	public List search(AdmissionSearchBean searchBean);
	
	public void processDischarge(Integer programId, Integer clientId, String dischargeNotes) throws AdmissionException;
	
	public void processDischargeToCommunity(Integer programId, Integer clientId, String providerNo, String dischargeNotes) throws AdmissionException;
	
}
