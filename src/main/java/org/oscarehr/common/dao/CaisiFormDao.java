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

import org.oscarehr.common.model.CaisiForm;
import org.springframework.stereotype.Repository;

@Repository
public class CaisiFormDao extends AbstractDao<CaisiForm>{

	public CaisiFormDao() {
		super(CaisiForm.class);
	}
	
	public List<CaisiForm> getActiveForms() {
		Query query = entityManager.createQuery("SELECT x FROM CaisiForm x  where x.status = 1 order by x.description ASC ");
		
		@SuppressWarnings("unchecked")
        List<CaisiForm> results = query.getResultList();
		return results;
    }
	
	public List<CaisiForm> getCaisiForms() {
		Query query = entityManager.createQuery("SELECT x FROM CaisiForm x");
		
		@SuppressWarnings("unchecked")
        List<CaisiForm> results = query.getResultList();
		return results;
	}
	
	
	public void updateStatus(Integer formId, Integer status) {
		CaisiForm form = find(formId);
		if(form != null) {
			form.setStatus(status);
			merge(form);
		}
	}
	
	public List<CaisiForm> getCaisiForms(Integer formId, Integer clientId) {
		Query query = entityManager.createQuery("SELECT x FROM CaisiForm x where x.formId = ?1 and x.clientId = ?2");
		query.setParameter(1, formId);
		query.setParameter(2, clientId);
		
		@SuppressWarnings("unchecked")
        List<CaisiForm> results = query.getResultList();
		return results;
	}
	
	public List<CaisiForm> getCaisiFormsByClientId(Integer clientId) {
		Query query = entityManager.createQuery("SELECT x FROM CaisiForm x where x.clientId = ?1");
		query.setParameter(1, clientId);
		
		@SuppressWarnings("unchecked")
        List<CaisiForm> results = query.getResultList();
		return results;
	}
	
	
	public List<CaisiForm> findActiveByFacilityIdOrNull(Integer facilityId) {
		Query query = entityManager.createQuery("SELECT x FROM CaisiForm x where x.status=1 and (x.facilityId is null or x.facilityId = ?1)");
		query.setParameter(1, facilityId);
			
		@SuppressWarnings("unchecked")
        List<CaisiForm> results = query.getResultList();
		return results;
	}

}
