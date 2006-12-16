package org.oscarehr.PMmodule.integrator.message;

import java.util.Date;
import java.util.List;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.service.IntegratorManager;
import org.oscarehr.PMmodule.service.ProgramManager;

public class RefreshProgramInfoMessage implements MessageListener {

	private static Log log = LogFactory.getLog(RefreshProgramInfoMessage.class);
	
	private IntegratorManager integratorManager;
	private ProgramManager programManager;
	
	private static Date dateLastUpdated = new Date();
	
	public void setIntegratorManager(IntegratorManager mgr) {
		this.integratorManager = mgr;
	}
	
	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}
	/**
	 * Message is empty - we should update our information
	 */
	public void onMessage(Message message) {
		log.debug("Received a RefreshProgramInfo message from the Integrator");
		System.out.println("Received a RefreshProgramInfo message from the Integrator");
		
		if((new Date().getTime() - dateLastUpdated.getTime()) < (60*1000)) {
			log.debug("Skipping update...under a minute");
			return;
		}
		List programs = programManager.getProgramsByAgencyId("0");
		integratorManager.updateProgramData(programs);
		dateLastUpdated = new Date();
		log.debug("updated program info @ " + dateLastUpdated);
		
	}

}
