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
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ConsultationResponse;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.consultations.ConsultationResponseSearchFilter;
import org.oscarehr.util.SpringUtils;

public class ConsultResponseDaoTest extends DaoTestFixtures {

	protected ConsultResponseDao consultDao = (ConsultResponseDao)SpringUtils.getBean(ConsultResponseDao.class);
	protected ProfessionalSpecialistDao specialistDao = (ProfessionalSpecialistDao)SpringUtils.getBean(ProfessionalSpecialistDao.class);
	protected DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean(DemographicDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("consultationResponse", "professionalSpecialists", "demographic", "admission");
	}

	@Test
	public void testCreate() throws Exception {
		ConsultationResponse entity = new ConsultationResponse();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		consultDao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testGetConsultationCount() throws Exception {
		
		Demographic d1 = new Demographic();
		EntityDataGenerator.generateTestDataForModelClass(d1);
		d1.setDemographicNo(null);
		demographicDao.save(d1);
		
		Demographic d2 = new Demographic();
		EntityDataGenerator.generateTestDataForModelClass(d2);
		d2.setDemographicNo(null);
		demographicDao.save(d2);
		
		Integer demoNo1 = d1.getDemographicNo();
		Integer demoNo2 = d2.getDemographicNo();
		
		ProfessionalSpecialist sp = new ProfessionalSpecialist();
		EntityDataGenerator.generateTestDataForModelClass(sp);
		specialistDao.persist(sp);
		Integer referringDocId = sp.getId();
		
		String[] format = new String[]{"yyyy-MM-dd"};
		Date date1 = DateUtils.parseDate("2015-03-05", format);
		Date date2 = DateUtils.parseDate("2015-03-26", format);
		
		String status1="1", status2="2";
		String team1="tttt1", team2="tttt2";
		String urgency1="u1", urgency2="u2";
		
		ConsultationResponse cr1 = new ConsultationResponse();
		EntityDataGenerator.generateTestDataForModelClass(cr1);
		cr1.setReferringDocId(referringDocId);
		cr1.setDemographicNo(demoNo1);
		cr1.setAppointmentDate(date1);
		cr1.setReferralDate(date1);
		cr1.setResponseDate(date1);
		cr1.setStatus(status1);
		cr1.setSendTo(team1);
		cr1.setUrgency(urgency1);
		consultDao.persist(cr1);
		
		ConsultationResponse cr2 = new ConsultationResponse();
		EntityDataGenerator.generateTestDataForModelClass(cr2);
		cr2.setReferringDocId(referringDocId);
		cr2.setDemographicNo(demoNo2);
		cr2.setAppointmentDate(date2);
		cr2.setReferralDate(date2);
		cr2.setResponseDate(date2);
		cr2.setStatus(status2);
		cr2.setSendTo(team2);
		cr2.setUrgency(urgency2);
		consultDao.persist(cr2);
		
		ConsultationResponse cr3 = new ConsultationResponse();
		EntityDataGenerator.generateTestDataForModelClass(cr3);
		cr3.setReferringDocId(referringDocId);
		cr3.setDemographicNo(demoNo1);
		cr3.setAppointmentDate(date2);
		cr3.setReferralDate(date1);
		cr3.setResponseDate(date1);
		cr3.setStatus(status1);
		cr3.setSendTo(team1);
		cr3.setUrgency(urgency1);
		consultDao.persist(cr3);
		
		ConsultationResponse cr4 = new ConsultationResponse();
		EntityDataGenerator.generateTestDataForModelClass(cr4);
		cr4.setReferringDocId(referringDocId);
		cr4.setDemographicNo(demoNo2);
		cr4.setAppointmentDate(date1);
		cr4.setReferralDate(date2);
		cr4.setResponseDate(date1);
		cr4.setStatus(status2);
		cr4.setSendTo(team2);
		cr4.setUrgency(urgency2);
		consultDao.persist(cr4);
		
		ConsultationResponse cr5 = new ConsultationResponse();
		EntityDataGenerator.generateTestDataForModelClass(cr5);
		cr5.setReferringDocId(referringDocId);
		cr5.setDemographicNo(demoNo1);
		cr5.setAppointmentDate(date1);
		cr5.setReferralDate(date1);
		cr5.setResponseDate(date1);
		cr5.setStatus(status1);
		cr5.setSendTo(team1);
		cr5.setUrgency(urgency2);
		consultDao.persist(cr5);
		
		ConsultationResponse cr6 = new ConsultationResponse();
		EntityDataGenerator.generateTestDataForModelClass(cr6);
		cr6.setReferringDocId(referringDocId);
		cr6.setDemographicNo(demoNo2);
		cr6.setAppointmentDate(date2);
		cr6.setReferralDate(date1);
		cr6.setResponseDate(date1);
		cr6.setStatus(status1);
		cr6.setSendTo(team2);
		cr6.setUrgency(urgency2);
		consultDao.persist(cr6);
		
		
		ConsultationResponseSearchFilter filter = new ConsultationResponseSearchFilter();
		filter.setDemographicNo(demoNo1);
		filter.setStatus(Integer.valueOf(status1));
		filter.setTeam(team1);
		filter.setUrgency(urgency1);
		
		ConsultationResponse[] expectedCR = new ConsultationResponse[]{cr1, cr3};
		assertEquals(expectedCR.length, consultDao.getConsultationCount(filter));
		
		filter = new ConsultationResponseSearchFilter();
		filter.setAppointmentStartDate(DateUtils.setDays(date2, 20));
		filter.setAppointmentEndDate(DateUtils.setDays(date2, 27));
		
		expectedCR = new ConsultationResponse[]{cr2, cr3, cr6};
		assertEquals(expectedCR.length, consultDao.getConsultationCount(filter));
		
		filter = new ConsultationResponseSearchFilter();
		filter.setReferralStartDate(DateUtils.setDays(date1, 1));
		filter.setReferralEndDate(DateUtils.setDays(date1, 10));
		
		expectedCR = new ConsultationResponse[]{cr1, cr3, cr5, cr6};
		assertEquals(expectedCR.length, consultDao.getConsultationCount(filter));
		
		filter = new ConsultationResponseSearchFilter();
		filter.setResponseStartDate(date1);
		filter.setResponseEndDate(DateUtils.setDays(date1, 25));
		
		expectedCR = new ConsultationResponse[]{cr1, cr3, cr4, cr5, cr6};
		assertEquals(expectedCR.length, consultDao.getConsultationCount(filter));
	}
	
	@Test
	public void testSearch() throws Exception {
		
		Demographic d1 = new Demographic();
		EntityDataGenerator.generateTestDataForModelClass(d1);
		d1.setDemographicNo(null);
		demographicDao.save(d1);
		
		Demographic d2 = new Demographic();
		EntityDataGenerator.generateTestDataForModelClass(d2);
		d2.setDemographicNo(null);
		demographicDao.save(d2);
		
		Integer demoNo1 = d1.getDemographicNo();
		Integer demoNo2 = d2.getDemographicNo();
		
		ProfessionalSpecialist sp = new ProfessionalSpecialist();
		EntityDataGenerator.generateTestDataForModelClass(sp);
		specialistDao.persist(sp);
		Integer referringDocId = sp.getId();
		
		String[] format = new String[]{"yyyy-MM-dd"};
		Date date1 = DateUtils.parseDate("2015-03-05", format);
		Date date2 = DateUtils.parseDate("2015-03-26", format);
		
		String status1="1", status2="2";
		String team1="tttt1", team2="tttt2";
		String urgency1="u1", urgency2="u2";
		
		ConsultationResponse cr1 = new ConsultationResponse();
		EntityDataGenerator.generateTestDataForModelClass(cr1);
		cr1.setReferringDocId(referringDocId);
		cr1.setDemographicNo(demoNo1);
		cr1.setAppointmentDate(date1);
		cr1.setReferralDate(date1);
		cr1.setResponseDate(date1);
		cr1.setStatus(status1);
		cr1.setSendTo(team1);
		cr1.setUrgency(urgency1);
		consultDao.persist(cr1);
		
		ConsultationResponse cr2 = new ConsultationResponse();
		EntityDataGenerator.generateTestDataForModelClass(cr2);
		cr2.setReferringDocId(referringDocId);
		cr2.setDemographicNo(demoNo2);
		cr2.setAppointmentDate(date2);
		cr2.setReferralDate(date2);
		cr2.setResponseDate(date2);
		cr2.setStatus(status2);
		cr2.setSendTo(team2);
		cr2.setUrgency(urgency2);
		consultDao.persist(cr2);
		
		ConsultationResponse cr3 = new ConsultationResponse();
		EntityDataGenerator.generateTestDataForModelClass(cr3);
		cr3.setReferringDocId(referringDocId);
		cr3.setDemographicNo(demoNo1);
		cr3.setAppointmentDate(date2);
		cr3.setReferralDate(date2);
		cr3.setResponseDate(date1);
		cr3.setStatus(status1);
		cr3.setSendTo(team1);
		cr3.setUrgency(urgency1);
		consultDao.persist(cr3);
		
		ConsultationResponse cr4 = new ConsultationResponse();
		EntityDataGenerator.generateTestDataForModelClass(cr4);
		cr4.setReferringDocId(referringDocId);
		cr4.setDemographicNo(demoNo2);
		cr4.setAppointmentDate(date1);
		cr4.setReferralDate(date2);
		cr4.setResponseDate(date2);
		cr4.setStatus(status2);
		cr4.setSendTo(team2);
		cr4.setUrgency(urgency2);
		consultDao.persist(cr4);
		
		ConsultationResponse cr5 = new ConsultationResponse();
		EntityDataGenerator.generateTestDataForModelClass(cr5);
		cr5.setReferringDocId(referringDocId);
		cr5.setDemographicNo(demoNo1);
		cr5.setAppointmentDate(date1);
		cr5.setReferralDate(date1);
		cr5.setResponseDate(date1);
		cr5.setStatus(status1);
		cr5.setSendTo(team1);
		cr5.setUrgency(urgency2);
		consultDao.persist(cr5);
		
		
		ConsultationResponseSearchFilter filter = new ConsultationResponseSearchFilter();
		filter.setNumToReturn(99);
		filter.setDemographicNo(demoNo1);
		filter.setStatus(Integer.valueOf(status1));
		filter.setTeam(team1);
		filter.setUrgency(urgency1);
		
		String failMsgResultCount = "Result count is wrong.";
		String failMsgItemMatch = "Items do not match ";
		
		ConsultationResponse[] expectedCR = new ConsultationResponse[]{cr3, cr1};
		Demographic[] expectedDemo = new Demographic[]{d1, d1};
		List<Object[]> results = consultDao.search(filter);
		String failSubject = "Fail search demoNo,status,team,urgency : ";
		assertEquals(failSubject+failMsgResultCount, expectedCR.length, results.size());
		for (int i=0; i<results.size(); i++) {
			Object[] result = results.get(i);
			assertEquals(failSubject+failMsgItemMatch+"["+i+"].", expectedCR[i], result[0]);
			assertEquals(failSubject+failMsgItemMatch+"["+i+"].", sp, result[1]);
			assertEquals(failSubject+failMsgItemMatch+"["+i+"].", expectedDemo[i], result[2]);
		}
		
		filter = new ConsultationResponseSearchFilter();
		filter.setNumToReturn(99);
		filter.setAppointmentStartDate(DateUtils.setDays(date2, 20));
		filter.setAppointmentEndDate(DateUtils.setDays(date2, 27));
		
		expectedCR = new ConsultationResponse[]{cr2, cr3};
		expectedDemo = new Demographic[]{d2, d1};
		results = consultDao.search(filter);
		failSubject = "Fail search appointmentStart/EndDate : ";
		assertEquals(failSubject+failMsgResultCount, expectedCR.length, results.size());
		for (int i=0; i<results.size(); i++) {
			Object[] result = results.get(i);
			assertEquals(failSubject+failMsgItemMatch+"["+i+"].", expectedCR[i], result[0]);
			assertEquals(failSubject+failMsgItemMatch+"["+i+"].", expectedDemo[i], result[2]);
		}
		
		filter = new ConsultationResponseSearchFilter();
		filter.setNumToReturn(99);
		filter.setReferralStartDate(DateUtils.setDays(date1, 1));
		filter.setReferralEndDate(DateUtils.setDays(date1, 10));
		
		expectedCR = new ConsultationResponse[]{cr1, cr5};
		expectedDemo = new Demographic[]{d1, d1};
		results = consultDao.search(filter);
		failSubject = "Fail search referralStart/EndDate : ";
		assertEquals(failSubject+failMsgResultCount, expectedCR.length, results.size());
		for (int i=0; i<results.size(); i++) {
			Object[] result = results.get(i);
			assertEquals(failSubject+failMsgItemMatch+"["+i+"].", expectedCR[i], result[0]);
			assertEquals(failSubject+failMsgItemMatch+"["+i+"].", expectedDemo[i], result[2]);
		}
		
		filter = new ConsultationResponseSearchFilter();
		filter.setNumToReturn(99);
		filter.setResponseStartDate(DateUtils.setDays(date2, 25));
		filter.setResponseEndDate(date2);
		
		expectedCR = new ConsultationResponse[]{cr2, cr4};
		expectedDemo = new Demographic[]{d2, d2};
		results = consultDao.search(filter);
		failSubject = "Fail search responseStart/EndDate : ";
		assertEquals(failSubject+failMsgResultCount, expectedCR.length, results.size());
		for (int i=0; i<results.size(); i++) {
			Object[] result = results.get(i);
			assertEquals(failSubject+failMsgItemMatch+"["+i+"].", expectedCR[i], result[0]);
			assertEquals(failSubject+failMsgItemMatch+"["+i+"].", expectedDemo[i], result[2]);
		}
	}
}
