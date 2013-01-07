/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Room implements Serializable {

    private static final String DEFAULT_NAME = "";
    private static final boolean DEFAULT_ACTIVE = true;
    private static final Integer DEFAULT_ASSIGNED_BED = new Integer(1);
    private static final Integer DEFAULT_OCCUPANCY = new Integer(1);

    public static String REF = "Room";

    private int hashCode = Integer.MIN_VALUE;// primary key

    private Integer id;// fields
    private Integer roomTypeId;
    private Integer programId;
    private String name;
    private String floor;
    private boolean active;
    private Integer facilityId;
    private Integer assignedBed;
    private Integer occupancy;

    private RoomType roomType;
    private Program program;

    // constructors
    public Room() {
        initialize();
    }

    /**
     * Constructor for primary key
     */
    public Room(Integer id) {
        this.setId(id);
        initialize();
    }

    /**
     * Constructor for required fields
     */
    public Room(
            Integer id,
            Integer roomTypeId,
            String name,
            boolean active,
            Integer facilityId) {

        this.setId(id);
        this.setRoomTypeId(roomTypeId);
        this.setName(name);
        this.setActive(active);
        this.setFacilityId(facilityId);
        initialize();
    }

    public static final Room create(Integer facilityId, RoomType roomType) {
        Room room = new Room();

        room.setFacilityId(facilityId);
        room.setRoomTypeId(roomType.getId());
        room.setName(DEFAULT_NAME);
        room.setActive(DEFAULT_ACTIVE);
        room.setAssignedBed(DEFAULT_ASSIGNED_BED);
        room.setOccupancy(DEFAULT_OCCUPANCY);

        return room;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public String getRoomTypeName() {
        return roomType.getName();
    }

    public void setRoomTypeName(String roomTypeName) {
        roomType.setName(roomTypeName);
    }

    public String getProgramName() {
        return program != null ? program.getName() : null;
    }

    public void setProgramName(String programName) {
        if (program != null) {
            program.setName(programName);
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    protected void initialize() {
    }

    /**
     * Return the unique identifier of this class
     *
     *  generator-class="native"
     * column="room_id"
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the unique identifier of this class
     *
     * @param id the new ID
     */
    public void setId(Integer id) {
        this.id = id;
        this.hashCode = Integer.MIN_VALUE;
    }

    /**
     * Return the value associated with the column: room_type_id
     */
    public Integer getRoomTypeId() {
        return roomTypeId;
    }

    /**
     * Set the value related to the column: room_type_id
     *
     * @param roomTypeId the room_type_id value
     */
    public void setRoomTypeId(Integer roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    /**
     * Return the value associated with the column: program_id
     */
    public Integer getProgramId() {
        return programId;
    }

    /**
     * Set the value related to the column: program_id
     *
     * @param programId the program_id value
     */
    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    /**
     * Return the value associated with the column: name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value related to the column: name
     *
     * @param name the name value
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the value associated with the column: floor
     */
    public String getFloor() {
        return floor;
    }

    /**
     * Set the value related to the column: floor
     *
     * @param floor the floor value
     */
    public void setFloor(String floor) {
        this.floor = floor;
    }

    /**
     * Return the value associated with the column: active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Set the value related to the column: active
     *
     * @param active the active value
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Integer facilityId) {
        this.facilityId = facilityId;
    }

    public boolean equals(Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof Room)) return false;
        else {
            Room room = (Room) obj;
            if (null == this.getId() || null == room.getId()) return false;
            else return (this.getId().equals(room.getId()));
        }
    }

    public int hashCode() {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getId()) return super.hashCode();
            else {
                String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    /**
     * Return the value associated with the column: assigned_bed
     */
	public Integer getAssignedBed() {
		return assignedBed;
	}
    /**
     * Set the value related to the column: assigned_bed
     *
     * @param assignedBed the assignedBed value
     */
	public void setAssignedBed(Integer assignedBed) {
		this.assignedBed = assignedBed;
	}
    /**
     * Return the value associated with the column: occupancy
     */
	public Integer getOccupancy() {
		return occupancy;
	}
    /**
     * Set the value related to the column: occupancy
     *
     * @param occupancy the occupancy value
     */
	public void setOccupancy(Integer occupancy) {
		this.occupancy = occupancy;
	}
}
