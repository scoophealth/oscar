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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.CollectionOfElements;

/**
 */
@Entity
public class IntegratorConsent extends AbstractModel<Integer> {

	public enum ConsentStatus {
		GIVEN, REVOKED, DEFERRED_NOT_APPROPRIATE, DEFERRED_CONSIDER_LATER, REFUSED_TO_SIGN
	}
	
	public enum SignatureStatus {
		PAPER, ELECTRONIC
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;
	/** This is the facility which is creating this record, i.e. the facility of the provider making this change. */
	private Integer facilityId = -1;
	private Integer demographicId = -1;
	private String providerNo = null;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate = new Date();

	/**
	 * (Integer,Boolean) is (IntegratorFacilityId,ShareData).
	 */
	@CollectionOfElements(fetch = FetchType.EAGER)
	@JoinTable(name = "IntegratorConsentShareDataMap")
	private Map<Integer, Boolean> consentToShareData = new HashMap<Integer, Boolean>();

	private boolean excludeMentalHealthData = false;
	
	@Enumerated(EnumType.STRING)
	private ConsentStatus clientConsentStatus = null;
	
	@Enumerated(EnumType.STRING)
	private SignatureStatus signatureStatus = null;
	
	private Date expiry=null;
	
	private Integer digitalSignatureId = null;

	public Integer getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}

	public Integer getDemographicId() {
		return demographicId;
	}

	public void setDemographicId(Integer demographicId) {
		this.demographicId = demographicId;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = StringUtils.trimToNull(providerNo);
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
    public Integer getId() {
		return id;
	}

	public Map<Integer, Boolean> getConsentToShareData() {
		return consentToShareData;
	}

	public void setConsentToShareData(Map<Integer, Boolean> consentToShareData) {
		this.consentToShareData = consentToShareData;
	}

	public boolean isExcludeMentalHealthData() {
		return excludeMentalHealthData;
	}

	public void setExcludeMentalHealthData(boolean excludeMentalHealthData) {
		this.excludeMentalHealthData = excludeMentalHealthData;
	}

	public Integer getDigitalSignatureId() {
		return digitalSignatureId;
	}

	public void setDigitalSignatureId(Integer digitalSignatureId) {
		this.digitalSignatureId = digitalSignatureId;
	}

	public ConsentStatus getClientConsentStatus() {
		return clientConsentStatus;
	}

	public void setClientConsentStatus(ConsentStatus clientConsentStatus) {
		this.clientConsentStatus = clientConsentStatus;
	}

	public SignatureStatus getSignatureStatus() {
		return signatureStatus;
	}

	public void setSignatureStatus(SignatureStatus signatureStatus) {
		this.signatureStatus = signatureStatus;
	}
	
	public Date getExpiry() {
    	return expiry;
    }

	public void setExpiry(Date expiry) {
    	this.expiry = expiry;
    }

	@PreRemove
	protected void jpa_preventDelete() {
		throw (new UnsupportedOperationException("Remove is not allowed for this type of item."));
	}

	@PreUpdate
	protected void jpa_preventUpdate() {
		throw (new UnsupportedOperationException("Update is not allowed for this type of item."));
	}
}
