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


package com.indivica.olis.segments;

import java.util.Random;

import com.indivica.olis.queries.Query;
import com.indivica.olis.queries.QueryType;

public class SPRSegment implements Segment {

	private QueryType queryType;
	private Query query;
	private Random r = new Random();
	private Integer transactionId = r.nextInt(999999999);
	
	public Integer getTransactionId() {
    	return transactionId;
    }

	public SPRSegment(QueryType queryType, Query query) {
		this.queryType = queryType;
		this.query = query;
	}
	
	private String getStoredProcedureName() {
		if (queryType == QueryType.Z01)
			return "Z_QryLabInfoForPatientID";
		else if (queryType == QueryType.Z02)
			return "Z_QryLabInfoForOrderID";
		else if (queryType == QueryType.Z04)
			return "Z_QryLabInfoUpdatesForPractitionerID";
		else if (queryType == QueryType.Z05)
			return "Z_QryLabInfoUpdatesForLaboratoryID";
		else if (queryType == QueryType.Z06)
			return "Z_QryLabInfoUpdatesForHCFID";
		else if (queryType == QueryType.Z07)
			return "Z_QryLabInfoByPHBReportFlag";
		else if (queryType == QueryType.Z08)
			return "Z_QryLabInfoByCCOReportFlag";
		else if (queryType == QueryType.Z50)
			return "Z_IDPatientByNameSexDoB";
		
		return "";
	}
	
	@Override
	public String getSegmentHL7String() {
		String queryHl7String = query.getQueryHL7String();
		
		if (queryHl7String.charAt(queryHl7String.length()-1) == '~')
			queryHl7String = queryHl7String.substring(0, queryHl7String.length()-2);
		
		return "SPR|" + transactionId + "|R|" + getStoredProcedureName() + "^^HL70471|" + query.getQueryHL7String();
	}

}
