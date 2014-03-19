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
import org.oscarehr.sharingcenter.DocumentType;
import org.oscarehr.sharingcenter.model.DemographicExport;
import org.springframework.stereotype.Repository;

/**
 * The demographic export DAO to access the document_export table.
 * @author oscar
 *
 */
@Repository
public class DemographicExportDao extends AbstractDao<DemographicExport> {

    /**
     * Initializes a new instance of the DemographicExportDao class.
     */
    public DemographicExportDao() {
        super(DemographicExport.class);
    }

    /**
     * Gets a list of all the demographic exports in the database.
     * @return Returns a list of DemographicExport objects.
     */
    @SuppressWarnings("unchecked")
    public List<DemographicExport> getAllDocuments() {
        String sql = "FROM DemographicExport";
        Query query = entityManager.createQuery(sql);

        return query.getResultList();
    }

    /**
     * Gets a list of all the demographic exports for a specific patient.
     * @param demographicNo The demographic number of the patient.
     * @return Returns a list of DemographicExport objects.
     */
    @SuppressWarnings("unchecked")
    public List<DemographicExport> getAllDocumentsForPatient(int demographicNo) {
        String sql = "SELECT id, document_type, document, de.demographic_no "
                + "FROM DemographicExport de, Demographic d "
                + "WHERE de.demographic_no = d.demographic_no AND de.demographic_no = ?";

        Query query = entityManager.createQuery(sql);
        query.setParameter(1, demographicNo);

        return query.getResultList();
    }

    /**
     * Gets a list of all the demographic exports of a specific document type.
     * @param documentType The document type.
     * @return Returns a list of DemographicExport objects.
     */
    @SuppressWarnings("unchecked")
    public List<DemographicExport> getAllDocumentsOfType(DocumentType documentType) {
        String sql = "FROM DemographicExport d WHERE d.documentType = ?";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, documentType.name());

        return query.getResultList();
    }

    /**
     * Gets a document by id.
     * @param id The id of the document.
     * @return Returns a DemographicExport object.
     */
    public DemographicExport getDocument(int id) {
        String sql = "FROM DemographicExport d WHERE d.id = ?";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, id);

        return getSingleResultOrNull(query);
    }
}
