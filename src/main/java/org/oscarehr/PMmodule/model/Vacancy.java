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

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.oscarehr.common.model.AbstractModel;

/**
 * Vacancy entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "vacancy")
public class Vacancy extends AbstractModel<Long> implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "VACANCY_ID", unique=true, nullable=false)
	private Long vacancyId;
	@Column(name = "TEMPLATE_ID", nullable = false)
	private Integer templateId;
	@Column(name = "STATUS", nullable = false, length = 24)
	private String status;
	@Column(name = "DATE_CLOSED", length = 19)
	private Timestamp dateClosed;
	@Column(name = "REASON_CLOSED")
	private String reasonClosed;

	/**
	 * @return the id
	 */
	public Long getId() {
		return vacancyId;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.vacancyId = id;
	}
	
	/** default constructor */
	public Vacancy() {
	}

	/** minimal constructor */
	public Vacancy(Integer templateId, String status) {
		this.templateId = templateId;
		this.status = status;
	}

	/** full constructor */
	public Vacancy(Integer templateId, String status, Timestamp dateClosed,
			String reasonClosed) {
		this.templateId = templateId;
		this.status = status;
		this.dateClosed = dateClosed;
		this.reasonClosed = reasonClosed;
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
     * @return the status
     */
    public String getStatus() {
    	return status;
    }

	/**
     * @param status the status to set
     */
    public void setStatus(String status) {
    	this.status = status;
    }

	/**
     * @return the dateClosed
     */
    public Timestamp getDateClosed() {
    	return dateClosed;
    }

	/**
     * @param dateClosed the dateClosed to set
     */
    public void setDateClosed(Timestamp dateClosed) {
    	this.dateClosed = dateClosed;
    }

	/**
     * @return the reasonClosed
     */
    public String getReasonClosed() {
    	return reasonClosed;
    }

	/**
     * @param reasonClosed the reasonClosed to set
     */
    public void setReasonClosed(String reasonClosed) {
    	this.reasonClosed = reasonClosed;
    }

}
