package org.caisi.PMmodule.model;

import org.caisi.PMmodule.model.base.BaseAgency;

/**
 * This is the object class that relates to the agency table.
 * Any customizations belong here.
 */
public class Agency extends BaseAgency {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Agency () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Agency (java.lang.Long _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public Agency (
		java.lang.Long _id,
		java.lang.String _name,
		boolean _local,
		boolean _integratorEnabled) {

		super (
			_id,
			_name,
			_local,
			_integratorEnabled);
	}
/*[CONSTRUCTOR MARKER END]*/

	public boolean getLocal() {
		return isLocal();
	}
}