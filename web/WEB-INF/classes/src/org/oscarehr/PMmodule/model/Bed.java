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

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.oscarehr.PMmodule.model.base.BaseBed;

public class Bed extends BaseBed {

	private static final long serialVersionUID = 1L;

	private static final Integer DEFAULT_ROOM_ID = 1;
	private static final String DEFAULT_NAME = "";
	private static final boolean DEFAULT_ACTIVE = true;

	public static Bed create(BedType bedType) {
		Bed bed = new Bed();
		bed.setBedTypeId(bedType.getId());
		bed.setRoomId(DEFAULT_ROOM_ID);
		bed.setRoomStart(Calendar.getInstance().getTime());
		bed.setName(DEFAULT_NAME);
		bed.setActive(DEFAULT_ACTIVE);

		return bed;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */

	public Bed() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Bed(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Bed(java.lang.Integer id, java.lang.Integer bedTypeId, java.lang.Integer roomId, java.util.Date roomStart, java.lang.String name, boolean active) {
		super(id, bedTypeId, roomId, roomStart, name, active);
	}

	/* [CONSTRUCTOR MARKER END] */

	private BedType bedType;
	private Room room;
	private ProgramTeam team;
	private BedDemographic bedDemographic;
	private Integer communityProgramId;

	public boolean isReserved() {
		return getReservationStart() != null && getReservationEnd() != null;
	}
	
	public boolean isLatePass() {
		return bedDemographic != null ? bedDemographic.isLatePass() : false;
	}

	public String getBedTypeName() {
		return bedType.getName();
	}

	public String getRoomName() {
		return room.getName();
	}

	public String getProgramName() {
		return room.getProgramName();
	}

	public String getTeamName() {
		return team != null ? team.getName() : null;
	}

	public BedDemographic getBedDemographic() {
	    return bedDemographic;
    }
	
	public String getDemographicName() {
		return bedDemographic != null ? bedDemographic.getDemographicName() : null;
	}

	public Integer getStatusId() {
		return bedDemographic != null ? bedDemographic.getBedDemographicStatusId() : null;
	}
	
	public String getStatusName() {
		return bedDemographic != null ? bedDemographic.getStatusName() : null;
	}

	public Date getReservationStart() {
		return bedDemographic != null ? bedDemographic.getReservationStart() : null;
	}
	
	public Date getReservationEnd() {
		return bedDemographic != null ? bedDemographic.getReservationEnd() : null;
	}

	public String getStrReservationEnd() {
		return bedDemographic != null ? bedDemographic.getStrReservationEnd() : null;
	}
	
	public Integer getCommunityProgramId() {
		return communityProgramId;
	}
	
	
	public void setBedType(BedType bedType) {
		this.bedType = bedType;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public void setTeam(ProgramTeam team) {
		this.team = team;
	}

	public void setBedDemographic(BedDemographic bedDemographic) {
		this.bedDemographic = bedDemographic;
	}

	public void setStatusId(Integer statusId) {
		if (bedDemographic != null) {
			bedDemographic.setBedDemographicStatusId(statusId);
		}
	}

	public void setLatePass(boolean latePass) {
		if (bedDemographic != null) {
			bedDemographic.setLatePass(latePass);
		}
	}

	public void setStrReservationEnd(String strReservationEnd) {
		if (bedDemographic != null) {
			bedDemographic.setStrReservationEnd(strReservationEnd);
		}
	}

	public void setCommunityProgramId(Integer communityProgramId) {
		this.communityProgramId = communityProgramId;
	}
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}