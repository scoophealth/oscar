package org.oscarehr.olis.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.oscarehr.common.model.AbstractModel;

@Entity
public class OLISProviderPreferences extends AbstractModel<String> {
	@Id
	private String providerId;

	private String startTime;

	@Override
	public String getId() {
		return providerId;
	}
	
    public String getProviderId() {
	    return providerId;
    }
	
	public void setProviderId(String providerNo) {
		this.providerId = providerNo;
    }	

	public String getStartTime() {
    	return startTime;
    }

	public void setStartTime(String startTime) {
    	this.startTime = startTime;
    }

	public OLISProviderPreferences(){
		super();
	}
}
