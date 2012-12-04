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
/**
 * @author Shazib
 */
package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
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
import org.oscarehr.common.model.Hsfo2Patient;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class Hsfo2PatientDaoTest extends DaoTestFixtures {
	protected Hsfo2PatientDao dao = (Hsfo2PatientDao)SpringUtils.getBean(Hsfo2PatientDao.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("hsfo2_patient");
	}

	@Test
	public void testGetHsfoPatientByPatientId() throws Exception {
		
		String patientId1 = "101";
		String patientId2 = "202";
		
		Hsfo2Patient hsfo2Patient1 = new Hsfo2Patient();
		EntityDataGenerator.generateTestDataForModelClass(hsfo2Patient1);
		hsfo2Patient1.setPatient_Id(patientId1);
		dao.persist(hsfo2Patient1);
		
		Hsfo2Patient hsfo2Patient2 = new Hsfo2Patient();
		EntityDataGenerator.generateTestDataForModelClass(hsfo2Patient2);
		hsfo2Patient2.setPatient_Id(patientId2);
		dao.persist(hsfo2Patient2);

		Hsfo2Patient hsfo2Patient3 = new Hsfo2Patient();
		EntityDataGenerator.generateTestDataForModelClass(hsfo2Patient3);
		hsfo2Patient3.setPatient_Id(patientId1);
		dao.persist(hsfo2Patient3);
		
		Hsfo2Patient expectedResult = hsfo2Patient3;
		Hsfo2Patient result = dao.getHsfoPatientByPatientId(patientId1);
	
		assertEquals(expectedResult, result);
	}

	@Test
	public void testGetAllHsfoPatients() throws Exception {
		
		String patientId1 = "101";
		String patientId2 = "202";
		
		Hsfo2Patient hsfo2Patient1 = new Hsfo2Patient();
		EntityDataGenerator.generateTestDataForModelClass(hsfo2Patient1);
		hsfo2Patient1.setPatient_Id(patientId1);
		dao.persist(hsfo2Patient1);
		
		Hsfo2Patient hsfo2Patient2 = new Hsfo2Patient();
		EntityDataGenerator.generateTestDataForModelClass(hsfo2Patient2);
		hsfo2Patient2.setPatient_Id(patientId2);
		dao.persist(hsfo2Patient2);

		Hsfo2Patient hsfo2Patient3 = new Hsfo2Patient();
		EntityDataGenerator.generateTestDataForModelClass(hsfo2Patient3);
		hsfo2Patient3.setPatient_Id(patientId1);
		dao.persist(hsfo2Patient3);
				
		List<Hsfo2Patient> expectedResult = new ArrayList<Hsfo2Patient>(Arrays.asList(hsfo2Patient1, hsfo2Patient2, hsfo2Patient3));
		List<Hsfo2Patient> result = dao.getAllHsfoPatients();

		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}
}
