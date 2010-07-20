package oscar.oscarLab.ca.all.upload.handlers.OscarToOscarHl7V2;

import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.List;

import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.DataTypeUtils;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.OscarAppointment;
import org.oscarehr.util.SpringUtils;

import oscar.appt.AppointmentDao;
import oscar.appt.status.model.AppointmentStatus;
import oscar.dao.ProviderDao;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v26.message.ADT_A09;
import ca.uhn.hl7v2.model.v26.segment.PV1;

public final class AdtA09Handler {
	
	private static final String WAITING_ROOM = "WAITING_ROOM";
	private static final String PATIENT_CLASS = "P";
	private static AppointmentDao appointmentDao=(AppointmentDao) SpringUtils.getBean("appointmentDao");
	private static DemographicDao demographicDao=(DemographicDao) SpringUtils.getBean("demographicDao");
	
	public static void handle(ADT_A09 message) throws HL7Exception, SQLException
	{
		// algorithm
		//----------
		// unparse the hl7 message so we know who's checking in and make sure it's a check in
		// look at appointments today for anyone with matching demographic info
		// flip appointment status to H
		
		// the 2 relavent segments
		// PID|1||hhhhhhhhhh^^^^^^199704^199903^BC||last_name^first_name^^^^^L||19750607
		// PV1||P|||||||||^WAITING_ROOM
		
		checkPv1(message.getPV1());
		Demographic demographic=DataTypeUtils.parsePid(message.getPID());
		
		// look for the patient four hours ago and four hours from now
		GregorianCalendar startTime=new GregorianCalendar();
		startTime.add(GregorianCalendar.HOUR_OF_DAY, -4);
		GregorianCalendar endTime=new GregorianCalendar();
		endTime.add(GregorianCalendar.HOUR_OF_DAY, 4);
		
		List<OscarAppointment> appointments=appointmentDao.findByDateRange(startTime.getTime(), endTime.getTime());
		
		// look through all appointments for matching demographic
		// set the here flag on matching
		// of not match throw exception.
		
		for (OscarAppointment appointment : appointments)
		{
			if (demographicMatches(appointment, demographic))
			{
				switchAppointmentStatus(appointment);
				return;
			}
		}
		
		throw(new IllegalStateException("Some one checking in who has no appointment."));
	}

	/**
	 * Check to make sure the PV1 is a check in as expected.
	 */
	private static void checkPv1(PV1 pv1) {
		String patientClass=pv1.getPatientClass().getValue();
		if (!PATIENT_CLASS.equals(patientClass)) throw(new UnsupportedOperationException("PV1 doesn't match expectations : patientClass="+patientClass));
		
		String room=pv1.getTemporaryLocation().getRoom().getValue();
		if (!WAITING_ROOM.equals(room)) throw(new UnsupportedOperationException("PV1 doesn't match expectations : room="+room));
    }

	private static boolean demographicMatches(OscarAppointment appointment, Demographic demographic) {
		Demographic appointmentDemographic=demographicDao.getDemographicById(appointment.getDemographic_no());

		return(demographicMatches(appointmentDemographic, demographic));
    }

	private static boolean demographicMatches(Demographic appointmentDemographic, Demographic demographic) {
		// the expectation is that the demographic should have at least 
		// lastname, firstname, birthday, health number, health province, gender
		// there's a chance we may need to relax the birthday requirement to only need birth year/month as some or all BC cards may have no birthdays on them.
		if (demographic.getLastName()==null || demographic.getFirstName()==null || demographic.getBirthDay()==null || demographic.getSex()==null || demographic.getHin()==null || demographic.getHcType()==null) return(false);

		if (!demographic.getLastName().equals(appointmentDemographic.getLastName())) return(false);
		if (!demographic.getFirstName().equals(appointmentDemographic.getFirstName())) return(false);
		if (!demographic.getSex().equals(appointmentDemographic.getSex())) return(false);
		if (!demographic.getBirthDay().equals(appointmentDemographic.getBirthDay())) return(false);
		if (!demographic.getHin().equals(appointmentDemographic.getHin())) return(false);
		if (!demographic.getHcType().equals(appointmentDemographic.getHcType())) return(false);
		
		// for people in ontario, if there's a hc version, it needs to match too.
		if (demographic.getVer()!=null && !demographic.getVer().equals(appointmentDemographic.getVer())) return(false);

		return(true);
    }

	private static void switchAppointmentStatus(OscarAppointment appointment) throws SQLException {
	    ProviderDao.updateAppointmentStatus(appointment.getAppointment_no(), AppointmentStatus.APPOINTMENT_STATUS_HERE);
    }

}
