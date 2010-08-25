package org.oscarehr.common.hl7.v2.oscar_to_oscar;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.casemgmt.model.Prescription;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;

import oscar.util.BuildInfo;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v26.datatype.XAD;
import ca.uhn.hl7v2.model.v26.datatype.XON;
import ca.uhn.hl7v2.model.v26.datatype.XTN;
import ca.uhn.hl7v2.model.v26.group.OMP_O09_ORDER;
import ca.uhn.hl7v2.model.v26.group.OMP_O09_PATIENT;
import ca.uhn.hl7v2.model.v26.message.OMP_O09;
import ca.uhn.hl7v2.model.v26.segment.ORC;

public final class OmpO09 {
	private static final Logger logger = MiscUtils.getLogger();

	private OmpO09() {
		// not meant to be instantiated
	}

	public static OMP_O09 makeOmpO09(Clinic clinic, Provider provider, Demographic demographic, Prescription prescription, List<Drug> drugs) throws HL7Exception, UnsupportedEncodingException {
		OMP_O09 prescriptionMsg = new OMP_O09();

		DataTypeUtils.fillMsh(prescriptionMsg.getMSH(), new Date(), clinic.getClinicName(), "OMP", "O09", "OMP_O09", DataTypeUtils.HL7_VERSION_ID);
		DataTypeUtils.fillSft(prescriptionMsg.getSFT(), BuildInfo.getBuildTag(), BuildInfo.getBuildDate());

		OMP_O09_PATIENT patient=prescriptionMsg.getPATIENT();
		DataTypeUtils.fillPid(patient.getPID(), 1, demographic);

		DataTypeUtils.fillNte(prescriptionMsg.getNTE(0), "Rx Comments", null, prescription.getRxComments().getBytes());

		for (Drug drug : drugs)
		{
			int counter=0;
			
			OMP_O09_ORDER order=prescriptionMsg.getORDER(counter);
			fillOrc(order, prescription, provider, clinic);
			
// done to this point, need to continue after ORC

			counter++;
		}		
		
		return (prescriptionMsg);
	}

	private static void fillOrc(OMP_O09_ORDER order, Prescription prescription, Provider provider, Clinic clinic) throws HL7Exception {
	    ORC orc=order.getORC();
	    
	    // NW = new order request
	    orc.getOrderControl().setValue("NW");
	    
	    // the EI of the placer order number should be the prescription id
	    orc.getPlacerOrderNumber().getEntityIdentifier().setValue(prescription.getId().toString());
	    
	    // set provider
	    DataTypeUtils.fillXcn(orc.getOrderingProvider(0), provider);
	    
	    // set prescription date
	    orc.getOrderEffectiveDateTime().setValue(DataTypeUtils.getAsHl7FormattedString(prescription.getDate_prescribed()));
	    
	    // set facility name / doctors office name
	    XON xon=orc.getOrderingFacilityName(0);	    
		xon.getOrganizationName().setValue(StringUtils.trimToNull(clinic.getClinicName()));

		XAD xad=orc.getOrderingFacilityAddress(0);
		DataTypeUtils.fillXAD(xad, clinic, null, "O");
		
		XTN xtn=orc.getOrderingFacilityPhoneNumber(0);
		xtn.getUnformattedTelephoneNumber().setValue(StringUtils.trimToNull(clinic.getClinicPhone()));
	}


	public static void main(String... argv) throws Exception {
		// this is here just to test some of the above functions since
		// we are not using junit tests...

		Clinic clinic=new Clinic();
		clinic.setClinicName("test clinic");
		clinic.setClinicAddress("123 my street");
		clinic.setClinicCity("my city");
		clinic.setClinicProvince("bc");
		clinic.setClinicPhone("12345");
		
		Provider provider=new Provider();
		provider.setProviderNo("9999");
		provider.setLastName("p_lname");
		provider.setFirstName("p_fname");
		provider.setTitle("DR");
		
		Demographic demographic=new Demographic();
		demographic.setDemographicNo(8383);
		demographic.setLastName("d_lname");
		demographic.setFirstName("d_fname");
		demographic.setSex("M");
		demographic.setBirthDay(new GregorianCalendar(1902, 04, 05));
		
		Prescription prescription=new Prescription();
		prescription.setDate_prescribed(new Date());
		prescription.setId(777l);
		prescription.setRxComments("this is a fake prescription");
		
		ArrayList<Drug> drugs=new ArrayList<Drug>();
		
		OMP_O09 prescriptionMsg = makeOmpO09(clinic, provider, demographic, prescription, drugs);

		String messageString = OscarToOscarUtils.pipeParser.encode(prescriptionMsg);
		logger.info(messageString);
	}

}
