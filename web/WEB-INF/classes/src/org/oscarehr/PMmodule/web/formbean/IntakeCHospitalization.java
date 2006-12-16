package org.oscarehr.PMmodule.web.formbean;

public class IntakeCHospitalization {
	private String date;
	private String length;
	private boolean psychiatric;
	private boolean physicalHealth;
	private boolean unknown;
	
	public IntakeCHospitalization() {
		setDate("");
		setLength("0");
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
	public boolean isPhysicalHealth() {
		return physicalHealth;
	}
	public void setPhysicalHealth(boolean physicalHealth) {
		this.physicalHealth = physicalHealth;
	}
	public boolean isPsychiatric() {
		return psychiatric;
	}
	public void setPsychiatric(boolean psychiatric) {
		this.psychiatric = psychiatric;
	}
	public boolean isUnknown() {
		return unknown;
	}
	public void setUnknown(boolean unknown) {
		this.unknown = unknown;
	}
	
	
}
