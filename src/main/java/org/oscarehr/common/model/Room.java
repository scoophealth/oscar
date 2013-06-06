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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.oscarehr.PMmodule.model.Program;

@Entity
@Table(name="room")
public class Room extends AbstractModel<Integer> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="room_id")
	private Integer id;
	
	@Column(name="room_type_id")
	private int roomTypeId = 1;
	
	@Column(name="program_id")
	private Integer programId;
	
	@Column(length=45)
	private String name;
	
	@Column(length=45)
	private String floor;
	
	private boolean active = true;
	
	@Column(name="facility_id")
	private Integer facilityId;
	
	@Column(name="assigned_bed")
	private Integer assignedBed = 1;
	
	private Integer occupancy = 0;

	
	//ported over.
	@Transient private RoomType roomType;
	@Transient private Program program;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(int roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Integer getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}

	public Integer getAssignedBed() {
		return assignedBed;
	}

	public void setAssignedBed(Integer assignedBed) {
		this.assignedBed = assignedBed;
	}

	public Integer getOccupancy() {
		return occupancy;
	}

	public void setOccupancy(Integer occupancy) {
		this.occupancy = occupancy;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}
	
	
    public static final Room create(Integer facilityId, RoomType roomType) {
        Room room = new Room();
        
        room.setFacilityId(facilityId);
        room.setRoomTypeId(roomType.getId());
        room.setName("");
        room.setActive(true);
        room.setAssignedBed(1);
        room.setOccupancy(1);

        return room;
    }
	
   
    public String getProgramName() {
        return program != null ? program.getName() : null;
    }

    public void setProgramName(String programName) {
        if (program != null) {
            program.setName(programName);
        }
    }

}
