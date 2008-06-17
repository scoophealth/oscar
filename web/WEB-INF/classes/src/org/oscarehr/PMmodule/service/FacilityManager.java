package org.oscarehr.PMmodule.service;

import java.util.List;

import org.oscarehr.PMmodule.dao.FacilityDao;
import org.oscarehr.PMmodule.model.Facility;
import org.springframework.beans.factory.annotation.Required;

/**
 * Manager for facilities
 */
public class FacilityManager {

    private FacilityDao facilityDao;
    
    public Facility getFacility(Integer facilityId) {
        return facilityDao.getFacility(facilityId);
    }

    public List<Facility> getFacilities() {
        return facilityDao.getFacilities();
    }

    public void saveFacility(Facility facility) {
        facilityDao.saveFacility(facility);
    }

    public FacilityDao getFacilityDao() {
        return facilityDao;
    }

    @Required
    public void setFacilityDao(FacilityDao facilityDao) {
        this.facilityDao = facilityDao;
    }

    
}
