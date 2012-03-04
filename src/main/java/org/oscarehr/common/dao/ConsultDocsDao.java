package org.oscarehr.common.dao;

import org.oscarehr.common.model.ConsultDocs;
import org.springframework.stereotype.Repository;

@Repository
public class ConsultDocsDao extends AbstractDao<ConsultDocs>{

	public ConsultDocsDao() {
		super(ConsultDocs.class);
	}
}
