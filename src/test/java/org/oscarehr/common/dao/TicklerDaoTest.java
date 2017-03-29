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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.common.model.TicklerComment;
import org.oscarehr.common.model.TicklerUpdate;
import org.oscarehr.util.SpringUtils;

public class TicklerDaoTest extends DaoTestFixtures {

	protected TicklerDao dao = SpringUtils.getBean(TicklerDao.class);
	//protected TicklerUpdate1Dao updateDao = SpringUtils.getBean(TicklerUpdate1Dao.class);
	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	private ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	private ProgramDao programDao = SpringUtils.getBean(ProgramDao.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("tickler", "tickler_category", "tickler_update","tickler_comments","custom_filter","provider","demographic","program","lst_gender", "admission", "demographic_merged",  
				"health_safety", "providersite", "site", "program_team","log", "Facility","program_queue");
	}
	
	protected Tickler createTickler(int demographicNo, String message, Integer programId,Date serviceDate, Tickler.STATUS status, String taskAssignedTo) {
		Tickler t = new Tickler();
		t.setCreator("999998");
		t.setDemographicNo(demographicNo);
		t.setMessage(message);
		t.setProgramId(programId);
		//t.setPriority(priority);
		t.setServiceDate(serviceDate);
		t.setStatus(status);
		t.setTaskAssignedTo(taskAssignedTo);
		
		return t;
	}
	
	protected Date today() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	protected Date past(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -days);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	protected Date yesterday() {
		return past(1);
	}
	
	protected Date tomorrow() {
		return future(1);
	}
	
	protected Date future(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, days);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	@Test 
	public void testFindSomeStuff() {
		dao.persist(this.createTickler(1, "hello there", 10015, today(), Tickler.STATUS.A, "1"));
		dao.persist(this.createTickler(1, "I am coding", 10015, yesterday(), Tickler.STATUS.C, "2"));
		dao.persist(this.createTickler(2, "this today", 10015, tomorrow(), Tickler.STATUS.A, "1"));
		
		assertTrue(1 == dao.findActiveByMessageForPatients(Arrays.asList(new Integer[] {1}), "hello").size());
		assertTrue(1 == dao.findActiveByDemographicNoAndMessage(1, "hello there").size());
		assertTrue(1 == dao.findByDemographicIdTaskAssignedToAndMessage(1, "1", "hello there").size());
		assertTrue(1 == dao.search_tickler_bydemo(2, "A", tomorrow(), future(2)).size());
	}
	
	@Test
	public void testSave() throws Exception {
		
		Demographic d = new Demographic();
		d.setFirstName("Test");
		d.setLastName("Patient");
		d.setPatientStatus("AC");
		d.setYearOfBirth("1978");
		d.setMonthOfBirth("04");
		d.setDateOfBirth("26");
		d.setHin("2222222222");
		d.setHcType("ON");
		d.setDateJoined(new Date());
		d.setSex("M");	
		demographicDao.save(d);
		
		Provider p = new Provider();
		p.setFirstName("John");
		p.setLastName("Hancock");
		p.setStatus("1");
		p.setProviderNo("000001");
		p.setProviderType("doctor");
		p.setSex("M");
		p.setSpecialty("");
		providerDao.saveProvider(p);
		
		Program prog = new Program();
		prog.setFacilityId(1);
		prog.setName("test");
		prog.setMaxAllowed(100);
		programDao.saveProgram(prog);
		
		Tickler entity = new Tickler();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setDemographicNo(d.getDemographicNo());
		entity.setCreator("999998");
		entity.setTaskAssignedTo("000001");
		//i'm testing the not found attribute here
		entity.setProgramId(9999);
		
		TicklerUpdate tu = new TicklerUpdate();
		tu.setAssignedTo("999998");
		tu.setPriority("Normal");
		tu.setProviderNo("999998");
		tu.setServiceDate(new Date());
		tu.setStatus(Tickler.STATUS.A);
		tu.setUpdateDate(new Date());
		
		TicklerComment tc = new TicklerComment();
		tc.setMessage("message");
		tc.setProviderNo("999998");
		tc.setUpdateDate(new Date());
		
		dao.persist(entity);
		assertNotNull(entity.getId());
		
		
		tu.setTicklerNo(entity.getId());
		dao.persist(tu);
		
		tc.setTicklerNo(entity.getId());
		dao.persist(tc);
		
		
		Tickler t = dao.find(entity.getId());
		assertTrue(1 == t.getUpdates().size());
		assertTrue(1 == t.getComments().size());
		assertTrue(t.getDemographic() != null);
		assertTrue(t.getProvider() != null);
		assertTrue(t.getAssignee() != null);
		assertTrue(t.getProgram() == null);
		assertTrue(t.getUpdates().iterator().next().getProvider() != null);
		assertTrue(t.getComments().iterator().next().getProvider() != null);
	}
}
