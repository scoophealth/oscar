package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.ConsultationRequestExt;
import org.springframework.stereotype.Repository;

@Repository
public class ConsultationRequestExtDao extends AbstractDao<ConsultationRequestExt> {

	public ConsultationRequestExtDao() {
		super(ConsultationRequestExt.class);
	}
	
	public List<ConsultationRequestExt> getConsultationRequestExts(int requestId) {
    	Query query = entityManager.createQuery("select cre from ConsultationRequestExt cre where cre.requestId=?1");
        query.setParameter(1, new Integer(requestId));
        return query.getResultList();
    }
	
	public String getConsultationRequestExtsByKey(int requestId,String key) {
		Query query = entityManager.createQuery("select cre.value from ConsultationRequestExt cre where cre.requestId=?1 and cre.key=?2");
        query.setParameter(1, new Integer(requestId));
        query.setParameter(2, key);
        List<String> results = query.getResultList();
        if(results.size()>0)
        	return results.get(0);
        return null;
	}
	
	public void clear(int requestId) {
		Query query = entityManager.createQuery("delete from ConsultationRequestExt cre where cre.requestId = ?1");
		query.setParameter(1, new Integer(requestId));
		query.executeUpdate();
	}
}