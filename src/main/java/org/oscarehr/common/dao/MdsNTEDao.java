package org.oscarehr.common.dao;

import org.oscarehr.common.model.MdsNTE;
import org.springframework.stereotype.Repository;

@Repository
public class MdsNTEDao extends AbstractDao<MdsNTE>{

	public MdsNTEDao() {
		super(MdsNTE.class);
	}
}
