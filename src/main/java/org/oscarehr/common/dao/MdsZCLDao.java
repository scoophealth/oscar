package org.oscarehr.common.dao;

import org.oscarehr.common.model.MdsZCL;
import org.springframework.stereotype.Repository;

@Repository
public class MdsZCLDao extends AbstractDao<MdsZCL>{

	public MdsZCLDao() {
		super(MdsZCL.class);
	}
}
