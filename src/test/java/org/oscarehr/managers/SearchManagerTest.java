package org.oscarehr.managers;
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
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.pdfbox.util.XMLUtil;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.PMmodule.dao.SecUserRoleDao;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.appointment.search.SearchConfig;
import org.oscarehr.appointment.search.TimeSlot;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.SecObjPrivilegeDao;
import org.oscarehr.common.dao.utils.AuthUtils;
import org.oscarehr.common.dao.utils.ConfigUtils;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.SecObjPrivilege;
import org.oscarehr.common.model.SecObjPrivilegePrimaryKey;
//import org.oscarehr.common.model.SecObjPrivilege;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import org.w3c.dom.Document;


public class SearchManagerTest extends DaoTestFixtures {
	protected Logger logger = MiscUtils.getLogger();
	AppointmentSearchManager appointmentSearchManager = SpringUtils.getBean(AppointmentSearchManager.class);
	SecObjPrivilegeDao secObjPrivilegeDao = SpringUtils.getBean(SecObjPrivilegeDao.class); 
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	Demographic demographic= null;
	
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(new String[] { "demographic", "appointment", "secUserRole","secObjPrivilege","secRole","provider","lst_gender","demographic_merged","admission","program","health_safety","log","scheduleholiday","scheduledate"});
		
		SecUserRoleDao secUserRoleDao = SpringUtils.getBean(SecUserRoleDao.class);
		
		SecUserRole secUserRole = new SecUserRole();
		secUserRole.setProviderNo("-1");//-1");
		secUserRole.setRoleName("doctor");
		secUserRole.setActive(true);
		
		secUserRoleDao.save(secUserRole);
		
		 demographic = new Demographic();
		 demographic.setProviderNo("-1");
		 demographic.setFirstName("Test");
		 demographic.setLastName("Test");
		 demographic.setMiddleNames("");
		 demographic.setSex("M");
		
		 demographicDao.save(demographic);
		
		
	}
	
	@Test
	public void searchAppointment() throws Exception{
		LoggedInInfo loggedInInfo = AuthUtils.initLoginContext();
		
		InputStream is = SearchManagerTest.class.getResourceAsStream("/bookingconfig.xml");
		Document doc = XMLUtil.parse(is);
		
		SearchConfig config =  SearchConfig.fromDocument(doc);
		
		Integer demographicNo = demographic.getId();
		Long appointmentTypeId  = 1L;
		Calendar startDate = Calendar.getInstance();
		List<TimeSlot> timeslot = appointmentSearchManager.findAppointment(loggedInInfo,config, demographicNo, appointmentTypeId, startDate);
		logger.error("timeslot size: "+timeslot.size());
		for(TimeSlot ts: timeslot) {
			logger.error("time "+ts.getAvailableApptTime());
		}
	}

}
