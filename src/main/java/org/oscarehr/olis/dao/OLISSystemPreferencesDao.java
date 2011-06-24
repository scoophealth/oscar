package org.oscarehr.olis.dao;

import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.olis.model.OLISSystemPreferences;
import org.springframework.stereotype.Repository;

@Repository
public class OLISSystemPreferencesDao extends AbstractDao<OLISSystemPreferences>{

	
	public OLISSystemPreferencesDao() {
	    super(OLISSystemPreferences.class);
    }

	public OLISSystemPreferences getPreferences() {
		try {
			String sql = "select x from "+ this.modelClass.getName() + " x";
			Query query = entityManager.createQuery(sql);	
			return (OLISSystemPreferences)query.getSingleResult();
		}
		catch (javax.persistence.NoResultException nre) {
			return new OLISSystemPreferences();
		}
	}

	public void save(OLISSystemPreferences olisPrefs) {
	    entityManager.merge(olisPrefs);
    }
}
