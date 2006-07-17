package org.caisi.PMmodule.model;

import org.caisi.PMmodule.model.base.BaseLog;

/**
 * This is the object class that relates to the log table.
 * Any customizations belong here.
 */
public class Log extends BaseLog {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Log () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Log (long _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public Log (
		long _id,
		java.lang.String _content) {

		super (
			_id,
			_content);
	}
/*[CONSTRUCTOR MARKER END]*/
}