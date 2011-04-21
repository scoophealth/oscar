package org.oscarehr.appointment.web;

public class NextAppointmentSearchBean {
	private String providerNo;
	private String dayOfWeek;
	private String startTimeOfDay;
	private String endTimeOfDay;
	private String code;
	private int numResults;
	
	public String getProviderNo() {
    	return providerNo;
    }
	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }
	public String getDayOfWeek() {
    	return dayOfWeek;
    }
	public void setDayOfWeek(String dayOfWeek) {
    	this.dayOfWeek = dayOfWeek;
    }
	public String getStartTimeOfDay() {
    	return startTimeOfDay;
    }
	public void setStartTimeOfDay(String startTimeOfDay) {
    	this.startTimeOfDay = startTimeOfDay;
    }
	public String getEndTimeOfDay() {
    	return endTimeOfDay;
    }
	public void setEndTimeOfDay(String endTimeOfDay) {
    	this.endTimeOfDay = endTimeOfDay;
    }
	public String getCode() {
    	return code;
    }
	public void setCode(String code) {
    	this.code = code;
    }
	public int getNumResults() {
    	return numResults;
    }
	public void setNumResults(int numResults) {
    	this.numResults = numResults;
    }
	
	
}
