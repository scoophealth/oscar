package org.oscarehr.common.dao;

import org.oscarehr.common.model.RaDetail;
import org.springframework.stereotype.Repository;

@Repository
public class RaDetailDao extends AbstractDao<RaDetail>{

	public RaDetailDao() {
		super(RaDetail.class);
	}
}
