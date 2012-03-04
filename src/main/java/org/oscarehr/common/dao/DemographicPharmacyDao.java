package org.oscarehr.common.dao;

import org.oscarehr.common.model.DemographicPharmacy;
import org.springframework.stereotype.Repository;

@Repository
public class DemographicPharmacyDao extends AbstractDao<DemographicPharmacy>{

	public DemographicPharmacyDao() {
		super(DemographicPharmacy.class);
	}
}
