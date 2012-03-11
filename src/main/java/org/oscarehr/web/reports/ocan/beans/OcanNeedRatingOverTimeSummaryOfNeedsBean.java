package org.oscarehr.web.reports.ocan.beans;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OcanNeedRatingOverTimeSummaryOfNeedsBean {

	private String ocanName;
	private Date ocanDate;

	//for summary table
	private Map<String,Integer> consumerNeedMap = new HashMap<String,Integer>();
	private Map<String,Integer>staffNeedMap = new HashMap<String,Integer>();

	public OcanNeedRatingOverTimeSummaryOfNeedsBean() {
		getConsumerNeedMap().put("unknown",0);
		getConsumerNeedMap().put("no",0);
		getConsumerNeedMap().put("met",0);
		getConsumerNeedMap().put("unmet",0);
		getStaffNeedMap().put("unknown",0);
		getStaffNeedMap().put("no",0);
		getStaffNeedMap().put("met",0);
		getStaffNeedMap().put("unmet",0);
	}
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
	public Map<String, Integer> getConsumerNeedMap() {
		return consumerNeedMap;
	}
	public void setConsumerNeedMap(Map<String, Integer> consumerNeedMap) {
		this.consumerNeedMap = consumerNeedMap;
	}
	public Map<String, Integer> getStaffNeedMap() {
		return staffNeedMap;
	}
	public void setStaffNeedMap(Map<String, Integer> staffNeedMap) {
		this.staffNeedMap = staffNeedMap;
	}


}
