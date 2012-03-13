package org.oscarehr.ws.transfer_objects;

import java.util.List;

import org.oscarehr.common.model.AppointmentType;
import org.springframework.beans.BeanUtils;

public final class AppointmentTypeTransfer {

	private Integer id=null;
	private String name = null;
	private String notes = null;
	private String reason = null;
	private String location = null;
	private String resources = null;
	private int duration;

	public Integer getId() {
		return (id);
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return (name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNotes() {
		return (notes);
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getReason() {
		return (reason);
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getLocation() {
		return (location);
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getResources() {
		return (resources);
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	public int getDuration() {
		return (duration);
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public static AppointmentTypeTransfer toTransfer(AppointmentType appointmentType) {
		AppointmentTypeTransfer appointmentTypeTransfer = new AppointmentTypeTransfer();

		BeanUtils.copyProperties(appointmentType, appointmentTypeTransfer);

		return (appointmentTypeTransfer);
	}

	public static AppointmentTypeTransfer[] toTransfer(List<AppointmentType> appointmentTypes) {
		AppointmentTypeTransfer[] result = new AppointmentTypeTransfer[appointmentTypes.size()];

		for (int i = 0; i < appointmentTypes.size(); i++) {
			result[i] = toTransfer(appointmentTypes.get(i));
		}

		return (result);
	}
}
