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

import static org.junit.Assert.assertNotNull;
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
import org.oscarehr.common.model.BillingCdmServiceCodes;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class BillingCdmServiceCodesDaoTest extends DaoTestFixtures {

	protected BillingCdmServiceCodesDao dao = SpringUtils.getBean(BillingCdmServiceCodesDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(false, "ctl_billingservice_age_rules", "billing_cdm_service_codes");
	}

	@Test
	public void testCreate() throws Exception {
		BillingCdmServiceCodes entity = new BillingCdmServiceCodes();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId(null);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testFindAll() throws Exception {
		
		BillingCdmServiceCodes bCSC1 = new BillingCdmServiceCodes();
		EntityDataGenerator.generateTestDataForModelClass(bCSC1);
		dao.persist(bCSC1);
		
		BillingCdmServiceCodes bCSC2 = new BillingCdmServiceCodes();
		EntityDataGenerator.generateTestDataForModelClass(bCSC2);
		dao.persist(bCSC2);
		
		BillingCdmServiceCodes bCSC3 = new BillingCdmServiceCodes();
		EntityDataGenerator.generateTestDataForModelClass(bCSC3);
		dao.persist(bCSC3);
		
		List<BillingCdmServiceCodes> expectedResult = new ArrayList<BillingCdmServiceCodes>(Arrays.asList(bCSC1, bCSC2, bCSC3));
		List<BillingCdmServiceCodes> result = dao.findAll();

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