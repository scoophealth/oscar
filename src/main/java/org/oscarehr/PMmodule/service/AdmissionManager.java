/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.oscarehr.PMmodule.dao.ClientReferralDAO;
import org.oscarehr.PMmodule.dao.ProgramClientStatusDAO;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProgramQueueDao;
import org.oscarehr.PMmodule.dao.VacancyDao;
import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.exception.AlreadyAdmittedException;
import org.oscarehr.PMmodule.exception.ProgramFullException;
import org.oscarehr.PMmodule.exception.ServiceRestrictionException;
import org.oscarehr.PMmodule.model.AdmissionSearchBean;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramClientRestriction;
import org.oscarehr.PMmodule.model.ProgramQueue;
import org.oscarehr.PMmodule.model.Vacancy;
import org.oscarehr.common.dao.AdmissionDao;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.BedDemographic;
import org.oscarehr.common.model.JointAdmission;
import org.oscarehr.common.model.RoomDemographic;
import org.oscarehr.managers.BedManager;
import org.oscarehr.managers.BedDemographicManager;
import org.oscarehr.managers.RoomManager;
import org.oscarehr.managers.RoomDemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import oscar.log.LogAction;

@Transactional
public class AdmissionManager {

	private AdmissionDao dao;
	private ProgramDao programDao;
	private ProgramQueueDao programQueueDao;
	private ClientReferralDAO clientReferralDAO;
	private BedDemographicManager bedDemographicManager;
	private ProgramClientStatusDAO programClientStatusDAO;
	private ClientRestrictionManager clientRestrictionManager;
	private RoomManager roomManager;
	private BedManager bedManager;
	private RoomDemographicManager roomDemographicManager;

    public List<Admission> getAdmissions_archiveView(String programId, Integer demographicNo) {
		return dao.getAdmissions_archiveView(Integer.valueOf(programId), demographicNo);
	}
	
	public Admission getAdmission(String programId, Integer demographicNo) {
		return dao.getAdmission(Integer.valueOf(programId), demographicNo);
	}

	public Admission getCurrentAdmission(String programId, Integer demographicNo) {
		return dao.getCurrentAdmission(Integer.valueOf(programId), demographicNo);
	}
		
	public List<Admission> getAdmissionsByFacility(Integer demographicNo, Integer facilityId) {
		return dao.getAdmissionsByFacility(demographicNo, facilityId);
	}

	public List<Admission> getCurrentAdmissionsByFacility(Integer demographicNo, Integer facilityId) {
		return dao.getCurrentAdmissionsByFacility(demographicNo, facilityId);
	}
	
	public List<Admission> getAdmissions() {
		return dao.getAdmissions();
	}

	public List<Admission> getAdmissions(Integer demographicNo) {
		return dao.getAdmissions(demographicNo);
	}
	
	public List<Admission> getCurrentAdmissions(Integer demographicNo) {
		return dao.getCurrentAdmissions(demographicNo);
	}

	public Admission getCurrentBedProgramAdmission(Integer demographicNo) {
		return dao.getCurrentBedProgramAdmission(programDao, demographicNo);
	}

	public List<Admission> getCurrentServiceProgramAdmission(Integer demographicNo) {
		return dao.getCurrentServiceProgramAdmission(programDao, demographicNo);
	}

	public Admission getCurrentExternalProgramAdmission(Integer demographicNo) {
		return dao.getCurrentExternalProgramAdmission(programDao, demographicNo);
	}
	
	public Admission getCurrentCommunityProgramAdmission(Integer demographicNo) {
		return dao.getCurrentCommunityProgramAdmission(programDao, demographicNo);
	}

	public List<Admission> getCurrentAdmissionsByProgramId(String programId) {
		return dao.getCurrentAdmissionsByProgramId(Integer.valueOf(programId));
	}

    public Admission getAdmission(Long id) {    	
		return dao.getAdmission(id.intValue());
	}
    
    public Admission getAdmission(Integer id) {    	
		return dao.getAdmission(id);
	}

	public void saveAdmission(Admission admission) {
		dao.saveAdmission(admission);
	}

	/*public void processAdmissionToExternal(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes) throws ProgramFullException, AdmissionException, ServiceRestrictionException {
		processAdmission(demographicNo, providerNo, program, dischargeNotes, admissionNotes, false, null, false);
	}
	*/
	public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes) throws ProgramFullException, AdmissionException, ServiceRestrictionException {
		processAdmission(demographicNo, providerNo, program, dischargeNotes, admissionNotes, false, null, false,null);
	}

	public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, boolean tempAdmission) throws ProgramFullException, AdmissionException, ServiceRestrictionException {
		processAdmission(demographicNo, providerNo, program, dischargeNotes, admissionNotes, tempAdmission, null, false,null);
	}

        public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, boolean tempAdmission,List<Integer> dependents) throws ProgramFullException, AdmissionException, ServiceRestrictionException {
		processAdmission(demographicNo, providerNo, program, dischargeNotes, admissionNotes, tempAdmission, null, false,dependents);
	}
        
	public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, boolean tempAdmission, boolean overrideRestriction) throws ProgramFullException, AdmissionException, ServiceRestrictionException {
		processAdmission(demographicNo, providerNo, program, dischargeNotes, admissionNotes, tempAdmission, null, overrideRestriction,null);
	}
        
        public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, Date admissionDate) throws ProgramFullException, AdmissionException, ServiceRestrictionException {
		processAdmission(demographicNo, providerNo, program, dischargeNotes, admissionNotes, false, admissionDate, false,null);
	}    
    
	public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, boolean tempAdmission,List<Integer> dependents, Date admissionDate) throws ProgramFullException, AdmissionException, ServiceRestrictionException {
	 	processAdmission(demographicNo, providerNo, program, dischargeNotes, admissionNotes, tempAdmission, admissionDate, false,dependents);
   	}    


	public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, boolean tempAdmission, Date admissionDate, boolean overrideRestriction, List<Integer> dependents) throws ProgramFullException, AdmissionException, ServiceRestrictionException {
		// see if there's room first
		if (program.getNumOfMembers().intValue() >= program.getMaxAllowed().intValue()) {
			throw new ProgramFullException();
		}

        // check if there's a service restriction in place on this individual for this program
        if (!overrideRestriction) {
            ProgramClientRestriction restrInPlace = clientRestrictionManager.checkClientRestriction(program.getId(), demographicNo, new Date());
            if (restrInPlace != null) {
                throw new ServiceRestrictionException("service restriction in place", restrInPlace);
            }
        }
                
        boolean fromTransfer=false;
        boolean automaticDischarge=false;
        // If admitting to bed program, discharge from old bed program
		if (program.getType().equalsIgnoreCase("bed") && !tempAdmission) {
			Admission fullAdmission = getCurrentBedProgramAdmission(demographicNo);

			// community?
			if (fullAdmission != null) {
			    Program oldProgram=programDao.getProgram(fullAdmission.getProgramId());
                Program newProgram=programDao.getProgram(program.getId());
			    fromTransfer=(oldProgram.getFacilityId()==newProgram.getFacilityId());			
			    
			     
			    //discharge from old bed program to a new bed program which is in the different facility
			    //This is called automatic discharge.
			    if(!fromTransfer)
			    	automaticDischarge = true;
			    
			    //processDischarge(new Integer(fullAdmission.getProgramId().intValue()), new Integer(demographicNo), dischargeNotes, "", null, fromTransfer);
			    processDischarge(new Integer(fullAdmission.getProgramId().intValue()), new Integer(demographicNo), dischargeNotes, "", null,null, fromTransfer,automaticDischarge);
			} else {
				fullAdmission = getCurrentCommunityProgramAdmission(demographicNo);
				if (fullAdmission != null) {
					processDischarge(new Integer(fullAdmission.getProgramId().intValue()), new Integer(demographicNo), dischargeNotes, "0",admissionDate);
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
		newAdmission.setProviderNo(providerNo);
		newAdmission.setTeamId(null);
		newAdmission.setTemporaryAdmission(tempAdmission);
		newAdmission.setAdmissionFromTransfer(fromTransfer);
		
		//keep the client status if he was in the same program with it.
		Integer clientStatusId = dao.getLastClientStatusFromAdmissionByProgramIdAndClientId(Integer.valueOf(program.getId()),demographicNo);
				
		//check if the client status is valid/existed in program_clientStatus
		if(programClientStatusDAO.getProgramClientStatus(clientStatusId.toString()) == null|| clientStatusId==0)
			clientStatusId = null;
				
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
                if(referral.getVacancyId()!=null){
                    //change vacancy's status
                    VacancyDao vacancyDao= SpringUtils.getBean(VacancyDao.class);
                    Vacancy v = vacancyDao.find(referral.getVacancyId());
		    if(v!=null) {
                    	v.setStatus("filled");
                    	vacancyDao.saveEntity(v);
		    }
                }
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
                
                //For the clients dependents
                if (dependents != null){
                   for(Integer l : dependents){  
                      processAdmission(new Integer(l.intValue()), providerNo,program,dischargeNotes,admissionNotes,tempAdmission,newAdmission.getAdmissionDate(),true,null);
                   }
                }
            
        //Once the patient is admitted to this program, the vacancy
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
		newAdmission.setProviderNo(providerNo);
		newAdmission.setTeamId(null);
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

    public boolean isDependentInDifferentProgramFromHead(Integer demographicNo, List<JointAdmission> dependentList){
        if(demographicNo == null  ||  dependentList == null  ||  dependentList.isEmpty()){
        	return false;
        }
        Integer[] dependentIds = new Integer[dependentList.size()];
        for(int i=0; i < dependentList.size(); i++ ){
            dependentIds[i] = new Integer(dependentList.get(i).getClientId().intValue());
        }
        
        //Check whether all family members are under same bed program -> if not, display error message.
        Integer headProgramId = null;
        Integer dependentProgramId = null;
        Admission headAdmission = getCurrentBedProgramAdmission(demographicNo);
        if(headAdmission != null){
            headProgramId = headAdmission.getProgramId();
        }else{
        	headProgramId = null;
        }
        for(int i=0; dependentIds != null  &&  i < dependentIds.length; i++ ){
            Admission dependentAdmission = getCurrentBedProgramAdmission(dependentIds[i]);
            if(dependentAdmission != null){
                dependentProgramId = dependentAdmission.getProgramId();
            }else{
            	dependentProgramId = null;
            }
            if( headProgramId != null  &&  dependentProgramId != null ){
                if( headProgramId.intValue() != dependentProgramId.intValue() ){
                    //Display message notifying that the dependent is under different bed program than family head -> cannot assign room/bed
                    return true;
                }
            }else if(headProgramId != null  &&  dependentProgramId == null){
            	return true;
            }else if(headProgramId == null){
            	return true;
            }
        }
        return false;
    }
	
	public List search(AdmissionSearchBean searchBean) {
		return dao.search(searchBean);
	}

    public void processDischarge(Integer programId, Integer demographicNo, String dischargeNotes, String radioDischargeReason) throws AdmissionException {
        processDischarge(programId, demographicNo, dischargeNotes, radioDischargeReason,null,null, false, false);
    }    
    
    public void processDischarge(Integer programId, Integer demographicNo, String dischargeNotes, String radioDischargeReason, Date dischargeDate) throws AdmissionException {
	processDischarge(programId, demographicNo, dischargeNotes, radioDischargeReason, dischargeDate, null, false, false);
    }

    public void processDischarge(Integer programId, Integer demographicNo, String dischargeNotes, String radioDischargeReason,Date dischargeDate, List<Integer> dependents, boolean fromTransfer, boolean automaticDischarge) throws AdmissionException {
	
		Admission fullAdmission = getCurrentAdmission(String.valueOf(programId), demographicNo);
	
		Program program=programDao.getProgram(programId);
        Integer facilityId=null;
        if (program!=null) facilityId=(int)program.getFacilityId();
		
		if (fullAdmission == null) {
			throw new AdmissionException("Admission Record not found");
		}
	
		if(dischargeDate == null)
			fullAdmission.setDischargeDate(new Date());
		else
			fullAdmission.setDischargeDate(dischargeDate);

		fullAdmission.setDischargeNotes(dischargeNotes);
		fullAdmission.setAdmissionStatus(Admission.STATUS_DISCHARGED);
		fullAdmission.setRadioDischargeReason(radioDischargeReason);
		fullAdmission.setDischargeFromTransfer(fromTransfer);
		fullAdmission.setAutomaticDischarge(automaticDischarge);
		
		saveAdmission(fullAdmission);
		
		if(roomManager != null  &&  roomManager.isRoomOfDischargeProgramAssignedToClient(demographicNo, programId)){
			if(roomDemographicManager != null){
				RoomDemographic roomDemographic = roomDemographicManager.getRoomDemographicByDemographic(demographicNo, facilityId);
				if(roomDemographic != null){
					roomDemographicManager.deleteRoomDemographic(roomDemographic);
				}
			}
		}
		
		if(bedManager != null  &&  bedManager.isBedOfDischargeProgramAssignedToClient(demographicNo, programId)){
			if(bedDemographicManager != null){
				BedDemographic bedDemographic = bedDemographicManager.getBedDemographicByDemographic(demographicNo, facilityId);
				if(bedDemographic != null){
					bedDemographicManager.deleteBedDemographic(bedDemographic);
				}
			}
		}
		
        if (dependents != null){
            for(Integer l:dependents){
                processDischarge(programId,new Integer(l.intValue()),dischargeNotes,radioDischargeReason, dischargeDate, null, fromTransfer, automaticDischarge);
            }
        }
    }

    public void processDischargeToCommunity(Integer communityProgramId, Integer demographicNo, String providerNo, String notes, String radioDischargeReason, Date dischargeDate) throws AdmissionException {
            processDischargeToCommunity(communityProgramId,demographicNo,providerNo,notes,radioDischargeReason,null,dischargeDate);
    }
        
	public void processDischargeToCommunity(Integer communityProgramId, Integer demographicNo, String providerNo, String notes, String radioDischargeReason,List<Integer> dependents,Date dischargeDate) throws AdmissionException {
		Admission currentBedAdmission = getCurrentBedProgramAdmission(demographicNo);

        Program program=programDao.getProgram(communityProgramId);
        Integer facilityId=null;
        if (program!=null) facilityId=(int)program.getFacilityId();
        
		if (currentBedAdmission != null) {
			processDischarge(currentBedAdmission.getProgramId(), demographicNo, notes, radioDischargeReason);
			
			BedDemographic bedDemographic = bedDemographicManager.getBedDemographicByDemographic(demographicNo, facilityId);
			
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
		admission.setProviderNo(providerNo);
		admission.setTeamId(null);
		admission.setTemporaryAdmission(false);
		admission.setRadioDischargeReason(radioDischargeReason);
		admission.setClientStatusId(null);
		saveAdmission(admission);
                
                if (dependents != null){
                    for(Integer l:dependents){
                        processDischargeToCommunity(communityProgramId,new Integer(l.intValue()),providerNo, notes, radioDischargeReason,null);
                    }
                }
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

    @Required
    public void setRoomManager(RoomManager roomManager) {
        this.roomManager = roomManager;
    }
    
    @Required
    public void setBedManager(BedManager bedManager) {
        this.bedManager= bedManager;
    }
    
    @Required
    public void setRoomDemographicManager(RoomDemographicManager roomDemographicManager) {
        this.roomDemographicManager = roomDemographicManager;
    }

    public boolean isActiveInCurrentFacility(LoggedInInfo loggedInInfo, int demographicId)
    {
        List<Admission> results=getCurrentAdmissionsByFacility(demographicId, loggedInInfo.getCurrentFacility().getId());
        if (results!=null && results.size()>0) return(true);
        
        return(false);
    }
    
    public List getActiveAnonymousAdmissions() {
    	return dao.getActiveAnonymousAdmissions();
    }
    
    public boolean wasInProgram(Integer programId, Integer clientId) {
    	if(dao.getAdmission(programId, clientId)!=null)
    		return true;
    	else
    		return false;
    
    }
    
    
    
    public List<Admission> findAdmissionsByProgramAndDate(LoggedInInfo loggedInInfo, Integer programNo, Date day, int startIndex, int numToReturn) {
    	List<Admission> results =  dao.findAdmissionsByProgramAndDate(programNo,day, startIndex, numToReturn);
    	
    	for(Admission result:results) {
    		LogAction.addLogSynchronous(loggedInInfo,"AdmissionManager.findAdmissionsByProgramAndDate", "admission id=" + result.getId());
    	}
    	return results;
    }
    
    public Integer findAdmissionsByProgramAndDateAsCount(LoggedInInfo loggedInInfo, Integer programNo, Date day) {
    	Integer count=  dao.findAdmissionsByProgramAndDateAsCount(programNo,day);
    	
    	return count;
    }
}
