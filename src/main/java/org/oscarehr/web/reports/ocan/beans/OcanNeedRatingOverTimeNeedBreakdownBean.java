package org.oscarehr.web.reports.ocan.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OcanNeedRatingOverTimeNeedBreakdownBean {

	private String ocanName;
	private Date ocanDate;

	//pre ordered..each item is a ROW
	private List<OcanConsumerStaffNeedBean> needs = new ArrayList<OcanConsumerStaffNeedBean>();



	public String getOcanName() {
		return ocanName;
	}



	public void setOcanName(String ocanName) {
		this.ocanName = ocanName;
	}



	public Date getOcanDate() {
		return ocanDate;
	}



	public void setOcanDate(Date ocanDate) {
		this.ocanDate = ocanDate;
	}



	public List<OcanConsumerStaffNeedBean> getNeeds() {
		return needs;
	}



	public void setNeeds(List<OcanConsumerStaffNeedBean> needs) {
		this.needs = needs;
	}

}
