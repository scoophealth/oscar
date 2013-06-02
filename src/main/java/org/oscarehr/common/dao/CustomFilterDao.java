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

import org.oscarehr.common.model.CustomFilter;
import org.springframework.stereotype.Repository;


@Repository
public class CustomFilterDao extends AbstractDao<CustomFilter>{

	public CustomFilterDao() {
		super(CustomFilter.class);
	}
	
    public CustomFilter findByName(String name) {
    	Query query = entityManager.createQuery("select c FROM CustomFilter c where c.name = ?1");
        query.setParameter(1, name);
        
        CustomFilter result = this.getSingleResultOrNull(query);
        
        return result;
    }

    public CustomFilter findByNameAndProviderNo(String name, String providerNo) {
    	Query query = entityManager.createQuery("select c FROM CustomFilter c where c.name = ?1 and c.providerNo = ?2");
        query.setParameter(1, name);
        query.setParameter(2,providerNo);
        
        CustomFilter result = this.getSingleResultOrNull(query);
        
        return result;
    }

  
    public List<CustomFilter> getCustomFilters() {
    	Query query = entityManager.createQuery("select c FROM CustomFilter c");
    	
    	@SuppressWarnings("unchecked")
    	List<CustomFilter> result = query.getResultList();
    	
    	return result;
    }

    public List<CustomFilter> findByProviderNo(String providerNo) {
    	Query query = entityManager.createQuery("select c FROM CustomFilter c where c.providerNo = ?1");
    	query.setParameter(1, providerNo);
    	
    	@SuppressWarnings("unchecked")
    	List<CustomFilter> result = query.getResultList();
    	
    	return result;
    }

    public List<CustomFilter> getCustomFilterWithShortCut(String providerNo){
    	Query query = entityManager.createQuery("select c FROM CustomFilter c where c.providerNo = ?1 and c.shortcut = true");
    	query.setParameter(1, providerNo);
    	
    	@SuppressWarnings("unchecked")
    	List<CustomFilter> result = query.getResultList();
    	
    	return result;
    }
    
    public void deleteCustomFilter(String name) {
        CustomFilter filter = findByName(name);
        if(filter != null) {
        	remove(filter);
        }
    }

    
}