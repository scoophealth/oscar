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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.model.AbstractModel;

@Entity
public class Vacancy extends AbstractModel<Integer> implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@Column(nullable = false)
	private Integer templateId;
	@Column(nullable = false, length = 24)
	private String status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateClosed;
	private String reasonClosed;
    private String emailNotificationAddressesCsv=null;
    
    @Column(name = "WL_PROGRAM_ID", nullable = false)
	private Integer	wlProgramId;
	
	@Column(name = "DATE_CREATE", length = 19)
	private Date dateCreated;
	
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
    	this.status = StringUtils.trimToNull(status);
    }

	/**
     * @return the dateClosed
     */
    public Date getDateClosed() {
    	return dateClosed;
    }

	/**
     * @param dateClosed the dateClosed to set
     */
    public void setDateClosed(Date dateClosed) {
    	this.dateClosed = dateClosed;
    }

    
	public Date getDateCreated() {
    	return dateCreated;
    }

	public void setDateCreated(Date dateCreated) {
    	this.dateCreated = dateCreated;
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
    	this.reasonClosed = StringUtils.trimToNull(reasonClosed);
    }

	public String getEmailNotificationAddressesCsv() {
    	return emailNotificationAddressesCsv;
    }

	public void setEmailNotificationAddressesCsv(String emailNotificationAddressesCsv) {
    	this.emailNotificationAddressesCsv = StringUtils.trimToNull(emailNotificationAddressesCsv);
    }
	public Integer getWlProgramId() {
    	return wlProgramId;
    }

	public void setWlProgramId(Integer wlProgramId) {
    	this.wlProgramId = wlProgramId;
    }
}
