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

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.Hl7TextMessage;
import org.springframework.stereotype.Repository;

@Repository
public class Hl7TextMessageDao extends AbstractDao<Hl7TextMessage> {

	public Hl7TextMessageDao() {
		super(Hl7TextMessage.class);
	}
	
	public void updateIfFillerOrderNumberMatches(String base64EncodedeMessage,int fileUploadCheckId,Integer id){
		Query query = entityManager.createQuery("update " + modelClass.getName() + " x set x.base64EncodedeMessage=?, fileUploadCheckId=? where x.type='TDIS' and x.id=?");
		query.setParameter(1, base64EncodedeMessage);
		query.setParameter(2, fileUploadCheckId);
		query.setParameter(3, id);
		
		query.executeUpdate();
	}
	
	public List<Hl7TextMessage> findByFileUploadCheckId(int id) {
		Query query = entityManager.createQuery("select x from Hl7TextMessage x where x.fileUploadCheckId = ?");
		query.setParameter(1,id);
		
		@SuppressWarnings("unchecked")
		List<Hl7TextMessage> results = query.getResultList();
		
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getLabResultsSince(Integer demographicNo, Date updateDate) {
		String query = "select m.id from Hl7TextMessage m, PatientLabRouting p WHERE m.id = p.labNo and p.labType='HL7' and p.demographicNo = ?1 and (m.created > ?2 or p.dateModified > ?3) ";
		Query q = entityManager.createQuery(query);
		
		q.setParameter(1, demographicNo);
		q.setParameter(2, updateDate);
		q.setParameter(3,updateDate);
		
		List<Integer> result =  q.getResultList();    
		
		return result;
	}
	
	public List<Hl7TextMessage> findByDemographicNo(Integer demographicNo, int offset, int limit) {
		String query = "select m from Hl7TextMessage m, PatientLabRouting p WHERE m.id = p.labNo and p.demographicNo = ?1 order by m.created";
		Query q = entityManager.createQuery(query);
		
		q.setParameter(1, demographicNo);
		q.setFirstResult(offset);
		q.setMaxResults(limit);
		
		@SuppressWarnings("unchecked")
		List<Hl7TextMessage> results = q.getResultList();
		
		return results;
	}
}
