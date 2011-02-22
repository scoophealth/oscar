package oscar.oscarEncounter.oscarMeasurements.model;

import oscar.oscarEncounter.oscarMeasurements.model.base.BaseMeasurements;



public class Measurements extends BaseMeasurements {
	private static final long serialVersionUID = 1L;
	
	private int appointmentNo;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Measurements () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Measurements (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Measurements (
		java.lang.Integer id,
		java.lang.String type,
		java.lang.Integer demographicNo,
		java.lang.String providerNo,
		java.lang.String dataField,
		java.lang.String measuringInstruction,
		java.lang.String comments,
		java.util.Date dateObserved,
		java.util.Date dateEntered) {

		super (
			id,
			type,
			demographicNo,
			providerNo,
			dataField,
			measuringInstruction,
			comments,
			dateObserved,
			dateEntered);
	}

	public int getAppointmentNo() {
		return appointmentNo;
	}

	public void setAppointmentNo(int appointmentNo) {
		this.appointmentNo = appointmentNo;
	}

/*[CONSTRUCTOR MARKER END]*/

	

}