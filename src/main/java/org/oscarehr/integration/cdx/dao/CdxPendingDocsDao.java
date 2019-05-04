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
import org.oscarehr.integration.cdx.model.CdxPendingDoc;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;


@Repository
public class CdxPendingDocsDao extends AbstractDao<CdxPendingDoc> {

    public CdxPendingDocsDao() {
        super(CdxPendingDoc.class);
    }


    public CdxPendingDoc getCdxPendingDoc(int id) {
        return find(id);
    }


    public List<String> getPendingErrorDocs() {
        List<String> result = new ArrayList<String>();
        String sql = "FROM CdxPendingDocs pd where pd.reasonCode = :rc";
        Query query = entityManager.createQuery(sql);
        query.setParameter("rc", CdxPendingDoc.error);

        List<CdxPendingDoc> pds = query.getResultList();

        for (CdxPendingDoc pd : pds ) {
            result.add(pd.getDocId());
        }
        return result;
    }
}