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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.dao.utils.SchemaUtils;

/**
 * 
 * @author rrusk
 *  This test class is meant to test the E2E document
 *  generated from a Velocity template.
 */
public class E2EVelocityTemplateTest {
	
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

	@Test
	public void testE2EVelocityTemplate() {
		assertNotNull(new E2EVelocityTemplate());
	}

	@Test
	public void testExport() {
		E2EVelocityTemplate e2etemplate = new E2EVelocityTemplate();
		assertNotNull(e2etemplate);
		PatientExport p = new PatientExport("1");
		assertNotNull(p);
		String s = null;
		try {
	        s = e2etemplate.export(p);
        } catch (Exception e) {
	        e.printStackTrace();
	        fail();
        }
		assertNotNull(s);
		assertFalse(s.isEmpty());
		// should be no $ variables in output
		assertFalse(s.contains("$"));
	}
}
