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

import org.oscarehr.common.model.Document;
import org.springframework.stereotype.Repository;


@Repository
@SuppressWarnings("unchecked")
public class DocumentDao extends AbstractDao<Document> {

	public enum Module {
		DEMOGRAPHIC;
		
		public String getName() {
			return this.name().toLowerCase();
		}
	}
	
	public enum DocumentType {
		CONSULT, LAB;
	
		public String getName() {
			return this.name().toLowerCase();
		}
	}
	
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
    
    public List<Object[]> findCtlDocsAndDocsByModuleDocTypeAndModuleId(Module module, DocumentType docType, Integer moduleId) {
    	String sql = "FROM CtlDocument c, Document d " +
    			"WHERE c.id.module = :module " +
    			"AND c.id.documentNo = d.documentNo " +
    			"AND d.docType = :docType " +
    			"AND c.moduleId = :moduleId";
		Query query = entityManager.createQuery(sql);
		query.setParameter("module", module.getName());
		query.setParameter("docType", docType.getName());
		query.setParameter("moduleId", moduleId);
		return query.getResultList();
    }
    
    public List<Object[]> findCtlDocsAndDocsByModuleAndModuleId(Module module, Integer moduleId) {
		String sql = "FROM CtlDocument c, Document d " + 
				"WHERE d.status = c.status " +
				"AND d.status != 'D' " +
				"AND c.id.documentNo = d.documentNo " +
				"AND c.id.module = :module " +
				"AND c.moduleId = :moduleId";
		
		Query query = entityManager.createQuery(sql);
		query.setParameter("module", module.getName());
		query.setParameter("moduleId", moduleId);
		return query.getResultList();
    }
    
    public List<Object[]> findDocsAndConsultDocsByConsultIdAndDocType(Integer consultationId, String doctype) {
    	String sql = "FROM Document d, ConsultDocs cd " +
    			"WHERE d.documentNo = cd.documentNo " +
    			"AND cd.requestId = :consultationId " +
    			"AND cd.docType = :doctype " +
    			"AND cd.deleted IS NULL";
		Query query = entityManager.createQuery(sql);
		query.setParameter("consultationId", consultationId);
		query.setParameter("doctype", doctype);
		return query.getResultList();
    }

	public List<Object[]> findCtlDocsAndDocsByDocNo(Integer documentNo) {
		String sql = "FROM Document d, CtlDocument c " + 
				"WHERE c.id.documentNo = d.documentNo " +
                "AND c.id.documentNo = :documentNo " +
                "ORDER BY d.observationdate DESC, d.updatedatetime DESC";
		Query query = entityManager.createQuery(sql);
		query.setParameter("documentNo", documentNo);
		return query.getResultList();
    }

	public List<Object[]> findCtlDocsAndDocsByModuleCreatorResponsibleAndDates(Module module, String doccreator, String responsible, Date from, Date to, boolean unmatchedDemographics) {
		String sql = "FROM Document d, CtlDocument c " +
				"WHERE c.documentNo = d.documentNo " +
				"AND c.module= :module " +
				"AND d.doccreator = :doccreator " +
				"AND d.responsible = :responsible " +
				"AND d.updatedatetime >= :from " +
				"AND d.updatedatetime <= :to";
		if (unmatchedDemographics) {
			sql += " AND c.moduleId = -1 ";
		}
		Query query = entityManager.createQuery(sql);
		query.setParameter("module", module.getName());
		query.setParameter("doccreator", doccreator);
		query.setParameter("responsible", responsible);
		query.setParameter("from", from);
		query.setParameter("to", to);
		return query.getResultList();
    }

	public List<Object[]> findConstultDocsDocsAndProvidersByModule(Module module, Integer moduleId) {
		String sql = "FROM Document d, Provider p, CtlDocument c " + 
				"WHERE d.doccreator = p.ProviderNo " +
				"AND d.id = c.id.documentNo " + 
				"AND c.id.module = :module " +
				"AND c.moduleId = :moduleId";
		Query query = entityManager.createQuery(sql);
		query.setParameter("module", module.getName());
		query.setParameter("moduleId", moduleId);
		return query.getResultList();
    }

	public Long findMaxDocNo() {
		String sql = "select max(d.documentNo) from Document d";
		Query query = entityManager.createQuery(sql);
		List<Object> o = query.getResultList();
		if (o.isEmpty()) {
			return 0L;
		}
		Object r = o.get(0);
		if (r == null) {
			return 0L;
		}
		return (Long) r; 
    }
}
