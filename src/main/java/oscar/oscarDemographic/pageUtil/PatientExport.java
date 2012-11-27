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

import java.util.Date;
import java.util.List;

import org.oscarehr.common.dao.AllergyDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.util.SpringUtils;

/**
 * @author Jeremy Ho
 * This class models a "patient" which bundles all data required to define a "patient" for export
 */
public class PatientExport {
	private static DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
	private static AllergyDao allergyDao = (AllergyDao)SpringUtils.getBean("allergyDao");
	private static DrugDao drugDao = (DrugDao)SpringUtils.getBean("drugDao");
	private static PreventionDao preventionDao = (PreventionDao)SpringUtils.getBean("preventionDao");
	
	private Date currentDate = new Date();
	private Integer demographicNo = null;
	private Demographic demographic = null;
	private List<Drug> drugs = null;
	private List<Allergy> allergies = null;
	private List<Prevention> preventions = null;
	
	private boolean exMedicationsAndTreatments = false;
	private boolean exAllergiesAndAdverseReactions = false;
	private boolean exImmunizations = false;
	
	protected PatientExport() {
	}
	
	public PatientExport(String demoNo) {
		this.demographicNo = new Integer(demoNo);
		this.demographic = demographicDao.getDemographic(demoNo);
		this.allergies = allergyDao.findAllergies(demographicNo);
		this.drugs = drugDao.findByDemographicId(demographicNo);
		this.preventions = preventionDao.findNotDeletedByDemographicId(demographicNo);
		//initialize(demoNo);
	}
	
	/*
	private void initialize(String demoNo) {
		RxPrescriptionData.Prescription[] drugs = new RxPrescriptionData().getPrescriptionsByPatient(Integer.parseInt(demoNo));
		for(RxPrescriptionData.Prescription d : drugs) {
			medications.add(parseDrugs(d));
		}
		
		Allergy[] allergiesArray = RxPatientData.getPatient(demoNo).getAllergies();
		for(Allergy a : allergiesArray) {
			allergies.add(a);
		}
	}
	
	private Medication parseDrugs(RxPrescriptionData.Prescription drug) {
		Medication medication = new Medication();
		
		medication.setDrugId(Integer.toString(drug.getDrugId()));
		medication.setStartDate(drug.getRxDate());
		medication.setEndDate(drug.getEndDate());
		medication.setDin(drug.getRegionalIdentifier());
		medication.setAtc(drug.getAtcCode());
		medication.setGenericName(drug.getGenericName());
		medication.setBrandName(drug.getBrandName());
		
		return medication;
	}
	*/
	
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
	
	/*
	 * Utility
	 */
	public Date getCurrentDate() {
		return currentDate;
	}
	
	/*
	 * Medication Sub-object
	 */
	/*
	public static class Medication {
		private String drugId;
		private Date startDate;
		private Date endDate;
		private String din;
		private String atc;
		private String genericName;
		private String brandName;
		
		public Medication() {
		}
		
		// Directly mappable functions
		public String getDrugId() {
			return this.drugId;
		}
		
		public void setDrugId(String rhs) {
			this.drugId = rhs;
		}
		
		public Date getStartDate() {
			return this.startDate;
		}
		
		public void setStartDate(Date rhs) {
			this.startDate = rhs;
		}
		
		public Date getEndDate() {
			return this.endDate;
		}
		
		public void setEndDate(Date rhs) {
			this.endDate = rhs;
		}
		
		public String getDin() {
			return this.din;
		}
		
		public void setDin(String rhs) {
			this.din = rhs;
		}
		
		public String getAtc() {
			return this.atc;
		}
		
		public void setAtc(String rhs) {
			this.atc = rhs;
		}
		
		public String getGenericName() {
			return this.genericName;
		}
		
		public void setGenericName(String rhs) {
			this.genericName = rhs;
		}
		
		public String getBrandName() {
			return this.brandName;
		}
		
		public void setBrandName(String rhs) {
			this.brandName = rhs;
		}
		
		// Output utility functions
		public boolean isActive() {
			Date currentDate = new Date();
			if(currentDate.after(endDate)) return false;
			else return true;
		}
		
		public boolean isValidAtc() {
			if (atc != null && !atc.trim().equals("")) {
				return true;
			}
			return false;
		}
	}
	*/
}
