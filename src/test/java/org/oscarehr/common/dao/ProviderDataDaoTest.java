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
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ProviderData;
import org.oscarehr.util.SpringUtils;

public class ProviderDataDaoTest extends DaoTestFixtures {

	protected ProviderDataDao dao = SpringUtils.getBean(ProviderDataDao.class);

	@Before
	public void before() throws Exception {
		this.beforeForInnoDB();
		SchemaUtils.restoreTable("provider","providersite","secUserRole");
	}

	@Test
	public void testFindByTypeAndOhip() {
		List<ProviderData> data = dao.findByTypeAndOhip("doctor", "OHIP NO");
		assertNotNull(data);
	}
	
	@Test
	public void testFindByType() {
		List<ProviderData> data = dao.findByType("doctor");
		assertNotNull(data);
	}
	
	@Test
	public void testFindByName() {
		List<ProviderData> data = dao.findByName(null, null, false);
		assertNotNull(data);
		
		data = dao.findByName(null, null, true);
		assertNotNull(data);
		
		data = dao.findByName(null, "FIRST", true);
		assertNotNull(data);
		
		data = dao.findByName(null, "FIRST", false);
		assertNotNull(data);
		
		data = dao.findByName("LAST", null, false);
		assertNotNull(data);
		
		data = dao.findByName("LAST", null, true);
		assertNotNull(data);
		
		data = dao.findByName("LAST", "FIRST", true);
		assertNotNull(data);
		
		data = dao.findByName("LAST", "FIRST", false);
		assertNotNull(data);
	}
	
	@Test
	public void testFindAll() {
		List<ProviderData> data = dao.findAll(true);
		assertNotNull(data);
		
		data = dao.findAll(false);
		assertNotNull(data);
	}
	
	@Test
	public void testGetLastId() {
		ProviderData pd = newProvider("-1001");
		dao.persist(pd);
		
		pd = newProvider("-2");
		dao.persist(pd);
		
		pd = newProvider("1");
		dao.persist(pd);
		
		Integer id = dao.getLastId();
		assertEquals(new Integer(-1001), id);
	}

	protected ProviderData newProvider(String id) {
	    ProviderData result = new ProviderData();
	    result.set(id);
	    try {
	        EntityDataGenerator.generateTestDataForModelClass(result);
        } catch (Exception e) {
        	fail();
        }	    
	    return result;
    }
}
