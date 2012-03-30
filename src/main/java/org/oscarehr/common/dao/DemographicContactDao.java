package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.DemographicContact;
import org.springframework.stereotype.Repository;

@Repository
public class DemographicContactDao extends AbstractDao<DemographicContact>{

	public DemographicContactDao() {
		super(DemographicContact.class);
	}

	public List<DemographicContact> findByDemographicNo(int demographicNo) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.demographicNo=? and x.deleted=false";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);
		@SuppressWarnings("unchecked")
		List<DemographicContact> dContacts = query.getResultList();
		return dContacts;
	}

	public List<DemographicContact> findByDemographicNoAndCategory(int demographicNo,String category) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.demographicNo=? and x.category=? and x.deleted=false";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);
		query.setParameter(2, category);
		@SuppressWarnings("unchecked")
		List<DemographicContact> dContacts = query.getResultList();
		return dContacts;
	}

	public List<DemographicContact> find(int demographicNo, int contactId) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.demographicNo=? and x.contactId = ? and x.deleted=false";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);
		query.setParameter(2, new Integer(contactId).toString());
		@SuppressWarnings("unchecked")
		List<DemographicContact> dContacts = query.getResultList();
		return dContacts;
	}
}
