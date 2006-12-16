package org.oscarehr.PMmodule.model;

import org.oscarehr.PMmodule.model.base.BaseConsentInterview;

/**
 * This is the object class that relates to the consent_interview table.
 * Any customizations belong here.
 */
public class ConsentInterview extends BaseConsentInterview {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ConsentInterview () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ConsentInterview (java.lang.Long _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public ConsentInterview (
		java.lang.Long _id,
		long _consentId,
		java.lang.Long _demographicNo,
		java.lang.String _providerNo) {

		super (
			_id,
			_consentId,
			_demographicNo,
			_providerNo);
	}

/*[CONSTRUCTOR MARKER END]*/
}