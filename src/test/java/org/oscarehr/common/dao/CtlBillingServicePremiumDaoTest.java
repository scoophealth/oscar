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
import org.oscarehr.common.model.CtlBillingServicePremium;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class CtlBillingServicePremiumDaoTest extends DaoTestFixtures {

	protected CtlBillingServicePremiumDao dao = SpringUtils.getBean(CtlBillingServicePremiumDao.class);


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("ctl_billingservice_premium","billingservice");
	}

       @Test
        public void testCreate() throws Exception {
                CtlBillingServicePremium entity = new CtlBillingServicePremium();
                EntityDataGenerator.generateTestDataForModelClass(entity);
                dao.persist(entity);
                assertNotNull(entity.getId());
        }

	@Test
	public void testFindByServiceCode() throws Exception {
		
		String serviceCode1 = "alpha";
		String serviceCode2 = "bravo";
		
		CtlBillingServicePremium ctlBillingServicePremium1 = new CtlBillingServicePremium();
		EntityDataGenerator.generateTestDataForModelClass(ctlBillingServicePremium1);
		ctlBillingServicePremium1.setServiceCode(serviceCode1);
		dao.persist(ctlBillingServicePremium1);
		
		CtlBillingServicePremium ctlBillingServicePremium2 = new CtlBillingServicePremium();
		EntityDataGenerator.generateTestDataForModelClass(ctlBillingServicePremium2);
		ctlBillingServicePremium2.setServiceCode(serviceCode1);
		dao.persist(ctlBillingServicePremium2);
		
		CtlBillingServicePremium ctlBillingServicePremium3 = new CtlBillingServicePremium();
		EntityDataGenerator.generateTestDataForModelClass(ctlBillingServicePremium3);
		ctlBillingServicePremium3.setServiceCode(serviceCode2);
		dao.persist(ctlBillingServicePremium3);
		
		CtlBillingServicePremium ctlBillingServicePremium4 = new CtlBillingServicePremium();
		EntityDataGenerator.generateTestDataForModelClass(ctlBillingServicePremium4);
		ctlBillingServicePremium4.setServiceCode(serviceCode1);
		dao.persist(ctlBillingServicePremium4);
		
		List<CtlBillingServicePremium> expectedResult = new ArrayList<CtlBillingServicePremium>(Arrays.asList(ctlBillingServicePremium1, ctlBillingServicePremium2, ctlBillingServicePremium4));
		List<CtlBillingServicePremium> result = dao.findByServiceCode(serviceCode1);

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
