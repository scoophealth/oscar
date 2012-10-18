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

import org.oscarehr.common.model.WorkFlow;
import org.springframework.stereotype.Repository;

@Repository
public class WorkFlowDao extends AbstractDao<WorkFlow>{

	public WorkFlowDao() {
		super(WorkFlow.class);
		
	}
	
	public List<WorkFlow> findByWorkflowType(String type) {
		Query q = entityManager.createQuery("SELECT w FROM WorkFlow w WHERE w.workflowType=?");
		q.setParameter(1, type);
		
		@SuppressWarnings("unchecked")
		List<WorkFlow> results = q.getResultList();
		
		return results;
	}
	
	public List<WorkFlow> findActiveByWorkflowType(String type) {
		Query q = entityManager.createQuery("SELECT w FROM WorkFlow w WHERE w.workflowType=? AND w.currentState <> ?");
		q.setParameter(1, type);
		q.setParameter(2, "C");
		
		@SuppressWarnings("unchecked")
		List<WorkFlow> results = q.getResultList();
		
		return results;
	}
	
	public List<WorkFlow> findActiveByWorkflowTypeAndDemographicNo(String type, String demographicNo) {
		Query q = entityManager.createQuery("SELECT w FROM WorkFlow w WHERE w.workflowType=? AND w.demographicNo=? AND w.currentState <> ?");
		q.setParameter(1, type);
		q.setParameter(2, demographicNo);
		q.setParameter(3, "C");
		
		@SuppressWarnings("unchecked")
		List<WorkFlow> results = q.getResultList();
		
		return results;
	}
}
