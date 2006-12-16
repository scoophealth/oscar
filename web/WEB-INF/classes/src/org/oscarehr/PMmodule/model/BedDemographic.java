package org.oscarehr.PMmodule.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.oscarehr.PMmodule.model.base.BaseBedDemographic;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;

public class BedDemographic extends BaseBedDemographic {

	private static final long serialVersionUID = 1L;

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	public static BedDemographic create(Integer demographicNo, BedDemographicStatus bedDemographicStatus, String providerNo) {
		BedDemographicPK id = new BedDemographicPK();
		id.setDemographicNo(demographicNo);

		BedDemographic bedDemographic = new BedDemographic();
		bedDemographic.setId(id);
		bedDemographic.setBedDemographicStatusId(bedDemographicStatus.getId());
		bedDemographic.setProviderNo(providerNo);

		// set reservation start to today and reservation end to today + duration
		Date today = DateTimeFormatUtils.getToday(DATE_FORMAT);
		
		bedDemographic.setReservationStart(today);
		bedDemographic.setReservationEnd(DateTimeFormatUtils.getFuture(today, bedDemographicStatus.getDuration(), DATE_FORMAT));

		return bedDemographic;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */

	public BedDemographic() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BedDemographic(org.oscarehr.PMmodule.model.BedDemographicPK id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BedDemographic(org.oscarehr.PMmodule.model.BedDemographicPK id, java.lang.Integer bedDemographicStatusId, java.lang.String providerNo, boolean latePass, java.util.Date reservationStart, java.util.Date reservationEnd) {
		super(id, bedDemographicStatusId, providerNo, latePass, reservationStart, reservationEnd);
	}

	/* [CONSTRUCTOR MARKER END] */

	private String statusName;

	private String providerName;

	private String bedName;

	private String demographicName;

	private String roomName;

	private String programName;

	public boolean isExpired() {
		Date end = DateTimeFormatUtils.getDateFromDate(getReservationEnd(), DATE_FORMAT);
		Date today = DateTimeFormatUtils.getToday(DATE_FORMAT);
		
		return end.before(today);
	}
	
	public boolean isValidReservation() {
		Date start = DateTimeFormatUtils.getDateFromDate(getReservationStart(), DATE_FORMAT);
		Date end = DateTimeFormatUtils.getDateFromDate(getReservationEnd(), DATE_FORMAT);
		Date today = DateTimeFormatUtils.getToday(DATE_FORMAT);

		return start.before(end) && today.before(end);
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getBedName() {
		return bedName;
	}

	public void setBedName(String bedName) {
		this.bedName = bedName;
	}

	public String getDemographicName() {
		return demographicName;
	}

	public void setDemographicName(String demographicName) {
		this.demographicName = demographicName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	// property adapted for view
	public Integer getBedId() {
		return getId().getBedId();
	}

	// property adapted for view
	public void setBedId(Integer bedId) {
		getId().setBedId(bedId);
	}

	// property adapted for view
	public String getStrReservationEnd() {
		return DateTimeFormatUtils.getStringFromDate(getReservationEnd(), DATE_FORMAT);
	}

	// property adapted for view
	public void setStrReservationEnd(String strReservationEnd) {
		setReservationEnd(DateTimeFormatUtils.getDateFromString(strReservationEnd, DATE_FORMAT));
	}

	public void setReservationEnd(Integer duration) {
		if (duration != null && duration > 0) {
			Date startPlusDuration = DateTimeFormatUtils.getFuture(getReservationStart(), duration, DATE_FORMAT);
			Date end = DateTimeFormatUtils.getDateFromDate(getReservationEnd(), DATE_FORMAT);
			
			// start + duration > end
			if (startPlusDuration.after(end)) {
				setReservationEnd(startPlusDuration);
			}
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}