package oscar.appt.status.model;

import oscar.appt.status.model.base.BaseAppointmentStatus;



public class AppointmentStatus extends BaseAppointmentStatus {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public AppointmentStatus () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public AppointmentStatus (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public AppointmentStatus (
		java.lang.Integer id,
		java.lang.String status,
		java.lang.String description,
		java.lang.String color,
		java.lang.String icon,
		java.lang.Integer active) {

		super (
			id,
			status,
			description,
			color,
			icon,
			active);
	}

/*[CONSTRUCTOR MARKER END]*/


}