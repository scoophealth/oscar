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
import org.oscarehr.common.model.EyeformMacro;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class EyeformMacroDaoTest extends DaoTestFixtures {

	protected EyeformMacroDao dao = SpringUtils.getBean(EyeformMacroDao.class);


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("eyeform_macro_def", "eyeform_macro_billing");
	}

        @Test
        public void testCreate() throws Exception {
                EyeformMacro entity = new EyeformMacro();
                EntityDataGenerator.generateTestDataForModelClass(entity);
                dao.persist(entity);
                assertNotNull(entity.getId());
        }

	@Test
	public void testGetMacros() throws Exception {
		
		String macroName1 = "bravo";
		String macroName2 = "charlie";
		String macroName3 = "alpha";

		EyeformMacro eyeformMacro1 = new EyeformMacro();
		EntityDataGenerator.generateTestDataForModelClass(eyeformMacro1);
		eyeformMacro1.setMacroName(macroName1);
		dao.persist(eyeformMacro1);
		
		EyeformMacro eyeformMacro2 = new EyeformMacro();
		EntityDataGenerator.generateTestDataForModelClass(eyeformMacro2);
		eyeformMacro2.setMacroName(macroName2);
		dao.persist(eyeformMacro2);
		
		EyeformMacro eyeformMacro3 = new EyeformMacro();
		EntityDataGenerator.generateTestDataForModelClass(eyeformMacro3);
		eyeformMacro3.setMacroName(macroName3);
		dao.persist(eyeformMacro3);
		
		List<EyeformMacro> expectedResult = new ArrayList<EyeformMacro>(Arrays.asList(eyeformMacro3, eyeformMacro1, eyeformMacro2));
		List<EyeformMacro> result = dao.getMacros();

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
