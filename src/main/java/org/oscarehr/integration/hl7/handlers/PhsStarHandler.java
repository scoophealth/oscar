package org.oscarehr.integration.hl7.handlers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.AdmissionDao;
import org.oscarehr.PMmodule.dao.ClientDao;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.OtherIdManager;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.OtherId;
import org.oscarehr.common.model.Provider;
import org.oscarehr.integration.hl7.model.PatientId;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

public class PhsStarHandler extends BasePhsStarHandler {

	Logger logger = MiscUtils.getLogger();
	
	ClientDao clientDao = (ClientDao)SpringUtils.getBean("clientDao");
	OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	ProgramDao programDao = (ProgramDao)SpringUtils.getBean("programDao");
	AdmissionDao admissionDao = (AdmissionDao)SpringUtils.getBean("admissionDao");
	
	public PhsStarHandler() {
		logger.setLevel(Level.DEBUG);
	}			
	

	/**
	 * Find a patient in the database.
	 * 
	 * This method uses the internal ids and matches based on (in order) MRN, Temporary MRN, Health Card.
	 * 
	 * @param internalIds
	 * @return
	 * @throws HL7Exception Usually for bad data, or missing internal id segment.
	 */
	protected Integer doKeyMatching(Map<String,PatientId> internalIds) throws HL7Exception {
		logger.debug("executing patient key match algorithm");
		
		//search by MRN
		logger.debug("Searching by MRN");
		PatientId mrn = internalIds.get("MR");
		if(mrn != null) {			
			logger.debug("MRN found in message");
			List<Demographic> records = clientDao.getClientsByChartNo(mrn.getId());
			if(records.size() == 1) {
				logger.debug("Found demographic:"+records.get(0).getDemographicNo());
				return records.get(0).getDemographicNo();
			} else if(records.size() > 1) {
				logger.debug("Found multiple demographics with MRN " + mrn.getId());
				throw new HL7Exception("Found multiple records with same MRN!!!! - " + mrn.getId());
			}
		}
		
		//search by Temporary MRN
		logger.debug("Searching by Temporary MRN");
		PatientId trn = internalIds.get("TMR");
		if(trn != null) {
			logger.debug("Temporary MRN found in message");			
			OtherId otherId = OtherIdManager.searchTable(OtherIdManager.DEMOGRAPHIC,"TRN",internalIds.get("TRN").getId());
			if(otherId != null) {
				logger.debug("Found demographic:"+otherId.getTableId());
				return Integer.parseInt(otherId.getTableId());
			}
		}
		
		//search by health card
		logger.debug("Searching by Health Card");
		PatientId hc = internalIds.get("JHN");
		if(hc != null) {
			logger.debug("Health Card found in message");	
			List<Demographic> records = clientDao.getClientsByHealthCard(hc.getId(),hc.getAuthority());
			if(records.size() == 1) {
				logger.debug("Found demographic:"+records.get(0).getDemographicNo());
				return records.get(0).getDemographicNo();
			} else if(records.size() > 1) {
				logger.debug("Found multiple demographics with health card " + hc.getId() + " " + hc.getAuthority());
				throw new HL7Exception("Found multiple records with same HC!!!! - " + hc.getId() +" " + hc.getAuthority());
			}
		}

		logger.debug("Found nothing. Done searching");
		return null;				
	}
	

	/**
	 * Create a new demographic (patient) record.
	 * This also saves the Temporary MRN into the OtherId table.
	 * 
	 * @throws HL7Exception
	 */
	private void createDemographic() throws HL7Exception {
		logger.info("creating new patient record for " + getLastName() + "," + getFirstName());
		Demographic demo = new Demographic();
		demo.setLastName(getLastName());
		demo.setFirstName(getFirstName());
		demo.setAddress(getAddress());
		Date dob = this.convertToDate(getDateOfBirth());
		Calendar cal = Calendar.getInstance();
		cal.setTime(dob);
		demo.setBirthDay(cal);
		demo.setCity(getCity());			
		demo.setPatientStatus("AC");
		demo.setPhone(getPhone());
		demo.setPhone2(getBusinessPhone());
		demo.setPostal(getPostalCode());
		demo.setProvince(getProvince());
		demo.setSex(getSex());
		demo.setDateJoined(new Date());
		demo.setEffDate(new Date());
		
		//set MRN
		Map<String,PatientId> internalIds = this.extractInternalPatientIds();
		if(internalIds.get("MR")!=null) {
			demo.setChartNo(internalIds.get("MR").getId());
		}
		
		//set Health Card #
		PatientId hc = internalIds.get("JHN");
		if(hc != null) {
			demo.setHin(hc.getId());
			demo.setHcType(hc.getAuthority());
		}
		
		//save 		
		clientDao.saveClient(demo);
		
		Integer demographicNo = demo.getDemographicNo();
		
		//save temporary MRN if available
		PatientId tempMrn = internalIds.get("TMR");
		if(tempMrn != null) {			
			OtherIdManager.saveIdDemographic(demographicNo, "TMR", tempMrn.getId());		
		}
	}
	
	/**
	 * Update demographic (patient) record.
	 * 
	 * @param demographicNo
	 * @throws HL7Exception
	 */
	private void updateDemographic(Integer demographicNo) throws HL7Exception {
		logger.info("Updating patient record " + demographicNo);
		Demographic demo = clientDao.getClientByDemographicNo(demographicNo);
		if(demo == null) {
			throw new HL7Exception("couldn't find the record to update");
		}
		
		//update the details
		demo.setLastName(getLastName());
		demo.setFirstName(getFirstName());
		demo.setAddress(getAddress());
		Date dob = this.convertToDate(getDateOfBirth());
		Calendar cal = Calendar.getInstance();
		cal.setTime(dob);
		demo.setBirthDay(cal);
		demo.setCity(getCity());			
		demo.setPatientStatus("AC");
		demo.setPhone(getPhone());
		demo.setPhone2(getBusinessPhone());
		demo.setPostal(getPostalCode());
		demo.setProvince(getProvince());
		demo.setSex(getSex());
		
		//set MRN
		Map<String,PatientId> internalIds = this.extractInternalPatientIds();
		if(internalIds.get("MR")!=null) {
			demo.setChartNo(internalIds.get("MR").getId());
		}
		
		//set Health Card #
		PatientId hc = internalIds.get("JHN");
		if(hc != null) {
			demo.setHin(hc.getId());
			demo.setHcType(hc.getAuthority());
		}
		
		//save 		
		clientDao.saveClient(demo);
				
		//save temporary MRN
		PatientId tempMrn = internalIds.get("TMR");
		if(tempMrn != null && OtherIdManager.getDemoOtherId(demographicNo, "TMR")==null) {			
			OtherIdManager.saveIdDemographic(demographicNo, "TMR", tempMrn.getId());
		}

	}
	
	/**
	 * 
	 * @param demographicNo
	 * @throws HL7Exception
	 */
	private void createNewAppointment(Integer demographicNo) throws HL7Exception {
		Demographic demographic = clientDao.getClientByDemographicNo(demographicNo);
		if(demographic == null) {
			logger.error("Unable to retrieve patient data..cannot make appointment");
			return;
		}
		//match provider		
		Provider provider = providerDao.getProviderByPractitionerNo(getApptPractitionerNo());
		if(provider == null) {
			logger.error("Unable to match provider..cannot make appointment");
			return;
		}
		//create appt
		Appointment appt = new Appointment();
		appt.setAppointmentDate(getApptStartDate());
		appt.setCreateDateTime(new Date());		
		appt.setCreator("PHS/STAR");
		appt.setDemographicNo(demographicNo);
		appt.setStartTime(getApptStartDate());
		appt.setEndTime(getApptEndDate());		
		appt.setLocation(getApptLocation());		
		appt.setName(demographic.getFormattedName());
		appt.setProviderNo(provider.getProviderNo());
		appt.setUpdateDateTime(new Date());
		appt.setType(getApptType());
		appt.setReason(getApptReason());	
		
		
		Program p = programDao.getProgramBySiteSpecificField(getApptResourceUnit());
		if(p != null) {
			appt.setProgramId(p.getId());
		} else {
			appt.setProgramId(0);
		}
		
		//TODO: fix the bug in schedule
		appt.setProgramId(0);
		
		appt.setNotes("");		
		appt.setRemarks("");
		appt.setResources("");
		appt.setStatus("t");
	
		if(appointmentDao.checkForConflict(appt)) {
			logger.error("Conflict");
			return;
		}
		
		//save it
		appointmentDao.persist(appt);
		
		//patbooking_id (alternate appt_id that relates to PHS/STAR)
		OtherIdManager.saveIdAppointment(appt.getId(), "patbooking_id", getApptPatBookingId());
		
		//Temporary Account number, and Account number
		Map<String,PatientId> internalIds = this.extractInternalPatientIds();
		PatientId tan = internalIds.get("TAN");
		if(tan != null) {
			OtherIdManager.saveIdAppointment(appt.getId(), "TAN", tan.getId());
		}
		PatientId an = internalIds.get("AN");
		if(an == null) {
			an = extractPatientAccountNumber();
		}
		if(an != null) {
			OtherIdManager.saveIdAppointment(appt.getId(), "AN", an.getId());
		}		

		
		//admit if necessary
		if(p != null && p.getId()>0) {
			//check to see if they are admitted to program already
			if(admissionDao.getCurrentAdmission(p.getId(), demographicNo) == null) {
				logger.info("need to do admission");
				Admission admission = new Admission();
				admission.setAdmissionDate(new Date());
				admission.setAdmissionNotes("PHS");
				admission.setAdmissionStatus("current");
				admission.setClient(demographic);
				admission.setClientId(demographicNo);
				admission.setProgram(p);
				admission.setProgramId(p.getId());
				//PHS user
				admission.setProviderNo("000001");
				admission.setClientStatusId(0);
				admission.setTeamId(0);
				admission.setRadioDischargeReason("0");
				admissionDao.saveAdmission(admission);
			}
		}
		
	}
	
	/**
	 * Reschedule an existing appointment. We match on Temporary Account Number or Account Number.
	 * 
	 * @throws HL7Exception
	 */
	public void rescheduleAppointment() throws HL7Exception {
		//find appt by TAN or AN
		Map<String,PatientId> ids = this.extractInternalPatientIds();
		OtherId otherId = null;
		if(ids.get("TAN")!=null) {
			otherId = OtherIdManager.searchTable(OtherIdManager.APPOINTMENT,"TAN",ids.get("TAN").getId());
		}
		if(ids.get("AN")!=null) {
			otherId = OtherIdManager.searchTable(OtherIdManager.APPOINTMENT,"AN",ids.get("AN").getId());
		}
		if(otherId == null) {
			logger.warn("Could not find appt to reschedule");
			return;
		}
		
		Appointment appt = appointmentDao.find(otherId.getTableId());
		
		if(appt == null) {
			logger.warn("couldn't load up the appt");
			return;
		}
		
		appt.setAppointmentDate(getApptStartDate());
		appt.setStartTime(getApptStartDate());
		appt.setEndTime(getApptEndDate());
		appt.setUpdateDateTime(new Date());
		
		appointmentDao.merge(appt);
	}
	
	/**
	 * Update the appointment status.
	 * 
	 * @param newStatus
	 * @throws HL7Exception
	 */
	public void updateAppointmentStatus(String newStatus) throws HL7Exception {
		//find appt by TAN/AN
		PatientId id = this.getTemporaryAccountNumber();		
		Map<String,PatientId> ids = this.extractInternalPatientIds();
		OtherId otherId = null;
		if(id != null) {
			otherId = OtherIdManager.searchTable(OtherIdManager.APPOINTMENT,"TAN",id.getId());
		} else {
			if(ids.get("TAN")!=null) {
				otherId = OtherIdManager.searchTable(OtherIdManager.APPOINTMENT,"TAN",ids.get("TAN").getId());
			}
			if(ids.get("AN")!=null) {
				otherId = OtherIdManager.searchTable(OtherIdManager.APPOINTMENT,"AN",ids.get("AN").getId());
			}
		}
		if(otherId == null) {
			logger.warn("Could not find appt to reschedule");
			return;
		}
		
		Appointment appt = appointmentDao.find(otherId.getTableId());
		
		if(appt == null) {
			logger.warn("couldn't load up the appt");
			return;
		}
		
		appt.setStatus(newStatus);
		
		appointmentDao.merge(appt);
	}
	
	public void updateAppointmentAccountNumber() throws HL7Exception {
		OtherId otherId = null;		
		PatientId id = getTemporaryAccountNumber();
		Map<String,PatientId> ids = this.extractInternalPatientIds();
		
		if(id != null) {
			otherId = OtherIdManager.searchTable(OtherIdManager.APPOINTMENT,"TAN",id.getId());
		} else {
			//find appt by TAN or AN				
			if(ids.get("TAN")!=null) {
				otherId = OtherIdManager.searchTable(OtherIdManager.APPOINTMENT,"TAN",ids.get("TAN").getId());
			}
			if(ids.get("AN")!=null) {
				otherId = OtherIdManager.searchTable(OtherIdManager.APPOINTMENT,"AN",ids.get("AN").getId());
			}			
		}
		
		if(otherId == null) {
			logger.warn("Could not find appt to reschedule");
			return;
		}
		
		Appointment appt = appointmentDao.find(otherId.getTableId());
		
		if(appt == null) {
			logger.warn("couldn't load up the appt");
			return;
		}
		if(getAccountNumber() != null) {
			OtherIdManager.saveIdAppointment(appt.getId(), "AN", getAccountNumber().getId());
		}
		if(ids.get("AN")!=null) {
			OtherIdManager.saveIdAppointment(appt.getId(), "AN", getAccountNumber().getId());
		}
	}
	
	/**
	 * This method is the entry point.
	 * 
	 * Logic to choose which message type we have, and to handle it.
	 * 
	 */
	public void init(String hl7Body) throws HL7Exception {
		Parser p = new GenericParser();
        p.setValidationContext(new NoValidation());       
        msg = p.parse(hl7Body.replaceAll( "\n", "\r\n" ));
        
        t = new Terser(msg);
    	String msgType = t.get("/MSH-9-1");
    	String triggerEvent = t.get("/MSH-9-2");
    	logger.info("msg = " + msgType + " " + triggerEvent);    	
    	
    	//ADT A14 is PENDING ADMIT
        if(msgType.equals("ADT") && triggerEvent.equals("A14")) {
        	logger.info("Pending Admit");        	
           	Integer demographicNo = this.doKeyMatching(extractInternalPatientIds());
           	if(demographicNo == null) {
           		createDemographic();
           	}
        }
        
        //ADT A08 is UPDATE PATIENT INFORMATION, ADT A31 UPDATE PERSON INFORMATION
        if(msgType.equals("ADT") && (triggerEvent.equals("A08")||triggerEvent.equals("A31"))) {
        	logger.info("Update Patient/Person Information");
        	
        	Integer demographicNo = this.doKeyMatching(extractInternalPatientIds());
        	if(demographicNo == null) {
        		logger.error("Patient not found!");
        		return;
        	}
        	updateDemographic(demographicNo);        	
        }
        
        //SIU S12 is NEW APPOINTMENT
        if(msgType.equals("SIU") && triggerEvent.equals("S12")) {
        	logger.info("Schedule a new appointment");
        	
        	Integer demographicNo = this.doKeyMatching(extractInternalPatientIds());
        	if(demographicNo == null) {
           		createDemographic();
        	}
        	
        	createNewAppointment(demographicNo);
        }
        
        //SIU S13 is RESCHEDULED APPOINTMENT
        if(msgType.equals("SIU") && triggerEvent.equals("S13")) {
        	logger.info("Reschedule Appointment");        	
        	rescheduleAppointment();
        }
        
      
        //ADT A27 is CANCEL PENDING ADMIT - don't do anything? (happens with an S15)
        if(msgType.equals("ADT") && triggerEvent.equals("A27")) {
        	logger.info("cancel pending admit");        	
        }
        
      
        //SIU S15 is CANCEL APPOINTMENT status='C'
        if(msgType.equals("SIU") && triggerEvent.equals("S15")) {
        	logger.info("cancel appointment");        	
        	updateAppointmentStatus("C");
        }
        
        
        //ADT A04 is REGISTER A PATIENT
        if(msgType.equals("ADT") && triggerEvent.equals("A04")) {        	
        	Integer demographicNo = this.doKeyMatching(extractInternalPatientIds());
        	if(demographicNo == null) {
        		logger.error("Patient not found!");
        		return;
        	}
        	updateDemographic(demographicNo);
        	updateAppointmentAccountNumber();
        	updateAppointmentStatus("H");
        }
        
      
        //ADT A03 is DISCHARGE A PATIENT (they are done!). 
        if(msgType.equals("ADT") && triggerEvent.equals("A03")) {        	
        	updateAppointmentStatus("E");
        }
        
        
	}
	
	
	/////////// GETTERS ////////////////
	
	
	public String getAddress() {
		try {
			String address1 = this.extractOrEmpty("PID-11-1");
			String address2 = this.extractOrEmpty("PID-11-1");
			return address1 + " " + address2;
		}catch(Exception e) {
			return "";
		}		
	}
	
	public String getCity() {
		try {
			String var = this.extractOrEmpty("PID-11-3");
			return var;
		}catch(Exception e) {
			return "";
		}
	}
	
	public String getProvince() {
		try {
			String var = this.extractOrEmpty("PID-11-4");
			return var;
		}catch(Exception e) {
			return "";
		}
	}
	
	public String getPostalCode() {
		try {
			String var = this.extractOrEmpty("PID-11-5");
			return var;
		}catch(Exception e) {
			return "";
		}
	}
	
	public String getCountry() {
		try {
			String var = this.extractOrEmpty("PID-11-6");
			return var;
		}catch(Exception e) {
			return "";
		}
	}
	
	public String getPhone() {
		try {
			String var = this.extractOrEmpty("PID-13-1");
			return var;
		}catch(Exception e) {
			return "";
		}
	}
	
	public String getBusinessPhone() {
		try {
			String var = this.extractOrEmpty("PID-14-1");
			return var;
		}catch(Exception e) {
			return "";
		}
	}
	
	public String getDateOfBirth() {
		try {
			String var = this.extractOrEmpty("PID-7-1");
			return var;
		}catch(Exception e) {
			return "";
		}
	}
	
	public PatientId getAccountNumber() {
		try {
			String id = t.get("PID-18-1");
			String authority = t.get("PID-18-4");
			String typeId = t.get("PID-18-5");
			
			if(id == null || authority == null)
				return null;
			
			return new PatientId(id,authority,typeId);			
		}catch(Exception e) {
			return null;
		}
	}
	
	public PatientId getTemporaryAccountNumber() {
		try {
			String id = t.get("PV1-50-1");
			String authority = t.get("PV1-50-4");
			String typeId = t.get("PV1-50-5");
			
			if(id == null || authority == null || typeId == null)
				return null;
			
			return new PatientId(id,authority,typeId);			
		}catch(Exception e) {
			return null;
		}
	}
	
	public String getApptPractitionerNo() {
		try {
			String var = this.extractOrEmpty("AIP-3-1");
			return var;
		}catch(Exception e) {
			return "";
		}
	}
	
	public Date getApptStartDate() {
		
		try {
			String startTimeStr = t.get("SCH-11-4");
			Date startTime = this.convertToDate(startTimeStr);		
			return startTime;
		}catch(Exception e) {
			return null;
		}
	}
	
	public Date getApptEndDate() {
		Date startDate = getApptStartDate();
		String aptDuration =null;		
		String aptDurationUnit = null;	
		
		try {
			aptDuration = t.get("SCH-9-1");		
			aptDurationUnit = t.get("SCH-10-1");	
		}catch(Exception e) {
			return null;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.add(Calendar.MINUTE, Integer.parseInt(aptDuration));
		
		return cal.getTime();
	}
	
	public String getApptLocation() {
		try {
			String var = this.extractOrEmpty("AIL-3-9");
			return var;
		}catch(Exception e) {
			return "";
		}
	}
	
	public String getApptPatBookingId() {
		try {
			String var = this.extractOrEmpty("/SCH-2(1)-1");
			return var;
		}catch(Exception e) {
			return "";
		}
	}
	
	public String getApptReason() {
		try {
			String var = this.extractOrEmpty("/SCH-7-1");
			return var;
		}catch(Exception e) {
			return "";
		}
	}
	
	public String getApptType() {
		try {
			String var = this.extractOrEmpty("/SCH-8-2");
			return var;
		}catch(Exception e) {
			return "";
		}
	}	
	
	public String getApptResourceUnit() {
		try {
			String var = this.extractOrEmpty("/SCH-5-2");
			return var;
		}catch(Exception e) {
			return "";
		}
	}	
	
	public String getSex() {
		try {
			String var = this.extractOrEmpty("PID-8");
			return var;
		}catch(Exception e) {
			return "";
		}
	}
	
	public String getFirstName() {
		try {
			String lastName = this.extractOrEmpty("PID-5-2");
			return lastName;
		}catch(Exception e) {
			return "";
		}
	}

	public String getLastName() {
		try {
			String lastName = this.extractOrEmpty("PID-5-1");
			return lastName;
		}catch(Exception e) {
			return "";
		}		
	}

	
	public String audit() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getAccessionNum() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getAge() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getCCDocs() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getClientRef() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getDOB() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getDocName() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public ArrayList getDocNums() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public ArrayList<String> getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getHealthNum() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getHomePhone() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getMsgDate() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getMsgPriority() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getMsgType() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getOBRComment(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public int getOBRCommentCount(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int getOBRCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public String getOBRName(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getOBXAbnormalFlag(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getOBXComment(int i, int j, int k) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public int getOBXCommentCount(int i, int j) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int getOBXCount(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int getOBXFinalResultCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public String getOBXIdentifier(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getOBXName(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getOBXReferenceRange(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getOBXResult(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getOBXResultStatus(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getOBXUnits(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getOBXValueType(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getObservationHeader(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getOrderStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getPatientLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getPatientName() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getServiceDate() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getTimeStamp(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getWorkPhone() {
		// TODO Auto-generated method stub
		return null;
	}


	
	public boolean isOBXAbnormal(int i, int j) {
		// TODO Auto-generated method stub
		return false;
	}

}
