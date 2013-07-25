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
import org.oscarehr.common.model.CaseManagementTmpSave;
import org.oscarehr.util.SpringUtils;

public class CaseManagementTmpSaveDaoTest extends DaoTestFixtures {

	protected CaseManagementTmpSaveDao dao = SpringUtils.getBean(CaseManagementTmpSaveDao.class);


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("casemgmt_tmpsave");
	}

	@Test
	public void testCreate() throws Exception {
		CaseManagementTmpSave entity = new CaseManagementTmpSave();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testFindNoDate() throws Exception {
		
		String providerNo1 = "alpha";
		int demographicNo1 = 101, demographicNo2 = 202;
		int programId1 = 111, programId2 = 222;
		
		CaseManagementTmpSave cMTS1 = new CaseManagementTmpSave();
		EntityDataGenerator.generateTestDataForModelClass(cMTS1);
		cMTS1.setProviderNo(providerNo1);
		cMTS1.setDemographicNo(demographicNo1);
		cMTS1.setProgramId(programId2);
		dao.persist(cMTS1);
		
		CaseManagementTmpSave cMTS2 = new CaseManagementTmpSave();
		EntityDataGenerator.generateTestDataForModelClass(cMTS2);
		cMTS2.setProviderNo(providerNo1);
		cMTS2.setDemographicNo(demographicNo2);
		cMTS2.setProgramId(programId1);
		dao.persist(cMTS2);
		
		CaseManagementTmpSave cMTS3 = new CaseManagementTmpSave();
		EntityDataGenerator.generateTestDataForModelClass(cMTS3);
		cMTS3.setProviderNo(providerNo1);
		cMTS3.setDemographicNo(demographicNo1);
		cMTS3.setProgramId(programId1);
		dao.persist(cMTS3);
		
		CaseManagementTmpSave expectedResult = cMTS3;
		CaseManagementTmpSave result = dao.find(providerNo1, demographicNo1, programId1);
		
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testFindDate() throws Exception {
		
		DateFormat dfm = new SimpleDateFormat("yyyyMMdd");
		
		Date date1 = new Date(dfm.parse("20110101").getTime());
		Date date2 = new Date(dfm.parse("20010101").getTime());
		Date date3 = new Date(dfm.parse("20100101").getTime());
		
		String providerNo1 = "alpha";
		int demographicNo1 = 101, demographicNo2 = 202;
		int programId1 = 111, programId2 = 222;
		
		CaseManagementTmpSave cMTS1 = new CaseManagementTmpSave();
		EntityDataGenerator.generateTestDataForModelClass(cMTS1);
		cMTS1.setProviderNo(providerNo1);
		cMTS1.setDemographicNo(demographicNo1);
		cMTS1.setProgramId(programId2);
		cMTS1.setUpdateDate(date1);
		dao.persist(cMTS1);
		
		CaseManagementTmpSave cMTS2 = new CaseManagementTmpSave();
		EntityDataGenerator.generateTestDataForModelClass(cMTS2);
		cMTS2.setProviderNo(providerNo1);
		cMTS2.setDemographicNo(demographicNo2);
		cMTS2.setProgramId(programId1);
		cMTS2.setUpdateDate(date2);
		dao.persist(cMTS2);
		
		CaseManagementTmpSave cMTS3 = new CaseManagementTmpSave();
		EntityDataGenerator.generateTestDataForModelClass(cMTS3);
		cMTS3.setProviderNo(providerNo1);
		cMTS3.setDemographicNo(demographicNo1);
		cMTS3.setProgramId(programId1);
		cMTS3.setUpdateDate(date3);
		dao.persist(cMTS3);
		
		CaseManagementTmpSave expectedResult = cMTS3;
		CaseManagementTmpSave result = dao.find(providerNo1, demographicNo1, programId1, date3);
				
		assertEquals(expectedResult, result);
	}	
}