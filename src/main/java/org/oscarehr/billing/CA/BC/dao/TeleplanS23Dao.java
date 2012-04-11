package org.oscarehr.billing.CA.BC.dao;

import org.oscarehr.billing.CA.BC.model.TeleplanS23;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class TeleplanS23Dao extends AbstractDao<TeleplanS23>{

	public TeleplanS23Dao() {
		super(TeleplanS23.class);
	}
}
