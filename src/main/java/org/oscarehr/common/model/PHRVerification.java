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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class PHRVerification  extends AbstractModel<Integer> implements Serializable {

	public static final String VERIFICATION_METHOD_FAX="FAX";
	public static final String VERIFICATION_METHOD_MAIL="MAIL";
	public static final String VERIFICATION_METHOD_EMAIL="EMAIL";
	public static final String VERIFICATION_METHOD_TEL="TEL";
	public static final String VERIFICATION_METHOD_VIDEOPHONE="VIDEOPHONE";
	public static final String VERIFICATION_METHOD_INPERSON="INPERSON";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Integer demographicNo; 
	private String phrUserName ;
	private String verificationLevel ;
	@Temporal(TemporalType.TIMESTAMP)
	private Date verificationDate ;
	private String verificationBy;
	private Boolean photoId ;
	private Boolean parentGuardian;
	private String comments;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	private Boolean archived;
	
	
	
	@Override
    public Integer getId() {
		return id;
    }
	public Integer getDemographicNo() {
    	return demographicNo;
    }
	public void setDemographicNo(Integer demographicNo) {
    	this.demographicNo = demographicNo;
    }
	public String getPhrUserName() {
    	return phrUserName;
    }
	public void setPhrUserName(String phrUserName) {
    	this.phrUserName = phrUserName;
    }
	public String getVerificationLevel() {
    	return verificationLevel;
    }
	public void setVerificationLevel(String authenticationLevel) {
    	this.verificationLevel = authenticationLevel;
    }
	public Date getVerificationDate() {
    	return verificationDate;
    }
	public void setVerificationDate(Date authenticationDate) {
    	this.verificationDate = authenticationDate;
    }
	public String getVerificationBy() {
    	return verificationBy;
    }
	public void setVerificationBy(String authenticationBy) {
    	this.verificationBy = authenticationBy;
    }
	public Boolean getPhotoId() {
    	return photoId;
    }
	public void setPhotoId(Boolean photoId) {
    	this.photoId = photoId;
    }
	public String getComments() {
    	return comments;
    }
	public void setComments(String comments) {
    	this.comments = comments;
    }
	public Date getCreatedDate() {
    	return createdDate;
    }
	public void setCreatedDate(Date createdDate) {
    	this.createdDate = createdDate;
    }
	public Boolean getArchived() {
    	return archived;
    }
	public void setArchived(Boolean archived) {
    	this.archived = archived;
    }
	public void setId(Integer id) {
    	this.id = id;
    }
	public Boolean getParentGuardian() {
    	return parentGuardian;
    }
	public void setParentGuardian(Boolean parentGuardian) {
    	this.parentGuardian = parentGuardian;
    }
	
}
