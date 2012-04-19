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


package org.oscarehr.hospitalReportManager.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.hospitalReportManager.model.HRMDocument;
import org.springframework.stereotype.Repository;

@Repository
public class HRMDocumentDao extends AbstractDao<HRMDocument> {

	public HRMDocumentDao() {
	    super(HRMDocument.class);
    }
	
	public List<HRMDocument> findById(int id) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.id=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, id);
		@SuppressWarnings("unchecked")
		List<HRMDocument> documents = query.getResultList();
		return documents;
	}
	
	public List<Integer> findByHash(String hash) {
		String sql = "select distinct id from " + this.modelClass.getName() + " x where x.reportHash=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, hash);
		@SuppressWarnings("unchecked")
		List<Integer> matches = query.getResultList();
		return matches;
	}
	
	public List<HRMDocument> findByNoTransactionInfoHash(String hash) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.reportLessTransactionInfoHash=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, hash);
		@SuppressWarnings("unchecked")
		List<HRMDocument> matches = query.getResultList();
		return matches;
	}
	
	@SuppressWarnings("unchecked")
    public List<Integer> findAllWithSameNoDemographicInfoHash(String hash) {
		String sql = "select distinct parentReport from " + this.modelClass.getName() + " x where x.reportLessDemographicInfoHash=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, hash);
		List<Integer> matches = query.getResultList();
		
		if (matches != null && matches.size() == 1 && matches.get(0) == null) {
			sql = "select distinct id from " + this.modelClass.getName() + " x where x.reportLessDemographicInfoHash=?";
			query = entityManager.createQuery(sql);
			query.setParameter(1, hash);
			matches = query.getResultList();
		}
		return matches;
	}
	
	@SuppressWarnings("unchecked")
    public List<HRMDocument> findAllDocumentsWithRelationship(Integer docId) {
		List<HRMDocument> documentsWithRelationship = new LinkedList<HRMDocument>();
		// Get the document that was specified first
		HRMDocument firstDocument = this.find(docId);
		if (firstDocument != null) {
			String sql = null;
			Query query = null;
			if (firstDocument.getParentReport() != null) {
				// This is a child report; get the parent and all siblings of this report
				sql = "select x from " + this.modelClass.getName() + " x where x.id = ? order by x.id asc";
				query = entityManager.createQuery(sql);
				query.setParameter(1, firstDocument.getParentReport());
				documentsWithRelationship.addAll(query.getResultList());
				
				sql = "select x from " + this.modelClass.getName() + " x where x.parentReport = ? order by x.id asc";
				query = entityManager.createQuery(sql);
				query.setParameter(1, firstDocument.getParentReport());
				documentsWithRelationship.addAll(query.getResultList());

				
			} else {
				// This is a parent report
				sql = "select x from " + this.modelClass.getName() + " x where x.parentReport = ? order by x.id asc";
				query = entityManager.createQuery(sql);
				query.setParameter(1, firstDocument.getId());
				documentsWithRelationship = query.getResultList();
			}
		
		}

		return documentsWithRelationship;
		
	}
	
	public List<HRMDocument> getAllChildrenOf(Integer docId) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.parentReport=? order by id asc";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, docId);
		@SuppressWarnings("unchecked")
		List<HRMDocument> documents = query.getResultList();
		return documents;
    }
	
}
