package org.oscarehr.ws.rest.to.model;

import org.oscarehr.appointment.search.AppointmentType;

public class AppointmentTypeTransfer {
	
	Long id;
	String name;
	int duration;

	/*public static AppointmentTypeTransfer getFromTransfer(org.oscarehr.ws.AppointmentTypeTransfer appointmentTypeTransfer){
		AppointmentTypeTransfer at = new AppointmentTypeTransfer();
		
		try{
			at.id = new Long(appointmentTypeTransfer.getId());
		}catch(Exception e) {
			//This will be null if it's coming from oscar and beanutils copy properties has not been fixed. But the id is not used internally so we will ignore it.
		}
		at.name = appointmentTypeTransfer.getName();
		at.duration = appointmentTypeTransfer.getDuration();
		return at;
	}
	*/
	
	public static AppointmentTypeTransfer getFromTransfer(AppointmentType appointmentTypeTransfer){
		AppointmentTypeTransfer at = new AppointmentTypeTransfer();
		at.id = appointmentTypeTransfer.getId();
		at.name = appointmentTypeTransfer.getName();
		at.duration = appointmentTypeTransfer.getDefaultDurationMinutes();
		return at;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	
}
