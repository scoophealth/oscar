package org.oscarehr.billing.CA.ON.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name="billing_on_errorCode")
public class BillingONErrorCode extends AbstractModel<String>{

	@Id
	private String id;

	private String description;

	public String getDescription() {
    	return description;
    }

	public void setDescription(String description) {
    	this.description = description;
    }

	public String getId() {
    	return id;
    }


}
