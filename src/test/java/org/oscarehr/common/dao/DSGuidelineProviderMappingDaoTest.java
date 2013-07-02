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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.decisionSupport.model.DSGuidelineProviderMapping;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class DSGuidelineProviderMappingDaoTest extends DaoTestFixtures {
	
	protected DSGuidelineProviderMappingDao dao = SpringUtils.getBean(DSGuidelineProviderMappingDao.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("dsGuidelineProviderMap");
	}
	
	@Test 
	public void testCreate() throws Exception {
		
		DSGuidelineProviderMapping dsGPM = new DSGuidelineProviderMapping();
		EntityDataGenerator.generateTestDataForModelClass(dsGPM);
		dao.persist(dsGPM);
		assertNotNull(dsGPM.getId());
	}
	
	@Test
	public void testGetMappingsByProvider() throws Exception {
		
		String providerNo1 = "101";
		String providerNo2 = "202";
		
		DSGuidelineProviderMapping dsGPM1 = new DSGuidelineProviderMapping();
		EntityDataGenerator.generateTestDataForModelClass(dsGPM1);
		dsGPM1.setProviderNo(providerNo1);
		dao.persist(dsGPM1);
		
		DSGuidelineProviderMapping dsGPM2 = new DSGuidelineProviderMapping();
		EntityDataGenerator.generateTestDataForModelClass(dsGPM2);
		dsGPM2.setProviderNo(providerNo2);
		dao.persist(dsGPM2);
		
		DSGuidelineProviderMapping dsGPM3 = new DSGuidelineProviderMapping();
		EntityDataGenerator.generateTestDataForModelClass(dsGPM3);
		dsGPM3.setProviderNo(providerNo1);
		dao.persist(dsGPM3);
		
		List<DSGuidelineProviderMapping> expectedResult = new ArrayList<DSGuidelineProviderMapping>(Arrays.asList(dsGPM1, dsGPM3));		
		List<DSGuidelineProviderMapping> result = dao.getMappingsByProvider(providerNo1);
		Logger logger = MiscUtils.getLogger();
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}

		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items do not match.");
				fail("Items do not match.");
			}
		}
		assertTrue(true);
	}
	
	@Test
	public void testMappingExists() throws Exception {
		
		String providerNo1 = "101";
		String providerNo2 = "202";
		
		String guidelineUUID1 = "alpha";
		String guidelineUUID2 = "bravo";
				
		DSGuidelineProviderMapping dsGPM1 = new DSGuidelineProviderMapping();
		EntityDataGenerator.generateTestDataForModelClass(dsGPM1);
		dsGPM1.setProviderNo(providerNo1);
		dsGPM1.setGuidelineUUID(guidelineUUID1);
		dao.persist(dsGPM1);
		
		DSGuidelineProviderMapping dsGPM2 = new DSGuidelineProviderMapping();
		EntityDataGenerator.generateTestDataForModelClass(dsGPM2);
		dsGPM2.setProviderNo(providerNo2);
		dsGPM2.setGuidelineUUID(guidelineUUID2);
		dao.persist(dsGPM2);
		
		DSGuidelineProviderMapping dsGPM3 = new DSGuidelineProviderMapping();
		EntityDataGenerator.generateTestDataForModelClass(dsGPM3);
		dsGPM3.setProviderNo(providerNo1);
		dsGPM3.setGuidelineUUID(guidelineUUID2);
		dao.persist(dsGPM3);
		
		boolean expectedResult = true;
		boolean result = dao.mappingExists(dsGPM1);
		
		assertEquals(expectedResult, result);
	}
}