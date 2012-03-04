package org.oscarehr.common.dao;

import org.oscarehr.common.model.OscarCommLocations;
import org.springframework.stereotype.Repository;

@Repository
public class OscarCommLocationsDao extends AbstractDao<OscarCommLocations>{

	public OscarCommLocationsDao() {
		super(OscarCommLocations.class);
	}
}
