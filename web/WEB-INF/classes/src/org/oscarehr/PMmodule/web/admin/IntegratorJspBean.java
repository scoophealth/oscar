package org.oscarehr.PMmodule.web.admin;

import java.io.IOException;
import java.util.List;

import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.client.CachedFacility;
import org.oscarehr.util.SpringUtils;

public class IntegratorJspBean {
    private static CaisiIntegratorManager caisiIntegratorManager=(CaisiIntegratorManager)SpringUtils.getBean("caisiIntegratorManager");
    
    public static List<CachedFacility> getIntegratorFacilityCommunity(int facilityId) throws IOException
    {
    	caisiIntegratorManager.flushCachedFacilityInfo(facilityId);
        return(caisiIntegratorManager.getRemoteFacilities(facilityId));
    }
}
