package org.oscarehr.common.dao;

import org.oscarehr.common.model.CtlDocType;
import org.springframework.stereotype.Repository;

@Repository
public class CtlDocTypeDao extends AbstractDao<CtlDocType>{

	public CtlDocTypeDao() {
		super(CtlDocType.class);
	}
}
