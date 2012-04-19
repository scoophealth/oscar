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
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.DemographicCust;
import org.oscarehr.util.SpringUtils;

public class DemographicCustDaoTest extends DaoTestFixtures {

	private DemographicCustDao dao = (DemographicCustDao)SpringUtils.getBean("demographicCustDao");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("demographiccust");
	}

	@Test
	public void testCreate() throws Exception {
		DemographicCust entity = new DemographicCust();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId(1);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	@Test
	public void testFindMultipleMidwife() throws Exception {
		DemographicCust entity = new DemographicCust();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId(1);
		entity.setMidwife("a");
		dao.persist(entity);

		entity = new DemographicCust();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId(2);
		entity.setMidwife("a");
		dao.persist(entity);

		entity = new DemographicCust();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setId(3);
		entity.setMidwife("b");
		dao.persist(entity);

		List<Integer> dnos = new ArrayList<Integer>();
		dnos.add(1); dnos.add(2); dnos.add(3);

		List<DemographicCust> dc = dao.findMultipleMidwife(dnos, "a");
		assertEquals(dc.size(),2);
	}

}
