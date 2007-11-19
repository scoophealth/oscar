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
//		integratorManager.updateProgramData(programs);
		dateLastUpdated = new Date();
		log.debug("updated program info @ " + dateLastUpdated);
		
	}

}
