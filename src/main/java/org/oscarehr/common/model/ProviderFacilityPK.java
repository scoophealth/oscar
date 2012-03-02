package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProviderFacilityPK implements Serializable {
	@Column(name="provider_no")
	private String providerNo;
	@Column(name="facility_id")
	private int facilityId;
	public String getProviderNo() {
    	return providerNo;
    }
	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }
	public int getFacilityId() {
    	return facilityId;
    }
	public void setFacilityId(int facilityId) {
    	this.facilityId = facilityId;
    }


	@Override
	public String toString() {
		return ("providerNo=" + providerNo + ", facilityId=" + facilityId);
	}

	@Override
	public int hashCode() {
		return (providerNo.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		try {
			ProviderFacilityPK o1 = (ProviderFacilityPK) o;
			return ((providerNo.equals(o1.providerNo)) && (facilityId == o1.facilityId));
		} catch (RuntimeException e) {
			return (false);
		}
	}
}
