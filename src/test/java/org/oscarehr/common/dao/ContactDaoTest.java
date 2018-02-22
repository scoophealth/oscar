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
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Contact;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ContactDaoTest extends DaoTestFixtures {

	protected ContactDao dao = (ContactDao)SpringUtils.getBean(ContactDao.class);
	Logger logger = MiscUtils.getLogger();

	@Override
	@Test
	public void doSimpleExceptionTest() {
		MiscUtils.getLogger().error("Unable to run doSimpleExceptionTest on this DAO");
	}
	
	@Before
	public void setUp() throws Exception {
		SchemaUtils.restoreTable("Contact");
	}

	@Test
	/**
	 * Ensures that search() method returns accurate list of contacts where
	 * the keyword is comma separated (lastname, firstname), and searchMode is search_name
	 * @throws Exception
	 */
	public void testSearch_SearchName_LastNameFirstName() throws Exception {
		String keyword = "Smith, Jon";
		String orderBy = "c.id";
		String searchMode = "search_name";

		Contact contact1 = new Contact();
		EntityDataGenerator.generateTestDataForModelClass(contact1);
		contact1.setLastName("Smith");
		contact1.setFirstName("Jon");

		Contact contact2 = new Contact();
		EntityDataGenerator.generateTestDataForModelClass(contact2);
		contact2.setLastName("Smith");
		contact2.setFirstName("Jon");

		Contact contact3 = new Contact();
		EntityDataGenerator.generateTestDataForModelClass(contact3);
		contact3.setLastName("Smith");
		contact3.setFirstName("Jim");

		dao.persist(contact1);
		dao.persist(contact2);
		dao.persist(contact3);

		List<Contact> result = dao.search(searchMode, orderBy, keyword,null,null,null); 
		List<Contact> expectedResult = new ArrayList<Contact>(Arrays.asList(
				contact1,
				contact2
				));

		// fail if list sizes aren't the same
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}

		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items not ordered by ID");
				fail("Items not ordered by ID");
			}
		}
		assertTrue(true);
	}


	@Test
	/**
	 * Ensures that search() method returns accurate list of contacts where
	 * the keyword is lastname only, and searchMode is search_name
	 * @throws Exception
	 */
	public void testSearch_SearchName_LastName() throws Exception {
		String keyword = "Smith";
		String orderBy = "c.id";
		String searchMode = "search_name";

		Contact contact1 = new Contact();
		EntityDataGenerator.generateTestDataForModelClass(contact1);
		contact1.setLastName("Smith");
		dao.persist(contact1);
		
		Contact contact2 = new Contact();
		EntityDataGenerator.generateTestDataForModelClass(contact2);
		contact2.setLastName("Jackson");
		dao.persist(contact2);
		
		Contact contact3 = new Contact();
		EntityDataGenerator.generateTestDataForModelClass(contact3);
		contact3.setLastName("Smith");
		dao.persist(contact3);

		List<Contact> result = dao.search(searchMode, orderBy, keyword,null,null,null); 
		List<Contact> expectedResult = new ArrayList<Contact>(Arrays.asList(
				contact1,
				contact3
				));


		// fail if list sizes aren't the same
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}

		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items not ordered by ID");
				fail("Items not ordered by ID");
			}
		}
		assertTrue(true);
	}
	
	@Test
	/**
	 * Ensures that search() method returns accurate list of contacts where
	 * the searchMode is anything other than search_name
	 * @throws Exception
	 */
	public void testSearch_NotSearchName() throws Exception {
		String keyword = "ON";
		String orderBy = "c.id";
		String searchMode = "province";

		// Wrong province
		Contact contact1 = new Contact();
		EntityDataGenerator.generateTestDataForModelClass(contact1);
		contact1.setProvince("BC");
		
		Contact contact2 = new Contact();
		EntityDataGenerator.generateTestDataForModelClass(contact2);
		contact2.setProvince("ON");

		Contact contact3 = new Contact();
		EntityDataGenerator.generateTestDataForModelClass(contact3);
		contact3.setProvince("ON");

		dao.persist(contact1);
		dao.persist(contact2);
		dao.persist(contact3);

		List<Contact> result = dao.search(searchMode, orderBy, keyword,null,null,null); 
		List<Contact> expectedResult = new ArrayList<Contact>(Arrays.asList(
				contact2,
				contact3
				));

		// fail if list sizes aren't the same
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
	
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items not ordered by ID");
				fail("Items not ordered by ID");
			}
		}
		assertTrue(true);
	}

}
