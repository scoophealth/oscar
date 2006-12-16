package org.oscarehr.casemgmt.model;

import org.oscarehr.casemgmt.model.base.BaseEncounterform;

/**
 * This is the object class that relates to the encounterform table.
 * Any customizations belong here.
 */
public class Encounterform extends BaseEncounterform {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Encounterform () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Encounterform (java.lang.String _formValue) {
		super(_formValue);
	}

	/**
	 * Constructor for required fields
	 */
	public Encounterform (
		java.lang.String _formValue,
		java.lang.Integer _hidden,
		java.lang.String _formName,
		java.lang.String _formTable) {

		super (
			_formValue,
			_hidden,
			_formName,
			_formTable);
	}

/*[CONSTRUCTOR MARKER END]*/
}