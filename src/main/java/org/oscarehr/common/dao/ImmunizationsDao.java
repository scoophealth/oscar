package org.oscarehr.common.dao;

import org.oscarehr.common.model.Immunizations;
import org.springframework.stereotype.Repository;


@Repository
public class ImmunizationsDao extends AbstractDao<Immunizations>{

	public ImmunizationsDao() {
		super(Immunizations.class);
	}
}
