package org.oscarehr.integration.fhir.model;
/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.model.Clinic;
// import org.oscarehr.common.model.ProfessionalContact;
import org.oscarehr.util.MiscUtils;


public class OrganizationTest {
	
	private static Organization<Clinic> organization;
	private static Clinic clinic;
	// private static final String testJSON = "{\"resourceType\":\"Patient\",\"identifier\":[{\"system\":\"urn:fake:mrns\",\"value\":\"122343\"}],\"name\":[{\"family\":[\"Warren\"],\"given\":[\"Dennis\"],\"suffix\":[\"Mr\"]}],\"gender\":{\"coding\":[{\"system\":\"http://hl7.org/fhir/v3/AdministrativeGender\",\"code\":\"M\"}]}}";
	// private static final String testXML = "<Patient xmlns=\"http://hl7.org/fhir\"><identifier><system value=\"urn:fake:mrns\"/><value value=\"122343\"/></identifier><name><family value=\"Warren\"/><given value=\"Dennis\"/><suffix value=\"Mr\"/></name><gender><coding><system value=\"http://hl7.org/fhir/v3/AdministrativeGender\"/><code value=\"M\"/></coding></gender></Patient>";
	private static Logger logger = MiscUtils.getLogger();

	@BeforeClass
	public static void setUpBeforeClass() {
		
		clinic = new Clinic();
		clinic.setId(2);
		clinic.setClinicAddress("123 Clinic Street");
		clinic.setClinicCity("Vancouver");
		clinic.setClinicProvince("BC");
		clinic.setClinicPhone("778-567-3445");
		clinic.setClinicFax("778-343-3453");
		clinic.setClinicName("Test Medical Clinic");

		organization = new org.oscarehr.integration.fhir.model.Organization<Clinic>( clinic );
	}

	@AfterClass
	public static void tearDownAfterClass() {
		organization = null;
	}

	@Test
	public void testGetOrganizationJson() {
		logger.info("testGetOrganizationJson");
		System.out.println( organization.getFhirJSON() );
	}
	

	@Test
	public void testGetOscarContactModel() {
		logger.info("testGetOscarContactModel");
		System.out.println( organization.getOscarResource() );
	}

}
