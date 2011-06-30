package org.oscarehr.hospitalReportManager.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.oscarehr.common.model.AbstractModel;

@Entity
public class HRMProviderConfidentialityStatement extends AbstractModel<String> {

	@Id
	private String providerNo;
	
	private String statement;
	
	@Override
	public String getId() {
		return providerNo;
	}
	
	public void setId(String id) {
		this.providerNo = id;
	}

	public String getStatement() {
    	return statement;
    }

	public void setStatement(String statement) {
    	this.statement = statement;
    }

}
