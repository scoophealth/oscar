package oscar.oscarLab.ca.all.upload.handlers.OscarToOscarHl7V2;

import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.DataTypeUtils;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.OscarAppointment;
import org.oscarehr.util.SpringUtils;

import oscar.appt.AppointmentDao;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v26.message.ADT_A09;
import ca.uhn.hl7v2.model.v26.segment.PID;

public final class AdtA09Handler {
	
	private static AppointmentDao appointmentDao=(AppointmentDao) SpringUtils.getBean("appointmentDao");
	
	public static void handle(ADT_A09 message) throws HL7Exception
	{
		// algorithm
		//----------
		// unparse the hl7 message so we know who's checking in
		// look at appointments today for anyone with matching demographic info
		// flip appointment status to H
		
		// the 2 relavent segments
		// PID|1||hhhhhhhhhh^^^^^^199704^199903^BC||last_name^first_name^^^^^L||19750607
		// PV1||P|||||||||^WAITING_ROOM
		
		Demographic demographic=DataTypeUtils.parsePid(message.getPID());
		
		// look for the patient four hours ago and four hours from now
		GregorianCalendar startTime=new GregorianCalendar();
		startTime.add(GregorianCalendar.HOUR_OF_DAY, -4);
		GregorianCalendar endTime=new GregorianCalendar();
		endTime.add(GregorianCalendar.HOUR_OF_DAY, 4);
		
		List<OscarAppointment> appointments=appointmentDao.findByDateRange(startTime.getTime(), endTime.getTime());
		
		// look through all appointments for matchin one
		// set the here flag on matching
		// of not match throw exception.
	}
}
