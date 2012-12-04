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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.RemoteReferral;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class RemoteReferralDaoTest extends DaoTestFixtures {

	protected RemoteReferralDao dao = (RemoteReferralDao)SpringUtils.getBean("remoteReferralDao");

	public RemoteReferralDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(new String[]{"RemoteReferral"});
	}

        @Test
        public void testCreate() throws Exception {
                RemoteReferral entity = new RemoteReferral();
                 EntityDataGenerator.generateTestDataForModelClass(entity);
                 dao.persist(entity);
                 assertNotNull(entity.getId());
        }

	@Test
	public void testFindByFacilityIdDemogprahicId() throws Exception {
		
		int demographicId1 = 100;
		int demographicId2 = 200;
		
		int facilityId1 = 111;
		
		RemoteReferral remoteReferral1 = new RemoteReferral();
		EntityDataGenerator.generateTestDataForModelClass(remoteReferral1);
		remoteReferral1.setDemographicId(demographicId1);
		remoteReferral1.setFacilityId(facilityId1);
		dao.persist(remoteReferral1);
		
		RemoteReferral remoteReferral2 = new RemoteReferral();
		EntityDataGenerator.generateTestDataForModelClass(remoteReferral2);
		remoteReferral2.setDemographicId(demographicId2);
		remoteReferral2.setFacilityId(facilityId1);
		dao.persist(remoteReferral2);
		
		RemoteReferral remoteReferral3 = new RemoteReferral();
		EntityDataGenerator.generateTestDataForModelClass(remoteReferral3);
		remoteReferral3.setDemographicId(demographicId1);
		remoteReferral3.setFacilityId(facilityId1);
		dao.persist(remoteReferral3);
		
		List<RemoteReferral> expectedResult = new ArrayList<RemoteReferral>(Arrays.asList(remoteReferral1, remoteReferral3));
		List<RemoteReferral> result = dao.findByFacilityIdDemogprahicId(facilityId1, demographicId1);

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
