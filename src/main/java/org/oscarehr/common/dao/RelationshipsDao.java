package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.Relationships;
import org.springframework.stereotype.Repository;

@Repository
public class RelationshipsDao extends AbstractDao<Relationships>{

	public RelationshipsDao() {
		super(Relationships.class);
	}

	//find all of them - for migration script
	public List<Relationships> findAll() {
		String sql = "select x from Relationships x order by x.demographicNo";
		Query query = entityManager.createQuery(sql);
		@SuppressWarnings("unchecked")
        List<Relationships> results =  query.getResultList();
		return results;
	}
}
