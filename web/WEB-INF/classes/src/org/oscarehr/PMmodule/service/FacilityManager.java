package org.oscarehr.PMmodule.service;

import org.oscarehr.PMmodule.dao.AdmissionDao;
import org.oscarehr.PMmodule.dao.ClientDao;
import org.oscarehr.PMmodule.dao.FacilityDAO;
import org.oscarehr.PMmodule.model.Facility;
import org.oscarehr.PMmodule.model.Program;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * Manager for facilities
 */
public class FacilityManager {

    private FacilityDAO facilityDAO;
    private ClientDao demographicDAO;
    private AdmissionDao admissionDao;
    
    public Facility getFacility(Integer facilityId) {
        return facilityDAO.getFacility(facilityId);
    }

    public List<Facility> getFacilities() {
        return facilityDAO.getFacilities();
    }

    @Deprecated
    public List<Program> getAssociatedPrograms(Integer facilityId) {
        return facilityDAO.getAssociatedPrograms(facilityId);
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
