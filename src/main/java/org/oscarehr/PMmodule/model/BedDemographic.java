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
 * BedDemographic
 */
public class BedDemographic implements Serializable {

	private static String INFINITE_DATE = "2999-12-31";
    public static String REF = "BedDemographic";

    private int hashCode = Integer.MIN_VALUE;// primary key

    private BedDemographicPK id;// fields
    private Integer roomId;
    private Integer bedDemographicStatusId;
    private String providerNo;
    private boolean latePass;
    private Date reservationStart;
    private Date reservationEnd;
    private String infiniteDate = INFINITE_DATE;
    private BedDemographicStatus bedDemographicStatus;
    private Provider provider;
    private Bed bed;
    private Demographic demographic;

    public static BedDemographic create(Integer demographicNo, BedDemographicStatus bedDemographicStatus, String providerNo) {
		BedDemographicPK id = new BedDemographicPK();
		id.setDemographicNo(demographicNo);

		BedDemographic bedDemographic = new BedDemographic();
		bedDemographic.setId(id);
		bedDemographic.setBedDemographicStatusId(bedDemographicStatus.getId());
		bedDemographic.setProviderNo(providerNo);

		// set reservation start to today and reservation end to today + duration
		Date today = new Date();
		
		bedDemographic.setReservationStart(today);
		//bedDemographic.setReservationEnd(DateTimeFormatUtils.getFuture(today, bedDemographicStatus.getDuration()));
		bedDemographic.setReservationEnd(DateTimeFormatUtils.getDateFromString(INFINITE_DATE));

		return bedDemographic;
	}


    // constructors
	public BedDemographic () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BedDemographic (org.oscarehr.PMmodule.model.BedDemographicPK id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BedDemographic (
		org.oscarehr.PMmodule.model.BedDemographicPK id,
		Integer bedDemographicStatusId,
		String providerNo,
		boolean latePass,
		java.util.Date reservationStart,
		java.util.Date reservationEnd) {

		this.setId(id);
		this.setBedDemographicStatusId(bedDemographicStatusId);
		this.setProviderNo(providerNo);
		this.setLatePass(latePass);
		this.setReservationStart(reservationStart);
		this.setReservationEnd(reservationEnd);
		initialize();
	}

	public void setBedDemographicStatus(BedDemographicStatus bedDemographicStatus) {
	    this.bedDemographicStatus = bedDemographicStatus;
    }
	
	public void setProvider(Provider provider) {
	    this.provider = provider;
    }
	
	public void setBed(Bed bed) {
	    this.bed = bed;
    }
	
	public void setDemographic(Demographic demographic) {
	    this.demographic = demographic;
    }
	
	public boolean isExpired() {
		Date end = DateTimeFormatUtils.getDateFromDate(getReservationEnd());
		Date today = new Date();
		
		return end.before(today);
	}
	
	public boolean isValidReservation() {
		Date start = DateTimeFormatUtils.getDateFromDate(getReservationStart());
		Date end = DateTimeFormatUtils.getDateFromDate(getReservationEnd());
		Date today = new Date();

		return start.before(end) && today.before(end);
	}

	public String getStatusName() {
		return bedDemographicStatus.getName();
	}

	public void setStatusName(String statusName) {
		// immutable
	}

	public String getProviderName() {
		return provider.getFormattedName();
	}

	public void setProviderName(String providerName) {
		// immutable
	}

	public String getBedName() {
		return bed != null ? bed.getName() : null;
	}

	public void setBedName(String bedName) {
		// immutable
	}

	public String getDemographicName() {
		return demographic != null ? demographic.getFormattedName() : null;
	}

	public void setDemographicName(String demographicName) {
		// immutable
	}

	public String getRoomName() {
		return bed != null ? bed.getRoomName() : null;
	}

	public void setRoomName(String roomName) {
		// immutable
	}

	public Integer getRoomId() {
		return roomId;
	}
	
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	
	public String getProgramName() {
		return bed != null ? bed.getProgramName() : null;
	}

	public void setProgramName(String programName) {
		// immutable
	}

	// property adapted for view
	public Integer getBedId() {
		return getId().getBedId();
	}

	// property adapted for view
	public void setBedId(Integer bedId) {
		getId().setBedId(bedId);
	}

	// property adapted for view
	public String getStrReservationEnd() {
		return DateTimeFormatUtils.getStringFromDate(getReservationEnd());
	}

	// property adapted for view
	public void setStrReservationEnd(String strReservationEnd) {
		setReservationEnd(DateTimeFormatUtils.getDateFromString(strReservationEnd));
	}

	public void setReservationEnd(Integer duration) {
		if (duration != null && duration > 0) {
			Date startPlusDuration = DateTimeFormatUtils.getFuture(getReservationStart(), duration);
			Date end = DateTimeFormatUtils.getDateFromDate(getReservationEnd());
			
			// start + duration > end
			if (startPlusDuration.after(end)) {
				setReservationEnd(startPlusDuration);
			}
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

    protected void initialize () {
    	//autogenerated code
    }

    /**
	 * Return the unique identifier of this class
* 
*/
    public BedDemographicPK getId () {
        return id;
    }

    /**
	 * Set the unique identifier of this class
     * @param id the new ID
     */
    public void setId (BedDemographicPK id) {
        this.id = id;
        this.hashCode = Integer.MIN_VALUE;
    }

    /**
	 * Return the value associated with the column: bed_demographic_status_id
     */
    public Integer getBedDemographicStatusId () {
        return bedDemographicStatusId;
    }

    /**
	 * Set the value related to the column: bed_demographic_status_id
     * @param bedDemographicStatusId the bed_demographic_status_id value
     */
    public void setBedDemographicStatusId (Integer bedDemographicStatusId) {
        this.bedDemographicStatusId = bedDemographicStatusId;
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
	 * Return the value associated with the column: late_pass
     */
    public boolean isLatePass () {
        return latePass;
    }

    /**
	 * Set the value related to the column: late_pass
     * @param latePass the late_pass value
     */
    public void setLatePass (boolean latePass) {
        this.latePass = latePass;
    }

    /**
	 * Return the value associated with the column: reservation_start
     */
    public Date getReservationStart () {
        return reservationStart;
    }

    /**
	 * Set the value related to the column: reservation_start
     * @param reservationStart the reservation_start value
     */
    public void setReservationStart (Date reservationStart) {
        this.reservationStart = reservationStart;
    }

    /**
	 * Return the value associated with the column: reservation_end
     */
    public Date getReservationEnd () {
        return reservationEnd;
    }

    /**
	 * Set the value related to the column: reservation_end
     * @param reservationEnd the reservation_end value
     */
    public void setReservationEnd (Date reservationEnd) {
        this.reservationEnd = reservationEnd;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof BedDemographic)) return false;
        else {
            BedDemographic bedDemographic = (BedDemographic) obj;
            if (null == this.getId() || null == bedDemographic.getId()) return false;
            else return (this.getId().equals(bedDemographic.getId()));
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

	public String getInfiniteDate() {
		return infiniteDate;
	}


	public void setInfiniteDate(String infiniteDate) {
		this.infiniteDate = infiniteDate;
	}
    
}
