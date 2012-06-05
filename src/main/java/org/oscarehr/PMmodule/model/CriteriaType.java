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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.oscarehr.common.model.AbstractModel;

/**
 * CriteriaType entity. @author azhou
 */
@Entity
@Table(name = "criteria_type")
public class CriteriaType extends AbstractModel<Integer> implements java.io.Serializable {

	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CRITERIA_TYPE_ID", unique = true, nullable = false)
	private Integer id;
	@Column(name = "FIELD_NAME", nullable = false, length = 128)
	private String fieldName;
	@Column(name = "FIELD_TYPE", nullable = false, length = 128)
	private String fieldType;
	@Column(name = "DEFAULT_VALUE")
	private String defaultValue;
	@Column(name = "ACTIVE", nullable = false)
	private Boolean active;
	@Column(name = "WL_PROGRAM_ID")
	private Integer wlProgramId;
	@Column(name = "CAN_BE_ADHOC", nullable = false)
	private Boolean canBeAdhoc;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}


	/** default constructor */
	public CriteriaType() {
	}

	/** minimal constructor */
	public CriteriaType(String fieldName, String fieldType, Boolean active,
			Boolean canBeAdhoc) {
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.active = active;
		this.canBeAdhoc = canBeAdhoc;
	}

	/** full constructor */
	public CriteriaType(String fieldName, String fieldType,
			String defaultValue, Boolean active, Integer wlProgramId,
			Boolean canBeAdhoc) {
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.defaultValue = defaultValue;
		this.active = active;
		this.wlProgramId = wlProgramId;
		this.canBeAdhoc = canBeAdhoc;
	}

	/**
     * @return the fieldName
     */
    public String getFieldName() {
    	return fieldName;
    }

	/**
     * @param fieldName the fieldName to set
     */
    public void setFieldName(String fieldName) {
    	this.fieldName = fieldName;
    }

	/**
     * @return the fieldType
     */
    public String getFieldType() {
    	return fieldType;
    }

	/**
     * @param fieldType the fieldType to set
     */
    public void setFieldType(String fieldType) {
    	this.fieldType = fieldType;
    }

	/**
     * @return the defaultValue
     */
    public String getDefaultValue() {
    	return defaultValue;
    }

	/**
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
    	this.defaultValue = defaultValue;
    }

	/**
     * @return the active
     */
    public Boolean getActive() {
    	return active;
    }

	/**
     * @param active the active to set
     */
    public void setActive(Boolean active) {
    	this.active = active;
    }

	/**
     * @return the wlProgramId
     */
    public Integer getWlProgramId() {
    	return wlProgramId;
    }

	/**
     * @param wlProgramId the wlProgramId to set
     */
    public void setWlProgramId(Integer wlProgramId) {
    	this.wlProgramId = wlProgramId;
    }

	/**
     * @return the canBeAdhoc
     */
    public Boolean getCanBeAdhoc() {
    	return canBeAdhoc;
    }

	/**
     * @param canBeAdhoc the canBeAdhoc to set
     */
    public void setCanBeAdhoc(Boolean canBeAdhoc) {
    	this.canBeAdhoc = canBeAdhoc;
    }

}
