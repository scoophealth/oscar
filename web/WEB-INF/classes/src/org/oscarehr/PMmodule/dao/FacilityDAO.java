package org.oscarehr.PMmodule.dao;

import java.util.List;

import org.oscarehr.PMmodule.model.Facility;
import org.oscarehr.PMmodule.model.Program;
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
    
    public List<Long> getFacilityIdsByNoteId(long noteId)
    {
        // select note_id,program_no,program_id,facility_id from casemgmt_note,room where casemgmt_note.program_no=room.program_id;
        
        String sqlCommand="select facility_id from casemgmt_note,room where casemgmt_note.program_no=room.program_id and note_id="+noteId;
        return(SqlUtils.selectLongList(sqlCommand));
    }
    
    public List<Long> getFacilityIdsByProgramId(int programId)
    {
        // select program_id,facility_id from room;
        
        String sqlCommand="select facility_id from room where room.program_id="+programId;
        return(SqlUtils.selectLongList(sqlCommand));
    }
    
    public List<Long> getFacilityIdsByProviderId(int providerId)
    {
        // select provider_no,program_provider.program_id,room.program_id,facility_id from program_provider,room where program_provider.program_id=room.program_id;

        String sqlCommand="select facility_id from program_provider,room where program_provider.program_id=room.program_id and provider_no="+providerId;
        return(SqlUtils.selectLongList(sqlCommand));
    }
    
    public static  boolean facilityHasIntersection(List<Long> providersFacilityIds, List<Long> noteFacilities) {
        for (Long id : noteFacilities) {
            if (providersFacilityIds.contains(id)) return(true);
        }
        
        return(false);
    }


}
