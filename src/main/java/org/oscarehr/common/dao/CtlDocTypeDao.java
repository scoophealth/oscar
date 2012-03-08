package org.oscarehr.common.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.CtlDocType;
import org.springframework.stereotype.Repository;

@Repository
public class CtlDocTypeDao extends AbstractDao<CtlDocType>{

	public CtlDocTypeDao() {
		super(CtlDocType.class);
	}

	public void changeDocType(String docType, String module, String status){
		String sql = "UPDATE CtlDocType SET status =?1 WHERE module =?2 AND doctype =?3";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1,status);
		query.setParameter(2, module);
		query.setParameter(3, docType);

		query.executeUpdate();

	}

	public List<CtlDocType> findByStatusAndModule(String[] status, String module){
		List<String> result = new ArrayList<String>();
		for(int x=0;x<status.length;x++) {
			result.add(status[x]);
		}
		return this.findByStatusAndModule(result, module);
	}
	public List<CtlDocType> findByStatusAndModule(List<String> status, String module){

		Query query = entityManager.createQuery("select c from CtlDocType c where c.status in (?1) and c.module=?2");
		query.setParameter(1, status);
		query.setParameter(2, module);
		@SuppressWarnings("unchecked")
		List<CtlDocType> results = query.getResultList();

		return results;

	}

	public List<CtlDocType> findByDocTypeAndModule(String docType, String module){

		Query query = entityManager.createQuery("select c from CtlDocType c where c.docType=?1 and c.module=?2");
		query.setParameter(1, docType);
		query.setParameter(2, module);
		@SuppressWarnings("unchecked")
		List<CtlDocType> results = query.getResultList();

		return results;

	}
}
