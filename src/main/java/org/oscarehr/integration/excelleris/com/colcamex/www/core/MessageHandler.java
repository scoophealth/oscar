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
public class MessageHandler {
	
	public static Logger logger = Logger.getLogger(MessageHandler.class);
	private String statusMessage;
	private ArrayList<String> errorMessages;
	private Date lastDownload;
	private boolean serviceStatus;
	private Date lastStartup;
	
	public MessageHandler() {
		// default constructor.
	}

	/**
	 * Adds an error message for immediate display to the user.
	 * @param message
	 */
	public void addErrorMessage(String message) {
		if(errorMessages != null) {
			errorMessages.add(message);
		}
	}
	
	public ArrayList<String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(ArrayList<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * Sets a status feedback message to the user. 
	 * @param statusMessage
	 */
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public Date getLastDownload() {
		return lastDownload;
	}

	public void setLastDownload(Date lastDownload) {
		this.lastDownload = lastDownload;
	}
	
	public Date getLastStartup() {
		return lastStartup;
	}

	public void setLastStartup(Date lastStartup) {
		this.lastStartup = lastStartup;
	}

	/**
	 * Indicates that the serviceExecuter completed correctly and is
	 * still running.
	 * @return
	 */
	public boolean isServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(boolean serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

}
