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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.SecurityToken;
import org.oscarehr.util.SpringUtils;

public class SecurityTokenDaoTest extends DaoTestFixtures {

	protected SecurityTokenDao dao = SpringUtils.getBean(SecurityTokenDao.class);
	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("SecurityToken");
	}

        @Test
        public void testCreate() throws Exception {
                SecurityToken entity = new SecurityToken();
                EntityDataGenerator.generateTestDataForModelClass(entity);
                dao.persist(entity);
                assertNotNull(entity.getId());
        }

	@Test
	public void testGetByTokenAndExpiry() throws Exception {
		
		String token1 = "alpha";
		String token2 = "bravo";
		
		Date date1 = new Date(dfm.parse("20100301").getTime());
		Date date2 = new Date(dfm.parse("20110301").getTime());
		Date date3 = new Date(dfm.parse("20130301").getTime());
		Date date4 = new Date(dfm.parse("20080301").getTime());
		
		Date expiry = new Date(dfm.parse("20090301").getTime());
		
		SecurityToken securityToken1 = new SecurityToken();
		EntityDataGenerator.generateTestDataForModelClass(securityToken1);
		securityToken1.setToken(token1);
		securityToken1.setExpiry(date1);
		dao.persist(securityToken1);
		
		SecurityToken securityToken2 = new SecurityToken();
		EntityDataGenerator.generateTestDataForModelClass(securityToken2);
		securityToken2.setToken(token2);
		securityToken2.setExpiry(date2);
		dao.persist(securityToken2);
		
		SecurityToken securityToken3 = new SecurityToken();
		EntityDataGenerator.generateTestDataForModelClass(securityToken3);
		securityToken3.setToken(token1);
		securityToken3.setExpiry(date3);
		dao.persist(securityToken3);
		
		SecurityToken securityToken4 = new SecurityToken();
		EntityDataGenerator.generateTestDataForModelClass(securityToken4);
		securityToken4.setToken(token1);
		securityToken4.setExpiry(date4);
		dao.persist(securityToken4);
		
		SecurityToken result = dao.getByTokenAndExpiry(token1, expiry);
		SecurityToken expectedResult = securityToken1;
		
		assertEquals(expectedResult, result);
	}
}
