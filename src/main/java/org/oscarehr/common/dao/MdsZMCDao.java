package org.oscarehr.common.dao;

import org.oscarehr.common.model.MdsZMC;
import org.springframework.stereotype.Repository;

@Repository
public class MdsZMCDao extends AbstractDao<MdsZMC>{

	public MdsZMCDao() {
		super(MdsZMC.class);
	}
}
