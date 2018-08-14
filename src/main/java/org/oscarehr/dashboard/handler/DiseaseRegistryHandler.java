package org.oscarehr.dashboard.handler;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

public class DiseaseRegistryHandler {

	private static Logger logger = MiscUtils.getLogger();
	
	public void addToDiseaseRegistry(int demographicNo, String icd9code) {
		logger.info(
			"Adding code (" + icd9code +
			") to disease registry for patient (" + demographicNo + ")"
		);
	}
}
