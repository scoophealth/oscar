package org.oscarehr.olis.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.olis.model.OLISProviderPreferences;
import org.springframework.stereotype.Repository;

@Repository
public class OLISProviderPreferencesDao extends AbstractDao<OLISProviderPreferences>{

	
	public OLISProviderPreferencesDao() {
	    super(OLISProviderPreferences.class);
    }

	public OLISProviderPreferences findById(String id) {
		try {
			String sql = "select x from "+ this.modelClass.getName() + " x where x.providerId=?";
			Query query = entityManager.createQuery(sql);
			query.setParameter(1, id);		
			return (OLISProviderPreferences)query.getSingleResult();
		}
		catch (javax.persistence.NoResultException nre) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
    public List<OLISProviderPreferences> findAll() {
		String sql = "select x from " + this.modelClass.getName() + " x";
		Query query = entityManager.createQuery(sql);
		return query.getResultList();
	}

}
