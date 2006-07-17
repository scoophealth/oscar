package org.caisi.PMmodule.model;

import org.caisi.PMmodule.model.base.BaseAdmission;

/**
 * This is the object class that relates to the admission table.
 * Any customizations belong here.
 */
public class Admission extends BaseAdmission {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Admission () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Admission (java.lang.Long _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public Admission (
		java.lang.Long _id,
		java.lang.Long _providerNo,
		java.lang.Long _clientId,
		java.lang.Long _programId) {

		super (
			_id,
			_providerNo,
			_clientId,
			_programId);
	}
/*[CONSTRUCTOR MARKER END]*/
	
}