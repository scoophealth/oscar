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
package org.oscarehr.PMmodule.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.PMmodule.model.VacancyClientMatch;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class VacancyClientMatchDao extends AbstractDao<VacancyClientMatch> {
	
	public VacancyClientMatchDao() {
		super(VacancyClientMatch.class);
	}
	
	public List<VacancyClientMatch> findByClientIdAndVacancyId(int clientId, int vacancyId){
		Query q = entityManager.createQuery("select x from VacancyClientMatch x where x.client_id = ? and x.vacancy_id = ?");
		q.setParameter(1, clientId);
		q.setParameter(2, vacancyId);
		
		@SuppressWarnings("unchecked")
		List<VacancyClientMatch> results = q.getResultList();
		
		return results;
	}
	
	public List<VacancyClientMatch> findByClientId(int clientId){
		Query q = entityManager.createQuery("select x from VacancyClientMatch x where x.client_id = ?");
		q.setParameter(1, clientId);
		
		@SuppressWarnings("unchecked")
		List<VacancyClientMatch> results = q.getResultList();
		
		return results;
	}
	
	public List<VacancyClientMatch> findBystatus(String status){
		Query q = entityManager.createQuery("select x from VacancyClientMatch x where x.status = ?");
		q.setParameter(1, status);
		
		@SuppressWarnings("unchecked")
		List<VacancyClientMatch> results = q.getResultList();
		
		return results;
	}
	
	
	public void updateStatus(String status, int clientId, int vacancyId) {
		for(VacancyClientMatch v:this.findByClientIdAndVacancyId(clientId, vacancyId)) {
			v.setStatus(status);
		}
	}

	public void updateStatusAndRejectedReason(String status, String rejectedReason, int clientId, int vacancyId) {
		for(VacancyClientMatch v:this.findByClientIdAndVacancyId(clientId, vacancyId)) {
			v.setStatus(status);
			v.setRejectionReason(rejectedReason);
		}
	}

}
