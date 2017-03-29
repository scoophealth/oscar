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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.OcanStaffForm;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class OcanStaffFormDaoTest extends DaoTestFixtures {

	protected OcanStaffFormDao dao = (OcanStaffFormDao)SpringUtils.getBean(OcanStaffFormDao.class);
	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("OcanStaffForm");
	}

	@Test
	public void testFindLatestCompletedInitialOcan() throws Exception {
		
		int facilityId1 = 100;
		
		int clientId1 = 111;
		
		String assessmentStatus1 = "Completed";
		
		String reasonForAssessment1 = "IA";
		
		Date created1 = new Date(dfm.parse("20120310").getTime());
		Date created2 = new Date(dfm.parse("20131110").getTime());
		Date created3 = new Date(dfm.parse("20120510").getTime());
		
		OcanStaffForm ocanStaffForm1 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm1);
		ocanStaffForm1.setFacilityId(facilityId1);
		ocanStaffForm1.setClientId(clientId1);
		ocanStaffForm1.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm1.setReasonForAssessment(reasonForAssessment1);
		ocanStaffForm1.setCreated(created1);
		dao.persist(ocanStaffForm1);
		
		OcanStaffForm ocanStaffForm2 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm2);
		ocanStaffForm2.setFacilityId(facilityId1);
		ocanStaffForm2.setClientId(clientId1);
		ocanStaffForm2.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm2.setReasonForAssessment(reasonForAssessment1);
		ocanStaffForm2.setCreated(created2);
		dao.persist(ocanStaffForm2);
		
		OcanStaffForm ocanStaffForm3 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm3);
		ocanStaffForm3.setFacilityId(facilityId1);
		ocanStaffForm3.setClientId(clientId1);
		ocanStaffForm3.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm3.setReasonForAssessment(reasonForAssessment1);
		ocanStaffForm3.setCreated(created3);
		dao.persist(ocanStaffForm3);

		OcanStaffForm ocanStaffForm4 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm4);
		dao.persist(ocanStaffForm4);
		
		OcanStaffForm ocanStaffForm5 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm5);
		dao.persist(ocanStaffForm5);
		
		OcanStaffForm expectedResult = ocanStaffForm2;
		OcanStaffForm result = dao.findLatestCompletedInitialOcan(facilityId1, clientId1);
		
		assertEquals(expectedResult, result);
	}

	@Test
	public void testFindLatestCompletedReassessment() throws Exception {
		
		int facilityId1 = 100;
		
		int clientId1 = 111;
		
		String assessmentStatus1 = "Completed";
		
		String reasonForAssessment1 = "RA";
		
		Date created1 = new Date(dfm.parse("20120310").getTime());
		Date created2 = new Date(dfm.parse("20131110").getTime());
		Date created3 = new Date(dfm.parse("20120510").getTime());
		
		OcanStaffForm ocanStaffForm1 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm1);
		ocanStaffForm1.setFacilityId(facilityId1);
		ocanStaffForm1.setClientId(clientId1);
		ocanStaffForm1.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm1.setReasonForAssessment(reasonForAssessment1);
		ocanStaffForm1.setCreated(created1);
		dao.persist(ocanStaffForm1);
		
		OcanStaffForm ocanStaffForm2 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm2);
		ocanStaffForm2.setFacilityId(facilityId1);
		ocanStaffForm2.setClientId(clientId1);
		ocanStaffForm2.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm2.setReasonForAssessment(reasonForAssessment1);
		ocanStaffForm2.setCreated(created2);
		dao.persist(ocanStaffForm2);
		
		OcanStaffForm ocanStaffForm3 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm3);
		ocanStaffForm3.setFacilityId(facilityId1);
		ocanStaffForm3.setClientId(clientId1);
		ocanStaffForm3.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm3.setReasonForAssessment(reasonForAssessment1);
		ocanStaffForm3.setCreated(created3);
		dao.persist(ocanStaffForm3);
		
		OcanStaffForm ocanStaffForm4 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm4);
		dao.persist(ocanStaffForm4);
		
		OcanStaffForm ocanStaffForm5 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm5);
		dao.persist(ocanStaffForm5);
		
		OcanStaffForm expectedResult = ocanStaffForm2;
		OcanStaffForm result = dao.findLatestCompletedReassessment(facilityId1, clientId1);
		
		assertEquals(expectedResult, result);
	}

	@Test
	public void testFindLatestCompletedDischargedAssessment() throws Exception {
		
		int facilityId1 = 100;
		
		int clientId1 = 111;
		
		String assessmentStatus1 = "Completed";
		
		String reasonForAssessment1 = "DIS";
		
		Date created1 = new Date(dfm.parse("20120310").getTime());
		Date created2 = new Date(dfm.parse("20131110").getTime());
		Date created3 = new Date(dfm.parse("20120510").getTime());
		
		OcanStaffForm ocanStaffForm1 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm1);
		ocanStaffForm1.setFacilityId(facilityId1);
		ocanStaffForm1.setClientId(clientId1);
		ocanStaffForm1.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm1.setReasonForAssessment(reasonForAssessment1);
		ocanStaffForm1.setCreated(created1);
		dao.persist(ocanStaffForm1);
		
		OcanStaffForm ocanStaffForm2 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm2);
		ocanStaffForm2.setFacilityId(facilityId1);
		ocanStaffForm2.setClientId(clientId1);
		ocanStaffForm2.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm2.setReasonForAssessment(reasonForAssessment1);
		ocanStaffForm2.setCreated(created2);
		dao.persist(ocanStaffForm2);
		
		OcanStaffForm ocanStaffForm3 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm3);
		ocanStaffForm3.setFacilityId(facilityId1);
		ocanStaffForm3.setClientId(clientId1);
		ocanStaffForm3.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm3.setReasonForAssessment(reasonForAssessment1);
		ocanStaffForm3.setCreated(created3);
		dao.persist(ocanStaffForm3);
		
		OcanStaffForm ocanStaffForm4 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm4);
		dao.persist(ocanStaffForm4);
		
		OcanStaffForm ocanStaffForm5 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm5);
		dao.persist(ocanStaffForm5);
		
		OcanStaffForm expectedResult = ocanStaffForm2;
		OcanStaffForm result = dao.findLatestCompletedDischargedAssessment(facilityId1, clientId1);
		
		assertEquals(expectedResult, result);
	}

	@Test
	public void testFindLatestByFacilityClient() throws Exception {
		
		int facilityId1 = 100;
		int facilityId2 = 200;
		
		int clientId1 = 111;
		int clientId2 = 222;
		
		String ocanType1 = "alpha";
		String ocanType2 = "bravo";
				
		OcanStaffForm ocanStaffForm1 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm1);
		ocanStaffForm1.setFacilityId(facilityId1);
		ocanStaffForm1.setClientId(clientId1);
		ocanStaffForm1.setOcanType(ocanType1);
		dao.persist(ocanStaffForm1);
		
		OcanStaffForm ocanStaffForm2 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm2);
		ocanStaffForm2.setFacilityId(facilityId2);
		ocanStaffForm2.setClientId(clientId2);
		ocanStaffForm2.setOcanType(ocanType2);
		dao.persist(ocanStaffForm2);
		
		OcanStaffForm ocanStaffForm3 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm3);
		ocanStaffForm3.setFacilityId(facilityId2);
		ocanStaffForm3.setClientId(clientId2);
		ocanStaffForm3.setOcanType(ocanType2);
		dao.persist(ocanStaffForm3);
					
		OcanStaffForm ocanStaffForm4 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm4);
		dao.persist(ocanStaffForm4);
		
		OcanStaffForm ocanStaffForm5 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm5);
		dao.persist(ocanStaffForm5);
		
		OcanStaffForm expectedResult = ocanStaffForm3;
		OcanStaffForm result = dao.findLatestByFacilityClient(facilityId2, clientId2, ocanType2);
		
		assertEquals(expectedResult, result);
	}

	@Test
	public void testGetLastCompletedOcanForm() throws Exception {
		
		int facilityId1 = 100;
		int facilityId2 = 200;
		
		int clientId1 = 111;
		int clientId2 = 222;
		
		String assessmentStatus1 = "Completed";
		String assessmentStatus2 = "NotCompleted";
		
		Date created1 = new Date(dfm.parse("20120310").getTime());
		Date created2 = new Date(dfm.parse("20131110").getTime());
		Date created3 = new Date(dfm.parse("20120510").getTime());
		
		OcanStaffForm ocanStaffForm1 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm1);
		ocanStaffForm1.setFacilityId(facilityId1);
		ocanStaffForm1.setClientId(clientId1);
		ocanStaffForm1.setAssessmentStatus(assessmentStatus2);
		ocanStaffForm1.setCreated(created1);
		dao.persist(ocanStaffForm1);
		
		OcanStaffForm ocanStaffForm2 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm2);
		ocanStaffForm2.setFacilityId(facilityId2);
		ocanStaffForm2.setClientId(clientId2);
		ocanStaffForm2.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm2.setCreated(created3);
		dao.persist(ocanStaffForm2);
		
		OcanStaffForm ocanStaffForm3 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm3);
		ocanStaffForm3.setFacilityId(facilityId2);
		ocanStaffForm3.setClientId(clientId2);
		ocanStaffForm3.setCreated(created2);
		ocanStaffForm3.setAssessmentStatus(assessmentStatus1);
		dao.persist(ocanStaffForm3);
					
		OcanStaffForm ocanStaffForm4 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm4);
		dao.persist(ocanStaffForm4);
		
		OcanStaffForm ocanStaffForm5 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm5);
		dao.persist(ocanStaffForm5);
		
		OcanStaffForm expectedResult = ocanStaffForm3;
		OcanStaffForm result = dao.getLastCompletedOcanForm(facilityId2, clientId2);
		assertEquals(expectedResult, result);
	}

	@Test
	public void testGetLastCompletedOcanFormByOcanType() throws Exception {
		
		int facilityId1 = 100;
		int facilityId2 = 200;
		
		int clientId1 = 111;
		int clientId2 = 222;
		
		String ocanType1 = "alpha";
		String ocanType2 = "bravo";
		
		String assessmentStatus1 = "Completed";
		String assessmentStatus2 = "NotCompleted";
		
		Date created1 = new Date(dfm.parse("20120310").getTime());
		Date created2 = new Date(dfm.parse("20131110").getTime());
		Date created3 = new Date(dfm.parse("20120510").getTime());
		
		OcanStaffForm ocanStaffForm1 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm1);
		ocanStaffForm1.setFacilityId(facilityId1);
		ocanStaffForm1.setClientId(clientId1);
		ocanStaffForm1.setOcanType(ocanType2);
		ocanStaffForm1.setAssessmentStatus(assessmentStatus2);
		ocanStaffForm1.setCreated(created1);
		dao.persist(ocanStaffForm1);
		
		OcanStaffForm ocanStaffForm2 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm2);
		ocanStaffForm2.setFacilityId(facilityId2);
		ocanStaffForm2.setClientId(clientId2);
		ocanStaffForm2.setOcanType(ocanType1);
		ocanStaffForm2.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm2.setCreated(created3);
		dao.persist(ocanStaffForm2);
		
		OcanStaffForm ocanStaffForm3 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm3);
		ocanStaffForm3.setFacilityId(facilityId2);
		ocanStaffForm3.setClientId(clientId2);
		ocanStaffForm3.setOcanType(ocanType2);
		ocanStaffForm3.setCreated(created2);
		ocanStaffForm3.setAssessmentStatus(assessmentStatus1);
		dao.persist(ocanStaffForm3);
					
		OcanStaffForm ocanStaffForm4 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm4);
		dao.persist(ocanStaffForm4);
		
		OcanStaffForm ocanStaffForm5 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm5);
		dao.persist(ocanStaffForm5);
		
		OcanStaffForm expectedResult = ocanStaffForm3;
		OcanStaffForm result = dao.getLastCompletedOcanFormByOcanType(facilityId2, clientId2, ocanType2);
		assertEquals(expectedResult, result);
	}

	@Test
	public void testFindByFacilityClient() throws Exception {
		
		int facilityId1 = 100;
		int facilityId2 = 200;
		
		int clientId1 = 111;
		int clientId2 = 222;
		
		int assessmentId1 = 101;
		int assessmentId2 = 202;
		int assessmentId3 = 303;
		
		String ocanType1 = "alpha";
		String ocanType2 = "bravo";
				
		Date created1 = new Date(dfm.parse("20120310").getTime());
		Date created2 = new Date(dfm.parse("20131110").getTime());
		Date created3 = new Date(dfm.parse("20120510").getTime());
		
		OcanStaffForm ocanStaffForm1 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm1);
		ocanStaffForm1.setFacilityId(facilityId1);
		ocanStaffForm1.setClientId(clientId1);
		ocanStaffForm1.setOcanType(ocanType1);
		ocanStaffForm1.setAssessmentId(assessmentId1);
		ocanStaffForm1.setCreated(created1);
		dao.persist(ocanStaffForm1);
		
		OcanStaffForm ocanStaffForm2 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm2);
		ocanStaffForm2.setFacilityId(facilityId2);
		ocanStaffForm2.setClientId(clientId2);
		ocanStaffForm2.setOcanType(ocanType2);
		ocanStaffForm2.setAssessmentId(assessmentId2);
		ocanStaffForm2.setCreated(created2);
		dao.persist(ocanStaffForm2);
		
		OcanStaffForm ocanStaffForm3 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm3);
		ocanStaffForm3.setFacilityId(facilityId1);
		ocanStaffForm3.setClientId(clientId1);
		ocanStaffForm3.setOcanType(ocanType1);
		ocanStaffForm3.setCreated(created3);
		ocanStaffForm3.setAssessmentId(assessmentId3);
		dao.persist(ocanStaffForm3);
					
		OcanStaffForm ocanStaffForm4 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm4);
		dao.persist(ocanStaffForm4);
		
		OcanStaffForm ocanStaffForm5 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm5);
		dao.persist(ocanStaffForm5);
		
		List<OcanStaffForm> expectedResult = new ArrayList<OcanStaffForm>(Arrays.asList(ocanStaffForm3, ocanStaffForm1));
		List<OcanStaffForm> result = dao.findByFacilityClient(facilityId1, clientId1, ocanType1);	
		
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

	@Test 
	public void testFindOcanStaffFormById() throws Exception {
		
		OcanStaffForm ocanStaffForm1 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm1);
		dao.persist(ocanStaffForm1);
		
		OcanStaffForm ocanStaffForm2 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm2);
		dao.persist(ocanStaffForm2);
		
		OcanStaffForm ocanStaffForm3 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm3);
		dao.persist(ocanStaffForm3);
		
		OcanStaffForm expectedResult = ocanStaffForm2;
		OcanStaffForm result = dao.findOcanStaffFormById(2);
		
		assertEquals(expectedResult, result);
	}

	@Test
	public void testFindLatestSignedOcanFormsIntegerStringDateDateString() throws Exception {
		
		int facilityId1 = 100;
		int facilityId2 = 200;
		
		String assessmentStatus1 = "Completed";
		String assessmentStatus2 = "NotCompleted";
		
		String cdsFormVersion1 = "Version1";
		String cdsFormVersion2 = "Version2";
		
		Date startDate1 = new Date(dfm.parse("20110110").getTime());
		Date startDate2 = new Date(dfm.parse("20131110").getTime());
		Date startDate3 = new Date(dfm.parse("20120410").getTime());
		Date startDate4 = new Date(dfm.parse("20111020").getTime());
		Date startDate5 = new Date(dfm.parse("20081010").getTime());
		
		Date startDate = new Date(dfm.parse("20100510").getTime());
		Date endDate = new Date(dfm.parse("20120510").getTime());
		
		String ocanType1 = "alpha";
		String ocanType2 = "bravo";
				
		int clientId1 = 111;
		int clientId2 = 222;
		
		int assessmentId1 = 101;
		int assessmentId2 = 202;
			
		OcanStaffForm ocanStaffForm1 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm1);
		ocanStaffForm1.setFacilityId(facilityId1);
		ocanStaffForm1.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm1.setOcanFormVersion(cdsFormVersion1);
		ocanStaffForm1.setStartDate(startDate1);
		ocanStaffForm1.setOcanType(ocanType1);
		ocanStaffForm1.setClientId(clientId1);
		ocanStaffForm1.setAssessmentId(assessmentId1);
		dao.persist(ocanStaffForm1);
		
		OcanStaffForm ocanStaffForm2 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm2);
		ocanStaffForm2.setFacilityId(facilityId2);
		ocanStaffForm2.setAssessmentStatus(assessmentStatus2);
		ocanStaffForm2.setOcanFormVersion(cdsFormVersion2);
		ocanStaffForm2.setStartDate(startDate2);
		ocanStaffForm2.setOcanType(ocanType2);
		ocanStaffForm2.setClientId(clientId2);
		ocanStaffForm2.setAssessmentId(assessmentId2);
		dao.persist(ocanStaffForm2);
		
		OcanStaffForm ocanStaffForm3 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm3);
		ocanStaffForm3.setFacilityId(facilityId1);
		ocanStaffForm3.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm3.setOcanFormVersion(cdsFormVersion1);
		ocanStaffForm3.setStartDate(startDate3);
		ocanStaffForm3.setOcanType(ocanType1);
		ocanStaffForm3.setClientId(clientId1);
		ocanStaffForm3.setAssessmentId(assessmentId1);
		dao.persist(ocanStaffForm3);
					
		OcanStaffForm ocanStaffForm4 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm4);
		ocanStaffForm4.setFacilityId(facilityId2);
		ocanStaffForm4.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm4.setOcanFormVersion(cdsFormVersion1);
		ocanStaffForm4.setStartDate(startDate4);
		ocanStaffForm4.setOcanType(ocanType1);
		ocanStaffForm4.setClientId(clientId1);
		ocanStaffForm4.setAssessmentId(assessmentId1);
		dao.persist(ocanStaffForm4);
		
		OcanStaffForm ocanStaffForm5 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm5);
		ocanStaffForm5.setFacilityId(facilityId2);
		ocanStaffForm5.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm5.setOcanFormVersion(cdsFormVersion1);
		ocanStaffForm5.setStartDate(startDate5);
		ocanStaffForm5.setOcanType(ocanType1);
		ocanStaffForm5.setClientId(clientId1);
		ocanStaffForm5.setAssessmentId(assessmentId1);
		dao.persist(ocanStaffForm5);
		
		List<OcanStaffForm> expectedResult = new ArrayList<OcanStaffForm>(Arrays.asList(ocanStaffForm3));
		List<OcanStaffForm> result = dao.findLatestSignedOcanForms(facilityId1, cdsFormVersion1, startDate, endDate, ocanType1);

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

	@Test
	public void testFindLatestSignedOcanFormsIntegerInteger() throws Exception {
		
		int facilityId1 = 100;
		int facilityId2 = 200;
		
		String assessmentStatus1 = "Completed";
		String assessmentStatus2 = "NotCompleted";
		
		Date created1 = new Date(dfm.parse("20110110").getTime());
		Date created2 = new Date(dfm.parse("20131110").getTime());
		Date created3 = new Date(dfm.parse("20120410").getTime());
		Date created4 = new Date(dfm.parse("20101020").getTime());
		
		int clientId1 = 111;
		int clientId2 = 222;
		
		int assessmentId1 = 101;
		int assessmentId2 = 202;
			
		OcanStaffForm ocanStaffForm1 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm1);
		ocanStaffForm1.setFacilityId(facilityId1);
		ocanStaffForm1.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm1.setCreated(created1);
		ocanStaffForm1.setClientId(clientId1);
		ocanStaffForm1.setAssessmentId(assessmentId1);
		dao.persist(ocanStaffForm1);
		
		OcanStaffForm ocanStaffForm2 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm2);
		ocanStaffForm2.setFacilityId(facilityId2);
		ocanStaffForm2.setAssessmentStatus(assessmentStatus2);
		ocanStaffForm2.setCreated(created2);
		ocanStaffForm2.setClientId(clientId2);
		ocanStaffForm2.setAssessmentId(assessmentId2);
		dao.persist(ocanStaffForm2);
		
		OcanStaffForm ocanStaffForm3 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm3);
		ocanStaffForm3.setFacilityId(facilityId1);
		ocanStaffForm3.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm3.setCreated(created3);
		ocanStaffForm3.setClientId(clientId1);
		ocanStaffForm3.setAssessmentId(assessmentId1);
		dao.persist(ocanStaffForm3);
					
		OcanStaffForm ocanStaffForm4 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm4);
		ocanStaffForm4.setFacilityId(facilityId2);
		ocanStaffForm4.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm4.setCreated(created4);
		ocanStaffForm4.setClientId(clientId1);
		ocanStaffForm4.setAssessmentId(assessmentId1);
		dao.persist(ocanStaffForm4);
		
		List<OcanStaffForm> expectedResult = new ArrayList<OcanStaffForm>(Arrays.asList(ocanStaffForm3));
		List<OcanStaffForm> result = dao.findLatestSignedOcanForms(facilityId1, clientId1);

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

	@Test 
	public void testFindUnsubmittedOcanFormsByOcanType() throws Exception {

		int facilityId1 = 100;
		int facilityId2 = 200;
		
		String assessmentStatus1 = "Completed";
		String assessmentStatus2 = "NotCompleted";
		
		String ocanType1 = "alpha";
		String ocanType2 = "bravo";
		
		int submissionId1 = 0;
		int submissionId2 = 1;
		
		int clientId1 = 111;
		int clientId2 = 222;
		
		int assessmentId1 = 101;
		int assessmentId2 = 202;
		int assessmentId3 = 303;
		int assessmentId4 = 404;
	
		String assessmentIds = "101,202,303,404";
	
		Date created1 = new Date(dfm.parse("20100110").getTime());
		Date created2 = new Date(dfm.parse("20121110").getTime());
		Date created3 = new Date(dfm.parse("20130410").getTime());
		Date created4 = new Date(dfm.parse("20111020").getTime());
		
		OcanStaffForm ocanStaffForm1 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm1);
		ocanStaffForm1.setFacilityId(facilityId1);
		ocanStaffForm1.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm1.setOcanType(ocanType1);
		ocanStaffForm1.setSubmissionId(submissionId1);
		ocanStaffForm1.setClientId(clientId1);
		ocanStaffForm1.setAssessmentId(assessmentId1);
		ocanStaffForm1.setCreated(created1);
		dao.persist(ocanStaffForm1);
		
		OcanStaffForm ocanStaffForm2 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm2);
		ocanStaffForm2.setFacilityId(facilityId2);
		ocanStaffForm2.setAssessmentStatus(assessmentStatus2);
		ocanStaffForm2.setOcanType(ocanType2);
		ocanStaffForm2.setSubmissionId(submissionId2);
		ocanStaffForm2.setClientId(clientId2);
		ocanStaffForm2.setAssessmentId(assessmentId2);
		ocanStaffForm2.setCreated(created2);
		dao.persist(ocanStaffForm2);
		
		OcanStaffForm ocanStaffForm3 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm3);
		ocanStaffForm3.setFacilityId(facilityId1);
		ocanStaffForm3.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm3.setOcanType(ocanType1);
		ocanStaffForm3.setSubmissionId(submissionId1);
		ocanStaffForm3.setClientId(clientId1);
		ocanStaffForm3.setAssessmentId(assessmentId3);
		ocanStaffForm3.setCreated(created3);
		dao.persist(ocanStaffForm3);
					
		OcanStaffForm ocanStaffForm4 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm4);
		ocanStaffForm4.setFacilityId(facilityId2);
		ocanStaffForm4.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm4.setOcanType(ocanType1);
		ocanStaffForm4.setSubmissionId(submissionId2);
		ocanStaffForm4.setClientId(clientId1);
		ocanStaffForm4.setAssessmentId(assessmentId4);
		ocanStaffForm4.setCreated(created4);
		dao.persist(ocanStaffForm4);
		
		List<OcanStaffForm> expectedResult = new ArrayList<OcanStaffForm>(Arrays.asList(ocanStaffForm3, ocanStaffForm1));
		List<OcanStaffForm> result = dao.findUnsubmittedOcanFormsByOcanType(facilityId1, ocanType1);

		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match. Result: " + result.size());
					
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

	@Test
	public void testFindUnsubmittedOcanForms() throws Exception {

		int facilityId1 = 100;
		int facilityId2 = 200;
		
		String assessmentStatus1 = "Completed";
		String assessmentStatus2 = "NotCompleted";
				
		int submissionId1 = 0;
		int submissionId2 = 1;
		
		int clientId1 = 111;
		int clientId2 = 222;
		
		int assessmentId1 = 101;
		int assessmentId2 = 202;
		int assessmentId3 = 303;
		int assessmentId4 = 404;
		
		Date created1 = new Date(dfm.parse("20100110").getTime());
		Date created2 = new Date(dfm.parse("20121110").getTime());
		Date created3 = new Date(dfm.parse("20130410").getTime());
		Date created4 = new Date(dfm.parse("20111020").getTime());
		
		OcanStaffForm ocanStaffForm1 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm1);
		ocanStaffForm1.setFacilityId(facilityId1);
		ocanStaffForm1.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm1.setSubmissionId(submissionId1);
		ocanStaffForm1.setClientId(clientId1);
		ocanStaffForm1.setAssessmentId(assessmentId1);
		ocanStaffForm1.setCreated(created1);
		dao.persist(ocanStaffForm1);
		
		OcanStaffForm ocanStaffForm2 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm2);
		ocanStaffForm2.setFacilityId(facilityId2);
		ocanStaffForm2.setAssessmentStatus(assessmentStatus2);
		ocanStaffForm2.setSubmissionId(submissionId2);
		ocanStaffForm2.setClientId(clientId2);
		ocanStaffForm2.setAssessmentId(assessmentId2);
		ocanStaffForm2.setCreated(created2);
		dao.persist(ocanStaffForm2);
		
		OcanStaffForm ocanStaffForm3 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm3);
		ocanStaffForm3.setFacilityId(facilityId1);
		ocanStaffForm3.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm3.setSubmissionId(submissionId1);
		ocanStaffForm3.setClientId(clientId1);
		ocanStaffForm3.setAssessmentId(assessmentId3);
		ocanStaffForm3.setCreated(created3);
		dao.persist(ocanStaffForm3);
					
		OcanStaffForm ocanStaffForm4 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm4);
		ocanStaffForm4.setFacilityId(facilityId2);
		ocanStaffForm4.setAssessmentStatus(assessmentStatus1);
		ocanStaffForm4.setSubmissionId(submissionId2);
		ocanStaffForm4.setClientId(clientId1);
		ocanStaffForm4.setAssessmentId(assessmentId4);
		ocanStaffForm4.setCreated(created4);
		dao.persist(ocanStaffForm4);
		
		List<OcanStaffForm> expectedResult = new ArrayList<OcanStaffForm>(Arrays.asList(ocanStaffForm3, ocanStaffForm1));
		List<OcanStaffForm> result = dao.findUnsubmittedOcanForms(facilityId1);

		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match. Result: " + result.size());
					
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

	@Test
	public void testFindSubmittedOcanFormsByAssessmentId() throws Exception {
		
		int assessmentId1 = 100;
		int assessmentId2 = 200;
		
		int submissionId1 = 1; 
		int submissionId2 = 0;
		
		OcanStaffForm ocanStaffForm1 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm1);
		ocanStaffForm1.setAssessmentId(assessmentId1);
		ocanStaffForm1.setSubmissionId(submissionId1);
		dao.persist(ocanStaffForm1);
		
		OcanStaffForm ocanStaffForm2 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm2);
		ocanStaffForm2.setAssessmentId(assessmentId2);
		ocanStaffForm2.setSubmissionId(submissionId2);
		dao.persist(ocanStaffForm2);
		
		OcanStaffForm ocanStaffForm3 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm3);
		ocanStaffForm3.setAssessmentId(assessmentId1);
		ocanStaffForm3.setSubmissionId(submissionId1);
		dao.persist(ocanStaffForm3);
		
		List<OcanStaffForm> expectedResult = new ArrayList<OcanStaffForm>(Arrays.asList(ocanStaffForm1, ocanStaffForm3));
		List<OcanStaffForm> result = dao.findSubmittedOcanFormsByAssessmentId(assessmentId1);

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

	@Test 
	public void testFindAllByFacility() throws Exception {
		
		int facilityId1 = 100;
		int facilityId2 = 200;
		
		Date created1 = new Date(dfm.parse("20110110").getTime());
		Date created2 = new Date(dfm.parse("20131110").getTime());
		Date created3 = new Date(dfm.parse("20120410").getTime());
		
		OcanStaffForm ocanStaffForm1 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm1);
		ocanStaffForm1.setFacilityId(facilityId1);
		ocanStaffForm1.setCreated(created1);
		dao.persist(ocanStaffForm1);
		
		OcanStaffForm ocanStaffForm2 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm2);
		ocanStaffForm2.setFacilityId(facilityId2);
		ocanStaffForm2.setCreated(created2);
		dao.persist(ocanStaffForm2);
		
		OcanStaffForm ocanStaffForm3 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm3);
		ocanStaffForm3.setFacilityId(facilityId1);
		ocanStaffForm3.setCreated(created3);
		dao.persist(ocanStaffForm3);
		
		List<OcanStaffForm> expectedResult = new ArrayList<OcanStaffForm>(Arrays.asList(ocanStaffForm3, ocanStaffForm1));
		List<OcanStaffForm> result = dao.findAllByFacility(facilityId1);

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

	@Test
	public void testFindBySubmissionId() throws Exception {

		int facilityId1 = 100;
		int facilityId2 = 200;
		
		int submissionId1 = 101;
		int submissionId2 = 202;
		
		Date created1 = new Date(dfm.parse("20110110").getTime());
		Date created2 = new Date(dfm.parse("20131110").getTime());
		Date created3 = new Date(dfm.parse("20120410").getTime());
		
		OcanStaffForm ocanStaffForm1 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm1);
		ocanStaffForm1.setFacilityId(facilityId1);
		ocanStaffForm1.setSubmissionId(submissionId1);
		ocanStaffForm1.setCreated(created1);
		dao.persist(ocanStaffForm1);
		
		OcanStaffForm ocanStaffForm2 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm2);
		ocanStaffForm2.setFacilityId(facilityId2);
		ocanStaffForm2.setSubmissionId(submissionId2);
		ocanStaffForm2.setCreated(created2);
		dao.persist(ocanStaffForm2);
		
		OcanStaffForm ocanStaffForm3 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm3);
		ocanStaffForm3.setFacilityId(facilityId1);
		ocanStaffForm3.setSubmissionId(submissionId1);
		ocanStaffForm3.setCreated(created3);
		dao.persist(ocanStaffForm3);
		
		List<OcanStaffForm> expectedResult = new ArrayList<OcanStaffForm>(Arrays.asList(ocanStaffForm3, ocanStaffForm1));
		List<OcanStaffForm> result = dao.findBySubmissionId(facilityId1, submissionId1);
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

	@Test 
	public void testFindLatestByAssessmentId() throws Exception {
		
		int facilityId1 = 100;
		int facilityId2 = 200;
		
		int assessmentId1 = 101;
		int assessmentId2 = 202;
		
		Date created1 = new Date(dfm.parse("20110110").getTime());
		Date created2 = new Date(dfm.parse("20131110").getTime());
		Date created3 = new Date(dfm.parse("20120410").getTime());
		
		OcanStaffForm ocanStaffForm1 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm1);
		ocanStaffForm1.setFacilityId(facilityId1);
		ocanStaffForm1.setAssessmentId(assessmentId1);
		ocanStaffForm1.setCreated(created1);
		dao.persist(ocanStaffForm1);
		
		OcanStaffForm ocanStaffForm2 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm2);
		ocanStaffForm2.setFacilityId(facilityId2);
		ocanStaffForm2.setAssessmentId(assessmentId2);
		ocanStaffForm2.setCreated(created2);
		dao.persist(ocanStaffForm2);
		
		OcanStaffForm ocanStaffForm3 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm3);
		ocanStaffForm3.setFacilityId(facilityId1);
		ocanStaffForm3.setAssessmentId(assessmentId1);
		ocanStaffForm3.setCreated(created3);
		dao.persist(ocanStaffForm3);
		
		OcanStaffForm expectedResult = ocanStaffForm3;
		OcanStaffForm result = dao.findLatestByAssessmentId(facilityId1, assessmentId1);
		
		assertEquals(expectedResult, result);
	}

	@Test
	public void testGetAllOcanClients() throws Exception {

		int facilityId1 = 100;
		int facilityId2 = 200;
		
		int clientId1 = 101;
		int clientId2 = 202;
		
		OcanStaffForm ocanStaffForm1 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm1);
		ocanStaffForm1.setFacilityId(facilityId1);
		ocanStaffForm1.setClientId(clientId1);
		dao.persist(ocanStaffForm1);
		
		OcanStaffForm ocanStaffForm2 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm2);
		ocanStaffForm2.setFacilityId(facilityId2);
		ocanStaffForm2.setClientId(clientId2);
		dao.persist(ocanStaffForm2);
		
		OcanStaffForm ocanStaffForm3 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm3);
		ocanStaffForm3.setFacilityId(facilityId1);
		ocanStaffForm3.setClientId(clientId1);
		dao.persist(ocanStaffForm3);
		
		OcanStaffForm ocanStaffForm4 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm4);
		ocanStaffForm4.setFacilityId(facilityId1);
		ocanStaffForm4.setClientId(clientId2);
		dao.persist(ocanStaffForm4);
		
		List<Integer> expectedResult = new ArrayList<Integer>(Arrays.asList(clientId1, clientId2));
		List<Integer> result = dao.getAllOcanClients(facilityId1);
		
		Logger logger = MiscUtils.getLogger();
		
		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match. Result: "+result.size());
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

	@Test
	public void testFindLatestOcanFormsByStaff() throws Exception {
		
		int facilityId1 = 100;
		int facilityId2 = 200;
		
		String providerNo1 = "111";
		String providerNo2 = "222";
		
		int assessmentId1 = 101;
		int assessmentId2 = 202;
	
		int clientId1 = 1;
		int clientId2 = 2;
		
		Date created1 = new Date(dfm.parse("20110110").getTime());
		Date created2 = new Date(dfm.parse("20131110").getTime());
		Date created3 = new Date(dfm.parse("20120410").getTime());
		
		OcanStaffForm ocanStaffForm1 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm1);
		ocanStaffForm1.setFacilityId(facilityId1);
		ocanStaffForm1.setProviderNo(providerNo1);
		ocanStaffForm1.setAssessmentId(assessmentId1);
		ocanStaffForm1.setCreated(created1);
		ocanStaffForm1.setClientId(clientId1);
		dao.persist(ocanStaffForm1);
		
		OcanStaffForm ocanStaffForm2 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm2);
		ocanStaffForm2.setFacilityId(facilityId2);
		ocanStaffForm2.setProviderNo(providerNo2);
		ocanStaffForm2.setAssessmentId(assessmentId2);
		ocanStaffForm2.setCreated(created2);
		ocanStaffForm2.setClientId(clientId2);
		dao.persist(ocanStaffForm2);
		
		OcanStaffForm ocanStaffForm3 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm3);
		ocanStaffForm3.setFacilityId(facilityId1);
		ocanStaffForm3.setProviderNo(providerNo1);
		ocanStaffForm3.setAssessmentId(assessmentId1);
		ocanStaffForm3.setCreated(created3);
		ocanStaffForm3.setClientId(clientId1);
		dao.persist(ocanStaffForm3);
		
		List<OcanStaffForm> expectedResult = new ArrayList<OcanStaffForm>(Arrays.asList(ocanStaffForm3));
		List<OcanStaffForm> result = dao.findLatestOcanFormsByStaff(facilityId1, providerNo1);
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

	@Test
	public void testFindLatestByConsumer() throws Exception {
		
		int facilityId1 = 100;
		int facilityId2 = 200;
		
		int clientId1 = 101;
		int clientId2 = 202;
		
		int assessmentId1 = 111;
		int assessmentId2 = 222;
		
		Date created1 = new Date(dfm.parse("20110110").getTime());
		Date created2 = new Date(dfm.parse("20131110").getTime());
		Date created3 = new Date(dfm.parse("20120410").getTime());
		
		OcanStaffForm ocanStaffForm1 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm1);
		ocanStaffForm1.setFacilityId(facilityId1);
		ocanStaffForm1.setClientId(clientId1);
		ocanStaffForm1.setCreated(created1);
		ocanStaffForm1.setAssessmentId(assessmentId1);
		dao.persist(ocanStaffForm1);
		
		OcanStaffForm ocanStaffForm2 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm2);
		ocanStaffForm2.setFacilityId(facilityId2);
		ocanStaffForm2.setClientId(clientId2);
		ocanStaffForm2.setAssessmentId(assessmentId2);
		ocanStaffForm2.setCreated(created2);
		dao.persist(ocanStaffForm2);
		
		OcanStaffForm ocanStaffForm3 = new OcanStaffForm();
		EntityDataGenerator.generateTestDataForModelClass(ocanStaffForm3);
		ocanStaffForm3.setFacilityId(facilityId1);
		ocanStaffForm3.setClientId(clientId1);
		ocanStaffForm3.setAssessmentId(assessmentId1);
		ocanStaffForm3.setCreated(created3);
		dao.persist(ocanStaffForm3);
		
		List<OcanStaffForm> expectedResult = new ArrayList<OcanStaffForm>(Arrays.asList(ocanStaffForm3));
		List<OcanStaffForm> result = dao.findLatestByConsumer(facilityId1, clientId1);
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
