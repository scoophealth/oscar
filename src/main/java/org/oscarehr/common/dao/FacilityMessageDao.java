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

import org.oscarehr.common.model.FacilityMessage;
import org.springframework.stereotype.Repository;

@Repository
public class FacilityMessageDao extends AbstractDao<FacilityMessage>{

	public FacilityMessageDao() {
		super(FacilityMessage.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<FacilityMessage> getMessages() {
		String sql = "select fm from FacilityMessage fm order by fm.expiryDate desc";
		Query query = entityManager.createQuery(sql);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<FacilityMessage> getMessagesByFacilityId(Integer facilityId) {
		String sql = "select fm from FacilityMessage fm where fm.facilityId=? order by fm.expiryDate desc";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, facilityId);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<FacilityMessage> getMessagesByFacilityIdOrNull(Integer facilityId) {
		String sql = "select fm from FacilityMessage fm where (fm.facilityId=? or fm.facilityId IS NULL or fm.facilityId=0) order by fm.expiryDate desc";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, facilityId);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<FacilityMessage> getMessagesByFacilityIdAndProgramId(Integer facilityId, Integer programId) {
		String sql = "select fm from FacilityMessage fm where fm.facilityId=? and fm.programId = ? order by fm.expiryDate desc";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, facilityId);
		query.setParameter(2, programId);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<FacilityMessage> getMessagesByFacilityIdOrNullAndProgramIdOrNull(Integer facilityId, Integer programId) {
		String sql = "select fm from FacilityMessage fm where (fm.facilityId=? or fm.facilityId IS NULL or fm.facilityId=0) and (fm.programId = ? or fm.programId IS NULL) order by fm.expiryDate desc";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, facilityId);
		query.setParameter(2, programId);
		return query.getResultList();
	}
	
}
