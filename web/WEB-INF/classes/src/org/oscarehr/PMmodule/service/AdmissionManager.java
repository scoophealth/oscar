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
	
	public Admission getAdmission(String programId, Integer demographicNo);
	
	public Admission getCurrentAdmission(String programId, Integer demographicNo);
	
	public List getAdmissions();

	public List getAdmissions(Integer demographicNo);
		
	public List getCurrentAdmissions(Integer demographicNo);

	public Admission getCurrentBedProgramAdmission(Integer demographicNo);

	public List getCurrentServiceProgramAdmission(Integer demographicNo);
	
	public Admission getCurrentCommunityProgramAdmission(Integer demographicNo);
	
	public List getCurrentAdmissionsByProgramId(String programId);
	
	public Admission getAdmission(Long id);
	
	public void saveAdmission(Admission admission);
	
	public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes) throws ProgramFullException,AdmissionException;

	public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, Date admissionDate) throws ProgramFullException,AdmissionException;

	public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, boolean tempAdmission) throws ProgramFullException, AdmissionException;

	public void processInitialAdmission(Integer demographicNo, String providerNo, Program program, String admissionNotes, Date admissionDate) throws ProgramFullException,AlreadyAdmittedException;

	public Admission getTemporaryAdmission(Integer demographicNo);
	
	public List getCurrentTemporaryProgramAdmission(Integer demographicNo);

	public List search(AdmissionSearchBean searchBean);
	
	public void processDischarge(Integer programId, Integer clientId, String dischargeNotes) throws AdmissionException;
	
	public void processDischargeToCommunity(Integer programId, Integer clientId, String providerNo, String dischargeNotes) throws AdmissionException;
	
}
