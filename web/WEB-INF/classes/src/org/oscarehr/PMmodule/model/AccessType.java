package org.oscarehr.PMmodule.model;

import org.oscarehr.PMmodule.model.base.BaseAccessType;

/**
 * This is the object class that relates to the access_type table.
 * Any customizations belong here.
 */
public class AccessType extends BaseAccessType {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public AccessType () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public AccessType (java.lang.Long _id) {
		super(_id);
	}

/*[CONSTRUCTOR MARKER END]*/
}