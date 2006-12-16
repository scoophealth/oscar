package org.oscarehr.PMmodule.task;

import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.exception.IntegratorException;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.IntegratorManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;

public class IntegratorUpdateTask extends TimerTask {

	private static final Log log = LogFactory.getLog(IntegratorUpdateTask.class);

	private IntegratorManager integratorManager;

	private AdmissionManager admissionManager;

	private ProgramManager programManager;

	private ProviderManager providerManager;

	private ClientManager clientManager;

	public void setIntegratorManager(IntegratorManager mgr) {
		this.integratorManager = mgr;
	}

	public void setAdmissionManager(AdmissionManager mgr) {
		this.admissionManager = mgr;
	}

	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}

	public void setProviderManager(ProviderManager mgr) {
		this.providerManager = mgr;
	}

	public void setClientManager(ClientManager mgr) {
		this.clientManager = mgr;
	}

	public void run() {
		if (!integratorManager.isEnabled()) {
			log.debug("integrator is not enabled");
			return;
		}
		
		log.info("updating integrator");

		try {
			integratorManager.refreshAdmissions(admissionManager.getAdmissions());
			log.info("Admissions updated");
		} catch (IntegratorException e) {
			log.error(e);
		}

		try {
			integratorManager.refreshPrograms(programManager.getProgramsByAgencyId("0"));
			log.info("Programs refreshed");
		} catch (IntegratorException e) {
			log.error(e);
		}

		try {
			integratorManager.refreshProviders(providerManager.getProviders());
			log.info("Providers refereshed");
		} catch (IntegratorException e) {
			log.error(e);
		}


		try {
			integratorManager.refreshReferrals(clientManager.getReferrals());
			log.info("referrals refreshed");
		} catch (IntegratorException e) {
			log.error(e);
		}

		log.info("integrator update task complete)");
	}

}