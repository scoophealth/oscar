package org.oscarehr.common.hl7.v2.oscar_to_oscar;

import java.util.Date;
import java.util.GregorianCalendar;

import org.oscarehr.common.model.Demographic;

import oscar.util.BuildInfo;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v25.group.ORU_R01_PATIENT_RESULT;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import ca.uhn.hl7v2.model.v25.segment.NTE;
import ca.uhn.hl7v2.model.v25.segment.ROL;

public final class OruR01 {
	
	/**
	 * This method is essentially used to make an ORU_R01 containning pretty much any random data.
	 */
	public static ORU_R01 makeOruR01(String facilityName, Demographic demographic) throws HL7Exception
	{
		ORU_R01 observationMsg=new ORU_R01();

		DataTypeUtils.fillMsh(observationMsg.getMSH(), new Date(), facilityName, "ORU", "R01", "ORU_R01", "2.5");
		DataTypeUtils.fillSft(observationMsg.getSFT(), BuildInfo.getBuildTag(), BuildInfo.getBuildDate());

		ORU_R01_PATIENT_RESULT patientResult=observationMsg.getPATIENT_RESULT(0);
		DataTypeUtils.fillPid(patientResult.getPATIENT().getPID(), 1, demographic);

		ORU_R01_ORDER_OBSERVATION orderObservation=patientResult.getORDER_OBSERVATION(0);
		
		// need to fill obr with provider information is returning to / receiving this data
// NEED TO FIX THIS IT SHOULDN"T BE BLANK, IT SHOULD HAVE THE RECEIVING PROVIDER 
		DataTypeUtils.fillBlankOBR(orderObservation.getOBR());

		// use NTE's to send random data, each nte is 64k only so 
		// we can break items into multiple nte segments
		// we'll use the NTE type to distinguish the content / type / and assembly
		// Example : type.Text="WCB Form", type.NameOfCodingSystem="pdf", 
		// multiple duplicate type.text+nameOfCodingSystem will be concatenated
		NTE nte;

		// use ROL for the provider who is sending this data
		ROL rol;
		
		return(observationMsg);
	}
	
	public static void main(String... argv) throws Exception
	{
		Demographic demographic=new Demographic();
		demographic.setLastName("test LN");
		demographic.setLastName("test FN");
		demographic.setBirthDay(new GregorianCalendar(1960, 2, 3));
		
		ORU_R01 observationMsg=makeOruR01("facility name", demographic);
		
		System.err.println(OscarToOscarUtils.pipeParser.encode(observationMsg));
	}
}
