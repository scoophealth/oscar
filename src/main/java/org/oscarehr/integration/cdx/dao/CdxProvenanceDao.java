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


import ca.uvic.leadlab.obibconnector.facades.receive.IDocument;
import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.integration.cdx.model.CdxProvenance;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;


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
    }

    public List<CdxProvenance> findByKindAndInFulFillment(String kind, String inFulfillmentId) {
        String sql = "FROM CdxProvenance p where p.kind = :kind and p.inFulfillmentOfId = :inFulfillmentId";
        Query query = entityManager.createQuery(sql);
        query.setParameter("kind", kind);
        query.setParameter("inFulfillmentId", inFulfillmentId);
        return query.getResultList();
    }

    public CdxProvenance  findLatestVersion(String documentID) {
        String sql = "FROM CdxProvenance p where p.documentId = :docId ";
        Query query = entityManager.createQuery(sql);
        query.setParameter("docId", documentID);

        List<CdxProvenance> versions = query.getResultList();

        if (versions.isEmpty())
            return null;
        else {
            CdxProvenance result = versions.get(0);
            for (CdxProvenance v : versions) {
                if (v.getVersion() > result.getVersion()) result = v;
            }

            return result;
            }
        }


    }