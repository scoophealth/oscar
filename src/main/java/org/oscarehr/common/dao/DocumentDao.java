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

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.oscarehr.common.model.ConsultDocs;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Document;
import org.springframework.stereotype.Repository;

import oscar.dms.EDocUtil.EDocSort;
import oscar.util.ConversionUtils;


@Repository
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
				"AND c.id.moduleId = :demoNo";
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
    			"AND c.id.moduleId = :moduleId";
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
				"AND c.id.moduleId = :moduleId";
		
		Query query = entityManager.createQuery(sql);
		query.setParameter("module", module.getName());
		query.setParameter("moduleId", moduleId);
		return query.getResultList();
    }
    
    public List<Object[]> findDocsAndConsultDocsByConsultId(Integer consultationId) {
    	String sql = "FROM Document d, ConsultDocs cd " +
    			"WHERE d.documentNo = cd.documentNo " +
    			"AND cd.requestId = :consultationId " +
    			"AND cd.docType = :doctype " +
    			"AND cd.deleted IS NULL";
		Query query = entityManager.createQuery(sql);
		query.setParameter("consultationId", consultationId);
		query.setParameter("doctype", ConsultDocs.DOCTYPE_DOC);
		return query.getResultList();
    }
    
    public List<Object[]> findDocsAndConsultResponseDocsByConsultId(Integer consultationId) {
    	String sql = "FROM Document d, ConsultResponseDoc crd " +
    			"WHERE d.documentNo = crd.documentNo " +
    			"AND crd.responseId = :consultationId " +
    			"AND crd.docType = 'D' " +
    			"AND crd.deleted IS NULL";
		Query query = entityManager.createQuery(sql);
		query.setParameter("consultationId", consultationId);
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

	public List<Object[]> findCtlDocsAndDocsByModuleCreatorResponsibleAndDates(Module module, String providerNo, String responsible, Date from, Date to, boolean unmatchedDemographics) {
		String sql = "FROM Document d, CtlDocument c " +
				"WHERE c.documentNo = d.documentNo " +
				"AND c.module= :module " +
				"AND d.doccreator = :doccreator " +
				"AND d.responsible = :responsible " +
				"AND d.updatedatetime >= :from " +
				"AND d.updatedatetime <= :to";
		if (unmatchedDemographics) {
			sql += " AND c.id.moduleId = -1 ";
		}
		Query query = entityManager.createQuery(sql);
		query.setParameter("module", module.getName());
		query.setParameter("doccreator", providerNo);
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
				"AND c.id.moduleId = :moduleId";
		Query query = entityManager.createQuery(sql);
		query.setParameter("module", module.getName());
		query.setParameter("moduleId", moduleId);
		return query.getResultList();
    }

	public Integer findMaxDocNo() {
		String sql = "select max(d.documentNo) from Document d";
		Query query = entityManager.createQuery(sql);
		List<Object> o = query.getResultList();
		if (o.isEmpty()) {
			return 0;
		}
		Object r = o.get(0);
		if (r == null) {
			return 0;
		}
		return (Integer) r; 
    }
	
	public Document getDocument(String documentNo) {
		Integer id = null;
		try {
			id = Integer.parseInt(documentNo);
		} catch(NumberFormatException e) {
			//ignore
			return null;
		}
		return find(id);
	}
	
	@SuppressWarnings("unchecked")
	public Demographic getDemoFromDocNo(String docNo) {//return null if no demographic linked to this document
		Demographic d = null;
		Integer id = null;
		try {
			id = Integer.parseInt(docNo);
		} catch(NumberFormatException e) {
			//ignore
			return null;
		}
		
		String q = "select d from Demographic d, CtlDocument c where c.id.module='demographic'" + " and c.id.moduleId!='-1' and c.id.moduleId=d.DemographicNo and c.id.documentNo=? ";
		
		Query query = entityManager.createQuery(q);
		query.setParameter(1, id);
		
		List<Demographic> rs = query.getResultList();
		if (rs.size() > 0) d = rs.get(0);
		return d;
	}


	public int getNumberOfDocumentsAttachedToAProviderDemographics(String providerNo, Date startDate, Date endDate) {
		Query query = entityManager.createNativeQuery("select count(*) from ctl_document c, demographic d,document doc where c.module_id = d.demographic_no and c.document_no = doc.document_no   and d.provider_no = :providerNo and doc.observationdate >= :startDate and doc.observationdate <= :endDate ");
		query.setParameter("providerNo", providerNo);
		query.setParameter("startDate", new Timestamp(startDate.getTime()));
		query.setParameter("endDate", new Timestamp(endDate.getTime()));
		BigInteger result = (BigInteger)query.getSingleResult();
		if(result == null)
			return 0;
		return result.intValue();
	}

	public void subtractPages(String documentNo, Integer i) {
		Document doc = getDocument(documentNo);
		if(doc != null) {
			doc.setNumberofpages(doc.getNumberofpages() - i);
			merge(doc);
		}
	}

	/**
	 * Finds all documents for the specified demographic id
	 * 
	 * @param demoNo
	 */
	public List<Document> findByDemographicId(String demoNo) {
		Integer id = null;
		try {
			id = Integer.parseInt(demoNo);
		} catch(NumberFormatException e) {
			//ignore
			return new ArrayList<Document>();
		}
		Query query = entityManager.createQuery("SELECT DISTINCT d FROM Document d, CtlDocument c WHERE d.status = c.status AND d.status != 'D' AND c.id.documentNo=d.documentNo AND " + "c.id.module='demographic' AND c.id.moduleId = ?");
		query.setParameter(1, id);
		
		List<Document> result = query.getResultList();
		return result;
	}
	
	/**
	 * Chop-chop. Please don't ask what this spagehtti does, better don't use it. 
	 * 
	 * @param module
	 * @param moduleid
	 * @param docType
	 * @param includePublic
	 * @param includeDeleted
	 * @param includeActive
	 * @return
	 * 		Returns a list containing array with CtlDocument and Document pairs in the corresponding order. 
	 */
	@SuppressWarnings("unchecked")
    public List<Object[]> findDocuments(String module, String moduleid, String docType, boolean includePublic, boolean includeDeleted, boolean includeActive, EDocSort sort, Date since) {
		Map<String, Object> params = new HashMap<String, Object>();
	
		StringBuilder buf = new StringBuilder("SELECT DISTINCT c, d " +
				"FROM Document d, CtlDocument c " +
				"WHERE c.id.documentNo = d.id AND c.id.module = :module");
		params.put("module",module);
		
		boolean isShowingAllDocuments = docType == null || docType.equals("all") || docType.length() == 0; 
		
		if (includePublic) {
			if (isShowingAllDocuments) {
				buf.append(" AND d.public1 = 1");
			} else {
				buf.append(" AND d.public1 = 1 AND d.doctype = :doctype");
				params.put("doctype", docType);
			}
		} else {
			if (isShowingAllDocuments) { 
				buf.append(" AND c.id.moduleId = :moduleId AND d.public1 = 0");
				params.put("moduleId", ConversionUtils.fromIntString(moduleid));
			} else {
				buf.append(" AND c.id.moduleId = :moduleId AND d.public1 = 0 AND d.doctype = :doctype");
				params.put("doctype", docType);
				params.put("moduleId", ConversionUtils.fromIntString(moduleid));
			}
		}

		if (includeDeleted) {
			buf.append(" AND d.status = 'D'");
		} else if (includeActive) {
			buf.append(" AND d.status != 'D'");
		}
		
		if(since != null) {
			buf.append(" AND d.updatedatetime > :updatedatetime ");
			params.put("updatedatetime",since);
		}
		
		buf.append(" ORDER BY ").append(sort.getValue());

		Query query = entityManager.createQuery(buf.toString());
		for(String key:params.keySet()) {
			Object val = params.get(key);
			query.setParameter(key, val);
		}
		List<Object[]> result = query.getResultList();
		return result;
		
	}
    
	/**
	 * @return results ordered by updatedatetime
	 */
    public List<Document> findByUpdateDate(Date updatedAfterThisDateExclusive, int itemsToReturn) {
    	String sql = "select x from "+modelClass.getSimpleName()+" x where x.updatedatetime>?1 order by x.updatedatetime";
    	
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1, updatedAfterThisDateExclusive);
    	setLimit(query, itemsToReturn);

        @SuppressWarnings("unchecked")
        List<Document> documents = query.getResultList();
        return documents;
    }
    
	/**
	 * @return results ordered by updatedatetime
	 */
    public List<Document> findByProgramProviderDemographicUpdateDate(Integer programId, String providerNo, Integer demographicId, Date updatedAfterThisDateExclusive, int itemsToReturn) {
    	String sql = "select d from "+modelClass.getSimpleName()+" d, CtlDocument c where c.id.documentNo=d.documentNo and c.id.module='demographic' AND c.id.moduleId = :demographicId and (d.programId=:programId or d.programId is null or d.programId=-1) and d.doccreator=:providerNo and d.updatedatetime>:updatedAfterThisDateExclusive order by d.updatedatetime";
    	
    	Query query = entityManager.createQuery(sql);
    	query.setParameter("demographicId", demographicId);
    	query.setParameter("programId", programId);
    	query.setParameter("providerNo", providerNo);
    	query.setParameter("updatedAfterThisDateExclusive", updatedAfterThisDateExclusive);
    	setLimit(query, itemsToReturn);

        @SuppressWarnings("unchecked")
        List<Document> documents = query.getResultList();
        return documents;
    }

    //for integrator
	public List<Integer> findDemographicIdsSince(Date since) {
		
		String sql = "SELECT DISTINCT c.id.moduleId " + "FROM Document d, CtlDocument c WHERE c.id.documentNo=d.documentNo AND c.id.module='demographic'";
		
		if(since != null) {
			sql += " AND d.updatedatetime > :since";
		}
		
		Query query = entityManager.createQuery(sql);
 		
		if(since != null) {
			query.setParameter("since",since);
		}
		@SuppressWarnings("unchecked")
		List<Integer> resultDocs = query.getResultList();
		
		
		return resultDocs;
	}
	
	public Document findByDocumentNo(int docNo) {
		Query query = entityManager.createNamedQuery("Document.findByDocumentNo");
		query.setParameter("documentNo", docNo);
		try {
			return getSingleResultOrNull(query);
		} catch (Exception e) {}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Document> findByDoctype(String docType) {
		Query query = entityManager.createQuery("SELECT d FROM Document d WHERE d.doctype = :doctype AND d.status = 'A'");
		query.setParameter("doctype", docType);
		return query.getResultList();
	}
	
	public List<Document> findByDoctypeAndProviderNo(String docType, String provider_no, Integer isPublic) {
		Query query = entityManager.createQuery("SELECT d FROM Document d WHERE d.doctype = :doctype AND d.status = 'A' and d.doccreator=:doccreator and d.public1=:public1 ORDER BY updatedatetime DESC");
		query.setParameter("doctype", docType);
		query.setParameter("doccreator", provider_no);
		query.setParameter("public1", isPublic);
		
		return query.getResultList();
		
	}
}
