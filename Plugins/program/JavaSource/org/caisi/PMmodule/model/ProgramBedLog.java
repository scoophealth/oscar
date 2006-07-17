package org.caisi.PMmodule.model;

import org.caisi.PMmodule.model.base.BaseProgramBedLog;

/**
 * This is the object class that relates to the program_bedlog table.
 * Any customizations belong here.
 */
public class ProgramBedLog extends BaseProgramBedLog {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ProgramBedLog () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ProgramBedLog (java.lang.Long _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public ProgramBedLog (
		java.lang.Long _id,
		boolean _enabled) {

		super (
			_id,
			_enabled);
	}

/*[CONSTRUCTOR MARKER END]*/
}