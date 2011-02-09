package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.OcanClientFormData;
import org.springframework.stereotype.Repository;

@Repository
public class OcanClientFormDataDao extends AbstractDao<OcanClientFormData>{

	public OcanClientFormDataDao() {
		super(OcanClientFormData.class);
	}
	
	public List<OcanClientFormData> findByQuestion(Integer ocanClientFormId, String question) {

		String sqlCommand = "select x from OcanClientFormData x where x.ocanClientFormId=?1 and x.question=?2";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, ocanClientFormId);
		query.setParameter(2, question);

		@SuppressWarnings("unchecked")
		List<OcanClientFormData> results=query.getResultList();
		
		return (results);
	}

    /**
     * Generally speaking this method is good for getting a form if
     * the answer is a CDS category, i.e. "019-04", this method is not
     * useful for answers which are random strings like "days hosipitalised"
     * where the answer may conflict with other numeric answers.
     */
    public OcanClientFormData findByAnswer(Integer ocanClientFormId, String answer) {

		String sqlCommand = "select x from OcanClientFormData x where x.ocanClientFormId=?1 and x.answer=?2";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, ocanClientFormId);
		query.setParameter(2, answer);

		return (getSingleResultOrNull(query));
	}

	public List<OcanClientFormData> findByForm(Integer ocanClientFormId) {

		String sqlCommand = "select x from OcanClientFormData x where x.ocanClientFormId=?1";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, ocanClientFormId);
	
		@SuppressWarnings("unchecked")
		List<OcanClientFormData> results=query.getResultList();
		
		return (results);
	}
	
}
