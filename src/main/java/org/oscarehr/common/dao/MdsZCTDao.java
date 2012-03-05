package org.oscarehr.common.dao;

import org.oscarehr.common.model.MdsZCT;
import org.springframework.stereotype.Repository;

@Repository
public class MdsZCTDao extends AbstractDao<MdsZCT>{

	public MdsZCTDao() {
		super(MdsZCT.class);
	}
}
