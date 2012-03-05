package org.oscarehr.common.dao;

import org.oscarehr.common.model.ConfigImmunization;
import org.springframework.stereotype.Repository;

@Repository
public class ConfigImmunizationDao extends AbstractDao<ConfigImmunization>{

	public ConfigImmunizationDao() {
		super(ConfigImmunization.class);
	}
}
