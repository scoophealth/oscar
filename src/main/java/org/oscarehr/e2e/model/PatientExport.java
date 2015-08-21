/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */
package org.oscarehr.e2e.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.oscarehr.casemgmt.dao.CaseManagementIssueDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteExtDAO;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.common.dao.AllergyDao;
import org.oscarehr.common.dao.CaseManagementIssueNotesDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.MeasurementsExtDao;
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.dao.PreventionExtDao;
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
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.constant.Mappings;
import org.oscarehr.e2e.util.EverestUtils;
import org.oscarehr.util.SpringUtils;

public class PatientExport {
	private static Logger log = Logger.getLogger(PatientExport.class.getName());

	private DemographicDao demographicDao = null;
	private AllergyDao allergyDao = null;
	private MeasurementDao measurementDao = null;
	private MeasurementsExtDao measurementsExtDao = null;
	private CaseManagementIssueDAO caseManagementIssueDao = null;
	private CaseManagementIssueNotesDao caseManagementIssueNotesDao = null;
	private CaseManagementNoteDAO caseManagementNoteDao = null;
	private CaseManagementNoteExtDAO caseManagementNoteExtDao = null;
	private PreventionDao preventionDao = null;
	private PreventionExtDao preventionExtDao = null;
	private PatientLabRoutingDao patientLabRoutingDao = null;
	private Hl7TextInfoDao hl7TextInfoDao = null;
	private DrugDao drugDao = null;
	private DxresearchDAO dxResearchDao = null;

	// Optimization for Measurements since both Labs and CMOs use the same table
	private List<Measurement> rawMeasurements = null;

	private Boolean loaded = false;
	private Boolean active = false;
	private Demographic demographic = null;
	private List<Allergy> allergies = null;
	private List<Measurement> measurements = null;
	private List<CaseManagementNote> alerts = null;
	private List<CaseManagementNote> encounters = null;
	private List<CaseManagementNote> riskFactors = null;
	private List<FamilyHistoryEntry> familyHistory = null;
	private List<Immunization> immunizations = null;
	private List<Lab> labs = null;
	private List<Drug> drugs = null;
	private List<Dxresearch> problems = null;

	public PatientExport(Integer demographicNo) {
		demographicDao = SpringUtils.getBean(DemographicDao.class);
		allergyDao = SpringUtils.getBean(AllergyDao.class);
		measurementDao = SpringUtils.getBean(MeasurementDao.class);
		measurementsExtDao = SpringUtils.getBean(MeasurementsExtDao.class);
		caseManagementIssueDao = SpringUtils.getBean(CaseManagementIssueDAO.class);
		caseManagementIssueNotesDao = SpringUtils.getBean(CaseManagementIssueNotesDao.class);
		caseManagementNoteDao = SpringUtils.getBean(CaseManagementNoteDAO.class);
		caseManagementNoteExtDao = SpringUtils.getBean(CaseManagementNoteExtDAO.class);
		preventionDao = SpringUtils.getBean(PreventionDao.class);
		preventionExtDao = SpringUtils.getBean(PreventionExtDao.class);
		patientLabRoutingDao = SpringUtils.getBean(PatientLabRoutingDao.class);
		hl7TextInfoDao = SpringUtils.getBean(Hl7TextInfoDao.class);
		drugDao = SpringUtils.getBean(DrugDao.class);
		dxResearchDao = SpringUtils.getBean(DxresearchDAO.class);

		loaded = loadPatient(demographicNo);
	}

	private Boolean loadPatient(Integer demographicNo) {
		demographic = demographicDao.getDemographicById(demographicNo);
		if(demographic == null) {
			log.error("Demographic ".concat(demographicNo.toString()).concat(" can't be loaded"));
			return false;
		}

		active = "AC".equals(demographic.getPatientStatus());

		try {
			allergies = allergyDao.findAllergies(demographicNo);
		} catch (Exception e) {
			log.error("loadPatient - Failed to load Allergies", e);
			allergies = null;
		}

		try {
			measurements = assembleMeasurements(demographicNo);
		} catch (Exception e) {
			log.error("loadPatient - Failed to load Measurements", e);
			measurements = null;
		}

		try {
			encounters = caseManagementNoteDao.getNotesByDemographic(demographicNo.toString());
		} catch (Exception e) {
			log.error("loadPatient - Failed to load Encounters", e);
			encounters = null;
		}

		try {
			immunizations = new ArrayList<Immunization>();
			List<Prevention> preventions = preventionDao.findNotDeletedByDemographicId(demographicNo);
			for(Prevention prevention : preventions) {
				List<PreventionExt> preventionExts = preventionExtDao.findByPreventionId(prevention.getId());
				immunizations.add(new Immunization(prevention, preventionExts));
			}
		} catch (Exception e) {
			log.error("loadPatient - Failed to load Immunizations", e);
			immunizations = null;
		}

		try {
			labs = assembleLabs(demographicNo);
		} catch (Exception e) {
			log.error("loadPatient - Failed to load Labs", e);
			labs = null;
		}

		try {
			drugs = drugDao.findByDemographicId(demographicNo);
		} catch (Exception e) {
			log.error("loadPatient - Failed to load Medications", e);
			drugs = null;
		}

		try {
			problems = dxResearchDao.getDxResearchItemsByPatient(demographicNo);
		} catch (Exception e) {
			log.error("loadPatient - Failed to load Problems", e);
			problems = null;
		}

		parseCaseManagement(demographicNo);

		return true;
	}

	private void parseCaseManagement(Integer demographicNo) {
		if(encounters != null) {
			List<CaseManagementIssue> caseManagementIssues = caseManagementIssueDao.getIssuesByDemographic(demographicNo.toString());
			List<String> cmRiskFactorIssues = new ArrayList<String>();
			List<String> cmFamilyHistoryIssues = new ArrayList<String>();
			List<String> cmAlertsIssues = new ArrayList<String>();

			if(caseManagementIssues != null) {
				for(CaseManagementIssue entry : caseManagementIssues) {
					if(entry.getIssue_id() == Mappings.issueId.get(Constants.IssueCodes.RiskFactors) ||
							entry.getIssue_id() == Mappings.issueId.get(Constants.IssueCodes.SocHistory)) {
						cmRiskFactorIssues.add(entry.getId().toString());
					}
					else if(entry.getIssue_id() == Mappings.issueId.get(Constants.IssueCodes.FamHistory)) {
						cmFamilyHistoryIssues.add(entry.getId().toString());
					}
					else if(entry.getIssue_id() == Mappings.issueId.get(Constants.IssueCodes.Reminders)) {
						cmAlertsIssues.add(entry.getId().toString());
					}
				}
			}

			try {
				List<Integer> cmRiskFactorNotes = caseManagementIssueNotesDao.getNoteIdsWhichHaveIssues(cmRiskFactorIssues.toArray(new String[cmRiskFactorIssues.size()]));
				List<Long> cmRiskFactorNotesLong = new ArrayList<Long>();
				if(cmRiskFactorNotes != null) {
					riskFactors = new ArrayList<CaseManagementNote>();
					for(Integer i : cmRiskFactorNotes) {
						cmRiskFactorNotesLong.add(Long.parseLong(String.valueOf(i)));
					}
				}

				for(CaseManagementNote entry : encounters) {
					if(cmRiskFactorNotesLong.contains(entry.getId())) {
						riskFactors.add(entry);
					}
				}
			} catch (Exception e) {
				log.error("loadPatient - Failed to load Risk Factors/Personal History", e);
				riskFactors = null;
			}

			try {
				List<Integer> cmFamilyHistoryNotes = caseManagementIssueNotesDao.getNoteIdsWhichHaveIssues(cmFamilyHistoryIssues.toArray(new String[cmFamilyHistoryIssues.size()]));
				List<Long> cmFamilyHistoryNotesLong = new ArrayList<Long>();
				if(cmFamilyHistoryNotes != null) {
					familyHistory = new ArrayList<FamilyHistoryEntry>();
					for(Integer i : cmFamilyHistoryNotes) {
						cmFamilyHistoryNotesLong.add(Long.parseLong(String.valueOf(i)));
					}
				}

				for(CaseManagementNote entry : encounters) {
					if(cmFamilyHistoryNotesLong.contains(entry.getId())) {
						List<CaseManagementNoteExt> noteExts = caseManagementNoteExtDao.getExtByNote(entry.getId());
						familyHistory.add(new FamilyHistoryEntry(entry, noteExts));
					}
				}

			} catch (Exception e) {
				log.error("loadPatient - Failed to load Family History", e);
				familyHistory = null;
			}

			try {
				List<Integer> cmAlertsNotes = caseManagementIssueNotesDao.getNoteIdsWhichHaveIssues(cmAlertsIssues.toArray(new String[cmAlertsIssues.size()]));
				List<Long> cmAlertsLong = new ArrayList<Long>();
				if(cmAlertsNotes != null) {
					alerts = new ArrayList<CaseManagementNote>();
					for(Integer i : cmAlertsNotes) {
						cmAlertsLong.add(Long.parseLong(String.valueOf(i)));
					}
				}

				for(CaseManagementNote entry : encounters) {
					if(cmAlertsLong.contains(entry.getId())) {
						alerts.add(entry);
					}
				}
			} catch (Exception e) {
				log.error("loadPatient - Failed to load Alerts", e);
				alerts = null;
			}
		}
	}

	private List<Measurement> assembleMeasurements(Integer demographicNo) {
		// Gather and filter Measurements based on existence of lab_no field
		if(rawMeasurements == null) {
			rawMeasurements = measurementDao.findByDemographicNo(demographicNo);
		}
		List<Measurement> cmoMeasurements = new ArrayList<Measurement>();
		for(Measurement measurement : rawMeasurements) {
			MeasurementsExt labNo = measurementsExtDao.getMeasurementsExtByMeasurementIdAndKeyVal(measurement.getId(), Constants.MeasurementsExtKeys.lab_no.toString());
			if(labNo == null) {
				cmoMeasurements.add(measurement);
			}
		}

		if(cmoMeasurements.isEmpty()) {
			return null;
		}
		return cmoMeasurements;
	}

	private List<Lab> assembleLabs(Integer demographicNo) {
		// Gather Hl7TextInfo labs
		List<PatientLabRouting> tempRouting = patientLabRoutingDao.findByDemographicAndLabType(demographicNo, "HL7");
		List<Hl7TextInfo> allHl7TextInfo = new ArrayList<Hl7TextInfo>();
		for(PatientLabRouting routing : tempRouting) {
			Hl7TextInfo temp = hl7TextInfoDao.findLabId(routing.getLabNo());
			if(temp != null) {
				allHl7TextInfo.add(temp);
			}
		}

		// Short circuit if no labs
		if(allHl7TextInfo.isEmpty()) {
			return null;
		}

		// Gather and filter Measurements based on existence of lab_no field
		if(rawMeasurements == null) {
			rawMeasurements = measurementDao.findByDemographicNo(demographicNo);
		}
		List<LabComponent> allLabComponents = new ArrayList<LabComponent>();
		for(Measurement measurement : rawMeasurements) {
			MeasurementsExt labNo = measurementsExtDao.getMeasurementsExtByMeasurementIdAndKeyVal(measurement.getId(), Constants.MeasurementsExtKeys.lab_no.toString());

			// Gather MeasurementsExt and pair with Measurements into LabComponents
			if(isValidLabMeasurement(tempRouting, labNo)) {
				List<MeasurementsExt> measurementsExts = measurementsExtDao.getMeasurementsExtByMeasurementId(measurement.getId());
				allLabComponents.add(new LabComponent(measurement, measurementsExts));
			}
		}

		// Create Lab Observations
		List<Lab> allLabs = new ArrayList<Lab>();
		for(Hl7TextInfo labReport : allHl7TextInfo) {
			Lab labObservation = new Lab(labReport);

			// Get LabComponents in this Lab Observation
			Integer labNumber = labReport.getLabNumber();
			List<LabComponent> tempLabComponents = new ArrayList<LabComponent>();
			for(LabComponent labComponent : allLabComponents) {
				String componentLabNumber = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.lab_no.toString());
				if(Integer.valueOf(componentLabNumber) == labNumber) {
					if(EverestUtils.isNullorEmptyorWhitespace(labObservation.getRequestDate())) {
						labObservation.setRequestDate(labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.request_datetime.toString()));
					}
					tempLabComponents.add(labComponent);
				}
			}

			// Cluster LabComponents into LabOrganizers
			Integer prevOrganizer = 0;
			LabOrganizer tempLabOrganizer = new LabOrganizer(prevOrganizer, labReport.getReportStatus());
			for(LabComponent labComponent : tempLabComponents) {
				String rawOtherId = labComponent.getMeasurementsMap().get(Constants.MeasurementsExtKeys.other_id.toString());
				if(!EverestUtils.isNullorEmptyorWhitespace(rawOtherId)) {
					Integer currOrganizer = parseOtherID(rawOtherId)[0];

					// Create New LabOrganizer Group
					if(prevOrganizer != currOrganizer) {
						labObservation.getLabOrganizer().add(tempLabOrganizer);
						prevOrganizer = currOrganizer;
						tempLabOrganizer = new LabOrganizer(prevOrganizer, labReport.getReportStatus());
					}
				}

				// Add current LabComponent to LabOrganizer
				tempLabOrganizer.getLabComponent().add(labComponent);
			}

			// Save final LabOrganizer and Lab Observation
			labObservation.getLabOrganizer().add(tempLabOrganizer);
			allLabs.add(labObservation);
		}

		return allLabs;
	}

	private Boolean isValidLabMeasurement(List<PatientLabRouting> routing, MeasurementsExt lab_no) {
		if(lab_no != null) {
			Integer labNo = Integer.parseInt(lab_no.getVal());
			for(PatientLabRouting entry : routing) {
				if(entry.getLabNo() == labNo) {
					return true;
				}
			}
		}
		return false;
	}

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

	// PatientExport Standard Interface
	public Boolean isLoaded() {
		return loaded;
	}

	public Boolean isActive() {
		return active;
	}

	public Demographic getDemographic() {
		return demographic;
	}

	public List<Allergy> getAllergies() {
		return allergies;
	}

	public List<Measurement> getMeasurements() {
		return measurements;
	}

	public List<CaseManagementNote> getAlerts() {
		return alerts;
	}

	public List<CaseManagementNote> getEncounters() {
		return encounters;
	}

	public List<FamilyHistoryEntry> getFamilyHistory() {
		return familyHistory;
	}

	public List<CaseManagementNote> getRiskFactors() {
		return riskFactors;
	}

	public List<Immunization> getImmunizations() {
		return immunizations;
	}

	public List<Lab> getLabs() {
		return labs;
	}

	public List<Drug> getMedications() {
		return drugs;
	}

	public List<Dxresearch> getProblems() {
		return problems;
	}

	// Supporting Family History Subclass
	public static class FamilyHistoryEntry {
		private CaseManagementNote familyHistory = new CaseManagementNote();
		private Map<String, String> extMap = new HashMap<String, String>();

		public FamilyHistoryEntry(CaseManagementNote familyHistory, List<CaseManagementNoteExt> extMap) {
			if(familyHistory != null) {
				this.familyHistory = familyHistory;
			}
			if(extMap != null) {
				for(CaseManagementNoteExt extElement : extMap) {
					this.extMap.put(extElement.getKeyVal(), extElement.getValue());
				}
			}
		}

		public CaseManagementNote getFamilyHistory() {
			return familyHistory;
		}

		public Map<String, String> getExtMap() {
			return extMap;
		}
	}

	// Supporting Immunization Subclass
	public static class Immunization {
		private Prevention prevention = new Prevention();
		private Map<String, String> preventionMap = new HashMap<String, String>();

		public Immunization(Prevention prevention, List<PreventionExt> preventionExt) {
			if(prevention != null) {
				this.prevention = prevention;
			}
			if(preventionExt != null) {
				for(PreventionExt extElement : preventionExt) {
					this.preventionMap.put(extElement.getkeyval(), extElement.getVal());
				}
			}
		}

		public Prevention getPrevention() {
			return prevention;
		}

		public Map<String, String> getPreventionMap() {
			return preventionMap;
		}
	}

	// Supporting Lab Grouping Subclasses
	public static class Lab {
		private Hl7TextInfo hl7TextInfo = new Hl7TextInfo();
		private List<LabOrganizer> labOrganizer = new ArrayList<LabOrganizer>();
		private String requestDate = null;

		public Lab(Hl7TextInfo hl7TextInfo) {
			if(hl7TextInfo != null) {
				this.hl7TextInfo = hl7TextInfo;
			}
		}

		public Hl7TextInfo getHl7TextInfo() {
			return hl7TextInfo;
		}

		public List<LabOrganizer> getLabOrganizer() {
			return labOrganizer;
		}

		public String getRequestDate() {
			return requestDate;
		}

		public void setRequestDate(String requestDate) {
			this.requestDate = requestDate;
		}
	}

	public static class LabOrganizer {
		private Integer id = Constants.Runtime.INVALID_VALUE;
		private String reportStatus = null;
		private List<LabComponent> labComponent = new ArrayList<LabComponent>();

		public LabOrganizer(Integer id, String reportStatus) {
			this.id = id;
			this.reportStatus = reportStatus;
		}

		public Integer getGroupId() {
			return id;
		}

		public String getReportStatus() {
			return reportStatus;
		}

		public List<LabComponent> getLabComponent() {
			return labComponent;
		}
	}

	public static class LabComponent {
		private Measurement measurement = new Measurement();
		private Map<String, String> measurementsMap = new HashMap<String, String>();

		public LabComponent(Measurement measurement, List<MeasurementsExt> measurementsExt) {
			if(measurement != null) {
				this.measurement = measurement;
			}
			if(measurementsExt != null) {
				for(MeasurementsExt extElement : measurementsExt) {
					this.measurementsMap.put(extElement.getKeyVal(), extElement.getVal());
				}
			}
		}

		public Measurement getMeasurement() {
			return measurement;
		}

		public Map<String, String> getMeasurementsMap() {
			return measurementsMap;
		}
	}
}
