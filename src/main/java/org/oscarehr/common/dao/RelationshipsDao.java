package org.oscarehr.common.dao;

import org.oscarehr.common.model.Relationships;
import org.springframework.stereotype.Repository;

@Repository
public class RelationshipsDao extends AbstractDao<Relationships>{

	public RelationshipsDao() {
		super(Relationships.class);
	}
}
