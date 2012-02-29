package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="reporttemp")
public class ReportTemp extends AbstractModel<ReportTempPK>  {

	@EmbeddedId
	private ReportTempPK id;

	@Column(name="demo_name")
	private String demoName;

	@Column(name="provider_no")
	private String providerNo;

	private String address;

	private String creator;

	public ReportTempPK getId() {
    	return id;
    }

	public void setId(ReportTempPK id) {
    	this.id = id;
    }

	public String getDemoName() {
    	return demoName;
    }

	public void setDemoName(String demoName) {
    	this.demoName = demoName;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public String getAddress() {
    	return address;
    }

	public void setAddress(String address) {
    	this.address = address;
    }

	public String getCreator() {
    	return creator;
    }

	public void setCreator(String creator) {
    	this.creator = creator;
    }



}
