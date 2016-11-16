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

package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.ConsultationRequestDao;
import org.oscarehr.common.dao.ConsultationRequestExtDao;
import org.oscarehr.common.dao.ConsultationServiceDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.ConsultationRequest;
import org.oscarehr.common.model.ConsultationServices;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;
import oscar.util.StringUtils;

public class EctConsultationFormRequestUtil {

	public String patientName;
	public String patientFirstName;
	public String patientLastName;
	public String patientAddress;
	public String patientPhone;
	public String patientWPhone;
	public String patientEmail;
	public String patientDOB;
	public String patientHealthNum;
	public String patientSex;
	public String patientAge;
	public String patientHealthCardType;
	public String patientHealthCardVersionCode;
	public String patientChartNo;
	public String familyPhysician;
	public String referalDate;
	public String service;
	public String specialist;
	public String appointmentDate;
	public String appointmentHour;
	public String appointmentMinute;
	public String appointmentPm;
	public String reasonForConsultation;
	public String clinicalInformation;
	public String concurrentProblems;
	public String currentMedications;
	public String allergies;
	public String sendTo;
	public String status;
	public String appointmentNotes;
	public String providerNo;
	public String urgency;
	public String specPhone;
	public String specFax;
	public String specAddr;
	public String specEmail;
	public Vector teamVec;
	public String demoNo;
	public String pwb;
	public String mrp = "";
	public String siteName;
	public String signatureImg;

	public String letterheadName;
	public String letterheadTitle;
	public String letterheadAddress;
	public String letterheadPhone;
	public String letterheadFax;
	
	public Integer fdid;
	
	private String appointmentInstructions;
	private String appointmentInstructionsLabel;
	
	private ConsultationServiceDao consultationServiceDao = (ConsultationServiceDao) SpringUtils.getBean("consultationServiceDao");
	private DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);

	private boolean bMultisites = org.oscarehr.common.IsPropertiesOn.isMultisitesEnable();

	public boolean estPatient(LoggedInInfo loggedInInfo, String demographicNo) {

		Demographic demographic = demographicManager.getDemographic(loggedInInfo, Integer.parseInt(demographicNo));
		boolean estPatient = false;

		if (demographic != null) {
			demoNo = demographicNo;
			patientFirstName = demographic.getFirstName();
			patientLastName = demographic.getLastName();
			patientName = demographic.getFormattedName();
			
			StringBuilder patientAddressSb = new StringBuilder();
			patientAddressSb.append(StringUtils.noNull(demographic.getAddress())).append("<br>")
							.append(StringUtils.noNull(demographic.getCity())).append(",")
							.append(StringUtils.noNull(demographic.getProvince())).append(",")
							.append(StringUtils.noNull(demographic.getPostal()));
			patientAddress = patientAddressSb.toString();
			patientPhone = StringUtils.noNull(demographic.getPhone());
			patientWPhone = StringUtils.noNull(demographic.getPhone2());
			patientEmail = StringUtils.noNull(demographic.getEmail());
			patientDOB = demographic.getFormattedDob();
			patientHealthNum = StringUtils.noNull(demographic.getHin());
			patientSex = demographic.getSex();
			patientHealthCardType = StringUtils.noNull(demographic.getHcType());
			patientHealthCardVersionCode = StringUtils.noNull(demographic.getVer());
			patientChartNo = StringUtils.noNull(demographic.getChartNo());
			patientAge = demographic.getAge();
			mrp = demographic.getFamilyDoctor();

			estPatient = true;
		}

		return estPatient;
	}

	public boolean estActiveTeams() {
		boolean verdict = true;
		teamVec = new Vector();

		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		for (String teamName : dao.getActiveTeams()) {
			if (!teamName.equals("")) {
				teamVec.add(teamName);
			}
		}
		return verdict;
	}

	public boolean estTeams() {
		boolean verdict = true;
		teamVec = new Vector();

		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		for (String teamName : dao.getActiveTeams()) {
			if (!teamName.equals("")) {
				teamVec.add(teamName);
			}
		}
		return verdict;
	}

	public boolean estTeamsBySite(String providerNo) {
		boolean verdict = true;
		teamVec = new Vector();
		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		for (String teamName : dao.getActiveTeamsViaSites(providerNo)) {
			if (!teamName.equals("")) {
				teamVec.add(teamName);
			}
		}
		return verdict;
	}

	public boolean estTeamsByTeam(String providerNo) {

		boolean verdict = true;
		teamVec = new Vector();

		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		for (String teamName : dao.getUniqueTeams()) {
			if (!teamName.equals("")) {
				teamVec.add(teamName);
			}
		}

		return verdict;
	}

	public boolean estRequestFromId(LoggedInInfo loggedInInfo, String id) {

		boolean verdict = true;
		getSpecailistsName(id);

		ConsultationRequestDao dao = SpringUtils.getBean(ConsultationRequestDao.class);
		ConsultationRequest cr = dao.find(ConversionUtils.fromIntString(id));
		ConsultationRequestExtDao daoExt = (ConsultationRequestExtDao) SpringUtils.getBean("consultationRequestExtDao");
		
		if (cr != null) {
			fdid = cr.getFdid();
			pwb = ConversionUtils.toBoolString(cr.isPatientWillBook());
			urgency = cr.getUrgency();
			providerNo = cr.getProviderNo();
			referalDate = ConversionUtils.toDateString(cr.getReferralDate());
			service = "" + cr.getServiceId();
			specialist = "" + cr.getSpecialistId();
			Date appointmentTime = cr.getAppointmentTime();
			reasonForConsultation = cr.getReasonForReferral();
			clinicalInformation = cr.getClinicalInfo();
			concurrentProblems = cr.getConcurrentProblems();
			currentMedications = cr.getCurrentMeds();
			allergies = cr.getAllergies();
			sendTo = cr.getSendTo();
			status = cr.getStatus();
			setAppointmentInstructions( cr.getAppointmentInstructions() );
			setAppointmentInstructionsLabel( cr.getAppointmentInstructionsLabel() );
			letterheadName = cr.getLetterheadName();
			letterheadTitle = daoExt.getConsultationRequestExtsByKey(ConversionUtils.fromIntString(id),"letterheadTitle");
			letterheadAddress = cr.getLetterheadAddress();
			letterheadPhone = cr.getLetterheadPhone();
			letterheadFax = cr.getLetterheadFax();

			letterheadName = letterheadName == null?"":letterheadName;
			letterheadTitle = letterheadTitle == null?"":letterheadTitle;
			letterheadAddress = letterheadAddress == null?"":letterheadAddress;
			letterheadPhone = letterheadPhone == null?"":letterheadPhone;
			letterheadFax = letterheadFax == null?"":letterheadFax;

			signatureImg = cr.getSignatureImg();

			appointmentNotes = cr.getStatusText();
			if (appointmentNotes == null || appointmentNotes.equals("null")) {
				appointmentNotes = "";
			}
			estPatient(loggedInInfo, "" + cr.getDemographicId());

			if (bMultisites) {
				siteName = cr.getSiteName();
			}

			String date = ConversionUtils.toDateString(cr.getAppointmentDate());
            if( date == null || date.equals("") ) {
            	appointmentDate ="";
            	appointmentHour = "";
            	appointmentMinute = "";
            	appointmentPm = "";

            }
            else {
            	appointmentDate = date;
            	Calendar cal = Calendar.getInstance();
				if (appointmentTime != null) {
					cal.setTime(appointmentTime);
					if (cal.get(Calendar.AM_PM) == Calendar.AM) 
						appointmentPm = "AM";
					else 
						appointmentPm = "PM";
					if(cal.get(Calendar.HOUR)==0)
						appointmentHour = "12";
					else
						appointmentHour = String.valueOf(cal.get(Calendar.HOUR));
					if(cal.get(Calendar.MINUTE)<10)
						appointmentMinute = "0" + String.valueOf(cal.get(Calendar.MINUTE));
					else
						appointmentMinute = String.valueOf(cal.get(Calendar.MINUTE));
				} else {
                	appointmentHour = "";
                	appointmentMinute = "";
                	appointmentPm = "";
				}
            }
        }

		return verdict;
	}

	public String getSpecailistsName(String id) {
		if (id == null || id.trim().length() == 0) {
			return "-1";
		}
		
		String retval = "";
		
		ProfessionalSpecialistDao dao = SpringUtils.getBean(ProfessionalSpecialistDao.class);
		ProfessionalSpecialist ps = dao.find(ConversionUtils.fromIntString(id));
		if (ps != null) {
			retval = ps.getFormattedTitle();
			specPhone = ps.getPhoneNumber();
			specFax = ps.getFaxNumber();
			specAddr = ps.getStreetAddress();
			specEmail = ps.getEmailAddress();
			MiscUtils.getLogger().debug("getting Null" + specEmail + "<");
			if (specPhone == null || specPhone.equals("null")) {
				specPhone = "";
			}
			if (specFax == null || specFax.equals("null")) {
				specFax = "";
			}
			if (specAddr == null || specAddr.equals("null")) {
				specAddr = "";
			}
			if (specEmail == null || specEmail.equalsIgnoreCase("null")) {
				specEmail = "";
			}
		}
			
		return retval;

	}

	public String getSpecailistsEmail(String id) {
		MiscUtils.getLogger().debug("in Get SPECAILISTS EMAIL \n\n" + id);
		String retval = "";
		
		ProfessionalSpecialistDao dao = SpringUtils.getBean(ProfessionalSpecialistDao.class);
		ProfessionalSpecialist ps = dao.find(ConversionUtils.fromIntString(id));
		if (ps != null) {
			if (specEmail == null || specEmail.equalsIgnoreCase("null")) {
				specEmail = new String();
			}
			retval = specEmail;
		}
		return retval;
	}

	public String getProviderTeam(String id) {
		if(id == null || id.length()==0)
			return "";
		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		Provider p = dao.getProvider(id);
		if (p != null) {
			return p.getTeam();
		}
		return "";
	}

	public String getProviderName(String id) {
		if(id == null || id.length()==0)
			return "";
		
		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		Provider p = dao.getProvider(id);
		if (p != null) {
			return p.getFormattedName();
		}
		return "";
	}

	public String getFamilyDoctor() {
		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		List<Provider> ps = dao.getProviderByPatientId(ConversionUtils.fromIntString(demoNo));
		if (ps.isEmpty()) {
			return "";
		}
		return ps.get(0).getFormattedName();
	}

	public String getServiceName(String id) {
		String retval = new String();
		ConsultationServices cs = consultationServiceDao.find(Integer.parseInt(id));
		if (cs != null) {
			retval = cs.getServiceDesc();
		}

		return retval;
	}

	public String getClinicName() {
		ClinicDAO clinicDao = (ClinicDAO) SpringUtils.getBean("clinicDAO");

		String retval = new String();
		Clinic clinic = clinicDao.getClinic();
		if (clinic != null) {
			retval = clinic.getClinicName();
		}

		return retval;
	}

	public String getAppointmentInstructions() {
		return appointmentInstructions;
	}

	private void setAppointmentInstructions(String appointmentInstructions) {
		if( appointmentInstructions == null ) {
			appointmentInstructions = "";
		}
		this.appointmentInstructions = appointmentInstructions;
	}

	public String getAppointmentInstructionsLabel() {
		return appointmentInstructionsLabel;
	}

	private void setAppointmentInstructionsLabel(String appointmentInstructionsLabel) {
		if( appointmentInstructionsLabel == null ) {
			appointmentInstructionsLabel = "";
		}
		this.appointmentInstructionsLabel = appointmentInstructionsLabel;
	}
}
