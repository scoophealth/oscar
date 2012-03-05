package org.oscarehr.common.dao;

import org.oscarehr.common.model.Diseases;
import org.springframework.stereotype.Repository;

@Repository
public class DiseasesDao extends AbstractDao<Diseases>{

	public DiseasesDao() {
		super(Diseases.class);
	}
}
