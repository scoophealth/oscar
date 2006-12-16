package org.oscarehr.PMmodule.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.oscarehr.PMmodule.dao.BedDAO;
import org.oscarehr.PMmodule.dao.ProgramTeamDAO;
import org.oscarehr.PMmodule.dao.RoomDAO;
import org.oscarehr.PMmodule.model.Bed;
import org.oscarehr.PMmodule.model.BedDemographic;
import org.oscarehr.PMmodule.model.BedType;
import org.oscarehr.PMmodule.model.ProgramTeam;
import org.oscarehr.PMmodule.model.Room;
import org.oscarehr.PMmodule.service.BedDemographicManager;
import org.oscarehr.PMmodule.service.BedManager;

public class BedManagerImpl implements BedManager {

	private BedDAO bedDAO;

	private RoomDAO roomDAO;

	private ProgramTeamDAO teamDAO;

	private BedDemographicManager bedDemographicManager;

	public void setBedDAO(BedDAO bedDAO) {
		this.bedDAO = bedDAO;
	}

	public void setRoomDAO(RoomDAO roomDAO) {
		this.roomDAO = roomDAO;
	}

	public void setTeamDAO(ProgramTeamDAO teamDAO) {
		this.teamDAO = teamDAO;
	}

	public void setBedDemographicManager(BedDemographicManager bedDemographicManager) {
		this.bedDemographicManager = bedDemographicManager;
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedManager#getBed(java.lang.Integer)
	 */
	public Bed getBed(Integer bedId) {
		Bed bed = bedDAO.getBed(bedId);
		setAttributes(bed);

		return bed;
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedManager#getBedsByProgram(java.lang.Integer, java.lang.Boolean, java.util.Date)
	 */
	public Bed[] getBedsByProgram(Integer programId, Boolean reserved, Date time) {
		if (programId == null) {
			return new Bed[] {};
		}

		List<Bed> beds = new ArrayList<Bed>();

		for (Room room : roomDAO.getRooms(programId, Boolean.TRUE)) {
			for (Bed bed : bedDAO.getBeds(room.getId(), Boolean.TRUE)) {
				setAttributes(bed);

				if (!filterBed(bed, reserved, time)) {
					beds.add(bed);
				}
			}
		}

		return (Bed[]) beds.toArray(new Bed[beds.size()]);
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedManager#getBeds()
	 */
	public Bed[] getBeds() {
		Bed[] beds = bedDAO.getBeds(null, null);

		for (Bed bed : beds) {
			setAttributes(bed);
		}

		return beds;
	}
	
	public BedType getDefaultBedType() {
		for (BedType bedType : getBedTypes()) {
			if (bedType.isDefault()) {
				return bedType;
			}
		}

		throw new IllegalStateException("no default bed type");
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedManager#getBedTypes()
	 */
	public BedType[] getBedTypes() {
		return bedDAO.getBedTypes();
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedManager#addBed()
	 */
	public void addBed() {
		BedType bedType = getDefaultBedType();
		Bed newBed = Bed.create(bedType);
		validate(newBed);
		bedDAO.saveBed(newBed);
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedManager#saveBeds(java.util.List)
	 */
	public void saveBeds(Bed[] beds) {
		if (beds == null) {
			throw new IllegalArgumentException("array beds is null");
		}

		for (Bed bed : beds) {
			validate(bed);
			bedDAO.saveBed(bed);
		}
	}

	void setAttributes(Bed bed) {
		// bed type is mandatory
		Integer bedTypeId = bed.getBedTypeId();
		BedType bedType = bedDAO.getBedType(bedTypeId);
		String bedTypeName = (bedType != null) ? bedType.getName() : "N/A";
		bed.setBedTypeName(bedTypeName);

		// room is mandatory
		Integer roomId = bed.getRoomId();
		Room room = roomDAO.getRoom(roomId);
		String roomName = (room != null) ? room.getName() : "N/A";
		bed.setRoomName(roomName);

		// team is optional
		Integer teamId = bed.getTeamId();

		if (teamId != null) {
			ProgramTeam team = teamDAO.getProgramTeam(teamId);
			String teamName = (team != null) ? team.getName() : "N/A";
			bed.setTeamName(teamName);
		}

		// demographic is optional
		BedDemographic bedDemographic = bedDemographicManager.getBedDemographicByBed(bed.getId());

		if (bedDemographic != null) {
			bed.setDemographicName(bedDemographic.getDemographicName());
			bed.setStatusName(bedDemographic.getStatusName());
			bed.setLatePass(bedDemographic.isLatePass());
			bed.setReservationStart(bedDemographic.getReservationStart());
			bed.setReservationEnd(bedDemographic.getReservationEnd());
		}
	}

	boolean filterBed(Bed bed, Boolean reserved, Date time) {
		if (reserved == null) {
			return false;
		}

		// TODO IC Bedlog - if time t not null, bed or historical bed exists with start < t and end > t
		return (time != null) ? reserved != bed.isReserved() && true : reserved != bed.isReserved();
	}

	void validate(Bed bed) {
		if (bed == null) {
			throw new IllegalArgumentException("bed is null");
		}

		validateBed(bed.getId(), bed);

		// mandatory
		validateBedType(bed.getBedTypeId());
		validateRoom(bed.getRoomId());

		// optional
		validateTeam(bed.getTeamId());
	}

	void validateBed(Integer bedId, Bed bed) {
		if (bedId != null) {
			if (!bedDAO.bedExists(bedId)) {
				throw new IllegalArgumentException("no bed with id : " + bedId);
			}
			
			if (!bed.isActive() && bedDemographicManager.demographicExists(bed.getId())) {
				throw new IllegalStateException("bed with id : " + bedId + " has a reservation");
			}
		}
	}

	void validateBedType(Integer bedTypeId) {
		if (!bedDAO.bedTypeExists(bedTypeId)) {
			throw new IllegalArgumentException("no bed type with id : " + bedTypeId);
		}
	}

	void validateRoom(Integer roomId) {
		if (!roomDAO.roomExists(roomId)) {
			throw new IllegalArgumentException("no room with id : " + roomId);
		}
	}

	void validateTeam(Integer teamId) {
		if (teamId != null && !teamDAO.teamExists(teamId)) {
			throw new IllegalArgumentException("no team with id : " + teamId);
		}
	}

}