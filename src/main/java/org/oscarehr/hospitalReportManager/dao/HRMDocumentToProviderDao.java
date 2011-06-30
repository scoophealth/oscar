package org.oscarehr.hospitalReportManager.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToProvider;
import org.springframework.stereotype.Repository;

@Repository
public class HRMDocumentToProviderDao extends AbstractDao<HRMDocumentToProvider> {

	public HRMDocumentToProviderDao() {
		super(HRMDocumentToProvider.class);
	}
	
	public List<HRMDocumentToProvider> findAllUnsigned(Integer page, Integer pageSize) {
		String sql = "select x from " + this.modelClass.getName() + " x where (x.signedOff IS NULL or x.signedOff = 0)";
		Query query = entityManager.createQuery(sql);
		query.setMaxResults(pageSize);
		query.setFirstResult(page*pageSize);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToProvider> documentToProviders = query.getResultList();
		return documentToProviders;
	}
	
	public List<HRMDocumentToProvider> findByProviderNo(String providerNo, Integer page, Integer pageSize) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.providerNo=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, providerNo);
		query.setMaxResults(pageSize);
		query.setFirstResult(page*pageSize);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToProvider> documentToProviders = query.getResultList();
		return documentToProviders;
	}
	
	public List<HRMDocumentToProvider> findByProviderNoLimit(String providerNo, Integer page, Integer limit, Integer viewed, Integer signedOff) {
		String sql = "";
		if (viewed != 2)
			sql = "select x from " + this.modelClass.getName() + " x where x.providerNo like ? and x.signedOff=? and x.viewed=?";
		else
			sql = "select x from " + this.modelClass.getName() + " x where x.providerNo like ? and x.signedOff=?";
		
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, providerNo);
		query.setParameter(2, signedOff);

		if (viewed != 2)
			query.setParameter(3, viewed);

		query.setFirstResult(page * limit);
		query.setMaxResults(limit);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToProvider> documentToProviders = query.getResultList();
		return documentToProviders;
	}
	

	public List<HRMDocumentToProvider> findByHrmDocumentId(String hrmDocumentId) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.hrmDocumentId=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, hrmDocumentId);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToProvider> documentToProviders = query.getResultList();
		return documentToProviders;
	}
	
	public List<HRMDocumentToProvider> findByHrmDocumentIdNoSystemUser(String hrmDocumentId) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.hrmDocumentId=? and x.providerNo != '-1'";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, hrmDocumentId);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToProvider> documentToProviders = query.getResultList();
		return documentToProviders;
	}
	
	public HRMDocumentToProvider findByHrmDocumentIdAndProviderNo(String hrmDocumentId, String providerNo) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.hrmDocumentId=? and x.providerNo=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, hrmDocumentId);
		query.setParameter(2, providerNo);
		try {
			return (HRMDocumentToProvider) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
}
