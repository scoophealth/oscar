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
	package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;

@Entity
@Table(name="room_demographic")
public class RoomDemographic extends AbstractModel<RoomDemographicPK> {

	@EmbeddedId
	private RoomDemographicPK id;
	
	@Column(name="provider_no",length=6)
	private String providerNo;
	
	@Column(name="assign_start")
	@Temporal(TemporalType.DATE)
	private Date assignStart;
	
	@Column(name="assign_end")
	@Temporal(TemporalType.DATE)
	private Date assignEnd;
	
	@Column(name="comments",length=50)
	private String comment;

	public RoomDemographic() {
		id = new RoomDemographicPK();
	}
	
	public RoomDemographicPK getId() {
		return id;
	}

	public void setId(RoomDemographicPK id) {
		this.id = id;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public Date getAssignStart() {
		return assignStart;
	}

	public void setAssignStart(Date assignStart) {
		this.assignStart = assignStart;
	}

	public Date getAssignEnd() {
		return assignEnd;
	}

	public void setAssignEnd(Date assignEnd) {
		this.assignEnd = assignEnd;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Transient
    private Provider provider;
	@Transient
    private Room room;
	@Transient
    private Demographic demographic;

    public static RoomDemographic create(Integer demographicNo, String providerNo) {
		RoomDemographicPK id = new RoomDemographicPK();
		id.setDemographicNo(demographicNo);

		RoomDemographic roomDemographic = new RoomDemographic();
		roomDemographic.setId(id);
		roomDemographic.setProviderNo(providerNo);

		// set assign start to today and assign end to today + duration
		Date today = new Date();
		
		roomDemographic.setAssignStart(today);
//		roomDemographic.setAssignEnd(today);
		return roomDemographic;
	}


	public void setDemographicNo(Integer demographicNo){
		getId().setDemographicNo(demographicNo);
	}
	public void setProvider(Provider provider) {
	    this.provider = provider;
    }
	
	public void setRoom(Room room) {
	    this.room = room;
    }
	
	public void setDemographic(Demographic demographic) {
	    this.demographic = demographic;
    }
	
	public boolean isExpired() {
		Date end = DateTimeFormatUtils.getDateFromDate(getAssignEnd());
		Date today = new Date();
		
		return end.before(today);
	}
	
	public boolean isValidAssign() {
		Date start = DateTimeFormatUtils.getDateFromDate(getAssignStart());
		Date end = DateTimeFormatUtils.getDateFromDate(getAssignEnd());
		Date today = new Date();

		return start.before(end) && today.before(end);
	}

	public String getProviderName() {
		return provider.getFormattedName();
	}

	public void setProviderName(String providerName) {
		// immutable
	}

	public Room getRoom() {
		return room;
	}

	public String getRoomName() {
		return room != null ? room.getName() : null;
	}

	public void setRoomName(String roomName) {
		// immutable
	}

	public String getDemographicName() {
		return demographic != null ? demographic.getFormattedName() : null;
	}

	public void setDemographicName(String demographicName) {
		// immutable
	}

	public String getProgramName() {
		return room != null ? room.getProgramName() : null;
	}

	public void setProgramName(String programName) {
		// immutable
	}

	// property adapted for view
	public Integer getRoomId() {
		return getId().getRoomId();
	}

	// property adapted for view
	public void setRoomId(Integer roomId) {
		getId().setRoomId(roomId);
	}

	// property adapted for view
	public String getStrAssignEnd() {
		return DateTimeFormatUtils.getStringFromDate(getAssignEnd());
	}

	// property adapted for view
	public void setStrAssignEnd(String strAssignEnd) {
		setAssignEnd(DateTimeFormatUtils.getDateFromString(strAssignEnd));
	}

	public void setAssignEnd(Integer duration) {
		if (duration != null && duration > 0) {
			Date startPlusDuration = DateTimeFormatUtils.getFuture(getAssignStart(), duration);
			Date end = DateTimeFormatUtils.getDateFromDate(getAssignEnd());
			
			// start + duration > end
			if (startPlusDuration.after(end)) {
				setAssignEnd(startPlusDuration);
			}
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public void setRoomDemographicFromBedDemographic(BedDemographic bedDemographic) {
	    
		if(bedDemographic == null ){
			return;
		}
		setRoomId(bedDemographic.getRoomId());
		setDemographicNo(bedDemographic.getId().getDemographicNo());
		setProviderNo(bedDemographic.getProviderNo());
		setAssignStart(bedDemographic.getReservationStart());
		setAssignEnd(bedDemographic.getReservationEnd());
		setComment(null);
		
		return; 
    }

  
	
}
