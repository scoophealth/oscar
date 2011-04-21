package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.ScheduleTemplateCode;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleTemplateCodeDao extends AbstractDao<ScheduleTemplateCode> {
	
	public ScheduleTemplateCodeDao() {
		super(ScheduleTemplateCode.class);
	}
	
	public List<ScheduleTemplateCode> getAll() {
		Query query = entityManager.createQuery("select s from ScheduleTemplateCode s order by s.code");
		
		@SuppressWarnings("unchecked")
		List<ScheduleTemplateCode> results = query.getResultList();
		
		return results;
	}
	
	public ScheduleTemplateCode getByCode(char code) {
		Query query = entityManager.createQuery("select s from ScheduleTemplateCode s where s.code=?");
		query.setParameter(1, code);
		
		@SuppressWarnings("unchecked")
		List<ScheduleTemplateCode> results = query.getResultList();
		if(!results.isEmpty()) {
			return results.get(0);
		}
		return null;
	}

}
