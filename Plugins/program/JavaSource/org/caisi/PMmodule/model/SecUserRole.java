package org.caisi.PMmodule.model;

import org.caisi.PMmodule.model.base.BaseSecUserRole;

/**
 * This is the object class that relates to the secUserRole table.
 * Any customizations belong here.
 */
public class SecUserRole extends BaseSecUserRole {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public SecUserRole () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public SecUserRole (
		java.lang.String _roleName,
		java.lang.String _providerNo) {

		super (
			_roleName,
			_providerNo);
	}

/*[CONSTRUCTOR MARKER END]*/
}