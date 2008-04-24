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

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.FacilityDAO;
import org.oscarehr.PMmodule.model.Facility;
import org.oscarehr.PMmodule.service.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.client.FacilityInfoWs;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

public class CaisiIntegratorUpdateTask extends TimerTask {

    private static final Log logger = LogFactory.getLog(CaisiIntegratorUpdateTask.class);

    private CaisiIntegratorManager caisiIntegratorManager;
    private FacilityDAO facilityDAO;

    public void setCaisiIntegratorManager(CaisiIntegratorManager mgr) {
        this.caisiIntegratorManager = mgr;
    }

    public void setFacilityDAO(FacilityDAO facilityDAO) {
        this.facilityDAO = facilityDAO;
    }

    public void run() {
        logger.debug("CaisiIntegratorUpdateTask starting");

        try {
            List<Facility> facilities = facilityDAO.getFacilities();
            for (Facility facility : facilities) {
                if (facility.isDisabled() == false && facility.isIntegratorEnabled() == true) {
                    updateFacility(facility);
                }
            }
        }
        catch (Exception e) {
            logger.error("unexpected error occurred", e);
        }
        finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();

            logger.debug("CaisiIntegratorUpdateTask finished)");
        }
    }

    private void updateFacility(Facility facility) {

        try {
            // get the time here so there's a slight over lap in actualy runtime and activity time, other wise you'll have a gap, better to unnecessarily send a few more records than to miss some.
            Date currentPushTime = new Date();

            // do all the sync work
            pushFacilityInfo(facility);
            
            // update late push time only if an exception didn't occur
            // reget the facility as the sync time could be very long and changes may have been made to the facility.
            facility=facilityDAO.getFacility(facility.getId());
            facility.setIntegratorLastPushTime(currentPushTime);
            facilityDAO.saveFacility(facility);
        }
        catch (Exception e) {
            logger.error("Unexpected error.", e);
        }
    }

    private void pushFacilityInfo(Facility facility) throws IOException {
        String integratorBaseUrl = facility.getIntegratorUrl();
        String user = facility.getIntegratorUser();
        String password = facility.getIntegratorPassword();

        if (integratorBaseUrl == null || user == null || password == null) {
            logger.warn("Integrator is enabled but information is incomplete. facilityId=" + facility.getId() + ", user=" + user + ", password=" + password + ", url=" + integratorBaseUrl);
            return;
        }

        System.err.println("INTEGRATOR : " + facility.getName() + ", user=" + user + ", password=" + password + ", url=" + integratorBaseUrl);
        FacilityInfoWs facilityInfoWs = caisiIntegratorManager.getFacilityInfoWs(facility);

        Properties p = new Properties();
        p.setProperty("name", facility.getName());
        p.setProperty("description", facility.getDescription());
        p.setProperty("contactName", facility.getContactName());
        p.setProperty("contactEmail", facility.getContactEmail());
        p.setProperty("contactPhone", facility.getContactPhone());
        facilityInfoWs.setMyFacilityInfo(MiscUtils.propertiesToXmlByteArray(p));
    }
}
