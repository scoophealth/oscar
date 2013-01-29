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

package org.oscarehr.document.dao;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.document.model.CtlDocument;
import org.oscarehr.document.model.Document;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.util.ConversionUtils;

/**
 *
 * @author jaygallagher
 */
public class DocumentDAO extends HibernateDaoSupport {

	public Document getDocument(String documentNo) {
		Document document = getHibernateTemplate().get(Document.class, Long.parseLong(documentNo));
		return document;
	}

	public void save(Document document) {
		getHibernateTemplate().saveOrUpdate(document);
	}

	@SuppressWarnings("unchecked")
	public Demographic getDemoFromDocNo(String docNo) {//return null if no demographic linked to this document
		Demographic d = null;
		Integer i = Integer.parseInt(docNo);
		String q = "select d from Demographic d, CtlDocument c where c.id.module='demographic'" + " and c.moduleId!='-1' and c.moduleId=d.DemographicNo and c.id.documentNo=? ";
		List<Demographic> rs = getHibernateTemplate().find(q, i);
		if (rs.size() > 0) d = rs.get(0);
		return d;
	}

	@SuppressWarnings("unchecked")
	public CtlDocument getCtrlDocument(Integer docId) {
		List<CtlDocument> cList = null;
		Session session = null;
		try {
			session = getSession();
			Criteria c = session.createCriteria(CtlDocument.class);
			c = c.add(Restrictions.eq("id.documentNo", docId));
			cList = c.list();
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		} finally {
			if (session != null) {
				releaseSession(session);
			}
		}

		if (cList != null && cList.size() > 0) {
			return cList.get(0);
		} else {
			return null;
		}
	}

	public void saveCtlDocument(CtlDocument ctlDocument) {
		getHibernateTemplate().saveOrUpdate(ctlDocument);
	}

	@SuppressWarnings("unchecked")
	public int getNumberOfDocumentsAttachedToAProviderDemographics(String providerNo, Date startDate, Date endDate) {
		Query query = getSession().createSQLQuery("select count(*) from ctl_document c, demographic d,document doc where c.module_id = d.demographic_no and c.document_no = doc.document_no   and d.provider_no = :providerNo and doc.observationdate >= :startDate and doc.observationdate <= :endDate ");
		query.setParameter("providerNo", providerNo);
		query.setParameter("startDate", new Timestamp(startDate.getTime()));
		query.setParameter("endDate", new Timestamp(endDate.getTime()));
		List<BigInteger> result = query.list();
		if (result.isEmpty()) return 0;
		return result.get(0).intValue();
	}

	public void subtractPages(String documentNo, Integer i) {
		Document doc = getDocument(documentNo);
		doc.setNumberOfPages(doc.getNumberOfPages() - i);
		save(doc);
	}

	/**
	 * Finds all documents for the specified demographic id
	 * 
	 * @param demoNo
	 */
	@SuppressWarnings("unchecked")
	public List<org.oscarehr.document.model.Document> findByDemographicId(String demoNo) {
		List<Document> result = getHibernateTemplate().find("SELECT DISTINCT d FROM Document d, CtlDocument c WHERE d.status = c.status AND d.status != 'D' AND c.document_no=d.document_no AND " + "c.module='demographic' AND c.module_id = ?", demoNo);
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
    public List<Object[]> findDocuments(String module, String moduleid, String docType, boolean includePublic, boolean includeDeleted, boolean includeActive, String sort) {
		Map<String, Object> params = new HashMap<String, Object>();
	
		StringBuilder buf = new StringBuilder("SELECT DISTINCT c, d " +
				"FROM org.oscarehr.document.model.Document d, org.oscarehr.document.model.CtlDocument c " +
				"WHERE c.id.documentNo = d.id AND c.id.module = :module");
		params.put("module",module);
		
		boolean isShowingAllDocuments = docType == null || docType.equals("all") || docType.length() == 0; 
		
		if (includePublic) {
			if (isShowingAllDocuments) {
				buf.append(" AND d.m_public = 1");
			} else {
				buf.append(" AND d.m_public = 1 AND d.doctype = :doctype");
				params.put("doctype", docType);
			}
		} else {
			if (isShowingAllDocuments) { 
				buf.append(" AND c.moduleId = :moduleId AND d.m_public = 0");
				params.put("moduleId", ConversionUtils.fromIntString(moduleid));
			} else {
				buf.append(" AND c.moduleId = :moduleId AND d.m_public = 0 AND d.doctype = :doctype");
				params.put("doctype", docType);
				params.put("moduleId", ConversionUtils.fromIntString(moduleid));
			}
		}

		if (includeDeleted) {
			buf.append(" AND d.status = 'D'");
		} else if (includeActive) {
			buf.append(" AND d.status != 'D'");
		}
		buf.append(" ORDER BY ").append(sort);

		// TODO Refactor TX handling here to use Spring infrastructure. At the moment this is impossible as DAOs are non-interface based 
		Session session = getSession();
		try {
			
			Query query = session.createQuery(buf.toString());
			for (Entry<String, Object> entry : params.entrySet())
				query.setParameter(entry.getKey(), entry.getValue());
			
			List<Object[]> result = query.list();
			return result;
		} catch (Exception e) {
			throw new RuntimeException("Unable to find documents", e);
		} finally {
			this.releaseSession(session);
		}
	}

	@SuppressWarnings("unchecked")
    public List<CtlDocument> findByDocumentNoAndModule(Integer ctlDocNo, String module) {
		Session session = getSession();
		try {
			Query query = session.createQuery("FROM CtlDocument c WHERE c.id.documentNo = :cltDocNo AND c.id.module = :module");
			query.setParameter("cltDocNo", ctlDocNo);
			query.setParameter("module", module);
			return query.list();
		} finally {
			this.releaseSession(session);
		}
    }

}
