package org.oscarehr.PMmodule.integrator.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.IntegratorManager;
import org.oscarehr.PMmodule.service.ProgramManager;

public class ReferralMessage implements MessageListener {
	
	private static Log log = LogFactory.getLog(ReferralMessage.class);
	
	private IntegratorManager integratorManager;
	private ProgramManager programManager;
	private ClientManager clientManager;
	private long agencyId;
	
	public void setIntegratorManager(IntegratorManager mgr) {
		this.integratorManager = mgr;
	}
	
	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}
	
	public void setClientManager(ClientManager mgr) {
		this.clientManager = mgr;
	}
	
	public void setAgencyId(long agencyId) {
		this.agencyId = agencyId;
	}
	
	/**
	 * 
	 */
	public void onMessage(Message message) {
		log.debug("Retrieved a referral");
		ClientReferral referral = null;
		
		ObjectMessage objMessage = (ObjectMessage)message;
		try {
			 referral = (ClientReferral)objMessage.getObject();
			log.debug("referral from agency#" + referral.getSourceAgencyId());
			if(referral.getSourceAgencyId().longValue() != this.agencyId) {
				//this is a new one
				try {
					//process the referral object
					clientManager.processRemoteReferral(referral);
				}catch(Exception e) {
					log.error(e);
				}
			} else {
				//i'm the source, update my object
				log.debug("need to update my client referral object");
			}
		}catch(JMSException e) {
			log.error(e);
		}
	}

}

