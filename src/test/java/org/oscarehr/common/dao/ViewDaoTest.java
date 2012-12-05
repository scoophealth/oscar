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

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.View;
import org.oscarehr.util.SpringUtils;

public class ViewDaoTest extends DaoTestFixtures {

	protected ViewDao dao = SpringUtils.getBean(ViewDao.class);

	public ViewDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("view");
	}

	@Test
	public void testCreate() throws Exception {
		View entity = new View();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
	
	@Test
	public void testGetView() throws Exception {
		
		String role1 = "alpha";
		String role2 = "bravo";
		
		String viewName1 = "sigma";
		String viewName2 = "delta";
		
		String name1 = "gamma";
		String name2 = "zeta";
		
		View view1 = new View();
		EntityDataGenerator.generateTestDataForModelClass(view1);
		view1.setRole(role1);
		view1.setView_name(viewName1);
		view1.setName(name1);
		dao.persist(view1);
		
		View view2 = new View();
		EntityDataGenerator.generateTestDataForModelClass(view2);
		view2.setRole(role2);
		view2.setView_name(viewName2);
		view2.setName(name2);
		dao.persist(view2);
		
		Map<String,View> expectedResult = new HashMap<String,View>();
		expectedResult.put(name1, view1);
		Map<String,View> result = dao.getView(viewName1, role1);
		
		assertEquals(expectedResult, result);
	}
}
