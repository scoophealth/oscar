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
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;

/**
 * RoomDemographic
 */
public class RoomDemographic implements Serializable {

    public static String REF = "RoomDemographic";

    private int hashCode = Integer.MIN_VALUE;// primary key

    private RoomDemographicPK id;// fields
    private Integer demographicNo;
    private String providerNo;
    private Date assignStart;
    private Date assignEnd;
    private String comment;
    private Provider provider;
    private Room room;
    private Demographic demographic;

    public static RoomDemographic create(Integer demographicNo, String providerNo) {
		RoomDemographicPK id = new RoomDemographicPK();
		id.setDemographicNo(demographicNo);

		RoomDemographic roomDemographic = new RoomDemographic();
		roomDemographic.setId(id);
		roomDemographic.setProviderNo(providerNo);

		// set assign start to today and assign end to today + duration
		Date today = new Date();
		
		roomDemographic.setAssignStart(today);
//		roomDemographic.setAssignEnd(today);
		return roomDemographic;
	}

    // constructors
	public RoomDemographic () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public RoomDemographic (org.oscarehr.PMmodule.model.RoomDemographicPK id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public RoomDemographic (
		org.oscarehr.PMmodule.model.RoomDemographicPK id,
		String providerNo,
		java.util.Date assignStart,
		java.util.Date assignEnd,
		String comment) {
		
		this.setId(id);
		this.setProviderNo(providerNo);
		this.setAssignStart(assignStart);
		this.setAssignEnd(assignEnd);
		this.setComment(comment);
		initialize();
	}

	public void setRoomDemographicFromBedDemographic(BedDemographic bedDemographic) {
	    
		if(bedDemographic == null ){
			return;
		}
		setRoomId(bedDemographic.getRoomId());
		setDemographicNo(bedDemographic.getId().getDemographicNo());
		setProviderNo(bedDemographic.getProviderNo());
		setAssignStart(bedDemographic.getReservationStart());
		setAssignEnd(bedDemographic.getReservationEnd());
		setComment(null);
		
		return; 
    }
	
	public void setDemographicNo(Integer demographicNo){
		this.demographicNo = demographicNo;
	}
	public void setProvider(Provider provider) {
	    this.provider = provider;
    }
	
	public void setRoom(Room room) {
	    this.room = room;
    }
	
	public void setDemographic(Demographic demographic) {
	    this.demographic = demographic;
    }
	
	public boolean isExpired() {
		Date end = DateTimeFormatUtils.getDateFromDate(getAssignEnd());
		Date today = new Date();
		
		return end.before(today);
	}
	
	public boolean isValidAssign() {
		Date start = DateTimeFormatUtils.getDateFromDate(getAssignStart());
		Date end = DateTimeFormatUtils.getDateFromDate(getAssignEnd());
		Date today = new Date();

		return start.before(end) && today.before(end);
	}

	public String getProviderName() {
		return provider.getFormattedName();
	}

	public void setProviderName(String providerName) {
		// immutable
	}

	public Room getRoom() {
		return room;
	}

	public String getRoomName() {
		return room != null ? room.getName() : null;
	}

	public void setRoomName(String roomName) {
		// immutable
	}

	public String getDemographicName() {
		return demographic != null ? demographic.getFormattedName() : null;
	}

	public void setDemographicName(String demographicName) {
		// immutable
	}

	public String getProgramName() {
		return room != null ? room.getProgramName() : null;
	}

	public void setProgramName(String programName) {
		// immutable
	}

	// property adapted for view
	public Integer getRoomId() {
		return getId().getRoomId();
	}

	// property adapted for view
	public void setRoomId(Integer roomId) {
		getId().setRoomId(roomId);
	}

	// property adapted for view
	public String getStrAssignEnd() {
		return DateTimeFormatUtils.getStringFromDate(getAssignEnd());
	}

	// property adapted for view
	public void setStrAssignEnd(String strAssignEnd) {
		setAssignEnd(DateTimeFormatUtils.getDateFromString(strAssignEnd));
	}

	public void setAssignEnd(Integer duration) {
		if (duration != null && duration > 0) {
			Date startPlusDuration = DateTimeFormatUtils.getFuture(getAssignStart(), duration);
			Date end = DateTimeFormatUtils.getDateFromDate(getAssignEnd());
			
			// start + duration > end
			if (startPlusDuration.after(end)) {
				setAssignEnd(startPlusDuration);
			}
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

    protected void initialize () {}

    /**
	 * Return the unique identifier of this class
* 
*/
    public RoomDemographicPK getId () {
        return id;
    }

    /**
	 * Set the unique identifier of this class
     * @param id the new ID
     */
    public void setId (RoomDemographicPK id) {
        this.id = id;
        this.hashCode = Integer.MIN_VALUE;
    }

    /**
	 * Return the value associated with the column: provider_no
     */
    public String getProviderNo () {
        return providerNo;
    }

    /**
	 * Set the value related to the column: provider_no
     * @param providerNo the provider_no value
     */
    public void setProviderNo (String providerNo) {
        this.providerNo = providerNo;
    }

    /**
	 * Return the value associated with the column: assign_start
     */
    public Date getAssignStart () {
        return assignStart;
    }

    /**
	 * Set the value related to the column: assign_start
     * @param assignStart the assign_start value
     */
    public void setAssignStart (Date assignStart) {
        this.assignStart = assignStart;
    }

    /**
	 * Return the value associated with the column: assign_end
     */
    public Date getAssignEnd () {
        return assignEnd;
    }

    /**
	 * Set the value related to the column: assign_end
     * @param assignEnd the assign_end value
     */
    public void setAssignEnd (Date assignEnd) {
        this.assignEnd = assignEnd;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof RoomDemographic)) return false;
        else {
            RoomDemographic roomDemographic = (RoomDemographic) obj;
            if (null == this.getId() || null == roomDemographic.getId()) return false;
            else return (this.getId().equals(roomDemographic.getId()));
        }
    }

    public int hashCode () {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getId()) return super.hashCode();
            else {
                String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}
}
