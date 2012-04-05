package org.oscarehr.common.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.RSchedule;
import org.springframework.stereotype.Repository;

@Repository(value="rScheduleDao")
public class RScheduleDao extends AbstractDao<RSchedule>{

	public RScheduleDao() {
		super(RSchedule.class);
	}

	public List<RSchedule> findByProviderAvailableAndDate(String providerNo, String available, Date sdate) {
		Query query = entityManager.createQuery("select s from RSchedule s where s.providerNo=? and s.available=? and s.sDate=?");
		query.setParameter(1, providerNo);
		query.setParameter(2, available);
		query.setParameter(3, sdate);

		@SuppressWarnings("unchecked")
        List<RSchedule> results = query.getResultList();
		return results;
	}
}
