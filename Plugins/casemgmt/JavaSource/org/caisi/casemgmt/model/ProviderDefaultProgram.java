package org.caisi.casemgmt.model;

import org.caisi.casemgmt.model.base.BaseProviderDefaultProgram;

/**
 * This is the object class that relates to the provider_default_program table.
 * Any customizations belong here.
 */
public class ProviderDefaultProgram extends BaseProviderDefaultProgram {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ProviderDefaultProgram () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ProviderDefaultProgram (java.lang.Integer _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public ProviderDefaultProgram (
		java.lang.Integer _id,
		java.lang.String _providerNo,
		java.lang.Integer _programId) {

		super (
			_id,
			_providerNo,
			_programId);
	}

/*[CONSTRUCTOR MARKER END]*/
}