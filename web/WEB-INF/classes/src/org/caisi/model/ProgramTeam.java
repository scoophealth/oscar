package org.caisi.model;

import java.util.List;

import org.caisi.model.base.BaseProgramTeam;

/**
 * This is the object class that relates to the program_team table.
 * Any customizations belong here.
 */
public class ProgramTeam extends BaseProgramTeam {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ProgramTeam () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ProgramTeam (java.lang.Long _id) {
		super(_id);
	}

/*[CONSTRUCTOR MARKER END]*/
	
	private List providers;
	private List admissions;
	/**
	 * @return Returns the admissions.
	 */
	public List getAdmissions() {
		return admissions;
	}

	/**
	 * @param admissions The admissions to set.
	 */
	public void setAdmissions(List admissions) {
		this.admissions = admissions;
	}

	/**
	 * @return Returns the providers.
	 */
	public List getProviders() {
		return providers;
	}

	/**
	 * @param providers The providers to set.
	 */
	public void setProviders(List providers) {
		this.providers = providers;
	}
	
}