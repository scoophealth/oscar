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

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ScratchPad;
import org.oscarehr.util.SpringUtils;

public class ScratchPadDaoTest extends DaoTestFixtures {

	protected ScratchPadDao dao = SpringUtils.getBean(ScratchPadDao.class);

	public ScratchPadDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("scratch_pad");
	}

        @Test
        public void testCreate() throws Exception {
                ScratchPad entity = new ScratchPad();
                EntityDataGenerator.generateTestDataForModelClass(entity);
                dao.persist(entity);

                assertNotNull(entity.getId());
        }

	@Test
	public void testIsScratchFilled() throws Exception {
		
		String providerNo1 = "111";
		String providerNo2 = "222";
		
		ScratchPad scratchPad1 = new ScratchPad();
		EntityDataGenerator.generateTestDataForModelClass(scratchPad1);
		scratchPad1.setProviderNo(providerNo1);
		dao.persist(scratchPad1);
		
		ScratchPad scratchPad2 = new ScratchPad();
		EntityDataGenerator.generateTestDataForModelClass(scratchPad2);
		scratchPad2.setProviderNo(providerNo2);
		dao.persist(scratchPad2);
		
		boolean expectedResult = true;
		boolean result = dao.isScratchFilled(providerNo1);
		
		assertEquals(expectedResult, result);
	}

	@Test
	public void testFindByProviderNo() throws Exception {
		
		String providerNo1 = "111";
		String providerNo2 = "222";
		
		ScratchPad scratchPad1 = new ScratchPad();
		EntityDataGenerator.generateTestDataForModelClass(scratchPad1);
		scratchPad1.setProviderNo(providerNo1);
		dao.persist(scratchPad1);
		
		ScratchPad scratchPad2 = new ScratchPad();
		EntityDataGenerator.generateTestDataForModelClass(scratchPad2);
		scratchPad2.setProviderNo(providerNo2);
		dao.persist(scratchPad2);
		
		ScratchPad expectedResult = scratchPad1;
		ScratchPad result = dao.findByProviderNo(providerNo1);
		
		assertEquals(expectedResult, result);
	}
}
