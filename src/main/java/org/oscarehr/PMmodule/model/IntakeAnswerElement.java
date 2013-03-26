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

import org.apache.commons.lang.builder.CompareToBuilder;

public class IntakeAnswerElement  implements Comparable<IntakeAnswerElement>, Serializable {

	public static final String TRUE = "T";
	public static final String FALSE = "F";
    public static String REF = "IntakeAnswerElement";
    private int hashCode = Integer.MIN_VALUE;// primary key
    private Integer id;// fields
    private boolean m_default;
    private String label;
    private String element;// many to one
    private IntakeNodeTemplate nodeTemplate;
    private IntakeAnswerValidation validation;

    // constructors
	public IntakeAnswerElement() {
	}

	/**
	 * Constructor for primary key
	 */
	public IntakeAnswerElement(Integer id) {
		this.setId(id);
	}

	/**
	 * Constructor for required fields
	 */
	public IntakeAnswerElement(Integer id, org.oscarehr.PMmodule.model.IntakeNodeTemplate nodeTemplate, String element) {

		this.setId(id);
		this.setNodeTemplate(nodeTemplate);
		this.setElement(element);
	}


	/* [CONSTRUCTOR MARKER END] */

	public String getValidationStr() {
		return getValidation() != null ? getValidation().getType() : "";
	}
	
	/**
	 * @see Comparable#compareTo(Object)
	 */
	public int compareTo(IntakeAnswerElement answerElement) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(getId(), answerElement.getId());
		compareToBuilder.append(getElement(), answerElement.getElement());

		return compareToBuilder.toComparison();
	}
	
	@Override
	public String toString() {
		return new StringBuilder(REF).append("(").append(getId()).append(", ").append(getElement()).append(", ").append(getValidation()).append(")").toString();
	}

    /**
	 * Return the unique identifier of this class
     *
     *  generator-class="native" column="intake_answer_element_id"
     */
    public Integer getId() {
        return id;
    }

    /**
	 * Set the unique identifier of this class
     *
     * @param id
     *            the new ID
     */
    public void setId(Integer id) {
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
    public String getElement() {
        return element;
    }

    /**
	 * Set the value related to the column: element
     *
     * @param element
     *            the element value
     */
    public void setElement(String element) {
        this.element = element;
    }

    public String getLabel() {
    	return label;
    }
    public void setLabel(String label) {
    	this.label = label;
    }
    /**
	 * Return the value associated with the column: intake_node_template_id
     */
    public IntakeNodeTemplate getNodeTemplate() {
        return nodeTemplate;
    }

    /**
	 * Set the value related to the column: intake_node_template_id
     *
     * @param nodeTemplate
     *            the intake_node_template_id value
     */
    public void setNodeTemplate(IntakeNodeTemplate nodeTemplate) {
        this.nodeTemplate = nodeTemplate;
    }

    /**
	 * Return the value associated with the column: intake_answer_validation_id
     */
    public IntakeAnswerValidation getValidation() {
        return validation;
    }

    /**
	 * Set the value related to the column: intake_answer_validation_id
     *
     * @param validation
     *            the intake_answer_validation_id value
     */
    public void setValidation(IntakeAnswerValidation validation) {
        this.validation = validation;
    }

    public boolean equals(Object obj) {
        if (null == obj)
            return false;
        if (!(obj instanceof IntakeAnswerElement))
            return false;
        else {
            IntakeAnswerElement intakeAnswerElement = (IntakeAnswerElement) obj;
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
}
