/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */

package org.oscarehr.integration.cdx.dao;


import ca.uvic.leadlab.obibconnector.facades.datatypes.DocumentType;
import ca.uvic.leadlab.obibconnector.facades.receive.IDocument;
import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.integration.cdx.model.CdxProvenance;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

import static org.oscarehr.util.SpringUtils.getBean;


@Repository
public class CdxProvenanceDao extends AbstractDao<CdxProvenance> {

    public CdxProvenanceDao() {
        super(CdxProvenance.class);
    }


    public CdxProvenance getCdxProvenance(int id) {
        return find(id);
    }

    public void logSentAction(IDocument doc) {
        CdxProvenance prov = new CdxProvenance();
        prov.populate(doc);
        prov.setAction("SEND");
        this.persist(prov);

        CdxAttachmentDao atDao = getBean(CdxAttachmentDao.class);

        atDao.saveAttachments(doc,prov);
    }

    public List<CdxProvenance> findByKindAndInFulFillment(DocumentType kind, String inFulfillmentId) {
        String sql = "FROM CdxProvenance p where p.kind = :kind and p.inFulfillmentOfId = :inFulfillmentId ORDER BY p.version DESC";
        Query query = entityManager.createQuery(sql);
        query.setParameter("kind", kind.label);
        query.setParameter("inFulfillmentId", inFulfillmentId);
        return query.getResultList();
    }

    public List<CdxProvenance> findByInFulFillment(String inFulfillmentId) {
        String sql = "FROM CdxProvenance p where p.inFulfillmentOfId = :inFulfillmentId";
        Query query = entityManager.createQuery(sql);
        query.setParameter("inFulfillmentId", inFulfillmentId);
        return query.getResultList();
    }

    public CdxProvenance findByDocumentNo(int documentNo) {

        String sql = "FROM CdxProvenance p where p.documentNo = :docno ORDER BY p.version DESC, p.effectiveTime DESC";
        Query query = entityManager.createQuery(sql);
        query.setParameter("docno", documentNo);
        List<CdxProvenance> results = query.getResultList();
        if (results.isEmpty())
            return null;
        else return results.get(0);
    }

    public CdxProvenance findByDocumentId(String docId) {

        String sql = "FROM CdxProvenance p where p.documentId = :docid";
        Query query = entityManager.createQuery(sql);
        query.setParameter("docid", docId);
        List<CdxProvenance> results = query.getResultList();
        if (results.isEmpty())
            return null;
        else return results.get(0);
    }


    public List<CdxProvenance> findVersionsOrderDesc(String setId) {
        String sql = "FROM CdxProvenance p where p.setId = :setId ORDER BY p.version DESC";
        Query query = entityManager.createQuery(sql);
        query.setParameter("setId", setId);

        return query.getResultList();
    }

    public List<CdxProvenance> findChildDocuments(String docId) {
        String sql = "FROM CdxProvenance p where p.parentDoc = :docId ORDER BY p.version DESC, p.effectiveTime DESC";
        Query query = entityManager.createQuery(sql);
        query.setParameter("docId", docId);

        return query.getResultList();
    }

    public List<CdxProvenance> findFulfillingDocuments(String docId) {
        String sql = "FROM CdxProvenance p where p.inFulfillmentOfId = :docId ORDER BY p.version DESC, p.effectiveTime DESC";
        Query query = entityManager.createQuery(sql);
        query.setParameter("docId", docId);

        return query.getResultList();
    }

    public List<CdxProvenance> findReceivedVersionsOrderDesc(String setId) {
        String sql = "FROM CdxProvenance p where p.setId = :setId and p.action = 'import' ORDER BY p.version DESC, p.effectiveTime DESC";
        Query query = entityManager.createQuery(sql);
        query.setParameter("setId", setId);

        return query.getResultList();
    }

    public List<CdxProvenance> findByMsgId(String msgId) {
        String sql = "FROM CdxProvenance p where p.msgId = :msgId";
        Query query = entityManager.createQuery(sql);
        query.setParameter("msgId", msgId);

        return query.getResultList();
    }


    public List<CdxProvenance> findRelatedDocsBySetId(String setId, String documentId) {
        String sql = "FROM CdxProvenance p where p.setId = :setId and not p.documentId = :docId";
        Query query = entityManager.createQuery(sql);
        query.setParameter("docId", documentId);
        query.setParameter("setId", setId);

        return query.getResultList();
    }

    public void removeProv(String msgId) {
        String sql = "DELETE FROM CdxProvenance p where p.msgId = :msgId";
        Query query = entityManager.createQuery(sql);
        query.setParameter("msgId", msgId);

        query.executeUpdate();
    }

    public void updateProvDistributionStatus(String documentId, String distributionStatus) {
        String sql = "UPDATE CdxProvenance p set p.distributionStatus = :distributionStatus where p.documentId = :documentId";
        Query query = entityManager.createQuery(sql);
        query.setParameter("documentId", documentId);
        query.setParameter("distributionStatus", distributionStatus);

        query.executeUpdate();
    }
}