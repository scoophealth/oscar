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
		if(referral.getSourceAgencyId().longValue() == integratorManager.getLocalAgencyId()) {
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
					demographicNo = integratorManager.getLocalClientId(referral.getSourceAgencyId().longValue(),referral.getClientId().longValue());
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
