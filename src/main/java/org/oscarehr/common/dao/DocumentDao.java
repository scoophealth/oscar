package org.oscarehr.common.dao;

import org.oscarehr.common.model.Document;
import org.springframework.stereotype.Repository;


@Repository
public class DocumentDao extends AbstractDao<Document> {

	public DocumentDao() {
		super(Document.class);
	}
}
