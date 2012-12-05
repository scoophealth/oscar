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

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.SurveyTestInstance;
import org.oscarehr.util.SpringUtils;

public class SurveyTestInstanceDaoTest extends DaoTestFixtures {

	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");
	protected SurveyTestInstanceDao dao = SpringUtils.getBean(SurveyTestInstanceDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("survey_test_instance","survey_test_data");
	}

	@Test
	public void testCreate() throws Exception {
		SurveyTestInstance entity = new SurveyTestInstance();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testGetSurveyInstance() throws Exception {
		
		int clientId1 = 101;
		int clientId2 = 202; 
		
		int surveyId1 = 111;
		int surveyId2 = 222;
		
		Date dateCreated1 = new Date(dfm.parse("20080101").getTime());
		Date dateCreated2 = new Date(dfm.parse("20110601").getTime());
		Date dateCreated3 = new Date(dfm.parse("20100701").getTime());
		Date dateCreated4 = new Date(dfm.parse("20101101").getTime());
		
		SurveyTestInstance surveyTestInstance1 = new SurveyTestInstance();
		EntityDataGenerator.generateTestDataForModelClass(surveyTestInstance1);
		surveyTestInstance1.setClientId(clientId2);
		surveyTestInstance1.setSurveyId(surveyId1);
		surveyTestInstance1.setDateCreated(dateCreated1);
		dao.persist(surveyTestInstance1);
		
		SurveyTestInstance surveyTestInstance2 = new SurveyTestInstance();
		EntityDataGenerator.generateTestDataForModelClass(surveyTestInstance2);
		surveyTestInstance2.setClientId(clientId1);
		surveyTestInstance2.setSurveyId(surveyId1);
		surveyTestInstance2.setDateCreated(dateCreated2);
		dao.persist(surveyTestInstance2);
		
		SurveyTestInstance surveyTestInstance3 = new SurveyTestInstance();
		EntityDataGenerator.generateTestDataForModelClass(surveyTestInstance3);
		surveyTestInstance3.setClientId(clientId1);
		surveyTestInstance3.setSurveyId(surveyId2);
		surveyTestInstance3.setDateCreated(dateCreated3);
		dao.persist(surveyTestInstance3);
		
		SurveyTestInstance surveyTestInstance4 = new SurveyTestInstance();
		EntityDataGenerator.generateTestDataForModelClass(surveyTestInstance4);
		surveyTestInstance4.setClientId(clientId1);
		surveyTestInstance4.setSurveyId(surveyId1);
		surveyTestInstance4.setDateCreated(dateCreated4);
		dao.persist(surveyTestInstance4);
		
		SurveyTestInstance expectedResult = surveyTestInstance2;
		SurveyTestInstance result = dao.getSurveyInstance(surveyId1, clientId1);
		
		assertEquals(expectedResult, result);
	}
}
