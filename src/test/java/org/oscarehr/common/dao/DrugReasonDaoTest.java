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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.DrugReason;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class DrugReasonDaoTest extends DaoTestFixtures {
	
	protected DrugReasonDao dao = (DrugReasonDao)SpringUtils.getBean(DrugReasonDao.class);
	Logger logger = MiscUtils.getLogger();

	@Before
	public void setUp() throws Exception {
		SchemaUtils.restoreTable("drugReason");
	}

	
	@Test
	/**
	 * Ensures that addNewDrugReason() persists the passed in drug reason record.
	 * @throws Exception
	 */
	public void testAddNewDrugReason() throws Exception {
		Integer drugId = 10;
		String codingSystem = "NDC";
		String code = "0123456789";
		boolean archivedFlag = true;
		
		DrugReason reason1 = new DrugReason();
		EntityDataGenerator.generateTestDataForModelClass(reason1);
		reason1.setDrugId(drugId);
		reason1.setCodingSystem(codingSystem);
		reason1.setCode(code);
		reason1.setArchivedFlag(archivedFlag);
		
		assertTrue(dao.addNewDrugReason(reason1));
	}
	
	
	@Test
	/**
	 * Ensures that a drug reason exists if a record has
	 * a coding system, code, drug id, archviedFlag.
	 * @throws Exception
	 */
	public void testHasReason() throws Exception {
		Integer drugId = 10;
		String codingSystem = "NDC";
		String code = "0123456789";
		boolean archivedFlag = true;
		
		// hasReason() selects opposite of passed in boolean; should not be selected. 
		DrugReason reason1 = new DrugReason();
		EntityDataGenerator.generateTestDataForModelClass(reason1);
		reason1.setDrugId(drugId);
		reason1.setCodingSystem(codingSystem);
		reason1.setCode(code);
		reason1.setArchivedFlag(archivedFlag);
		
		DrugReason reason2 = new DrugReason();
		EntityDataGenerator.generateTestDataForModelClass(reason2);
		reason2.setDrugId(drugId);
		reason2.setCodingSystem(codingSystem);
		reason2.setCode(code);
		reason2.setArchivedFlag(false);
		
		dao.persist(reason1);
		dao.persist(reason2);
		
		assertTrue(dao.hasReason(drugId, codingSystem, code, archivedFlag));
	}

	@Test
	/**
	 * Ensures that getReasonsForDrugID() returns a list of drug reasons where
	 * drug id matches, and archivedFlag is opposite of the flag passed in.
	 * @throws Exception
	 */
	public void testGetReasonsForDrugID() throws Exception {
		Integer drugId = 10;
		boolean archivedFlag = true;
		
		// getReasonsForDrugID() selects opposite of passed in boolean; should not be selected.
		DrugReason reason1 = new DrugReason();
		EntityDataGenerator.generateTestDataForModelClass(reason1);
		reason1.setDrugId(drugId);
		reason1.setArchivedFlag(archivedFlag);
		
		// Wrong drug id; should not be selected.
		DrugReason reason2 = new DrugReason();
		EntityDataGenerator.generateTestDataForModelClass(reason2);
		reason2.setDrugId(11);
		reason2.setArchivedFlag(false);
		
		DrugReason reason3 = new DrugReason();
		EntityDataGenerator.generateTestDataForModelClass(reason3);
		reason3.setDrugId(drugId);
		reason3.setArchivedFlag(false);
		
		dao.persist(reason1);
		dao.persist(reason2);
		dao.persist(reason3);
		
		List<DrugReason> result = dao.getReasonsForDrugID(drugId, archivedFlag);
		List<DrugReason> expectedResult = new ArrayList<DrugReason>(Arrays.asList(
				reason3
				));
		
		assertEquals(expectedResult.size(), result.size());
		assertTrue(result.containsAll(expectedResult));
	}

}
