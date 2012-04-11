package org.oscarehr.billing.CA.BC.dao;

import org.oscarehr.billing.CA.BC.model.TeleplanS22;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class TeleplanS22Dao extends AbstractDao<TeleplanS22>{

	public TeleplanS22Dao() {
		super(TeleplanS22.class);
	}
}
