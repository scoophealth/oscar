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
package org.oscarehr.util;

import static junit.framework.Assert.*;

import java.util.Date;
import java.util.Map;

import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;

import oscar.util.Textualizer;

public class TextualizerTest {

	@Test
	public void testConversion() throws Exception {
		TestClass test = new TestClass();
		EntityDataGenerator.generateTestDataForModelClass(test);
		// # of MS is not converted - must be made 0 
		test.setDateObj(new Date((test.getDateObj().getTime() / 1000) * 1000));
		Textualizer textualizer = new Textualizer();
		Map<String, String> map = textualizer.toMap(test);
		TestClass testCheck = new TestClass();
		textualizer.fromMap(testCheck, map);
		assertEquals(test, testCheck);
	}

	@Test
	public void testProps() throws Exception {
		TestClass test = new TestClass();
		EntityDataGenerator.generateTestDataForModelClass(test);
		Textualizer textualizer = new Textualizer();
		Map<String, String> map = textualizer.toMap(test);
		for (String key : new String[] {"fancyColunmNameForDateObj", "integerObj", "booleanObj", "longObj", 
				"stringObj", "characterObj", "byteObj", "shortObj", "floatObj", "doubleObj", "intPrim", "booleanPrim", 
				"longPrim", "charPrim", "bytePrim", "shortPrim", "floatPrim", "doublePrim" }) {
			assertTrue("Map doesn't contain " + key + " field.", map.containsKey(key));
		}

	}

}
