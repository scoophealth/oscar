package org.oscarehr.common.dao;

import org.oscarehr.common.model.CtlDiagCode;
import org.springframework.stereotype.Repository;

@Repository
public class CtlDiagCodeDao extends AbstractDao<CtlDiagCode>{

	public CtlDiagCodeDao() {
		super(CtlDiagCode.class);
	}

}
