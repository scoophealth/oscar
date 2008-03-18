package com.quatro.model;
import java.util.List;
public class ReportGroupValue {
	int reportGroupId;
	String description;
	String providerNo;
	List reports;
	public String getProviderNo() {
		return providerNo;
	}
	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}
	public int getReportGroupId() {
		return reportGroupId;
	}
	public void setReportGroupId(int id) {
		this.reportGroupId = id;
	}
	public String getReportGroupDesc() {
		return description;
	}
	public void setReportGroupDesc(String description) {
		this.description = description;
	}
	public List getReports() {
		return reports;
	}
	public void setReports(List reports) {
		this.reports = reports;
	}
}
