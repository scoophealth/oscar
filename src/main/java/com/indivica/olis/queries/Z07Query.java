/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package com.indivica.olis.queries;

import com.indivica.olis.parameters.OBR22;
import com.indivica.olis.parameters.QRD7;
import com.indivica.olis.parameters.ZPD1;

/**
 * Z07 - Retrieve Test Results Reportable to Public Health
 * @author jen
 *
 */
public class Z07Query extends Query {

	private OBR22 startEndTimestamp = new OBR22(); // mandatory
	private QRD7 quantityLimitedRequest = null;
	
	@Override
	public String getQueryHL7String() {
		String query = "";

		if (startEndTimestamp != null)
			query += startEndTimestamp.toOlisString() + "~";

		if (quantityLimitedRequest != null)
			query += quantityLimitedRequest.toOlisString() + "~";

		if(query.endsWith("~")) {
			query = query.substring(0,query.length()-1);
		}
		
		return query;
	}

	public void setStartEndTimestamp(OBR22 startEndTimestamp) {
    	this.startEndTimestamp = startEndTimestamp;
    }

	public void setQuantityLimitedRequest(QRD7 quantityLimitedRequest) {
    	this.quantityLimitedRequest = quantityLimitedRequest;
    }

	@Override
	public QueryType getQueryType() {
		return QueryType.Z07;
	}
	@Override
    public void setConsentToViewBlockedInformation(ZPD1 consentToViewBlockedInformation) {
		throw new RuntimeException("Not valid for this type of query.");
    }
}
