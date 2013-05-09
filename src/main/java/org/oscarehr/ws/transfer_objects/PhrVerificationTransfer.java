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


package org.oscarehr.ws.transfer_objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.oscarehr.common.model.PHRVerification;
import org.springframework.beans.BeanUtils;

public final class PhrVerificationTransfer {

	private Integer id;
	private Integer demographicNo; 
	private String phrUserName ;
	private String verificationLevel;
	private Date verificationDate ;
	private String verificationBy;
	private Boolean photoId ;
	private Boolean parentGuardian;
	private String comments;
	private Date createdDate;
	private Boolean archived;
	


	public Integer getId() {
		return (id);
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDemographicNo() {
		return (demographicNo);
	}

	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}

	public String getPhrUserName() {
		return (phrUserName);
	}

	public void setPhrUserName(String phrUserName) {
		this.phrUserName = phrUserName;
	}

	public String getVerificationLevel() {
		return (verificationLevel);
	}

	public void setVerificationLevel(String verificationLevel) {
		this.verificationLevel = verificationLevel;
	}

	public Date getVerificationDate() {
		return (verificationDate);
	}

	public void setVerificationDate(Date verificationDate) {
		this.verificationDate = verificationDate;
	}

	public String getVerificationBy() {
		return (verificationBy);
	}

	public void setVerificationBy(String verificationBy) {
		this.verificationBy = verificationBy;
	}

	public Boolean getPhotoId() {
		return (photoId);
	}

	public void setPhotoId(Boolean photoId) {
		this.photoId = photoId;
	}

	public Boolean getParentGuardian() {
		return (parentGuardian);
	}

	public void setParentGuardian(Boolean parentGuardian) {
		this.parentGuardian = parentGuardian;
	}

	public String getComments() {
		return (comments);
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getCreatedDate() {
		return (createdDate);
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Boolean getArchived() {
		return (archived);
	}

	public void setArchived(Boolean archived) {
		this.archived = archived;
	}

	public static PhrVerificationTransfer toTransfer(PHRVerification phrVerification) {
		if (phrVerification==null) return(null);
		
		PhrVerificationTransfer transfer = new PhrVerificationTransfer();

		BeanUtils.copyProperties(phrVerification, transfer);

		return (transfer);
	}

	public static PhrVerificationTransfer[] toTransfers(List<PHRVerification> phrVerifications) {
		ArrayList<PhrVerificationTransfer> results = new ArrayList<PhrVerificationTransfer>();

		for (PHRVerification phrVerification : phrVerifications) {
			results.add(toTransfer(phrVerification));
		}

		return (results.toArray(new PhrVerificationTransfer[0]));
	}

	@Override
	public String toString() {
		return (ReflectionToStringBuilder.toString(this));
	}
}
