package org.oscarehr.PMmodule.task;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ProviderManager;

public class ErProgramDischargeTask extends TimerTask {

	private static final Log log = LogFactory.getLog(ErProgramDischargeTask.class);

	private ProviderManager providerManager;

	private AdmissionManager admissionManager;

	private int lengthOfStay = 60 * 24;

	public void setProviderManager(ProviderManager mgr) {
		this.providerManager = mgr;
	}

	public void setAdmissionManager(AdmissionManager mgr) {
		this.admissionManager = mgr;
	}

	public void setLengthOfStay(int minutes) {
		this.lengthOfStay = minutes;
	}

	public void run() {
		log.debug("running ErProgramDischargeTask");
		
		// get all "ER" Service programs
		List providers = providerManager.getProvidersByType("er_clerk");
		for (Iterator i = providers.iterator(); i.hasNext();) {
			Provider provider = (Provider) i.next();

			// they should only have 1 service program
			List programDomain = providerManager.getProgramDomain(provider.getProviderNo());
			ProgramProvider programProvider = (ProgramProvider) programDomain.get(0);

			if (programProvider != null) {
				// loop clients in the ER program
				List programAdmissions = admissionManager.getCurrentAdmissionsByProgramId(programProvider.getProgramId().toString());
				
				for (Iterator j = programAdmissions.iterator(); j.hasNext();) {
					Admission admission = (Admission) j.next();
					
					// check admission date, determine if we should discharge
					Date admissionDate = admission.getAdmissionDate();
					Date currentDate = new Date();
					long diff = currentDate.getTime() - admissionDate.getTime();
					
					log.debug("difference = " + diff);
					
					if (diff > (lengthOfStay * 60 * 1000)) {
						admission.setDischargeDate(new Date());
						admission.setDischargeNotes("Auto-Discharge");
						admission.setAdmissionStatus("discharged");
						admissionManager.saveAdmission(admission);
						
						log.debug("discharged");
					}
				}
			}
		}
	}

}