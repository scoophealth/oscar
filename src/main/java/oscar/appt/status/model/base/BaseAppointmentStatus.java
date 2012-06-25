/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.appt.status.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the appointment_status table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="appointment_status"
 */

public abstract class BaseAppointmentStatus  implements Serializable {

	public static String REF = "AppointmentStatus";


	// constructors
	public BaseAppointmentStatus () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseAppointmentStatus (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseAppointmentStatus (
		java.lang.Integer id,
		java.lang.String status,
		java.lang.String description,
		java.lang.String color,
		java.lang.String icon,
		java.lang.Integer active) {

		this.setId(id);
		this.setStatus(status);
		this.setDescription(description);
		this.setColor(color);
		this.setIcon(icon);
		this.setActive(active);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String status;
	private java.lang.String description;
	private java.lang.String color;
	private java.lang.String icon;
	private java.lang.Integer active;
	private java.lang.Integer editable;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="id"
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
	 * Return the value associated with the column: status
	 */
	public java.lang.String getStatus () {
		return status;
	}

	/**
	 * Set the value related to the column: status
	 * @param status the status value
	 */
	public void setStatus (java.lang.String status) {
		this.status = status;
	}



	/**
	 * Return the value associated with the column: description
	 */
	public java.lang.String getDescription () {
		return description;
	}

	/**
	 * Set the value related to the column: description
	 * @param description the description value
	 */
	public void setDescription (java.lang.String description) {
		this.description = description;
	}



	/**
	 * Return the value associated with the column: color
	 */
	public java.lang.String getColor () {
		return color;
	}

	/**
	 * Set the value related to the column: color
	 * @param color the color value
	 */
	public void setColor (java.lang.String color) {
		this.color = color;
	}



	/**
	 * Return the value associated with the column: icon
	 */
	public java.lang.String getIcon () {
		return icon;
	}

	/**
	 * Set the value related to the column: icon
	 * @param icon the icon value
	 */
	public void setIcon (java.lang.String icon) {
		this.icon = icon;
	}



	/**
	 * Return the value associated with the column: active
	 */
	public java.lang.Integer getActive () {
		return active;
	}

	/**
	 * Set the value related to the column: active
	 * @param active the active value
	 */
	public void setActive (java.lang.Integer active) {
		this.active = active;
	}



	/**
	 * Return the value associated with the column: editable
	 */
	public java.lang.Integer getEditable () {
		return editable;
	}

	/**
	 * Set the value related to the column: editable
	 * @param editable the editable value
	 */
	public void setEditable (java.lang.Integer editable) {
		this.editable = editable;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof oscar.appt.status.model.AppointmentStatus)) return false;
		else {
			oscar.appt.status.model.AppointmentStatus appointmentStatus = (oscar.appt.status.model.AppointmentStatus) obj;
			if (null == this.getId() || null == appointmentStatus.getId()) return false;
			else return (this.getId().equals(appointmentStatus.getId()));
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
