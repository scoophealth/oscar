package org.oscarehr.PMmodule.model;

import org.oscarehr.PMmodule.model.base.BaseConsent;

/**
 * This is the object class that relates to the consent table.
 * Any customizations belong here.
 */
public class Consent extends BaseConsent {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Consent () {
		super();
		this.setHardcopy(false);
		this.setSignatureDeclaration(false);
		this.setRefusedToSign(false);
	}

	/**
	 * Constructor for primary key
	 */
	public Consent (java.lang.Long _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public Consent (
		java.lang.Long _id,
		java.lang.Long _demographicNo,
		java.lang.String _providerNo) {

		super (
			_id,
			_demographicNo,
			_providerNo);
	}
/*[CONSTRUCTOR MARKER END]*/
	
	private String optout;

	public String getOptout() {
		return optout;
	}

	public void setOptout(String optout) {
		this.optout = optout;
	}
	
	
	private String exclusionString;

	public String getExclusionString() {
		return exclusionString;
	}

	public void setExclusionString(String exclusionString) {
		this.exclusionString = exclusionString;
	}
	
	
	
}