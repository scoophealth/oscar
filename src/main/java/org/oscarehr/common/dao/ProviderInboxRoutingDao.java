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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.IncomingLabRules;
import org.oscarehr.common.model.ProviderInboxItem;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

import oscar.oscarLab.ca.on.CommonLabResultData;

/**
 *
 * @author jay gallagher
 */
@Repository
public class ProviderInboxRoutingDao extends AbstractDao<ProviderInboxItem> {

	public ProviderInboxRoutingDao() {
		super(ProviderInboxItem.class);
	}
    



    public boolean removeLinkFromDocument(String docType, Integer docId, String providerNo) {
    	return CommonLabResultData.updateReportStatus(docId, providerNo, 'X', "Archived", "DOC");    	
    }

	public List<ProviderInboxItem> getProvidersWithRoutingForDocument(String docType, Integer docId) {
		Query query = entityManager.createQuery("select p from ProviderInboxItem p where p.labType = ? and p.labNo = ?");
		query.setParameter(1, docType);
		query.setParameter(2, docId);

		@SuppressWarnings("unchecked")
		List<ProviderInboxItem> results = query.getResultList();

		return results;
	}

	public boolean hasProviderBeenLinkedWithDocument(String docType, Integer docId, String providerNo) {
		Query query = entityManager.createQuery("select p from ProviderInboxItem p where p.labType = ? and p.labNo = ? and p.providerNo=?");
		query.setParameter(1, docType);
		query.setParameter(2, docId);
		query.setParameter(3, providerNo);

		@SuppressWarnings("unchecked")
		List<ProviderInboxItem> results = query.getResultList();

		return (results.size() > 0);
	}

	public int howManyDocumentsLinkedWithAProvider(String providerNo) {
		Query query = entityManager.createQuery("select p from ProviderInboxItem p where p.providerNo=?");
		query.setParameter(1, providerNo);

		@SuppressWarnings("unchecked")
		List<ProviderInboxItem> results = query.getResultList();

		return results.size();
	}

	/**
	 * Adds lab results to the provider inbox
	 * 
	 * @param providerNo
	 * 		Provider to add lab results to
	 * @param labNo
	 * 		Document id to be added to the inbox
	 * @param labType
	 * 		Type of the document to be added. Available document types are defined in {@link oscar.oscarLab.ca.on.LabResultData} class.
	 * 
	 */
	// TODO Replace labType parameter with an enum
	@SuppressWarnings("unchecked")
    public void addToProviderInbox(String providerNo, Integer labNo, String labType) {
		ArrayList<String> listofAdditionalProviders = new ArrayList<String>();
		boolean fileForMainProvider = false;

		try {
			Query rulesQuery = entityManager.createQuery("FROM IncomingLabRules r WHERE r.archive = 0 AND r.providerNo = :providerNo");
			rulesQuery.setParameter("providerNo", providerNo);

			for (IncomingLabRules rules : (List<IncomingLabRules>) rulesQuery.getResultList()) {
				String status = rules.getStatus();
				String frwdProvider = rules.getFrwdProviderNo();

				listofAdditionalProviders.add(frwdProvider);
				if (status != null && status.equals("F")) fileForMainProvider = true;
			}

			ProviderInboxItem p = new ProviderInboxItem();
			p.setProviderNo(providerNo);
			p.setLabNo(labNo);
			p.setLabType(labType);
			p.setStatus(fileForMainProvider ? ProviderInboxItem.FILE : ProviderInboxItem.NEW);

			if (!hasProviderBeenLinkedWithDocument(labType, labNo, providerNo)) persist(p);

			for (String s : listofAdditionalProviders) {
				if (!hasProviderBeenLinkedWithDocument(labType, labNo, s)) {
					addToProviderInbox(s, labNo, labType);
				}
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}

	}

}
