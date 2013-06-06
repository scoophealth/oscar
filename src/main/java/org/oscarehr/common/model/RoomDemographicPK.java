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
public class RoomDemographicPK implements Serializable {

	@Column(name="demographic_no")
    private Integer demographicNo;
	
	@Column(name="room_id")
	private Integer roomId;

	public RoomDemographicPK() {
		//required by JPA
	}
   
    public RoomDemographicPK(Integer demographicNo, Integer roomId) {
	   	this.demographicNo = demographicNo;
	   	this.roomId = roomId;   
    }

	public Integer getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}
	
	public Integer getRoomId() {
		return roomId;
	}
	
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	@Override
	public String toString() {
		return ("demographicNo=" + demographicNo + ", roomId=" + roomId);
	}

	@Override
	public int hashCode() {
		return (toString().hashCode());
	}

	@Override
	public boolean equals(Object o) {
		try {
			RoomDemographicPK o1 = (RoomDemographicPK) o;
			return ((demographicNo == o1.getDemographicNo()) && (roomId == o1.getRoomId()));
		} catch (RuntimeException e) {
			return (false);
		}
	}
	
	
   
}
