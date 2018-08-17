package org.oscarehr.dashboard.handler;

import java.util.Date;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class DiseaseRegistryHandler {

	private static Logger logger = MiscUtils.getLogger();

	private DxresearchDAO dao = (DxresearchDAO) SpringUtils.getBean("DxresearchDAO");

	public void addToDiseaseRegistry(int demographicNo, String icd9code, String providerNo) {
		// XXX is there an API for getting this reliably? (other than `new Icd9().getCodingSystem()`)
		String codingSystem = "icd9";

		boolean activeEntryExists = dao.activeEntryExists(demographicNo, codingSystem, icd9code);
		if (activeEntryExists) {
			logger.info(
				"Patient (" + demographicNo +
				") already has active entry for (" +
				icd9code + ")"
			);
			return;
		}

		Dxresearch dx = new Dxresearch();
		dx.setStartDate(new Date());
		dx.setCodingSystem(codingSystem);
		dx.setDemographicNo(demographicNo);
		dx.setDxresearchCode(icd9code);
		dx.setStatus('A');
		dx.setProviderNo(providerNo);

		dao.persist(dx);

		logger.info(
			"Added code (" + icd9code +
			") to disease registry for patient (" + demographicNo + ")" +
			" with provider no (" + providerNo + ")"
		);
	}
}
