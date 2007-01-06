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

