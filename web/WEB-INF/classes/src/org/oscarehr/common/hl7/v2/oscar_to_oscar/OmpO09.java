package org.oscarehr.common.hl7.v2.oscar_to_oscar;

import java.util.Date;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.util.BuildInfo;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v26.message.OMP_O09;

public final class OmpO09 {
	private static final Logger logger = MiscUtils.getLogger();

	private OmpO09() {
		// not meant to be instantiated
	}

	public static OMP_O09 makeOmpO09(String facilityName) throws DataTypeException {
		OMP_O09 prescriptionMsg = new OMP_O09();

		DataTypeUtils.fillMsh(prescriptionMsg.getMSH(), new Date(), facilityName, "OMP", "O09", "OMP_O09", DataTypeUtils.HL7_VERSION_ID);
		DataTypeUtils.fillSft(prescriptionMsg.getSFT(), BuildInfo.getBuildTag(), BuildInfo.getBuildDate());

		return (prescriptionMsg);
	}

	public static void main(String... argv) throws Exception {
		// this is here just to test some of the above functions since
		// we are not using junit tests...

		OMP_O09 prescriptionMsg = makeOmpO09("facility name");

		String messageString = OscarToOscarUtils.pipeParser.encode(prescriptionMsg);
		logger.info(messageString);
	}

}
