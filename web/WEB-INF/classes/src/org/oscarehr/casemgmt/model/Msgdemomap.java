package org.oscarehr.casemgmt.model;

import org.oscarehr.casemgmt.model.base.BaseMsgdemomap;

/**
 * This is the object class that relates to the msgdemomap table.
 * Any customizations belong here.
 */
public class Msgdemomap extends BaseMsgdemomap {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Msgdemomap () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Msgdemomap (
		java.lang.Integer _messageID,
		java.lang.Integer _demographicNo) {

		super (
			_messageID,
			_demographicNo);
	}

/*[CONSTRUCTOR MARKER END]*/
}