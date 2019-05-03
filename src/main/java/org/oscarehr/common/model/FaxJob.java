/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FaxJob")
@Entity
@Table(name="faxes")
public class FaxJob extends AbstractModel<Integer> implements Comparable<FaxJob> {
	
	public enum STATUS {
		
		RECEIVED,SENT,COMPLETE,ERROR,WAITING,CANCELLED,RESOLVED				
				
	}
	public static enum Direction {IN, OUT}
	
	public FaxJob() {		
		this.id = null;
		this.user = null;
		this.password = null;
		this.file_name = null;
		this.fax_line = null;
		this.destination = null;
		this.status = null;
		this.statusString = null;
		this.numPages = null;
		this.stamp = null;
		this.document = null;
		this.jobId = null;
		this.senderEmail = null;
		this.direction = null;

	}
	
	public FaxJob( FaxJob faxJob ) {		
		this.id = null;
		this.user = faxJob.getUser();
		this.password = faxJob.getPassword();
		this.file_name = faxJob.getFile_name();
		this.fax_line = faxJob.getFax_line();
		this.destination = faxJob.getDestination();
		this.status = faxJob.getStatus();
		this.statusString = faxJob.getStatusString();
		this.numPages = faxJob.getNumPages();
		this.stamp = faxJob.getStamp();
		this.document = faxJob.getDocument();
		this.jobId = faxJob.getJobId();
		this.senderEmail = faxJob.getSenderEmail();
		this.direction = faxJob.getDirection();

	}

    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
        
    private String user;
    
    @Transient
    private String password;
        
    @Column(name="filename")
    private String file_name;
    
    @Column(name="faxline")
    private String fax_line;
    
    private String destination;
    
    @Enumerated(EnumType.STRING)
    private STATUS status;
    
    private Integer numPages;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date stamp;
    
    private String document;

    @Column(name="jobId")
    private Long jobId;
    
    private String oscarUser;
    
    private String statusString;
    
    private Integer demographicNo;
    
    @Transient
    private String senderEmail;
    
    @Transient
    private Direction direction;
    
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
    
    /**
     * @return the file_namne
     */
    public String getFile_name() {
        return file_name;
    }
    /**
     * @param file_namne the file_namne to set
     */
    public void setFile_name(String file_namne) {
        this.file_name = file_namne;
    }
    /**
     * @return the fax_line
     */
    public String getFax_line() {
        return fax_line;
    }
    /**
     * @param fax_line the fax_line to set
     */
    public void setFax_line(String fax_line) {
        this.fax_line = fax_line;
    }
    /**
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }
    /**
     * @param destination the destination to set
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }
    /**
     * @return the status
     */
    public STATUS getStatus() {
    	return this.status;
    }
    /**
     * @param status the status to set
     */
    public void setStatus(STATUS status) {
        this.status = status;       
    }
    /**
     * @return the document
     */
    public String getDocument() {
        return document;
    }
    /**
     * @param document the document to set
     */
    public void setDocument(String document) {
        this.document = document;
    }
    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }
    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * @return the numPages
     */
    public Integer getNumPages() {
        return numPages;
    }
    /**
     * @param numPages the numPages to set
     */
    public void setNumPages(Integer numPages) {
        this.numPages = numPages;
    }
    /**
     * @return the stamp
     */
    public Date getStamp() {
        return stamp;
    }
    /**
     * @param stamp the stamp to set
     */
    public void setStamp(Date stamp) {
        this.stamp = stamp;
    }

	/**
	 * @return the jobId
	 */
    public Long getJobId() {
	    return jobId;
    }

	/**
	 * @param jobId the jobId to set
	 */
    public void setJobId(Long jobId) {
	    this.jobId = jobId;
    }

	@Override
    public int compareTo(FaxJob arg0) {
	
		if( arg0 == null ) {
			return 1;
		}
		
		return stamp.compareTo(arg0.getStamp());
    }

	/**
	 * @return the oscarUser
	 */
    public String getOscarUser() {
	    return oscarUser;
    }

	/**
	 * @param oscarUser the oscarUser to set
	 */
    public void setOscarUser(String oscarUser) {
	    this.oscarUser = oscarUser;
    }

	/**
	 * @return the statusString
	 */
    public String getStatusString() {
	    return statusString;
    }

	/**
	 * @param statusString the statusString to set
	 */
    public void setStatusString(String statusString) {
	    this.statusString = statusString;
    }

	/**
	 * @return the demographic_no
	 */
	public Integer getDemographicNo() {
		return demographicNo;
	}

	/**
	 * @param demographic_no the demographic_no to set
	 */
	public void setDemographicNo(Integer demographic_no) {
		this.demographicNo = demographic_no;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

}
