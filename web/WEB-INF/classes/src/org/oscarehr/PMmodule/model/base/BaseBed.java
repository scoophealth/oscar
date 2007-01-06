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
 * This is an object that contains data related to the bed table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bed"
 */

public abstract class BaseBed  implements Serializable {

	public static String REF = "Bed";
	public static String PROP_ACTIVE = "active";
	public static String PROP_TEAM_ID = "teamId";
	public static String PROP_BED_TYPE_ID = "bedTypeId";
	public static String PROP_NAME = "name";
	public static String PROP_ROOM_START = "roomStart";
	public static String PROP_ROOM_ID = "roomId";
	public static String PROP_ID = "id";


	// constructors
	public BaseBed () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBed (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBed (
		java.lang.Integer id,
		java.lang.Integer bedTypeId,
		java.lang.Integer roomId,
		java.util.Date roomStart,
		java.lang.String name,
		boolean active) {

		this.setId(id);
		this.setBedTypeId(bedTypeId);
		this.setRoomId(roomId);
		this.setRoomStart(roomStart);
		this.setName(name);
		this.setActive(active);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Integer bedTypeId;
	private java.lang.Integer roomId;
	private java.util.Date roomStart;
	private java.lang.Integer teamId;
	private java.lang.String name;
	private boolean active;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="bed_id"
     */
	public java.lang.Integer getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: bed_type_id
	 */
	public java.lang.Integer getBedTypeId () {
		return bedTypeId;
	}

	/**
	 * Set the value related to the column: bed_type_id
	 * @param bedTypeId the bed_type_id value
	 */
	public void setBedTypeId (java.lang.Integer bedTypeId) {
		this.bedTypeId = bedTypeId;
	}



	/**
	 * Return the value associated with the column: room_id
	 */
	public java.lang.Integer getRoomId () {
		return roomId;
	}

	/**
	 * Set the value related to the column: room_id
	 * @param roomId the room_id value
	 */
	public void setRoomId (java.lang.Integer roomId) {
		this.roomId = roomId;
	}



	/**
	 * Return the value associated with the column: room_start
	 */
	public java.util.Date getRoomStart () {
		return roomStart;
	}

	/**
	 * Set the value related to the column: room_start
	 * @param roomStart the room_start value
	 */
	public void setRoomStart (java.util.Date roomStart) {
		this.roomStart = roomStart;
	}



	/**
	 * Return the value associated with the column: team_id
	 */
	public java.lang.Integer getTeamId () {
		return teamId;
	}

	/**
	 * Set the value related to the column: team_id
	 * @param teamId the team_id value
	 */
	public void setTeamId (java.lang.Integer teamId) {
		this.teamId = teamId;
	}



	/**
	 * Return the value associated with the column: name
	 */
	public java.lang.String getName () {
		return name;
	}

	/**
	 * Set the value related to the column: name
	 * @param name the name value
	 */
	public void setName (java.lang.String name) {
		this.name = name;
	}



	/**
	 * Return the value associated with the column: active
	 */
	public boolean isActive () {
		return active;
	}

	/**
	 * Set the value related to the column: active
	 * @param active the active value
	 */
	public void setActive (boolean active) {
		this.active = active;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.Bed)) return false;
		else {
			org.oscarehr.PMmodule.model.Bed bed = (org.oscarehr.PMmodule.model.Bed) obj;
			if (null == this.getId() || null == bed.getId()) return false;
			else return (this.getId().equals(bed.getId()));
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