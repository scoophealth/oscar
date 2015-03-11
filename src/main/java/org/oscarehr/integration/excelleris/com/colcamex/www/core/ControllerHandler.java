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

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ScheduledFuture;
import org.apache.log4j.Logger;
import org.oscarehr.integration.excelleris.com.colcamex.www.main.*;
import oscar.util.StringUtils;

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
public class ControllerHandler {
	
	public static Logger logger = Logger.getLogger(ControllerHandler.class);

	private static final String DEFAULT_PROVIDER = "999999";
	private static final String DEFAULT_SERVICE_NAME = "Excelleris";
	
	private ExcellerisConfigurationBean configurationBean;
	private Properties properties;
	private ServiceExecuter serviceExecuter;
	private MessageHandler messageHandler;
	private static ControllerHandler instance;
	private boolean excellerison;
	private static Integer pollFrequency;

	private ControllerHandler() {
		// cannot be directly instantiated.
	}
		
	public static ControllerHandler getInstance() {
		if(instance == null) {
			instance = new ControllerHandler();
		}
		return instance;
	}

	public static ControllerHandler getInstance(Properties properties) {
		if(instance == null) {
			instance = new ControllerHandler(properties, null);
		}
		return instance;
	}
	
	private ControllerHandler(Properties properties) {
		_init(properties, null);
	}
	
	private ControllerHandler(Properties properties, 
			ExcellerisConfigurationBean configurationBean) {
		_init(properties, configurationBean);
	}


	private void _init(Properties properties, ExcellerisConfigurationBean configurationBean) {
		Thread thread = Thread.currentThread();
		thread.setName("ExpediusControllerHandler"+"["+thread.getId()+"]");
		logger.debug("Initializing ExpediusControllerHandler. Current Thread is: " + thread.getName());
		
		setMessageHandler(new MessageHandler());
		
		HL7LabHandler labHandler = null;
		Connect connection = null;
		W3CDocumentHandler documentHandler = null;
		String providerNumber = null;
		String serviceName = null;
		String savePath = null;
		String frequency = "";
		
		if(properties != null) {
			
			setProperties(properties);
			
			if(properties.containsKey("EXCELLERIS")) {
				setExcellerison( Boolean.parseBoolean(properties.getProperty("EXCELLERIS")) );
			} 

			if(properties.containsKey("SERVICE_NUMBER")) {
				providerNumber = properties.getProperty("SERVICE_NUMBER").trim();
			} else {
				providerNumber = DEFAULT_PROVIDER;
			}
			
			if(properties.containsKey("SERVICE_NAME")) {
				serviceName = properties.getProperty("SERVICE_NAME").trim();
			}else {
				serviceName = DEFAULT_SERVICE_NAME;
			}
			
			if(properties.containsKey("HL7_SAVE_PATH")) {
				savePath = properties.getProperty("HL7_SAVE_PATH").trim();
			}
			
			if(properties.containsKey("PULL_INTERVAL")) {			
				frequency = properties.getProperty("PULL_INTERVAL").trim();
			} 
				
			if ( StringUtils.isNumeric( frequency ) ){				
				 setPollFrequency( frequency );				
			} else {
				logger.error("Missing poll frequency in properties. Setting to default.");
			}
	
		} else {
			logger.error("Properties file not provided.");
			return;
		}

		setConfigurationBean(configurationBean);

				
		labHandler = new HL7LabHandler(providerNumber);
		labHandler.setServiceName(serviceName);
		labHandler.setSavePath(savePath);						
	
		
		documentHandler = new W3CDocumentHandler();
		connection = Connect.getInstance(documentHandler);
	
		if( isExcellerison() ){
			
			logger.info("Setting Excelleris service ");	
			
			AbstractConnectionController excelleris = new ExcellerisController(getProperties(), getConfigurationBean());
			excelleris.setServiceName(getConfigurationBean().getServiceName());
			excelleris.setDocumentHandler(documentHandler);
			excelleris.setConnection(connection);

			serviceExecuter = ServiceExecuter.getInstance();
			serviceExecuter.setService(excelleris);

			if(this.getMessageHandler() != null) {
				serviceExecuter.setMessageHandler(this.getMessageHandler());
			}
			if(labHandler != null) {
				serviceExecuter.setLabHandler(labHandler);	
			}
		}

	}
	
	/**
	 * Stop the timer
	 * @param pollTimer
	 */
	public void stop() {
		if(PollTimer.isRunning()) {	
			List<Runnable> stopStatus = PollTimer.stop();
			
			for(Runnable status : stopStatus) {
				if(status instanceof ScheduledFuture) {
					logger.debug("Stopping thread(s): "+status.toString());
					((ScheduledFuture) status).cancel(true);				
				}
						
			}

			getMessageHandler().setServiceStatus(Boolean.FALSE);
			
		}
	}
    
	/**
	 * Start the timer to execute timed lab downloads
	 * @param pollTimer
	 */
    public void start() {
    	
    	logger.info("<-- STARTING AUTO DOWNLOAD -->");
    	
    	boolean clientStatus = isClientsReady();    	
    	logger.info("Client status is: "+clientStatus);

    	if( clientStatus){

	    	if(PollTimer.isRunning()) {
	    		stop();
	    	}
	    	
	    	if(! PollTimer.isRunning()) {		    		
				PollTimer.start(serviceExecuter, getPollFrequency());					
				getMessageHandler().setServiceStatus(PollTimer.isRunning());
				getMessageHandler().setLastStartup(new Date());
	    	}
	
    		    	
    	}
    }

    public ExcellerisConfigurationBean getConfigurationBean() {
		return configurationBean;
	}

	public void setConfigurationBean(ExcellerisConfigurationBean configurationBean) {
		this.configurationBean = configurationBean;
	}

    public boolean isHostStatus() {    		
    	return serviceExecuter.checkHostStatus();
	}

	public boolean isClientsReady() {   	   	
    	return serviceExecuter.checkClientStatus();
    }
	
	public static Integer getPollFrequency() {
		return pollFrequency;
	}
	
	public static void setPollFrequency(String pollFrequency) {
		setPollFrequency( Integer.parseInt( pollFrequency ) );
	}

	public static void setPollFrequency(Integer pollFrequency) {
		ControllerHandler.pollFrequency = pollFrequency;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public MessageHandler getMessageHandler() {
		return messageHandler;
	}

	public void setMessageHandler(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	public boolean isExcellerison() {
		return excellerison;
	}

	private void setExcellerison(boolean excellerison) {
		this.excellerison = excellerison;
	}

}
