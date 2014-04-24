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

import org.oscarehr.common.model.MdsMSH;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class MdsMSHDao extends AbstractDao<MdsMSH> {

	public MdsMSHDao() {
		super(MdsMSH.class);
	}

	public List<Object[]> findLabsByAccessionNumAndId(Integer id, String controlId) {
		String sql = "FROM MdsMSH a, MdsMSH b " + "WHERE a.controlId like :controlId " + "AND b.id = :id" + "ORDER BY a.controlId";
		Query query = entityManager.createQuery(sql);
		query.setParameter("id", id);
		query.setParameter("controlId", controlId);
		return query.getResultList();
	}
	
	public List<Object[]> findMdsSementDataById(Integer id) {
		String sql = "select mdsMSH.dateTime, mdsMSH.controlId, min(mdsZFR.reportFormStatus) " +
				"FROM MdsMSH mdsMSH, MdsZFR mdsZFR " +
				"WHERE mdsMSH.id = mdsZFR.id " +
				"AND mdsMSH.id = :segmentID" +
				"GROUP BY mdsMSH.id";
		Query query = entityManager.createQuery(sql);
		query.setParameter("id", id);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getLabResultsSince(Integer demographicNo, Date updateDate) {
		String query = "select m.id from MdsMSH m, PatientLabRouting p WHERE m.id = p.labNo and p.labType='MDS' and p.demographicNo = ?1 and (m.dateTime > ?2 or p.created > ?3) ";
		Query q = entityManager.createQuery(query);
		
		q.setParameter(1, demographicNo);
		q.setParameter(2, updateDate);
		q.setParameter(3, updateDate);
		
		
		return q.getResultList();    
	}
}
