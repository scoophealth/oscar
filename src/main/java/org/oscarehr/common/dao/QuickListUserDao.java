package org.oscarehr.common.dao;

import org.oscarehr.common.model.QuickListUser;
import org.springframework.stereotype.Repository;

@Repository
public class QuickListUserDao extends AbstractDao<QuickListUser>{

	public QuickListUserDao() {
		super(QuickListUser.class);
	}

}
