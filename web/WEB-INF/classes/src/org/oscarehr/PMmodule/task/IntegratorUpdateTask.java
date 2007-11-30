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

package org.oscarehr.PMmodule.task;

import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.exception.IntegratorException;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.IntegratorManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.util.DbConnectionFilter;

public class IntegratorUpdateTask extends TimerTask {

    private static final Log log = LogFactory.getLog(IntegratorUpdateTask.class);

    private IntegratorManager integratorManager;
    private AdmissionManager admissionManager;
    private ProgramManager programManager;
    private ProviderManager providerManager;
    private ClientManager clientManager;

    public void setIntegratorManager(IntegratorManager mgr) {
        this.integratorManager = mgr;
    }

    public void setAdmissionManager(AdmissionManager mgr) {
        this.admissionManager = mgr;
    }

    public void setProgramManager(ProgramManager mgr) {
        this.programManager = mgr;
    }

    public void setProviderManager(ProviderManager mgr) {
        this.providerManager = mgr;
    }

    public void setClientManager(ClientManager mgr) {
        this.clientManager = mgr;
    }

    public void run() {
        log.debug("IntegratorUpdateTask starting");

        try {
            if (!integratorManager.isEnabled()) {
                log.debug("integrator is not enabled");
                return;
            }

            try {
                integratorManager.refreshAdmissions(admissionManager.getAdmissions());
                log.info("Admissions updated");
            }
            catch (IntegratorException e) {
                log.error(e);
            }

            try {
                integratorManager.refreshReferrals(clientManager.getReferrals());
                log.info("referrals refreshed");
            }
            catch (IntegratorException e) {
                log.error(e);
            }
        }
        finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();
            
            log.debug("IntegratorUpdateTask finished)");
        }
    }

}