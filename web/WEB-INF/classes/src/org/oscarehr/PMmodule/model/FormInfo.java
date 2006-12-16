package org.oscarehr.PMmodule.model;

import java.util.Date;

public class FormInfo {
	private Long formId;
	private Long providerNo;
	private Date formDate;
	private String providerName;
	
	public Date getFormDate() {
		return formDate;
	}
	public void setFormDate(Date formDate) {
		this.formDate = formDate;
	}
	public Long getFormId() {
		return formId;
	}
	public void setFormId(Long formId) {
		this.formId = formId;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public Long getProviderNo() {
		return providerNo;
	}
	public void setProviderNo(Long providerNo) {
		this.providerNo = providerNo;
	}
}
