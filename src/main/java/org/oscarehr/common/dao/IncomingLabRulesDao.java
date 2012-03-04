package org.oscarehr.common.dao;

import org.oscarehr.common.model.IncomingLabRules;
import org.springframework.stereotype.Repository;

@Repository
public class IncomingLabRulesDao extends AbstractDao<IncomingLabRules>{

	public IncomingLabRulesDao() {
		super(IncomingLabRules.class);
	}
}
