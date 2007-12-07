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

package org.oscarehr.PMmodule.integrator.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.exception.IntegratorException;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.ProgramQueue;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.IntegratorManager;
import org.oscarehr.PMmodule.service.ProgramQueueManager;
import org.springframework.jms.core.JmsTemplate;

public class IncomingReferralBean implements MessageListener {

	private Log log = LogFactory.getLog(getClass());
	private AdmissionManager admissionManager;
	private ProgramQueueManager programQueueManager;
	private IntegratorManager integratorManager;
	private ClientManager clientManager;
	private JmsTemplate jmsTemplate;
	
	public void setAdmissionManager(AdmissionManager mgr) {
		this.admissionManager = mgr;
	}
	
	public void setProgramQueueManager(ProgramQueueManager mgr) {
		this.programQueueManager = mgr;
	}
	
	public void setIntegratorManager(IntegratorManager mgr) {
		this.integratorManager = mgr;
	}
	
	public void setClientManager(ClientManager mgr) {
		this.clientManager = mgr;
	}
	
	public void setJmsTemplate(JmsTemplate j) {
		this.jmsTemplate = j;
	}
	
	public void onMessage(Message message) {
		log.debug("RECEIVED A REFERRAL MESSAGE");
		ObjectMessage objMessage = (ObjectMessage)message;
		ClientReferral referral = null;
		try {
			referral = (ClientReferral)objMessage.getObject();
		}catch(JMSException e) {
			log.error(e);
			return;
		}
		
		log.debug("referral is from agency #" + referral.getSourceAgencyId());
		
		
		//here, i can be the source, if not, i am the receiver
		if(referral.getSourceAgencyId().longValue() == integratorManager.getLocalAgency().getId()) {
			//i am the source of this referral,i should have it in record
			log.debug("my own referral coming back to me");
			log.debug("client_id=" + referral.getClientId() + ", agency_id = " + referral.getAgencyId() + ", program_id = " + referral.getProgramId());
			ClientReferral myReferral = clientManager.getReferralToRemoteAgency(referral.getClientId().longValue(),referral.getAgencyId().longValue(),referral.getProgramId().longValue());
			if(myReferral == null) {
				log.warn("i have no record of making this referral");
				return;
			}
			log.debug("found my reference");
			myReferral.setCompletionDate(referral.getCompletionDate());
			myReferral.setCompletionNotes(referral.getCompletionNotes());
			myReferral.setStatus(referral.getStatus());
			
			clientManager.saveClientReferral(myReferral);
			
		} else {
			//i need to do a lookup, if this client referral doesn't exist
			//it's new, or else, i'm doing a status update
			ClientReferral myReferral = clientManager.getReferralToRemoteAgency(referral.getClientId().longValue(),referral.getAgencyId().longValue(),referral.getProgramId().longValue());
			
			if(myReferral != null) {
				//it's a status update - maybe a cancel?
				log.error("not implemented");
				
			} else {
			
				//need to get this client's "local" id
				long demographicNo = 0;
				try {
                    // TODO re-enable or reimplement
//					demographicNo = integratorManager.getLocalClientId(referral.getSourceAgencyId().longValue(),referral.getDemographicId().longValue());
					log.debug("local id is " + demographicNo);
				} catch(IntegratorException e) {
					log.error(e);
				}
				
				Demographic client = null;
				if(demographicNo > 0) {
					client = clientManager.getClientByDemographicNo(String.valueOf(demographicNo));
				}
				
				if(client != null) {
					log.debug("loaded local client: " + client.getFormattedName());
				} else {
					log.warn("exiting");
					return;
				}
				
				
				//check if client is admitted to this program
				Admission currentAdmission = admissionManager.getCurrentAdmission(String.valueOf(referral.getProgramId()), client.getDemographicNo());
				if(currentAdmission != null) {
					log.debug("client already admitted to this program!");
					log.debug("need to reject this referral, and send the message back");
					return;
				}
				
				//check if client is in the queue
				ProgramQueue queue = programQueueManager.getActiveProgramQueue(String.valueOf(referral.getProgramId()),String.valueOf(client.getDemographicNo()));
				if(queue != null) {
					log.debug("client already waiting in queue for this program!");
					log.debug("need to reject this referral, and send the message back");
					return;
				}
				
				log.debug("accepting this referral");
				
				ClientReferral localReferral = new ClientReferral();
				localReferral.setAgencyId(new Long(0));
				localReferral.setClientId(new Long(client.getDemographicNo().intValue()));
				localReferral.setCompletionDate(referral.getCompletionDate());
				localReferral.setCompletionNotes(referral.getCompletionNotes());
				localReferral.setNotes(referral.getNotes());
				localReferral.setProgramId(referral.getProgramId());
				localReferral.setProviderNo(referral.getProviderNo());
				localReferral.setReferralDate(referral.getReferralDate());
				localReferral.setSourceAgencyId(referral.getSourceAgencyId());
				localReferral.setStatus("active");
				localReferral.setTemporaryAdmission(referral.isTemporaryAdmission());
				
				clientManager.saveClientReferral(localReferral);
				
				log.debug("referral saved locally");
				
				log.debug("need to send back referral with updated status");
	
				referral.setStatus("active");
				OutgoingReferralBean orb = new OutgoingReferralBean();
				orb.setReferral(referral);
				jmsTemplate.setDefaultDestinationName(referral.getSourceAgencyId() + ".refer");
				jmsTemplate.send(orb);
			}
			
		}
		
	}
}
