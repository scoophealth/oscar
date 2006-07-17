package org.caisi.PMmodule.model;

import org.caisi.PMmodule.model.base.BaseBedLog;

/**
 * This is the object class that relates to the bed_log table.
 * Any customizations belong here.
 */
public class BedLog extends BaseBedLog {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BedLog () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BedLog (java.lang.Long _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public BedLog (
		java.lang.Long _id,
		java.lang.Long _programId,
		java.lang.String _providerNo,
		java.lang.Long _sheetId,
		java.lang.Long _demographicNo,
		java.lang.String _time,
		java.lang.String _status,
		java.util.Date _dateCreated) {

		super (
			_id,
			_programId,
			_providerNo,
			_sheetId,
			_demographicNo,
			_time,
			_status,
			_dateCreated);
	}
/*[CONSTRUCTOR MARKER END]*/
	private Demographic client;
	public Demographic getClient() {
		return client;
	}

	public void setClient(Demographic client) {
		this.client = client;
	}
	
}