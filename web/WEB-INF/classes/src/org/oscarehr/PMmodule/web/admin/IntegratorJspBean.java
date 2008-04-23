package org.oscarehr.PMmodule.web.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.oscarehr.PMmodule.model.Facility;
import org.oscarehr.PMmodule.service.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.client.FacilityInfoWs;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class IntegratorJspBean {
    private static CaisiIntegratorManager caisiIntegratorManager=(CaisiIntegratorManager)SpringUtils.getBean("caisiIntegratorManager");
    
    public static ArrayList<Properties> getIntegratorFacilityCommunity(int facilityId) throws IOException
    {
        Facility facility=caisiIntegratorManager.getFacility(facilityId);
        FacilityInfoWs facilityInfoWs=caisiIntegratorManager.getFacilityInfoWs(facility);
        
        List<byte[]> facilityInfoBytes=facilityInfoWs.getAllFacilityInfo();
        
        ArrayList<Properties> al=new ArrayList<Properties>();
        for (byte[] b : facilityInfoBytes)
        {
            Properties p=MiscUtils.xmlByteArrayToProperties(b);
            al.add(p);
        }
        
        return(al);
    }
}
