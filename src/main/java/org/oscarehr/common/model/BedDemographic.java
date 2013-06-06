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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;

@Entity
@Table(name="bed_demographic")
public class BedDemographic  extends AbstractModel<BedDemographicPK> {

	private static String INFINITE_DATE = "2999-12-31";
    
	@EmbeddedId
	private BedDemographicPK id;

	@Column(name="bed_demographic_status_id")
	private Integer bedDemographicStatusId = 1;
	
	@Column(name="provider_no",length=6)
	private String providerNo;
	
	@Column(name="late_pass")
	private Boolean latePass = false;
	
	@Column(name="reservation_start")
	@Temporal(TemporalType.DATE)
	private Date reservationStart;
	
	@Column(name="reservation_end")
	@Temporal(TemporalType.DATE)
	private Date reservationEnd;
	
	
	@Transient private BedDemographicStatus bedDemographicStatus;
	@Transient private Provider provider;
	@Transient private Bed bed;
	@Transient private Demographic demographic;
	@Transient private Integer roomId;
	@Transient private String infiniteDate = INFINITE_DATE;
	
	public BedDemographic() {
		id = new BedDemographicPK();
	}


	public BedDemographicPK getId() {
		return id;
	}


	public void setId(BedDemographicPK id) {
		this.id = id;
	}


	public Integer getBedDemographicStatusId() {
		return bedDemographicStatusId;
	}


	public void setBedDemographicStatusId(Integer bedDemographicStatusId) {
		this.bedDemographicStatusId = bedDemographicStatusId;
	}


	public String getProviderNo() {
		return providerNo;
	}


	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}


	public Boolean getLatePass() {
		return latePass;
	}


	public void setLatePass(Boolean latePass) {
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


	public BedDemographicStatus getBedDemographicStatus() {
		return bedDemographicStatus;
	}


	public void setBedDemographicStatus(BedDemographicStatus bedDemographicStatus) {
		this.bedDemographicStatus = bedDemographicStatus;
	}


	public Provider getProvider() {
		return provider;
	}


	public void setProvider(Provider provider) {
		this.provider = provider;
	}


	public Bed getBed() {
		return bed;
	}


	public void setBed(Bed bed) {
		this.bed = bed;
	}


	public Demographic getDemographic() {
		return demographic;
	}


	public void setDemographic(Demographic demographic) {
		this.demographic = demographic;
	}
	
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




		public String getInfiniteDate() {
			return infiniteDate;
		}


		public void setInfiniteDate(String infiniteDate) {
			this.infiniteDate = infiniteDate;
		}
	
}
