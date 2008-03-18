package com.quatro.web.report;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class QuatroReportListForm extends ActionForm{
	List reportGroups;
	String provider;
	String chkDel;
	
	public List getReportGroups() {
		return reportGroups;
	}

	public void setReportGroups(List reportGroups) {
		this.reportGroups = reportGroups;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getChkDel() {
		return chkDel;
	}

	public void setChkDel(String chkDel) {
		this.chkDel = chkDel;
	}
	
}
