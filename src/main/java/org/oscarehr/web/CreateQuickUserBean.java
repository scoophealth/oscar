package org.oscarehr.web;

import org.apache.struts.action.ActionForm;

public class CreateQuickUserBean extends ActionForm {
	private String firstName;
	private String lastName;
	private String gender;
	private String username;
	private String password;
	private String confirmPassword;
	private String copyProviderNo;
	private String pin;
	
	
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getCopyProviderNo() {
		return copyProviderNo;
	}

	public void setCopyProviderNo(String copyProviderNo) {
		this.copyProviderNo = copyProviderNo;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}
	
	
	
}
