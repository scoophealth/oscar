package org.oscarehr.PMmodule.model;

import java.util.Calendar;
import java.util.Date;

import org.oscarehr.PMmodule.model.base.BaseFormintakea;

/**
 * This is the object class that relates to the formintakea table.
 * Any customizations belong here.
 */
public class Formintakea extends BaseFormintakea {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Formintakea () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Formintakea (java.lang.Long _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public Formintakea (
		java.lang.Long _id,
		java.lang.Long _demographicNo) {

		super (
			_id,
			_demographicNo);
	}
/*[CONSTRUCTOR MARKER END]*/
	
	public void setDOB(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		setDay(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
		setMonth(String.valueOf(cal.get(Calendar.MONTH)+1));
		setYear(String.valueOf(cal.get(Calendar.YEAR)));
	}
}