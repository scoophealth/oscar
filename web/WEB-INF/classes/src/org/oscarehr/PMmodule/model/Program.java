package org.oscarehr.PMmodule.model;

import org.oscarehr.PMmodule.model.base.BaseProgram;

/**
 * This is the object class that relates to the program table. Any customizations belong here.
 */
public class Program extends BaseProgram {
	
	public static final Integer DEFAULT_COMMUNITY_PROGRAM_ID = new Integer(10010);
	
	public static final String BED_TYPE = "Bed";
	public static final String SERVICE_TYPE = "Service";
	
	/* [CONSTRUCTOR MARKER BEGIN] */
	
	public Program() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Program(java.lang.Integer _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public Program(java.lang.Integer _id, java.lang.Integer _maxAllowed, java.lang.String _name, java.lang.Long _agencyId) {
		super(_id, _maxAllowed, _name, _agencyId);
	}

	/* [CONSTRUCTOR MARKER END] */
	
	public boolean isFull() {
		if (getNumOfMembers().intValue() >= getMaxAllowed().intValue()) {
			return true;
		}
		
		return false;
	}
	
	public boolean isBed() {
		return BED_TYPE.equalsIgnoreCase(getType());
	}
	
	public boolean isService() {
		return SERVICE_TYPE.equalsIgnoreCase(getType());
	}

	public boolean getHoldingTank() {
		return isHoldingTank();
	}
	
}