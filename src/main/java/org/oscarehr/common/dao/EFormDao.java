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

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.EForm;
import org.springframework.stereotype.Repository;

@Repository
public class EFormDao extends AbstractDao<EForm> {
	
	public EFormDao() {
		super(EForm.class);
	}
	
    public EForm findByName(String name){
    	Query query = entityManager.createQuery("SELECT e from EForm e where e.formName = ? and e.current=?");
    	query.setParameter(1, name);
    	query.setParameter(2,true);
    	
        @SuppressWarnings("unchecked")
        List<EForm> results = query.getResultList();
        if(!results.isEmpty())
        	return results.get(0);
        return null;
    }

	/**
	 * @param current can be null for both
	 * @return
	 */
    public List<EForm> findAll(Boolean current)
	{
    	StringBuilder sb=new StringBuilder();
    	sb.append("select x from ");
    	sb.append(modelClass.getSimpleName());
    	sb.append(" x");
    	
    	if (current!=null)
    	{
    		sb.append(" where x.current=?1");
    	}

    	Query query = entityManager.createQuery(sb.toString());
    	if (current!=null)
    	{
    		query.setParameter(1, current);
    	}
		
    	
		@SuppressWarnings("unchecked")
		List<EForm> results = query.getResultList();

		return(results);
	}
    
    public boolean isIndivicaRTLEnabled() { 

    	StringBuilder sb=new StringBuilder();
    	sb.append("select x from ");
    	sb.append(modelClass.getSimpleName());
    	sb.append(" x where x.formName='Rich Text Letter' and x.subject='Rich Text Letter Generator'");
       	Query query = entityManager.createQuery(sb.toString());
    	
		@SuppressWarnings("unchecked")
		List<EForm> forms = query.getResultList();
		
		boolean enabled = false;
		for (EForm form : forms) {
			enabled |= form.isCurrent();
		}
		
		return enabled;	
    }
    
    public void setIndivicaRTLEnabled(boolean enabled) { 

    	StringBuilder sb=new StringBuilder();
    	sb.append("select x from ");
    	sb.append(modelClass.getSimpleName());
    	sb.append(" x where x.formName='Rich Text Letter' and x.subject='Rich Text Letter Generator'");
       	Query query = entityManager.createQuery(sb.toString());
    	
		@SuppressWarnings("unchecked")
		List<EForm> forms = query.getResultList();
		for (EForm form : forms) {
			form.setCurrent(enabled);			
			enabled |= form.isCurrent();
			merge(form);
		}
    }
}
