package org.oscarehr.common.dao;

import org.oscarehr.common.model.ClinicLocation;
import org.springframework.stereotype.Repository;

@Repository
public class ClinicLocationDao extends AbstractDao<ClinicLocation> {

	public ClinicLocationDao() {
		super(ClinicLocation.class);
	}
}
