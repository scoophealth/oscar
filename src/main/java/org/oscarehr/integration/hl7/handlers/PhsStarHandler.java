/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package org.oscarehr.integration.hl7.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.OtherIdManager;
import org.oscarehr.common.dao.AdmissionDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.OscarLogDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.OscarLog;
import org.oscarehr.common.model.OtherId;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.integration.hl7.model.PatientId;
import org.oscarehr.integration.hl7.model.StaffId;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

public class PhsStarHandler extends BasePhsStarHandler {

	Logger logger = MiscUtils.getLogger();

	DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
	OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	ProgramDao programDao = (ProgramDao)SpringUtils.getBean("programDao");
	AdmissionDao admissionDao = (AdmissionDao)SpringUtils.getBean("admissionDao");
	OscarLogDao logDao = (OscarLogDao)SpringUtils.getBean("oscarLogDao");

	public PhsStarHandler() {
		logger.setLevel(Level.DEBUG);
	}


	/**
	 * Find a patient in the database.
	 *
	 * This method uses the internal ids and matches based on (in order) MRN, Temporary MRN, Health Card.
	 *
	 * @param internalIds
	 * @return Integer
	 * @throws HL7Exception Usually for bad data, or missing internal id segment.
	 */
	protected Integer doKeyMatching(Map<String,PatientId> internalIds) throws HL7Exception {
		logger.debug("executing patient key match algorithm");

		//search by MRN
		logger.debug("Searching by MRN");
		PatientId mrn = internalIds.get("MR");
		if(mrn != null) {
			logger.debug("MRN found in message");
			List<Demographic> records = demographicDao.getClientsByChartNo(mrn.getId());
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
		PatientId tmr = internalIds.get("TMR");
		if(tmr != null) {
			logger.debug("Temporary MRN found in message");
			OtherId otherId = OtherIdManager.searchTable(OtherIdManager.DEMOGRAPHIC,"TMR",internalIds.get("TMR").getId());
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
			List<Demographic> records = demographicDao.getClientsByHealthCard(hc.getId(),hc.getAuthority());
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
	private Integer createDemographic() throws HL7Exception {
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

		if(OscarProperties.getInstance().hasProperty("DEFAULT_PHS_PROVIDER")) {
			demo.setProviderNo(OscarProperties.getInstance().getProperty("DEFAULT_PHS_PROVIDER"));
		}

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
			String ver = this.getHealthCardVer();
			if(ver != null) {
				demo.setVer(ver);
			}
			String renew = this.getHealthCardRenewDate();
			if(renew != null) {
				Date tmp = this.convertToDate(renew);
				demo.setHcRenewDate(tmp);
			}
		}

		if(this.getPrimaryPractitionerId() != null && this.getPrimaryPractitionerId().length()>0) {
			demo.setFamilyDoctor("<rdohip>"+getPrimaryPractitionerId()+"</rdohip><rd>" + this.getPrimaryPractitionerLastName() + ", "+this.getPrimaryPractitionerFirstName()+"</rd>");
		}
		//save
		demographicDao.saveClient(demo);

		Integer demographicNo = demo.getDemographicNo();
		logger.debug("new patient saved with demographicNo="+demographicNo);

		//save temporary MRN if available
		PatientId tempMrn = internalIds.get("TMR");
		if(tempMrn != null) {
			OtherIdManager.saveIdDemographic(demographicNo, "TMR", tempMrn.getId());
		}

		Program p = programDao.getProgramByName(OscarProperties.getInstance().getProperty("phs.default_program", "OSCAR"));
		if(p != null && admissionDao.getCurrentAdmission(p.getId(), demographicNo) == null) {
			logger.info("need to do admission");
			doAdmit(demo,p,"000001");
		}
		return demographicNo;
	}

	/**
	 * Update demographic (patient) record.
	 *
	 * @param demographicNo
	 * @throws HL7Exception
	 */
	private void updateDemographic(Integer demographicNo) throws HL7Exception {
		logger.info("Updating patient record " + demographicNo);
		Demographic demo = demographicDao.getClientByDemographicNo(demographicNo);
		if(demo == null) {
			logger.error("couldn't find the patient record to update - " + demographicNo);
			throw new HL7Exception("couldn't find the record to update - " + demographicNo);
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

		//TODO: will this overwrite other values? should probably parse the XML and add if missing
		if(this.getPrimaryPractitionerId() != null && this.getPrimaryPractitionerId().length()>0) {
			demo.setFamilyDoctor("<rdohip>"+getPrimaryPractitionerId()+"</rdohip><rd>" + this.getPrimaryPractitionerLastName() + ", "+this.getPrimaryPractitionerFirstName()+"</rd>");
		}

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
			String ver = this.getHealthCardVer();
			if(ver != null) {
				demo.setVer(ver);
			}
			String renew = this.getHealthCardRenewDate();
			if(renew != null) {
				Date tmp = this.convertToDate(renew);
				demo.setHcRenewDate(tmp);
			}
		}

		//save
		demographicDao.saveClient(demo);

		//save temporary MRN (might want to check to see if this key already exists)
		PatientId tempMrn = internalIds.get("TMR");
		if(tempMrn != null && OtherIdManager.getDemoOtherId(demographicNo, "TMR")==null) {
			if(OtherIdManager.getDemoOtherId(demographicNo, "TMR")!= null) {
				OtherIdManager.saveIdDemographic(demographicNo, "TMR", tempMrn.getId());
			} else {
				OtherId oid = OtherIdManager.getDemoOtherIdAsOtherId(demographicNo, "TMR");
				oid.setOtherId(tempMrn.getId());
				OtherIdManager.merge(oid);
			}
		}

	}

	/**
	 *
	 * @param demographicNo
	 * @throws HL7Exception
	 */
	private void createNewAppointment(Integer demographicNo) throws HL7Exception {
		Demographic demographic = demographicDao.getClientByDemographicNo(demographicNo);
		if(demographic == null) {
			logger.error("Unable to retrieve patient data..cannot make appointment");
			throw new HL7Exception("Unable to retrieve patient data..cannot make appointment");
		}
		//match provider - STAR id is linked from OtherId table
		Provider provider = null;
		OtherId otherId = OtherIdManager.searchTable(OtherIdManager.PROVIDER,"STAR",getApptPractitionerNo());
		if(otherId != null) {
			provider = providerDao.getProvider(otherId.getTableId());
		}
		if(provider == null) {
			logger.error("Unable to match provider..cannot make appointment - " + getApptPractitionerNo());
			throw new HL7Exception ("Unable to match provider..cannot make appointment - " + getApptPractitionerNo());
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
		appt.setType(getApptType());
		appt.setReason(getApptReason());

		logger.info("resource unit = " + getApptResourceUnit());
		logger.info("procedure = " + getProcedureName());

		//String programId = findProgram(getApptResourceUnit(),getProcedureName());
		/*
		String programId = findProgram2();
		Program p = null;
		if(programId != null) {
			p = programDao.getProgram(Integer.parseInt(programId));
			if(p != null) {
				appt.setProgramId(p.getId());
			} else {
				throw new HL7Exception("System not configured to accept messages for runit " + getApptResourceUnit());
			}
		}
		*/

		//TODO: fix the bug in schedule
		appt.setProgramId(0);

		appt.setNotes("");
		appt.setRemarks("");
		appt.setResources("");
		appt.setStatus("t");

		//if(appointmentDao.checkForConflict(appt)) {
		//	logger.error("Conflict");
		//	throw new HL7Exception("Unable to schedule this appointment due to conflict");
		//}

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

		/*
		//admit if necessary
		if(p != null && p.getId()>0) {
			//check to see if they are admitted to program already
			if(admissionDao.getCurrentAdmission(p.getId(), demographicNo) == null) {
				logger.info("need to do admission");
				doAdmit(demographic,p,"000001");
			}
		}
		*/

	}

	private void doAdmit(Integer demographicNo, Program p, String providerNo) {
		Demographic demographic = demographicDao.getClientByDemographicNo(demographicNo);
		doAdmit(demographic,p,providerNo);
	}

	private void doAdmit(Demographic demographic, Program p, String providerNo) {
		Admission admission = new Admission();
		admission.setAdmissionDate(new Date());
		admission.setAdmissionNotes("PHS");
		admission.setAdmissionStatus("current");
		admission.setClient(demographic);
		admission.setClientId(demographic.getDemographicNo());
		admission.setProgram(p);
		admission.setProgramId(p.getId());
		admission.setProviderNo(providerNo);
		admission.setClientStatusId(null);
		admission.setTeamId(0);
		admission.setRadioDischargeReason("0");
		admissionDao.saveAdmission(admission);
		logger.info("admission made to program " + p.getName() + " for " + demographic.getFormattedName());
	}
	/**
	 * Reschedule an existing appointment. We match on Temporary Account Number or Account Number.
	 *
	 * @throws HL7Exception
	 */
	public void rescheduleAppointment() throws HL7Exception {
		//find appointment by TAN or AN
		Map<String,PatientId> ids = this.extractInternalPatientIds();
		OtherId otherId = null;
		if(ids.get("TAN")!=null) {
			otherId = OtherIdManager.searchTable(OtherIdManager.APPOINTMENT,"TAN",ids.get("TAN").getId());
		}
		if(ids.get("AN")!=null) {
			otherId = OtherIdManager.searchTable(OtherIdManager.APPOINTMENT,"AN",ids.get("AN").getId());
		}
		if(otherId==null && extractPatientAccountNumber() != null) {
			otherId = OtherIdManager.searchTable(OtherIdManager.APPOINTMENT,"AN",extractPatientAccountNumber().getId());
		}
		if(otherId == null) {
			logger.warn("Could not find appt to reschedule");
			return;
		}

		Appointment appt = appointmentDao.find(Integer.valueOf(otherId.getTableId()));

		if(appt == null) {
			logger.warn("couldn't load up the appt");
			return;
		}

		Demographic demographic = demographicDao.getClientByDemographicNo(appt.getDemographicNo());

		//match provider - STAR id is linked from OtherId table
		Provider provider = null;
		OtherId pOtherId = OtherIdManager.searchTable(OtherIdManager.PROVIDER,"STAR",getApptPractitionerNo());
		if(pOtherId != null) {
			provider = providerDao.getProvider(pOtherId.getTableId());
		}
		if(provider == null) {
			logger.error("Unable to match provider..cannot make appointment - " + getApptPractitionerNo());
			throw new HL7Exception ("Unable to match provider..cannot make appointment - " + getApptPractitionerNo());
		}

		appt.setProviderNo(provider.getProviderNo());
		appt.setAppointmentDate(getApptStartDate());
		appt.setStartTime(getApptStartDate());
		appt.setEndTime(getApptEndDate());
		appt.setLocation(getApptLocation());
		appt.setType(getApptType());
		appt.setReason(getApptReason());

		logger.info("resource unit = " + getApptResourceUnit());
		logger.info("procedure = " + getProcedureName());
//		String programId = findProgram(getApptResourceUnit(),getProcedureName());
		String programId = findProgram2();
		Program p = null;
		if(programId != null) {
			p = programDao.getProgram(Integer.parseInt(programId));
			if(p != null) {
				appt.setProgramId(p.getId());
			} else {
				throw new HL7Exception("System not configured to accept messages for runit " + getApptResourceUnit());
			}
		}
		//TODO: fix the bug in schedule
		appt.setProgramId(0);


		//if(appointmentDao.checkForConflict(appt)) {
		//	logger.error("Conflict");
		//	throw new HL7Exception("Unable to schedule this appointment due to conflict");
		//}

		appointmentDao.merge(appt);

		//admit if necessary
		if(p != null && p.getId()>0) {
			//check to see if they are admitted to program already
			if(admissionDao.getCurrentAdmission(p.getId(), demographic.getDemographicNo()) == null) {
				logger.info("need to do admission");
				doAdmit(demographic,p,"000001");
			}
		}

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
			if(otherId==null && extractPatientAccountNumber() != null) {
				otherId = OtherIdManager.searchTable(OtherIdManager.APPOINTMENT,"AN",extractPatientAccountNumber().getId());
			}
		}


		if(otherId == null) {
			logger.warn("Could not find appt to reschedule");
			return;
		}

		Appointment appt = appointmentDao.find(Integer.parseInt(otherId.getTableId()));

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
			if(otherId==null && extractPatientAccountNumber() != null) {
				otherId = OtherIdManager.searchTable(OtherIdManager.APPOINTMENT,"AN",extractPatientAccountNumber().getId());
			}
		}

		if(otherId == null) {
			logger.warn("Could not find appt to reschedule");
			return;
		}

		Appointment appt = appointmentDao.find(Integer.parseInt(otherId.getTableId()));

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
	 * MFI - Master File Identification
	 * MFE - Master File Entry
	 * STF - Staff Identification
	 * PRA - Practitioner Detail
	 * ZST - <unknown>
	 * ZRA - <unknown>
	 * @throws HL7Exception
	 */
	public void handleStaffMasterFile() throws HL7Exception {
		//MFI section is pretty straightforward., don't think we need these values.
		String mfIdentifierId = 		this.extractOrEmpty("MFI-1-1");
		String mfIdentifierText = 		this.extractOrEmpty("MFI-1-2");
		String mfIdentifierCodeSys =	this.extractOrEmpty("MFI-1-3");
		String mfIdentifierAltId = 		this.extractOrEmpty("MFI-1-4");
		String mfIdentifierAltText = 	this.extractOrEmpty("MFI-1-5");
		String mfIdentifierAltCodeSys = this.extractOrEmpty("MFI-1-6");
		String fileLevelEventCode	=	this.extractOrEmpty("MFI-3");
		String effectiveDateTime	=	this.extractOrEmpty("MFI-5");
		String responseLevelCode	= 	this.extractOrEmpty("MFI-6");


		String recordLevelEventCode	=	this.extractOrEmpty("/MF_STAFF/MFE-1");	//handle only MAD/MUP

		if(recordLevelEventCode.equals("MDL")) {
			//delete event.
			logger.warn("Received an MFN delete..ignoring.");
			return;
		}

		String recordEffectiveDateTime	=	this.extractOrEmpty("/MF_STAFF/MFE-3"); //assume now
		String practId					=	this.extractOrEmpty("/MF_STAFF/MFE-4-1"); //use 1st comp. to insert/update.pri key

		//staff segment - the meat of the info.
		String stfPractId	= this.extractOrEmpty("/MF_STAFF/STF-1");	//same as practId .. or should be.
		Map<String,StaffId> staffIds = this.extractStaffIds();
		String lastName = this.extractOrEmpty("/MF_STAFF/STF-3-1");
		String firstName = this.extractOrEmpty("/MF_STAFF/STF-3-2");
		String middleName = this.extractOrEmpty("/MF_STAFF/STF-3-3");
		String staffType = this.extractOrEmpty("/MF_STAFF/STF-4");		 //eg. MD
		String active = this.extractOrEmpty("/MF_STAFF/STF-7"); //eg. 'A' or 'I'

		//repetition
		//(905)555-1212X1234^WPN^PH^^^905^5551212^1234
		//[NNN] [(999)]999-9999 [X99999] [B99999] [C any text] ^ <telecommunication use code (ID)> ^ <telecommunication equipment type (ID)> ^ <email address (ST)> ^ <county code (NM)> ^ <area/city code (NM)> ^ <phone number (NM) ^ <extension (NM)> ^ <any text (st)>
		String phone = null;
		String fax = null;

		for(int x=0;x<3;x++) {
			String phoneTmp = this.extractOrEmpty("/MF_STAFF/STF-10("+x+")-1");
			if(phoneTmp != null&& phoneTmp.indexOf("C")!=-1) {
				phoneTmp = phoneTmp.substring(0,phoneTmp.indexOf("C"));
			}
			String phoneType = this.extractOrEmpty("/MF_STAFF/STF-10("+x+")-3");

			if(phoneType != null && phoneType.equals("PH")) {
				phone = phoneTmp;
			}
			if(phoneType != null && phoneType.equals("FX")) {
				fax = phoneTmp;
			}
		}

		//repetitive
		String address = this.extractOrEmpty("/MF_STAFF/STF-11-1");
		String address2 = this.extractOrEmpty("/MF_STAFF/STF-11-2");
		String city = this.extractOrEmpty("/MF_STAFF/STF-11-3");
		String province = this.extractOrEmpty("/MF_STAFF/STF-11-4");
		String postal = this.extractOrEmpty("/MF_STAFF/STF-11-5");
		String country = this.extractOrEmpty("/MF_STAFF/STF-11-6");
		String type = this.extractOrEmpty("/MF_STAFF/STF-11-7"); //H,O,M

		String jobCode = this.extractOrEmpty("/MF_STAFF/STF-19");	//MD

		//practitioner - do we need this?
		String praPractId = this.extractOrEmpty("/MF_STAFF/PRA-1-1"); //should be same as pract_id
		//specialty = 1&FAMILY PRACTITIONER&99H62&1&FAMILY PRACTITIONER&99SPC
		String praSpecialty = this.extractOrEmpty("/MF_STAFF/PRA-5-1-2"); //should be same as pract_id

		logger.info("need to do a provider add/update for id " + practId);

		//logger.info("mfId="+mfId);
		ProfessionalSpecialistDao professionalSpecialistDao = (ProfessionalSpecialistDao) SpringUtils.getBean("professionalSpecialistDao");
		ProfessionalSpecialist specialist = null;
		specialist = professionalSpecialistDao.getByReferralNo(practId);
		if(specialist == null) {
			//insert
			specialist = new ProfessionalSpecialist();
			specialist.setReferralNo(practId);
		}
		//update fields
		specialist.setLastName(lastName);
		specialist.setFirstName(firstName);
		specialist.setSpecialtyType(praSpecialty);
		specialist.setStreetAddress(address + "\n" + address2 + "\n" + city + " " + province + "\n" + postal + "\n" + country);
		specialist.setPhoneNumber(phone);
		specialist.setFaxNumber(fax);

		professionalSpecialistDao.merge(specialist);
	}


	public void updatePrimaryPhysician(int demographicNo) {
		String attendingId = this.getAttendingId();
		if(attendingId.length()>0) {
			OtherId otherId = OtherIdManager.searchTable(OtherIdManager.PROVIDER, "STAR", attendingId);
			if(otherId != null) {
				String providerNo = otherId.getTableId();
				Demographic d = demographicDao.getClientByDemographicNo(demographicNo);
				if(d != null) {
					d.setProviderNo(providerNo);
					demographicDao.saveClient(d);
					logger.info("Updated primary physician to attending - " + providerNo);
				}
			} else {
				logger.warn("Attending not found in oscar");
			}
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
    	String controlId = t.get("/MSH-10-1");

    	logger.info("hl7 message id is " + controlId);
    	logger.info("\n"+hl7Body);
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
        	this.logPatientMessage(controlId,msgType+"^"+triggerEvent,hl7Body,demographicNo);
        }

        //SIU S12 is NEW APPOINTMENT
        if(msgType.equals("SIU") && triggerEvent.equals("S12")) {
        	logger.info("Schedule a new appointment");

        	Integer demographicNo = this.doKeyMatching(extractInternalPatientIds());
        	if(demographicNo == null) {
           		demographicNo = createDemographic();
        	}

        	createNewAppointment(demographicNo);
        	this.logPatientMessage(controlId,msgType+"^"+triggerEvent,hl7Body,demographicNo);
        }

        //SIU S13 is RESCHEDULED APPOINTMENT
        if(msgType.equals("SIU") && triggerEvent.equals("S13")) {
        	logger.info("Reschedule Appointment");
        	Integer demographicNo = this.doKeyMatching(extractInternalPatientIds());
        	rescheduleAppointment();
        	this.logPatientMessage(controlId,msgType+"^"+triggerEvent,hl7Body,demographicNo);
        }

        //SIU S14 is CHANGED APPOINTMENT
        if(msgType.equals("SIU") && triggerEvent.equals("S14")) {
        	logger.info("Change Appointment");
        	Integer demographicNo = this.doKeyMatching(extractInternalPatientIds());
        	rescheduleAppointment();
        	this.logPatientMessage(controlId,msgType+"^"+triggerEvent,hl7Body,demographicNo);
        }


        //ADT A27 is CANCEL PENDING ADMIT - don't do anything? (happens with an S15)
        if(msgType.equals("ADT") && triggerEvent.equals("A27")) {
        	logger.info("cancel pending admit");
        }


        //SIU S15 is CANCEL APPOINTMENT status='C'
        if(msgType.equals("SIU") && triggerEvent.equals("S15")) {
        	logger.info("cancel appointment");
        	Integer demographicNo = this.doKeyMatching(extractInternalPatientIds());
        	updateAppointmentStatus("C");
        	this.logPatientMessage(controlId,msgType+"^"+triggerEvent,hl7Body,demographicNo);
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
        	//updatePrimaryPhysician(demographicNo);
        	String programId = findProgram2();
    		Program pp = null;
    		if(programId != null) {
    			pp = programDao.getProgram(Integer.parseInt(programId));
    			if(pp != null) {
    				if(admissionDao.getCurrentAdmission(pp.getId(), demographicNo) == null) {
    					logger.info("need to do admission");
    					doAdmit(demographicNo,pp,"000001");
    				}
    			}
    		}

    		this.logPatientMessage(controlId,msgType+"^"+triggerEvent,hl7Body,demographicNo);
        }


        //ADT A03 is DISCHARGE A PATIENT (they are done!).
        if(msgType.equals("ADT") && triggerEvent.equals("A03")) {
        	Integer demographicNo = this.doKeyMatching(extractInternalPatientIds());
        	updateAppointmentStatus("E");
        	this.logPatientMessage(controlId,msgType+"^"+triggerEvent,hl7Body,demographicNo);
        }

        //MFN M02 is Master file - staff practitioner
        if(msgType.equals("MFN") && triggerEvent.equals("M02")) {
        	handleStaffMasterFile();
        }

	}


	/////////// GETTERS ////////////////


	public String getAddress() {
		try {
			String address1 = this.extractOrEmpty("PID-11-1");
			String address2 = this.extractOrEmpty("PID-11-2");
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

			if(id == null || authority == null || typeId == null) {
				//try insurance way
				id = t.get("/INSURANCE/PV1-50-1");
				authority = t.get("/INSURANCE/PV1-50-4");
				typeId = t.get("/INSURANCE/PV1-50-5");
				if(id == null || authority == null || typeId == null) {
					return null;
				}
			}

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
		cal.add(Calendar.MINUTE, Integer.parseInt(aptDuration)-1);

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
			String firstName = this.extractOrEmpty("PID-5-2");
			return firstName;
		}catch(Exception e) {
			return "";
		}
	}

	public String getMiddleName() {
		try {
			String middleName = this.extractOrEmpty("PID-5-3");
			//"" populating middleName on update
			if(middleName.equals("\"\"")){middleName="";}
			return middleName;
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



	public String getPrimaryPractitionerId() {
		String var = null;
		try {
			var = this.extractOrEmpty("/PD1-4-1");
		}catch(Exception e) {/*swallow exception*/}
		if(var == null || var.equals("")) {
			try {
				var = this.extractOrEmpty("/INSURANCE/PD1-4-1");
				return var;
			}catch(Exception ee) {/*swallow exception*/}
		}
		return new String();

	}

	public String getPrimaryPractitionerLastName() {
		try {
			String var = this.extractOrEmpty("/PD1-4-2");
			return var;
		}catch(Exception e) {/*swallow exception*/}
		try {
			String var = this.extractOrEmpty("/INSURANCE/PD1-4-2");
			return var;
		}catch(Exception ee) {/*swallow exception*/}
		return new String();

	}

	public String getPrimaryPractitionerFirstName() {
		try {
			String var = this.extractOrEmpty("/PD1-4-3");
			return var;
		}catch(Exception e) {/*swallow exception*/}
		try {
			String var = this.extractOrEmpty("/INSURANCE/PD1-4-3");
			return var;
		}catch(Exception ee) {/*swallow exception*/}
		return new String();

	}

	protected void logPatientMessage(String messageId,String messageType,String message, int demographicNo) {
		OscarLog log = new OscarLog();
		log.setAction("incoming_hl7");
		log.setContentId(messageId);
		log.setContent(messageType);
		log.setData(message);
		log.setDemographicId(demographicNo);
		logDao.persist(log);
	}

	//PV1-7
	public String getAttendingId() {
		try {
			String var = this.extractOrEmpty("/PV1-7-1");
			if(var != null && var.length()>0) {
				return var;
			}
		}catch(Exception e) {/*swallow exception*/}

		try {
			String var = this.extractOrEmpty("/INSURANCE/PV1-7-1");
			return var;
		}catch(Exception ee) {/*swallow exception*/}
		return new String();

	}

	public String getAttendingLastName() {
		try {
			String var = this.extractOrEmpty("/PV1-7-2");
			return var;
		}catch(Exception e) {/*swallow exception*/}
		try {
			String var = this.extractOrEmpty("/INSURANCE/PV1-7-2");
			return var;
		}catch(Exception ee) {/*swallow exception*/}
		return new String();

	}

	public String getAttendingFirstName() {
		try {
			String var = this.extractOrEmpty("/PV1-7-3");
			return var;
		}catch(Exception e) {/*swallow exception*/}
		try {
			String var = this.extractOrEmpty("/INSURANCE/PV1-7-3");
			return var;
		}catch(Exception ee) {/*swallow exception*/}
		return new String();

	}


	//PV1-17
	public String getAdmittingId() {
		try {
			String var = this.extractOrEmpty("/PV1-17-1");
			return var;
		}catch(Exception e) {/*swallow exception*/}
		try {
			String var = this.extractOrEmpty("/INSURANCE/PV1-17-1");
			return var;
		}catch(Exception ee) {/*swallow exception*/}
		return new String();

	}

	public String getAdmittingLastName() {
		try {
			String var = this.extractOrEmpty("/PV1-17-2");
			return var;
		}catch(Exception e) {/*swallow exception*/}
		try {
			String var = this.extractOrEmpty("/INSURANCE/PV1-17-2");
			return var;
		}catch(Exception ee) {/*swallow exception*/}
		return new String();

	}

	public String getAdmittingFirstName() {
		try {
			String var = this.extractOrEmpty("/PV1-17-3");
			return var;
		}catch(Exception e) {/*swallow exception*/}
		try {
			String var = this.extractOrEmpty("/INSURANCE/PV1-17-3");
			return var;
		}catch(Exception ee) {/*swallow exception*/}
		return new String();

	}

	public String getProcedureName() {
		try {
			String var = this.extractOrEmpty("/AIS-3-1");
			return var;
		}catch(Exception e) {/*swallow exception*/}

		return new String();

	}

	private String findProgram2() {
		//service = pv1-10
		//patient_type = pv1-18
		//location= pv2-23-3
		String service =null;
		String patientType = null;
		String location=null;

		service = getValueAndTryGroup("/PV1-10","INSURANCE");
		patientType = getValueAndTryGroup("/PV1-18","INSURANCE");
		location = getValueAndTryGroup("/PV2-23-3","INSURANCE");

		logger.info("service="+service);
		logger.info("patientType="+patientType);
		logger.info("location="+location);

		if(service.length()==0 || patientType.length()==0 || location.length()==0) {
			logger.warn("Did not have all information to determine program - " + service + "," + patientType + "," + location);
			return null;
		}

		String programId = readProgramMappingFile(service,patientType,location);

		logger.info("mapped to program " + programId);

		return programId;
	}

	private String readProgramMappingFile(String service, String patientType, String location) {
		String filename = OscarProperties.getInstance().getProperty("phs_star.program_file");
		if(filename == null) {
        	logger.warn("Cannot lookup program. Config file not found - " + filename);
        	return null;
        }
        InputStream is = null;

        try {
        	is = new FileInputStream(new File(filename));

	        if(is != null) {
		        SAXBuilder parser = new SAXBuilder();
		        Document doc = null;
		        try {
		        	doc = parser.build(is);
		        }catch(Exception e) {
		        	logger.error("Error",e);
		        	return null;
		        }

		        Element root = doc.getRootElement();
		        @SuppressWarnings("unchecked")
		        List<Element> items = root.getChildren();
		        for (int i = 0; i < items.size(); i++){
		            Element e = items.get(i);
		            if(e.getName().equals("mapping")) {
		            	String service1 = e.getAttributeValue("service");
		            	String patientType1 = e.getAttributeValue("patientType");
		            	String location1 = e.getAttributeValue("location");

		            	if(service1.equals(service) && patientType1.equals(patientType) && location1.equals(location) ) {
		            		return e.getAttributeValue("programId");
		            	}
		            }
		        }
	        }
        }catch(Exception e) {
        	logger.error("error",e);
        } finally {
        	if(is != null) {
        		try {
        			is.close();
        		}catch(IOException e) {
        			logger.error("error",e);
        		}
        	}
        }
        return null;
	}

	public String getValueAndTryGroup(String path, String group) {
		try {
			String var = this.extractOrEmpty(path);
			if(var != null && var.length()>0) {
				return var;
			}
		}catch(Exception e) {/*swallow exception*/}

		try {
			String var = this.extractOrEmpty("/"+group+path);
			return var;
		}catch(Exception ee) {/*swallow exception*/}
		return new String();

	}
}
