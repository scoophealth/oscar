package org.oscarehr.billing.CA.BC.dao;

import org.oscarehr.billing.CA.BC.model.TeleplanS21;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class TeleplanS21Dao extends AbstractDao<TeleplanS21>{

	public TeleplanS21Dao() {
		super(TeleplanS21.class);
	}
}
