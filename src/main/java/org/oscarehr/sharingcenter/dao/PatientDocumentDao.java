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
package org.oscarehr.sharingcenter.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.sharingcenter.model.PatientDocument;
import org.springframework.stereotype.Repository;

@Repository
public class PatientDocumentDao extends AbstractDao<PatientDocument> {

    public PatientDocumentDao() {
        super(PatientDocument.class);
    }

    /**
     * Finds all Patient Document objects for a specific patient (without
     * pagination)
     *
     * @param demographicId Oscar's patient ID
     * @return list of all Patient Documents
     */
    public List<PatientDocument> findPatientDocuments(int demographicId) {
        String sql = "FROM PatientDocument e where e.demographic_no = ?";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, demographicId);

        @SuppressWarnings("unchecked")
        List<PatientDocument> retVal = query.getResultList();
        return retVal;
    }

    /**
     * Checks to see if a documentUniqueId for a repositoryUniqueId exists or
     * not.
     *
     * @param documentUniqueId   external unique document id
     * @param repositoryUniqueId external repository (xds) id
     * @return boolean whether or not the document exists
     */
    public boolean documentExists(String documentUniqueId, String repositoryUniqueId) {
        String sql = "SELECT count(*) FROM PatientDocument e where e.uniqueDocumentId = ? AND e.repositoryUniqueId = ?";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, documentUniqueId);
        query.setParameter(2, repositoryUniqueId);

        int retVal = ((Long) query.getSingleResult()).intValue();
        return retVal > 0;
    }

    public PatientDocument getDocument(String documentUniqueId, String repositoryUniqueId) {
        String sql = "FROM PatientDocument e where e.uniqueDocumentId = ? AND e.repositoryUniqueId = ?";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, documentUniqueId);
        query.setParameter(2, repositoryUniqueId);

        query.setMaxResults(1);
        PatientDocument retVal = getSingleResultOrNull(query);
        return retVal;
    }

    /**
     * Gets the count of all records under a demographic id.
     *
     * @param demographicId Oscar's patient ID
     * @return a count of the documents.
     */
    public int getDocumentCount(int demographicId) {
        String sql = "SELECT count(*) FROM PatientDocument e where e.demographic_no = ?";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, demographicId);

        int retVal = ((Long) query.getSingleResult()).intValue();
        return retVal;
    }

    /**
     * Finds a list of Patient Document objects for a patient with pagination
     * (limit and offset)
     *
     * @param demographicId Oscar's patient ID
     * @param offset        The offset of the result-set (index/starting row)
     * @param elements      The number of elements/rows to return
     * @return a list of PatientDocument objects
     */
    public List<PatientDocument> findPatientDocumentsWithPagination(int demographicId, int offset, int elements) {
        String sql = "FROM PatientDocument e where e.demographic_no = ?";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, demographicId);
        query.setFirstResult(offset);
        query.setMaxResults(elements);

        @SuppressWarnings("unchecked")
        List<PatientDocument> retVal = query.getResultList();
        return retVal;
    }
}
