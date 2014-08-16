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
import static org.junit.Assert.assertNotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.PHRVerification;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.MiscUtils;

public class PHRVerificationDaoTest extends DaoTestFixtures {

	protected PHRVerificationDao dao = SpringUtils.getBean(PHRVerificationDao.class);
	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");
	

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("PHRVerification");
	}

	@Test
	public void testCreate() throws Exception {
		PHRVerification entity = new PHRVerification();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	public void testGetForDemographic() throws Exception {
		
		int demographicNo1 = 100;
		int demographicNo2 = 200;
		
		boolean isArchived = true;
		boolean isNotArchived = false;
		
		PHRVerification phrVerification1 = new PHRVerification();
		EntityDataGenerator.generateTestDataForModelClass(phrVerification1);
		phrVerification1.setDemographicNo(demographicNo1);
		phrVerification1.setArchived(isNotArchived);
		Date createdDate1 = new Date(dfm.parse("20110701").getTime());
		phrVerification1.setCreatedDate(createdDate1);
		dao.persist(phrVerification1);
		
		PHRVerification phrVerification2 = new PHRVerification();
		EntityDataGenerator.generateTestDataForModelClass(phrVerification2);
		phrVerification2.setDemographicNo(demographicNo2);
		phrVerification2.setArchived(isArchived);
		Date createdDate2 = new Date(dfm.parse("20100701").getTime());
		phrVerification2.setCreatedDate(createdDate2);
		dao.persist(phrVerification2);
		
		PHRVerification phrVerification3 = new PHRVerification();
		EntityDataGenerator.generateTestDataForModelClass(phrVerification3);
		phrVerification3.setDemographicNo(demographicNo1);
		phrVerification3.setArchived(isNotArchived);
		Date createdDate3 = new Date(dfm.parse("20090701").getTime());
		phrVerification3.setCreatedDate(createdDate3);
		dao.persist(phrVerification3);
		
		List<PHRVerification> expectedResult = new ArrayList<PHRVerification>(Arrays.asList(phrVerification1, phrVerification3));
		List<PHRVerification> result = dao.findByDemographic(demographicNo1, true);

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

	@Test
	public void testGetVerificationLevel() throws Exception {
		
		int demographicNo1 = 100;
		int demographicNo2 = 200;
		
		String authenticationLevel1 = PHRVerification.VERIFICATION_METHOD_FAX;
		String authenticationLevel2 = PHRVerification.VERIFICATION_METHOD_VIDEOPHONE;
		
		boolean isArchived = true;
		boolean isNotArchived = false;
		
		PHRVerification phrVerification1 = new PHRVerification();
		EntityDataGenerator.generateTestDataForModelClass(phrVerification1);
		phrVerification1.setDemographicNo(demographicNo1);
		phrVerification1.setArchived(isNotArchived);
		Date createdDate1 = new Date(dfm.parse("20110701").getTime());
		phrVerification1.setCreatedDate(createdDate1);
		phrVerification1.setVerificationLevel(authenticationLevel1);
		dao.persist(phrVerification1);
		
		PHRVerification phrVerification2 = new PHRVerification();
		EntityDataGenerator.generateTestDataForModelClass(phrVerification2);
		phrVerification2.setDemographicNo(demographicNo2);
		phrVerification2.setArchived(isArchived);
		Date createdDate2 = new Date(dfm.parse("20100701").getTime());
		phrVerification2.setCreatedDate(createdDate2);
		phrVerification2.setVerificationBy(authenticationLevel2);
		dao.persist(phrVerification2);
		
		String expectedResult = "+1";
		DemographicManager demographicManager=(DemographicManager) SpringUtils.getBean("demographicManager");
		String result = demographicManager.getPhrVerificationLevelByDemographicId(getLoggedInInfo(),demographicNo1);
		
		assertEquals(expectedResult, result);
	}
}
