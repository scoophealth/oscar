package org.oscarehr.PMmodule.web.formbean;

public class PreIntakeForm {
	private String firstName;
	private String lastName;
	private long agencyId;
	private String clientId;
	
	private String monthOfBirth;
	private String dayOfBirth;
	private String yearOfBirth;
	
	private String healthCardNumber;
	private String healthCardVersion;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public long getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(long agencyId) {
		this.agencyId = agencyId;
	}
	public String getDayOfBirth() {
		return dayOfBirth;
	}
	public String getMonthOfBirth() {
		return monthOfBirth;
	}
	public String getYearOfBirth() {
		return yearOfBirth;
	}
	public void setDayOfBirth(String dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	}
	public void setMonthOfBirth(String monthOfBirth) {
		this.monthOfBirth = monthOfBirth;
	}
	public void setYearOfBirth(String yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}
	public String getHealthCardNumber() {
		return healthCardNumber;
	}
	public String getHealthCardVersion() {
		return healthCardVersion;
	}
	public void setHealthCardNumber(String healthCardNumber) {
		this.healthCardNumber = healthCardNumber;
	}
	public void setHealthCardVersion(String healthCardVersion) {
		this.healthCardVersion = healthCardVersion;
	}
	
}
