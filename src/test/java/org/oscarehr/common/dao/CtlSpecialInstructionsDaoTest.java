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
import org.oscarehr.common.model.CtlSpecialInstructions;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class CtlSpecialInstructionsDaoTest extends DaoTestFixtures {

	protected CtlSpecialInstructionsDao dao = (CtlSpecialInstructionsDao)SpringUtils.getBean("ctlSpecialInstructionsDao");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(false, "ctl_specialinstructions");
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

		CtlSpecialInstructions ctlSpecialInstructions1 = new CtlSpecialInstructions();
		EntityDataGenerator.generateTestDataForModelClass(ctlSpecialInstructions1);
		dao.persist(ctlSpecialInstructions1);
		
		CtlSpecialInstructions ctlSpecialInstructions2 = new CtlSpecialInstructions();
		EntityDataGenerator.generateTestDataForModelClass(ctlSpecialInstructions2);
		dao.persist(ctlSpecialInstructions2);
		
		CtlSpecialInstructions ctlSpecialInstructions3 = new CtlSpecialInstructions();
		EntityDataGenerator.generateTestDataForModelClass(ctlSpecialInstructions3);
		dao.persist(ctlSpecialInstructions3);
		
		CtlSpecialInstructions ctlSpecialInstructions4 = new CtlSpecialInstructions();
		EntityDataGenerator.generateTestDataForModelClass(ctlSpecialInstructions4);
		dao.persist(ctlSpecialInstructions4);
		
		CtlSpecialInstructions ctlSpecialInstructions5 = new CtlSpecialInstructions();
		EntityDataGenerator.generateTestDataForModelClass(ctlSpecialInstructions5);
		dao.persist(ctlSpecialInstructions5);
		
		List<CtlSpecialInstructions> expectedResult = new ArrayList<CtlSpecialInstructions>(Arrays.asList(ctlSpecialInstructions1, ctlSpecialInstructions2, ctlSpecialInstructions3, ctlSpecialInstructions4, ctlSpecialInstructions5));
		List<CtlSpecialInstructions> result = dao.findAll();

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
