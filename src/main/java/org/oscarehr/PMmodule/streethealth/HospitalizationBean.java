package org.oscarehr.PMmodule.streethealth;

public class HospitalizationBean {
	private String date;
	private String length;
	private String psychiatric;
	private String physical;
	private String declined;
	
	public HospitalizationBean() {
		
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getPsychiatric() {
		return psychiatric;
	}
	public void setPsychiatric(String psychiatric) {
		this.psychiatric = psychiatric;
	}
	public String getPhysical() {
		return physical;
	}
	public void setPhysical(String physical) {
		this.physical = physical;
	}
	public String getDeclined() {
		return declined;
	}
	public void setDeclined(String declined) {
		this.declined = declined;
	}
	
	
}
