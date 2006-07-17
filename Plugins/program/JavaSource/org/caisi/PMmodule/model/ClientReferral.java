package org.caisi.PMmodule.model;

import org.caisi.PMmodule.model.base.BaseClientReferral;

/**
 * This is the object class that relates to the client_referral table.
 * Any customizations belong here.
 */
public class ClientReferral extends BaseClientReferral {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ClientReferral () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ClientReferral (java.lang.Long _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public ClientReferral (
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
}