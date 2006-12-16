package org.oscarehr.PMmodule.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.oscarehr.PMmodule.dao.AdmissionDao;
import org.oscarehr.PMmodule.dao.ClientReferralDAO;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProgramQueueDao;
import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.exception.AlreadyAdmittedException;
import org.oscarehr.PMmodule.exception.ProgramFullException;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.AdmissionSearchBean;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramQueue;
import org.oscarehr.PMmodule.service.AdmissionManager;

public class AdmissionManagerImpl implements AdmissionManager {

	private AdmissionDao dao;
	
	private ProgramDao programDao;

	private ProgramQueueDao programQueueDao;

	private ClientReferralDAO clientReferralDAO;

	public void setAdmissionDao(AdmissionDao dao) {
		this.dao = dao;
	}

	public void setProgramDao(ProgramDao programDao) {
	    this.programDao = programDao;
    }
	
	public void setProgramQueueDao(ProgramQueueDao dao) {
		this.programQueueDao = dao;
	}

	public void setClientReferralDAO(ClientReferralDAO dao) {
		this.clientReferralDAO = dao;
	}

	public Admission getAdmission(String programId, String demographicNo) {
		return dao.getAdmission(Long.valueOf(programId), Integer.valueOf(demographicNo));
	}

	public Admission getCurrentAdmission(String programId, String demographicNo) {
		return dao.getCurrentAdmission(Long.valueOf(programId), Integer.valueOf(demographicNo));
	}

	public List getAdmissions() {
		return dao.getAdmissions();
	}

	public List getAdmissions(String demographicNo) {
		return dao.getAdmissions(Integer.valueOf(demographicNo));
	}

	public List getCurrentAdmissions(String demographicNo) {
		return dao.getCurrentAdmissions(Integer.valueOf(demographicNo));
	}

	public Admission getCurrentBedProgramAdmission(String demographicNo) {
		return dao.getCurrentBedProgramAdmission(programDao, Integer.valueOf(demographicNo));
	}

	public List getCurrentServiceProgramAdmission(String demographicNo) {
		return dao.getCurrentServiceProgramAdmission(programDao, Integer.valueOf(demographicNo));
	}

	public Admission getCurrentCommunityProgramAdmission(String demographicNo) {
		return dao.getCurrentCommunityProgramAdmission(programDao, Integer.valueOf(demographicNo));
	}

	public List getCurrentAdmissionsByProgramId(String programId) {
		return dao.getCurrentAdmissionsByProgramId(Long.valueOf(programId));
	}

	public Admission getAdmission(Long id) {
		return dao.getAdmission(id);
	}

	public void saveAdmission(Admission admission) {
		dao.saveAdmission(admission);
	}

	public void processAdmission(String demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes) throws ProgramFullException, AdmissionException {
		processAdmission(demographicNo, providerNo, program, dischargeNotes, admissionNotes, false, null);
	}

	public void processAdmission(String demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, boolean tempAdmission) throws ProgramFullException, AdmissionException {
		processAdmission(demographicNo, providerNo, program, dischargeNotes, admissionNotes, tempAdmission, null);
	}

	public void processAdmission(String demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, Date admissionDate) throws ProgramFullException, AdmissionException {
		processAdmission(demographicNo, providerNo, program, dischargeNotes, admissionNotes, false, admissionDate);
	}

	public void processAdmission(String demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, boolean tempAdmission, Date admissionDate) throws ProgramFullException, AdmissionException {
		// see if there's room first
		if (program.getNumOfMembers().intValue() >= program.getMaxAllowed().intValue()) {
			throw new ProgramFullException();
		}

		// If admitting to bed program, Discharge from old bed program
		if (program.getType().equalsIgnoreCase("bed") && !tempAdmission) {
			Admission fullAdmission = getCurrentBedProgramAdmission(demographicNo);

			// community?
			if (fullAdmission != null) {
				this.processDischarge(new Integer(fullAdmission.getProgramId().intValue()), new Integer(demographicNo), dischargeNotes);
			} else {
				fullAdmission = this.getCurrentCommunityProgramAdmission(demographicNo);
				if (fullAdmission != null) {
					this.processDischarge(new Integer(fullAdmission.getProgramId().intValue()), new Integer(demographicNo), dischargeNotes);
				}
			}
		}

		// Can only be in a single temporary bed program.
		if (tempAdmission && getTemporaryAdmission(demographicNo) != null) {
			throw new AdmissionException("Already in a temporary program.");
		}

		// Create/Save admission object
		Admission newAdmission = new Admission();
		if (admissionDate != null) {
			newAdmission.setAdmissionDate(admissionDate);
		} else {
			newAdmission.setAdmissionDate(new Date());
		}
		
		newAdmission.setAdmissionNotes(admissionNotes);
		newAdmission.setAdmissionStatus("current");
		newAdmission.setClientId(Integer.valueOf(demographicNo));
		newAdmission.setProgramId(new Long(program.getId().intValue()));
		newAdmission.setProviderNo(Long.valueOf(providerNo));
		newAdmission.setTeamId(new Long(0));
		newAdmission.setAgencyId(new Long(0));
		newAdmission.setTemporaryAdmission(tempAdmission);
		
		saveAdmission(newAdmission);

		// Clear them from the queue, Update their referral
		ProgramQueue pq = programQueueDao.getActiveProgramQueue(new Long(program.getId().longValue()), Integer.valueOf(demographicNo));
		if (pq != null) {
			pq.setStatus("admitted");
			programQueueDao.saveProgramQueue(pq);

			// is there a referral
			if (pq.getReferralId() != null && pq.getReferralId().longValue() > 0) {
				ClientReferral referral = clientReferralDAO.getClientReferral(pq.getReferralId());
				referral.setStatus("complete");
				referral.setCompletionDate(new Date());
				referral.setCompletionNotes(admissionNotes);
				clientReferralDAO.saveClientReferral(referral);
			}
		}
	}

	public void processInitialAdmission(String demographicNo, String providerNo, Program program, String admissionNotes, Date admissionDate) throws ProgramFullException, AlreadyAdmittedException {
		// see if there's room first
		if (program.getNumOfMembers().intValue() >= program.getMaxAllowed().intValue()) {
			throw new ProgramFullException();
		}

		Admission admission = this.getCurrentAdmission(String.valueOf(program.getId()), demographicNo);
		if (admission != null) {
			throw new AlreadyAdmittedException();
		}

		Admission newAdmission = new Admission();
		if (admissionDate == null) {
			newAdmission.setAdmissionDate(new Date());
		} else {
			newAdmission.setAdmissionDate(admissionDate);
		}
		newAdmission.setAdmissionNotes(admissionNotes);
		newAdmission.setAdmissionStatus("current");
		newAdmission.setClientId(Integer.valueOf(demographicNo));
		newAdmission.setProgramId(new Long(program.getId().intValue()));
		newAdmission.setProviderNo(Long.valueOf(providerNo));
		newAdmission.setTeamId(new Long(0));
		newAdmission.setAgencyId(new Long(0));
		saveAdmission(newAdmission);
	}

	public Admission getTemporaryAdmission(String demographicNo) {
		return dao.getTemporaryAdmission(Long.valueOf(demographicNo));
	}

	public List getCurrentTemporaryProgramAdmission(String demographicNo) {
		Admission admission = dao.getTemporaryAdmission(Long.valueOf(demographicNo));
		if (admission != null) {
			List results = new ArrayList();
			results.add(admission);
			return results;
		}
		return null;
	}

	public List search(AdmissionSearchBean searchBean) {
		return dao.search(searchBean);
	}

	public void processDischarge(Integer programId, Integer clientId, String dischargeNotes) throws AdmissionException {
		Admission fullAdmission = getCurrentAdmission(String.valueOf(programId), String.valueOf(clientId));

		if (fullAdmission == null) {
			throw new AdmissionException("Admission Record not found");
		}

		fullAdmission.setDischargeDate(new Date());
		fullAdmission.setDischargeNotes(dischargeNotes);
		fullAdmission.setAdmissionStatus("discharged");

		saveAdmission(fullAdmission);
	}

	public void processDischargeToCommunity(Integer programId, Integer clientId, String providerNo, String notes) throws AdmissionException {
		Admission currentBedAdmission = getCurrentBedProgramAdmission(clientId.toString());

		if (currentBedAdmission != null) {
			processDischarge(currentBedAdmission.getProgramId().intValue(), clientId, notes);
		}

		Admission currentCommunityAdmission = getCurrentCommunityProgramAdmission(clientId.toString());

		if (currentCommunityAdmission != null) {
			processDischarge(currentCommunityAdmission.getProgramId().intValue(), clientId, notes);
		}

		// Create and save admission object
		Admission newAdmission = new Admission();
		newAdmission.setAdmissionDate(new Date());
		newAdmission.setAdmissionNotes(notes);
		newAdmission.setAdmissionStatus("current");
		newAdmission.setClientId(clientId);
		newAdmission.setProgramId(new Long(programId));
		newAdmission.setProviderNo(Long.valueOf(providerNo));
		newAdmission.setTeamId(new Long(0));
		newAdmission.setAgencyId(new Long(0));
		newAdmission.setTemporaryAdmission(false);

		saveAdmission(newAdmission);
	}
}
