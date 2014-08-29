/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.DemographicTransfer;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.common.dao.ClientLinkDao;
import org.oscarehr.common.model.ClientLink;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.FacilityDemographicPrimaryKey;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ManageLinkedClientsAction {
	public static Logger logger = MiscUtils.getLogger();
	public static ClientLinkDao clientLinkDao = (ClientLinkDao) SpringUtils.getBean("clientLinkDao");

	private Integer newHnrLinkedId = null;
	private ArrayList<FacilityDemographicPrimaryKey> linkedCaisiIds = new ArrayList<FacilityDemographicPrimaryKey>();

	/**
	 * @param linkString should be of the format linked.<ClientLink.Type.name()>.<type specific identifier>
	 */
	public void addLinkedId(String linkString) {
		String[] linkStringSplit = linkString.split("\\.");

		if (ClientLink.Type.HNR.name().equals(linkStringSplit[1])) {
			// this is an odd case where we only support 1 HNR link so we'll just take the last selected one.
			newHnrLinkedId = Integer.parseInt(linkStringSplit[2]);
		} else if (ClientLink.Type.OSCAR_CAISI.name().equals(linkStringSplit[1])) {
			FacilityDemographicPrimaryKey pk = new FacilityDemographicPrimaryKey();
			pk.setFacilityId(Integer.parseInt(linkStringSplit[2]));
			pk.setDemographicId(Integer.parseInt(linkStringSplit[3]));
			linkedCaisiIds.add(pk);
		}
	}

	public void saveLinkedIds(LoggedInInfo loggedInInfo, Facility facility, Provider provider, Integer demographicId) {
		saveHnrLinkedIds(facility, provider, demographicId);
		saveCaisiLinkedIds(loggedInInfo, facility,provider, demographicId);
	}

	private void saveCaisiLinkedIds(LoggedInInfo loggedInInfo, Facility facility,Provider provider, Integer demographicId) {
		try {
			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, facility);
			List<DemographicTransfer> tempLinks = demographicWs.getDirectlyLinkedDemographicsByDemographicId(demographicId);
			HashSet<FacilityDemographicPrimaryKey> currentLinks = new HashSet<FacilityDemographicPrimaryKey>();

			// check for removals and populate a hashSet (for later use)
			for (DemographicTransfer demographicTransfer : tempLinks) {
				FacilityDemographicPrimaryKey pk = new FacilityDemographicPrimaryKey();
				pk.setFacilityId(demographicTransfer.getIntegratorFacilityId());
				pk.setDemographicId(demographicTransfer.getCaisiDemographicId());
				currentLinks.add(pk);

				if (!linkedCaisiIds.contains(pk)) demographicWs.unLinkDemographics(demographicId, pk.getFacilityId(), pk.getDemographicId());
			}

			// process additions
			for (FacilityDemographicPrimaryKey pk : linkedCaisiIds) {
				if (!currentLinks.contains(pk)) demographicWs.linkDemographics(provider.getProviderNo(), demographicId, pk.getFacilityId(), pk.getDemographicId());
			}
		} catch (Exception e) {
	        logger.error("Unexpected Error.", e);
		}
	}

	private void saveHnrLinkedIds(Facility facility, Provider provider, Integer demographicId) {
		try {
	        List<ClientLink> currentLinks = clientLinkDao.findByFacilityIdClientIdType(facility.getId(), demographicId, true, ClientLink.Type.HNR);

	        boolean isCheckedClientExistingLink=false;
	        
	        // remove old links and check to see if the current link is an existing link
	        for (ClientLink existingLink : currentLinks)
	        {
	        	if (newHnrLinkedId==null || !newHnrLinkedId.equals(existingLink.getRemoteLinkId()))
	        	{
	        		existingLink.setUnlinkDate(new Date());
	        		existingLink.setUnlinkProviderNo(provider.getProviderNo());
	        		clientLinkDao.merge(existingLink);
	        	}
	        	else
	        	{
	        		isCheckedClientExistingLink=true;
	        	}
	        }
	        
	        // if something is checked and it wasn't previously checked, add it
	        // how unfortunate, since the removal and adding isn't an atomic operation
	        // this will lead to multiple hnr entries if on totally rare chance 2 threads collide running this method on 2 different linking ids.
	        // oh well, I took out the check for single hnr entry so it should still "function" even though there's an anomalie here.
	        if (newHnrLinkedId != null && !isCheckedClientExistingLink) {
	        	ClientLink newLink = new ClientLink();
	        	newLink.setFacilityId(facility.getId());
	        	newLink.setClientId(demographicId);
	        	newLink.setLinkDate(new Date());
	        	newLink.setLinkProviderNo(provider.getProviderNo());
	        	newLink.setLinkType(ClientLink.Type.HNR);
	        	newLink.setRemoteLinkId(newHnrLinkedId);
	        	clientLinkDao.persist(newLink);
	        }
        } catch (Exception e) {
	        logger.error("Unexpected Error.", e);
        }
	}
}
