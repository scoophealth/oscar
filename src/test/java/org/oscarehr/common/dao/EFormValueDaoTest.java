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

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.EFormValue;
import org.oscarehr.util.SpringUtils;

public class EFormValueDaoTest extends DaoTestFixtures {

	protected EFormValueDao eFormValueDao = (EFormValueDao) SpringUtils.getBean("EFormValueDao");

	public EFormValueDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(new String[]{"eform_values"});
	}

	@Test
	public void testFindByFormDataIdList() {
		EFormValue v1 = generateEFormValue(1);
		EFormValue v2 = generateEFormValue(1);
		EFormValue v3 = generateEFormValue(2);
		EFormValue v4 = generateEFormValue(3);
		eFormValueDao.persist(v1);
		eFormValueDao.persist(v2);
		eFormValueDao.persist(v3);
		eFormValueDao.persist(v4);

		List<Integer> searchIds = new ArrayList<Integer>();
		searchIds.add(1);
		searchIds.add(2);
		List<EFormValue> results = eFormValueDao.findByFormDataIdList(searchIds);
		assertEquals(3,results.size());
	}

	protected EFormValue generateEFormValue(int fdid) {
		EFormValue v1 = new EFormValue();
		v1.setDemographicId(1);
		v1.setFormDataId(fdid);
		v1.setFormId(1);
		v1.setVarName("a");
		v1.setVarValue("b");

		return v1;
	}


}
