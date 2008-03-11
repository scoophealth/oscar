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
    
    public List<Integer> getFacilityIdsByProgramId(int programId)
    {
        // select program_id,facility_id from room;
        
        String sqlCommand="select facility_id from room where room.program_id="+programId;
        return(SqlUtils.selectIntList(sqlCommand));
    }
    
    private static TimeClearedHashMap<Integer, List<Integer>> facilityIdsByProgramIdCache=new TimeClearedHashMap<Integer, List<Integer>>(DateUtils.MILLIS_PER_MINUTE*5, DateUtils.MILLIS_PER_MINUTE);
    public List<Integer> getCachedFacilityIdsByProgramId(int programId)
    {
        List<Integer> results=facilityIdsByProgramIdCache.get(programId);
        
        if (results==null)
        {
            results=getFacilityIdsByProgramId(programId);
            facilityIdsByProgramIdCache.put(programId, results);
        }
        
        return(results);
    }    
    
    public List<Integer> getFacilityIdsByProviderId(String providerId)
    {
        // select provider_no,program_provider.program_id,room.program_id,facility_id from program_provider,room where program_provider.program_id=room.program_id;

        String sqlCommand="select facility_id from program_provider,room where program_provider.program_id=room.program_id and provider_no='"+providerId+'\'';
        return(SqlUtils.selectIntList(sqlCommand));
    }
    
    private static TimeClearedHashMap<String, List<Integer>> facilityIdsByProviderIdCache=new TimeClearedHashMap<String, List<Integer>>(DateUtils.MILLIS_PER_MINUTE*5, DateUtils.MILLIS_PER_MINUTE);
    public List<Integer> getCachedFacilityIdsByProviderId(String providerId)
    {
        List<Integer> results=facilityIdsByProviderIdCache.get(providerId);
        
        if (results==null)
        {
            results=getFacilityIdsByProviderId(providerId);
            facilityIdsByProviderIdCache.put(providerId, results);
        }
        
        return(results);
    }    
    
    public static  boolean facilityHasIntersection(List<Integer> providersFacilityIds, List<Integer> noteFacilities) {
        for (Integer id : noteFacilities) {
            if (providersFacilityIds.contains(id)) return(true);
        }
        
        return(false);
    }


}
