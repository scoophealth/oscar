/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.PMmodule.service;

import org.oscarehr.PMmodule.dao.*;
import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.exception.AlreadyAdmittedException;
import org.oscarehr.PMmodule.exception.ProgramFullException;
import org.oscarehr.PMmodule.exception.ServiceRestrictionException;
import org.oscarehr.PMmodule.model.*;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdmissionManager {

	private AdmissionDao dao;
	private ProgramDao programDao;
	private ProgramQueueDao programQueueDao;
	private ClientReferralDAO clientReferralDAO;
	private BedDemographicManager bedDemographicManager;
	private ProgramClientStatusDAO programClientStatusDAO;
	private ClientRestrictionManager clientRestrictionManager;

    public List getAdmissions_archiveView(String programId, Integer demographicNo) {
		return dao.getAdmissions_archiveView(Integer.valueOf(programId), demographicNo);
	}
	
	public Admission getAdmission(String programId, Integer demographicNo) {
		return dao.getAdmission(Integer.valueOf(programId), demographicNo);
	}

	public Admission getCurrentAdmission(String programId, Integer demographicNo) {
		return dao.getCurrentAdmission(Integer.valueOf(programId), demographicNo);
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
		return dao.getCurrentAdmissionsByProgramId(Integer.valueOf(programId));
	}

    public Admission getAdmission(Long id) {
		return dao.getAdmission(id);
	}

	public void saveAdmission(Admission admission) {
		dao.saveAdmission(admission);
	}

	public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes) throws ProgramFullException, AdmissionException, ServiceRestrictionException {
		processAdmission(demographicNo, providerNo, program, dischargeNotes, admissionNotes, false, null);
	}

	public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, boolean tempAdmission) throws ProgramFullException, AdmissionException, ServiceRestrictionException {
		processAdmission(demographicNo, providerNo, program, dischargeNotes, admissionNotes, tempAdmission, null);
	}

	public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, Date admissionDate) throws ProgramFullException, AdmissionException, ServiceRestrictionException {
		processAdmission(demographicNo, providerNo, program, dischargeNotes, admissionNotes, false, admissionDate);
	}

	public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, boolean tempAdmission, Date admissionDate) throws ProgramFullException, AdmissionException, ServiceRestrictionException {
		// see if there's room first
		if (program.getNumOfMembers().intValue() >= program.getMaxAllowed().intValue()) {
			throw new ProgramFullException();
		}

        // check if there's a service restriction in place on this individual for this program
        ProgramClientRestriction restrInPlace = clientRestrictionManager.checkClientRestriction(program.getId(), demographicNo, new Date());
        if (restrInPlace != null) {
            throw new ServiceRestrictionException("service restriction in place", restrInPlace);
        }

        // If admitting to bed program, discharge from old bed program
		if (program.getType().equalsIgnoreCase("bed") && !tempAdmission) {
			Admission fullAdmission = getCurrentBedProgramAdmission(demographicNo);

			// community?
			if (fullAdmission != null) {
				processDischarge(new Integer(fullAdmission.getProgramId().intValue()), new Integer(demographicNo), dischargeNotes, "");
			} else {
				fullAdmission = getCurrentCommunityProgramAdmission(demographicNo);
				if (fullAdmission != null) {
					processDischarge(new Integer(fullAdmission.getProgramId().intValue()), new Integer(demographicNo), dischargeNotes, "0");
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
		newAdmission.setAdmissionStatus(Admission.STATUS_CURRENT);
		newAdmission.setClientId(demographicNo);
		newAdmission.setProgramId(program.getId());
		newAdmission.setProviderNo(Long.valueOf(providerNo));
		newAdmission.setTeamId(0);
		newAdmission.setAgencyId(new Long(0));
		newAdmission.setTemporaryAdmission(tempAdmission);
		
		//keep the client status if he was in the same program with it.
		Integer clientStatusId = dao.getLastClientStatusFromAdmissionByProgramIdAndClientId(Integer.valueOf(program.getId()),demographicNo);
				
		//check if the client status is valid/existed in program_clientStatus
		if(programClientStatusDAO.getProgramClientStatus(clientStatusId.toString()) == null)
			clientStatusId = 0;
				
		newAdmission.setClientStatusId(clientStatusId);					

		saveAdmission(newAdmission);

		// Clear them from the queue, Update their referral
		ProgramQueue pq = programQueueDao.getActiveProgramQueue(program.getId().longValue(), (long) demographicNo);
		if (pq != null) {
			pq.setStatus(ProgramQueue.STATUS_ADMITTED);
			programQueueDao.saveProgramQueue(pq);

			// is there a referral
			if (pq.getReferralId() != null && pq.getReferralId().longValue() > 0) {
				ClientReferral referral = clientReferralDAO.getClientReferral(pq.getReferralId());
				referral.setStatus(ClientReferral.STATUS_CURRENT);
				referral.setCompletionDate(new Date());
				referral.setCompletionNotes(admissionNotes);
				clientReferralDAO.saveClientReferral(referral);
			}
		}
		
		
		//if they are in a service program linked to this bed program, discharge them from that service program
		//TODO:
		if(program.getType().equalsIgnoreCase("Bed")) {
			List<Program> programs = programDao.getLinkedServicePrograms(newAdmission.getProgramId(),demographicNo);
			for(Program p:programs) {
				//discharge them from this program
				this.processDischarge(p.getId(), demographicNo,"", "");
			}
		}
	}

	public void processInitialAdmission(Integer demographicNo, String providerNo, Program program, String admissionNotes, Date admissionDate) throws ProgramFullException, AlreadyAdmittedException, ServiceRestrictionException {
		// see if there's room first
		if (program.getNumOfMembers().intValue() >= program.getMaxAllowed().intValue()) {
			throw new ProgramFullException();
		}

        // check if there's a service restriction in place on this individual for this program
        ProgramClientRestriction restrInPlace = clientRestrictionManager.checkClientRestriction(program.getId(), demographicNo, new Date());
        if (restrInPlace != null) {
            throw new ServiceRestrictionException("service restriction in place", restrInPlace);
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
		newAdmission.setAdmissionStatus(Admission.STATUS_CURRENT);
		newAdmission.setClientId(demographicNo);
		newAdmission.setProgramId(program.getId());
		newAdmission.setProviderNo(Long.valueOf(providerNo));
		newAdmission.setTeamId(0);
		newAdmission.setAgencyId(new Long(0));
		saveAdmission(newAdmission);
	}

	public Admission getTemporaryAdmission(Integer demographicNo) {
		return dao.getTemporaryAdmission(demographicNo);
	}

	public List<Admission> getCurrentTemporaryProgramAdmission(Integer demographicNo) {
		Admission admission = dao.getTemporaryAdmission(demographicNo);
		if (admission != null) {
			List<Admission> results = new ArrayList<Admission>();
			results.add(admission);
			return results;
		}
		return null;
	}

	public List search(AdmissionSearchBean searchBean) {
		return dao.search(searchBean);
	}

	public void processDischarge(Integer programId, Integer demographicNo, String dischargeNotes, String radioDischargeReason) throws AdmissionException {
		
		Admission fullAdmission = getCurrentAdmission(String.valueOf(programId), demographicNo);

		if (fullAdmission == null) {
			throw new AdmissionException("Admission Record not found");
		}

		fullAdmission.setDischargeDate(new Date());
		fullAdmission.setDischargeNotes(dischargeNotes);
		fullAdmission.setAdmissionStatus(Admission.STATUS_DISCHARGED);
		fullAdmission.setRadioDischargeReason(radioDischargeReason);
		saveAdmission(fullAdmission);
	}

	public void processDischargeToCommunity(Integer communityProgramId, Integer demographicNo, String providerNo, String notes, String radioDischargeReason) throws AdmissionException {
		Admission currentBedAdmission = getCurrentBedProgramAdmission(demographicNo);

		if (currentBedAdmission != null) {
			processDischarge(currentBedAdmission.getProgramId(), demographicNo, notes, radioDischargeReason);
			
			BedDemographic bedDemographic = bedDemographicManager.getBedDemographicByDemographic(demographicNo);
			
			if (bedDemographic != null) {
				bedDemographicManager.deleteBedDemographic(bedDemographic);
			}
		}

		Admission currentCommunityAdmission = getCurrentCommunityProgramAdmission(demographicNo);

		if (currentCommunityAdmission != null) {
			processDischarge(currentCommunityAdmission.getProgramId(), demographicNo, notes, radioDischargeReason);
		}

		// Create and save admission object
		Admission admission = new Admission();
		admission.setAdmissionDate(new Date());
		admission.setAdmissionNotes(notes);
		admission.setAdmissionStatus(Admission.STATUS_CURRENT);
		admission.setClientId(demographicNo);
		admission.setProgramId(communityProgramId);
		admission.setProviderNo(Long.valueOf(providerNo));
		admission.setTeamId(0);
		admission.setAgencyId(0L);
		admission.setTemporaryAdmission(false);
		admission.setRadioDischargeReason(radioDischargeReason);
		admission.setClientStatusId(0);
		saveAdmission(admission);
	}

    @Required
    public void setAdmissionDao(AdmissionDao dao) {
		this.dao = dao;
	}

    @Required
    public void setProgramDao(ProgramDao programDao) {
	    this.programDao = programDao;
    }

    @Required
    public void setProgramQueueDao(ProgramQueueDao dao) {
		this.programQueueDao = dao;
	}

    @Required
    public void setClientReferralDAO(ClientReferralDAO dao) {
		this.clientReferralDAO = dao;
	}

    @Required
    public void setBedDemographicManager(BedDemographicManager bedDemographicManager) {
	    this.bedDemographicManager = bedDemographicManager;
    }

	@Required
    public void setProgramClientStatusDAO(ProgramClientStatusDAO programClientStatusDAO) {
		this.programClientStatusDAO = programClientStatusDAO;
	}

    @Required
    public void setClientRestrictionManager(ClientRestrictionManager clientRestrictionManager) {
        this.clientRestrictionManager = clientRestrictionManager;
    }


}
