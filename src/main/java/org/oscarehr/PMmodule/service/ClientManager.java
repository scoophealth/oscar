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
import org.oscarehr.PMmodule.exception.AlreadyAdmittedException;
import org.oscarehr.PMmodule.exception.AlreadyQueuedException;
import org.oscarehr.PMmodule.exception.ServiceRestrictionException;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.ProgramClientRestriction;
import org.oscarehr.PMmodule.model.ProgramQueue;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;
import org.oscarehr.PMmodule.web.formbean.DemographicExtra;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.FunctionalCentreAdmissionDao;
import org.oscarehr.common.dao.JointAdmissionDao;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.FunctionalCentreAdmission;
import org.oscarehr.common.model.JointAdmission;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ClientManager {

    private DemographicDao dao;
    private DemographicExtDao demographicExtDao;
    private ClientReferralDAO referralDAO;
    private JointAdmissionDao jointAdmissionDao;
    private ProgramQueueManager queueManager;
    private AdmissionManager admissionManager;
    private ClientRestrictionManager clientRestrictionManager;
    private FunctionalCentreAdmissionDao fcAdmissionDao;

    private boolean outsideOfDomainEnabled;

    public boolean isOutsideOfDomainEnabled() {
        return outsideOfDomainEnabled;
    }
    
    public DemographicExtra getClientExtraByDemographicNo(String demographicNo) {
        if (demographicNo == null || demographicNo.length() == 0) {
            return null;
        }
    	
        String middleName = "";
        String preferredName = "";
        String lastNameAtBirth="";
    	String maritalStatus="";
    	String recipientLocation="";
    	String lhinConsumerResides="";
    	String address2="";
    	
        DemographicExt mn = demographicExtDao.getLatestDemographicExt(Integer.valueOf(demographicNo), "middleName");
    	if(mn != null) {
    		middleName = mn.getValue();
    	}    	
    	
    	DemographicExt pn = demographicExtDao.getLatestDemographicExt(Integer.valueOf(demographicNo), "preferredName");
    	if(pn != null) {
    		preferredName = pn.getValue();
    	}
    	
    	DemographicExt lnab = demographicExtDao.getLatestDemographicExt(Integer.valueOf(demographicNo), "lastNameAtBirth");
    	if(lnab != null) {
    		lastNameAtBirth = lnab.getValue();
    	}
    	
    	DemographicExt ms = demographicExtDao.getLatestDemographicExt(Integer.valueOf(demographicNo), "maritalStatus");
        if(ms != null) {
        	maritalStatus = ms.getValue();
        }
        
        DemographicExt rl = demographicExtDao.getLatestDemographicExt(Integer.valueOf(demographicNo), "recipientLocation");
        if(rl != null) {
        	recipientLocation = rl.getValue();
        }
        
        DemographicExt lcr = demographicExtDao.getLatestDemographicExt(Integer.valueOf(demographicNo), "lhinConsumerResides");
        if(lcr != null) {
        	lhinConsumerResides = lcr.getValue();
        }
        
        DemographicExt addr2 = demographicExtDao.getLatestDemographicExt(Integer.valueOf(demographicNo), "address2");
        if(addr2 != null) {
        	address2 = addr2.getValue();
        }
        
        DemographicExtra demoExtra = new DemographicExtra();
        demoExtra.setMiddleName(middleName);
        demoExtra.setPreferredName(preferredName);
        demoExtra.setLastNameAtBirth(lastNameAtBirth);
        demoExtra.setMaritalStatus(maritalStatus);
        demoExtra.setRecipientLocation(recipientLocation);
        demoExtra.setLhinConsumerResides(lhinConsumerResides);
        demoExtra.setAddress2(address2);
        
        return demoExtra;
    }
    
    public Demographic getClientByDemographicNo(String demographicNo) {
        if (demographicNo == null || demographicNo.length() == 0) {
            return null;
        }
        return dao.getClientByDemographicNo(Integer.valueOf(demographicNo));
    }

    public List<Demographic> getClients() {
        return dao.getClients();
    }

    public List<Demographic> search(ClientSearchFormBean criteria, boolean returnOptinsOnly,boolean excludeMerged) {
        return dao.search(criteria, returnOptinsOnly,excludeMerged);
    }
    public List<Demographic> search(ClientSearchFormBean criteria) {
        return dao.search(criteria);
    }

    public List<ClientReferral> getReferrals() {
        return referralDAO.getReferrals();
    }

    public List<ClientReferral> getReferrals(String clientId) {
        return referralDAO.getReferrals(Long.valueOf(clientId));
    }

    public List<ClientReferral> getReferralsByFacility(Integer clientId, Integer facilityId) {
        return referralDAO.getReferralsByFacility(clientId.longValue(), facilityId);
    }

    public List<ClientReferral> getActiveReferrals(String clientId, String sourceFacilityId) {
        List<ClientReferral> results = referralDAO.getActiveReferrals(Long.valueOf(clientId), Integer.parseInt(sourceFacilityId));

        return results;
    }

    public ClientReferral getClientReferral(String id) {
        return referralDAO.getClientReferral(Long.valueOf(id));
    }
    
    public List<FunctionalCentreAdmission> getFcAdmissionsByClientId(Integer clientId) {
        return fcAdmissionDao.getAllAdmissionsByDemographicNo(clientId);
    }
    /*
     * This should always be a new one. add the queue to the program.
     */
    public void saveClientReferral(ClientReferral referral) {

        referralDAO.saveClientReferral(referral);
        addClientReferralToProgramQueue(referral);
    } 

    
    public void addClientReferralToProgramQueue(ClientReferral referral) {
        if (referral.getStatus().equalsIgnoreCase(ClientReferral.STATUS_ACTIVE)) {
            ProgramQueue queue = new ProgramQueue();
            queue.setClientId(referral.getClientId());
            queue.setNotes(referral.getNotes());
            queue.setProgramId(referral.getProgramId());
            queue.setProviderNo(Long.parseLong(referral.getProviderNo()));
            queue.setReferralDate(referral.getReferralDate());
            queue.setStatus(ProgramQueue.STATUS_ACTIVE);
            queue.setReferralId(referral.getId());
            queue.setTemporaryAdmission(referral.isTemporaryAdmission());
            queue.setPresentProblems(referral.getPresentProblems());
            queue.setVacancyName(referral.getSelectVacancy());

            queueManager.saveProgramQueue(queue);
        }
    }

    public List<ClientReferral> searchReferrals(ClientReferral referral) {
        return referralDAO.search(referral);
    }

    public void saveJointAdmission(JointAdmission admission) {
        if (admission == null) {
            throw new IllegalArgumentException();
        }

        jointAdmissionDao.persist(admission);
    }

    public List<JointAdmission> getDependents(Integer clientId) {
        return jointAdmissionDao.getSpouseAndDependents(clientId);
    }

    public List<Integer> getDependentsList(Integer clientId) {
        List<Integer> list = new ArrayList<Integer>();
        List<JointAdmission> jadms = jointAdmissionDao.getSpouseAndDependents(clientId);
        for (JointAdmission jadm : jadms) {
            list.add(jadm.getClientId());
        }
        return list;
    }

    public JointAdmission getJointAdmission(Integer clientId) {
        return jointAdmissionDao.getJointAdmission(clientId);
    }

    public boolean isClientDependentOfFamily(Integer clientId){

		JointAdmission clientsJadm = null;
		if(clientId != null){
			clientsJadm = getJointAdmission(Integer.valueOf(clientId.toString()));
		}
		if (clientsJadm != null  &&  clientsJadm.getHeadClientId() != null) {
			return true;
		}
		return false;
    }


    public boolean isClientFamilyHead(Integer clientId){

		List<JointAdmission> dependentList = getDependents(Integer.valueOf(clientId.toString()));
		if(dependentList != null  &&  dependentList.size() > 0){
			return true;
		}
		return false;
    }

    public void removeJointAdmission(Integer clientId, String providerNo) {
        jointAdmissionDao.removeJointAdmission(clientId, providerNo);
    }

    public void removeJointAdmission(JointAdmission admission) {
        jointAdmissionDao.removeJointAdmission(admission);
    }

    public void processReferral(ClientReferral referral) throws AlreadyAdmittedException, AlreadyQueuedException, ServiceRestrictionException {
        processReferral(referral, false);
    }

    public void processReferral(ClientReferral referral, boolean override) throws AlreadyAdmittedException, AlreadyQueuedException, ServiceRestrictionException {
        if (!override) {
            // check if there's a service restriction in place on this individual for this program
            ProgramClientRestriction restrInPlace = clientRestrictionManager.checkClientRestriction(referral.getProgramId().intValue(), referral.getClientId().intValue(), new Date());
            if (restrInPlace != null) {
                throw new ServiceRestrictionException("service restriction in place", restrInPlace);
            }
        }

        Admission currentAdmission = admissionManager.getCurrentAdmission(String.valueOf(referral.getProgramId()), referral.getClientId().intValue());
        if (currentAdmission != null) {
            referral.setStatus(ClientReferral.STATUS_REJECTED);
            referral.setCompletionNotes("Client currently admitted");
            referral.setCompletionDate(new Date());

            //saveClientReferral(referral); //???what's the point to save it if it's already admitted
            throw new AlreadyAdmittedException();
        }

        ProgramQueue queue = queueManager.getActiveProgramQueue(String.valueOf(referral.getProgramId()), String.valueOf(referral.getClientId()));
        if (queue != null) {
            referral.setStatus(ClientReferral.STATUS_REJECTED);
            referral.setCompletionNotes("Client already in queue");
            referral.setCompletionDate(new Date());

            //saveClientReferral(referral); //???what's the point to save it if's already in queue
            throw new AlreadyQueuedException();
        }

        saveClientReferral(referral);
        List<JointAdmission> dependents = getDependents(new Long(referral.getClientId()).intValue());
        for (JointAdmission jadm : dependents) {
            referral.setClientId(new Long(jadm.getClientId()));
            saveClientReferral(referral);
        }

    }

    public void saveClient(Demographic client) {
        dao.saveClient(client);
    }

    public DemographicExt getDemographicExt(String id) {
        return demographicExtDao.getDemographicExt(Integer.valueOf(id));
    }

    public List<DemographicExt> getDemographicExtByDemographicNo(int demographicNo) {
        return demographicExtDao.getDemographicExtByDemographicNo(demographicNo);
    }

    public DemographicExt getDemographicExt(int demographicNo, String key) {
        return demographicExtDao.getDemographicExt(demographicNo, key);
    }

    public void updateDemographicExt(DemographicExt de) {
    	demographicExtDao.updateDemographicExt(de);
    }

    public void saveDemographicExt(int demographicNo, String key, String value) {
    	demographicExtDao.saveDemographicExt(demographicNo, key, value);
    }

    public void removeDemographicExt(String id) {
    	demographicExtDao.removeDemographicExt(Integer.valueOf(id));
    }

    public void removeDemographicExt(int demographicNo, String key) {
    	demographicExtDao.removeDemographicExt(demographicNo, key);
    }

    public void setJointAdmissionDao(JointAdmissionDao jointAdmissionDao) {
        this.jointAdmissionDao = jointAdmissionDao;
    }

    @Required
    public void setDemographicDao(DemographicDao dao) {
        this.dao = dao;
    }

    @Required
    public void setDemographicExtDao(DemographicExtDao dao) {
        this.demographicExtDao = dao;
    }

    @Required
    public void setClientReferralDAO(ClientReferralDAO dao) {
        this.referralDAO = dao;
    }

    @Required
    public void setFcAdmissionDao(FunctionalCentreAdmissionDao dao) {
        this.fcAdmissionDao = dao;
    }
    
    @Required
    public void setProgramQueueManager(ProgramQueueManager mgr) {
        this.queueManager = mgr;
    }

    @Required
    public void setAdmissionManager(AdmissionManager mgr) {
        this.admissionManager = mgr;
    }

    @Required
    public void setClientRestrictionManager(ClientRestrictionManager clientRestrictionManager) {
        this.clientRestrictionManager = clientRestrictionManager;
    }

    @Required
    public void setOutsideOfDomainEnabled(boolean outsideOfDomainEnabled) {
        this.outsideOfDomainEnabled = outsideOfDomainEnabled;
    }


	public boolean checkHealthCardExists(String hin, String hcType) {
	   List<Demographic> results = this.dao.searchByHealthCard(hin,hcType);
	   return (results.size()>0)?true:false;
	}
}
