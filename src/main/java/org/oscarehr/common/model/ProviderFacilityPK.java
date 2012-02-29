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


}
