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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.DataExport;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class DataExportDaoTest extends DaoTestFixtures {

	protected DataExportDao dao = (DataExportDao)SpringUtils.getBean(DataExportDao.class);
	Logger logger = MiscUtils.getLogger();

	@Before
	public void setUp() throws Exception {
		SchemaUtils.restoreTable("dataExport");
	}

	@Test
	public void testFindAll() {
		//Date date1 = new Date(dfm.parse("20091231").getTime());

		DataExport de1 = new DataExport();
		Timestamp ts1 = new Timestamp(1000);
		de1.setDaterun(ts1);

		DataExport de2 = new DataExport();
		Timestamp ts2 = new Timestamp(2000);
		de2.setDaterun(ts2);

		DataExport de3 = new DataExport();
		Timestamp ts3 = new Timestamp(1500);
		de3.setDaterun(ts3);

		dao.persist(de1);
		dao.persist(de2);
		dao.persist(de3);
		
		List<DataExport> expectedResult = new ArrayList<DataExport>(Arrays.asList(de1, de3, de2));
		List<DataExport> result = dao.findAll();

		// fail if list sizes aren't the same
		if (result.size() != expectedResult.size()) {
			fail("Array sizes do not match.");
		}

		/*
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items not sorted by DateRun.");
				fail("Items not sorted by DateRun.");
			}
		}
		*/
		assertTrue(true);
	}

	@Test
	public void testFindAllByType() {
		String type = "typeA";

		DataExport de1 = new DataExport();
		Timestamp ts1 = new Timestamp(1000);
		de1.setDaterun(ts1);
		de1.setType(type);

		DataExport de2 = new DataExport();
		Timestamp ts2 = new Timestamp(2000);
		de2.setDaterun(ts2);
		de2.setType("typeB");

		DataExport de3 = new DataExport();
		Timestamp ts3 = new Timestamp(1500);
		de3.setDaterun(ts3);
		de3.setType(type);

		dao.persist(de1);
		dao.persist(de2);
		dao.persist(de3);
		
		List<DataExport> result = dao.findAllByType(type);
		List<DataExport> expectedResult = new ArrayList<DataExport>(Arrays.asList(de1, de3));

		// fail if list sizes aren't the same
		if (result.size() != expectedResult.size()) {
			fail("Array sizes do not match.");
		}

		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))){
				logger.warn("Items not sorted by DateRun.");
				fail("Items not sorted by DateRun.");
			}
		}
		assertTrue(true);
	}

}
