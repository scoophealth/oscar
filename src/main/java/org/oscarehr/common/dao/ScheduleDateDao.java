package org.oscarehr.common.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.ScheduleDate;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleDateDao extends AbstractDao<ScheduleDate>{

	public ScheduleDateDao() {
		super(ScheduleDate.class);
	}

	public ScheduleDate findByProviderNoAndDate(String providerNo, Date date) {
		Query query = entityManager.createQuery("select s from ScheduleDate s where s.providerNo=? and s.date=? and s.status=?");
		query.setParameter(1, providerNo);
		query.setParameter(2, date);
		query.setParameter(3, 'A');

		return(getSingleResultOrNull(query));
	}

	public List<ScheduleDate> findByProviderPriorityAndDateRange(String providerNo, char priority, Date date, Date date2) {
		Query query = entityManager.createQuery("select s from ScheduleDate s where s.providerNo=? and s.priority=? and s.date>=? and s.date <=?");
		query.setParameter(1, providerNo);
		query.setParameter(2, priority);
		query.setParameter(3, date);
		query.setParameter(4, date2);

		@SuppressWarnings("unchecked")
        List<ScheduleDate> results = query.getResultList();
		return results;
	}

	public List<ScheduleDate> findByProviderAndDateRange(String providerNo, Date date, Date date2) {
		Query query = entityManager.createQuery("select s from ScheduleDate s where s.providerNo=? and s.date>=? and s.date <=?");
		query.setParameter(1, providerNo);
		query.setParameter(2, date);
		query.setParameter(3, date2);

		@SuppressWarnings("unchecked")
        List<ScheduleDate> results = query.getResultList();
		return results;
	}
}
