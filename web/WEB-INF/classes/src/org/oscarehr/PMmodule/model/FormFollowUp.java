package org.oscarehr.PMmodule.model;

import org.oscarehr.PMmodule.model.base.BaseFormFollowUp;

/**
 * This is the object class that relates to the formfollowup table.
 * Any customizations belong here.
 */
public class FormFollowUp extends BaseFormFollowUp {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public FormFollowUp () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public FormFollowUp (java.lang.Long _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public FormFollowUp (
		java.lang.Long _id,
		java.lang.Long _demographicNo) {

		super (
			_id,
			_demographicNo);
	}

/*[CONSTRUCTOR MARKER END]*/
}