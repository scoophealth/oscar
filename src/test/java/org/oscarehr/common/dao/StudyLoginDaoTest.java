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
import org.oscarehr.common.model.StudyLogin;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class StudyLoginDaoTest extends DaoTestFixtures {
	protected StudyLoginDao dao = SpringUtils.getBean(StudyLoginDao.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("studylogin");
	}

	@Test
	public void testFind() throws Exception {
		
		String providerNo1 = "101";
		String providerNo2 = "202";
		
		int studyNo1 = 111;
		int studyNo2 = 222;
		
		int current1 = 1;
		int current2 = 2;
		
		StudyLogin studyLogin1 = new StudyLogin();
		EntityDataGenerator.generateTestDataForModelClass(studyLogin1);
		studyLogin1.setProviderNo(providerNo1);
		studyLogin1.setStudyNo(studyNo1);
		studyLogin1.setCurrent1(current1);
		dao.persist(studyLogin1);
		
		StudyLogin studyLogin2 = new StudyLogin();
		EntityDataGenerator.generateTestDataForModelClass(studyLogin2);
		studyLogin2.setProviderNo(providerNo2);
		studyLogin2.setStudyNo(studyNo2);
		studyLogin2.setCurrent1(current2);
		dao.persist(studyLogin2);
		
		StudyLogin studyLogin3 = new StudyLogin();
		EntityDataGenerator.generateTestDataForModelClass(studyLogin3);
		studyLogin3.setProviderNo(providerNo1);
		studyLogin3.setStudyNo(studyNo1);
		studyLogin3.setCurrent1(current1);
		dao.persist(studyLogin3);
		
		StudyLogin studyLogin4 = new StudyLogin();
		EntityDataGenerator.generateTestDataForModelClass(studyLogin4);
		studyLogin4.setProviderNo(providerNo1);
		studyLogin4.setStudyNo(studyNo1);
		studyLogin4.setCurrent1(current2);
		dao.persist(studyLogin4);
		
		List<StudyLogin> expectedResult = new ArrayList<StudyLogin>(Arrays.asList(studyLogin1, studyLogin3));
		List<StudyLogin> result = dao.find(providerNo1, studyNo1);

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