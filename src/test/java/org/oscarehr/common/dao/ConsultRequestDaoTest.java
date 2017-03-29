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
import org.junit.Ignore;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ConsultationRequest;
import org.oscarehr.common.model.ConsultationServices;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.consultations.ConsultationRequestSearchFilter;
import org.oscarehr.util.SpringUtils;

public class ConsultRequestDaoTest extends DaoTestFixtures {

	protected ConsultRequestDao consultDao = (ConsultRequestDao)SpringUtils.getBean(ConsultRequestDao.class);
	protected ConsultationServiceDao serviceDao = (ConsultationServiceDao)SpringUtils.getBean(ConsultationServiceDao.class); 
	protected DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean(DemographicDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("consultationRequests", "consultationServices", "demographic","admission","professionalSpecialists","provider","lst_gender","demographic_merged","program","health_safety","serviceSpecialists");
	}

	@Test
	public void testCreate() throws Exception {
		ConsultationRequest entity = new ConsultationRequest();
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
		
		ConsultationServices cs = new ConsultationServices();
		EntityDataGenerator.generateTestDataForModelClass(cs);
		serviceDao.persist(cs);
		Integer serviceId = cs.getId();
		
		String[] format = new String[]{"yyyy-MM-dd"};
		Date date1 = DateUtils.parseDate("2015-03-05", format);
		Date date2 = DateUtils.parseDate("2015-03-26", format);
		
		String status1="1", status2="2";
		String team1="tttt1", team2="tttt2";
		String urgency1="u1", urgency2="u2";
		
		ConsultationRequest cr1 = new ConsultationRequest();
		EntityDataGenerator.generateTestDataForModelClass(cr1);
		cr1.setServiceId(serviceId);
		cr1.setDemographicId(demoNo1);
		cr1.setAppointmentDate(date1);
		cr1.setReferralDate(date1);
		cr1.setStatus(status1);
		cr1.setSendTo(team1);
		cr1.setUrgency(urgency1);
		consultDao.persist(cr1);
		
		ConsultationRequest cr2 = new ConsultationRequest();
		EntityDataGenerator.generateTestDataForModelClass(cr2);
		cr2.setServiceId(serviceId);
		cr2.setDemographicId(demoNo2);
		cr2.setAppointmentDate(date2);
		cr2.setReferralDate(date2);
		cr2.setStatus(status2);
		cr2.setSendTo(team2);
		cr2.setUrgency(urgency2);
		consultDao.persist(cr2);
		
		ConsultationRequest cr3 = new ConsultationRequest();
		EntityDataGenerator.generateTestDataForModelClass(cr3);
		cr3.setServiceId(serviceId);
		cr3.setDemographicId(demoNo1);
		cr3.setAppointmentDate(date2);
		cr3.setReferralDate(date1);
		cr3.setStatus(status1);
		cr3.setSendTo(team1);
		cr3.setUrgency(urgency1);
		consultDao.persist(cr3);
		
		ConsultationRequest cr4 = new ConsultationRequest();
		EntityDataGenerator.generateTestDataForModelClass(cr4);
		cr4.setServiceId(serviceId);
		cr4.setDemographicId(demoNo2);
		cr4.setAppointmentDate(date1);
		cr4.setReferralDate(date1);
		cr4.setStatus(status2);
		cr4.setSendTo(team2);
		cr4.setUrgency(urgency2);
		consultDao.persist(cr4);
		
		ConsultationRequest cr5 = new ConsultationRequest();
		EntityDataGenerator.generateTestDataForModelClass(cr5);
		cr5.setServiceId(serviceId);
		cr5.setDemographicId(demoNo1);
		cr5.setAppointmentDate(date2);
		cr5.setReferralDate(date1);
		cr5.setStatus(status1);
		cr5.setSendTo(team1);
		cr5.setUrgency(urgency2);
		consultDao.persist(cr5);
		
		
		ConsultationRequestSearchFilter filter = new ConsultationRequestSearchFilter();
		filter.setDemographicNo(demoNo1);
		filter.setStatus(Integer.valueOf(status1));
		filter.setTeam(team1);
		filter.setUrgency(urgency1);
		
		ConsultationRequest[] expectedCR = new ConsultationRequest[]{cr1, cr3};
		assertEquals(expectedCR.length, consultDao.getConsultationCount2(filter));
		
		filter = new ConsultationRequestSearchFilter();
		filter.setAppointmentStartDate(DateUtils.setDays(date2, 20));
		filter.setAppointmentEndDate(DateUtils.setDays(date2, 27));
		
		expectedCR = new ConsultationRequest[]{cr2, cr3, cr5};
		assertEquals(expectedCR.length, consultDao.getConsultationCount2(filter));
		
		filter = new ConsultationRequestSearchFilter();
		filter.setReferralStartDate(DateUtils.setDays(date1, 1));
		filter.setReferralEndDate(DateUtils.setDays(date1, 10));
		
		expectedCR = new ConsultationRequest[]{cr1, cr3, cr4, cr5};
		assertEquals(expectedCR.length, consultDao.getConsultationCount2(filter));
	}
	
	@Test
	@Ignore
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
		
		ConsultationServices cs = new ConsultationServices();
		EntityDataGenerator.generateTestDataForModelClass(cs);
		serviceDao.persist(cs);
		Integer serviceId = cs.getId();
		
		String[] format = new String[]{"yyyy-MM-dd"};
		Date date1 = DateUtils.parseDate("2015-03-05", format);
		Date date2 = DateUtils.parseDate("2015-03-26", format);
		
		String status1="1", status2="2";
		String team1="tttt1", team2="tttt2";
		String urgency1="u1", urgency2="u2";
		
		ConsultationRequest cr1 = new ConsultationRequest();
		EntityDataGenerator.generateTestDataForModelClass(cr1);
		cr1.setServiceId(serviceId);
		cr1.setDemographicId(demoNo1);
		cr1.setAppointmentDate(date1);
		cr1.setReferralDate(date1);
		cr1.setStatus(status1);
		cr1.setSendTo(team1);
		cr1.setUrgency(urgency1);
		consultDao.persist(cr1);
		
		ConsultationRequest cr2 = new ConsultationRequest();
		EntityDataGenerator.generateTestDataForModelClass(cr2);
		cr2.setServiceId(serviceId);
		cr2.setDemographicId(demoNo2);
		cr2.setAppointmentDate(date2);
		cr2.setReferralDate(date2);
		cr2.setStatus(status2);
		cr2.setSendTo(team2);
		cr2.setUrgency(urgency2);
		consultDao.persist(cr2);
		
		ConsultationRequest cr3 = new ConsultationRequest();
		EntityDataGenerator.generateTestDataForModelClass(cr3);
		cr3.setServiceId(serviceId);
		cr3.setDemographicId(demoNo1);
		cr3.setAppointmentDate(date2);
		cr3.setReferralDate(date2);
		cr3.setStatus(status1);
		cr3.setSendTo(team1);
		cr3.setUrgency(urgency1);
		consultDao.persist(cr3);
		
		ConsultationRequest cr4 = new ConsultationRequest();
		EntityDataGenerator.generateTestDataForModelClass(cr4);
		cr4.setServiceId(serviceId);
		cr4.setDemographicId(demoNo2);
		cr4.setAppointmentDate(date1);
		cr4.setReferralDate(date2);
		cr4.setStatus(status2);
		cr4.setSendTo(team2);
		cr4.setUrgency(urgency2);
		consultDao.persist(cr4);
		
		ConsultationRequest cr5 = new ConsultationRequest();
		EntityDataGenerator.generateTestDataForModelClass(cr5);
		cr5.setServiceId(serviceId);
		cr5.setDemographicId(demoNo1);
		cr5.setAppointmentDate(date1);
		cr5.setReferralDate(date1);
		cr5.setStatus(status1);
		cr5.setSendTo(team1);
		cr5.setUrgency(urgency2);
		consultDao.persist(cr5);
		
		
		ConsultationRequestSearchFilter filter = new ConsultationRequestSearchFilter();
		filter.setNumToReturn(99);
		filter.setDemographicNo(demoNo1);
		filter.setStatus(Integer.valueOf(status1));
		filter.setTeam(team1);
		filter.setUrgency(urgency1);
		
		String failMsgResultCount = "Result count is wrong.";
		String failMsgItemMatch = "Items do not match ";
		
		ConsultationRequest[] expectedCR = new ConsultationRequest[]{cr3, cr1};
		Demographic[] expectedDemo = new Demographic[]{d1, d1};
		List<Object[]> results = consultDao.search(filter);
		String failSubject = "Fail search demoNo,status,team,urgency : ";
		assertEquals(failSubject+failMsgResultCount, expectedCR.length, results.size());
		for (int i=0; i<results.size(); i++) {
			Object[] result = results.get(i);
			assertEquals(failSubject+failMsgItemMatch+"["+i+"].", expectedCR[i], result[0]);
			assertEquals(failSubject+failMsgItemMatch+"["+i+"].", cs, result[2]);
			assertEquals(failSubject+failMsgItemMatch+"["+i+"].", expectedDemo[i], result[3]);
		}
		
		filter = new ConsultationRequestSearchFilter();
		filter.setNumToReturn(99);
		filter.setAppointmentStartDate(DateUtils.setDays(date2, 20));
		filter.setAppointmentEndDate(DateUtils.setDays(date2, 27));
		
		expectedCR = new ConsultationRequest[]{cr2, cr3};
		expectedDemo = new Demographic[]{d2, d1};
		results = consultDao.search(filter);
		failSubject = "Fail search appointmentStart/EndDate : ";
		assertEquals(failSubject+failMsgResultCount, expectedCR.length, results.size());
		for (int i=0; i<results.size(); i++) {
			Object[] result = results.get(i);
			assertEquals(failSubject+failMsgItemMatch+"["+i+"].", expectedCR[i], result[0]);
			assertEquals(failSubject+failMsgItemMatch+"["+i+"].", expectedDemo[i], result[3]);
		}
		
		filter = new ConsultationRequestSearchFilter();
		filter.setNumToReturn(99);
		filter.setReferralStartDate(DateUtils.setDays(date1, 1));
		filter.setReferralEndDate(DateUtils.setDays(date1, 10));
		
		expectedCR = new ConsultationRequest[]{cr1, cr5};
		expectedDemo = new Demographic[]{d1, d1};
		results = consultDao.search(filter);
		failSubject = "Fail search referralStart/EndDate : ";
		assertEquals(failSubject+failMsgResultCount, expectedCR.length, results.size());
		for (int i=0; i<results.size(); i++) {
			Object[] result = results.get(i);
			assertEquals(failSubject+failMsgItemMatch+"["+i+"].", expectedCR[i], result[0]);
			assertEquals(failSubject+failMsgItemMatch+"["+i+"].", expectedDemo[i], result[3]);
		}
	}
}
