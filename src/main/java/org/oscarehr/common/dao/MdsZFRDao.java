package org.oscarehr.common.dao;

import org.oscarehr.common.model.MdsZFR;
import org.springframework.stereotype.Repository;

@Repository
public class MdsZFRDao extends AbstractDao<MdsZFR>{

	public MdsZFRDao() {
		super(MdsZFR.class);
	}
}
