package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="providerbillcenter")
public class ProviderBillCenter extends AbstractModel<String>{

	@Id
	@Column(name="provider_no")
	private String providerNo;
	@Column(name="billcenter_code")
	private String billCenterCode;

	public String getProviderNo() {
    	return providerNo;
    }
	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }
	public String getBillCenterCode() {
    	return billCenterCode;
    }
	public void setBillCenterCode(String billCenterCode) {
    	this.billCenterCode = billCenterCode;
    }
	@Override
    public String getId() {
	    return getProviderNo();
    }



}
