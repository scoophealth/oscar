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
package org.oscarehr.dashboard.handler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
// import org.junit.Test;
import org.oscarehr.common.model.Dashboard;
import org.oscarehr.common.model.IndicatorTemplate;
import org.oscarehr.dashboard.display.beans.DashboardBean;
import org.oscarehr.dashboard.factory.DashboardBeanFactory;


/**
 * 
 * A test of the IndicatorTemplateHandler, Dashboard entity and the DashboardBeanFactory. 
 *
 */
public class DashboardBeanFactoryTest {

	private static DashboardBeanFactory dashboardBeanFactory;
	private static Dashboard dashboard;
	private static DashboardBean dashboardBean;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		
		URL url = Thread.currentThread().getContextClassLoader().getResource("indicatorXMLTemplates/diabetes_hba1c_test.xml");
		byte[] byteArray = null;
		InputStream is = null;
		try {
			is = url.openStream();
			byteArray = IOUtils.toByteArray(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	    // test new handler with a byte array feed.
	    IndicatorTemplateHandler indicatorTemplateHandler = new IndicatorTemplateHandler(byteArray);	
	    
	    // test retrieve new IndicatorTemplate entity.
	    IndicatorTemplate indicatorTemplate = indicatorTemplateHandler.getIndicatorTemplateEntity();
	    
	    // test add IndicatorTemplate entity to new List
	    ArrayList<IndicatorTemplate> indicatorTemplateList = new ArrayList<IndicatorTemplate>();
	    indicatorTemplateList.add(indicatorTemplate);
	    indicatorTemplateList.add(indicatorTemplate);
	    
	    dashboard = new Dashboard();
	    dashboard.setActive(Boolean.TRUE);
	    dashboard.setCreator("colcamex");
	    dashboard.setDescription("colcamex test case");
	    dashboard.setEdited(new Date( System.currentTimeMillis() ) );
	    dashboard.setId(100);
	    dashboard.setLocked(Boolean.FALSE);
	    dashboard.setName("test dashboard");
	    
	    // The DashboardBeanFactory should construct a full DashboardBean with complete queries etc... for final display.
	    // Dashboard entities in production will already contain a complete list of related IndicatorTemplates
	    // dashboardBeanFactory = new DashboardBeanFactory(dashboard, indicatorTemplateList);
	    dashboardBean = dashboardBeanFactory.getDashboardBean();
	}

	@AfterClass
	public static void tearDownAfterClass() {
		dashboardBeanFactory = null;
		dashboard = null;
		dashboardBean = null;
	}


	//@Test
	public void testGetDashboardBean() {
		assertEquals( "100", dashboardBean.getId() + "" );
	}
	 
	//@Test
	public void testGetActive () {
		assertTrue( dashboardBean.isActive() );
	}
	
	//@Test
	public void testGetCreator () {
		assertEquals( dashboard.getCreator(), dashboardBean.getCreator() );
	}
	
	//@Test
	public void testGetDescription () {
		assertEquals( dashboard.getDescription(), dashboardBean.getDescription() );
	}
	
	//@Test
	public void testGetEdited () {
		assertEquals( dashboard.getEdited(), dashboardBean.getEdited() );
	}
	
	//@Test
	public void testGetId () {
		assertEquals( dashboard.getId(), dashboardBean.getId() );
	}
	
	//@Test
	public void testGetLocked () {
		assertFalse( dashboardBean.isLocked() );
	}
	
	//@Test
	public void testGetName () {
		assertEquals( dashboard.getName(), dashboardBean.getName() );
	}

}
