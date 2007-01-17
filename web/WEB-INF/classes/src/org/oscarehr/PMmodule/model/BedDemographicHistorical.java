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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.oscarehr.PMmodule.model.base.BaseBedDemographicHistorical;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;

public class BedDemographicHistorical extends BaseBedDemographicHistorical {

	private static final long serialVersionUID = 1L;

	public static BedDemographicHistorical create(BedDemographic bedDemographic) {
		BedDemographicHistorical historical = new BedDemographicHistorical();

		historical.setId(BedDemographicHistoricalPK.create(bedDemographic.getId(), bedDemographic.getReservationStart()));
		historical.setUsageEnd(DateTimeFormatUtils.getToday());

		return historical;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */

	public BedDemographicHistorical() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BedDemographicHistorical(org.oscarehr.PMmodule.model.BedDemographicHistoricalPK id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BedDemographicHistorical(org.oscarehr.PMmodule.model.BedDemographicHistoricalPK id, java.util.Date usageEnd) {
		super(id, usageEnd);
	}

	/* [CONSTRUCTOR MARKER END] */

	private Bed bed;
	private Demographic demographic;
	
	public void setBed(Bed bed) {
	    this.bed = bed;
    }
	
	public void setDemographic(Demographic demographic) {
		this.demographic = demographic;
	}

	public String getBedName() {
		return bed != null ? bed.getName() : null;
	}
	
	public String getDemographicName() {
		return demographic != null ? demographic.getFormattedName() : null;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}