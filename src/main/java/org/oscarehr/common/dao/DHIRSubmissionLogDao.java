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

import org.oscarehr.common.model.DHIRSubmissionLog;
import org.springframework.stereotype.Repository;

@Repository
public class DHIRSubmissionLogDao extends AbstractDao<DHIRSubmissionLog> {

	public DHIRSubmissionLogDao() {
		super(DHIRSubmissionLog.class);
	}

	@SuppressWarnings("unchecked")
	public List<DHIRSubmissionLog> findAll() {
		Query query = createQuery("x", null);
		return query.getResultList();
	}

	public DHIRSubmissionLog findLatestPendingByPreventionId(Integer preventionId) {
		String sqlCommand = "select x from DHIRSubmissionLog x where x.preventionId=?1 and x.status = ?2 order by x.dateCreated DESC";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, preventionId);
		query.setParameter(2, "pending");

		List<DHIRSubmissionLog> results = query.getResultList();
		
		if(!results.isEmpty()) {
			return results.get(0);
		}
		
		return null;
	}
	
	public List<DHIRSubmissionLog> findByPreventionId(Integer preventionId) {
		String sqlCommand = "select x from DHIRSubmissionLog x where x.preventionId=?1 order by x.dateCreated DESC";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, preventionId);
		
		List<DHIRSubmissionLog> results = query.getResultList();
		
		return results;
	}
}
