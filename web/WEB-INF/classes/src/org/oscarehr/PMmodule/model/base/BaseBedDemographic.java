/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.PMmodule.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bed_demographic table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bed_demographic"
 */

public abstract class BaseBedDemographic  implements Serializable {

	public static String REF = "BedDemographic";
	public static String PROP_PROVIDER_NO = "providerNo";
	public static String PROP_RESERVATION_START = "reservationStart";
	public static String PROP_RESERVATION_END = "reservationEnd";
	public static String PROP_BED_DEMOGRAPHIC_STATUS_ID = "bedDemographicStatusId";
	public static String PROP_ID = "id";
	public static String PROP_LATE_PASS = "latePass";


	// constructors
	public BaseBedDemographic () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBedDemographic (org.oscarehr.PMmodule.model.BedDemographicPK id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBedDemographic (
		org.oscarehr.PMmodule.model.BedDemographicPK id,
		java.lang.Integer bedDemographicStatusId,
		java.lang.String providerNo,
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

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private org.oscarehr.PMmodule.model.BedDemographicPK id;

	// fields
	private java.lang.Integer bedDemographicStatusId;
	private java.lang.String providerNo;
	private boolean latePass;
	private java.util.Date reservationStart;
	private java.util.Date reservationEnd;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     */
	public org.oscarehr.PMmodule.model.BedDemographicPK getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (org.oscarehr.PMmodule.model.BedDemographicPK id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: bed_demographic_status_id
	 */
	public java.lang.Integer getBedDemographicStatusId () {
		return bedDemographicStatusId;
	}

	/**
	 * Set the value related to the column: bed_demographic_status_id
	 * @param bedDemographicStatusId the bed_demographic_status_id value
	 */
	public void setBedDemographicStatusId (java.lang.Integer bedDemographicStatusId) {
		this.bedDemographicStatusId = bedDemographicStatusId;
	}



	/**
	 * Return the value associated with the column: provider_no
	 */
	public java.lang.String getProviderNo () {
		return providerNo;
	}

	/**
	 * Set the value related to the column: provider_no
	 * @param providerNo the provider_no value
	 */
	public void setProviderNo (java.lang.String providerNo) {
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
	public java.util.Date getReservationStart () {
		return reservationStart;
	}

	/**
	 * Set the value related to the column: reservation_start
	 * @param reservationStart the reservation_start value
	 */
	public void setReservationStart (java.util.Date reservationStart) {
		this.reservationStart = reservationStart;
	}



	/**
	 * Return the value associated with the column: reservation_end
	 */
	public java.util.Date getReservationEnd () {
		return reservationEnd;
	}

	/**
	 * Set the value related to the column: reservation_end
	 * @param reservationEnd the reservation_end value
	 */
	public void setReservationEnd (java.util.Date reservationEnd) {
		this.reservationEnd = reservationEnd;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.BedDemographic)) return false;
		else {
			org.oscarehr.PMmodule.model.BedDemographic bedDemographic = (org.oscarehr.PMmodule.model.BedDemographic) obj;
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


	public String toString () {
		return super.toString();
	}


}