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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class RoomBedHistoricalPK implements Serializable {

	@Column(name="bed_id")
    private Integer bedId;
	
	@Column(name="room_id")
	private Integer roomId;

	@Column(name="contain_start")
	@Temporal(TemporalType.DATE)
	private Date containStart;

	
	public RoomBedHistoricalPK() {
		//required by JPA
	}
   
    public RoomBedHistoricalPK(Integer bedId, Integer roomId, Date containStart) {
	   	this.bedId = bedId;
	   	this.roomId = roomId;   
	   	this.containStart = containStart;
    }

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	public Integer getRoomId() {
		return roomId;
	}
	
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Date getContainStart() {
		return containStart;
	}

	public void setContainStart(Date containStart) {
		this.containStart = containStart;
	}

	@Override
	public String toString() {
		return ("bedId=" + bedId + ", roomId=" + roomId + ",containStart=" + containStart);
	}

	@Override
	public int hashCode() {
		return (toString().hashCode());
	}

	@Override
	public boolean equals(Object o) {
		try {
			RoomBedHistoricalPK o1 = (RoomBedHistoricalPK) o;
			return ((bedId == o1.getBedId()) && (roomId == o1.getRoomId()) && (containStart.equals(o1.getContainStart())));
		} catch (RuntimeException e) {
			return (false);
		}
	}
}
