package org.oscarehr.common.dao;

import org.oscarehr.common.model.QuickList;
import org.springframework.stereotype.Repository;

@Repository
public class QuickListDao extends AbstractDao<QuickList>{

	public QuickListDao() {
		super(QuickList.class);
	}
}
