package oscar.oscarEncounter.oscarMeasurements.model;

import oscar.oscarEncounter.oscarMeasurements.model.base.BaseMeasurementtype;



public class Measurementtype extends BaseMeasurementtype {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Measurementtype () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Measurementtype (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Measurementtype (
		java.lang.Integer id,
		java.lang.String type,
		java.lang.String typeDisplayName,
		java.lang.String typeDescription,
		java.lang.String measuringInstruction,
		java.lang.String validation) {

		super (
			id,
			type,
			typeDisplayName,
			typeDescription,
			measuringInstruction,
			validation);
	}

/*[CONSTRUCTOR MARKER END]*/


}