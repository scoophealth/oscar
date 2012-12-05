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
/**
 * @author Shazib
 */
package org.oscarehr.common.dao;

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
import org.oscarehr.common.model.TicklerTextSuggest;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class TicklerTextSuggestDaoTest extends DaoTestFixtures {
	
	protected TicklerTextSuggestDao dao = (TicklerTextSuggestDao)SpringUtils.getBean(TicklerTextSuggestDao.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(false, "tickler_text_suggest");
		
	}
	@Test
	public void testGetActiveTicklerTextSuggests() throws Exception {
		
		boolean isActive = true;
		String suggestedTextActive1 = "This tickler is active";
		String suggestedTextActive2 = "This tickler is also active";
		String suggestedTextNotActive1 = "This tickler is not active";
		
		TicklerTextSuggest tickler1 = new TicklerTextSuggest();
		EntityDataGenerator.generateTestDataForModelClass(tickler1);
		tickler1.setActive(isActive);
		tickler1.setSuggestedText(suggestedTextActive1);
		dao.persist(tickler1);
		
		TicklerTextSuggest tickler2 = new TicklerTextSuggest();
		EntityDataGenerator.generateTestDataForModelClass(tickler2);
		tickler2.setActive(isActive);
		tickler2.setSuggestedText(suggestedTextActive2);
		dao.persist(tickler2);
		
		TicklerTextSuggest tickler3 = new TicklerTextSuggest();
		EntityDataGenerator.generateTestDataForModelClass(tickler3);
		tickler3.setActive(!isActive);
		tickler3.setSuggestedText(suggestedTextNotActive1);
		dao.persist(tickler3);
		
		List<TicklerTextSuggest> expectedResult = new ArrayList<TicklerTextSuggest>(Arrays.asList(tickler1, tickler2));
		List<TicklerTextSuggest> result = dao.getActiveTicklerTextSuggests();
		
		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match." + result.size() +"    "+expectedResult.size());
			fail("Array sizes do not match.");
		}

		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items do not match.");
				fail("Items do not match.");
			}
		}
		assertTrue(true);
	}

	@Test
	public void testGetInactiveTicklerTextSuggests() throws Exception {
		boolean isActive = false;
		String suggestedTextActive1 = "This tickler is not active";
		String suggestedTextActive2 = "This tickler is also not active";
		String suggestedTextNotActive1 = "This tickler is active";
		
		TicklerTextSuggest tickler1 = new TicklerTextSuggest();
		EntityDataGenerator.generateTestDataForModelClass(tickler1);
		tickler1.setActive(isActive);
		tickler1.setSuggestedText(suggestedTextActive1);
		dao.persist(tickler1);
		
		TicklerTextSuggest tickler2 = new TicklerTextSuggest();
		EntityDataGenerator.generateTestDataForModelClass(tickler2);
		tickler2.setActive(isActive);
		tickler2.setSuggestedText(suggestedTextActive2);
		dao.persist(tickler2);
		
		TicklerTextSuggest tickler3 = new TicklerTextSuggest();
		EntityDataGenerator.generateTestDataForModelClass(tickler3);
		tickler3.setActive(!isActive);
		tickler3.setSuggestedText(suggestedTextNotActive1);
		dao.persist(tickler3);
		
		List<TicklerTextSuggest> expectedResult = new ArrayList<TicklerTextSuggest>(Arrays.asList(tickler2, tickler1));
		List<TicklerTextSuggest> result = dao.getInactiveTicklerTextSuggests();
		
		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}

		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items do not match.");
				fail("Items do not match.");
			}
		}
		assertTrue(true);
	}
}