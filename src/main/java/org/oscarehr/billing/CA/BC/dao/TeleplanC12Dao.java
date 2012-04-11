package org.oscarehr.billing.CA.BC.dao;

import org.oscarehr.billing.CA.BC.model.TeleplanC12;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class TeleplanC12Dao extends AbstractDao<TeleplanC12>{

	public TeleplanC12Dao() {
		super(TeleplanC12.class);
	}
}
