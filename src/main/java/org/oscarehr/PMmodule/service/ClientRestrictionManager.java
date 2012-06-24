/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.oscarehr.PMmodule.dao.ProgramClientRestrictionDAO;
import org.oscarehr.PMmodule.exception.ClientAlreadyRestrictedException;
import org.oscarehr.PMmodule.model.ProgramClientRestriction;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

/**
 * Manage client restrictions
 */
@Transactional
public class ClientRestrictionManager {

    private ProgramClientRestrictionDAO programClientRestrictionDAO;

    public List<ProgramClientRestriction> getActiveRestrictionsForProgram(int programId, Date asOfDate) {
        // check dao for restriction
        Collection<ProgramClientRestriction> pcrs = programClientRestrictionDAO.findForProgram(programId);
        List<ProgramClientRestriction> returnPcrs = new ArrayList<ProgramClientRestriction>();
        if (pcrs != null && !pcrs.isEmpty()) {
            for (ProgramClientRestriction pcr : pcrs) {
                if (pcr.getStartDate().getTime() <= asOfDate.getTime() && pcr.getEndDate().getTime() <= pcr.getEndDate().getTime()) returnPcrs.add(pcr);
            }
        }
        return returnPcrs;
    }

    public List<ProgramClientRestriction> getDisabledRestrictionsForProgram(Integer programId, Date asOfDate) {
        // check dao for restriction
        Collection<ProgramClientRestriction> pcrs = programClientRestrictionDAO.findDisabledForProgram(programId);
        List<ProgramClientRestriction> returnPcrs = new ArrayList<ProgramClientRestriction>();
        if (pcrs != null && !pcrs.isEmpty()) {
            for (ProgramClientRestriction pcr : pcrs) {
                if (pcr.getStartDate().getTime() <= asOfDate.getTime() && pcr.getEndDate().getTime() <= pcr.getEndDate().getTime()) returnPcrs.add(pcr);
            }
        }
        return returnPcrs;
    }

    public List<ProgramClientRestriction> getActiveRestrictionsForClient(int demographicNo, Date asOfDate) {
        // check dao for restriction
        Collection<ProgramClientRestriction> pcrs = programClientRestrictionDAO.findForClient(demographicNo);
        List<ProgramClientRestriction> returnPcrs = new ArrayList<ProgramClientRestriction>();
        if (pcrs != null && !pcrs.isEmpty()) {
            for (ProgramClientRestriction pcr : pcrs) {
                if (pcr.getStartDate().getTime() <= asOfDate.getTime() && pcr.getEndDate().getTime() <= pcr.getEndDate().getTime()) returnPcrs.add(pcr);
            }
        }
        return returnPcrs;
    }

    public List<ProgramClientRestriction> getActiveRestrictionsForClient(int demographicNo, int facilityId, Date asOfDate) {
        // check dao for restriction
        Collection<ProgramClientRestriction> pcrs = programClientRestrictionDAO.findForClient(demographicNo, facilityId);
        List<ProgramClientRestriction> returnPcrs = new ArrayList<ProgramClientRestriction>();
        if (pcrs != null && !pcrs.isEmpty()) {
            for (ProgramClientRestriction pcr : pcrs) {
                if (pcr.getStartDate().getTime() <= asOfDate.getTime() && pcr.getEndDate().getTime() <= pcr.getEndDate().getTime()) returnPcrs.add(pcr);
            }
        }
        return returnPcrs;
    }

    public List<ProgramClientRestriction> getDisabledRestrictionsForClient(int demographicNo, Date asOfDate) {
        // check dao for restriction
        Collection<ProgramClientRestriction> pcrs = programClientRestrictionDAO.findDisabledForClient(demographicNo);
        List<ProgramClientRestriction> returnPcrs = new ArrayList<ProgramClientRestriction>();
        if (pcrs != null && !pcrs.isEmpty()) {
            for (ProgramClientRestriction pcr : pcrs) {
                if (pcr.getStartDate().getTime() <= asOfDate.getTime() && pcr.getEndDate().getTime() <= pcr.getEndDate().getTime()) returnPcrs.add(pcr);
            }
        }
        return returnPcrs;
    }

    public ProgramClientRestriction checkClientRestriction(int programId, int demographicNo, Date asOfDate) {
        // check dao for restriction
        Collection<ProgramClientRestriction> pcrs = programClientRestrictionDAO.find(programId, demographicNo);
        if (pcrs != null && !pcrs.isEmpty()) {
            for (ProgramClientRestriction pcr : pcrs) {
                if (pcr.getStartDate().getTime() <= asOfDate.getTime() && asOfDate.getTime() <= pcr.getEndDate().getTime()) return pcr;
            }
        }
        return null;
    }

    public void saveClientRestriction(ProgramClientRestriction restriction) throws ClientAlreadyRestrictedException {
        if (restriction.getId() == null) {
            ProgramClientRestriction result = checkClientRestriction(restriction.getProgramId(), restriction.getDemographicNo(), new Date());
            if (result != null) throw new ClientAlreadyRestrictedException("the client has already been service restricted in this program");
        }

        programClientRestrictionDAO.save(restriction);
    }

    public void terminateEarly(int programClientRestrictionId, String providerNo) {
        ProgramClientRestriction x = programClientRestrictionDAO.find(programClientRestrictionId);

        if (x != null) {
            x.setEarlyTerminationProvider(providerNo);
            x.setEndDate(new Date());
            programClientRestrictionDAO.save(x);
        }
    }

    public void disableClientRestriction(int restrictionId) {
        ProgramClientRestriction pcr = programClientRestrictionDAO.find(restrictionId);
        pcr.setEnabled(false);
        try {
            saveClientRestriction(pcr);
        }
        catch (ClientAlreadyRestrictedException e) {
            // this exception should not happen here, so toss it up as a runtime exception to be caught higher up
            throw new RuntimeException(e);
        }
    }

    public void enableClientRestriction(Integer restrictionId) {
        ProgramClientRestriction pcr = programClientRestrictionDAO.find(restrictionId);
        pcr.setEnabled(true);
        try {
            saveClientRestriction(pcr);
        }
        catch (ClientAlreadyRestrictedException e) {
            // this exception should not happen here, so toss it up as a runtime exception to be caught higher up
            throw new RuntimeException(e);
        }
    }

    public ProgramClientRestrictionDAO getProgramClientRestrictionDAO() {
        return programClientRestrictionDAO;
    }

    @Required
    public void setProgramClientRestrictionDAO(ProgramClientRestrictionDAO programClientRestrictionDAO) {
        this.programClientRestrictionDAO = programClientRestrictionDAO;
    }

}
