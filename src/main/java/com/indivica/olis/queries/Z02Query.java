/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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

package com.indivica.olis.queries;

import com.indivica.olis.parameters.ORC4;
import com.indivica.olis.parameters.PID3;
import com.indivica.olis.parameters.ZBX1;
import com.indivica.olis.parameters.ZPD1;
import com.indivica.olis.parameters.ZPD3;
import com.indivica.olis.parameters.ZRP1;

/**
 * Z02 - Retrieve Laboratory Information for Order ID
 * @author jen
 *
 */
public class Z02Query extends Query {

	private ZBX1 retrieveAllTestResults = null;
	private ZRP1 requestingHic = new ZRP1(); // mandatory
	private ZPD1 consentToViewBlockedInformation = null;
	private ZPD3 patientConsentBlockAllIndicator = null;
	private PID3 patientIdentifier = new PID3(); // mandatory
	private ORC4 placerGroupNumber = new ORC4(); // mandatory
	

	@Override
	public String getQueryHL7String() {
		String query = "";
		
		if (retrieveAllTestResults != null)
			query += retrieveAllTestResults.toOlisString() + "~";
		
		if (requestingHic != null)
			query += requestingHic.toOlisString() + "~";
		
		if (consentToViewBlockedInformation != null)
			query += consentToViewBlockedInformation.toOlisString() + "~";
		
		if (patientConsentBlockAllIndicator != null)
			query += patientConsentBlockAllIndicator.toOlisString() + "~";
		
		if (patientIdentifier != null)
			query += patientIdentifier.toOlisString() + "~";
		
		if (placerGroupNumber != null)
			query += placerGroupNumber.toOlisString() + "~";
		
		if(query.endsWith("~")) {
			query = query.substring(0,query.length()-1);
		}
		
		return query;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.Z02;
	}
	
	public void setRetrieveAllTestResults(ZBX1 retrieveAllTestResults) {
    	this.retrieveAllTestResults = retrieveAllTestResults;
    }

	public void setRequestingHic(ZRP1 requestingHic) {
    	this.requestingHic = requestingHic;
    }

	public void setConsentToViewBlockedInformation(ZPD1 consentToViewBlockedInformation) {
    	this.consentToViewBlockedInformation = consentToViewBlockedInformation;
    }

	public void setPatientConsentBlockAllIndicator(ZPD3 patientConsentBlockAllIndicator) {
    	this.patientConsentBlockAllIndicator = patientConsentBlockAllIndicator;
    }

	public void setPatientIdentifier(PID3 patientIdentifier) {
    	this.patientIdentifier = patientIdentifier;
    }

	public void setPlacerGroupNumber(ORC4 placerGroupNumber) {
    	this.placerGroupNumber = placerGroupNumber;
    }


}
