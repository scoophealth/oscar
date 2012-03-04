package org.oscarehr.common.dao;

import org.oscarehr.common.model.MdsPV1;
import org.springframework.stereotype.Repository;

@Repository
public class MdsPV1Dao extends AbstractDao<MdsPV1>{

	public MdsPV1Dao() {
		super(MdsPV1.class);
	}
}
