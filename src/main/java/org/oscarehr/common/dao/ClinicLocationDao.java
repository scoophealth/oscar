package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.ClinicLocation;
import org.springframework.stereotype.Repository;

@Repository
public class ClinicLocationDao extends AbstractDao<ClinicLocation> {

	public ClinicLocationDao() {
		super(ClinicLocation.class);
	}

	public List<ClinicLocation> findByClinicNo(Integer clinicNo) {
	   	String sql = "select c from ClinicLocation c where c.clinicNo=?1 order by c.clinicLocationNo";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,clinicNo);

        @SuppressWarnings("unchecked")
        List<ClinicLocation> results = query.getResultList();

        return results;
	}

	public String searchVisitLocation(String clinicLocationNo) {
		String sql = "select c from ClinicLocation c where c.clinicLocationNo=?1";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,clinicLocationNo);

        @SuppressWarnings("unchecked")
        List<ClinicLocation> results = query.getResultList();
        if(!results.isEmpty()) {
        	return results.get(0).getClinicLocationName();
        }
        return null;
	}

	public ClinicLocation searchBillLocation(Integer clinicNo, String clinicLocationNo) {
		String sql = "select c from ClinicLocation c where c.clinicNo=?1 and c.clinicLocationNo=?2";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,clinicNo);
    	query.setParameter(2,clinicLocationNo);

        return this.getSingleResultOrNull(query);

	}

	/*
	 * @deprecated
	 */
	public void removeByClinicLocationNo(String clinicLocationNo) {
		String sql = "select c from ClinicLocation c where c.clinicLocationNo=?1";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,clinicLocationNo);

    	 @SuppressWarnings("unchecked")
         List<ClinicLocation> results = query.getResultList();
    	 for(ClinicLocation clinicLocation:results) {
    		 this.remove(clinicLocation);
    	 }

	}
}
