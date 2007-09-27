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

import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jencks.JCAConnector;
import org.oscarehr.PMmodule.service.AgencyManager;
import org.oscarehr.util.DbConnectionFilter;
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
            try {
                if (updateConnector != null) {
                    updateConnectorFactory.stopConsumption(updateConnector);
                }
                updateConnector = updateConnectorFactory.startConsumption();
            }
            catch (Exception e) {
                log.error(e);
            }

            try {
                if (referralConnector != null) {
                    referralConnectorFactory.stopConsumption(referralConnector);
                }
                referralConnector = referralConnectorFactory.startConsumption();
            }
            catch (Exception e) {
                log.error(e);
            }
        }
        finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();
        }
    }

}
