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

import org.oscarehr.common.model.ConsultDocs;
import org.springframework.stereotype.Repository;

import oscar.util.ConversionUtils;

@Repository
@SuppressWarnings("unchecked")
public class ConsultDocsDao extends AbstractDao<ConsultDocs>{

	public ConsultDocsDao() {
		super(ConsultDocs.class);
	}

	public List<ConsultDocs> findByRequestIdDocNoDocType(Integer requestId, Integer documentNo, String docType) {
	  	String sql = "select x from ConsultDocs x where x.requestId=? and x.documentNo=? and x.docType=? and x.deleted is NULL";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,requestId);
    	query.setParameter(2,documentNo);
    	query.setParameter(3,docType);

        List<ConsultDocs> results = query.getResultList();
        return results;
	}

	public List<ConsultDocs> findByRequestId(Integer requestId) {
	  	String sql = "select x from ConsultDocs x where x.requestId=? and x.deleted is NULL";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,requestId);

        List<ConsultDocs> results = query.getResultList();
        return results;
	}
	
	public List<ConsultDocs> findByRequestIdAndDocType(Integer requestId, String docType) {
	  	String sql = "select x from ConsultDocs x where x.requestId=? and x.docType=? and x.deleted is NULL";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,requestId);
    	query.setParameter(2, docType);

        List<ConsultDocs> results = query.getResultList();
        return results;
	}

	public void detachDocConsult(String docNo, String consultId, String docType) {
		List<ConsultDocs> consultDocs = findByRequestIdDocNoDocType(ConversionUtils.fromIntString(consultId), ConversionUtils.fromIntString(docNo), docType);
		for (ConsultDocs consultDoc : consultDocs) {
			consultDoc.setDeleted("Y");
			merge(consultDoc);
		}
	}
	
	public void attachDocConsult(String providerNo, String docNo, String consultId, String docType) {
		ConsultDocs consultDoc = new ConsultDocs();
		consultDoc.setRequestId(ConversionUtils.fromIntString(consultId));
		consultDoc.setDocumentNo(ConversionUtils.fromIntString(docNo));
		consultDoc.setDocType(docType);
		consultDoc.setAttachDate(new Date());
		consultDoc.setProviderNo(providerNo);
		persist(consultDoc);
	}
	

	public List<Object[]> findLabs(Integer consultationId) {
		String sql = "FROM ConsultDocs cd, PatientLabRouting plr " +
				"WHERE plr.labNo = cd.documentNo " +
				"AND cd.requestId = :consultationId " +
				"AND cd.docType = :docType " +
				"AND cd.deleted IS NULL " +
				"ORDER BY cd.documentNo";
		Query q = entityManager.createQuery(sql);
		q.setParameter("consultationId", consultationId);
		q.setParameter("docType", ConsultDocs.DOCTYPE_LAB);
		return q.getResultList();
	}
}
