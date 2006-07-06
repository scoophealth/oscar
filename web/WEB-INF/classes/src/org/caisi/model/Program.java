package org.caisi.model;

import org.caisi.model.base.BaseProgram;

/**
 * This is the object class that relates to the program table.
 * Any customizations belong here.
 */
public class Program extends BaseProgram {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Program () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Program (java.lang.Integer _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public Program (
		java.lang.Integer _id,
		java.lang.String _name,
		java.lang.Long _agencyId) {

		super (
			_id,
			_name,
			_agencyId);
	}

/*[CONSTRUCTOR MARKER END]*/
}