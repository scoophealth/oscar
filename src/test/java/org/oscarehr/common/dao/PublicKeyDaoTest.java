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
import org.oscarehr.common.model.PublicKey;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class PublicKeyDaoTest extends DaoTestFixtures {
	
	protected PublicKeyDao dao =(PublicKeyDao)SpringUtils.getBean(PublicKeyDao.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("publicKeys");
	}

	@Test
	public void testFindAll() throws Exception {
		
		PublicKey publicKey1 = new PublicKey();
		EntityDataGenerator.generateTestDataForModelClass(publicKey1);
		publicKey1.setService("100");
		dao.persist(publicKey1);
		
		PublicKey publicKey2 = new PublicKey();
		EntityDataGenerator.generateTestDataForModelClass(publicKey2);
		publicKey2.setService("200");
		dao.persist(publicKey2);
		
		PublicKey publicKey3 = new PublicKey();
		EntityDataGenerator.generateTestDataForModelClass(publicKey3);
		publicKey3.setService("300");
		dao.persist(publicKey3);
		
		List<PublicKey> expectedResult = new ArrayList<PublicKey>(Arrays.asList(publicKey1, publicKey2, publicKey3));
		List<PublicKey> result = dao.findAll();

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
