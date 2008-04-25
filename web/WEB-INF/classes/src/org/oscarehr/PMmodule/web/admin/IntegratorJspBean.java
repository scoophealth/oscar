package org.oscarehr.PMmodule.web.admin;

import java.io.IOException;
import java.util.List;

import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.model.Facility;
import org.oscarehr.caisi_integrator.ws.client.CachedFacilityInfo;
import org.oscarehr.caisi_integrator.ws.client.FacilityInfoWs;
import org.oscarehr.util.SpringUtils;

public class IntegratorJspBean {
    private static CaisiIntegratorManager caisiIntegratorManager=(CaisiIntegratorManager)SpringUtils.getBean("caisiIntegratorManager");
    
    public static List<CachedFacilityInfo> getIntegratorFacilityCommunity(int facilityId) throws IOException
    {
        Facility facility=caisiIntegratorManager.getFacility(facilityId);
        FacilityInfoWs facilityInfoWs=caisiIntegratorManager.getFacilityInfoWs(facility);
        
        return(facilityInfoWs.getAllFacilityInfo());
    }
}
