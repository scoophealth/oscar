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