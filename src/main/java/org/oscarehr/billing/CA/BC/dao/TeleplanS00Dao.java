package org.oscarehr.billing.CA.BC.dao;

import org.oscarehr.billing.CA.BC.model.TeleplanS00;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class TeleplanS00Dao extends AbstractDao<TeleplanS00>{

	public TeleplanS00Dao() {
		super(TeleplanS00.class);
	}
}
