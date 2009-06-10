package org.oscarehr.PMmodule.web.admin;

import java.io.IOException;
import java.util.List;

import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.CachedFacility;

public class IntegratorJspBean {    
    public static List<CachedFacility> getIntegratorFacilityCommunity() throws IOException
    {
    	CaisiIntegratorManager.flushCachedFacilityInfo();
        return(CaisiIntegratorManager.getRemoteFacilities());
    }
}
