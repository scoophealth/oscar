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

package org.oscarehr.PMmodule.caisi_integrator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.dao.DemographicDAO;
import org.oscarehr.PMmodule.dao.FacilityDAO;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Facility;
import org.oscarehr.caisi_integrator.ws.client.CachedDemographicInfo;
import org.oscarehr.caisi_integrator.ws.client.CachedFacilityInfo;
import org.oscarehr.caisi_integrator.ws.client.DemographicInfoWs;
import org.oscarehr.caisi_integrator.ws.client.FacilityInfoWs;
import org.oscarehr.util.DbConnectionFilter;
import org.springframework.beans.BeanUtils;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

public class CaisiIntegratorUpdateTask extends TimerTask {

    private static final Log logger = LogFactory.getLog(CaisiIntegratorUpdateTask.class);

    private CaisiIntegratorManager caisiIntegratorManager;
    private FacilityDAO facilityDAO;
    private DemographicDAO demographicDAO;

    public void setCaisiIntegratorManager(CaisiIntegratorManager mgr) {
        this.caisiIntegratorManager = mgr;
    }

    public void setFacilityDAO(FacilityDAO facilityDAO) {
        this.facilityDAO = facilityDAO;
    }

    public void setDemographicDAO(DemographicDAO demographicDAO) {
        this.demographicDAO = demographicDAO;
    }

    public void run() {
        logger.debug("CaisiIntegratorUpdateTask starting");

        try {
            List<Facility> facilities = facilityDAO.getFacilities();

            for (Facility facility : facilities) {
                if (facility.isDisabled() == false && facility.isIntegratorEnabled() == true) {
                    pushAllFacilityData(facility);
                }
            }
        }
        catch (WebServiceException e) {
            logger.warn("Error connecting to integrator. "+e.getMessage());
            logger.debug("Error connecting to integrator.", e);
        }
        catch (Exception e) {
            logger.error("unexpected error occurred", e);
        }
        finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();

            logger.debug("CaisiIntegratorUpdateTask finished)");
        }
    }

    private void pushAllFacilityData(Facility facility) throws IOException {
        logger.debug("Pushing data for facility : " + facility.getId());

        // check all parameters are present
        String integratorBaseUrl = facility.getIntegratorUrl();
        String user = facility.getIntegratorUser();
        String password = facility.getIntegratorPassword();

        if (integratorBaseUrl == null || user == null || password == null) {
            logger.warn("Integrator is enabled but information is incomplete. facilityId=" + facility.getId() + ", user=" + user + ", password=" + password + ", url=" + integratorBaseUrl);
            return;
        }

        // get the time here so there's a slight over lap in actual runtime and activity time, other wise you'll have a gap, better to unnecessarily send a few more records than to miss some.
        Date currentPushTime = new Date();

        // do all the sync work
        pushFacilityInfo(facility);
        pushDemographics(facility);

        // update late push time only if an exception didn't occur
        // re-get the facility as the sync time could be very long and changes may have been made to the facility.
        facility = facilityDAO.getFacility(facility.getId());
        facility.setIntegratorLastPushTime(currentPushTime);
        facilityDAO.saveFacility(facility);
    }

    private void pushDemographics(Facility facility) throws MalformedURLException {
        List<Integer> demographicIds = DemographicDAO.getDemographicIdsAdmittedIntoFacility(facility.getId());
        DemographicInfoWs service = caisiIntegratorManager.getDemographicInfoWs(facility.getId());

        for (Integer demographicId : demographicIds) {
            logger.debug("pushing demographicInfo facilityId:" + facility.getId() + ", demographicId:" + demographicId);

            Demographic demographic = demographicDAO.getDemographicById(demographicId);

            CachedDemographicInfo cachedDemographicInfo = new CachedDemographicInfo();

            XMLGregorianCalendar cal = new XMLGregorianCalendarImpl();
            if (demographic.getYearOfBirth() != null) cal.setYear(Integer.parseInt(demographic.getYearOfBirth()));
            if (demographic.getMonthOfBirth() != null) cal.setMonth(Integer.parseInt(demographic.getMonthOfBirth()));
            if (demographic.getDateOfBirth() != null) cal.setDay(Integer.parseInt(demographic.getDateOfBirth()));
            cachedDemographicInfo.setBirthDate(cal);

            cachedDemographicInfo.setCity(demographic.getCity());
            cachedDemographicInfo.setFacilityDemographicId(demographic.getDemographicNo());
            cachedDemographicInfo.setFirstName(demographic.getFirstName());
            cachedDemographicInfo.setGender(demographic.getSex());
            cachedDemographicInfo.setHin(demographic.getHin());
            cachedDemographicInfo.setLastName(demographic.getLastName());
            cachedDemographicInfo.setProvince(demographic.getProvince());
            cachedDemographicInfo.setSin(demographic.getSin());

            service.setCachedDemographicInfo(cachedDemographicInfo);
        }
    }

    private void pushFacilityInfo(Facility facility) throws IOException {

        CachedFacilityInfo cachedFacilityInfo = new CachedFacilityInfo();
        BeanUtils.copyProperties(facility, cachedFacilityInfo, new String[] { "id" });

        FacilityInfoWs service = caisiIntegratorManager.getFacilityInfoWs(facility.getId());

        logger.debug("pushing facilityInfo");
        service.setMyFacilityInfo(cachedFacilityInfo);
    }
}
