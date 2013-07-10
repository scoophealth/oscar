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
package org.oscarehr.PMmodule.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.PMmodule.model.CriteriaSelectionOption;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class CriteriaSelectionOptionDaoTest extends DaoTestFixtures {

	public CriteriaSelectionOptionDao dao = SpringUtils.getBean(CriteriaSelectionOptionDao.class);


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("criteria_selection_option");
	}

	@Test
	public void testCreate() throws Exception {
		CriteriaSelectionOption entity = new CriteriaSelectionOption();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testGetCriteriaSelectedOptionsByCriteriaId() throws Exception {
		
		int criteriaId1 = 101, criteriaId2 = 202;
		
		CriteriaSelectionOption cSO1 = new CriteriaSelectionOption();
		EntityDataGenerator.generateTestDataForModelClass(cSO1);
		cSO1.setCriteriaId(criteriaId1);
		dao.saveEntity(cSO1);
		
		CriteriaSelectionOption cSO2 = new CriteriaSelectionOption();
		EntityDataGenerator.generateTestDataForModelClass(cSO2);
		cSO2.setCriteriaId(criteriaId2);
		dao.saveEntity(cSO2);
		
		CriteriaSelectionOption cSO3 = new CriteriaSelectionOption();
		EntityDataGenerator.generateTestDataForModelClass(cSO3);
		cSO3.setCriteriaId(criteriaId1);
		dao.saveEntity(cSO3);
		
		CriteriaSelectionOption cSO4 = new CriteriaSelectionOption();
		EntityDataGenerator.generateTestDataForModelClass(cSO4);
		cSO4.setCriteriaId(criteriaId1);
		dao.saveEntity(cSO4);
		
		List<CriteriaSelectionOption> expectedResult = new ArrayList<CriteriaSelectionOption>(Arrays.asList(cSO1, cSO3, cSO4));
		List<CriteriaSelectionOption> result = dao.getCriteriaSelectedOptionsByCriteriaId(criteriaId1);

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
