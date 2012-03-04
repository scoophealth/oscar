package org.oscarehr.common.dao;

import org.oscarehr.common.model.MdsMSH;
import org.springframework.stereotype.Repository;

@Repository
public class MdsMSHDao extends AbstractDao<MdsMSH>{

	public MdsMSHDao() {
		super(MdsMSH.class);
	}
}
