package org.oscarehr.PMmodule.model;

import org.oscarehr.PMmodule.model.base.BaseAdmission;

/**
 * This is the object class that relates to the admission table. Any customizations belong here.
 */
public class Admission extends BaseAdmission {

	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
	public Admission() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Admission(java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Admission(java.lang.Long id, java.lang.Long agencyId, java.lang.Long providerNo, java.lang.Integer clientId, java.lang.Long programId) {
		super(id, agencyId, providerNo, clientId, programId);
	}
	
	/* [CONSTRUCTOR MARKER END] */

	private Program program;
	
	public void setProgram(Program p) {
		this.program = p;
	}

	public Program getProgram() {
		return program;
	}

}