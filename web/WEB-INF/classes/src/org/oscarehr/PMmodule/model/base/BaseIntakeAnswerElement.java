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
 * This is an object that contains data related to the intake_answer_element table. Do not modify this class because it will be overwritten if the configuration file related to this class is modified.
 * 
 * @hibernate.class table="intake_answer_element"
 */

public abstract class BaseIntakeAnswerElement implements Serializable {

	public static String REF = "IntakeAnswerElement";

	public static String PROP_VALIDATION = "validation";

	public static String PROP_NODE_TEMPLATE = "nodeTemplate";

	public static String PROP_ELEMENT = "element";

	public static String PROP_DEFAULT = "default";

	public static String PROP_ID = "id";

	// constructors
	public BaseIntakeAnswerElement() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseIntakeAnswerElement(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseIntakeAnswerElement(java.lang.Integer id, org.oscarehr.PMmodule.model.IntakeNodeTemplate nodeTemplate, java.lang.String element) {

		this.setId(id);
		this.setNodeTemplate(nodeTemplate);
		this.setElement(element);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private boolean m_default;

	private java.lang.String element;

	// many to one
	private org.oscarehr.PMmodule.model.IntakeNodeTemplate nodeTemplate;

	private org.oscarehr.PMmodule.model.IntakeAnswerValidation validation;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="native" column="intake_answer_element_id"
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
	 * Return the value associated with the column: dflt
	 */
	public boolean isDefault() {
		return m_default;
	}

	/**
	 * Set the value related to the column: dflt
	 * 
	 * @param m_default
	 *            the dflt value
	 */
	public void setDefault(boolean m_default) {
		this.m_default = m_default;
	}

	/**
	 * Return the value associated with the column: element
	 */
	public java.lang.String getElement() {
		return element;
	}

	/**
	 * Set the value related to the column: element
	 * 
	 * @param element
	 *            the element value
	 */
	public void setElement(java.lang.String element) {
		this.element = element;
	}

	/**
	 * Return the value associated with the column: intake_node_template_id
	 */
	public org.oscarehr.PMmodule.model.IntakeNodeTemplate getNodeTemplate() {
		return nodeTemplate;
	}

	/**
	 * Set the value related to the column: intake_node_template_id
	 * 
	 * @param nodeTemplate
	 *            the intake_node_template_id value
	 */
	public void setNodeTemplate(org.oscarehr.PMmodule.model.IntakeNodeTemplate nodeTemplate) {
		this.nodeTemplate = nodeTemplate;
	}

	/**
	 * Return the value associated with the column: intake_answer_validation_id
	 */
	public org.oscarehr.PMmodule.model.IntakeAnswerValidation getValidation() {
		return validation;
	}

	/**
	 * Set the value related to the column: intake_answer_validation_id
	 * 
	 * @param validation
	 *            the intake_answer_validation_id value
	 */
	public void setValidation(org.oscarehr.PMmodule.model.IntakeAnswerValidation validation) {
		this.validation = validation;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.IntakeAnswerElement))
			return false;
		else {
			org.oscarehr.PMmodule.model.IntakeAnswerElement intakeAnswerElement = (org.oscarehr.PMmodule.model.IntakeAnswerElement) obj;
			if (null == this.getId() || null == intakeAnswerElement.getId())
				return false;
			else
				return (this.getId().equals(intakeAnswerElement.getId()));
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