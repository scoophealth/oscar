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
package org.oscarehr.common.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.EncounterForm;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class EncounterFormDaoTest extends DaoTestFixtures {

	protected EncounterFormDao dao = (EncounterFormDao)SpringUtils.getBean(EncounterFormDao.class);
	Logger logger = MiscUtils.getLogger();

	@Before
	public void setUp() throws Exception {
		SchemaUtils.restoreTable(false, "encounterForm");
	}

	@Test
	/**
	 * Ensures that the findAll() method returns all encounter form records.
	 * @throws Exception
	 */
	public void testFindAll() throws Exception {
		EncounterForm form1 = new EncounterForm();
		EntityDataGenerator.generateTestDataForModelClass(form1);
		form1.setFormName("FormA");
		form1.setFormValue("1");

		EncounterForm form2 = new EncounterForm();
		EntityDataGenerator.generateTestDataForModelClass(form2);
		form2.setFormName("FormC");
		form2.setFormValue("2");

		EncounterForm form3 = new EncounterForm();
		EntityDataGenerator.generateTestDataForModelClass(form3);
		form3.setFormName("FormB");
		form3.setFormValue("3");

		dao.persist(form1);
		dao.persist(form2);
		dao.persist(form3);

		List<EncounterForm> result = dao.findAll();
		List<EncounterForm> expectedResult = new ArrayList<EncounterForm>(Arrays.asList(
				form1, form3, form2
				));
		
		// fail if list sizes aren't the same
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}

		// make sure each item in the list is as expected
		//marc: removed the sort from the DAO
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.contains(result.get(i))){
				//logger.warn("Items not ordered by CodeType and Code.");
				fail("Items not sorted by CodeType and Code.");
			}
		}
		assertTrue(true);
	}

	@Test
	/**
	 * Ensures that the findByFormName() method returns records where
	 * the form name matches the specified form name.
	 * @throws Exception
	 */
	public void testFindByFormName() throws Exception {
		String formName = "EncounterForm";
		
		EncounterForm form1 = new EncounterForm();
		EntityDataGenerator.generateTestDataForModelClass(form1);
		form1.setFormName(formName);
		form1.setFormValue("1");

		EncounterForm form2 = new EncounterForm();
		EntityDataGenerator.generateTestDataForModelClass(form2);
		form2.setFormName("FormC");
		form2.setFormValue("2");
		
		EncounterForm form3 = new EncounterForm();
		EntityDataGenerator.generateTestDataForModelClass(form3);
		form3.setFormName(formName);
		form3.setFormValue("3");
		
		dao.persist(form1);
		dao.persist(form2);
		dao.persist(form3);

		List<EncounterForm> result = dao.findByFormName(formName);
		List<EncounterForm> expectedResult = new ArrayList<EncounterForm>(Arrays.asList(
				form1, form3
				));
		
		// fail if list sizes aren't the same
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}

		// make sure each item in the list is as expected
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items not ordered by CodeType and Code.");
				fail("Items not sorted by CodeType and Code.");
			}
		}
		assertTrue(true);
	}

}
