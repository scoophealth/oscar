/*
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

/**
 */

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.oscarehr.PMmodule.dao.FacilityDAO;
import org.oscarehr.PMmodule.model.Facility;
import org.oscarehr.caisi_integrator.ws.client.DemographicInfoWs;
import org.oscarehr.caisi_integrator.ws.client.DemographicInfoWsService;
import org.oscarehr.caisi_integrator.ws.client.FacilityInfoWs;
import org.oscarehr.caisi_integrator.ws.client.FacilityInfoWsService;
import org.oscarehr.util.TimeClearedHashMap;

public class CaisiIntegratorManager {

    private static Log logger = LogFactory.getLog(CaisiIntegratorManager.class);

    private FacilityDAO facilityDAO;

    public void setFacilityDAO(FacilityDAO facilityDAO) {
        this.facilityDAO = facilityDAO;
    }

    /**
     * This is a simple cache mechanism which removes objects based on time. The expectation is that anyon who puts items into this will identify themselves uniquely via the map key.
     */
    private static TimeClearedHashMap<String, Object> simpleTimeCache = new TimeClearedHashMap<String, Object>(DateUtils.MILLIS_PER_HOUR, DateUtils.MILLIS_PER_HOUR);

    public boolean isIntegratorEnabled(int facilityId) {
        Facility facility = getFacility(facilityId); 
        if (facility != null && facility.isIntegratorEnabled() == true) return(true);
        else return(false);
    }

    public Facility getFacility(int facilityId)
    {
        return(facilityDAO.getFacility(facilityId));
    }
    
    private void addAuthenticationInterceptor(Facility facility, Object wsPort)
    {
        Client cxfClient = ClientProxy.getClient(wsPort);
        cxfClient.getOutInterceptors().add(new AuthenticationOutInterceptor(facility.getIntegratorUser(), facility.getIntegratorPassword()));        
    }
    
    private URL buildURL(Facility facility, String servicePoint) throws MalformedURLException
    {
        return(new URL(facility.getIntegratorUrl()+'/'+servicePoint+"?wsdl"));
    }
    
    public FacilityInfoWs getFacilityInfoWs(Facility facility) throws MalformedURLException {
        FacilityInfoWsService service = new FacilityInfoWsService(buildURL(facility, "FacilityInfoService"));
        FacilityInfoWs port = service.getFacilityInfoWsPort();

        addAuthenticationInterceptor(facility, port);
        
        return(port);
    }

    public DemographicInfoWs getDemographicInfoWs(Facility facility) throws MalformedURLException {
        DemographicInfoWsService service = new DemographicInfoWsService(buildURL(facility, "DemographicInfoService"));
        DemographicInfoWs port = service.getDemographicInfoWsPort();

        addAuthenticationInterceptor(facility, port);
        
        return(port);
    }
}
