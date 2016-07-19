/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */
package org.oscarehr.common.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.SecRole;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SecRoleDao extends AbstractDao<SecRole> {

	public SecRoleDao() {
		super(SecRole.class);
	}

    public List<SecRole> findAll()
	{
		StringBuilder sb=new StringBuilder();
		sb.append("select x from SecRole x");

		sb.append(" order by x.name");

		Query query = entityManager.createQuery(sb.toString());

		@SuppressWarnings("unchecked")
		List<SecRole> results = query.getResultList();

		return(results);
	}
    
    public List<String> findAllNames()
 	{
 		StringBuilder sb=new StringBuilder();
 		sb.append("select x.name from SecRole x");

 		sb.append(" order by x.name");

 		Query query = entityManager.createQuery(sb.toString());

 		@SuppressWarnings("unchecked")
 		List<String> results = query.getResultList();

 		return(results);
 	}

    public SecRole findByName(String name) {
    	Query q = entityManager.createQuery("select x from SecRole x where x.name=?");

    	q.setParameter(1, name);
    	
    	return this.getSingleResultOrNull(q);
    }

    public List<SecRole> findAllOrderByRole()
	{
		Query query = entityManager.createQuery("select x from SecRole x order by x.name");

		@SuppressWarnings("unchecked")
		List<SecRole> results = query.getResultList();

		return(results);
	}
    
    /**
     * get provider's roles being in secRole and secUserRole at the same time
     * @param providerNo
     * @return
     */
    public List<Integer> findRoleNosByProviderNo(String providerNo) {
    	Query query = entityManager.createQuery("select t1.id from SecRole t1, org.oscarehr.common.model.SecUserRole t2 where"
    			+ " t1.name=t2.roleName and t2.providerNo = :providerNo");
    	query.setParameter("providerNo", providerNo);
    	return query.getResultList();
    }
    
    public List<SecRole> findByRoleNos(List<Integer> roleIds) {
    	if (roleIds == null || roleIds.size() == 0) {
    		return new ArrayList<SecRole>();
    	}
    	Query q = entityManager.createQuery("select s from SecRole s where s.id in (:roleIds)");
    	q.setParameter("roleIds", roleIds);
    	
    	return q.getResultList();
    }
    
    public List<SecRole> getRoles() {
        @SuppressWarnings("unchecked")
        Query q = entityManager.createQuery("select s from SecRole s order by s.name");

        return q.getResultList();
    }
}
