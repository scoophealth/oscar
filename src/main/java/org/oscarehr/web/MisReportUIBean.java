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
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.common.dao.AdmissionDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.FunctionalCentreDao;
import org.oscarehr.common.dao.GroupNoteLinkDao;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.FunctionalCentre;
import org.oscarehr.common.model.GroupNoteLink;
import org.oscarehr.common.model.Provider;
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
	private ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
	private GroupNoteLinkDao groupNoteLinkDao = (GroupNoteLinkDao) SpringUtils.getBean("groupNoteLinkDao");
	
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

	public static class DataSPIRow {
		public String dataSPIString;
		public String dataSPIProgramName;
		public String dataSPIEncounterType;
		public ArrayList<String> dataSPIReportResult = new ArrayList<String>();
		public ArrayList<Integer> dataSPIReportTotalResult = new ArrayList<Integer>();
		
		private DataSPIRow(String dataSpiString, String dataSpiProgramName, String dataSpiEncounterType, String lessThan5mins, String min5To30, String min31To1Hr, String hr1To2, 
				String hr2To5, String moreThan5hrs, String total, String totalMIS) {
			this.dataSPIString = dataSpiString;
			this.dataSPIProgramName = dataSpiProgramName;
			this.dataSPIEncounterType = StringEscapeUtils.escapeHtml(dataSpiEncounterType);
			this.dataSPIReportResult.add(lessThan5mins);
			this.dataSPIReportResult.add(min5To30);
			this.dataSPIReportResult.add(min31To1Hr);
			this.dataSPIReportResult.add(hr1To2);
			this.dataSPIReportResult.add(hr2To5);
			this.dataSPIReportResult.add(moreThan5hrs);
			this.dataSPIReportResult.add(total);
			this.dataSPIReportResult.add(totalMIS);
			
		}
	}
	
	public static class DataSRIRow {
		public String dataSRIProgram;
		public ArrayList<String> dataSRIReportResult = new ArrayList<String>();
		
		private DataSRIRow(String dataSriProgram, String programCapacity, String SRICapacity, String initialVacancies, String newAdmissions, 
				String newDischarges, String SRIAdult, String SRISenior, String uniqueIndividual, String totalSRI, String vacancyDays) {		
			this.dataSRIProgram = dataSriProgram;
			this.dataSRIReportResult.add(programCapacity);
			this.dataSRIReportResult.add(SRICapacity);
			this.dataSRIReportResult.add(initialVacancies);
			this.dataSRIReportResult.add(newAdmissions);
			this.dataSRIReportResult.add(newDischarges);	
			this.dataSRIReportResult.add(SRIAdult);
			this.dataSRIReportResult.add(SRISenior);
			this.dataSRIReportResult.add(uniqueIndividual);
			this.dataSRIReportResult.add(totalSRI);
			this.dataSRIReportResult.add(vacancyDays);
									
		}
	}
	
	private String reportByDescription = null;
	private GregorianCalendar startDate = null;
	private GregorianCalendar endDate = null;
	private List<Program> selectedPrograms = new ArrayList<Program>();
	private List<Admission> admissions = new ArrayList<Admission>();
	private String providerNo = null;
	
	private ArrayList<DataRow> dataRows=null;
	private ArrayList<String> headerRow=new ArrayList<String>();
	
	private ArrayList<DataSPIRow> dataSPIRows = null;
	private ArrayList<String> headerSPIRow = new ArrayList<String>();
	
	private int lessThan5mins_SPI_total = 0;
	private int min5To30_SPI_total = 0;
	private int min31To1Hr_SPI_total = 0;
	private int hr1To2_SPI_total = 0;
	private int hr2To5_SPI_total = 0;
	private int moreThan5hrs_SPI_total = 0;
	private int total_SPI_total = 0;
	private int total_SPI_Reporting_total = 0;
	
	private int lessThan5mins_SPGI_total = 0;
	private int min5To30_SPGI_total = 0;
	private int min31To1Hr_SPGI_total = 0;
	private int hr1To2_SPGI_total = 0;
	private int hr2To5_SPGI_total = 0;
	private int moreThan5hrs_SPGI_total = 0;
	private int total_SPGI_total = 0;
	private int total_SPGI_Reporting_total = 0;
	
	private ArrayList<DataSRIRow> dataSRIRows = null;
	private ArrayList<String> headerSRIRow = new ArrayList<String>();
	
	private int programCapacity_SRI_total = 0; 
	private int SRICapacity_SRI_total = 0;
	private int initialVacancies_SRI_total = 0;
	private int newAdmissions_SRI_total = 0;
	private int newDischarges_SRI_total = 0;
	private int SRIAdult_SRI_total = 0;
	private int SRISenior_SRI_total = 0;
	private int uniqueIndividuals_SRI_total = 0;
	private int totalSRI_SRI_total = 0;
	private int vacancyDays_SRI_total = 0; 
	
	{
		headerRow.add("Id");
		headerRow.add("Description");
		headerRow.add("Results");
		
		headerSPIRow.add("SPI/SPGI");
		headerSPIRow.add("Program");
		headerSPIRow.add("Encounter Type");
		headerSPIRow.add("Less Than 5 Mins");
		headerSPIRow.add("5 Mins to 30 Mins");
		headerSPIRow.add("31 Mins to 1 Hr");
		headerSPIRow.add("1 Hr to 2 Hrs");
		headerSPIRow.add("2 Hrs to 5 Hrs");
		headerSPIRow.add("More Than 5 Hrs");
		headerSPIRow.add("Total");
		headerSPIRow.add("Total for MIS Reporting");
		
		headerSRIRow.add("Program Name");
		headerSRIRow.add("Program Capacity");
		headerSRIRow.add("SRIs at Capacity");
		headerSRIRow.add("Initial Vacancies");
		headerSRIRow.add("Admissions");
		headerSRIRow.add("Discharges");
		headerSRIRow.add("SRI Adult");
		headerSRIRow.add("SRI Senior");
		headerSRIRow.add("Unique Individuals Served");
		headerSRIRow.add("Total SRI");
		headerSRIRow.add("Vacancy Days");
		
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
	
	/**
	 * End dates should be treated as inclusive.
	 */
	public MisReportUIBean(String providerNo, String programType, GregorianCalendar startDate, GregorianCalendar endDate) {

		this.startDate =startDate; 
		this.endDate=endDate;
		this.providerNo = providerNo;
		
		//LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		Provider provider = providerDao.getProvider(providerNo);
		reportByDescription=StringEscapeUtils.escapeHtml(provider.getFormattedName());
		List<String> programIds = caseManagementNoteDAO.getProgramIdsByProviderNoAndObservationDate(providerNo, startDate.getTime(), endDate.getTime());
		for( String programIdString : programIds) {
			int programId=Integer.parseInt(programIdString);
			Program program=programDao.getProgram(programId);
			if(programType.equalsIgnoreCase(program.getType()) && program.getFunctionalCentreId()!=null && program.getFunctionalCentreId().length()>1) { //only service programs which are assigned to one functional centre
				selectedPrograms.add(program);
			}
		}		
		
		populateAdmissions();
		generateDataRows();
	}
	
	public ArrayList<String> getHeaderRow()
	{
		return(headerRow);
	}
	
	public ArrayList<String> getHeaderSPIRow()
	{
		return(headerSPIRow);
	}
	
	public ArrayList<String> getHeaderSRIRow()
	{
		return(headerSRIRow);
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
		if(selectedPrograms.size()>0) {
			for (Program program : selectedPrograms) {
				List<Admission> programAdmissions = admissionDao.getAdmissionsByProgramAndDate(program.getId(), startDate.getTime(), endDate.getTime());
	
				logger.debug("corresponding mis admissions count:" + admissions.size());
	
				for (Admission admission : programAdmissions) {
					admissions.add(admission);
					logger.debug("valid mis admission, id=" + admission.getId());
				}
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
		
		//SPI report
		dataSPIRows = new ArrayList<DataSPIRow>();
		//addBedProgramInfoForSPI(dataSPIRows);
		getSPI(dataSPIRows);
		getSPGI(dataSPIRows);
		
		//SRI report
		dataSRIRows = new ArrayList<DataSRIRow>();
		addBedProgramInfoForSRI(dataSRIRows);
		
	}

	public ArrayList<DataRow> getDataRows()
	{
		return(dataRows);
	}
	
	public ArrayList<DataSPIRow> getDataSPIRows()
	{
		return(dataSPIRows);
	}
	
	public ArrayList<DataSRIRow> getDataSRIRows()
	{
		return(dataSRIRows);
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

	private void addBedProgramInfoForSRI(ArrayList<DataSRIRow> results) {		
		// only pertains to bed programs
		for(Program program : selectedPrograms) {	
			String programName = "";
			int programCapacity = 0; 
			int SRICapacity = 0;
			int initialVacancies = 0;
			int newAdmissions = 0;
			int newDischarges = 0;
			int SRIAdult = 0;
			int SRISenior = 0;
			int uniqueIndividuals = 0;
			int totalSRI = 0;
			int vacancyDays = 0; 			
			
			programName = program.getName();		
			
			if(program.getType().equals(Program.BED_TYPE)) {	
				programCapacity = program.getMaxAllowed();				
				SRICapacity = DateUtils.calculateDayDifference(startDate, endDate) * programCapacity;	
				
				int activeClientsOnStartDate = 0;
				for(Admission admission : admissions) {
					if(admission.getProgramId().intValue() == program.getId().intValue()) {
						int residentDays = calculateResidentDays(admission);
						Demographic demographic = demographicDao.getDemographicById(admission.getClientId());
						Integer age = DateUtils.getAge(demographic.getBirthDay(), endDate);
						
						if (age != null && age.intValue() >= ELDERLY_AGE) {	
							SRISenior = SRISenior + residentDays;
							SRISenior_SRI_total = SRISenior_SRI_total + residentDays;
						} else {
							SRIAdult = SRIAdult + residentDays;
							SRIAdult_SRI_total = SRIAdult_SRI_total + residentDays;
						}
						totalSRI = totalSRI + residentDays;
						
						if(isNewAdmission(admission)) {
							newAdmissions ++;
							newAdmissions_SRI_total ++;
						} else {						
							activeClientsOnStartDate ++;						
						}
						
						if(isNewDischarge(admission)) {
							newDischarges ++;
							newDischarges_SRI_total ++;
						}
					}
				}
				programCapacity_SRI_total = programCapacity_SRI_total + programCapacity;
				SRICapacity_SRI_total = SRICapacity_SRI_total + SRICapacity;
				
				initialVacancies = programCapacity - activeClientsOnStartDate;					
				uniqueIndividuals = activeClientsOnStartDate + newAdmissions;								
				vacancyDays = SRICapacity - totalSRI;				
				
				initialVacancies_SRI_total = initialVacancies_SRI_total + initialVacancies;
				uniqueIndividuals_SRI_total = uniqueIndividuals_SRI_total + uniqueIndividuals;
				totalSRI_SRI_total = totalSRI_SRI_total + totalSRI;
				vacancyDays_SRI_total = vacancyDays_SRI_total + vacancyDays;
				
				results.add(new DataSRIRow(programName, String.valueOf(programCapacity), String.valueOf(SRICapacity), String.valueOf(initialVacancies), String.valueOf(newAdmissions), 
						String.valueOf(newDischarges), String.valueOf(SRIAdult), String.valueOf(SRISenior), String.valueOf(uniqueIndividuals), String.valueOf(totalSRI), String.valueOf(vacancyDays) ));
			
			} else if (program.getType().equals(Program.SERVICE_TYPE)) {
				int activeClientsOnStartDate = 0;
				for(Admission admission : admissions) {
					if(admission.getProgramId().intValue() == program.getId().intValue()) { 
						Demographic demographic = demographicDao.getDemographicById(admission.getClientId());
						if(providerNo!=null && !providerNo.equals(demographic.getProviderNo())) 
							continue;						
						
						if(isNewAdmission(admission)) {
							newAdmissions ++;
							newAdmissions_SRI_total ++;
						} else {						
							activeClientsOnStartDate ++;
						}
						
						if(isNewDischarge(admission)) {
							newDischarges ++;
							newDischarges_SRI_total ++;
						}
					}
				}
				
				//Get notes order by clientId, observation_date asc
				List<CaseManagementNote> notes=caseManagementNoteDAO.getCaseManagementNoteByProgramIdAndObservationDate(program.getId(), startDate.getTime(), endDate.getTime());
				int clientId_previous = 0;				
				String date_previous = null;
				String date_current = null;
				for(CaseManagementNote note : notes) {
					Demographic demographic = demographicDao.getDemographicById(Integer.valueOf(note.getDemographic_no()));
					if(providerNo!=null && !providerNo.equals(demographic.getProviderNo()))
						continue;
					
					if(providerNo!=null && !providerNo.equals(note.getProviderNo()))
						continue;
					
					boolean shouldCount = false;
					if(note.getEncounter_type()!=null && (EncounterUtil.EncounterType.FACE_TO_FACE_WITH_CLIENT.getOldDbValue().equals(note.getEncounter_type()) || 
														EncounterUtil.EncounterType.TELEPHONE_WITH_CLIENT.getOldDbValue().equals(note.getEncounter_type()) )) {
						if(note.getHourOfEncounterTime()==null || note.getHourOfEncounterTime()==0) {
							if(note.getMinuteOfEncounterTime()==null || note.getMinuteOfEncounterTime()<5) {
								continue; //don't count the encounter time <5
							}
						} 						
						
						date_current = note.getObservation_date().toString().substring(0,10);
						
						if(date_previous==null || clientId_previous==0) {							
							shouldCount = true;							
								
						} else if(date_previous!=null && clientId_previous!=Integer.parseInt(note.getDemographic_no()) ) {
							shouldCount = true;	
							
						} else if(date_previous!=null && clientId_previous==Integer.parseInt(note.getDemographic_no()) && !date_previous.equals(date_current)) {
							shouldCount = true;	
							
						} 
						
						if(shouldCount) {	
							Integer age = DateUtils.getAge(demographic.getBirthDay(), endDate);
							
							if (age != null && age.intValue() >= ELDERLY_AGE) {	
								SRISenior ++;	
								SRISenior_SRI_total ++;
								
							} else {
								SRIAdult ++;
								SRIAdult_SRI_total ++;
							}
							totalSRI ++ ;
							
							clientId_previous = Integer.valueOf(note.getDemographic_no()).intValue();
							date_previous = date_current;
							
						} 
						
					}//face to face and telephone
				}//for note
				
												
				uniqueIndividuals = activeClientsOnStartDate + newAdmissions;	
				
				
				initialVacancies_SRI_total = initialVacancies_SRI_total + initialVacancies;
				uniqueIndividuals_SRI_total = uniqueIndividuals_SRI_total + uniqueIndividuals;
				totalSRI_SRI_total = totalSRI_SRI_total + totalSRI;
				//vacancyDays_SRI_total = vacancyDays_SRI_total + vacancyDays;
				
				results.add(new DataSRIRow(programName, "", "", "", String.valueOf(newAdmissions), 
						String.valueOf(newDischarges), String.valueOf(SRIAdult), String.valueOf(SRISenior), String.valueOf(uniqueIndividuals), String.valueOf(totalSRI), "" ));
				
			} //service program
			
		} //for program
		
		results.add(new DataSRIRow("Total", String.valueOf(programCapacity_SRI_total), String.valueOf(SRICapacity_SRI_total), String.valueOf(initialVacancies_SRI_total), String.valueOf(newAdmissions_SRI_total), 
				String.valueOf(newDischarges_SRI_total), String.valueOf(SRIAdult_SRI_total), String.valueOf(SRISenior_SRI_total), String.valueOf(uniqueIndividuals_SRI_total), String.valueOf(totalSRI_SRI_total), String.valueOf(vacancyDays_SRI_total) ));
	
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

	private void getSPI(ArrayList<DataSPIRow> results) {
		for(Program program : selectedPrograms) {
			countEncounterNoteSPI(results, program, EncounterUtil.EncounterType.FACE_TO_FACE_WITH_CLIENT.getOldDbValue());
			countEncounterNoteSPI(results, program, EncounterUtil.EncounterType.TELEPHONE_WITH_CLIENT.getOldDbValue());
			countEncounterNoteSPI(results, program, EncounterUtil.EncounterType.ENCOUNTER_WITH_OUT_CLIENT.getOldDbValue());
			countEncounterNoteSPI(results, program, EncounterUtil.EncounterType.EMAIL_WITH_CLIENT.getOldDbValue());			
		}
		results.add(new DataSPIRow("", "","SPI Total", String.valueOf(lessThan5mins_SPI_total), String.valueOf(min5To30_SPI_total), String.valueOf(min31To1Hr_SPI_total), 
				String.valueOf(hr1To2_SPI_total), String.valueOf(hr2To5_SPI_total), String.valueOf(moreThan5hrs_SPI_total), String.valueOf(total_SPI_total), String.valueOf(total_SPI_Reporting_total)) );		
	
	}
		
	private void countEncounterNoteSPI(ArrayList<DataSPIRow> results, Program program, String encounterType) {
		
		// count face to face notes in the system pertaining to the allowed programs during that time frame.
		int lessThan5mins = 0;
		int min5To30 = 0;
		int min31To1Hr = 0;
		int hr1To2 = 0;
		int hr2To5 = 0;
		int moreThan5hrs = 0;
		int total = 0;
		int total_mis_report = 0;
		
		List<CaseManagementNote> notes=caseManagementNoteDAO.getCaseManagementNoteByProgramIdAndObservationDate(program.getId(), startDate.getTime(), endDate.getTime());
			
		for(CaseManagementNote note : notes) {
			if(providerNo!=null && !providerNo.equals(note.getProviderNo()))
				continue;
			
			if(encounterType!=null && encounterType.equals(note.getEncounter_type())) {
				if(note.getHourOfEncounterTime()==null || note.getHourOfEncounterTime()==0) {
					if(note.getMinuteOfEncounterTime()==null || note.getMinuteOfEncounterTime()<5) {
						lessThan5mins ++;
						lessThan5mins_SPI_total ++;
						if(encounterType!=null && (EncounterUtil.EncounterType.FACE_TO_FACE_WITH_CLIENT.getOldDbValue().equals(encounterType) || 
													EncounterUtil.EncounterType.TELEPHONE_WITH_CLIENT.getOldDbValue().equals(encounterType) ) ) {
							total ++;
							total_SPI_total ++;							
						}
					} else if(note.getMinuteOfEncounterTime()<=30) {
						min5To30 ++;
						total ++;
						min5To30_SPI_total ++;
						total_SPI_total ++;
						
						if(encounterType!=null && (EncounterUtil.EncounterType.FACE_TO_FACE_WITH_CLIENT.getOldDbValue().equals(encounterType) || 
								EncounterUtil.EncounterType.TELEPHONE_WITH_CLIENT.getOldDbValue().equals(encounterType) ) ) {
							total_mis_report ++;						
							total_SPI_Reporting_total ++;
						}
					} else {
						min31To1Hr ++;
						total ++;
						min31To1Hr_SPI_total ++;
						total_SPI_total ++;
						
						if(encounterType!=null && (EncounterUtil.EncounterType.FACE_TO_FACE_WITH_CLIENT.getOldDbValue().equals(encounterType) || 
								EncounterUtil.EncounterType.TELEPHONE_WITH_CLIENT.getOldDbValue().equals(encounterType) ) ) {
							total_mis_report ++;
							total_SPI_Reporting_total ++;
						}
					}
				} else if(note.getHourOfEncounterTime()<=2) {
					hr1To2 ++;
					total ++;
					hr1To2_SPI_total ++;
					total_SPI_total ++;
					
					if(encounterType!=null && (EncounterUtil.EncounterType.FACE_TO_FACE_WITH_CLIENT.getOldDbValue().equals(encounterType) || 
							EncounterUtil.EncounterType.TELEPHONE_WITH_CLIENT.getOldDbValue().equals(encounterType) ) ) {
						total_mis_report ++;
						total_SPI_Reporting_total ++;
					}
					
				} else if(note.getHourOfEncounterTime()<=5) {
					hr2To5 ++;
					total ++;
					hr2To5_SPI_total ++;
					total_SPI_total ++;
					
					if(encounterType!=null && (EncounterUtil.EncounterType.FACE_TO_FACE_WITH_CLIENT.getOldDbValue().equals(encounterType) || 
							EncounterUtil.EncounterType.TELEPHONE_WITH_CLIENT.getOldDbValue().equals(encounterType) ) ) {
						total_mis_report ++;
						total_SPI_Reporting_total ++;
					}
				} else {
					moreThan5hrs ++;
					moreThan5hrs_SPI_total ++;
					total ++;
					total_SPI_total ++;
					
					if(encounterType!=null && (EncounterUtil.EncounterType.FACE_TO_FACE_WITH_CLIENT.getOldDbValue().equals(encounterType) || 
							EncounterUtil.EncounterType.TELEPHONE_WITH_CLIENT.getOldDbValue().equals(encounterType) ) ) {
						total_mis_report ++;
						total_SPI_Reporting_total ++;
					}
				}
					
			}			
				
		}
		
		if(EncounterUtil.EncounterType.ENCOUNTER_WITH_OUT_CLIENT.getOldDbValue().equals(encounterType) || 
											EncounterUtil.EncounterType.EMAIL_WITH_CLIENT.getOldDbValue().equals(encounterType) )  {
			results.add(new DataSPIRow("SPI", program.getName(),encounterType, String.valueOf(lessThan5mins), String.valueOf(min5To30), String.valueOf(min31To1Hr), 
					String.valueOf(hr1To2), String.valueOf(hr2To5), String.valueOf(moreThan5hrs), String.valueOf(total), "Not Reported") );				
		} else {
			results.add(new DataSPIRow("SPI", program.getName(),encounterType, String.valueOf(lessThan5mins), String.valueOf(min5To30), String.valueOf(min31To1Hr), 
					String.valueOf(hr1To2), String.valueOf(hr2To5), String.valueOf(moreThan5hrs), String.valueOf(total), String.valueOf(total_mis_report)) );		
		}
	}	
	
	private void getSPGI(ArrayList<DataSPIRow> results) {
		for(Program program : selectedPrograms) {
			countEncounterNoteSPGI(results, program, EncounterUtil.EncounterType.FACE_TO_FACE_WITH_CLIENT.getOldDbValue());
			countEncounterNoteSPGI(results, program, EncounterUtil.EncounterType.TELEPHONE_WITH_CLIENT.getOldDbValue());			
		}
		results.add(new DataSPIRow("", "", "SPGI Total", String.valueOf(lessThan5mins_SPGI_total), String.valueOf(min5To30_SPGI_total), String.valueOf(min31To1Hr_SPGI_total), 
				String.valueOf(hr1To2_SPGI_total), String.valueOf(hr2To5_SPGI_total), String.valueOf(moreThan5hrs_SPGI_total), String.valueOf(total_SPGI_total), String.valueOf(total_SPGI_Reporting_total)) );		
	
	}
	
	private void countEncounterNoteSPGI(ArrayList<DataSPIRow> results, Program program, String encounterType) {
		
		// count face to face notes in the system pertaining to the allowed programs during that time frame.
		int lessThan5mins = 0;
		int min5To30 = 0;
		int min31To1Hr = 0;
		int hr1To2 = 0;
		int hr2To5 = 0;
		int moreThan5hrs = 0;
		int total = 0;
		int total_mis_report = 0;
		
		List<CaseManagementNote> notes=caseManagementNoteDAO.getCaseManagementNoteByProgramIdAndObservationDate(program.getId(), startDate.getTime(), endDate.getTime());
			
		for(CaseManagementNote note : notes) {
			if(providerNo!=null && !providerNo.equals(note.getProviderNo()))
				continue;
			
			if(encounterType!=null && encounterType.equals(note.getEncounter_type())) {
				List<GroupNoteLink> groupNotesLink = groupNoteLinkDao.getGroupNotesByNoteId(Integer.valueOf(note.getId().intValue()));
				int groupNotesCount = groupNotesLink.size();
				if(note.getHourOfEncounterTime()==null || note.getHourOfEncounterTime()==0) {
					if(note.getMinuteOfEncounterTime()==null || note.getMinuteOfEncounterTime()<5) {						
						lessThan5mins = lessThan5mins + groupNotesCount;	
						lessThan5mins_SPGI_total = lessThan5mins_SPGI_total +  groupNotesCount;						
						
					} else if(note.getMinuteOfEncounterTime()<=30) {											
						min5To30 = min5To30 + groupNotesCount;						
						min5To30_SPGI_total = min5To30_SPGI_total + groupNotesCount;	
						total_mis_report = total_mis_report + groupNotesCount;
						total_SPGI_Reporting_total = total_SPGI_Reporting_total + groupNotesCount;
						
					} else {
						min31To1Hr = min31To1Hr + groupNotesCount ;						
						min31To1Hr_SPGI_total = min31To1Hr_SPGI_total + groupNotesCount;
						total_mis_report = total_mis_report + groupNotesCount;
						total_SPGI_Reporting_total = total_SPGI_Reporting_total + groupNotesCount;
										
					}
				} else if(note.getHourOfEncounterTime()<=2) {					
					hr1To2 = hr1To2 + groupNotesCount;					
					hr1To2_SPGI_total = hr1To2_SPGI_total + groupNotesCount;	
					total_mis_report = total_mis_report + groupNotesCount;
					total_SPGI_Reporting_total = total_SPGI_Reporting_total + groupNotesCount;
					
					
				} else if(note.getHourOfEncounterTime()<=5) {					
					hr2To5 = hr2To5 + groupNotesCount;					
					hr2To5_SPGI_total = hr2To5_SPGI_total + groupNotesCount;	
					total_mis_report = total_mis_report + groupNotesCount;
					total_SPGI_Reporting_total = total_SPGI_Reporting_total + groupNotesCount;
					
				} else {					
					moreThan5hrs = moreThan5hrs + groupNotesCount;
					moreThan5hrs_SPGI_total = moreThan5hrs_SPGI_total + groupNotesCount;	
					total_mis_report = total_mis_report + groupNotesCount;
					total_SPGI_Reporting_total = total_SPGI_Reporting_total + groupNotesCount;
				}
					
				total = total + groupNotesCount;				
				total_SPGI_total = total_SPGI_total + groupNotesCount;
								
			}	
		}
		
		results.add(new DataSPIRow("SPGI", program.getName(),encounterType, String.valueOf(lessThan5mins), String.valueOf(min5To30), String.valueOf(min31To1Hr), 
					String.valueOf(hr1To2), String.valueOf(hr2To5), String.valueOf(moreThan5hrs), String.valueOf(total), String.valueOf(total_mis_report)) );		
		
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
