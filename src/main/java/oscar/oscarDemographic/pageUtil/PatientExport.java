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
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.dao.PreventionExtDao;
import org.oscarehr.common.dao.ProviderDataDao;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.Hl7TextInfo;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.PreventionExt;
import org.oscarehr.common.model.ProviderData;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

/**
 * @author Jeremy Ho
 * This class models a "patient" which bundles all data required to define a "patient" for export
 */
public class PatientExport {
	private static Logger log = MiscUtils.getLogger();
	private static DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
	private static AllergyDao allergyDao = (AllergyDao)SpringUtils.getBean("allergyDao");
	private static DrugDao drugDao = (DrugDao)SpringUtils.getBean("drugDao");
	private static PreventionDao preventionDao = (PreventionDao)SpringUtils.getBean("preventionDao");
	private static PreventionExtDao preventionExtDao = (PreventionExtDao)SpringUtils.getBean("preventionExtDao");
	private static ProviderDataDao providerDataDao = (ProviderDataDao)SpringUtils.getBean("providerDataDao");
	private static DxresearchDAO dxResearchDao = SpringUtils.getBean(DxresearchDAO.class);
	private static Icd9Dao icd9Dao = (Icd9Dao)SpringUtils.getBean("icd9Dao");
	private static PatientLabRoutingDao patientLabRoutingDao = (PatientLabRoutingDao)SpringUtils.getBean("patientLabRoutingDao");
	private static Hl7TextInfoDao hl7TextInfoDao = (Hl7TextInfoDao)SpringUtils.getBean("hl7TextInfoDao");
	
	private Date currentDate = new Date();
	private Integer demographicNo = null;
	private Demographic demographic = null;
	private List<Drug> drugs = null;
	private List<Allergy> allergies = null;
	private List<Prevention> preventions = null;
	private List<Dxresearch> problems = null;
	private List<Hl7TextInfo> labs = null;
	
	private boolean exMedicationsAndTreatments = false;
	private boolean exAllergiesAndAdverseReactions = false;
	private boolean exImmunizations = false;
	private boolean exProblemList = false;
	private boolean exLaboratoryResults = false;
	
	protected PatientExport() {
	}
	
	public PatientExport(String demoNo) {
		try {
			this.demographicNo = new Integer(demoNo);
			this.demographic = demographicDao.getDemographic(demoNo);
			this.allergies = allergyDao.findAllergies(demographicNo);
			// Master to 12.1 compatibility fix - revisit before submitting
			this.drugs = drugDao.findByDemographicId(demographicNo, null);
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
			
			// Master to 12.1 compatibility fix - added method to patientLabRoutingDao - revisit before submitting
			List<PatientLabRouting> tempLabs = patientLabRoutingDao.findByDemographicAndLabType(demographicNo, "HL7");
			labs = new ArrayList<Hl7TextInfo>();
			for(PatientLabRouting routing : tempLabs) {
				this.labs.add(hl7TextInfoDao.findLabId(routing.getLabNo()));
			}
		}
	    catch (Exception e) {
	        log.error(e.getMessage(), e);
	    }
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
		return!(!exAllergiesAndAdverseReactions || allergies==null || allergies.isEmpty());
	}
	
	/*
	 * Immunizations
	 */
	public List<Prevention> getImmunizations() {
		return preventions;
	}
	
	public boolean hasImmunizations() {
		return!(!exImmunizations || preventions==null || preventions.isEmpty());
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
	public List<Hl7TextInfo> getLabs() {
		return labs;
	}
	
	public boolean hasLabs() {
		return!(!exLaboratoryResults || labs==null || labs.isEmpty());
	}
	
	/*
	 * Medications
	 */
	public List<Drug> getMedications() {
		return drugs;
	}
	
	public boolean hasMedications() {
		return!(!exMedicationsAndTreatments || drugs==null || drugs.isEmpty());
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
		return!(!exProblemList || problems==null || problems.isEmpty());
	}

	// Function to allow access to ICD9 Description table data based on ICD9 code
	public static String getICD9Description(String code) {
		return icd9Dao.findByCode(code).getDescription();
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
	
	public String getProviderFirstName(String providerNo) {
		ProviderData providerData = providerDataDao.findByProviderNo(providerNo);
		return providerData.getFirstName();
	}
	
	public String getProviderLastName(String providerNo) {
		ProviderData providerData = providerDataDao.findByProviderNo(providerNo);
		return providerData.getLastName();
	}
}
