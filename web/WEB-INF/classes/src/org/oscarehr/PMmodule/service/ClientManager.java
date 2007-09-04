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

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.ClientDao;
import org.oscarehr.PMmodule.dao.ClientReferralDAO;
import org.oscarehr.PMmodule.exception.AlreadyAdmittedException;
import org.oscarehr.PMmodule.exception.AlreadyQueuedException;
import org.oscarehr.PMmodule.exception.IntegratorNotEnabledException;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.DemographicExt;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramQueue;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;

public class ClientManager {

	private static Log log = LogFactory.getLog(ClientManager.class);

	private ClientDao dao;

	private ClientReferralDAO referralDAO;

	private ProgramQueueManager queueManager;

	private IntegratorManager integratorManager;

	private AdmissionManager admissionManager;
	
	private AgencyManager agencyManager;
	
	private boolean outsideOfDomainEnabled;
	
	public boolean isOutsideOfDomainEnabled() {
		return outsideOfDomainEnabled;
	}

	public void setOutsideOfDomainEnabled(boolean outsideOfDomainEnabled) {
		this.outsideOfDomainEnabled = outsideOfDomainEnabled;
	}

	public void setClientDao(ClientDao dao) {
		this.dao = dao;
	}

	public void setClientReferralDAO(ClientReferralDAO dao) {
		this.referralDAO = dao;
	}

	public void setProgramQueueManager(ProgramQueueManager mgr) {
		this.queueManager = mgr;
	}

	public void setAdmissionManager(AdmissionManager mgr) {
		this.admissionManager = mgr;
	}

	public void setIntegratorManager(IntegratorManager mgr) {
		this.integratorManager = mgr;
	}

	public void setAgencyManager(AgencyManager mgr) {
		this.agencyManager = mgr;
	}
	
	public Demographic getClientByDemographicNo(String demographicNo) {
		if (demographicNo == null || demographicNo.length() == 0) {
			return null;
		}
		return dao.getClientByDemographicNo(Integer.valueOf(demographicNo));
	}

	public List getClients() {
		return dao.getClients();
	}

	public List search(ClientSearchFormBean criteria, boolean returnOptinsOnly) {
		return dao.search(criteria, returnOptinsOnly);
	}

	public java.util.Date getMostRecentIntakeADate(String demographicNo) {
		return dao.getMostRecentIntakeADate(Integer.valueOf(demographicNo));
	}

	public java.util.Date getMostRecentIntakeCDate(String demographicNo) {
		return dao.getMostRecentIntakeCDate(Integer.valueOf(demographicNo));
	}

	public String getMostRecentIntakeAProvider(String demographicNo) {
		return dao.getMostRecentIntakeAProvider(Integer.valueOf(demographicNo));
	}

	public String getMostRecentIntakeCProvider(String demographicNo) {
		return dao.getMostRecentIntakeCProvider(Integer.valueOf(demographicNo));
	}

	public List getReferrals() {
		return referralDAO.getReferrals();
	}

	public List getReferrals(String clientId) {
		return referralDAO.getReferrals(Long.valueOf(clientId));
	}

	public List getActiveReferrals(String clientId) {
		List results = referralDAO.getActiveReferrals(Long.valueOf(clientId));
		for (Iterator iter = results.iterator(); iter.hasNext();) {
			ClientReferral referral = (ClientReferral) iter.next();
			if (referral.getAgencyId().longValue() > 0) {
				try {
					Program p = integratorManager.getProgram(referral.getAgencyId(), referral.getProgramId());
					referral.setProgramName(p.getName());
				} catch (org.codehaus.xfire.XFireRuntimeException ex) {
					log.error(ex);
					referral.setProgramName("<Unavailable>");
				} catch (IntegratorNotEnabledException e) {
					log.error(e);
					referral.setProgramName("<Unavailable>");
				}
			}
		}
		return results;
	}

	public ClientReferral getClientReferral(String id) {
		return referralDAO.getClientReferral(Long.valueOf(id));
	}

	/*
	 * This should always be a new one. add the queue to the program.
	 */
	public void saveClientReferral(ClientReferral referral) {

		referralDAO.saveClientReferral(referral);

		if (referral.getStatus().equalsIgnoreCase("active")) {
			ProgramQueue queue = new ProgramQueue();
			queue.setAgencyId(referral.getAgencyId());
			queue.setClientId(referral.getClientId());
			queue.setNotes(referral.getNotes());
			queue.setProgramId(referral.getProgramId());
			queue.setProviderNo(referral.getProviderNo());
			queue.setReferralDate(referral.getReferralDate());
			queue.setStatus("active");
			queue.setReferralId(referral.getId());
			queue.setTemporaryAdmission(referral.isTemporaryAdmission());
			queue.setPresentProblems(referral.getPresentProblems());
			
			queueManager.saveProgramQueue(queue);
		}
	}

	public List searchReferrals(ClientReferral referral) {
		return referralDAO.search(referral);
	}

	public void processReferral(ClientReferral referral) throws AlreadyAdmittedException, AlreadyQueuedException {
		Admission currentAdmission = admissionManager.getCurrentAdmission(String.valueOf(referral.getProgramId()), referral.getClientId().intValue());
		if (currentAdmission != null) {
			referral.setStatus("rejected");
			referral.setCompletionNotes("Client currently admitted");
			referral.setCompletionDate(new Date());
			
			saveClientReferral(referral);
			throw new AlreadyAdmittedException();
		}

		ProgramQueue queue = queueManager.getActiveProgramQueue(String.valueOf(referral.getProgramId()), String.valueOf(referral.getClientId()));
		if (queue != null) {
			referral.setStatus("rejected");
			referral.setCompletionNotes("Client already in queue");
			referral.setCompletionDate(new Date());
			
			saveClientReferral(referral);
			throw new AlreadyQueuedException();
		}

		saveClientReferral(referral);
	}

	public void processRemoteReferral(ClientReferral referral) {
		/*
		 * Admission currentAdmission = admissionManager.getCurrentAdmission(String.valueOf(program.getId()),id); if(currentAdmission != null) { referral.setStatus("rejected"); referral.setCompletionNotes("Client currently admitted"); referral.setCompletionDate(new Date()); } ProgramQueue queue =
		 * queueManager.getActiveProgramQueue(String.valueOf(program.getId()),id); if(queue != null) { referral.setStatus("rejected"); referral.setCompletionNotes("Client already in queue"); referral.setCompletionDate(new Date()); }
		 */
		ProgramQueue queue = new ProgramQueue();
		queue.setAgencyId(referral.getSourceAgencyId());
		queue.setClientId(referral.getClientId());
		queue.setNotes(referral.getNotes());
		queue.setProgramId(referral.getProgramId());
		queue.setProviderNo(referral.getProviderNo());
		queue.setReferralDate(referral.getReferralDate());
		queue.setStatus("active");
		queue.setReferralId(referral.getId());
		queue.setTemporaryAdmission(referral.isTemporaryAdmission());
		queueManager.saveProgramQueue(queue);

		referral.setStatus("active");

		// send back jms message
		integratorManager.sendReferral(referral.getSourceAgencyId(), referral);
	}

	public void saveClient(Demographic client) {
		dao.saveClient(client);
	}

	public ClientReferral getReferralToRemoteAgency(long clientId, long agencyId, long programId) {
		referralDAO.getReferralToRemoteAgency(clientId, agencyId, programId);
		return null;
	}

	public DemographicExt getDemographicExt(String id) {
		return dao.getDemographicExt(Integer.valueOf(id));
	}

	public List getDemographicExtByDemographicNo(Integer demographicNo) {
		return dao.getDemographicExtByDemographicNo(demographicNo);
	}

	public DemographicExt getDemographicExt(Integer demographicNo, String key) {
		return dao.getDemographicExt(demographicNo, key);
	}

	public void updateDemographicExt(DemographicExt de) {
		dao.updateDemographicExt(de);
	}

	public void saveDemographicExt(Integer demographicNo, String key, String value) {
		dao.saveDemographicExt(demographicNo, key, value);
	}

	public void removeDemographicExt(String id) {
		dao.removeDemographicExt(Integer.valueOf(id));
	}

	public void removeDemographicExt(Integer demographicNo, String key) {
		dao.removeDemographicExt(demographicNo, key);
	}
	
}
