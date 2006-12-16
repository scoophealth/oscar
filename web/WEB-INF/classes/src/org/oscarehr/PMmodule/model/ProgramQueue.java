package org.oscarehr.PMmodule.model;

import org.oscarehr.PMmodule.model.base.BaseProgramQueue;
/**
 * This is the object class that relates to the program_queue table.
 * Any customizations belong here.
 */
public class ProgramQueue extends BaseProgramQueue {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ProgramQueue () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ProgramQueue (java.lang.Long _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public ProgramQueue (
		java.lang.Long _id,
		java.lang.Long _agencyId,
		java.lang.Long _clientId,
		java.lang.Long _providerNo,
		java.lang.Long _programId) {

		super (
			_id,
			_agencyId,
			_clientId,
			_providerNo,
			_programId);
	}
/*[CONSTRUCTOR MARKER END]*/
	public String getProviderFormattedName() {
		return getProviderLastName() + "," + getProviderFirstName();
	}
	
	public String getClientFormattedName() {
		return getClientLastName() + "," + getClientFirstName();
	}
}