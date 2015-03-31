/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.oscarLab.ca.all.upload.handlers.OscarToOscarHl7V2;

import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.DataTypeUtils;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.AppointmentStatus;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v26.datatype.CX;
import ca.uhn.hl7v2.model.v26.message.ADT_A09;
import ca.uhn.hl7v2.model.v26.segment.PID;
import ca.uhn.hl7v2.model.v26.segment.PV1;

public final class AdtA09Handler {
	private static Logger logger = MiscUtils.getLogger();

	private static final String WAITING_ROOM = "WAITING_ROOM";
	private static final String PATIENT_CLASS = "P";
	private static OscarAppointmentDao appointmentDao = (OscarAppointmentDao) SpringUtils.getBean("oscarAppointmentDao");
	private static DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
	private static int checkInLateAllowance = Integer.parseInt(OscarProperties.getInstance().getProperty(AdtA09Handler.class.getSimpleName() + ".CHECK_IN_LATE_ALLOWANCE"));
	private static int checkInEarlyAllowance = Integer.parseInt(OscarProperties.getInstance().getProperty(AdtA09Handler.class.getSimpleName() + ".CHECK_IN_EARLY_ALLOWANCE"));

	public static void handle(LoggedInInfo loggedInInfo, ADT_A09 message) throws HL7Exception {
		// algorithm
		// ----------
		// unparse the hl7 message so we know who's checking in and make sure it's a check in
		// look at appointments today for anyone with matching demographic info
		// flip appointment status to H

		// the 2 relavent segments
		// PID|1||hhhhhhhhhh^^^^^^199704^199903^BC||last_name^first_name^^^^^L||19750607
		// PV1||P|||||||||^WAITING_ROOM

		checkPv1(message.getPV1());

		// look for the patient four hours ago and four hours from now
		GregorianCalendar startTime = new GregorianCalendar();
		startTime.add(GregorianCalendar.HOUR_OF_DAY, -checkInLateAllowance);
		GregorianCalendar endTime = new GregorianCalendar();
		// add 24 because it's at the start of the day and it's exclusive of that day
		endTime.add(GregorianCalendar.HOUR_OF_DAY, 24 + checkInEarlyAllowance);

		// so this only sorts out the day ranges i.e. we could have just done a select from today but this way we bridge 
		// people checking in at 11:50pm for an appointment at 1:00am.
		List<Appointment> appointments = appointmentDao.findByDateRange(startTime.getTime(), endTime.getTime());
		logger.debug("Qualifying appointments found : " + appointments.size());

		// adt messages can come in the form of pull PID's or just the chart number to switch
		// if the chart number exists then use the chart number, otherwise match demographic record.
		String chartNo = getChartNo(message);
		if (chartNo != null) {
			switchMatchingAppointment(loggedInInfo, chartNo, appointments);
		} else {
			Demographic demographic = DataTypeUtils.parsePid(message.getPID());
			switchMatchingAppointment(loggedInInfo, demographic, appointments);
		}

	}

	private static String getChartNo(ADT_A09 message) throws HL7Exception {
		PID pid = message.getPID();
		CX cx = pid.getPatientIdentifierList(0);
		if (cx.getIdentifierTypeCode() != null && DataTypeUtils.CHART_NUMBER.equals(cx.getIdentifierTypeCode().getValue())) {
			return (StringUtils.trimToNull(cx.getIDNumber().getValue()));
		} else {
			return (null);
		}
	}

	private static void switchMatchingAppointment(LoggedInInfo loggedInInfo, Demographic demographic, List<Appointment> appointments) {
		// look through all appointments for matching demographic
		// set the here flag on matching
		// of not match throw exception.

		for (Appointment appointment : appointments) {
			logger.debug("checking appointment : " + appointment.getId());

			if (!isValidAppointmentStatusForMatch(appointment)) continue;

			if (demographicMatches(loggedInInfo, appointment, demographic)) {
				switchAppointmentStatus(appointment);
				return;
			}
		}

		throw (new IllegalStateException("Some one checking in who has no appointment."));
	}

	private static void switchMatchingAppointment(LoggedInInfo loggedInInfo, String chartNo, List<Appointment> appointments) {
		// look through all appointments for matching demographic
		// set the here flag on matching
		// of not match throw exception.

		for (Appointment appointment : appointments) {
			logger.debug("checking appointment : " + appointment.getId());

			if (!isValidAppointmentStatusForMatch(appointment)) continue;

			if (chartNoMatches(loggedInInfo, appointment, chartNo)) {
				switchAppointmentStatus(appointment);
				return;
			}
		}

		throw (new IllegalStateException("Some one checking in who has no appointment."));
	}

	private static boolean isValidAppointmentStatusForMatch(Appointment appointment) {
		if ("H".equals(appointment.getStatus())) return (false);
		else if ("N".equals(appointment.getStatus())) return (false);
		else if ("C".equals(appointment.getStatus())) return (false);
		else if ("B".equals(appointment.getStatus())) return (false);

		return (true);
	}

	/**
	 * Check to make sure the PV1 is a check in as expected.
	 */
	private static void checkPv1(PV1 pv1) {
		String patientClass = pv1.getPatientClass().getValue();
		if (!PATIENT_CLASS.equals(patientClass)) throw (new UnsupportedOperationException("PV1 doesn't match expectations : patientClass=" + patientClass));

		String room = pv1.getTemporaryLocation().getRoom().getValue();
		if (!WAITING_ROOM.equals(room)) throw (new UnsupportedOperationException("PV1 doesn't match expectations : room=" + room));
	}

	private static boolean chartNoMatches(LoggedInInfo loggedInInfo, Appointment appointment, String chartNo) {
		Demographic appointmentDemographic = demographicManager.getDemographic(loggedInInfo, appointment.getDemographicNo());

		if (appointmentDemographic == null) {
			logger.debug("appointmentDemographic was null, appointment_no=" + appointment.getId());
			return (false);
		}

		return (chartNo.equals(appointmentDemographic.getChartNo()));
	}

	private static boolean demographicMatches(LoggedInInfo loggedInInfo, Appointment appointment, Demographic demographic) {
		Demographic appointmentDemographic = demographicManager.getDemographic(loggedInInfo, appointment.getDemographicNo());
		return (demographicMatches(appointmentDemographic, demographic));
	}

	private static boolean demographicMatches(Demographic appointmentDemographic, Demographic demographic) {
		// the expectation is that the demographic should have at least
		// lastname, firstname, health number, health province, (gender/birthday are not ubiquitously available)
		// there's a chance we may need to relax the birthday requirement to only need birth year/month as some or all BC cards may have no birthdays on them.
		// because of name truncation, we will match only first and last initials of the names.

		if (appointmentDemographic == null) {
			logger.debug("appointmentDemographic was null");
			return (false);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Checking demographic : " + ReflectionToStringBuilder.toString(demographic));
			logger.debug("Against appointmentDemographic : " + ReflectionToStringBuilder.toString(appointmentDemographic));
		}

		if (demographic.getLastName() == null || demographic.getFirstName() == null || demographic.getHin() == null || demographic.getHcType() == null) {
			logger.debug("fail : demographic has null data");
			return (false);
		}

		char firstLetter = demographic.getLastName().toLowerCase().charAt(0);
		if (firstLetter != appointmentDemographic.getLastName().toLowerCase().charAt(0)) {
			logger.debug("fail : last name");
			return (false);
		}

		firstLetter = demographic.getFirstName().toLowerCase().charAt(0);
		if (firstLetter != appointmentDemographic.getFirstName().toLowerCase().charAt(0)) {
			logger.debug("fail : first name");
			return (false);
		}

		if (!demographic.getHin().equals(appointmentDemographic.getHin())) {
			logger.debug("fail : hin");
			return (false);
		}

		if (!demographic.getHcType().equalsIgnoreCase(appointmentDemographic.getHcType())) {
			logger.debug("fail : hc type");
			return (false);
		}

		// bc has no birthday
		if (demographic.getBirthDay() != null && !demographic.getBirthDay().equals(appointmentDemographic.getBirthDay())) {
			logger.debug("fail : birthday");
			return (false);
		}

		// BC has no gender
		if (demographic.getSex() != null && !demographic.getSex().equalsIgnoreCase(appointmentDemographic.getSex())) {
			logger.debug("fail : gender");
			return (false);
		}

		// for people in ontario, if there's a hc version, it needs to match too.
		if (demographic.getVer() != null && !demographic.getVer().equalsIgnoreCase(appointmentDemographic.getVer())) {
			logger.debug("fail : hc ver");
			return (false);
		}

		logger.debug("successful match");
		return (true);
	}

	private static void switchAppointmentStatus(Appointment appointment) {
		appointment.setStatus(AppointmentStatus.APPOINTMENT_STATUS_HERE);
		appointmentDao.merge(appointment);
	}
}
