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

import org.oscarehr.common.model.Document;
import org.springframework.stereotype.Repository;


@Repository
@SuppressWarnings("unchecked")
public class DocumentDao extends AbstractDao<Document> {

	public enum Module {DEMOGRAPHIC}
	
	public enum DocumentType {CONSULT}
	
	public DocumentDao() {
		super(Document.class);
	}

    public List<Object[]> getCtlDocsAndDocsByDemoId(Integer demoId, Module moduleName, DocumentType docType) {
		String sql = "FROM CtlDocument c, Document d " +
				"WHERE c.id.module = :moduleName " +
				"AND c.id.documentNo = d.documentNo " +
				"AND d.doctype = :docType " +
				"AND c.moduleId = :demoNo";
		Query query = entityManager.createQuery(sql);
		query.setParameter("moduleName", moduleName.name().toLowerCase());
		query.setParameter("docType", docType.name().toLowerCase());
		query.setParameter("demoNo", demoId);
		return query.getResultList();
	    
    }
    
    public List<Document> findActiveByDocumentNo(Integer demoId) {
		String sql = "SELECT d FROM Document d where d.documentNo = ?";
				
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demoId);
		return query.getResultList();
		
	    
    }
}
