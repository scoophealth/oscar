package org.oscarehr.ws.rest.to.model;

public class AllowedAppointmentTypeTransfer {
	
	Long id;
	Character[] codes;
	int duration;
	String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Character[] getCodes() {
		return codes;
	}

	public void setCodes(Character[] codes) {
		this.codes = codes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
