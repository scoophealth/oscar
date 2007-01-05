package org.oscarehr.PMmodule.model;

import org.oscarehr.PMmodule.model.base.BaseBedCheckTime;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;

public class BedCheckTime extends BaseBedCheckTime {

	private static final long serialVersionUID = 1L;
	
	public static BedCheckTime create(Integer programId, String time) {
		BedCheckTime bedCheckTime = new BedCheckTime();
		bedCheckTime.setProgramId(programId);
		bedCheckTime.setStrTime(time);
		
		return bedCheckTime;
	}
	
	/* [CONSTRUCTOR MARKER BEGIN] */

	public BedCheckTime() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BedCheckTime(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BedCheckTime(java.lang.Integer id, java.lang.Integer programId, java.util.Date time) {
		super(id, programId, time);
	}

	/* [CONSTRUCTOR MARKER END] */

	public String getStrTime() {
		return DateTimeFormatUtils.getStringFromTime(getTime());
	}

	// property adapted for view
	public void setStrTime(String strTime) {
		setTime(DateTimeFormatUtils.getTimeFromString(strTime));
	}

}