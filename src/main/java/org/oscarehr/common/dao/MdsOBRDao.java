package org.oscarehr.common.dao;

import org.oscarehr.common.model.MdsOBR;
import org.springframework.stereotype.Repository;

@Repository
public class MdsOBRDao extends AbstractDao<MdsOBR>{

	public MdsOBRDao() {
		super(MdsOBR.class);
	}
}
