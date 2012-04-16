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

package com.indivica.olis;

import com.indivica.olis.queries.Query;
import com.indivica.olis.segments.DSCSegment;
import com.indivica.olis.segments.MSHSegment;
import com.indivica.olis.segments.SPRSegment;
import com.indivica.olis.segments.ZSHSegment;

public class OLISMessage {

	private MSHSegment mshSegment;
	private ZSHSegment zshSegment;
	private SPRSegment sprSegment;
	private DSCSegment dscSegment = null;
	
	public OLISMessage(Query query) {
		mshSegment = new MSHSegment(query.getQueryType());
		zshSegment = new ZSHSegment();
		sprSegment = new SPRSegment(query.getQueryType(), query);
	}
	
	public OLISMessage(Query query, String continuationPointer) {
		mshSegment = new MSHSegment(query.getQueryType());
		zshSegment = new ZSHSegment();
		sprSegment = new SPRSegment(query.getQueryType(), query);
		dscSegment = new DSCSegment(continuationPointer);
	}	
	
	public String getTransactionId() {
		return mshSegment.getUuidString();
	}
	
	public String getOlisHL7String() {
		String output = "";
		
		output += mshSegment.getSegmentHL7String() + "\r";
		output += zshSegment.getSegmentHL7String() + "\r";
		output += sprSegment.getSegmentHL7String();
		
		if (dscSegment != null)
			output += "\r" + dscSegment.getSegmentHL7String();
		
		return output;
	}
	
}
