package org.oscarehr.common.model;

import javax.persistence.Column;


public class ProviderSitePK implements java.io.Serializable  {

	@Column(name="provider_no")
	private String providerNo;
	@Column(name="site_id")
	private int siteId;

	public ProviderSitePK() {

	}

	public ProviderSitePK(String providerNo, int siteId) {
		this.providerNo = providerNo;
		this.siteId = siteId;
	}

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public int getSiteId() {
    	return siteId;
    }

	public void setSiteId(int siteId) {
    	this.siteId = siteId;
    }


	public String toString() {
		return ("ProviderNo=" + providerNo + ", siteId=" + siteId);
	}

	@Override
	public int hashCode() {
		return (toString().hashCode());
	}

	@Override
	public boolean equals(Object o) {
		try {
			ProviderSitePK o1 = (ProviderSitePK) o;
			return ((providerNo == o1.providerNo) && (siteId == o1.siteId));
		} catch (RuntimeException e) {
			return (false);
		}
	}
}
