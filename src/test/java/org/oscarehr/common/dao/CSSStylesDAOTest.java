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
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.CssStyle;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class CSSStylesDAOTest extends DaoTestFixtures {

	protected CSSStylesDAO dao = (CSSStylesDAO)SpringUtils.getBean(CSSStylesDAO.class);
	Logger logger = MiscUtils.getLogger();

	@Before
	public void setUp() throws Exception { 
		SchemaUtils.restoreTable("cssStyles");
	}

	@Test
	/**
	 * Ensures that persisted records are contained in the table.
	 * Lexicographical comparison ensures that statuses are ordered A-Z.
	 */
	public void testFindAll() {
		CssStyle style1 = new CssStyle();
		style1.setName("Style1");
		style1.setStatus(CssStyle.ACTIVE);

		CssStyle style2 = new CssStyle();
		style2.setName("Style2");
		style2.setStatus(CssStyle.DELETED);

		CssStyle style3 = new CssStyle();
		style3.setName("Style3");
		style3.setStatus(CssStyle.ACTIVE);

		dao.persist(style1);
		dao.persist(style2);
		dao.persist(style3);

		List<CssStyle> result = dao.findAll();
		List<CssStyle> expectedResult = new ArrayList<CssStyle>(Arrays.asList(style1, style3));
		
		// ensure that all persisted items are in the table
		// ensure ordered by status A-Z
		assertTrue(result.containsAll(expectedResult));
		String str1;
		String str2;
		for (int i = 0; i < result.size() - 1; i++) {
			str1 = result.get(i).getName();
			str2 = result.get(i+1).getName();
			if (str1.compareTo(str2) > -1 ){
				fail("Results not ordred by name.");
			}
		}
		assertTrue(true);
	}

}
