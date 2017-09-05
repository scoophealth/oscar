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

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.util.MiscUtils;

public class ImmunizationTest {
	private static Logger logger = MiscUtils.getLogger();
	private static Prevention prevention;
	private static OscarFhirResource< org.hl7.fhir.dstu3.model.Immunization, Prevention> oscarFhirResource;
	private static final String testJSON = "";
	private static final String testXML = "";
	private static Date date = new Date(System.currentTimeMillis());
	
	@BeforeClass
	public static void setUpBeforeClass() {
		prevention = new Prevention();
		prevention.setPreventionDate(date);
		prevention.setNever(Boolean.FALSE);
		prevention.setRefused(Boolean.FALSE);
		prevention.setPreventionType("HPV");

		oscarFhirResource = new Immunization( prevention );		
	}

	@AfterClass
	public static void tearDownAfterClass() {
		prevention = null;
		oscarFhirResource = null;
	}

	@Test
	public void testGetPrevention() {
		logger.info( "testGetPrevention" );
		assertEquals( date, oscarFhirResource.getOscarResource().getCreationDate() );
	}

	@Test
	public void testGetFhirImmunization() {
		logger.info( "testGetFhirImmunization" );
		assertEquals( date, oscarFhirResource.getFhirResource().getDate() );
	}

	// @Test
	public void testGetFhirImmunizationJSONDstu3() {
		logger.info( "testGetFhirImmunizationJSONDstu3" );
		logger.info( oscarFhirResource.getFhirJSON() );
		assertEquals( testJSON, oscarFhirResource.getFhirJSON() );		
	}


	// @Test
	public void testGetFhirImmunizationXML() {	
		logger.info( "testGetFhirImmunizationXML" );
		logger.info( oscarFhirResource.getFhirXML() );
		assertEquals( testXML, oscarFhirResource.getFhirXML() );		
	}

}
