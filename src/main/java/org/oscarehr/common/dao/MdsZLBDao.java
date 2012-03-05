package org.oscarehr.common.dao;

import org.oscarehr.common.model.MdsZLB;
import org.springframework.stereotype.Repository;

@Repository
public class MdsZLBDao extends AbstractDao<MdsZLB>{

	public MdsZLBDao() {
		super(MdsZLB.class);
	}

}
