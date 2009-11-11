package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.DxAssociation;
import org.springframework.stereotype.Repository;

@Repository
public class DxDao extends AbstractDao<DxAssociation> {

	public DxDao() {
		super(DxAssociation.class);
	}

	public List<DxAssociation> findAllAssociations()
	{			
		Query query = entityManager.createQuery("select x from DxAssociation x order by x.dxCodeType,x.dxCode");
		
		@SuppressWarnings("unchecked")
		List<DxAssociation> results = query.getResultList();

		return(results);
	}
    
    public int removeAssociations() {
    	Query query = entityManager.createQuery("DELETE from DxAssociation");
    	return query.executeUpdate();
    }
    
    public DxAssociation findAssociation(String codeType, String code) {    	
    	Query query = entityManager.createQuery("SELECT x from DxAssociation x where x.codeType = ?1 and x.code = ?2");
    	query.setParameter(1, codeType);
    	query.setParameter(2, code);
    	    	
        @SuppressWarnings("unchecked")
    	List<DxAssociation> results = query.getResultList();
    	if(!results.isEmpty()) {
    		return results.get(0);
    	}
    	return null;
    }
}
