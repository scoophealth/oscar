package org.oscarehr.web.reports.ocan.beans;

import java.util.ArrayList;
import java.util.List;

public class SummaryOfActionsAndCommentsDomainBean {

	private String domainName;
	private String domainId;
	private List<SummaryOfActionsAndCommentsOCANBean> ocanBeans = new ArrayList<SummaryOfActionsAndCommentsOCANBean>();

	public SummaryOfActionsAndCommentsDomainBean() {

	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public List<SummaryOfActionsAndCommentsOCANBean> getOcanBeans() {
		return ocanBeans;
	}

	public void setOcanBeans(List<SummaryOfActionsAndCommentsOCANBean> ocanBeans) {
		this.ocanBeans = ocanBeans;
	}


}
