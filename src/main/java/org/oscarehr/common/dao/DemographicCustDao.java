package org.oscarehr.common.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.DemographicCust;
import org.springframework.stereotype.Repository;

@Repository
public class DemographicCustDao extends AbstractDao<DemographicCust> {

	public DemographicCustDao() {
		super(DemographicCust.class);
	}

    public List<DemographicCust> findMultipleMidwife(Collection<Integer> demographicNos, String oldMidwife) {
    	String sql = "select x from DemographicCust x where x.id IN (?1) and x.midwife=?2";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,demographicNos);
    	query.setParameter(2, oldMidwife);

        @SuppressWarnings("unchecked")
        List<DemographicCust> results = query.getResultList();
        return results;
    }

    public List<DemographicCust> findMultipleResident(Collection<Integer> demographicNos, String oldResident) {
    	String sql = "select x from DemographicCust x where x.id IN (?1) and x.resident=?2";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,demographicNos);
    	query.setParameter(2, oldResident);

        @SuppressWarnings("unchecked")
        List<DemographicCust> results = query.getResultList();
        return results;
    }

    public List<DemographicCust> findMultipleNurse(Collection<Integer> demographicNos, String oldNurse) {
    	String sql = "select x from DemographicCust x where x.id IN (?1) and x.nurse=?2";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,demographicNos);
    	query.setParameter(2, oldNurse);

        @SuppressWarnings("unchecked")
        List<DemographicCust> results = query.getResultList();
        return results;
    }
}
