package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ctl_billingtype")
public class CtlBillingType extends AbstractModel<String>{

	@Id
	@Column(name="servicetype")
	private String id;

	@Column(name="billtype")
	private String billType;

	public String getId() {
    	return id;
    }

	public void setId(String id) {
    	this.id = id;
    }

	public String getBillType() {
    	return billType;
    }

	public void setBillType(String billType) {
    	this.billType = billType;
    }


}
