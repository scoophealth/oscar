package org.oscarehr.PMmodule.model;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.oscarehr.PMmodule.model.base.BaseBed;

public class Bed extends BaseBed {

	private static final long serialVersionUID = 1L;

	private static final Integer DEFAULT_ROOM_ID = 1;

	private static final String DEFAULT_NAME = "new bed";

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

	private String bedTypeName;

	private String roomName;

	private String teamName;
	
	private String demographicName;
	
	private String statusName;
	
	private boolean latePass;
	
	private Date reservationStart;

	private Date reservationEnd;

	public String getBedTypeName() {
		return bedTypeName;
	}

	public void setBedTypeName(String bedTypeName) {
		this.bedTypeName = bedTypeName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	public String getDemographicName() {
	    return demographicName;
    }
	
	public void setDemographicName(String clientName) {
	    this.demographicName = clientName;
    }
	
	public String getStatusName() {
	    return statusName;
    }
	
	public void setStatusName(String statusName) {
	    this.statusName = statusName;
    }
	
	public boolean isLatePass() {
	    return latePass;
    }
	
	public void setLatePass(boolean latePass) {
	    this.latePass = latePass;
    }
	
	public Date getReservationStart() {
	    return reservationStart;
    }
	
	public void setReservationStart(Date reservationStart) {
	    this.reservationStart = reservationStart;
    }

	public Date getReservationEnd() {
		return reservationEnd;
	}

	public void setReservationEnd(Date reservationEnd) {
		this.reservationEnd = reservationEnd;
	}

	public boolean isReserved() {
		return reservationStart != null && reservationEnd != null;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}