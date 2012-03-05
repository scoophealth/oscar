package org.oscarehr.common.dao;

import org.oscarehr.common.model.RaHeader;
import org.springframework.stereotype.Repository;

@Repository
public class RaHeaderDao extends AbstractDao<RaHeader>{

	public RaHeaderDao() {
		super(RaHeader.class);
	}

}
