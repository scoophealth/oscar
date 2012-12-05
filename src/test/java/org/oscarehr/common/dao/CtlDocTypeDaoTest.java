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

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.CtlDocType;
import org.oscarehr.util.SpringUtils;

public class CtlDocTypeDaoTest extends DaoTestFixtures {

	protected CtlDocTypeDao dao = (CtlDocTypeDao)SpringUtils.getBean("ctlDocTypeDao");

	public CtlDocTypeDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("ctl_doctype");
	}

	@Test
	public void findByStatusAndModuleTest() {
		CtlDocType tmp = new CtlDocType();
		tmp.setModule("provider");
		tmp.setDocType("test1");
		tmp.setStatus("H");
		dao.persist(tmp);
		assertNotNull(tmp.getId());

		tmp = new CtlDocType();
		tmp.setModule("provider");
		tmp.setDocType("test2");
		tmp.setStatus("I");
		dao.persist(tmp);
		assertNotNull(tmp.getId());

		int aCount=0;
		List<CtlDocType> result = dao.findByStatusAndModule(new String[]{"A"}, "provider");
		assertNotNull(result);
		aCount = result.size();

		result = dao.findByStatusAndModule(new String[]{"A","H"}, "provider");
		assertNotNull(result);
		assertEquals(aCount+1,result.size());

		result = dao.findByStatusAndModule(new String[]{"A","H","I"}, "provider");
		assertNotNull(result);
		assertEquals(aCount+2,result.size());
	}

	@Test
	public void findByDocTypeAndModuleTest() {
		CtlDocType tmp = new CtlDocType();
		tmp.setModule("provider");
		tmp.setDocType("test1");
		tmp.setStatus("H");
		dao.persist(tmp);
		assertNotNull(tmp.getId());

		assertEquals(dao.findByDocTypeAndModule("test1", "provider").size(),1);
		assertEquals(dao.findByDocTypeAndModule("test1", "demographic").size(),0);
	}
}
