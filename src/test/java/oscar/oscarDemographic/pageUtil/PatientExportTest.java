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

//import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.Hl7TextInfo;
import org.oscarehr.common.model.Prevention;

/**
 * 
 * @author rrusk
 * This class tests the model used for patient data export
 */
public class PatientExportTest {
		
    @BeforeClass
    public static void onlyOnce() throws Exception {
    	// Loading in the test data takes too long to do it
    	// before each unit test.
    	//
    	// In the sql script, configure FOREIGN_KEYS_CHECK=0
    	// before loading the db and reset to original value
    	// at end.
    	assertEquals("Error loading test data",
    			SchemaUtils.loadFileIntoMySQL(System.getProperty("basedir")+
    					"/src/test/resources/e2e-test-db.sql"),0);
    }
    
    // Testing constructors

    @Test
    public void testPatientExport0() {
    	// very little can be done with default constructor
    	assertNotNull("PatientExport default constructor failed",new PatientExport());
    }

    @Test
	public void testPatientExport1() {
		PatientExport p = new PatientExport("1");
		assertNotNull("PatientExport object unexpectedly null",p);
		assertEquals("Unexpected demographic number",p.getDemographicNo(),"1");
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
		PatientExport p = new PatientExport("1");
		assertNotNull("PatientExport object unexpectedly null", p);
		p.setDemographicNo("599999");
		assertEquals("Wrong demographicNo", p.getDemographicNo(),"599999");
		p.setDemographicNo("1");
	}
	
	@Test
	public void testGetSetFirstName() {
		PatientExport p = new PatientExport("1");
		String old = p.getFirstName();
		p.setFirstName("Jane");
		assertEquals(p.getFirstName(),"Jane");
		p.setFirstName(old);
		assertEquals(p.getFirstName(), old);
	}
	
	@Test
	public void testGetSetLastName() {
		PatientExport p = new PatientExport("1");
		String old = p.getLastName();
		p.setLastName("Doe");
		assertEquals(p.getLastName(),"Doe");
		p.setLastName(old);
		assertEquals(p.getLastName(), old);
	}
	
	@Test
	public void testGetSetGender() {
		PatientExport p = new PatientExport("1");
		String old = p.getGender();
		p.setGender("new_gender");
		assertEquals(p.getGender(),"new_gender");
		p.setGender(old);
		assertEquals(p.getGender(), old);
	}
	
	@Test
	public void testGetSetDateOfBirth() {
		PatientExport p = new PatientExport("1");
		String old = p.getDateOfBirth();
		p.setDateOfBirth("new_date");
		assertEquals(p.getDateOfBirth(),"new_date");
		p.setDateOfBirth(old);
		assertEquals(p.getDateOfBirth(), old);
	}
	
	@Test
	public void testGetSetMonthOfBirth() {
		PatientExport p = new PatientExport("1");
		String old = p.getMonthOfBirth();
		p.setMonthOfBirth("new_month");
		assertEquals(p.getMonthOfBirth(),"new_month");
		p.setMonthOfBirth(old);
		assertEquals(p.getMonthOfBirth(), old);
	}
	
	@Test
	public void testGetSetYearOfBirth() {
		PatientExport p = new PatientExport("1");
		String old = p.getYearOfBirth();
		p.setYearOfBirth("new_year");
		assertEquals(p.getYearOfBirth(),"new_year");
		p.setYearOfBirth(old);
		assertEquals(p.getYearOfBirth(), old);
	}
	
	@Test
	public void testGetSetHin() {
		PatientExport p = new PatientExport("1");
		String old = p.getHin();
		p.setHin("new_Hin");
		assertEquals(p.getHin(),"new_Hin");
		p.setHin(old);
		assertEquals(p.getHin(), old);
	}
	
	@Test
	public void testGetSetProviderNo() {
		PatientExport p = new PatientExport("1");
		String old = p.getProviderNo();
		p.setProviderNo("new_provider_no");
		assertEquals(p.getProviderNo(),"new_provider_no");
		p.setProviderNo(old);
		assertEquals(p.getProviderNo(), old);
	}
	
	// Test convenience methods
	@Test
	public void testGetBirthDate() {
		PatientExport p = new PatientExport("1");
		String result = p.getBirthDate();
		assertNotNull("Birth date unexpectedly null", result);
		assertTrue("Birth date unexpectedly empty", !result.isEmpty());
	}
	
	@Test
	public void testGetGenderDesc() {
		PatientExport p = new PatientExport("1");
		String result = p.getBirthDate();
		assertNotNull("GenderDesc unexpectedly null", result);
		assertFalse("GenderDesc unexpectedly empty", result.isEmpty());
	}
	
	// Test allergies
	@Test
	public void testAllergies() {
		PatientExport p = new PatientExport("1");
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
		if (!p.hasAllergies() && list!=null) {
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
		PatientExport p = new PatientExport("1");
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
	
	@Test
	public void testGetImmuExtValue() {
		//TODO
	}
	
	// Test laboratory reports
	@Test
	public void testLabs() {
		PatientExport p = new PatientExport("1");
		List<Hl7TextInfo> list = p.getLabs();
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
	
	// Test medications
	@Test
	public void testMedications() {
		PatientExport p = new PatientExport("1");
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
	
	@Test
	public void testSortByDin() {
		// TODO
	}

	// Test problem list
	@Test
	public void testProblems() {
		PatientExport p = new PatientExport("1");
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
		PatientExport p = new PatientExport("1");
		// the test dataset has a row in the provider
		// table for provider_no '000000'
		String provider = p.getProviderFirstName("000000");
		assertNotNull(provider);
		assertFalse(provider.isEmpty());
	}
	
	@Test
	public void testGetProviderLastName() {
		PatientExport p = new PatientExport("1");
		String provider = p.getProviderLastName("000000");
		assertNotNull(provider);
		assertFalse(provider.isEmpty());
	}
}
