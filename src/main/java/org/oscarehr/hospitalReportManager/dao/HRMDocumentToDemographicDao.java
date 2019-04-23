/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.hospitalReportManager.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.common.model.ConsultDocs;
import org.oscarehr.common.model.EFormDocs;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class HRMDocumentToDemographicDao extends AbstractDao<HRMDocumentToDemographic> {
	
	public HRMDocumentToDemographicDao() {
		super(HRMDocumentToDemographic.class);
	}
	
	
	public List<HRMDocumentToDemographic> findByDemographicNo(String demographicNo) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.demographicNo=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, demographicNo);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToDemographic> documentToDemographics = query.getResultList();
		return documentToDemographics;
	}

	public List<HRMDocumentToDemographic> findByHrmDocumentId(String hrmDocumentId) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.hrmDocumentId=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, hrmDocumentId);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToDemographic> documentToDemographics = query.getResultList();
		return documentToDemographics;
	}
	
	/**
	 * Gets all HRMDocumentToDemographics where the hrmDocumentId matches the documentNo in ConsultDocs, the requestId equals the given consultationId, and the docType is 'H' for HRM
	 * @param consultationId
	 * @return A list of HRMDocumentToDemographic objects, the list is empty if an error occurs or no results
	 */
	@SuppressWarnings("unchecked")
	public List<HRMDocumentToDemographic> findHRMDocumentsAttachedToConsultation(String consultationId) {
		//Creates a new empty list, it remains empty if an error occurs
		List<HRMDocumentToDemographic> attachedHRMDocumentToDemographics = new ArrayList<HRMDocumentToDemographic>();
		
		if (!consultationId.equalsIgnoreCase("null") || !consultationId.equals("") || !consultationId.equals(null))
		{
			try {
				//Parses the consultationId to an integer
				Integer parsedConsultationId = Integer.parseInt(consultationId);

				//Creates the SQL
				String sql = "SELECT hdtd "
						+ "FROM " + this.modelClass.getName() + " hdtd "
						+ "WHERE hdtd.hrmDocumentId IN (SELECT cd.documentNo "
						+ "FROM " + ConsultDocs.class.getName() + " cd "
						+ "WHERE cd.requestId = ? AND cd.docType = 'H' AND cd.deleted IS NULL)";

				//Creates the query using the SQL
				Query query = entityManager.createQuery(sql);
				//Sets the query parameters
				query.setParameter(1, parsedConsultationId);
				//Gets the query results and converts them to ConsultDocs
				attachedHRMDocumentToDemographics = query.getResultList();
			}
			catch (NumberFormatException nfe) {
				MiscUtils.getLogger().error("The consultationId is not a valid integer. consultationId: " + consultationId);
			}
			catch (IllegalStateException ise) {
				MiscUtils.getLogger().error("The entity manager has been closed" + System.getProperty("line.separator") + ise.getMessage());
			}
			catch (IllegalArgumentException iae) {
				MiscUtils.getLogger().error("An IllegalArgumentException has occured, the cause could be from the SQL or the consultationId" + System.getProperty("line.separator") + iae.getMessage());
			}
		}
		
		return attachedHRMDocumentToDemographics;
	}
	
	@SuppressWarnings("unchecked")
	public List<HRMDocumentToDemographic> findHRMDocumentsAttachedToEForm(String fdid) {
		//Creates a new empty list, it remains empty if an error occurs
		List<HRMDocumentToDemographic> attachedHRMDocumentToDemographics = new ArrayList<HRMDocumentToDemographic>();
		
		if(StringUtils.isEmpty(fdid)) {
			return attachedHRMDocumentToDemographics;
		}
		
		if(fdid == null) {
			return attachedHRMDocumentToDemographics;
		}
		
		if (!fdid.equalsIgnoreCase("null") || !fdid.equals("") || !fdid.equals(null))
		{
			try {
				//Parses the fdid to an integer
				Integer parsedFdid = Integer.parseInt(fdid);

				//Creates the SQL
				String sql = "SELECT hdtd "
						+ "FROM " + this.modelClass.getName() + " hdtd "
						+ "WHERE hdtd.hrmDocumentId IN (SELECT cd.documentNo "
						+ "FROM " + EFormDocs.class.getName() + " cd "
						+ "WHERE cd.fdid = ? AND cd.docType = 'H' AND cd.deleted IS NULL)";

				//Creates the query using the SQL
				Query query = entityManager.createQuery(sql);
				//Sets the query parameters
				query.setParameter(1, parsedFdid);
				//Gets the query results and converts them to EFormDocs
				attachedHRMDocumentToDemographics = query.getResultList();
			}
			catch (NumberFormatException nfe) {
				MiscUtils.getLogger().error("The fdid is not a valid integer. fdid: " + fdid);
			}
			catch (IllegalStateException ise) {
				MiscUtils.getLogger().error("The entity manager has been closed" + System.getProperty("line.separator") + ise.getMessage());
			}
			catch (IllegalArgumentException iae) {
				MiscUtils.getLogger().error("An IllegalArgumentException has occured, the cause could be from the SQL or the fdid" + System.getProperty("line.separator") + iae.getMessage());
			}
		}
		
		return attachedHRMDocumentToDemographics;
	}
}
