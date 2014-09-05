/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
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
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.common.dao.AdmissionDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.FunctionalCentreDao;
import org.oscarehr.common.model.Admission;
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
		public ArrayList<Integer> dataReportResult=new ArrayList<Integer>();
		
		private DataRow(int dataReportId, String dataReportDescription, int dataReportResult) {
			this.dataReportId = dataReportId;
			this.dataReportDescription = StringEscapeUtils.escapeHtml(dataReportDescription);
			this.dataReportResult.add(dataReportResult);
		}
	}

	private String reportByDescription = null;
	private GregorianCalendar startDate = null;
	private GregorianCalendar endDate = null;
	private List<Program> selectedPrograms = null;
	private List<Admission> admissions=null;
	private ArrayList<DataRow> dataRows=null;
	private ArrayList<String> headerRow=new ArrayList<String>();
	{
		headerRow.add("Id");
		headerRow.add("Description");
		headerRow.add("Results");
	}
	
	/**
	 * End dates should be treated as inclusive.
	 */
	public MisReportUIBean(LoggedInInfo loggedInInfo, String functionalCentreId, GregorianCalendar startDate, GregorianCalendar endDate) {

		this.startDate =startDate; 
		this.endDate=endDate;

		FunctionalCentre functionalCentre = functionalCentreDao.find(functionalCentreId);
		reportByDescription=StringEscapeUtils.escapeHtml(functionalCentre.getAccountId() + ", " + functionalCentre.getDescription());
		selectedPrograms = programDao.getProgramsByFacilityIdAndFunctionalCentreId(loggedInInfo.getCurrentFacility().getId(), functionalCentreId);
		
		populateAdmissions();
		generateDataRows();
	}

	/**
	 * End dates should be treated as inclusive.
	 */
	public MisReportUIBean(String[] programIds, GregorianCalendar startDate, GregorianCalendar endDate) {

		this.startDate =startDate; 
		this.endDate=endDate;

		selectedPrograms=new ArrayList<Program>();
		StringBuilder programNameList=new StringBuilder();
		for (String programIdString : programIds)
		{
			int programId=Integer.parseInt(programIdString);
			Program program=programDao.getProgram(programId);
			selectedPrograms.add(program);
			
			if (programNameList.length()>0) programNameList.append(", ");
			programNameList.append(program.getName());
		}
		reportByDescription=StringEscapeUtils.escapeHtml(programNameList.toString());
		
		populateAdmissions();
		generateDataRows();
	}
	
	
	public ArrayList<String> getHeaderRow()
	{
		return(headerRow);
	}
	
	public String getReportByDescription() {
		return (reportByDescription);
	}

	public static String getDateRangeForDisplay(GregorianCalendar startDate, GregorianCalendar endDate) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return (StringEscapeUtils.escapeHtml(simpleDateFormat.format(startDate.getTime()) + " to " + simpleDateFormat.format(endDate.getTime()) + " (inclusive)"));
	}
	
	private void populateAdmissions() {

		admissions = new ArrayList<Admission>();

		for (Program program : selectedPrograms) {
			List<Admission> programAdmissions = admissionDao.getAdmissionsByProgramAndDate(program.getId(), startDate.getTime(), endDate.getTime());

			logger.debug("corresponding mis admissions count:" + admissions.size());

			for (Admission admission : programAdmissions) {
				admissions.add(admission);
				logger.debug("valid mis admission, id=" + admission.getId());
			}
		}
	}

	public void generateDataRows() {
		dataRows = new ArrayList<DataRow>();

		addBedProgramInfo(dataRows);
		
		dataRows.add(getFaceToFace());
		dataRows.add(getPhone());
		dataRows.add(getIndividualsSeen());
		dataRows.add(getTotalIndividualsServed());
	}

	public ArrayList<DataRow> getDataRows()
	{
		return(dataRows);
	}
	
	private boolean isProgramType(Integer programId, String programType) {
		for (Program program : selectedPrograms) {
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
		for (Program program : selectedPrograms)
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
		for (Program program : selectedPrograms)
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
		for (Program program : selectedPrograms)
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
		for (Program program : selectedPrograms)
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
	
	public static MisReportUIBean getSplitProgramReports(String[] programIds, GregorianCalendar startDate, GregorianCalendar endDate)
	{
		ArrayList<MisReportUIBean> misReportBeans=new ArrayList<MisReportUIBean>();
		StringBuilder description=new StringBuilder();
		MisReportUIBean tempMisReportBean=null;
		
		ArrayList<String> headerRow=new ArrayList<String>();
		headerRow.add("Id");
		headerRow.add("Description");

		for (String programIdString : programIds)
		{
			tempMisReportBean=new MisReportUIBean(new String[]{programIdString}, startDate, endDate);
			
			if (description.length()>0) description.append(", ");
			description.append(tempMisReportBean.getReportByDescription());
			
			headerRow.add(tempMisReportBean.getReportByDescription());
		
			misReportBeans.add(tempMisReportBean);
		}
		
		tempMisReportBean.reportByDescription=description.toString();
		tempMisReportBean.dataRows=combineDataSet(misReportBeans);
		tempMisReportBean.headerRow=headerRow;
		return(tempMisReportBean);
	}

	private static ArrayList<DataRow> combineDataSet(ArrayList<MisReportUIBean> misReportBeans) {
		ArrayList<DataRow> combinedData=new ArrayList<DataRow>();
		
		combineDataIds(combinedData, misReportBeans);
		combineDataResults(combinedData, misReportBeans);
		
		return(combinedData);
	}

	private static void combineDataIds(ArrayList<DataRow> combinedData, ArrayList<MisReportUIBean> misReportBeans) {
		for (MisReportUIBean misReportUIBean : misReportBeans) combineDataIds(combinedData, misReportUIBean);
	}

	private static void combineDataIds(ArrayList<DataRow> combinedData, MisReportUIBean misReportUIBean) {
		for (DataRow beanRow : misReportUIBean.getDataRows())
		{
			DataRow dataRow=getDataRowFromId(combinedData, beanRow.dataReportId);
			if (dataRow==null) combinedData.add(new DataRow(beanRow.dataReportId, beanRow.dataReportDescription, 0));
		}
    }

	private static DataRow getDataRowFromId(ArrayList<DataRow> combinedData, int dataReportId) {
		for (DataRow dataRow : combinedData) 
		{
			if (dataRow.dataReportId==dataReportId) return(dataRow);
		}
		
		return(null);
	}

	private static void combineDataResults(ArrayList<DataRow> combinedData, ArrayList<MisReportUIBean> misReportBeans) {
		for (DataRow dataRow : combinedData)
		{
			combineDataResultsFromBeans(dataRow, misReportBeans);
		}
    }

	private static void combineDataResultsFromBeans(DataRow combinedDataRow, ArrayList<MisReportUIBean> misReportBeans) {
		ArrayList<Integer> tempResults=new ArrayList<Integer>();

		for (MisReportUIBean reportBean : misReportBeans)
		{
			tempResults.add(getDataResultsFromArrayRows(reportBean.getDataRows(), combinedDataRow.dataReportId));
		}
		
		combinedDataRow.dataReportResult=tempResults;
	}

	private static Integer getDataResultsFromArrayRows(ArrayList<DataRow> dataRows, int dataReportId) {
		DataRow tempDataRow=getDataRowFromId(dataRows, dataReportId);
		if (tempDataRow==null) return(null);
		else return(tempDataRow.dataReportResult.get(0));
		
    }

}
