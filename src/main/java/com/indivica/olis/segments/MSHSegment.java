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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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
		return "MSH|^~\\&|^CN=EMR.MCMUN2.CST,OU=Applications,OU=eHealthUsers,OU=Subscribers,DC=subscribers,DC=ssh^X500|BSD 4001|" +
			"^OLIS^X500||" + dateFormatter.format(new Date()) + "||SPQ^" + queryType.toString() + "^SPQ_Q08|" + uuidString + "|T|2.3.1||||||8859/1";
	}

}
