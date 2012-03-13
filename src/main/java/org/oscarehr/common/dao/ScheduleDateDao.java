package org.oscarehr.common.dao;

import java.util.Date;

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
}
