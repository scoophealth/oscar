package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.EFormGroup;
import org.springframework.stereotype.Repository;

@Repository
public class EFormGroupDao extends AbstractDao<EFormGroup>{
	
	public EFormGroupDao() {
		super(EFormGroup.class);
	}
	
	public List<EFormGroup> getByGroupName(String groupName) {
		String sql = "select eg from EFormGroup eg where eg.groupName=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1,groupName);
		
		@SuppressWarnings("unchecked")
		List<EFormGroup> results = query.getResultList();
		
		return results;
	}

}
