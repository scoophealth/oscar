package org.oscarehr.PMmodule.model;

import org.oscarehr.PMmodule.model.base.BaseFunctionalUserType;

/**
 * This is the object class that relates to the functional_user_type table.
 * Any customizations belong here.
 */
public class FunctionalUserType extends BaseFunctionalUserType {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public FunctionalUserType () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public FunctionalUserType (java.lang.Long _id) {
		super(_id);
	}

/*[CONSTRUCTOR MARKER END]*/
}