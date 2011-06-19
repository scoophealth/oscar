package org.oscarehr.hospitalReportManager.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;
import org.springframework.stereotype.Repository;

@Repository
public class HRMDocumentToDemographicDao extends AbstractDao<HRMDocumentToDemographic> {
	
	public HRMDocumentToDemographicDao() {
		super(HRMDocumentToDemographic.class);
	}
	
	public List<HRMDocumentToDemographic> findByDemographicNo(String demographicNo) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.demographicNo=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToDemographic> documentToDemographics = query.getResultList();
		return documentToDemographics;
	}

	public List<HRMDocumentToDemographic> findByHrmDocumentId(String hrmDocumentId) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.hrmDocumentId=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, hrmDocumentId);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToDemographic> documentToDemographics = query.getResultList();
		return documentToDemographics;
	}
}
