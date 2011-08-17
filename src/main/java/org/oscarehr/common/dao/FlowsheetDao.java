package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.Flowsheet;
import org.springframework.stereotype.Repository;

@Repository
public class FlowsheetDao extends AbstractDao<Flowsheet>{

	public FlowsheetDao() {
		super(Flowsheet.class);
	}
	
	public List<Flowsheet> findAll() {
		Query query = entityManager.createQuery("select f from Flowsheet f");
		
		@SuppressWarnings("unchecked")
		List<Flowsheet> results = query.getResultList();
		
		return results;
	}
	
	public Flowsheet findByName(String name) {
		Query query = entityManager.createQuery("select f from Flowsheet f where f.name=?");
		query.setParameter(1, name);
		
		return getSingleResultOrNull(query);
	}
}
