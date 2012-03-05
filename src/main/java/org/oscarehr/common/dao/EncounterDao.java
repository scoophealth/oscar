package org.oscarehr.common.dao;

import org.oscarehr.common.model.Encounter;
import org.springframework.stereotype.Repository;

@Repository
public class EncounterDao extends AbstractDao<Encounter>{

	public EncounterDao() {
		super(Encounter.class);
	}
}
