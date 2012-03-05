package org.oscarehr.common.dao;

import org.oscarehr.common.model.MdsOBX;
import org.springframework.stereotype.Repository;

@Repository
public class MdsOBXDao extends AbstractDao<MdsOBX>{

	public MdsOBXDao() {
		super(MdsOBX.class);
	}
}
