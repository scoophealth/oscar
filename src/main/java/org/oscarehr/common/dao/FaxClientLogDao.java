package org.oscarehr.common.dao;

import org.oscarehr.common.model.FaxClientLog;
import org.springframework.stereotype.Repository;

@Repository
public class FaxClientLogDao extends AbstractDao<FaxClientLog>{

	public FaxClientLogDao() {
		super(FaxClientLog.class);
	}

}
