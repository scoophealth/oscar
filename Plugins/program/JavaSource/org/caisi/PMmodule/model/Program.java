package org.caisi.PMmodule.model;

import java.util.List;

import org.caisi.PMmodule.model.base.BaseProgram;

/**
 * This is the object class that relates to the program table.
 * Any customizations belong here.
 */
public class Program extends BaseProgram {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Program () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Program (java.lang.Integer _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public Program (
		java.lang.Integer _id,
		java.lang.Integer _maxAllowed,
		java.lang.String _name,
		java.lang.Long _agencyId) {

		super (
			_id,
			_maxAllowed,
			_name,
			_agencyId);
	}
/*[CONSTRUCTOR MARKER END]*/
	public boolean getHoldingTank() {
		return isHoldingTank();
	}
	
	private List teamList;

	public List getTeamList() {
		return teamList;
	}

	public void setTeamList(List teamList) {
		this.teamList = teamList;
	}
	
}