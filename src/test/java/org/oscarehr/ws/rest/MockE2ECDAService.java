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


package org.oscarehr.ws.rest;

import org.marc.everest.formatters.interfaces.IFormatterGraphResult;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.rest.E2ECDAService;

public class MockE2ECDAService extends E2ECDAService {
	
	protected String document = null;
	protected ClinicalDocument clinicalDocument = null;
	protected boolean CDAIsValid = false;
	protected boolean XMLIsValid = false;
	protected LoggedInInfo loggedInInfo = null;
	
	public MockE2ECDAService() {
		securityInfoManager = null;
	}

	public void setSecurityInfoManager(SecurityInfoManager securityInfoManager) {
		this.securityInfoManager = securityInfoManager;
	}

  	public LoggedInInfo getLoggedInInfo() {
		return loggedInInfo;
	}
	
	public void setLoggedInInfo(LoggedInInfo loggedInInfo) {
		this.loggedInInfo = loggedInInfo;
	}

	
	public void setClinicalDocument(ClinicalDocument clinicalDocument) {
		this.clinicalDocument = clinicalDocument;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public Boolean isValidCDA(IFormatterGraphResult details)
	{
		return CDAIsValid;
	}
	
	public void setCDAIsValid(Boolean CDAIsValid) {
		this.CDAIsValid = CDAIsValid;
	}
	
	protected Boolean isValidXML(String output) {
		return XMLIsValid;
	}
	
	public void setXMLIsValid(Boolean XMLIsValid) {
		this.XMLIsValid = XMLIsValid;
	}  	

    protected String generateDocumentToString(ClinicalDocument clinicalDocument, Boolean validation) {
    	return document;
    }
    
    protected ClinicalDocument createEmrConversionDocument(int id) {
    	return clinicalDocument;
    }
    
    protected String prettyFormatXML(String string, Integer indent) {
    	return document;
    }
    
    protected void addLogSynchronous(LoggedInInfo loggedInInfo, String message, String paramaValue) {
    	return;
    }
}