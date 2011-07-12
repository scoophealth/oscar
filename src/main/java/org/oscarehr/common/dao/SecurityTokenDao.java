package org.oscarehr.common.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.SecurityToken;
import org.springframework.stereotype.Repository;

@Repository
public class SecurityTokenDao extends AbstractDao<SecurityToken>{

	public SecurityTokenDao() {
		super(SecurityToken.class);
	}
	
	public SecurityToken getByTokenAndExpiry(String token, Date expiry) {
		Query query = entityManager.createQuery("select t from SecurityToken t where t.token=? and t.expiry >= ?");
		query.setParameter(1, token);
		query.setParameter(2, expiry);
		
		@SuppressWarnings("unchecked")
		List<SecurityToken> results = query.getResultList();
		
		if(results.size()>0) {
			return results.get(0);
		}
		return null;
		
	}
}
