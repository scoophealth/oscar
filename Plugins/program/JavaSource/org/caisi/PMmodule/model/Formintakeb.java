package org.caisi.PMmodule.model;

import org.caisi.PMmodule.model.base.BaseFormintakeb;

/**
 * This is the object class that relates to the formintakeb table.
 * Any customizations belong here.
 */
public class Formintakeb extends BaseFormintakeb {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Formintakeb () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Formintakeb (java.lang.Long _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public Formintakeb (
		java.lang.Long _id,
		java.lang.Long _demographicNo) {

		super (
			_id,
			_demographicNo);
	}

/*[CONSTRUCTOR MARKER END]*/
}