package org.caisi.model;

import java.util.ArrayList;
import java.util.List;


public class TicklerFilter  {
	private String startDate;
	private String endDate;
	private char status;
	private String provider;
	private String assignee;
	private String custom;
	private List customFilters;
	
	public TicklerFilter() {
		customFilters = new ArrayList();
	}
	
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public String getCustom() {
		return custom;
	}
	public void setCustom(String custom) {
		this.custom = custom;
	}
	public List getCustomFilters() {
		return customFilters;
	}
	public void setCustomFilters(List customFilters) {
		this.customFilters = customFilters;
	}
	
}
