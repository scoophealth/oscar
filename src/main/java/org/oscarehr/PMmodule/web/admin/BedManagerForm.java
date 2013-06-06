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

package org.oscarehr.PMmodule.web.admin;

import java.util.Map;

import org.apache.struts.action.ActionForm;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.model.Bed;
import org.oscarehr.common.model.BedType;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.Room;
import org.oscarehr.common.model.RoomType;

/**
 */
public class BedManagerForm extends ActionForm {

    private Integer facilityId;
    private Facility facility;
    private Integer numRooms;
    private Integer numBeds;
    private Room[] rooms;
    private Room[] assignedBedRooms;
    private RoomType[] roomTypes;
    private Bed[] beds;
    private BedType[] bedTypes;
    private Program[] programs;
    private Integer roomToDelete;
    private Integer bedToDelete;
    private Integer roomStatusFilter;
    private Integer bedStatusFilter;
    private Integer bedProgramFilterForRoom;
    private Integer bedRoomFilterForBed;
    private Map roomStatusNames;
    private Map bedStatusNames;
   

    public Integer getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Integer facilityId) {
        this.facilityId = facilityId;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public Integer getNumRooms() {
        return numRooms;
    }

    public void setNumRooms(Integer numRooms) {
        this.numRooms = numRooms;
    }

    public Integer getNumBeds() {
        return numBeds;
    }

    public void setNumBeds(Integer numBeds) {
        this.numBeds = numBeds;
    }

    public Room[] getRooms() {
        return rooms;
    }

    public void setRooms(Room[] rooms) {
        this.rooms = rooms;
    }

    public RoomType[] getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(RoomType[] roomTypes) {
        this.roomTypes = roomTypes;
    }

    public Bed[] getBeds() {
        return beds;
    }

    public void setBeds(Bed[] beds) {
        this.beds = beds;
    }

    public BedType[] getBedTypes() {
        return bedTypes;
    }

    public void setBedTypes(BedType[] bedTypes) {
        this.bedTypes = bedTypes;
    }

    public Program[] getPrograms() {
        return programs;
    }

    public void setPrograms(Program[] programs) {
        this.programs = programs;
    }

	public Room[] getAssignedBedRooms() {
		return assignedBedRooms;
	}

	public void setAssignedBedRooms(Room[] assignedBedRooms) {
		this.assignedBedRooms = assignedBedRooms;
	}

	public Integer getBedToDelete() {
		return bedToDelete;
	}

	public void setBedToDelete(Integer bedToDelete) {
		this.bedToDelete = bedToDelete;
	}

	public Integer getRoomToDelete() {
		return roomToDelete;
	}

	public void setRoomToDelete(Integer roomToDelete) {
		this.roomToDelete = roomToDelete;
	}

	public Integer getBedRoomFilterForBed() {
		return bedRoomFilterForBed;
	}

	public void setBedRoomFilterForBed(Integer bedRoomFilterForBed) {
		this.bedRoomFilterForBed = bedRoomFilterForBed;
	}

	public Integer getBedProgramFilterForRoom() {
		return bedProgramFilterForRoom;
	}

	public void setBedProgramFilterForRoom(Integer bedProgramFilterForRoom) {
		this.bedProgramFilterForRoom = bedProgramFilterForRoom;
	}

	public Integer getBedStatusFilter() {
		return bedStatusFilter;
	}

	public void setBedStatusFilter(Integer bedStatusFilter) {
		this.bedStatusFilter = bedStatusFilter;
	}

	public Integer getRoomStatusFilter() {
		return roomStatusFilter;
	}

	public void setRoomStatusFilter(Integer roomStatusFilter) {
		this.roomStatusFilter = roomStatusFilter;
	}

	public Map getBedStatusNames() {
		return bedStatusNames;
	}

	public void setBedStatusNames(Map bedStatusNames) {
		this.bedStatusNames = bedStatusNames;
	}

	public Map getRoomStatusNames() {
		return roomStatusNames;
	}

	public void setRoomStatusNames(Map roomStatusNames) {
		this.roomStatusNames = roomStatusNames;
	}

}
