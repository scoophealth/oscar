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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.ProviderDataDao;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.ProviderData;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDemographic.pageUtil.PatientExport.Lab;

/**
 * 
 * @author Raymond Rusk
 * This class tests the model used for patient data export
 */
public class PatientExportTest extends DaoTestFixtures {
	
    private static DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
    private static ProviderDataDao providerDataDao = (ProviderDataDao)SpringUtils.getBean("providerDataDao");
    private static Integer demographicNo;

    @BeforeClass
	public static void onlyOnce() throws Exception {
		SchemaUtils.restoreTable("demographicSets", "lst_gender", "demographic_merged",
				"admission", "program", "health_safety", "demographic", "provider",
				"allergies", "drugs", "preventions", "dxresearch", "patientLabRouting",
				"icd9");
    	Demographic entity = new Demographic();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setDemographicNo(null);
		demographicDao.save(entity);
		demographicNo = entity.getDemographicNo();
	}
    
    // Testing constructors

    @Test
    public void testPatientExport0() {
    	
    	// very little can be done with default constructor
    	assertNotNull("PatientExport default constructor failed",new PatientExport());
    }

    @Test
	public void testPatientExport1() {
    	assertNotNull(demographicNo);
		assertNotNull(demographicDao.getDemographic(demographicNo.toString()));
		assertNotNull(demographicDao.getDemographicById(demographicNo));
		assertNotNull(demographicDao.getClientByDemographicNo(demographicNo));
		
		PatientExport p = new PatientExport(demographicNo.toString());
		assertNotNull("PatientExport object unexpectedly null",p);
		assertEquals("Unexpected demographic number",p.getDemographicNo(),demographicNo.toString());
	}
    
    // Testing utility methods that are used in other tests
    @Test
    public void testGetCurrentDate() {
    	assertNotNull((new PatientExport()).getCurrentDate());
    }
    
    @Test
    public void testStringToDate() {
    	assertNotNull((new PatientExport()).stringToDate("2013-03-07 15:30:00"));
    }
    
    // Testing booleans is done within other tests below
    
    // Testing Demographics
    
	@Test
	public void testGetSetDemographicNo() {
		PatientExport p = new PatientExport(demographicNo.toString());
		assertNotNull("PatientExport object unexpectedly null", p);
		p.setDemographicNo("599999");
		assertEquals("Wrong demographicNo", p.getDemographicNo(),"599999");
		p.setDemographicNo(demographicNo.toString());
	}
	
	@Test
	public void testGetSetFirstName() {		
		PatientExport p = new PatientExport(demographicNo.toString());
		String old = p.getFirstName();
		p.setFirstName("Jane");
		assertEquals(p.getFirstName(),"Jane");
		p.setFirstName(old);
		assertEquals(p.getFirstName(), old);
	}
	
	@Test
	public void testGetSetLastName() {
		PatientExport p = new PatientExport(demographicNo.toString());
		String old = p.getLastName();
		p.setLastName("Doe");
		assertEquals(p.getLastName(),"Doe");
		p.setLastName(old);
		assertEquals(p.getLastName(), old);
	}
	
	@Test
	public void testGetSetGender() {		
		PatientExport p = new PatientExport(demographicNo.toString());
		String old = p.getGender();
		p.setGender("new_gender");
		assertEquals(p.getGender(),"new_gender");
		p.setGender(old);
		assertEquals(p.getGender(), old);
	}
	
	@Test
	public void testGetSetDateOfBirth() {
		PatientExport p = new PatientExport(demographicNo.toString());
		String old = p.getDateOfBirth();
		p.setDateOfBirth("new_date");
		assertEquals(p.getDateOfBirth(),"new_date");
		p.setDateOfBirth(old);
		assertEquals(p.getDateOfBirth(), old);
	}
	
	@Test
	public void testGetSetMonthOfBirth() {
		PatientExport p = new PatientExport(demographicNo.toString());
		String old = p.getMonthOfBirth();
		p.setMonthOfBirth("new_month");
		assertEquals(p.getMonthOfBirth(),"new_month");
		p.setMonthOfBirth(old);
		assertEquals(p.getMonthOfBirth(), old);
	}
	
	@Test
	public void testGetSetYearOfBirth() {
		PatientExport p = new PatientExport(demographicNo.toString());
		String old = p.getYearOfBirth();
		p.setYearOfBirth("new_year");
		assertEquals(p.getYearOfBirth(),"new_year");
		p.setYearOfBirth(old);
		assertEquals(p.getYearOfBirth(), old);
	}
	
	@Test
	public void testGetSetHin() {
		PatientExport p = new PatientExport(demographicNo.toString());
		String old = p.getHin();
		p.setHin("new_Hin");
		assertEquals(p.getHin(),"new_Hin");
		p.setHin(old);
		assertEquals(p.getHin(), old);
	}
	
	@Test
	public void testGetSetProviderNo() {
		PatientExport p = new PatientExport(demographicNo.toString());
		String old = p.getProviderNo();
		p.setProviderNo("new_provider_no");
		assertEquals(p.getProviderNo(),"new_provider_no");
		p.setProviderNo(old);
		assertEquals(p.getProviderNo(), old);
	}
	
	// Test convenience methods
	@Test
	public void testGetBirthDate() {
		PatientExport p = new PatientExport(demographicNo.toString());
		String result = p.getBirthDate();
		assertNotNull("Birth date unexpectedly null", result);
		assertTrue("Birth date unexpectedly empty", !result.isEmpty());
	}
	
	@Test
	public void testGetGenderDesc() {
		PatientExport p = new PatientExport(demographicNo.toString());
		String result = p.getBirthDate();
		assertNotNull("GenderDesc unexpectedly null", result);
		assertFalse("GenderDesc unexpectedly empty", result.isEmpty());
	}
	
	// Test allergies
	@Test
	public void testAllergies() {		
		PatientExport p = new PatientExport(demographicNo.toString());
		List<Allergy> list = p.getAllergies();
		if (p.hasAllergies()) {
			// exAllergiesAndAdverseReachtions must be true
			assertNotNull(list);
			p.setExAllergiesAndAdverseReactions(false);
			assertFalse(p.hasAllergies());
			// put boolean back to original state
			p.setExAllergiesAndAdverseReactions(true);
			assertTrue(p.hasAllergies());
		}
		if (!p.hasAllergies() && list!=null && !list.isEmpty()) {
			// exAllergiesAndAdverseReachtions must be false
			p.setExAllergiesAndAdverseReactions(true);
			assertTrue(p.hasAllergies());
			// put boolean back to original state
			p.setExAllergiesAndAdverseReactions(false);
			assertFalse(p.hasAllergies());
		}
	}
	
	// Test immunizations
	@Test
	public void testImmunizations() {
		PatientExport p = new PatientExport(demographicNo.toString());
		List<Prevention> list = p.getImmunizations();
		if (p.hasImmunizations()) {
			// exImmunizations must be true
			assertNotNull(list);
			assertFalse(list.isEmpty());
			p.setExImmunizations(false);
			assertFalse(p.hasImmunizations());
			// put boolean back to original state
			p.setExImmunizations(true);
			assertTrue(p.hasImmunizations());
		}
		if (!p.hasImmunizations() && list!=null && !list.isEmpty()) {
			// exImmunizations must be false
			p.setExImmunizations(true);
			assertTrue(p.hasImmunizations());
			// put boolean back to original state
			p.setExImmunizations(false);
			assertFalse(p.hasImmunizations());
		}
	}
	
	//TODO testGetImmuExtValue()
	
	// Test laboratory reports
	@Test
	public void testLabs() {
		PatientExport p = new PatientExport(demographicNo.toString());
		List<Lab> list = p.getLabs();
		if (p.hasLabs()) {
			// exLaboratoryResults must be true
			assertNotNull("lab results unexpectedly null",list);
			assertFalse("lab results unexpectedly empty",list.isEmpty());
			p.setExLaboratoryResults(false);
			assertFalse(p.hasLabs());
			// put boolean back to original state
			p.setExLaboratoryResults(true);
			assertTrue(p.hasLabs());
		}
		if (!p.hasLabs() && list!=null && !list.isEmpty()) {
			// exLaboratoryResults must be false
			p.setExLaboratoryResults(true);
			assertTrue("has labs unexpectedly not true", p.hasLabs());
			// put boolean back to original state
			p.setExLaboratoryResults(false);
			assertFalse("has labs unexpectedly not false",p.hasLabs());
		}
	}

	//TODO testGetLabExtValue() (2 versions)
	
	//TODO Possibly test classes Lab and LabGroup
	
	// Test medications
	@Test
	public void testMedications() {
		PatientExport p = new PatientExport(demographicNo.toString());
		List<Drug> list = p.getMedications();
		if (p.hasMedications()) {
			// exMedicationsAndTreatments must be true
			assertNotNull("drugs unexpectedly null",list);
			assertFalse("drugs unexpectedly empty",list.isEmpty());
			p.setExMedications(false);
			assertFalse(p.hasMedications());
			// put boolean back to original state
			p.setExMedications(true);
			assertTrue(p.hasMedications());
		}
		if (!p.hasMedications() && list!=null && !list.isEmpty()) {
			// exMedicationsAndTreatments must be false
			p.setExMedications(true);
			assertTrue("medications unexpectedly not true", p.hasMedications());
			// put boolean back to original state
			p.setExMedications(false);
			assertFalse("medications unexpectedly not false",p.hasMedications());
		}
	}
	
	@Test
	public void testIsActiveDrug() {
		// can get by with no argument constructor here
		PatientExport p = new PatientExport();
		Date pastDate = p.stringToDate("2012-01-01 00:00:00");
		assertFalse(p.isActiveDrug(pastDate));
		Date futureDate = p.stringToDate("9999-12-31 23:59:59");
		assertTrue(p.isActiveDrug(futureDate));
	}
	
	// TODO testSortByDin()

	// Test problem list
	@Test
	public void testProblems() {
		PatientExport p = new PatientExport(demographicNo.toString());
		List<Dxresearch> list = p.getProblems();
		if (p.hasProblems()) {
			// exProblemList must be true
			assertNotNull("problems unexpectedly null",list);
			assertFalse("problems unexpectedly empty",list.isEmpty());
			p.setExProblemList(false);
			assertFalse(p.hasProblems());
			// put boolean back to original state
			p.setExProblemList(true);
			assertTrue(p.hasProblems());
		}
		if (!p.hasProblems() && list!=null && !list.isEmpty()) {
			// exProblemList must be false
			p.setExProblemList(true);
			assertTrue("problems unexpectedly not true", p.hasProblems());
			// put boolean back to original state
			p.setExProblemList(false);
			assertFalse("medications unexpectedly not false",p.hasProblems());
		}
	}
	
	@Test
	public void testGetICD9Description() {
		// the test dataset has a row in the icd9
		// table for icd9 = '001' ("CHOLERA")
		String desc = PatientExport.getICD9Description("001");
		assertNotNull(desc);
		assertFalse(desc.isEmpty());
	}
	
	// Test remaining Utility methods (others tested above)
	@Test
	public void testGetProviderFirstName() {
		PatientExport p = new PatientExport(demographicNo.toString());

		assertNotNull(providerDataDao);
		List<ProviderData> list = providerDataDao.findAllOrderByLastName();
		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertNotNull(list.get(0));
		assertNotNull(list.get(0).getId());
		assertFalse(list.get(0).getId().isEmpty());
		
		String provider = p.getProviderFirstName(list.get(0).getId());
		assertNotNull(provider);
		assertFalse(provider.isEmpty());
		assertTrue(provider.equals(list.get(0).getFirstName()));
	}
	
	@Test
	public void testGetProviderLastName() {
		PatientExport p = new PatientExport(demographicNo.toString());

		assertNotNull(providerDataDao);
		List<ProviderData> list = providerDataDao.findAllOrderByLastName();
		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertNotNull(list.get(0));
		assertNotNull(list.get(0).getId());
		assertFalse(list.get(0).getId().isEmpty());
		
		String provider = p.getProviderLastName(list.get(0).getId());
		assertNotNull(provider);
		assertFalse(provider.isEmpty());
		assertTrue(provider.equals(list.get(0).getLastName()));
	}
}
