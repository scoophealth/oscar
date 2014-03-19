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
import org.oscarehr.sharingcenter.model.ExportedDocument;
import org.springframework.stereotype.Repository;

@Repository
public class ExportedDocumentDao extends AbstractDao<ExportedDocument> {

    public ExportedDocumentDao() {
        super(ExportedDocument.class);
    }

    /**
     * Finds all Exported Documents (to XDS) in our Data model
     * 
     * @return
     * list of all exported documents
     */
    public List<ExportedDocument> findAll() {
        String sql = "FROM ExportedDocument";
        Query query = entityManager.createQuery(sql);

        @SuppressWarnings("unchecked")
        List<ExportedDocument> retVal = query.getResultList();
        return retVal;
    }

    /**
     * Finds all exported documents for a patient
     * 
     * @param demographicId
     * @return ExportedDocument objects for a patient
     */
    public List<ExportedDocument> findByPatient(int demographicId) {
        String sql = "FROM ExportedDocument e where e.demographicNo = ? ORDER BY id DESC";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, demographicId);

        @SuppressWarnings("unchecked")
        List<ExportedDocument> retVal = query.getResultList();
        return retVal;
    }

    /**
     * Finds all exported documents for a patient in a specific affinity domain
     * 
     * @param affinityDomain
     * @param demographicId
     * @return ExportedDocument objects for a patient in an affinty domain
     */
    public List<ExportedDocument> findByPatientInDomain(int affinityDomain, int demographicId) {
        String sql = "FROM ExportedDocument e where e.affinityDomain = ? and e.demographicNo = ? ORDER BY id DESC";

        Query query = entityManager.createQuery(sql);
        query.setParameter(1, affinityDomain);
        query.setParameter(2, demographicId);

        @SuppressWarnings("unchecked")
        List<ExportedDocument> retVal = query.getResultList();
        return retVal;
    }

    /**
     * Finds all exported documents for a patient in a specific affinity domain provided that the documents
     * are of a specific type.
     * 
     * @param affinityDomain
     * @param demographicId
     * @param documentType
     * @return ExportedDocument documents of a specific type for a patient in an affinity domain
     */
    public List<ExportedDocument> findByTypeForPatientInDomain(int affinityDomain, int demographicId, String documentType) {
        String sql = "FROM ExportedDocument e where e.affinityDomain = ? and e.demographicNo = ? and e.documentType = ? ORDER BY id DESC";

        Query query = entityManager.createQuery(sql);
        query.setParameter(1, affinityDomain);
        query.setParameter(2, demographicId);
        query.setParameter(3, documentType);

        @SuppressWarnings("unchecked")
        List<ExportedDocument> retVal = query.getResultList();
        return retVal;
    }

}
