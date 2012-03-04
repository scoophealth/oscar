package org.oscarehr.common.dao;

import org.oscarehr.common.model.Prescribe;
import org.springframework.stereotype.Repository;

@Repository
public class PrescribeDao extends AbstractDao<Prescribe>{

	public PrescribeDao() {
		super(Prescribe.class);
	}
}
