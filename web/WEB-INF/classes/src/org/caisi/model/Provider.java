package org.caisi.model;

public class Provider extends BaseObject {
	private String provider_no;
	private String firstName;
	private String lastName;
	private CaisiRole role;
	private String provider_type;
	
	public Provider() {}
	
	public Provider(String provider_no) {
		this.provider_no = provider_no;
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
	public String getProvider_no() {
		return provider_no;
	}
	public void setProvider_no(String provider_no) {
		this.provider_no = provider_no;
	}
	
	/* custom */
	public String getFormattedName() {
		return getLastName() + "," + getFirstName();
	}
	
	public boolean equals(Object obj) { 
		if(!(obj instanceof Provider)) {
			return false;
		}
		if(((Provider)obj).getProvider_no().equals(provider_no)) {
			return true;
		}
		return false;
	}

	public CaisiRole getRole() {
		return role;
	}

	public void setRole(CaisiRole role) {
		this.role = role;
	}

	public String getProvider_type() {
		return provider_type;
	}

	public void setProvider_type(String provider_type) {
		this.provider_type = provider_type;
	}
	
}
