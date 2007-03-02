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
 * This is an object that contains data related to the intake_answer_validation table. Do not modify this class because it will be overwritten if the configuration file related to this class is modified.
 * 
 * @hibernate.class table="intake_answer_validation"
 */

public abstract class BaseIntakeAnswerValidation implements Serializable {

	public static String REF = "IntakeAnswerValidation";

	public static String PROP_TYPE = "type";

	public static String PROP_ID = "id";

	// constructors
	public BaseIntakeAnswerValidation() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseIntakeAnswerValidation(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseIntakeAnswerValidation(java.lang.Integer id, java.lang.String type) {

		this.setId(id);
		this.setType(type);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String type;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="native" column="intake_answer_validation_id"
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
	 * Return the value associated with the column: type
	 */
	public java.lang.String getType() {
		return type;
	}

	/**
	 * Set the value related to the column: type
	 * 
	 * @param type
	 *            the type value
	 */
	public void setType(java.lang.String type) {
		this.type = type;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.IntakeAnswerValidation))
			return false;
		else {
			org.oscarehr.PMmodule.model.IntakeAnswerValidation intakeAnswerValidation = (org.oscarehr.PMmodule.model.IntakeAnswerValidation) obj;
			if (null == this.getId() || null == intakeAnswerValidation.getId())
				return false;
			else
				return (this.getId().equals(intakeAnswerValidation.getId()));
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