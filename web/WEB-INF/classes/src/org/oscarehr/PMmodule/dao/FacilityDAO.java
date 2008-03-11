package org.oscarehr.PMmodule.dao;

import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.oscarehr.PMmodule.model.Facility;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.util.TimeClearedHashMap;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.util.SqlUtils;

/**
 * Data access object for retrieving, creating, and updating facilities.
 */
public class FacilityDAO extends HibernateDaoSupport {

    public Facility getFacility(Integer id) {
        return (Facility) getHibernateTemplate().get(Facility.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Facility> getFacilities() {
        String query = "from Facility f";
        return getHibernateTemplate().find(query);
    }

    @SuppressWarnings("unchecked")
    public List<Facility> getActiveFacilities() {
        String query = "from Facility f where disabled=false";
        return getHibernateTemplate().find(query);
    }
    
    
    @SuppressWarnings("unchecked")
    public List<Program> getAssociatedPrograms(Integer facilityId) {
        return getHibernateTemplate().find("select distinct p from Program p, Facility f, Room r where p.id = r.programId and r.facilityId = f.id and f.id = ?", facilityId);
    }
    
    public void saveFacility(Facility facility) {
        getHibernateTemplate().saveOrUpdate(facility);
        getHibernateTemplate().flush();
        getHibernateTemplate().refresh(facility);
    }
}
