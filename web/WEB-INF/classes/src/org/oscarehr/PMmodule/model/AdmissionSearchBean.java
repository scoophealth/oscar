package org.oscarehr.PMmodule.model;

import java.util.Date;

public class AdmissionSearchBean {
	private Long providerNo;
	private String admissionStatus;
	private Long clientId;
	private Date startDate;
	private Date endDate;
	private Long programId;
	
	public Long getProgramId() {
		return programId;
	}
	public void setProgramId(Long programId) {
		this.programId = programId;
	}
	public String getAdmissionStatus() {
		return admissionStatus;
	}
	public void setAdmissionStatus(String admissionStatus) {
		this.admissionStatus = admissionStatus;
	}
	public Long getClientId() {
		return clientId;
	}
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Long getProviderNo() {
		return providerNo;
	}
	public void setProviderNo(Long providerNo) {
		this.providerNo = providerNo;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}
