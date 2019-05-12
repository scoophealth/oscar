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

import org.oscarehr.common.model.AppointmentSearch;
import org.springframework.stereotype.Repository;

@Repository
public class AppointmentSearchDao extends AbstractDao<AppointmentSearch> {

	public AppointmentSearchDao() {
		super(AppointmentSearch.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<AppointmentSearch> findAll() {
		Query query = entityManager.createQuery("FROM " + modelClass.getSimpleName() +" a order by a.createDate desc");
		return query.getResultList();
	}
    
    public List<AppointmentSearch> findActive() {
	    	Query q = entityManager.createQuery("select a from AppointmentSearch a where a.active= true");
	    	@SuppressWarnings("unchecked")
	    	List<AppointmentSearch> results = q.getResultList();
	    	
	    	return results;
    }
    
    public List<AppointmentSearch> findByUUID(String uuid,Boolean active) {
    	String queryStr = "select a from AppointmentSearch a where a.uuid=?1";
    	if(active != null && active.booleanValue()) {
    		queryStr = queryStr + " and a.active = true";
    	}
    	Query q = entityManager.createQuery(queryStr);
    	q.setParameter(1, uuid);
    	
    	@SuppressWarnings("unchecked")
    	List<AppointmentSearch> results = q.getResultList();
    	
    	return results;
}
    
    public AppointmentSearch findForProvider(String providerNo) {
	    	if(providerNo == null || providerNo.length() == 0){
	    		return null;
	    	}
	    	
	    	Query q = entityManager.createQuery("select a from AppointmentSearch a where a.providerNo = ? and a.active = true order by a.updateDate desc");
	    	q.setParameter(1, providerNo);
    	
	    	@SuppressWarnings("unchecked")
	    	List<AppointmentSearch> results = q.getResultList();
    	
	    	if(results.isEmpty()) {
	    		return null;
	    	}
		
	    	return results.get(0);	
    }

    
    
    
}
