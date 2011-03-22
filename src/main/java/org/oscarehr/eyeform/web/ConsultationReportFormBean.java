package org.oscarehr.eyeform.web;

import java.util.List;

import org.oscarehr.common.model.Provider;

public class ConsultationReportFormBean {

	private String status;
	private String providerNo;
	private String demographicNo;
	private String startDate;
	private String endDate;
	private String demographicName;
	
	private List<Provider> providerList;
	
	
	
	public List<Provider> getProviderList() {
		return providerList;
	}
	public void setProviderList(List<Provider> providerList) {
		this.providerList = providerList;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProviderNo() {
		return providerNo;
	}
	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}
	public String getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(String demographicNo) {
		this.demographicNo = demographicNo;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getDemographicName() {
		return demographicName;
	}
	public void setDemographicName(String demographicName) {
		this.demographicName = demographicName;
	}
	
	
	
	
}
