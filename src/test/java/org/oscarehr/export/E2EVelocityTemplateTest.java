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
package org.oscarehr.export;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.ProviderDataDao;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.export.E2EExportValidator;
import org.oscarehr.export.E2EVelocityTemplate;
import org.oscarehr.export.PatientExport;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

/**
 * This test class tests the validity of EMR2EMR documents generated from a Velocity template.
 * 
 * @author Raymond Rusk
 */
public class E2EVelocityTemplateTest extends DaoTestFixtures {
	private static Logger logger = MiscUtils.getLogger();
	private static DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	private static ProviderDataDao providerDataDao = SpringUtils.getBean(ProviderDataDao.class);
	private static Integer demographicNo;
	private static String[] tables = {"allergies", "casemgmt_issue", "clinic", "demographic",
		"drugs", "dxresearch", "icd9", "issue", "measurementMap", "measurementType", "measurements",
		"measurementsExt", "patientLabRouting", "preventions", "program", "provider"};

	@BeforeClass
	public static void onlyOnce() throws Exception {
		SchemaUtils.restoreTable(tables);
		Demographic entity = new Demographic();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setDemographicNo(null);
		// Ugly hack to ensure that oscar_test has valid numeric data for the year, month and day in demographic table.
		// Without this fix, birthDate ends up being "yearmoda" which causes an XML schema validation error.
		if (entity.getYearOfBirth().toLowerCase().contains("year")) {
			entity.setYearOfBirth("1940");
		}
		if (entity.getMonthOfBirth().toLowerCase().contains("mo")) {
			entity.setMonthOfBirth("09");
		}
		if (entity.getDateOfBirth().toLowerCase().contains("da")) {
			entity.setDateOfBirth("25");
		}
		entity.setProviderNo(providerDataDao.getLastId().toString());
		demographicDao.save(entity);
		demographicNo = entity.getDemographicNo();
	}

	@Test
	public void testE2EVelocityTemplate() {
		assertNotNull(new E2EVelocityTemplate());
	}

	@Test
	public void testExport() {
		E2EVelocityTemplate e2etemplate = new E2EVelocityTemplate();
		assertNotNull(e2etemplate);
		PatientExport p = new PatientExport();
		assertNotNull(p);
		p.loadPatient(demographicNo.toString());
		String s = null;
		try {
			s = e2etemplate.export(p);
		} catch (Exception e) {
			logger.error("VALIDATION ERROR: (template export failed) ", e);
			fail();
		}

		assertNotNull(s);
		assertFalse("XML document unexpectedly empty", s.isEmpty());
		// should be no $ variables in output
		assertFalse("XML document unexpectedly contains '$'", s.contains("$"));

		// check output is well-formed
		assertTrue("XML unexpectedly not well-formed", E2EExportValidator.isWellFormedXML(s));
		logger.warn("There should be one VALIDATION ERROR warning below.");
		// following statement should cause error
		assertFalse("XML well-formed, expected not well-formed", E2EExportValidator.isWellFormedXML(s.replace("</ClinicalDocument>", "</clinicalDocument>")));

		// validate against XML schema
		assertTrue("XML document unexpectedly not valid", E2EExportValidator.isValidXML(s));
		logger.warn("There should be one VALIDATION ERROR warning below.");
		// following statement should cause error
		assertFalse("XML valid, expected not valid", E2EExportValidator.isValidXML(s.replace("DOCSECT", "DOXSECT")));
	}

/*	@Test
	public void tortureTest() {
		long startTime = System.nanoTime();
		System.out.println("TortureTest start time = "+startTime);
		PatientExport p = new PatientExport(demographicNo.toString());
		long prev = startTime;
		long curr = prev;
		for (int i = 0; i < 10000; i++) {
			if (i%500 == 0) {
				prev = curr;
				curr = System.nanoTime();
				System.out.println("["+i+"] "+curr);
			}
			String s = null;
			try {
				E2EVelocityTemplate e2etemplate = new E2EVelocityTemplate();
				s = e2etemplate.export(p);
			} catch (Exception e) {
				logger.error(e.getMessage());
				fail();
			}
			assertTrue(E2EExportValidator.isValidXML(s));
		}
		long stopTime = System.nanoTime();
		System.out.println("TortureTest stop time = "+stopTime);
		System.out.println("Total time (sec) = " + (stopTime-startTime)/1000000000.0);
	}*/
}
