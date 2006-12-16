package org.oscarehr.PMmodule.model;

import org.oscarehr.PMmodule.model.base.BaseFormintakec;

/**
 * This is the object class that relates to the formintakec table.
 * Any customizations belong here.
 */
public class Formintakec extends BaseFormintakec {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Formintakec () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Formintakec (java.lang.Long _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public Formintakec (
		java.lang.Long _id,
		java.lang.Long _demographicNo) {

		super (
			_id,
			_demographicNo);
	}

/*[CONSTRUCTOR MARKER END]*/
}