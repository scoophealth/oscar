package org.oscarehr.PMmodule.model;

import org.oscarehr.PMmodule.model.base.BaseProgramAccess;

/**
 * This is the object class that relates to the program_access table.
 * Any customizations belong here.
 */
public class ProgramAccess extends BaseProgramAccess {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ProgramAccess () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ProgramAccess (java.lang.Long _id) {
		super(_id);
	}

/*[CONSTRUCTOR MARKER END]*/
}