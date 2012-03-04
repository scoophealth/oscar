package org.oscarehr.common.dao;

import org.oscarehr.common.model.MdsPID;
import org.springframework.stereotype.Repository;

@Repository
public class MdsPIDDao extends AbstractDao<MdsPID>{

	public MdsPIDDao() {
		super(MdsPID.class);
	}
}
