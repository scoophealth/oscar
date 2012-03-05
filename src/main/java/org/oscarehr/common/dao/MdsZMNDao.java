package org.oscarehr.common.dao;

import org.oscarehr.common.model.MdsZMN;
import org.springframework.stereotype.Repository;

@Repository
public class MdsZMNDao extends AbstractDao<MdsZMN>{

	public MdsZMNDao() {
		super(MdsZMN.class);
	}
}
