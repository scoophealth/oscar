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
package org.oscarehr.exports.e2e;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.Hl7TextInfo;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.MeasurementsExt;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.PreventionExt;
import org.oscarehr.exports.PatientExport;

/**
 * Models a "patient" which bundles all data required to define a "patient" for export
 * 
 * @author Jeremy Ho
 */
public class E2EPatientExport extends PatientExport {
	//private String authorId = LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo();
	private String authorId = null;
	private String patientStatus = "";
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

	/**
	 * Constructs an empty Patient Export object
	 */
	public E2EPatientExport() {
		super();
	}

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
		if(this.demographic == null) {
			log.error("Demographic ".concat(demoNo).concat(" can't be loaded"));
			return false;
		}

		this.patientStatus = demographic.getPatientStatus();
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
		log.debug("Loaded Demo: ".concat(demographicNo.toString()));
		return true;
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

		return splitBloodPressureMeasurements(tempMeasurements);
	}

	private List<Measurement> splitBloodPressureMeasurements(List<Measurement> entries) {
		List<Measurement> output = new ArrayList<Measurement>();

		// For all BP measurements, attempt to split into Systolic and Diastolic
		// Assumes 2 integer numbers exist in the data - first is systolic, second is diastolic
		for(Measurement entry : entries) {
			if(entry.getType().equals("BP")) {
				Pattern pattern = Pattern.compile("\\d+");
				Matcher matcher = pattern.matcher(entry.getDataField());
				List<Integer> values = new ArrayList<Integer>();

				while(matcher.find()) {
					values.add(Integer.parseInt(matcher.group()));
				}

				if(values.size() == 2) {
					Measurement entry2 = null;
					try {
						// Make a copy of measurement object
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ObjectOutputStream out = new ObjectOutputStream(bos);
						out.writeObject(entry);
						out.flush();
						out.close();
						ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
						entry2 = (Measurement)in.readObject();
					}
					catch(Exception e) {
						log.error(e.getMessage(), e);
					}

					if(entry2 != null) {
						// Add split up Systolic/Diastolic values to objects
						entry.setType("DIAS");
						entry.setDataField(values.get(1).toString());

						entry2.setType("SYST");
						entry2.setDataField(values.get(0).toString());
						output.add(entry2);
					}
				}
			}

			output.add(entry);
		}

		return output;
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
				if(temp != null && !temp.isEmpty()) {
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
	 * Demographics
	 */
	public Demographic getDemographic() {
		return demographic;
	}

	public String getAuthorId() {
		if (authorId.length() < 1 || authorId == null) {
			return "0";
		}
		return authorId;
	}

	public boolean isActive() {
		return patientStatus.equals("AC");
	}

	// Output convenience functions
	public String getBirthDate() {
		String out = demographic.getYearOfBirth() + demographic.getMonthOfBirth() + demographic.getDateOfBirth();
		if(isNumeric(out)) {
			return out;
		}
		return "00010101";
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

	/*
	 * Problem List
	 */
	public List<Dxresearch> getProblems() {
		return problems;
	}

	public boolean hasProblems() {
		return exProblemList && problems!=null && !problems.isEmpty();
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
}
