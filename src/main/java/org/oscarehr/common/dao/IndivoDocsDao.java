package org.oscarehr.common.dao;

import org.oscarehr.common.model.IndivoDocs;
import org.springframework.stereotype.Repository;

@Repository
public class IndivoDocsDao extends AbstractDao<IndivoDocs>{

	public IndivoDocsDao() {
		super(IndivoDocs.class);
	}
}
