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
 * CriteriaSelectionOption entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "criteria_selection_option")
public class CriteriaSelectionOption extends AbstractModel<Integer> implements java.io.Serializable {

	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SELECT_OPTION_ID", unique=true, nullable=false)
	private Integer id;
	@Column(name = "CRITERIA_ID", nullable = false)
	private Integer criteriaId;
	@Column(name = "OPTION_VALUE")
	private String optionValue;

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
	public CriteriaSelectionOption() {
	}

	/** minimal constructor */
	public CriteriaSelectionOption(Integer criteriaId) {
		this.criteriaId = criteriaId;
	}

	/** full constructor */
	public CriteriaSelectionOption(Integer criteriaId, String optionValue) {
		this.criteriaId = criteriaId;
		this.optionValue = optionValue;
	}


	/**
     * @return the criteriaId
     */
    public Integer getCriteriaId() {
    	return criteriaId;
    }

	/**
     * @param criteriaId the criteriaId to set
     */
    public void setCriteriaId(Integer criteriaId) {
    	this.criteriaId = criteriaId;
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

}
