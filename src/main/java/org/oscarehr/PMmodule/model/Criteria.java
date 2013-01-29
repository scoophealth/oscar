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
 * Criteria entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "criteria")
public class Criteria extends AbstractModel<Integer> implements java.io.Serializable {

	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CRITERIA_ID", unique = true, nullable = false)
	private Integer id;
	@Column(name = "CRITERIA_TYPE_ID", nullable = false)
	private Integer criteriaTypeId;
	@Column(name = "CRITERIA_VALUE")
	private String criteriaValue;
	@Column(name = "RANGE_START_VALUE")
	private Integer rangeStartValue;
	@Column(name = "RANGE_END_VALUE")
	private Integer rangeEndValue;
	@Column(name = "TEMPLATE_ID")
	private Integer templateId;
	@Column(name = "VACANCY_ID")
	private Integer vacancyId;
	@Column(name = "MATCH_SCORE_WEIGHT", nullable = false, precision = 22, scale = 0)
	private Double matchScoreWeight=1.0;
	@Column(name = "CAN_BE_ADHOC", nullable = false)
	private Integer canBeAdhoc=0;

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
	public Criteria() {
		
	}

	/** minimal constructor */
	public Criteria(Integer criteriaTypeId, Double matchScoreWeight, Integer canBeAdhoc) {
		this.criteriaTypeId = criteriaTypeId;
		this.matchScoreWeight = matchScoreWeight;
		this.canBeAdhoc = canBeAdhoc;
	}

	/** full constructor */
	public Criteria(Integer criteriaTypeId, String criteriaValue, Integer rangeStartValue, Integer rangeEndValue, Integer templateId, Integer vacancyId, Double matchScoreWeight, Integer canBeAdhoc) {
		this.criteriaTypeId = criteriaTypeId;
		this.criteriaValue = criteriaValue;
		this.rangeStartValue = rangeStartValue;
		this.rangeEndValue = rangeEndValue;
		this.templateId = templateId;
		this.vacancyId = vacancyId;
		this.matchScoreWeight = matchScoreWeight;
		this.canBeAdhoc = canBeAdhoc;
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
	 * @return the criteriaValue
	 */
	public String getCriteriaValue() {
		return criteriaValue;
	}

	/**
	 * @param criteriaValue the criteriaValue to set
	 */
	public void setCriteriaValue(String criteriaValue) {
		this.criteriaValue = criteriaValue;
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

	/**
	 * @return the templateId
	 */
	public Integer getTemplateId() {
		return templateId;
	}

	/**
	 * @param templateId the templateId to set
	 */
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	/**
	 * @return the vacancyId
	 */
	public Integer getVacancyId() {
		return vacancyId;
	}

	/**
	 * @param vacancyId the vacancyId to set
	 */
	public void setVacancyId(Integer vacancyId) {
		this.vacancyId = vacancyId;
	}

	/**
	 * @return the matchScoreWeight
	 */
	public Double getMatchScoreWeight() {
		return matchScoreWeight;
	}

	/**
	 * @param matchScoreWeight the matchScoreWeight to set
	 */
	public void setMatchScoreWeight(Double matchScoreWeight) {
		this.matchScoreWeight = matchScoreWeight;
	}

	/**
	 * @return the canBeAdhoc
	 */
	public Integer getCanBeAdhoc() {
		return canBeAdhoc;
	}

	/**
	 * @param canBeAdhoc the canBeAdhoc to set
	 */
	public void setCanBeAdhoc(Integer canBeAdhoc) {
		this.canBeAdhoc = canBeAdhoc;
	}

}
