package org.oscarehr.casemgmt.model;

import org.oscarehr.casemgmt.model.base.BaseMessagetbl;

/**
 * This is the object class that relates to the messagetbl table.
 * Any customizations belong here.
 */
public class Messagetbl extends BaseMessagetbl {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Messagetbl () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Messagetbl (java.lang.Integer _messageid) {
		super(_messageid);
	}

/*[CONSTRUCTOR MARKER END]*/
}