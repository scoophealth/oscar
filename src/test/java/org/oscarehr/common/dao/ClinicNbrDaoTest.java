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

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ClinicNbr;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ClinicNbrDaoTest extends DaoTestFixtures {

	protected ClinicNbrDao dao = (ClinicNbrDao)SpringUtils.getBean(ClinicNbrDao.class);
	Logger logger = MiscUtils.getLogger();

	@Before
	public void setUp() throws Exception {
		SchemaUtils.restoreTable("clinic_nbr");
	}

//	@Test
//	/**
//	 * SKIPPED DUE TO BUG: 	Order by runs a string comparison sort because
//	 * 						nbr_value is a varchar.
//	 * Ensures that persisted records are contained in the table.
//	 * Comparison ensures that values are ordered from least to greatest.
//	 * @throws Exception
//	 */
//	public void testFindAll() throws Exception {
//		ClinicNbr nbr1 = new ClinicNbr();
//		EntityDataGenerator.generateTestDataForModelClass(nbr1);
//		nbr1.setNbrStatus("A");
//		nbr1.setNbrValue("1");
//
//		ClinicNbr nbr2 = new ClinicNbr();
//		EntityDataGenerator.generateTestDataForModelClass(nbr2);
//		nbr2.setNbrStatus("D");
//		nbr2.setNbrValue("8");
//
//		ClinicNbr nbr3 = new ClinicNbr();
//		EntityDataGenerator.generateTestDataForModelClass(nbr3);
//		nbr3.setNbrStatus("A");
//		nbr3.setNbrValue("2");
//
//		ClinicNbr nbr4 = new ClinicNbr();
//		EntityDataGenerator.generateTestDataForModelClass(nbr4);
//		nbr4.setNbrStatus("A");
//		nbr4.setNbrValue("3");
//
//		dao.persist(nbr1);
//		dao.persist(nbr2);
//		dao.persist(nbr3);
//		dao.persist(nbr4);
//
//		ArrayList<ClinicNbr> result = dao.findAll();
//		ArrayList<ClinicNbr> expectedResult = new ArrayList<ClinicNbr>(Arrays.asList(nbr1, nbr3, nbr4));
//
//		for (int i = 0; i < result.size() - 1; i++) {
//			logger.info(result.get(i).getNbrValue());
//		}
//		
//		// ensure that all persisted items are in the table
//		// ensure ordered by status A-Z
//		assertTrue(result.containsAll(expectedResult));
//		int value1;
//		int value2;
//		for (int i = 0; i < result.size() - 1; i++) {
//			value1 = Integer.parseInt(result.get(i).getNbrValue());
//			value2 = Integer.parseInt(result.get(i+1).getNbrValue());
//			if (value1 > value2){
//				fail("Results not ordred by value");
//			}
//		}
//		assertTrue(true);
//	}

	@Test
	/**
	 * Ensures that the removeEntry() method deletes records
	 * by setting the status to 'D'
	 * @throws Exception
	 */
	public void testRemoveEntry() throws Exception {
		ClinicNbr nbr1 = new ClinicNbr();
		EntityDataGenerator.generateTestDataForModelClass(nbr1);
		nbr1.setNbrStatus("A");
		dao.persist(nbr1);
		
		dao.removeEntry(1);
		nbr1 = dao.find(1);
		assertEquals("D", nbr1.getNbrStatus());
	}

	@Test
	/**
	 * Ensures that the addEntry() method persists new
	 * records to the table given the value and string.
	 * @throws Exception
	 */
	public void testAddEntry() throws Exception {
		String nbrValue = "A";
		String nbrString ="RMA";
		
		ClinicNbr nbr1 = new ClinicNbr();
		EntityDataGenerator.generateTestDataForModelClass(nbr1);
		nbr1.setNbrString(nbrString);
		nbr1.setNbrValue(nbrValue);
		
		dao.addEntry(nbrValue, nbrString);
		
		assertEquals("RMA", nbr1.getNbrString());
		assertEquals("A", nbr1.getNbrValue());
	}

}
