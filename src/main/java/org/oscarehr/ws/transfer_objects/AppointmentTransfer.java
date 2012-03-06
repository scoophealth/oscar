package org.oscarehr.ws.transfer_objects;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.oscarehr.common.model.Appointment;
import org.springframework.beans.BeanUtils;

import oscar.util.DateUtils;

public final class AppointmentTransfer {
	private Integer id;
	private String providerNo;
	private Calendar appointmentStartDateTime;
	private Calendar appointmentEndDateTime;
	private String name;
	private int demographicNo;
	private int programId;
	private String notes;
	private String reason;
	private String location;
	private String resources;
	private String type;
	private String style;
	private String billing;
	private String status;
	private Calendar createDateTime;
	private Calendar updateDateTime;
	private String creator;
	private String lastUpdateUser;
	private String remarks;
	private String urgency;

	public Integer getId() {
		return (id);
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProviderNo() {
		return (providerNo);
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public Calendar getAppointmentStartDateTime() {
		return (appointmentStartDateTime);
	}

	public void setAppointmentStartDateTime(Calendar appointmentStartDateTime) {
		this.appointmentStartDateTime = appointmentStartDateTime;
	}

	public Calendar getAppointmentEndDateTime() {
		return (appointmentEndDateTime);
	}

	public void setAppointmentEndDateTime(Calendar appointmentEndDateTime) {
		this.appointmentEndDateTime = appointmentEndDateTime;
	}

	public String getName() {
		return (name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDemographicNo() {
		return (demographicNo);
	}

	public void setDemographicNo(int demographicNo) {
		this.demographicNo = demographicNo;
	}

	public int getProgramId() {
		return (programId);
	}

	public void setProgramId(int programId) {
		this.programId = programId;
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

	public String getType() {
		return (type);
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStyle() {
		return (style);
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getBilling() {
		return (billing);
	}

	public void setBilling(String billing) {
		this.billing = billing;
	}

	public String getStatus() {
		return (status);
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Calendar getCreateDateTime() {
		return (createDateTime);
	}

	public void setCreateDateTime(Calendar createDateTime) {
		this.createDateTime = createDateTime;
	}

	public Calendar getUpdateDateTime() {
		return (updateDateTime);
	}

	public void setUpdateDateTime(Calendar updateDateTime) {
		this.updateDateTime = updateDateTime;
	}

	public String getCreator() {
		return (creator);
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getLastUpdateUser() {
		return (lastUpdateUser);
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public String getRemarks() {
		return (remarks);
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUrgency() {
		return (urgency);
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public static AppointmentTransfer toTransfer(Appointment appointment) {
		AppointmentTransfer appointmentTransfer = new AppointmentTransfer();

		String[] ignored = { "appointmentDate", "startTime", "endTime", "createDateTime", "updateDateTime" };
		BeanUtils.copyProperties(appointment, appointmentTransfer, ignored);

		GregorianCalendar cal = DateUtils.toGregorianCalendar(appointment.getAppointmentDate(), appointment.getStartTime());
		appointmentTransfer.setAppointmentStartDateTime(cal);

		cal = DateUtils.toGregorianCalendar(appointment.getAppointmentDate(), appointment.getStartTime());
		appointmentTransfer.setAppointmentStartDateTime(cal);

		appointmentTransfer.setCreateDateTime(DateUtils.toGregorianCalendar(appointment.getCreateDateTime()));
		appointmentTransfer.setUpdateDateTime(DateUtils.toGregorianCalendar(appointment.getUpdateDateTime()));

		return (appointmentTransfer);
	}

	public static AppointmentTransfer[] toTransfer(List<Appointment> appointments) {
		AppointmentTransfer[] result = new AppointmentTransfer[appointments.size()];

		for (int i = 0; i < appointments.size(); i++) {
			result[i]=toTransfer(appointments.get(i));
		}

		return (result);
	}
}
