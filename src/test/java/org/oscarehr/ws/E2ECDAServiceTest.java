/**
 * Copyright (c) 2013-2015. Leverage Analytics. All Rights Reserved.
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
 */


package org.oscarehr.ws;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.oscarehr.common.exception.AccessDeniedException;
import org.oscarehr.common.exception.XMLValidationException;
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.MockSecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.common.exception.NotFoundException;
import org.oscarehr.ws.rest.MockE2ECDAService;

public class E2ECDAServiceTest {
 
    private MockE2ECDAService mockE2ECDAService;

    @Before
    public void before() {

        this.mockE2ECDAService = new MockE2ECDAService();

    }

    @After
    public void after() {

        this.mockE2ECDAService = null;

    }

    @Test(expected=AccessDeniedException.class)
    public void testProhibitedRecord() {
    	try{
    		MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
    		mockSecurityInfoManager.setAccessPatientRecord(false);//access prohibited
    		mockE2ECDAService.setSecurityInfoManager(mockSecurityInfoManager);
    		LoggedInInfo loggedInInfo = new LoggedInInfo();
    		mockE2ECDAService.setLoggedInInfo(loggedInInfo);
    		
    		Provider loggedInProvider = new Provider();
    		loggedInProvider.setFirstName("Test");
    		loggedInProvider.setLastName("Doe");
    		
    		loggedInInfo.setLoggedInProvider(loggedInProvider);
    		mockE2ECDAService.getDemographicData(1);
    	}
    	catch(AccessDeniedException expectedException) {
    		throw expectedException;
    	}
    	catch(RuntimeException e) {
    		e.printStackTrace();
    		throw e;
    	}
    }
 
    @Test(expected=NotFoundException.class)
    public void testNotFoundRecord() {
    	try{
    		MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
    		mockSecurityInfoManager.setAccessPatientRecord(true);
    		mockE2ECDAService.setClinicalDocument(null);//no record
    		mockE2ECDAService.setSecurityInfoManager(mockSecurityInfoManager);
    		LoggedInInfo loggedInInfo = new LoggedInInfo();
    		mockE2ECDAService.setLoggedInInfo(loggedInInfo);		
    		
    		mockE2ECDAService.getDemographicData(1);//access non-prohibited record
    	}
    	catch(NotFoundException expectedException) {
    		throw expectedException;
    	}
    	catch(RuntimeException e) {
    		e.printStackTrace();
    		throw e;
    	}
    }
 
    /*
    @Test(expected=CDAValidationException.class)
    public void testInvalidCDA() {
    	((TestableE2ECDAService)service).setDocument("Test\n");//access to non null record
    	((TestableE2ECDAService)service).setCDADocument(new ClinicalDocument());//access non-null record
    	((TestableE2ECDAService)service).setCDAisValid(false);
    	((TestableE2ECDAService)service).setXMLisValid(true);
    	
    	Response response = service.getDemographicData(1);//access non-prohibited record
    }*///TODO: Revisit this after the E2E CDA bug is resolved
    
    @Test(expected=XMLValidationException.class)
    public void testInvalidXML() {
		MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
		mockSecurityInfoManager.setAccessPatientRecord(true);
		mockE2ECDAService.setClinicalDocument(new ClinicalDocument());
		mockE2ECDAService.setDocument("Test\n");
    	mockE2ECDAService.setCDAIsValid(true);
    	mockE2ECDAService.setXMLIsValid(false);//xml invalid
		mockE2ECDAService.setSecurityInfoManager(mockSecurityInfoManager);
		LoggedInInfo loggedInInfo = new LoggedInInfo();
		mockE2ECDAService.setLoggedInInfo(loggedInInfo);
    	Response response = mockE2ECDAService.getDemographicData(1);//access non-prohibited record
    }
    
    @Test
    public void testFoundRecord() {
		MockSecurityInfoManager mockSecurityInfoManager = new MockSecurityInfoManager();
		mockSecurityInfoManager.setAccessPatientRecord(true);
		mockE2ECDAService.setClinicalDocument(new ClinicalDocument());
		mockE2ECDAService.setDocument("Test\n");
    	mockE2ECDAService.setCDAIsValid(true);
    	mockE2ECDAService.setXMLIsValid(true);
		mockE2ECDAService.setSecurityInfoManager(mockSecurityInfoManager);
		LoggedInInfo loggedInInfo = new LoggedInInfo();
		mockE2ECDAService.setLoggedInInfo(loggedInInfo);
    	
    	Response response = mockE2ECDAService.getDemographicData(1);//access non-prohibited record
    	
    	assert response.getStatus() == 200;
    }
}