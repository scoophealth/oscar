/**
 * Copyright (C) 2007.
 * Centre for Research on Inner City Health, St. Michael's Hospital, Toronto, Ontario, Canada.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.oscarehr.PMmodule.model.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the intake_node_label table. Do not modify this class because it will be overwritten if the configuration file related to this class is modified.
 * 
 * @hibernate.class table="intake_node_label"
 */

public abstract class BaseIntakeNodeLabel implements Serializable {

	public static String REF = "IntakeNodeLabel";

	public static String PROP_LABEL = "label";

	public static String PROP_ID = "id";

	// constructors
	public BaseIntakeNodeLabel() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseIntakeNodeLabel(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseIntakeNodeLabel(java.lang.Integer id, java.lang.String label) {

		this.setId(id);
		this.setLabel(label);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String label;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="native" column="intake_node_label_id"
	 */
	public java.lang.Integer getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param id
	 *            the new ID
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: lbl
	 */
	public java.lang.String getLabel() {
		return label;
	}

	/**
	 * Set the value related to the column: lbl
	 * 
	 * @param label
	 *            the lbl value
	 */
	public void setLabel(java.lang.String label) {
		this.label = label;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.IntakeNodeLabel))
			return false;
		else {
			org.oscarehr.PMmodule.model.IntakeNodeLabel intakeNodeLabel = (org.oscarehr.PMmodule.model.IntakeNodeLabel) obj;
			if (null == this.getId() || null == intakeNodeLabel.getId())
				return false;
			else
				return (this.getId().equals(intakeNodeLabel.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

}