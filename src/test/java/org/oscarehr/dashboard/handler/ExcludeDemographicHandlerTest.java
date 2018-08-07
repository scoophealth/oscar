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
import org.oscarehr.e2e.constant.Constants;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class ExcludeDemographicHandlerTest {
	
	private static ExcludeDemographicHandler excludeDemographicHandler;
	private static String indicatorName;
	Date now = new java.util.Date();
	
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        SchemaUtils.restoreTable("demographic", "lst_gender", "admission", "demographic_merged",
        		"program", "health_safety", "provider", "providersite", "site", "program_team",
        		"log", "Facility","demographicExt");
    	// Can't just restore tables because the data generator isn't generating
    	// data for demographicExt after some recent code changes.
    	// Instead restoring 2 patient records that are used for E2E testing so that
    	// demogrpahic_no 1 and 2 are populated.  Otherwise, there will be an error
    	// that they are missing when trying to retrieve from the demographicExt table
        // using those demographic_no.
    	SchemaUtils.restoreTable(Constants.Runtime.TABLES);
        assertEquals(0, SchemaUtils.loadFileIntoMySQL(Constants.Runtime.E2E_SETUP));
    	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoAsCurrentClassAndMethod();
        Provider provider = new Provider();
        provider.setProviderNo("100");
        loggedInInfo.setLoggedInProvider( provider );
        excludeDemographicHandler = new ExcludeDemographicHandler();
        excludeDemographicHandler.setLoggedinInfo(loggedInInfo);
    }

    @Test
    public void getDemoIds() {
    	indicatorName = "indicatorName_getDemoIds";
    	assertEquals(0, excludeDemographicHandler.getDemoIds(indicatorName).size());
    }
    
    @Test
    public void getDemoExts() {
    	indicatorName = "indicatorName_getDemoExts";
    	assertEquals(0, excludeDemographicHandler.getDemoExts(indicatorName).size());
    }
    
    @Test
    public void setDemoId() {
    	indicatorName = "myIndicatorName_setDemoId";
    	excludeDemographicHandler.excludeDemoId(2, indicatorName);
    	assertEquals(1, excludeDemographicHandler.getDemoIds(indicatorName).size());
    	assertTrue(excludeDemographicHandler.getDemoIds(indicatorName).get(0).equals(2));
    }
    
    @Test
    public void setDemoIDList() {
    	indicatorName = "myIndicatorName_setDemoIDList";
    	List<Integer> list = getDemoNos();
    	excludeDemographicHandler.excludeDemoIds(list, indicatorName);
    	List<Integer> demoIds = excludeDemographicHandler.getDemoIds(indicatorName);
    	assertEquals(list.size(), demoIds.size());
     	for (Integer el: list) {
    		assertTrue(demoIds.contains(el));
    	}
    	//assertTrue(demoIds.containsAll(list));
    }
    
    @Test
    public void unsetDemoIDList() {
    	indicatorName = "myIndicatorName_unsetDemoIDList";
    	List<Integer> list = getDemoNos();
    	excludeDemographicHandler.excludeDemoIds(list, indicatorName);
    	List<Integer> demoIds = excludeDemographicHandler.getDemoIds(indicatorName);
    	assertEquals(list.size(), demoIds.size());
    	// check whether we can add the same entries yet again
    	excludeDemographicHandler.excludeDemoIds(list, indicatorName);
    	demoIds = excludeDemographicHandler.getDemoIds(indicatorName);
    	assertEquals(2*list.size(), demoIds.size());
    	// also checks whether duplicated entries are removed
    	excludeDemographicHandler.unExcludeDemoIds(list, indicatorName);
    	assertEquals(0, excludeDemographicHandler.getDemoIds(indicatorName).size());
    }
    
    @Test
    public void setDemoIdJson() {
    	indicatorName = "myIndicatorName_setDemoIdJson";
    	String jsonStr = "1,2";
    	excludeDemographicHandler.excludeDemoIds(jsonStr, indicatorName);
    	List<Integer> demoIds = excludeDemographicHandler.getDemoIds(indicatorName);
    	for (int el = 1; el < 3; ++el) {
    		assertTrue(demoIds.contains(el));
    	}
    }
    
    @Test
    public void unsetDemoIdJson() {
    	indicatorName = "myIndicatorName_unsetDemoIdJson";
    	String jsonStr = "1,2";
    	excludeDemographicHandler.excludeDemoIds(jsonStr, indicatorName);
    	List<Integer> demoIds = excludeDemographicHandler.getDemoIds(indicatorName);
    	for (int el = 1; el < 3; ++el) {
    		assertTrue(demoIds.contains(el));
    	}
    	excludeDemographicHandler.unExcludeDemoIds(jsonStr, indicatorName);
    	assertEquals(0, excludeDemographicHandler.getDemoIds(indicatorName).size());
    }
    
    private static List<Integer> getDemoNos() {
    	int[] array = {1,2};
    	List<Integer> list = new ArrayList<Integer>();
    	for (int el: array) {
    		list.add(el);
    	}
    	return list;
    }
}