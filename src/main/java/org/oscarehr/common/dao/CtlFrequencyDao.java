package org.oscarehr.common.dao;

import org.oscarehr.common.model.CtlFrequency;
import org.springframework.stereotype.Repository;

@Repository
public class CtlFrequencyDao extends AbstractDao<CtlFrequency>{

	public CtlFrequencyDao() {
		super(CtlFrequency.class);
	}

}
