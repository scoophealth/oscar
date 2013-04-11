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
package oscar.oscarDemographic.pageUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.AllergyDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.dao.Icd9Dao;
import org.oscarehr.common.dao.MeasurementDao;
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
import org.oscarehr.common.model.MeasurementsExt;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.PreventionExt;
import org.oscarehr.common.model.ProviderData;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

/**
 * @author Jeremy Ho
 * This class models a "patient" which bundles all data required to define a "patient" for export
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
	private static MeasurementDao measurementDao = SpringUtils.getBean(MeasurementDao.class);
	private static MeasurementsExtDao measurementsExtDao = SpringUtils.getBean(MeasurementsExtDao.class);
	
	private Date currentDate = new Date();
	private String authorId = LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo();
	private Integer demographicNo = null;
	private Demographic demographic = null;
	private List<Drug> drugs = null;
	private List<Allergy> allergies = null;
	private List<Prevention> preventions = null;
	private List<Dxresearch> problems = null;
	private List<Lab> labs = null;
	
	private boolean exMedicationsAndTreatments = false;
	private boolean exAllergiesAndAdverseReactions = false;
	private boolean exImmunizations = false;
	private boolean exProblemList = false;
	private boolean exLaboratoryResults = false;
	
	protected PatientExport() {
	}
	
	public PatientExport(String demoNo) {
		this.demographicNo = new Integer(demoNo);
		this.demographic = demographicDao.getDemographic(demoNo);
		this.allergies = allergyDao.findAllergies(demographicNo);
		this.drugs = drugDao.findByDemographicId(demographicNo);
		
		// Sort drugs by reverse chronological order & group by DIN by sorting
		Collections.reverse(drugs);
		Collections.sort(drugs, new sortByDin());
		
		this.preventions = preventionDao.findNotDeletedByDemographicId(demographicNo);
		List <Dxresearch> tempProblems = dxResearchDao.getDxResearchItemsByPatient(demographicNo);
		problems = new ArrayList<Dxresearch>();
		for(Dxresearch problem : tempProblems) {
			if(problem.getStatus() != 'D' && problem.getCodingSystem().equals("icd9")) {
				this.problems.add(problem);
			}
		}
		
		this.labs = assembleLabs();
	}
	
	private List<Lab> assembleLabs() {
		// Gather hl7TextInfo labs
		List<PatientLabRouting> tempRouting = patientLabRoutingDao.findByDemographicAndLabType(demographicNo, "HL7");
		List<Hl7TextInfo> tempLabs = new ArrayList<Hl7TextInfo>();
		for(PatientLabRouting routing : tempRouting) {
			tempLabs.add(hl7TextInfoDao.findLabId(routing.getLabNo()));
		}
		
		// Short circuit if no labs
		if(tempLabs.size() == 0)
			return null;
		
		// Gather and filter measurements
		List<Measurement> rawMeasurements = measurementDao.findByDemographicIdUpdatedAfterDate(demographicNo, new Date(0));
		List<Measurement> tempMeasurements = new ArrayList<Measurement>();
		for(Measurement entry : rawMeasurements) {
			if(Integer.parseInt(entry.getProviderNo()) == 0) {
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
				int currGroup = parseOtherID(getLabExtValue(labMeasurementsExtAll.get(i), "other_id"))[0];
				
				// Create New Group
				if(prevGroup != currGroup) {
					labObj.group.add(tempGroup);
					prevGroup = currGroup;
					tempGroup = new LabGroup(prevGroup);
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
	
	/*
	 * Demographics
	 */
	// Directly mapped functions
	public String getDemographicNo() {
		return demographicNo.toString();
	}
	
	public void setDemographicNo(String demoNo) {
		demographicNo = Integer.parseInt(demoNo);
		demographic.setDemographicNo(demographicNo);
	}
	
	public String getFirstName() {
		return demographic.getFirstName();
	}

	public void setFirstName(String rhs) {
		demographic.setFirstName(rhs);
	}
	
	public String getLastName() {
		return demographic.getLastName();
	}

	public void setLastName(String rhs) {
		demographic.setLastName(rhs);
	}
	
	public String getGender() {
		return demographic.getSex();
	}

	public void setGender(String rhs) {
		demographic.setSex(rhs);
	}
	
	public String getDateOfBirth() {
		return demographic.getDateOfBirth();
	}

	public void setDateOfBirth(String rhs) {
		demographic.setDateOfBirth(rhs);
	}
	
	public String getMonthOfBirth() {
		return demographic.getMonthOfBirth();
	}

	public void setMonthOfBirth(String rhs) {
		demographic.setMonthOfBirth(rhs);
	}
	
	public String getYearOfBirth() {
		return demographic.getYearOfBirth();
	}

	public void setYearOfBirth(String rhs) {
		demographic.setYearOfBirth(rhs);
	}
	
	public String getHin() {
		return demographic.getHin();
	}
	
	public void setHin(String rhs) {
		demographic.setHin(rhs);
	}
	
	public String getProviderNo() {
		return demographic.getProviderNo();
	}
	
	public void setProviderNo(String rhs) {
		demographic.setProviderNo(rhs);
	}
	
	// Output get convenience functions
	public String getBirthDate() {
		return demographic.getYearOfBirth() + demographic.getMonthOfBirth() + demographic.getDateOfBirth();
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
	 * Immunizations
	 */
	public List<Prevention> getImmunizations() {
		return preventions;
	}
	
	public boolean hasImmunizations() {
		return exImmunizations && preventions!=null && !preventions.isEmpty();
	}
	
	// Function to allow access to PreventionsExt table data based on prevention id
	public static String getImmuExtValue(String id, String keyval) {
    	try {
    		List<PreventionExt> preventionExts = preventionExtDao.findByPreventionIdAndKey(Integer.valueOf(id), keyval);
    		for (PreventionExt preventionExt : preventionExts) {
    			return preventionExt.getVal();
    		}
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
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
	
	// Function to allow access to LabsExt table data based on prevention id (direct version)
	public static String getLabExtValue(String id, String keyval) {
		try {
			List<MeasurementsExt> measurementExt= measurementsExtDao.getMeasurementsExtByMeasurementId(Integer.valueOf(id));
			for (MeasurementsExt entry : measurementExt) {
				if(entry.getKeyVal().equals(keyval)) {
					return entry.getVal();
				}
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	// Function to allow access to LabsExt table data based on prevention id (object version)
	public static String getLabExtValue(List<MeasurementsExt> measurementExt, String keyval) {
		for (MeasurementsExt entry : measurementExt) {
			if(entry.getKeyVal().equals(keyval)) {
				return entry.getVal();
			}
		}
		return null;
	}
	
	// Handles Other ID field parsing
	private Integer[] parseOtherID(String rhs) {
		Integer[] lhs = null;
		try {
			String[] temp = rhs.split("-");
			lhs = new Integer[temp.length];
			for(int i=0; i < temp.length; i++) {
				lhs[i] = Integer.parseInt(temp[i]);
			}
		}
		catch (Exception e) {
            log.error(e.getMessage(), e);
        }
		
		return lhs;
	}
	
	// Nested Lab Object Models
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
	
	public boolean isActiveDrug(Date rhs) {
		if(currentDate.after(rhs)) return false;
		else return true;
	}
	
	public class sortByDin implements Comparator<Drug> {
		public int compare(Drug one, Drug two) {
			 return Integer.parseInt(one.getRegionalIdentifier()) - Integer.parseInt(two.getRegionalIdentifier());
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

	// Function to allow access to ICD9 Description table data based on ICD9 code
	public static String getICD9Description(String code) {
		String result = icd9Dao.findByCode(code).getDescription();
		if (result == null || result.isEmpty()) {
			return " ";
		}
		
		return result;
    }
	
	/*
	 * Utility
	 */
	public Date getCurrentDate() {
		return currentDate;
	}
	
	public Date stringToDate(String rhs) {
		Date date = new Date();
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			date = formatter.parse(rhs);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return date;
	}
	
	public String getAuthorId() {
		return authorId;
	}
	
	public String getProviderFirstName(String providerNo) {
		ProviderData providerData = providerDataDao.findByProviderNo(providerNo);
		return providerData.getFirstName();
	}
	
	public String getProviderLastName(String providerNo) {
		ProviderData providerData = providerDataDao.findByProviderNo(providerNo);
		return providerData.getLastName();
	}
}
