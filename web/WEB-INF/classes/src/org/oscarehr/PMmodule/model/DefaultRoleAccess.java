package org.oscarehr.PMmodule.model;

import org.oscarehr.PMmodule.model.base.BaseDefaultRoleAccess;

/**
 * This is the object class that relates to the default_role_access table.
 * Any customizations belong here.
 */
public class DefaultRoleAccess extends BaseDefaultRoleAccess {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public DefaultRoleAccess () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public DefaultRoleAccess (java.lang.Long _id) {
		super(_id);
	}

/*[CONSTRUCTOR MARKER END]*/
}