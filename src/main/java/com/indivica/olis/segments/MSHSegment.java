/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package com.indivica.olis.segments;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import oscar.OscarProperties;

import com.indivica.olis.queries.QueryType;

public class MSHSegment implements Segment {

	private QueryType queryType;
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmssZZZZZ");
	private String uuidString = UUID.randomUUID().toString();
	
	public MSHSegment(QueryType queryType) {
		this.queryType = queryType;
	}
	
	public String getUuidString() {
		return uuidString;
	}
	
	@Override
	public String getSegmentHL7String() {
		
		String sendingApplication  = OscarProperties.getInstance().getProperty("OLIS_SENDING_APPLICATION", "^CN=EMR.MCMUN2.CST,OU=Applications,OU=eHealthUsers,OU=Subscribers,DC=subscribers,DC=ssh^X500");
		String processingId = OscarProperties.getInstance().getProperty("OLIS_PROCESSING_ID","P"); //set to "T" for testing;
		
		return "MSH|^~\\&|"+sendingApplication+"|MCMUN2|" +
			"^OLIS^X500||" + dateFormatter.format(new Date()) + "||SPQ^" + queryType.toString() + "^SPQ_Q08|" + uuidString + "|"+processingId+"|2.3.1||||||8859/1";
	}

}
