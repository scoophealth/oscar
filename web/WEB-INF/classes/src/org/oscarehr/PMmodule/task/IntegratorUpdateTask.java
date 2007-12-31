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

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.integrator.model.transfer.GetReferralResponseTransfer;
import org.caisi.integrator.model.transfer.ProgramTransfer;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.service.IntegratorManager;
import org.oscarehr.util.DbConnectionFilter;

public class IntegratorUpdateTask extends TimerTask {

    private static final Log log = LogFactory.getLog(IntegratorUpdateTask.class);

    private IntegratorManager integratorManager;
    private ProgramDao programDao;

    public void setIntegratorManager(IntegratorManager mgr) {
        this.integratorManager = mgr;
    }

    public void setProgramDao(ProgramDao programDao) {
        this.programDao = programDao;
    }

    public void run() {
        log.debug("IntegratorUpdateTask starting");

        try {
            if (!integratorManager.isEnabled()) {
                log.debug("integrator is not enabled");
                return;
            }

            pushPrograms();
        }
        finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();

            log.debug("IntegratorUpdateTask finished)");
        }
    }


    private void pushPrograms() {
        try {
            List<Program> programs=programDao.getActiveUserDefinedPrograms();
            ArrayList<ProgramTransfer> al=new ArrayList<ProgramTransfer>();
            for (Program program : programs) al.add(program.getProgramTransfer());
            integratorManager.publishPrograms(al.toArray(new ProgramTransfer[0]));
        }
        catch (Exception e) {
            log.error("Unexpected error occurred.", e);
        }
    }

}