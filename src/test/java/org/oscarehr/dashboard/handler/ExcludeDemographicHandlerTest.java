/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */

package org.oscarehr.dashboard.handler;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class ExcludeDemographicHandlerTest {
	
	private static ExcludeDemographicHandler excludeDemographicHandler;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    	SchemaUtils.restoreTable("provider", "demographicExt");
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoAsCurrentClassAndMethod();
        Provider provider = new Provider();
        provider.setProviderNo("100");
        loggedInInfo.setLoggedInProvider( provider );
        excludeDemographicHandler = new ExcludeDemographicHandler();
        excludeDemographicHandler.setLoggedinInfo(loggedInInfo);
    }

//    @After
//    public void tearDown() throws Exception {
//    }

//    @Test
//    public void getDemoIdMap() {
//    	assertEquals(0, excludeDemographicHandler.getDemoIdMap().size());
//    }
    
    @Test
    public void getDemoIds() {
    	//excludeDemographicHandler = new ExcludeDemographicHandler(); 
    	assertEquals(0, excludeDemographicHandler.getDemoIds("indicatorName").size());
    }
    
    @Test
    public void setDemoId() {
    	//excludeDemographicHandler = new ExcludeDemographicHandler();
    	excludeDemographicHandler.excludeDemoId(20, "myIndicatorName1");
    	assertEquals(1, excludeDemographicHandler.getDemoIds("myIndicatorName1").size());
    	assertTrue(20 == excludeDemographicHandler.getDemoIds("myIndicatorName1").get(0));
    }
    
    @Test
    public void setDemoIDList() {
    	//excludeDemographicHandler = new ExcludeDemographicHandler();
    	int[] array = {20,30,40,50};
    	List<Integer> list = new ArrayList<Integer>();
    	for (int el: array) {
    		list.add(el);
    	}
    	excludeDemographicHandler.excludeDemoIds(list, "myIndicatorName2");
    	assertEquals(array.length, excludeDemographicHandler.getDemoIds("myIndicatorName2").size());
    	int i = 0;
    	for (int el: array) {
    		assertTrue(el == excludeDemographicHandler.getDemoIds("myIndicatorName2").get(i));
    		i++;
    	}
    }
    
    @Test
    public void setDemoIdJson() {
    	//excludeDemographicHandler = new ExcludeDemographicHandler();
    	String jsonStr = "20,30,40,50";
    	excludeDemographicHandler.excludeDemoIds(jsonStr, "myIndicatorName3");
    	int i = 0;
    	for (int el = 20; el < 60; el = el + 10) {
    		assertTrue(el == excludeDemographicHandler.getDemoIds("myIndicatorName3").get(i));
    		i++;
    	}
    }
}