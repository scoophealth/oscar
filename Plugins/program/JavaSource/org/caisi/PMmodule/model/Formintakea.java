package org.caisi.PMmodule.model;

import org.caisi.PMmodule.model.base.BaseFormintakea;

/**
 * This is the object class that relates to the formintakea table.
 * Any customizations belong here.
 */
public class Formintakea extends BaseFormintakea {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Formintakea () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Formintakea (java.lang.Long _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public Formintakea (
		java.lang.Long _id,
		java.lang.Long _demographicNo) {

		super (
			_id,
			_demographicNo);
	}

/*[CONSTRUCTOR MARKER END]*/
}