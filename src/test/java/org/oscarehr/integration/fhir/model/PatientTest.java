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

import static org.junit.Assert.*;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
//import org.oscarehr.managers.DemographicManager;
//import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

public class PatientTest {

	private static Patient patient;
	private static Demographic demographic;
	private static final String testJSON = "{\"resourceType\":\"Patient\",\"identifier\":[{\"system\":\"urn:fake:mrns\",\"value\":\"122343\"}],\"name\":[{\"family\":[\"Warren\"],\"given\":[\"Dennis\"],\"suffix\":[\"Mr\"]}],\"gender\":{\"coding\":[{\"system\":\"http://hl7.org/fhir/v3/AdministrativeGender\",\"code\":\"M\"}]}}";
	private static final String testXML = "<Patient xmlns=\"http://hl7.org/fhir\"><identifier><system value=\"urn:fake:mrns\"/><value value=\"122343\"/></identifier><name><family value=\"Warren\"/><given value=\"Dennis\"/><suffix value=\"Mr\"/></name><gender><coding><system value=\"http://hl7.org/fhir/v3/AdministrativeGender\"/><code value=\"M\"/></coding></gender></Patient>";
	private static Logger logger = MiscUtils.getLogger();
	
	@BeforeClass
	public static void setUpBeforeClass() {

		demographic = new Demographic();
		demographic.setDemographicNo( 122343 );
		demographic.setTitle( "Mr" );
		demographic.setSex( "M" );
		demographic.setFirstName( "Dennis" );
		demographic.setLastName( "Warren" );
		Calendar birthdate = Calendar.getInstance();
		birthdate.set(1969, 6, 18);
		demographic.setBirthDay(birthdate);
		
		//PRACTITIONER
		Provider provider = new Provider();
		provider.setProviderNo("8879");
		provider.setFirstName( "Doug" );
		provider.setLastName( "Ross" );
		provider.setHsoNo( "12342" );
		provider.setOhipNo( "12342" );

		patient = new Patient( demographic );		
		patient.addGeneralPractitioner( provider );
	}

	@AfterClass
	public static void tearDownAfterClass() {
		demographic = null;
		patient = null;
	}

	@Test
	public void testGetDemographic() {
		logger.info( "testGetDemographic" );
		assertEquals( demographic, patient.getOscarResource() );
	}

	@Test
	public void testGetFhirpatient() {
		logger.info( "testGetFhirpatient" );
		assertEquals( AdministrativeGender.MALE, patient.getFhirResource().getGender() );
	}

	// @Test
	public void testGetFhirpatientJSONDstu3() {
		logger.info( "testGetFhirpatientJSONDstu3" );
		logger.info( patient.getFhirJSON() );
		assertEquals( testJSON, patient.getFhirJSON() );		
	}


	//@Test
	public void testGetFhirpatientXML() {	
		logger.info( "testGetFhirpatientXML" );
		logger.info( patient.getFhirXML() );
		assertEquals( testXML, patient.getFhirXML() );		
	}

}

