package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.ConsultDocs;
import org.springframework.stereotype.Repository;

@Repository
public class ConsultDocsDao extends AbstractDao<ConsultDocs>{

	public ConsultDocsDao() {
		super(ConsultDocs.class);
	}

	public List<ConsultDocs> findByRequestIdDocumentNoAndDocumentType(Integer requestId, Integer documentNo, String docType) {
	  	String sql = "select x from ConsultDocs x where x.requestId=? and x.documentNo=? and x.docType=?";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,requestId);
    	query.setParameter(2,documentNo);
    	query.setParameter(3,docType);

        @SuppressWarnings("unchecked")
        List<ConsultDocs> results = query.getResultList();
        return results;
	}
}
