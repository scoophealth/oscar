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

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.oscarehr.PMmodule.model.ProgramTeam;

@Entity
@Table(name="bed")
public class Bed  extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="bed_id")
	private Integer id;
	
	@Column(name="bed_type_id")
	private Integer bedTypeId = 1;
	
	@Column(name="room_id")
	private Integer roomId;
	
	@Column(name="facility_id")
	private Integer facilityId;
	
	@Column(name="room_start")
	@Temporal(TemporalType.DATE)
	private Date roomStart;
	
	@Column(name="team_id")
	private Integer teamId;
	
	@Column(length=45)
	private String name;
	
	private boolean active = true;

	//ported from old classes
	@Transient private BedType bedType;
	@Transient private Room room;
	@Transient private ProgramTeam team;
	@Transient private BedDemographic bedDemographic;
	@Transient private Integer communityProgramId;
	@Transient private Integer familyId;
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBedTypeId() {
		return bedTypeId;
	}

	public void setBedTypeId(Integer bedTypeId) {
		this.bedTypeId = bedTypeId;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}

	public Date getRoomStart() {
		return roomStart;
	}

	public void setRoomStart(Date roomStart) {
		this.roomStart = roomStart;
	}

	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
    public static Bed create(Integer facilityId, Integer roomId, BedType bedType) {
        Bed bed = new Bed();
        bed.setBedTypeId(bedType.getId());
        bed.setRoomId(roomId);
        bed.setRoomStart(Calendar.getInstance().getTime());
        bed.setName("");
        bed.setActive(true);
        bed.setFacilityId(facilityId);
        return bed;
    }

	public BedType getBedType() {
		return bedType;
	}

	public void setBedType(BedType bedType) {
		this.bedType = bedType;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public ProgramTeam getTeam() {
		return team;
	}
	
	public void setTeam(ProgramTeam team) {
		this.team = team;
	}
	

    public BedDemographic getBedDemographic() {
		return bedDemographic;
	}

	public void setBedDemographic(BedDemographic bedDemographic) {
		this.bedDemographic = bedDemographic;
	}

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

    public String getDemographicName() {
        return bedDemographic != null ? bedDemographic.getDemographicName() : null;
    }

	public Integer getFamilyId() {
		return familyId;
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

	public void setFamilyId(Integer familyId) {
		this.familyId = familyId;
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


	
	
}
