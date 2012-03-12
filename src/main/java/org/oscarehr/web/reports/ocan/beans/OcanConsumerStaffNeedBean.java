package org.oscarehr.web.reports.ocan.beans;

public class OcanConsumerStaffNeedBean {
	private String consumerNeed;
	private String staffNeed;

	public OcanConsumerStaffNeedBean() {

	}

	public OcanConsumerStaffNeedBean(String consumerNeed, String staffNeed) {
		this.consumerNeed = consumerNeed;
		this.staffNeed = staffNeed;
	}

	public String getConsumerNeed() {
		return consumerNeed;
	}
	public void setConsumerNeed(String consumerNeed) {
		this.consumerNeed = consumerNeed;
	}
	public String getStaffNeed() {
		return staffNeed;
	}
	public void setStaffNeed(String staffNeed) {
		this.staffNeed = staffNeed;
	}

	public boolean isInAgreement() {
		return staffNeed!=null && consumerNeed!=null && staffNeed.equals(consumerNeed);
	}
}