package org.oscarehr.common.dao;

import org.oscarehr.common.model.MdsZRG;
import org.springframework.stereotype.Repository;

@Repository
public class MdsZRGDao extends AbstractDao<MdsZRG>{

	public MdsZRGDao() {
		super(MdsZRG.class);
	}
}
