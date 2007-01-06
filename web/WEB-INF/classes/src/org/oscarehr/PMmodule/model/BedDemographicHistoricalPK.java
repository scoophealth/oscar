/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.PMmodule.model;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.oscarehr.PMmodule.model.base.BaseBedDemographicHistoricalPK;

public class BedDemographicHistoricalPK extends BaseBedDemographicHistoricalPK {
	
	private static final long serialVersionUID = 1L;
	
	public static BedDemographicHistoricalPK create(BedDemographicPK bedDemographicPK, Date usageStart) {
		BedDemographicHistoricalPK historicalPK = new BedDemographicHistoricalPK();
		
		historicalPK.setBedId(bedDemographicPK.getBedId());
		historicalPK.setDemographicNo(bedDemographicPK.getDemographicNo());
		historicalPK.setUsageStart(usageStart);
		
		return historicalPK;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	
	public BedDemographicHistoricalPK() {
	}

	public BedDemographicHistoricalPK(java.lang.Integer bedId, java.lang.Integer demographicNo, java.util.Date usageStart) {
		super(bedId, demographicNo, usageStart);
	}
	
	/* [CONSTRUCTOR MARKER END] */
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}