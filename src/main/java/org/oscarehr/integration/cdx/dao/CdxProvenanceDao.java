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


import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.integration.cdx.model.CdxPerson;
import org.oscarehr.integration.cdx.model.CdxProvenance;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;


@Repository
public class CdxProvenanceDao extends AbstractDao<CdxProvenance> {

    public CdxProvenanceDao() {
        super(CdxProvenance.class);
    }


    public CdxProvenance getCdxProvenance(int id) {
        return find(id);
    }

    public void logSentAction(String doc) {
        // pseudo code, please implement:

        // the "doc" xml structure contains all the information about the sent document
        // use xpath to retrieve all data for CDX Provenance entry:
        // doc_id, version, effective_time, parent_doc, set_id, in_fullfillment, etc.
        // note, some of them may be optional.
        // create new Provenance instance, populate with extracted data
        // create now entry in "log" table to log user action
        // populate log column in Provenance model with id of newly created log entry
        // persist both objects
    }

    public List<CdxProvenance> findByKindAndInFulFillment(String kind, String inFulfillmentId) {
        String sql = "FROM CdxProvenance p where p.kind = :kind and p.inFulfillmentOfId = :inFulfillmentId";
        Query query = entityManager.createQuery(sql);
        query.setParameter("kind", kind);
        query.setParameter("inFulfillmentId", inFulfillmentId);
        return query.getResultList();
    }

}