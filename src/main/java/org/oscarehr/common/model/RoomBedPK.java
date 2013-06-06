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

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RoomBedPK implements Serializable {

	@Column(name="bed_id")
    private Integer bedId;
	
	@Column(name="room_id")
	private Integer roomId;

	public RoomBedPK() {
		//required by JPA
	}
   
    public RoomBedPK(Integer bedId, Integer roomId) {
	   	this.bedId = bedId;
	   	this.roomId = roomId;   
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

	@Override
	public String toString() {
		return ("bedId=" + bedId + ", roomId=" + roomId);
	}

	@Override
	public int hashCode() {
		return (toString().hashCode());
	}

	@Override
	public boolean equals(Object o) {
		try {
			RoomBedPK o1 = (RoomBedPK) o;
			return ((bedId == o1.getBedId()) && (roomId == o1.getRoomId()));
		} catch (RuntimeException e) {
			return (false);
		}
	}
	
	
   
}