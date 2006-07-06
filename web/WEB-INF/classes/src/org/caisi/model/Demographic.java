package org.caisi.model;

import org.caisi.model.base.BaseDemographic;

/**
 * This is the object class that relates to the demographic table.
 * Any customizations belong here.
 */
public class Demographic extends BaseDemographic {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Demographic () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Demographic (java.lang.Long _demographicNo) {
		super(_demographicNo);
	}

	/**
	 * Constructor for required fields
	 */
	public Demographic (
		java.lang.Long _demographicNo,
		java.lang.String _sex,
		java.lang.String _firstName,
		java.lang.String _lastName) {

		super (
			_demographicNo,
			_sex,
			_firstName,
			_lastName);
	}

/*[CONSTRUCTOR MARKER END]*/
	
    public String getFormattedName() {
        return getLastName() + "," + getFirstName();
    }

    public String getDemographic_no() {
    	return String.valueOf(getDemographicNo());
    }
    
    public void setDemographic_no(String demographic_no) {
    	setDemographicNo(Long.valueOf(demographic_no));
    }
}