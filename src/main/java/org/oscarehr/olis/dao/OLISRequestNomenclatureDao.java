package org.oscarehr.olis.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.olis.model.OLISRequestNomenclature;
import org.springframework.stereotype.Repository;

@Repository
public class OLISRequestNomenclatureDao extends AbstractDao<OLISRequestNomenclature>{

	
	public OLISRequestNomenclatureDao() {
	    super(OLISRequestNomenclature.class);
    }

	public OLISRequestNomenclature findByNameId(String id) {
		String sql = "select x from "+ this.modelClass.getName() + " x where x.nameId=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, id);		
		return (OLISRequestNomenclature)query.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
    public List<OLISRequestNomenclature> findAll() {
		String sql = "select x from " + this.modelClass.getName() + " x";
		Query query = entityManager.createQuery(sql);
		return query.getResultList();
	}
	
}
