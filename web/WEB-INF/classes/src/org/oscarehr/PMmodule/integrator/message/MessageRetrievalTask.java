package org.oscarehr.PMmodule.integrator.message;

import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jencks.JCAConnector;
import org.oscarehr.PMmodule.service.AgencyManager;
import org.springframework.jms.core.JmsTemplate;

public class MessageRetrievalTask extends TimerTask {

	private static Log log = LogFactory.getLog(MessageRetrievalTask.class);

	private AgencyManager agencyManager;
	private JmsTemplate jmsTemplate;
	private JCAConnectorFactory updateConnectorFactory;
	private JCAConnectorFactory referralConnectorFactory;
	private static JCAConnector updateConnector = null;
	private static JCAConnector referralConnector = null;
	
	public void setUpdateConnector(JCAConnectorFactory jca) {
		this.updateConnectorFactory = jca;
	}
	
	public void setReferralConnector(JCAConnectorFactory jca) {
		this.referralConnectorFactory = jca;
	}
	
	public void setAgencyManager(AgencyManager agencyManager) {
		this.agencyManager = agencyManager;
	}
	
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	
	public void run() {
		log.debug("MESSAGE RETRIEVAL TASK RUNNING");
		try {
			if(updateConnector != null) {
				updateConnectorFactory.stopConsumption(updateConnector);
			}
			updateConnector = updateConnectorFactory.startConsumption();
		}catch(Exception e) {
			log.error(e);
		}
		
		try {
			if(referralConnector != null) {
				referralConnectorFactory.stopConsumption(referralConnector);
			}
			referralConnector = referralConnectorFactory.startConsumption();
		}catch(Exception e) {
			log.error(e);
		}
	}

}
