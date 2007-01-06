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
import org.oscarehr.PMmodule.model.base.BaseBedDemographic;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;

public class BedDemographic extends BaseBedDemographic {

	private static final long serialVersionUID = 1L;

	public static BedDemographic create(Integer demographicNo, BedDemographicStatus bedDemographicStatus, String providerNo) {
		BedDemographicPK id = new BedDemographicPK();
		id.setDemographicNo(demographicNo);

		BedDemographic bedDemographic = new BedDemographic();
		bedDemographic.setId(id);
		bedDemographic.setBedDemographicStatusId(bedDemographicStatus.getId());
		bedDemographic.setProviderNo(providerNo);

		// set reservation start to today and reservation end to today + duration
		Date today = DateTimeFormatUtils.getToday();
		
		bedDemographic.setReservationStart(today);
		bedDemographic.setReservationEnd(DateTimeFormatUtils.getFuture(today, bedDemographicStatus.getDuration()));

		return bedDemographic;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */

	public BedDemographic() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BedDemographic(org.oscarehr.PMmodule.model.BedDemographicPK id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BedDemographic(org.oscarehr.PMmodule.model.BedDemographicPK id, java.lang.Integer bedDemographicStatusId, java.lang.String providerNo, boolean latePass, java.util.Date reservationStart, java.util.Date reservationEnd) {
		super(id, bedDemographicStatusId, providerNo, latePass, reservationStart, reservationEnd);
	}

	/* [CONSTRUCTOR MARKER END] */

	private BedDemographicStatus bedDemographicStatus;
	private Provider provider;
	private Bed bed;
	private Demographic demographic;
	
	public void setBedDemographicStatus(BedDemographicStatus bedDemographicStatus) {
	    this.bedDemographicStatus = bedDemographicStatus;
    }
	
	public void setProvider(Provider provider) {
	    this.provider = provider;
    }
	
	public void setBed(Bed bed) {
	    this.bed = bed;
    }
	
	public void setDemographic(Demographic demographic) {
	    this.demographic = demographic;
    }
	
	public boolean isExpired() {
		Date end = DateTimeFormatUtils.getDateFromDate(getReservationEnd());
		Date today = DateTimeFormatUtils.getToday();
		
		return end.before(today);
	}
	
	public boolean isValidReservation() {
		Date start = DateTimeFormatUtils.getDateFromDate(getReservationStart());
		Date end = DateTimeFormatUtils.getDateFromDate(getReservationEnd());
		Date today = DateTimeFormatUtils.getToday();

		return start.before(end) && today.before(end);
	}

	public String getStatusName() {
		return bedDemographicStatus.getName();
	}

	public void setStatusName(String statusName) {
		// immutable
		System.out.println("BedDemographic.setStatusName()");
	}

	public String getProviderName() {
		return provider.getFormattedName();
	}

	public void setProviderName(String providerName) {
		// immutable
		System.out.println("BedDemographic.setProviderName()");
	}

	public String getBedName() {
		return bed != null ? bed.getName() : null;
	}

	public void setBedName(String bedName) {
		// immutable
		System.out.println("BedDemographic.setBedName()");
	}

	public String getDemographicName() {
		return demographic != null ? demographic.getFormattedName() : null;
	}

	public void setDemographicName(String demographicName) {
		// immutable
		System.out.println("BedDemographic.setDemographicName()");
	}

	public String getRoomName() {
		return bed != null ? bed.getRoomName() : null;
	}

	public void setRoomName(String roomName) {
		// immutable
		System.out.println("BedDemographic.setRoomName()");
	}

	public String getProgramName() {
		return bed != null ? bed.getProgramName() : null;
	}

	public void setProgramName(String programName) {
		// immutable
		System.out.println("BedDemographic.setProgramName()");
	}

	// property adapted for view
	public Integer getBedId() {
		return getId().getBedId();
	}

	// property adapted for view
	public void setBedId(Integer bedId) {
		getId().setBedId(bedId);
	}

	// property adapted for view
	public String getStrReservationEnd() {
		return DateTimeFormatUtils.getStringFromDate(getReservationEnd());
	}

	// property adapted for view
	public void setStrReservationEnd(String strReservationEnd) {
		setReservationEnd(DateTimeFormatUtils.getDateFromString(strReservationEnd));
	}

	public void setReservationEnd(Integer duration) {
		if (duration != null && duration > 0) {
			Date startPlusDuration = DateTimeFormatUtils.getFuture(getReservationStart(), duration);
			Date end = DateTimeFormatUtils.getDateFromDate(getReservationEnd());
			
			// start + duration > end
			if (startPlusDuration.after(end)) {
				setReservationEnd(startPlusDuration);
			}
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}