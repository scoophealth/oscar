/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.Security;
import org.springframework.stereotype.Repository;


@Repository
public class SecurityDao  extends AbstractDao<Security> {

	public SecurityDao() {
	    super(Security.class);
    }
	
	public List<Security> findAllOrderBy(String columnName) {
		
        Query query = entityManager.createQuery("SELECT s FROM Security s ORDER BY " + columnName);

        @SuppressWarnings("unchecked")
        List<Security> secList = query.getResultList();

        return secList;
		
	}


    public List<Security> findByProviderNo(String providerNo) {
    	Query query = entityManager.createQuery("select x from Security x where x.providerNo=?");
    	query.setParameter(1, providerNo);
    	
    	@SuppressWarnings("unchecked")
        List<Security> secList = query.getResultList();
    	
    	return secList;
    }

    public List<Security> findByLikeProviderNo(String providerNo) {
    	Query query = entityManager.createQuery("select x from Security x where x.providerNo like ?");
    	query.setParameter(1, providerNo);
    	
    	@SuppressWarnings("unchecked")
        List<Security> secList = query.getResultList();
    	
    	return secList;
    }

    public List<Security> findByUserName(String userName) {
    	Query query = entityManager.createQuery("select x from Security x where x.userName=?");
    	query.setParameter(1, userName);
    	
    	@SuppressWarnings("unchecked")
        List<Security> secList = query.getResultList();
    	
    	return secList;
    }
	
    public List<Security> findByLikeUserName(String userName) {
    	Query query = entityManager.createQuery("select x from Security x where x.userName like ?");
    	query.setParameter(1, userName);
    	
    	@SuppressWarnings("unchecked")
        List<Security> secList = query.getResultList();
    	
    	return secList;
    }

    public Security getByProviderNo(String providerNo) {
    	
    	List<Security> secList = this.findByProviderNo(providerNo);
    	
    	if(secList.isEmpty())
    		return null;
    	
    	return secList.get(0);
    }
    
    public List<Object[]> findProviders() {
    	String sql = "FROM Security s, Provider p WHERE p.providerNo = s.providerNo ORDER BY p.lastName";
    	Query query = entityManager.createQuery(sql);
    	return query.getResultList();
    }
    
    public List<Security> findByProviderSite(String providerNo) {
    	
    	String queryStr = "select * from security s inner join providersite p on s.provider_no = p.provider_no " +
    						"where p.site_id in(select site_id from providersite where provider_no=?)";
    	
        Query query = entityManager.createNativeQuery(queryStr, modelClass);
        query.setParameter(1, providerNo);

    	@SuppressWarnings("unchecked")
        List<Security> secList = query.getResultList();
    	
    	return secList;
    }

	
}
