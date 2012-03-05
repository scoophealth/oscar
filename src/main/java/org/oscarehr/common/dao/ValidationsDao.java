package org.oscarehr.common.dao;

import org.oscarehr.common.model.Validations;
import org.springframework.stereotype.Repository;

@Repository
public class ValidationsDao extends AbstractDao<Validations>{

	public ValidationsDao() {
		super(Validations.class);
	}
}
