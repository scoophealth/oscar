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

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.CtlSpecialInstructions;
import org.oscarehr.util.SpringUtils;

public class CtlSpecialInstructionsDaoTest extends DaoTestFixtures {

	private CtlSpecialInstructionsDao dao = (CtlSpecialInstructionsDao)SpringUtils.getBean("ctlSpecialInstructionsDao");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("ctl_specialinstructions");
	}

	@Test
	public void testCreate() throws Exception {
		CtlSpecialInstructions entity = new CtlSpecialInstructions();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	@Test
	public void testFindAll() throws Exception {

		int startNo = dao.findAll().size();

		CtlSpecialInstructions entity = new CtlSpecialInstructions();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());

		List<CtlSpecialInstructions> list = dao.findAll();

		assertNotNull(list);
		assertEquals(list.size(),startNo+1);
	}
}
