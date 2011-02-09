package org.oscarehr.PMmodule.web.formbean;

import org.apache.struts.validator.ValidatorForm;

public class StaffForm extends ValidatorForm{
	private String firstName;
	private String lastName;
	private String orgcd;
	
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
	public String getOrgcd() {
		return orgcd;
	}
	public void setOrgcd(String orgcd) {
		this.orgcd = orgcd;
	}
	
	
}
