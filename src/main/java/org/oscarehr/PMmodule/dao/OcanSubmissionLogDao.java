package org.oscarehr.PMmodule.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.PMmodule.model.OcanSubmissionLog;
import org.oscarehr.PMmodule.model.OcanSubmissionRecordLog;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class OcanSubmissionLogDao extends AbstractDao<OcanSubmissionLog> {

	public OcanSubmissionLogDao() {
		super(OcanSubmissionLog.class);
	}
	
	public void persistRecord(OcanSubmissionRecordLog rec) {
		entityManager.persist(rec);
	}
	
	public List<OcanSubmissionLog> findAll() {
		Query query = entityManager.createQuery("select l from OcanSubmissionLog l order by l.submitDateTime DESC");
		@SuppressWarnings("unchecked")
		List<OcanSubmissionLog> results = query.getResultList();
		return results;
	}
	
}
