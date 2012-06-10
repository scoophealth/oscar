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

package org.oscarehr.managers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.junit.Test;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.Vacancy;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.managers.WaitListManager.AdmissionDemographicPair;
import org.oscarehr.util.VelocityUtils;

public class WaitListManagerTest {

	@Test
	public void testLoadingProperties() {
		String fromAddress = WaitListManager.waitListProperties.getProperty("from_address");
		assertNotNull(fromAddress);
	}

	@Test
	public void testVelocityMerge() throws IOException {
		Program program = new Program();
		program.setName("MyProgramName");

		String notes = "some of my notes";

		Calendar startCal = new GregorianCalendar(2012, 3, 4);
		Calendar endCal = new GregorianCalendar(2012, 3, 5);

		AdmissionDemographicPair a1 = new AdmissionDemographicPair();
		Demographic d1 = new Demographic();
		d1.setLastName("lastName1");
		d1.setFirstName("firstName1");
		d1.setSex("sex1");
		a1.setDemographic(d1);

		AdmissionDemographicPair a2 = new AdmissionDemographicPair();
		Demographic d2 = new Demographic();
		d2.setLastName("lastName2");
		d2.setFirstName("firstName2");
		d2.setSex("sex2");
		a2.setDemographic(d2);

		ArrayList<AdmissionDemographicPair> admissionDemographicPairs = new ArrayList<AdmissionDemographicPair>();
		admissionDemographicPairs.add(a1);
		admissionDemographicPairs.add(a2);

		VelocityContext velocityContext = WaitListManager.getAdmissionVelocityContext(program, notes, startCal.getTime(), endCal.getTime(), admissionDemographicPairs);

		InputStream is = WaitListManagerTest.class.getResourceAsStream("/wait_list_velocity_template.txt");
		String template = IOUtils.toString(is);

		String mergedtemplate = VelocityUtils.velocityEvaluate(velocityContext, template);

		is = WaitListManagerTest.class.getResourceAsStream("/wait_list_velocity_template_results.txt");
		String expectedResults=IOUtils.toString(is);

		assertEquals(expectedResults, mergedtemplate);
	}
	
	@Test 
	public void testVacancyMerge() throws IOException
	{
		Vacancy vacancy=new Vacancy();
		vacancy.setTemplateId(1234);
		vacancy.setStatus("test status");
		
		Calendar cal = new GregorianCalendar(2012, 3, 4);
		
		
		VelocityContext velocityContext = WaitListManager.getVacancyVelocityContext(vacancy, "test notes", cal.getTime());

		InputStream is = WaitListManagerTest.class.getResourceAsStream("/wait_list_immediate_vacancy_email_template.txt");
		String template = IOUtils.toString(is);

		String mergedtemplate = VelocityUtils.velocityEvaluate(velocityContext, template);

		is = WaitListManagerTest.class.getResourceAsStream("/wait_list_immediate_vacancy_email_template_results.txt");
		String expectedResults=IOUtils.toString(is);

		assertEquals(expectedResults, mergedtemplate);
		
	}
}
