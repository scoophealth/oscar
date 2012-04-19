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


package org.oscarehr.common.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;

import org.apache.commons.lang.StringUtils;

@Entity
public class RemoteReferral extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer facilityId;
	private Integer demographicId;
	private String referringProviderNo;
	private String referredToFacilityName;
	private String referredToProgramName;
	private Calendar referalDate;
	private String reasonForReferral;
	private String presentingProblem;
	private Calendar createDate = new GregorianCalendar();

	public Integer getFacilityId() {
    	return (facilityId);
    }

	public void setFacilityId(Integer facilityId) {
    	this.facilityId = facilityId;
    }

	public Integer getDemographicId() {
		return (demographicId);
	}

	public void setDemographicId(Integer demographicId) {
		this.demographicId = demographicId;
	}

	public String getReferringProviderNo() {
		return (referringProviderNo);
	}

	public void setReferringProviderNo(String referringProviderNo) {
		this.referringProviderNo = StringUtils.trimToNull(referringProviderNo);
	}

	public String getReferredToFacilityName() {
		return (referredToFacilityName);
	}

	public void setReferredToFacilityName(String referredToFacilityName) {
		this.referredToFacilityName = StringUtils.trimToNull(referredToFacilityName);
	}

	public String getReferredToProgramName() {
		return (referredToProgramName);
	}

	public void setReferredToProgramName(String referredToProgramName) {
		this.referredToProgramName = StringUtils.trimToNull(referredToProgramName);
	}

	public Calendar getReferalDate() {
		return (referalDate);
	}

	public void setReferalDate(Calendar referalDate) {
		this.referalDate = referalDate;
	}

	public String getReasonForReferral() {
		return (reasonForReferral);
	}

	public void setReasonForReferral(String reasonForReferral) {
		this.reasonForReferral = StringUtils.trimToNull(reasonForReferral);
	}

	public String getPresentingProblem() {
		return (presentingProblem);
	}

	public void setPresentingProblem(String presentingProblem) {
		this.presentingProblem = StringUtils.trimToNull(presentingProblem);
	}

	public Calendar getCreateDate() {
		return (createDate);
	}

	public void setCreateDate(Calendar createDate) {
		this.createDate = createDate;
	}

	@Override
	public Integer getId() {
		return (id);
	}
	
	@PreRemove
	protected void jpaPreventDelete()
	{
		throw(new UnsupportedOperationException("not allowed to remove this type of object."));
	}
}
