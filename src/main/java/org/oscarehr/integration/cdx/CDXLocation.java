package org.oscarehr.integration.cdx;

import ca.uvic.leadlab.obibconnector.facades.exceptions.OBIBException;
import ca.uvic.leadlab.obibconnector.facades.registry.IClinic;
import ca.uvic.leadlab.obibconnector.facades.registry.ISearchClinic;
import ca.uvic.leadlab.obibconnector.impl.registry.SearchClinic;
import org.oscarehr.util.MiscUtils;

import java.util.ArrayList;
import java.util.List;

public class CDXLocation {

    private final CDXConfiguration config;

    public CDXLocation() {
        config = new CDXConfiguration();
    }

    public List<IClinic> findLocationByName(String name) {
        List<IClinic> clinics = new ArrayList<IClinic>();
        try {
            ISearchClinic searchClinic = new SearchClinic(config);
            clinics = searchClinic.findByName(name);
        } catch (OBIBException e) {
            MiscUtils.getLogger().info("findByName('"+name+"') returned: " + e.getMessage());
        }
        return clinics;
    }

    public List<IClinic> findLocationById(String id) {
        List<IClinic> clinics = new ArrayList<IClinic>();
        try {
            ISearchClinic searchClinic = new SearchClinic(config);
            clinics = searchClinic.findByID(id);
        } catch (OBIBException e) {
            MiscUtils.getLogger().info("findById('"+id+"') returned: " + e.getMessage());
        }
        return clinics;
    }
}
