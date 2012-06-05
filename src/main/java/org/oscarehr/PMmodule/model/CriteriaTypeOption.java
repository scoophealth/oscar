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
 * CriteriaTypeOption entity. @author azhou
 */
@Entity
@Table(name = "criteria_type_option")
public class CriteriaTypeOption extends AbstractModel<Integer> implements java.io.Serializable  {

	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "OPTION_ID", unique = true, nullable = false)
	private Integer id;
	@Column(name = "CRITERIA_TYPE_ID", nullable = false)
	private Integer criteriaTypeId;
	@Column(name = "DISPLAY_ORDER_NUMBER", nullable = false)
	private Integer displayOrderNumber;
	@Column(name = "OPTION_LABEL", nullable = false, length = 128)
	private String optionLabel;
	@Column(name = "OPTION_VALUE")
	private String optionValue;
	@Column(name = "RANGE_START_VALUE")
	private Integer rangeStartValue;
	@Column(name = "RANGE_END_VALUE")
	private Integer rangeEndValue;

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
	public CriteriaTypeOption() {
	}

	/** minimal constructor */
	public CriteriaTypeOption(Integer criteriaTypeId,
			Integer displayOrderNumber, String optionLabel) {
		this.criteriaTypeId = criteriaTypeId;
		this.displayOrderNumber = displayOrderNumber;
		this.optionLabel = optionLabel;
	}

	/** full constructor */
	public CriteriaTypeOption(Integer criteriaTypeId,
			Integer displayOrderNumber, String optionLabel, String optionValue,
			Integer rangeStartValue, Integer rangeEndValue) {
		this.criteriaTypeId = criteriaTypeId;
		this.displayOrderNumber = displayOrderNumber;
		this.optionLabel = optionLabel;
		this.optionValue = optionValue;
		this.rangeStartValue = rangeStartValue;
		this.rangeEndValue = rangeEndValue;
	}

	/**
     * @return the criteriaTypeId
     */
    public Integer getCriteriaTypeId() {
    	return criteriaTypeId;
    }

	/**
     * @param criteriaTypeId the criteriaTypeId to set
     */
    public void setCriteriaTypeId(Integer criteriaTypeId) {
    	this.criteriaTypeId = criteriaTypeId;
    }

	/**
     * @return the displayOrderNumber
     */
    public Integer getDisplayOrderNumber() {
    	return displayOrderNumber;
    }

	/**
     * @param displayOrderNumber the displayOrderNumber to set
     */
    public void setDisplayOrderNumber(Integer displayOrderNumber) {
    	this.displayOrderNumber = displayOrderNumber;
    }

	/**
     * @return the optionLabel
     */
    public String getOptionLabel() {
    	return optionLabel;
    }

	/**
     * @param optionLabel the optionLabel to set
     */
    public void setOptionLabel(String optionLabel) {
    	this.optionLabel = optionLabel;
    }

	/**
     * @return the optionValue
     */
    public String getOptionValue() {
    	return optionValue;
    }

	/**
     * @param optionValue the optionValue to set
     */
    public void setOptionValue(String optionValue) {
    	this.optionValue = optionValue;
    }

	/**
     * @return the rangeStartValue
     */
    public Integer getRangeStartValue() {
    	return rangeStartValue;
    }

	/**
     * @param rangeStartValue the rangeStartValue to set
     */
    public void setRangeStartValue(Integer rangeStartValue) {
    	this.rangeStartValue = rangeStartValue;
    }

	/**
     * @return the rangeEndValue
     */
    public Integer getRangeEndValue() {
    	return rangeEndValue;
    }

	/**
     * @param rangeEndValue the rangeEndValue to set
     */
    public void setRangeEndValue(Integer rangeEndValue) {
    	this.rangeEndValue = rangeEndValue;
    }

}
