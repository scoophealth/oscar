/**
 * Copyright (c) 2013. Department of Family Practice, University of British Columbia. All Rights Reserved.
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
 * Department of Family Practice
 * Faculty of Medicine
 * University of British Columbia
 * Vancouver, Canada
 */
package org.oscarehr.export;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.casemgmt.dao.CaseManagementIssueDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteExtDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.common.dao.AllergyDao;
import org.oscarehr.common.dao.CaseManagementIssueNotesDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.dao.Icd9Dao;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.MeasurementTypeDao;
import org.oscarehr.common.dao.MeasurementsExtDao;
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.dao.PreventionExtDao;
import org.oscarehr.common.dao.ProviderDataDao;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.Hl7TextInfo;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.MeasurementType;
import org.oscarehr.common.model.MeasurementsExt;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.PreventionExt;
import org.oscarehr.common.model.ProviderData;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

/**
 * Models a "patient" which bundles all data required to define a "patient" for export
 * 
 * @author Jeremy Ho
 */
public class PatientExport {
	private static Logger log = MiscUtils.getLogger();
	private static DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	private static AllergyDao allergyDao = SpringUtils.getBean(AllergyDao.class);
	private static DrugDao drugDao = SpringUtils.getBean(DrugDao.class);
	private static PreventionDao preventionDao = SpringUtils.getBean(PreventionDao.class);
	private static PreventionExtDao preventionExtDao = SpringUtils.getBean(PreventionExtDao.class);
	private static ProviderDataDao providerDataDao = SpringUtils.getBean(ProviderDataDao.class);
	private static DxresearchDAO dxResearchDao = SpringUtils.getBean(DxresearchDAO.class);
	private static Icd9Dao icd9Dao = SpringUtils.getBean(Icd9Dao.class);
	private static PatientLabRoutingDao patientLabRoutingDao = SpringUtils.getBean(PatientLabRoutingDao.class);
	private static Hl7TextInfoDao hl7TextInfoDao = SpringUtils.getBean(Hl7TextInfoDao.class);
	private static IssueDAO issueDao = SpringUtils.getBean(IssueDAO.class);
	private static MeasurementDao measurementDao = SpringUtils.getBean(MeasurementDao.class);
	private static MeasurementsExtDao measurementsExtDao = SpringUtils.getBean(MeasurementsExtDao.class);
	private static MeasurementTypeDao measurementTypeDao = SpringUtils.getBean(MeasurementTypeDao.class);
	private static CaseManagementIssueDAO caseManagementIssueDao = SpringUtils.getBean(CaseManagementIssueDAO.class);
	private static CaseManagementIssueNotesDao caseManagementIssueNotesDao = SpringUtils.getBean(CaseManagementIssueNotesDao.class);
	private static CaseManagementNoteDAO caseManagementNoteDao = SpringUtils.getBean(CaseManagementNoteDAO.class);
	private static CaseManagementNoteExtDAO caseManagementNoteExtDao = SpringUtils.getBean(CaseManagementNoteExtDAO.class);

	//private static final long OTHERMEDS = getIssueID("OMeds");
	private static final long SOCIALHISTORY = getIssueID("SocHistory");
	//private static final long MEDICALHISTORY = getIssueID("MedHistory");
	//private static final long ONGOINGCONCERNS = getIssueID("Concerns");
	private static final long REMINDERS = getIssueID("Reminders");
	private static final long FAMILYHISTORY = getIssueID("FamHistory");
	private static final long RISKFACTORS = getIssueID("RiskFactors");

	private Date currentDate = new Date();
	//private String authorId = LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo();
	private String authorId = null;
	private Integer demographicNo = null;
	private Demographic demographic = null;
	private List<Drug> drugs = null;
	private List<Allergy> allergies = null;
	private List<Prevention> preventions = null;
	private List<Dxresearch> problems = null;
	private List<Lab> labs = null;
	private List<CaseManagementNote> riskFactors = null;
	private List<CaseManagementNote> familyHistory = null;
	private List<CaseManagementNote> alerts = null;
	private List<Measurement> measurements = null;

	private boolean isLoaded = false;
	private boolean exMedicationsAndTreatments = false;
	private boolean exAllergiesAndAdverseReactions = false;
	private boolean exImmunizations = false;
	private boolean exProblemList = false;
	private boolean exLaboratoryResults = false;
	private boolean exRiskFactors = false;
	private boolean exPersonalHistory = false;
	private boolean exFamilyHistory = false;
	private boolean exAlertsAndSpecialNeeds = false;

	/**
	 * Constructs an empty Patient Export object
	 */
	public PatientExport() {}

	/**
	 * Attempts to load the specified patient.
	 * This function must be called AFTER the sections are set AND before exporting
	 * 
	 * @param demoNo as String
	 * @return True if successful, else false
	 */
	public boolean loadPatient(String demoNo) {
		if(isLoaded) {
			log.error("PatientExport object is already loaded with a patient");
			return false;
		}

		this.demographicNo = new Integer(demoNo);
		this.demographic = demographicDao.getDemographic(demoNo);
		this.authorId = demographic.getProviderNo();

		if(exAllergiesAndAdverseReactions) {
			this.allergies = allergyDao.findAllergies(demographicNo);
		}

		if(exMedicationsAndTreatments) {
			this.drugs = drugDao.findByDemographicId(demographicNo);

			// Sort drugs by reverse chronological order & group by DIN by sorting
			Collections.reverse(drugs);
			Collections.sort(drugs, new sortByDin());
		}

		if(exImmunizations) {
			this.preventions = preventionDao.findNotDeletedByDemographicId(demographicNo);
		}

		if(exProblemList) {
			List <Dxresearch> tempProblems = dxResearchDao.getDxResearchItemsByPatient(demographicNo);
			this.problems = new ArrayList<Dxresearch>();
			for(Dxresearch problem : tempProblems) {
				if(problem.getStatus() != 'D' && problem.getCodingSystem().equals("icd9")) {
					this.problems.add(problem);
				}
			}
		}

		//TODO Temporarily hooked into Lab Results checkbox - consider creating unique checkbox on UI down the road
		if(exLaboratoryResults) {
			this.labs = assembleLabs();
			this.measurements = parseMeasurements();
		}

		if(exRiskFactors || exPersonalHistory || exFamilyHistory || exAlertsAndSpecialNeeds) {
			parseCaseManagement();
		}

		this.isLoaded = true;
		log.debug("Loaded Demo: " + demographicNo.toString());
		return true;
	}

	/**
	 * @param rhs
	 * @return issueID as long
	 */
	private static long getIssueID(String rhs) {
		long answer;
		try {
			answer = issueDao.findIssueByCode(rhs).getId();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			answer = 0;
		}
		return answer;
	}

	private List<Measurement> parseMeasurements() {
		// Gather and filter measurements based on lack of lab_no field
		List<Measurement> rawMeasurements = measurementDao.findByDemographicNo(demographicNo);
		List<Measurement> tempMeasurements = new ArrayList<Measurement>();
		for(Measurement entry : rawMeasurements) {
			MeasurementsExt isFromLab = measurementsExtDao.getMeasurementsExtByMeasurementIdAndKeyVal(entry.getId(), "lab_no");
			if(isFromLab == null) {
				tempMeasurements.add(entry);
			}
		}
		return tempMeasurements;
	}

	private void parseCaseManagement() {
		// Gather all Case Management data
		List<CaseManagementIssue> caseManagementIssues = caseManagementIssueDao.getIssuesByDemographic(demographicNo.toString());
		List<String> cmRiskFactorIssues = new ArrayList<String>();
		List<String> cmFamilyHistoryIssues = new ArrayList<String>();
		List<String> cmAlertsIssues = new ArrayList<String>();

		for(CaseManagementIssue entry : caseManagementIssues) {
			if(exRiskFactors && entry.getIssue_id() == RISKFACTORS) {
				cmRiskFactorIssues.add(entry.getId().toString());
			}
			else if(exPersonalHistory && entry.getIssue_id() == SOCIALHISTORY) {
				cmRiskFactorIssues.add(entry.getId().toString());
			}
			else if(exFamilyHistory && entry.getIssue_id() == FAMILYHISTORY) {
				cmFamilyHistoryIssues.add(entry.getId().toString());
			}
			else if(exAlertsAndSpecialNeeds && entry.getIssue_id() == REMINDERS) {
				cmAlertsIssues.add(entry.getId().toString());
			}
		}

		if(exRiskFactors || exPersonalHistory) {
			List<Integer> cmRiskFactorNotes = caseManagementIssueNotesDao.getNoteIdsWhichHaveIssues(cmRiskFactorIssues.toArray(new String[cmRiskFactorIssues.size()]));
			List<Long> cmRiskFactorNotesLong = new ArrayList<Long>();
			if(cmRiskFactorNotes != null) {
				for(Integer i : cmRiskFactorNotes) {
					cmRiskFactorNotesLong.add(Long.parseLong(String.valueOf(i)));
				}
			}
			this.riskFactors = caseManagementNoteDao.getNotes(cmRiskFactorNotesLong);
		}

		if(exFamilyHistory) {
			List<Integer> cmFamilyHistoryNotes = caseManagementIssueNotesDao.getNoteIdsWhichHaveIssues(cmFamilyHistoryIssues.toArray(new String[cmFamilyHistoryIssues.size()]));
			List<Long> cmFamilyHistoryNotesLong = new ArrayList<Long>();
			if(cmFamilyHistoryNotes != null) {
				for(Integer i : cmFamilyHistoryNotes) {
					cmFamilyHistoryNotesLong.add(Long.parseLong(String.valueOf(i)));
				}
			}
			this.familyHistory = caseManagementNoteDao.getNotes(cmFamilyHistoryNotesLong);
		}

		if(exAlertsAndSpecialNeeds) {
			List<Integer> cmAlertsNotes = caseManagementIssueNotesDao.getNoteIdsWhichHaveIssues(cmAlertsIssues.toArray(new String[cmAlertsIssues.size()]));
			List<Long> cmAlertsNotesLong = new ArrayList<Long>();
			if(cmAlertsNotes != null) {
				for(Integer i : cmAlertsNotes) {
					cmAlertsNotesLong.add(Long.parseLong(String.valueOf(i)));
				}
			}
			this.alerts = caseManagementNoteDao.getNotes(cmAlertsNotesLong);
		}
	}

	private List<Lab> assembleLabs() {
		// Gather hl7TextInfo labs
		List<PatientLabRouting> tempRouting = patientLabRoutingDao.findByDemographicAndLabType(demographicNo, "HL7");
		List<Hl7TextInfo> tempLabs = new ArrayList<Hl7TextInfo>();
		for(PatientLabRouting routing : tempRouting) {
			Hl7TextInfo temp = hl7TextInfoDao.findLabId(routing.getLabNo());
			if(temp != null) {
				tempLabs.add(temp);
			}
		}

		// Short circuit if no labs
		if(tempLabs.size() < 1)
			return null;

		// Gather and filter measurements based on existence of lab_no field
		List<Measurement> rawMeasurements = measurementDao.findByDemographicNo(demographicNo);
		List<Measurement> tempMeasurements = new ArrayList<Measurement>();
		for(Measurement entry : rawMeasurements) {
			MeasurementsExt isFromLab = measurementsExtDao.getMeasurementsExtByMeasurementIdAndKeyVal(entry.getId(), "lab_no");
			if(isFromLab != null && isValidLabMeasurement(tempRouting, isFromLab.getVal())) {
				tempMeasurements.add(entry);
			}
		}

		// Gather measurementsExt
		List<List<MeasurementsExt>> tempMeasurementsExt = new ArrayList<List<MeasurementsExt>>();
		for(Measurement entry : tempMeasurements) {
			List<MeasurementsExt> tempMeasurementsExtElement = measurementsExtDao.getMeasurementsExtByMeasurementId(entry.getId());
			tempMeasurementsExt.add(tempMeasurementsExtElement);
		}

		// Create Lab Objects
		List<Lab> allLabs = new ArrayList<Lab>();

		// Group Measurements into Lab Objects
		for(Hl7TextInfo labReport : tempLabs) {
			Lab labObj = new Lab();
			labObj.hl7TextInfo = labReport;

			// Group Measurements by Lab Number
			int labNumber = labReport.getLabNumber();
			List<Measurement> labMeasurementAll = new ArrayList<Measurement>();
			List<List<MeasurementsExt>> labMeasurementsExtAll = new ArrayList<List<MeasurementsExt>>();

			for(int i=0; i < tempMeasurementsExt.size(); i++) {
				List<MeasurementsExt> entry = tempMeasurementsExt.get(i);
				String entryLabNo = getLabExtValue(entry, "lab_no");

				// Add related entries to correct Lab
				if(labNumber == Integer.valueOf(entryLabNo)) {
					labMeasurementsExtAll.add(entry);
					entry.get(0).getMeasurementId();
					Measurement entryMeasurement = tempMeasurements.get(i);
					labMeasurementAll.add(entryMeasurement);
				}
			}

			// Group Measurements into Organizer Groups
			int prevGroup = 0;
			LabGroup tempGroup = new LabGroup(prevGroup);
			for(int i=0; i < labMeasurementsExtAll.size(); i++) {
				String temp = getLabExtValue(labMeasurementsExtAll.get(i), "other_id");
				if(temp != "" && temp != null) {
					int currGroup = parseOtherID(temp)[0];

					// Create New Group
					if(prevGroup != currGroup) {
						labObj.group.add(tempGroup);
						prevGroup = currGroup;
						tempGroup = new LabGroup(prevGroup);
					}
				}

				// Add current measurement to Organizer Group
				tempGroup.measurement.add(labMeasurementAll.get(i));
				tempGroup.measurementsExt.add(labMeasurementsExtAll.get(i));
			}

			// Save final Group
			labObj.group.add(tempGroup);

			// Save Lab Object
			allLabs.add(labObj);
		}

		return allLabs;
	}

	/**
	 * @param routing
	 * @param lab_no
	 * @return True if valid Lab Measurement, else false
	 */
	private boolean isValidLabMeasurement(List<PatientLabRouting> routing, String lab_no) {
		int labNo = Integer.parseInt(lab_no);
		for(PatientLabRouting entry : routing) {
			if(entry.getLabNo() == labNo) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Section Booleans
	 */
	public void setExMedications(boolean rhs) {
		this.exMedicationsAndTreatments = rhs;
	}

	public void setExAllergiesAndAdverseReactions(boolean rhs) {
		this.exAllergiesAndAdverseReactions = rhs;
	}

	public void setExImmunizations(boolean rhs) {
		this.exImmunizations = rhs;
	}

	public void setExProblemList(boolean rhs) {
		this.exProblemList = rhs;
	}

	public void setExLaboratoryResults(boolean rhs) {
		this.exLaboratoryResults = rhs;
	}

	public void setExRiskFactors(boolean rhs) {
		this.exRiskFactors = rhs;
	}

	public void setExPersonalHistory(boolean rhs) {
		this.exPersonalHistory = rhs;
	}

	public void setExFamilyHistory(boolean rhs) {
		this.exFamilyHistory = rhs;
	}

	public void setExAlertsAndSpecialNeeds(boolean rhs) {
		this.exAlertsAndSpecialNeeds = rhs;
	}

	public void setExAllTrue() {
		this.exMedicationsAndTreatments = true;
		this.exAllergiesAndAdverseReactions = true;
		this.exImmunizations = true;
		this.exProblemList = true;
		this.exLaboratoryResults = true;
		this.exRiskFactors = true;
		this.exPersonalHistory = true;
		this.exFamilyHistory = true;
		this.exAlertsAndSpecialNeeds = true;
	}

	/*
	 * Demographics
	 */
	public Demographic getDemographic() {
		return demographic;
	}

	// Output convenience functions
	public String getBirthDate() {
		String out = demographic.getYearOfBirth() + demographic.getMonthOfBirth() + demographic.getDateOfBirth();
		if(isNumeric(out)) {
			return out;
		}
		return "00000000";
	}

	public String getGenderDesc() {
		if(demographic.getSex().equals("M")) return "Male";
		else return "Female";
	}

	/*
	 * Allergies
	 */
	public List<Allergy> getAllergies() {
		return allergies;
	}

	public boolean hasAllergies() {
		return exAllergiesAndAdverseReactions && allergies!=null && !allergies.isEmpty();
	}

	/*
	 * Clinical Measured Observations
	 */
	public List<Measurement> getMeasurements() {
		return measurements;
	}

	//TODO Temporarily hooked into Lab Results checkbox - consider creating unique checkbox on UI down the road
	public boolean hasMeasurements() {
		return exLaboratoryResults && measurements!=null && !measurements.isEmpty();
	}

	public String getTypeDescription(String rhs) {
		try {
			List<MeasurementType> measurementType = measurementTypeDao.findByType(rhs);
			for(MeasurementType entry : measurementType) {
				return entry.getTypeDescription();
			}
		} catch (Exception e) {
			log.warn("getTypeDescription - Type description not found");
		}
		return rhs;
	}

	/*
	 * Immunizations
	 */
	public List<Prevention> getImmunizations() {
		return preventions;
	}

	public boolean hasImmunizations() {
		return exImmunizations && preventions!=null && !preventions.isEmpty();
	}

	/**
	 * Function to allow access to PreventionsExt table data based on prevention id
	 * 
	 * @param id
	 * @param keyval
	 * @return String containing result, or empty string if not available
	 */
	public static String getImmuExtValue(String id, String keyval) {
		try {
			List<PreventionExt> preventionExts = preventionExtDao.findByPreventionIdAndKey(Integer.valueOf(id), keyval);
			for (PreventionExt preventionExt : preventionExts) {
				return preventionExt.getVal();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "";
	}

	/*
	 * Laboratory Reports
	 */
	public List<Lab> getLabs() {
		return labs;
	}

	public boolean hasLabs() {
		return exLaboratoryResults && labs!=null && !labs.isEmpty();
	}

	/**
	 * Function to allow access to LabsExt table data based on prevention id (direct version)
	 * 
	 * @param id
	 * @param keyval
	 * @return String containing result, or empty string if not available
	 */
	public static String getLabExtValue(String id, String keyval) {
		try {
			List<MeasurementsExt> measurementExt= measurementsExtDao.getMeasurementsExtByMeasurementId(Integer.valueOf(id));
			for (MeasurementsExt entry : measurementExt) {
				if(entry.getKeyVal().equals(keyval)) {
					return entry.getVal();
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "";
	}

	/**
	 * Function to allow access to LabsExt table data based on prevention id (object version)
	 * 
	 * @param measurementExt
	 * @param keyval
	 * @return String containing result, or empty string if not available
	 */
	public static String getLabExtValue(List<MeasurementsExt> measurementExt, String keyval) {
		for (MeasurementsExt entry : measurementExt) {
			if(entry.getKeyVal().equals(keyval)) {
				return entry.getVal();
			}
		}
		return "";
	}

	/**
	 * Handles Other ID field parsing
	 * 
	 * @param rhs
	 * @return Integer x of string "x-y"
	 */
	private Integer[] parseOtherID(String rhs) {
		Integer[] lhs = null;
		try {
			String[] temp = rhs.split("-");
			lhs = new Integer[temp.length];
			for(int i=0; i < temp.length; i++) {
				lhs[i] = Integer.parseInt(temp[i]);
			}
		} catch (Exception e) {
			log.error("parseOtherID - other_id field not in expected format");
		}

		return lhs;
	}

	/**
	 * Nested Lab Object Model
	 * 
	 * @author Jeremy Ho
	 */
	public static class Lab {
		public Hl7TextInfo hl7TextInfo;
		public List<LabGroup> group = new ArrayList<LabGroup>();

		public Hl7TextInfo getHl7TextInfo() {
			return hl7TextInfo;
		}

		public List<LabGroup> getGroup() {
			return group;
		}
	}

	/**
	 * Lab Group Object Model
	 * 
	 * @author Jeremy Ho
	 */
	public static class LabGroup {
		public int id;
		public List<Measurement> measurement = new ArrayList<Measurement>();
		public List<List<MeasurementsExt>> measurementsExt = new ArrayList<List<MeasurementsExt>>();

		public LabGroup(int id) {
			this.id = id;
		}

		public int getGroupId() {
			return id;
		}

		public List<Measurement> getMeasurement() {
			return measurement;
		}

		public List<List<MeasurementsExt>> getMeasurementsExt() {
			return measurementsExt;
		}
	}

	/*
	 * Medications
	 */
	public List<Drug> getMedications() {
		return drugs;
	}

	public boolean hasMedications() {
		return exMedicationsAndTreatments && drugs!=null && !drugs.isEmpty();
	}

	/**
	 * Checks if drug is "active" by comparing to current date
	 * 
	 * @param rhs
	 * @return True if active, false if not
	 */
	public boolean isActiveDrug(Date rhs) {
		if(currentDate.after(rhs)) return false;
		else return true;
	}

	/**
	 * Allows for collection sort by DIN numbers
	 * 
	 * @author Jeremy Ho
	 *
	 */
	public class sortByDin implements Comparator<Drug> {
		public int compare(Drug one, Drug two) {
			int answer;
			try {
				answer = Integer.parseInt(one.getRegionalIdentifier()) - Integer.parseInt(two.getRegionalIdentifier());
			} catch (Exception e){
				answer = 0;
			}
			return answer;
		}
	}

	/*
	 * Problem List
	 */
	public List<Dxresearch> getProblems() {
		return problems;
	}

	public boolean hasProblems() {
		return exProblemList && problems!=null && !problems.isEmpty();
	}

	/**
	 * Function to allow access to ICD9 Description table data based on ICD9 code
	 * 
	 * @param code
	 * @return String representing the ICD9 Code's description
	 */
	public static String getICD9Description(String code) {
		String result = icd9Dao.findByCode(code).getDescription();
		if (result == null || result.isEmpty()) {
			return "";
		}

		return result;
	}

	/*
	 * Case Management Utility Functions
	 */
	/**
	 * Function to allow access to Casemanagement Note Ext table data based on note id
	 * 
	 * @param id
	 * @param keyval
	 * @return String of result if available, otherwise empty string
	 */
	public static String getCMNoteExtValue(String id, String keyval) {
		List<CaseManagementNoteExt> cmNoteExts = caseManagementNoteExtDao.getExtByNote(Long.valueOf(id));
		for(CaseManagementNoteExt entry : cmNoteExts) {
			if(entry.getKeyVal().equals(keyval)) {
				return entry.getValue();
			}
		}
		return "";
	}

	/* 
	 * Risk Factors (and Personal History)
	 * Both sections map into the Risk Factors section of the export
	 */
	public List<CaseManagementNote> getRiskFactorsandPersonalHistory() {
		return riskFactors;
	}

	public boolean hasRiskFactorsandPersonalHistory() {
		return (exRiskFactors || exPersonalHistory) && riskFactors!=null && !riskFactors.isEmpty();
	}

	/*
	 * Family History
	 */
	public List<CaseManagementNote> getFamilyHistory() {
		return familyHistory;
	}

	public boolean hasFamilyHistory() {
		return exFamilyHistory && familyHistory!=null && !familyHistory.isEmpty();
	}

	/*
	 * Alerts
	 */
	public List<CaseManagementNote> getAlerts() {
		return alerts;
	}

	public boolean hasAlerts() {
		return exAlertsAndSpecialNeeds && alerts!=null && !alerts.isEmpty();
	}

	/*
	 * Utility
	 */
	public Date getCurrentDate() {
		return currentDate;
	}

	/**
	 * Checks if this object is loaded with patient data
	 * 
	 * @return True if loaded, else false
	 */
	public boolean isLoaded() {
		return this.isLoaded;
	}

	/**
	 * Takes in string dates in multiple possible formats and returns a date object of that time
	 * 
	 * @param rhs
	 * @return Date represented by the string if possible, else return current time
	 */
	public static Date stringToDate(String rhs) {
		String[] formatStrings = {"yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd hh:mm", "yyyy-MM-dd"};
		for(String format : formatStrings) {
			try {
				return new SimpleDateFormat(format).parse(rhs);
			} catch (Exception e) {}
		}
		log.warn("stringToDate - Can't parse " + rhs);
		return new Date();
	}

	/**
	 * Check if string is valid numeric
	 * 
	 * @param rhs
	 * @return True if rhs is a number, else false
	 */
	public static boolean isNumeric(String rhs) {
		try {
			Double.parseDouble(rhs);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Remove invalid characters and formatting from strings
	 * 
	 * @param rhs
	 * @return String without invalid characters
	 */
	public static String cleanString(String rhs) {
		String eol = System.getProperty("line.separator");
		String str = rhs.replaceAll("<br( )+/>", eol);
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		return str;
	}

	public String getAuthorId() {
		if (authorId.length() < 1 || authorId == null) {
			return "0";
		}
		return authorId;
	}

	/**
	 * Get Provider's First Name based on provider number
	 * 
	 * @param providerNo
	 * @return String of name if available, else empty string
	 */
	public String getProviderFirstName(String providerNo) {
		String name;
		try {
			ProviderData providerData = providerDataDao.findByProviderNo(providerNo);
			name = providerData.getFirstName();
		} catch (Exception e) {
			log.warn("getProviderFirstName - Provider not found");
			name = "";
		}
		return name;
	}

	/**
	 * Get Provider's Last Name based on provider number
	 * 
	 * @param providerNo
	 * @return String of name if available, else empty string
	 */
	public String getProviderLastName(String providerNo) {
		String name;
		try {
			ProviderData providerData = providerDataDao.findByProviderNo(providerNo);
			name = providerData.getLastName();
		} catch (Exception e) {
			log.warn("getProviderLastName - Provider not found");
			name = "";
		}
		return name;
	}
}
