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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

/**
 * 
 * @author rrusk
 *  This test class is meant to test the E2E document
 *  generated from a Velocity template.
 */
public class E2EVelocityTemplateTest extends DaoTestFixtures {
	
    private static DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
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
		// Ugly hack to ensure that oscar_test has valid numeric
		// data for the year, month and day in demographic table.
		// Without this fix, birthDate ends up being "yearmoda"
		// which causes an XML schema validation error.
		if (entity.getYearOfBirth().toLowerCase().contains("year")) {
			entity.setYearOfBirth("1940");
		}
		if (entity.getMonthOfBirth().toLowerCase().contains("mo")) {
			entity.setMonthOfBirth("09");
		}
		if (entity.getDateOfBirth().toLowerCase().contains("da")) {
			entity.setDateOfBirth("25");
		}
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
		PatientExport p = new PatientExport(demographicNo.toString());
		assertNotNull(p);
		String s = null;
		try {
	        s = e2etemplate.export(p);
        } catch (Exception e) {
        	MiscUtils.getLogger().error(e.getMessage());
        	fail();
        }
		
		assertNotNull(s);
		assertFalse(s.isEmpty());
		// should be no $ variables in output
		assertFalse(s.contains("$"));
		
		boolean logErrors;
		// check output is well-formed
		logErrors = true;
		assertTrue(E2EExportValidator.isWellFormedXML(s, logErrors));
		logErrors = false; // following statement should cause error, don't log
		assertFalse(E2EExportValidator.isWellFormedXML(s.replace("</ClinicalDocument>",
				"</clinicalDocument>"), logErrors));
		
		// validate against XML schema
		logErrors = true;
		assertTrue(E2EExportValidator.isValidXML(s, logErrors));
		logErrors = false;  // following statement should cause error, don't log
		assertFalse(E2EExportValidator.isValidXML(s.replace("DOCSECT", "DOXSECT"), logErrors));
	}
}
