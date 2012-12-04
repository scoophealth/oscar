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
import org.oscarehr.common.dao.BillingBCDao;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.SpringUtils;

public class BillingBCDaoTest extends DaoTestFixtures {

	public BillingBCDao dao = SpringUtils.getBean(BillingBCDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("billing", "ctl_billingservice", "billingservice", "billinglocation", "billingvisit", "wcb_side");
	}

	@Test
	public void testFindBillingServices() {
		List<Object[]> result = dao.findBillingServices("REG", "SVC", "TYPE");
		assertNotNull(result);
		
		result = dao.findBillingServices("ON", "SVG", "ST", "2011-09-01");
		assertNotNull(result);
	}

	@Test
	public void testFindBillingLocations() {
		List<Object[]> result = dao.findBillingLocations("ON");
		assertNotNull(result);
	}

	@Test
	public void testFindBillingVisits() {
		List<Object[]> result = dao.findBillingVisits("ON");
		assertNotNull(result);
	}

	@Test
	public void testFindInjuryLocations() {
		List<Object[]> result = dao.findInjuryLocations();
		assertNotNull(result);
	}

}
