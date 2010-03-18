/*
 * Copyright (c) 2007-2009. CAISI, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * This software was written for 
 * CAISI, 
 * Toronto, Ontario, Canada 
 */

package org.oscarehr.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.AdmissionDao;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.FunctionalCentreDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.FunctionalCentre;
import org.oscarehr.util.EncounterUtil;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.DateUtils;

public final class MisReportUIBean {

	private static Logger logger = MiscUtils.getLogger();

	private FunctionalCentreDao functionalCentreDao = (FunctionalCentreDao) SpringUtils.getBean("functionalCentreDao");
	private AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");
	private ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
	private DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	private CaseManagementNoteDAO caseManagementNoteDAO=(CaseManagementNoteDAO) SpringUtils.getBean("CaseManagementNoteDAO");
	
	private static int ELDERLY_AGE = 65;

	public static class DataRow {
		public int dataReportId;
		public String dataReportDescription;
		public int dataReportResult;

		private DataRow(int dataReportId, String dataReportDescription, int dataReportResult) {
			this.dataReportId = dataReportId;
			this.dataReportDescription = dataReportDescription;
			this.dataReportResult = dataReportResult;
		}
	}

	private FunctionalCentre functionalCentre = null;
	private GregorianCalendar startDate = null;
	private GregorianCalendar endDate = null;
	private List<Program> programs = null;
	private List<Admission> admissions=null;
	
	/**
	 * End dates should be treated as inclusive.
	 */
	public MisReportUIBean(String functionalCentreId, int startYear, int startMonth, int endYear, int endMonth) {

		startDate = new GregorianCalendar(startYear, startMonth, 1);
		endDate = new GregorianCalendar(endYear, endMonth, 1);
		endDate.add(GregorianCalendar.MONTH, 1); // this is to set it inclusive

		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		functionalCentre = functionalCentreDao.find(functionalCentreId);
		programs = programDao.getProgramsByFacilityIdAndFunctionalCentreId(loggedInInfo.currentFacility.getId(), functionalCentreId);
		populateAdmissions();
	}

	public String getFunctionalCentreDescription() {
		return (StringEscapeUtils.escapeHtml(functionalCentre.getAccountId() + ", " + functionalCentre.getDescription()));
	}

	public String getDateRangeForDisplay() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar displayEndDate = (GregorianCalendar) endDate.clone();
		displayEndDate.add(GregorianCalendar.MONTH, -1);
		return (StringEscapeUtils.escapeHtml(simpleDateFormat.format(startDate.getTime()) + " to " + simpleDateFormat.format(displayEndDate.getTime()) + " (inclusive)"));
	}

	private void populateAdmissions() {

		admissions = new ArrayList<Admission>();

		for (Program program : programs) {
			List<Admission> programAdmissions = admissionDao.getAdmissionsByProgramAndDate(program.getId(), startDate.getTime(), endDate.getTime());

			logger.debug("corresponding mis admissions count:" + admissions.size());

			for (Admission admission : programAdmissions) {
				admissions.add(admission);
				logger.debug("valid mis admission, id=" + admission.getId());
			}
		}
	}

	public ArrayList<DataRow> getDataRows() {
		ArrayList<DataRow> results = new ArrayList<DataRow>();

		addBedProgramInfo(results);
		
		results.add(getFaceToFace());
		results.add(getPhone());
		results.add(getIndividualsSeen());
		results.add(getTotalIndividualsServed());
		
		return (results);
	}

	private boolean isProgramType(Integer programId, String programType) {
		for (Program program : programs) {
			if (program.getId().equals(programId)) {
				return (programType.equals(program.getType()));
			}
		}

		return (false);
	}

	private void addBedProgramInfo(ArrayList<DataRow> results) {
		// only pertains to bed programs

		int totalDaysElderly = 0;
		int totalDaysAdult = 0;
		int totalNewAdmissionsElderly=0;
		int totalNewAdmissionsAdult=0;
		int totalNewDischargeAdult=0;
		HashSet<Integer> uniqueElderlyClients=new HashSet<Integer>();
		HashSet<Integer> uniqueAdultClients=new HashSet<Integer>();
		
		for (Admission admission : admissions) {
			if (isProgramType(admission.getProgramId(), Program.BED_TYPE)) {
				int residentDays = calculateResidentDays(admission);
				boolean isNewAdmission=isNewAdmission(admission);
				
				Demographic demographic = demographicDao.getDemographicById(admission.getClientId());
				Integer age = DateUtils.getAge(demographic.getBirthDay(), endDate);
				
				if (age != null && age.intValue() >= ELDERLY_AGE) {
					totalDaysElderly = totalDaysElderly + residentDays;
					if (isNewAdmission) totalNewAdmissionsElderly++;
					
					uniqueElderlyClients.add(admission.getClientId());
				} else {
					totalDaysAdult=totalDaysAdult+residentDays;
					if (isNewAdmission) totalNewAdmissionsAdult++;

					if (isNewDischarge(admission)) totalNewDischargeAdult++;

					uniqueAdultClients.add(admission.getClientId());
				}
			}
		}

		results.add(new DataRow(4034520, "Resident days - Elderly", totalDaysElderly));
		results.add(new DataRow(4034540, "Resident days - Adult", totalDaysAdult));
		results.add(new DataRow(4014520, "New Resident Admission - Elderly", totalNewAdmissionsElderly));
		results.add(new DataRow(4014540, "New Resident Admission - Adult", totalNewAdmissionsAdult));
		results.add(new DataRow(4104540, "New Resident discharged - Adult", totalNewDischargeAdult));
		results.add(new DataRow(4554520, "Unique Individuals Served by FC - Elderly", uniqueElderlyClients.size()));
		results.add(new DataRow(4554540, "Unique Individuals Served by FC - Adult", uniqueAdultClients.size()));
	}

	private boolean isNewAdmission(Admission admission) {
		return(startDate.before(admission.getAdmissionCalendar()) && endDate.after(admission.getAdmissionCalendar()));
	}

	private boolean isNewDischarge(Admission admission) {
		return(startDate.before(admission.getDischargeCalendar()) && endDate.after(admission.getDischargeCalendar()));
	}

	private int calculateResidentDays(Admission admission) {
		GregorianCalendar startBound = startDate;
		if (startBound.before(admission.getAdmissionCalendar())) startBound = admission.getAdmissionCalendar();

		GregorianCalendar endBound = endDate;
		if (endBound.after(admission.getDischargeCalendar())) endBound = admission.getDischargeCalendar();

		return (DateUtils.calculateDayDifference(startBound, endBound));
	}
	
	private DataRow getFaceToFace() {
		int visitsCount=0;
		
		// count face to face notes in the system pertaining to the allowed programs during that time frame.
		for (Program program : programs)
		{
			visitsCount=visitsCount+countFaceToFaceNotesInProgram(program);
		}
		
		return(new DataRow(4502440, "Visits Face to Face (must have client record)", visitsCount));	    
    }

	private int countFaceToFaceNotesInProgram(Program program) {
		List<CaseManagementNote> notes=caseManagementNoteDAO.getCaseManagementNoteByProgramIdAndObservationDate(program.getId(), startDate.getTime(), endDate.getTime());
		
		int count=0;
		
		for (CaseManagementNote note : notes)
		{
			if (EncounterUtil.EncounterType.FACE_TO_FACE_WITH_CLIENT.getOldDbValue().equals(note.getEncounter_type()))
			{
				// as per request/requirements only count individuals with a client record.
				// I know, this doesn't entirely make sense as a note must have a client record...
				Demographic demographic=demographicDao.getDemographic(note.getDemographic_no());
				if (demographic!=null) count++;
			}
		}
		
		return(count);
	}

	private DataRow getPhone() {
		int visitsCount=0;
		
		// count phone notes in the system pertaining to the allowed programs during that time frame.
		for (Program program : programs)
		{
			visitsCount=visitsCount+countPhoneNotesInProgram(program);
		}
		
		return(new DataRow(4512440, "Visits - Telephone (named and unique anonymous)", visitsCount));	    
    }

	private int countPhoneNotesInProgram(Program program) {
		List<CaseManagementNote> notes=caseManagementNoteDAO.getCaseManagementNoteByProgramIdAndObservationDate(program.getId(), startDate.getTime(), endDate.getTime());
		
		int count=0;
		
		for (CaseManagementNote note : notes)
		{
			if (EncounterUtil.EncounterType.TELEPHONE_WITH_CLIENT.getOldDbValue().equals(note.getEncounter_type()))
			{
				// as per request/requirements only count individuals who are named or are unique anonymous
				Demographic demographic=demographicDao.getDemographic(note.getDemographic_no());
				if (demographic!=null)
				{
					if (demographic.getAnonymous()==null || Demographic.UNIQUE_ANONYMOUS.equals(demographic.getAnonymous())) count++;
				}
			}
		}
		
		return(count);
	}

	private DataRow getIndividualsSeen() {
		HashSet<Integer> individuals=new HashSet<Integer>();
		
		// count face to face individuals in the system pertaining to the allowed programs during that time frame.
		for (Program program : programs)
		{
			addIndividualsSeenInProgram(individuals, program);
		}
		
		return(new DataRow(4526000, "Service Recipients Seen, (anonymous or no client record, excluding unique anonymous)", individuals.size()));	    
    }

	private void addIndividualsSeenInProgram(HashSet<Integer> individuals, Program program) {
		List<CaseManagementNote> notes=caseManagementNoteDAO.getCaseManagementNoteByProgramIdAndObservationDate(program.getId(), startDate.getTime(), endDate.getTime());
		
		for (CaseManagementNote note : notes)
		{
			if (EncounterUtil.EncounterType.FACE_TO_FACE_WITH_CLIENT.getOldDbValue().equals(note.getEncounter_type()))
			{
				// as per request/requirements only count individuals who are anonymous or have no client record, excluding unique_anonymous
				// sorry can only count people who have demographic records.
				Demographic demographic=demographicDao.getDemographic(note.getDemographic_no());
				if (demographic!=null && Demographic.ANONYMOUS.equals(demographic.getAnonymous())) individuals.add(demographic.getDemographicNo());
			}
		}
	}

	private DataRow getTotalIndividualsServed() {
		HashSet<Integer> individuals=new HashSet<Integer>();
		
		// count individuals in the system pertaining to the allowed programs during that time frame.
		for (Program program : programs)
		{
			addIndividualsServedInProgram(individuals, program);
		}
		
		return(new DataRow(4552440, "Individuals Served, (total clients)", individuals.size()));	    
    }

	private void addIndividualsServedInProgram(HashSet<Integer> individuals, Program program) {
		List<CaseManagementNote> notes=caseManagementNoteDAO.getCaseManagementNoteByProgramIdAndObservationDate(program.getId(), startDate.getTime(), endDate.getTime());
		
		for (CaseManagementNote note : notes)
		{
			Demographic demographic=demographicDao.getDemographic(note.getDemographic_no());
			if (demographic!=null) individuals.add(demographic.getDemographicNo());
		}
	}
}
