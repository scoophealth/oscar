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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name = "vacancy")
public class Vacancy extends AbstractModel<Integer> implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@Column(name="vacancyName")
	private String name;
	
	@Column(nullable = false)
	private Integer templateId;
	@Column(nullable = false, length = 24)
	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateClosed;
		
	private String reasonClosed;
	
    private String emailNotificationAddressesCsv=null;
        
	private Integer	wlProgramId;
		
	private Date dateCreated;
	
	private String statusUpdateUser;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date statusUpdateDate;
	
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
			String reasonClosed, String name) {
		this.templateId = templateId;
		this.status = status;
		this.dateClosed = dateClosed;
		this.reasonClosed = reasonClosed;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatusUpdateUser() {
		return statusUpdateUser;
	}

	public void setStatusUpdateUser(String statusUpdateUser) {
		this.statusUpdateUser = statusUpdateUser;
	}

	public Date getStatusUpdateDate() {
		return statusUpdateDate;
	}

	public void setStatusUpdateDate(Date statusUpdateDate) {
		this.statusUpdateDate = statusUpdateDate;
	}
	
	
}
