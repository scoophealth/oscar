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
package org.oscarehr.billing.CA.BC.dao;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.billing.CA.BC.model.BillingHistory;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.SpringUtils;

public class BillingHistoryDaoTest extends DaoTestFixtures {

	public BillingHistoryDao dao = SpringUtils.getBean(BillingHistoryDao.class);

	public BillingHistoryDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("billing_history", "billing_payment_type","billingmaster", "wcb", "billing");
	}

	@Test
	public void testCreate() throws Exception {
		BillingHistory entity = new BillingHistory();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	@Test
	public void testFindByBillingMasterNo() {
		List<Object[]> bs = dao.findByBillingMasterNo(100);
		assertNotNull(bs);
	}
	
	@Test
	public void testGetTotalPaidFromHistory() {
		Double dd = dao.getTotalPaidFromHistory(100, true);
		assertNotNull(dd);
		
		dd = dao.getTotalPaidFromHistory(100, false);
		assertNotNull(dd);
	}
	
}
