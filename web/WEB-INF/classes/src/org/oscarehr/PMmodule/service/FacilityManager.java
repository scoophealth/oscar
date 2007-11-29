package org.oscarehr.PMmodule.service;

import org.oscarehr.PMmodule.dao.FacilityDAO;
import org.oscarehr.PMmodule.model.Facility;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * Manager for facilities
 */
public class FacilityManager {

    private FacilityDAO facilityDAO;

    public Facility getFacility(Integer facilityId) {
        return facilityDAO.getFacility(facilityId);
    }

    public List<Facility> getFacilities() {
        return facilityDAO.getFacilities();
    }

    public void saveFacility(Facility facility) {
        facilityDAO.saveFacility(facility);
    }

    public FacilityDAO getFacilityDAO() {
        return facilityDAO;
    }

    @Required
    public void setFacilityDAO(FacilityDAO facilityDAO) {
        this.facilityDAO = facilityDAO;
    }

}
