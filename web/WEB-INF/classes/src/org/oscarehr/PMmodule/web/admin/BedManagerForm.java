package org.oscarehr.PMmodule.web.admin;

import org.apache.struts.action.ActionForm;
import org.oscarehr.PMmodule.model.*;

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
}
