package org.oscarehr.common.dao;

import org.oscarehr.common.model.RecycleBin;
import org.springframework.stereotype.Repository;

@Repository
public class RecycleBinDao extends AbstractDao<RecycleBin>{

	public RecycleBinDao() {
		super(RecycleBin.class);
	}
}
