/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.integration.excelleris.com.colcamex.www.core;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
/**
 * @author Dennis Warren Colcamex Resources
 * 
 * Major Contributors: 
 *  OSCARprn
 *  NERD
 *   
 * This community edition of Expedius is for use at your own risk, without warranty, and 
 * support. 
 * 
 */
public class ServiceExecuter implements Runnable {

	private static Logger logger = Logger.getLogger("ServiceExecuter");

	private AbstractConnectionController service;
	private static ServiceExecuter instance = null;
	private Date lastRunTime;
	private boolean serviceStatus;
	private MessageHandler messageHandler;
	private HL7LabHandler labHandler;
	
	private ServiceExecuter() {
		// default
	}
	
	public static ServiceExecuter getInstance() {
		if(ServiceExecuter.instance == null) {						
			instance = new ServiceExecuter();
			logger.info("Instantiating Service Executer.");
		}
		return instance;
	}
	
	private boolean execute() {
		return execute(0);
	}
	
	private boolean execute(int mode) {
		
		boolean status = true;

		if(getService() != null) {

			logger.info("Executing service " + getService().getServiceName());			
			status = execute(getService(), mode); 

		} else {
			status = Boolean.FALSE;
		}
		
		return status;
	}
	
	private boolean execute(AbstractConnectionController service, int mode) {
		
		boolean status = Boolean.FALSE;
		
		// set the connection
		if(service != null) {

			if(getMessageHandler() != null) {
				service.setMessageHandler(getMessageHandler());
			}
			
			// inject the lab handler.
			if(getLabHandler() != null) {
				service.setLabHandler(getLabHandler());
			}
			
			if(mode > 0) {
				status = service.start(mode);	
			} else {
				status = service.start();
			}
		}
		
		return status;			 
	}

	/**
	 * Checks if the service host is available.
	 * @return
	 */
	public boolean checkHostStatus() {
		return execute(AbstractConnectionController.LOGIN_MODE);
	}
	
	/**
	 * Performs a login routine for each of the lab distribution services 
	 * to confirm connection status.
	 * @return
	 */
	public boolean checkClientStatus() {

		boolean status = Boolean.TRUE;
		

		if(! getService().getConfigurationBean().isCertificateInstalled()) {
			
			logger.error("Missing security certificate for service " + getService().getServiceName());
			
			if(getMessageHandler() != null) {
				getMessageHandler().addErrorMessage("Cannot start client for " + getService().getServiceName() + 
						" - missing security certificate.");
			}
			status = Boolean.FALSE;
		} 
		
		if(! getService().getConfigurationBean().isLoginInfoSet()) {
			
			logger.error("Missing login credentials for service " + getService().getServiceName());
			
			if(getMessageHandler() != null) {
				getMessageHandler().addErrorMessage("Cannot start client for " + getService().getServiceName() + 
						" - missing login information.");
			}
			status = Boolean.FALSE;
		}
		
		
		return status;
	}

	
	/**
	 * Executes the lab retrieval sequence for each of the distribution services.
	 */
	@Override
	public void run() {
		Thread thread = Thread.currentThread();
		thread.setName("ServiceExecuter"+"["+thread.getId()+"]");
		logger.debug("Running ServiceExecuter [" + thread.getName() + "]");
		
		boolean status = Boolean.FALSE;
		
		// reset error messages for next run.
		getMessageHandler().setErrorMessages(new ArrayList<String>());
		
		// log run time.
		setLastRunTime(new Date());

		if(getService() != null) {
			status = execute();
		} else {
			logger.error("No Service to execute");
		}
		
		logger.info("Service execution status is: " + status);

		// set the service status on every run to keep track.
		setServiceStatus(PollTimer.isRunning());
	}

	/**
	 * @return Array of ExpediusConnectionControllers.
	 */
	public AbstractConnectionController getService() {
		return service;
	}

	public void setService(AbstractConnectionController service) {
		this.service = service;
	}

	public Date getLastRunTime() {
		return lastRunTime;
	}

	public void setLastRunTime(Date lastRunTime) {
		this.lastRunTime = lastRunTime;
		
		if(getMessageHandler() != null) {
			getMessageHandler().setLastDownload(this.lastRunTime);
		}
	}

	public MessageHandler getMessageHandler() {
		return messageHandler;
	}

	/**
	 * Set an ExpediusMessageHandler that tracks and stores status 
	 * and error messages.
	 * @param messageHandler
	 */
	public void setMessageHandler(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	public boolean isServiceStatus() {
		return serviceStatus;
	}

	private void setServiceStatus(boolean serviceStatus) {
		this.serviceStatus = serviceStatus;
		if(getMessageHandler() != null) {
			getMessageHandler().setServiceStatus(serviceStatus);
		}
	}

	public HL7LabHandler getLabHandler() {
		return labHandler;
	}

	public void setLabHandler(HL7LabHandler labHandler) {
		this.labHandler = labHandler;
	}

}
