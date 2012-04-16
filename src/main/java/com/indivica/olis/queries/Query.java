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

import com.indivica.olis.parameters.ZPD1;

public abstract class Query implements Cloneable {

	public String demographicNo;
	
	public String getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(String demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public abstract String getQueryHL7String();
	
	public abstract QueryType getQueryType();
	
	public abstract void setConsentToViewBlockedInformation(ZPD1 consentToViewBlockedInformation);
	
	 public Object clone() {
	    try {
	        return super.clone();
	    }
	    catch (CloneNotSupportedException e) {
	        throw new InternalError(e.toString());
	    }
	}	
}
