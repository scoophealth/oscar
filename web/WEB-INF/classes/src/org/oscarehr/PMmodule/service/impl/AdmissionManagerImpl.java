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
import org.oscarehr.PMmodule.model.BedDemographic;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramQueue;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.BedDemographicManager;

public class AdmissionManagerImpl implements AdmissionManager {

	private AdmissionDao dao;
	private ProgramDao programDao;
	private ProgramQueueDao programQueueDao;
	private ClientReferralDAO clientReferralDAO;
	private BedDemographicManager bedDemographicManager;
	
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
	
	public void setBedDemographicManager(BedDemographicManager bedDemographicManager) {
	    this.bedDemographicManager = bedDemographicManager;
    }
	
	public Admission getAdmission(String programId, Integer demographicNo) {
		return dao.getAdmission(Long.valueOf(programId), demographicNo);
	}

	public Admission getCurrentAdmission(String programId, Integer demographicNo) {
		return dao.getCurrentAdmission(Long.valueOf(programId), demographicNo);
	}

	public List getAdmissions() {
		return dao.getAdmissions();
	}

	public List getAdmissions(Integer demographicNo) {
		return dao.getAdmissions(demographicNo);
	}

	public List getCurrentAdmissions(Integer demographicNo) {
		return dao.getCurrentAdmissions(demographicNo);
	}

	public Admission getCurrentBedProgramAdmission(Integer demographicNo) {
		return dao.getCurrentBedProgramAdmission(programDao, demographicNo);
	}

	public List getCurrentServiceProgramAdmission(Integer demographicNo) {
		return dao.getCurrentServiceProgramAdmission(programDao, demographicNo);
	}

	public Admission getCurrentCommunityProgramAdmission(Integer demographicNo) {
		return dao.getCurrentCommunityProgramAdmission(programDao, demographicNo);
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

	public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes) throws ProgramFullException, AdmissionException {
		processAdmission(demographicNo, providerNo, program, dischargeNotes, admissionNotes, false, null);
	}

	public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, boolean tempAdmission) throws ProgramFullException, AdmissionException {
		processAdmission(demographicNo, providerNo, program, dischargeNotes, admissionNotes, tempAdmission, null);
	}

	public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, Date admissionDate) throws ProgramFullException, AdmissionException {
		processAdmission(demographicNo, providerNo, program, dischargeNotes, admissionNotes, false, admissionDate);
	}

	public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, boolean tempAdmission, Date admissionDate) throws ProgramFullException, AdmissionException {
		// see if there's room first
		if (program.getNumOfMembers().intValue() >= program.getMaxAllowed().intValue()) {
			throw new ProgramFullException();
		}

		// If admitting to bed program, discharge from old bed program
		if (program.getType().equalsIgnoreCase("bed") && !tempAdmission) {
			Admission fullAdmission = getCurrentBedProgramAdmission(demographicNo);

			// community?
			if (fullAdmission != null) {
				processDischarge(new Integer(fullAdmission.getProgramId().intValue()), new Integer(demographicNo), dischargeNotes);
			} else {
				fullAdmission = getCurrentCommunityProgramAdmission(demographicNo);
				if (fullAdmission != null) {
					processDischarge(new Integer(fullAdmission.getProgramId().intValue()), new Integer(demographicNo), dischargeNotes);
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
		newAdmission.setClientId(demographicNo);
		newAdmission.setProgramId(new Long(program.getId().intValue()));
		newAdmission.setProviderNo(Long.valueOf(providerNo));
		newAdmission.setTeamId(new Long(0));
		newAdmission.setAgencyId(new Long(0));
		newAdmission.setTemporaryAdmission(tempAdmission);
		
		saveAdmission(newAdmission);

		// Clear them from the queue, Update their referral
		ProgramQueue pq = programQueueDao.getActiveProgramQueue(new Long(program.getId().longValue()), demographicNo);
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

	public void processInitialAdmission(Integer demographicNo, String providerNo, Program program, String admissionNotes, Date admissionDate) throws ProgramFullException, AlreadyAdmittedException {
		// see if there's room first
		if (program.getNumOfMembers().intValue() >= program.getMaxAllowed().intValue()) {
			throw new ProgramFullException();
		}

		Admission admission = getCurrentAdmission(String.valueOf(program.getId()), demographicNo);
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
		newAdmission.setClientId(demographicNo);
		newAdmission.setProgramId(new Long(program.getId().intValue()));
		newAdmission.setProviderNo(Long.valueOf(providerNo));
		newAdmission.setTeamId(new Long(0));
		newAdmission.setAgencyId(new Long(0));
		saveAdmission(newAdmission);
	}

	public Admission getTemporaryAdmission(Integer demographicNo) {
		return dao.getTemporaryAdmission(demographicNo);
	}

	public List getCurrentTemporaryProgramAdmission(Integer demographicNo) {
		Admission admission = dao.getTemporaryAdmission(demographicNo);
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

	public void processDischarge(Integer programId, Integer demographicNo, String dischargeNotes) throws AdmissionException {
		Admission fullAdmission = getCurrentAdmission(String.valueOf(programId), demographicNo);

		if (fullAdmission == null) {
			throw new AdmissionException("Admission Record not found");
		}

		fullAdmission.setDischargeDate(new Date());
		fullAdmission.setDischargeNotes(dischargeNotes);
		fullAdmission.setAdmissionStatus("discharged");

		saveAdmission(fullAdmission);
	}

	public void processDischargeToCommunity(Integer communityProgramId, Integer demographicNo, String providerNo, String notes) throws AdmissionException {
		Admission currentBedAdmission = getCurrentBedProgramAdmission(demographicNo);

		if (currentBedAdmission != null) {
			processDischarge(currentBedAdmission.getProgramId().intValue(), demographicNo, notes);
			
			BedDemographic bedDemographic = bedDemographicManager.getBedDemographicByDemographic(demographicNo);
			
			if (bedDemographic != null) {
				bedDemographicManager.deleteBedDemographic(bedDemographic);
			}
		}

		Admission currentCommunityAdmission = getCurrentCommunityProgramAdmission(demographicNo);

		if (currentCommunityAdmission != null) {
			processDischarge(currentCommunityAdmission.getProgramId().intValue(), demographicNo, notes);
		}

		// Create and save admission object
		Admission admission = new Admission();
		admission.setAdmissionDate(new Date());
		admission.setAdmissionNotes(notes);
		admission.setAdmissionStatus("current");
		admission.setClientId(demographicNo);
		admission.setProgramId(communityProgramId.longValue());
		admission.setProviderNo(Long.valueOf(providerNo));
		admission.setTeamId(0L);
		admission.setAgencyId(0L);
		admission.setTemporaryAdmission(false);

		saveAdmission(admission);
	}
}
