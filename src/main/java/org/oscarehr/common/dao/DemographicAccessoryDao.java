package org.oscarehr.common.dao;

import javax.persistence.Query;

import org.oscarehr.common.model.DemographicAccessory;
import org.springframework.stereotype.Repository;

@Repository
public class DemographicAccessoryDao extends AbstractDao<DemographicAccessory>{

	public DemographicAccessoryDao() {
		super(DemographicAccessory.class);
	}

    public long findCount(Integer demographicNo) {
    	String sql = "select count(x) from DemographicAccessory x where x.demographicNo=?1";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,demographicNo);

        Long count = (Long)query.getSingleResult();

        return count;
    }
}
