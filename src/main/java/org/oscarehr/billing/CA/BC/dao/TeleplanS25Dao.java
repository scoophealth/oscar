package org.oscarehr.billing.CA.BC.dao;

import org.oscarehr.billing.CA.BC.model.TeleplanS25;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class TeleplanS25Dao extends AbstractDao<TeleplanS25>{

	public TeleplanS25Dao() {
		super(TeleplanS25.class);
	}
}
