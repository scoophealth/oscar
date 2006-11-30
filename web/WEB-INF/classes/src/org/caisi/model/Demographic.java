package org.caisi.model;

import org.caisi.model.base.BaseDemographic;

/**
 * This is the object class that relates to the demographic table. Any customizations belong here.
 */
public class Demographic extends BaseDemographic {

	/* [CONSTRUCTOR MARKER BEGIN] */
	public Demographic() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Demographic(java.lang.Integer demographicNo) {
		super(demographicNo);
	}

	/**
	 * Constructor for required fields
	 */
	public Demographic(java.lang.Integer demographicNo, java.lang.String sex, java.lang.String firstName, java.lang.String lastName) {
		super(demographicNo, sex, firstName, lastName);
	}

	/* [CONSTRUCTOR MARKER END] */

	public String getFormattedName() {
		return getLastName() + "," + getFirstName();
	}

	public String getDemographic_no() {
		return String.valueOf(getDemographicNo());
	}

	public void setDemographic_no(String demographic_no) {
		setDemographicNo(Integer.valueOf(demographic_no));
	}
}