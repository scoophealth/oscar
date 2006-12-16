package org.oscarehr.PMmodule.model;

import org.oscarehr.PMmodule.model.base.BaseProvider;

/**
 * This is the object class that relates to the provider table. Any customizations belong here.
 */
public class Provider extends BaseProvider {
	
	public static final String SYSTEM_PROVIDER_NO = "1";

	/* [CONSTRUCTOR MARKER BEGIN] */
	public Provider() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Provider(java.lang.String _providerNo) {
		super(_providerNo);
	}

	/**
	 * Constructor for required fields
	 */
	public Provider(java.lang.String _providerNo, java.lang.String _lastName, java.lang.String _providerType, java.lang.String _sex, java.lang.String _specialty, java.lang.String _firstName) {
		super(_providerNo, _lastName, _providerType, _sex, _specialty, _firstName);
	}

	/* [CONSTRUCTOR MARKER END] */

	public String getFormattedName() {
		return getLastName() + ", " + getFirstName();
	}

	public String getProvider_no() {
		return getProviderNo();
	}
}