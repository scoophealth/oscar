package org.caisi.PMmodule.model;

import org.caisi.PMmodule.model.base.BaseBedLogSheet;

/**
 * This is the object class that relates to the bed_log_sheet table.
 * Any customizations belong here.
 */
public class BedLogSheet extends BaseBedLogSheet {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BedLogSheet () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BedLogSheet (java.lang.Long _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public BedLogSheet (
		java.lang.Long _id,
		java.lang.Long _programId,
		boolean _closed,
		java.util.Date _dateCreated) {

		super (
			_id,
			_programId,
			_closed,
			_dateCreated);
	}
/*[CONSTRUCTOR MARKER END]*/
}