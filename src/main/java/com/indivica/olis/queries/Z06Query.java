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

import com.indivica.olis.parameters.OBR22;
import com.indivica.olis.parameters.ORC21;
import com.indivica.olis.parameters.QRD7;
import com.indivica.olis.parameters.ZPD1;

/**
 * Z06 - Retrieve Laboratory Information Updates for Ordering Facility 
 * @author jen
 *
 */
public class Z06Query extends Query {

	private OBR22 startEndTimestamp = new OBR22(); // mandatory
	private QRD7 quantityLimitedRequest = null;
	private ORC21 orderingFacilityId = new ORC21(); // mandatory
	
	@Override
	public String getQueryHL7String() {
		String query = "";

		if (startEndTimestamp != null)
			query += startEndTimestamp.toOlisString() + "~";

		if (quantityLimitedRequest != null)
			query += quantityLimitedRequest.toOlisString() + "~";

		if (orderingFacilityId != null)
			query += orderingFacilityId.toOlisString() + "~";
		
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

	public void setOrderingFacilityId(ORC21 orderingFacilityId) {
    	this.orderingFacilityId = orderingFacilityId;
    }

	@Override
	public QueryType getQueryType() {
		return QueryType.Z06;
	}
	@Override
    public void setConsentToViewBlockedInformation(ZPD1 consentToViewBlockedInformation) {
		throw new RuntimeException("Not valid for this type of query.");
    }
}
