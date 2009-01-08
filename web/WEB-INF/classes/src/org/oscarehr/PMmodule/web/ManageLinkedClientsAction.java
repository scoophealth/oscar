package org.oscarehr.PMmodule.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.client.CachedDemographic;
import org.oscarehr.caisi_integrator.ws.client.DemographicWs;
import org.oscarehr.common.dao.ClientLinkDao;
import org.oscarehr.common.model.ClientLink;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.FacilityDemographicPrimaryKey;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

public class ManageLinkedClientsAction {
	public static Logger logger = LogManager.getLogger(ManageLinkedClientsAction.class);
	public static CaisiIntegratorManager caisiIntegratorManager = (CaisiIntegratorManager) SpringUtils.getBean("caisiIntegratorManager");
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
			newHnrLinkedId=Integer.parseInt(linkStringSplit[2]);
		} else if (ClientLink.Type.OSCAR_CAISI.name().equals(linkStringSplit[1])) {
			FacilityDemographicPrimaryKey pk = new FacilityDemographicPrimaryKey();
			pk.setFacilityId(Integer.parseInt(linkStringSplit[2]));
			pk.setDemographicId(Integer.parseInt(linkStringSplit[3]));
			linkedCaisiIds.add(pk);
		}
	}

	public void saveLinkedIds(Facility facility, Provider provider, Integer demographicId) {
		saveHnrLinkedIds(facility, provider, demographicId);
		saveCaisiLinkedIds(facility, provider, demographicId);
	}

	private void saveCaisiLinkedIds(Facility facility, Provider provider, Integer demographicId) {
		try {
			DemographicWs demographicWs = caisiIntegratorManager.getDemographicWs(facility.getId());
			List<CachedDemographic> tempLinks = demographicWs.getDirectlyLinkedCachedDemographicsByDemographicId(demographicId);
			HashSet<FacilityDemographicPrimaryKey> currentLinks = new HashSet<FacilityDemographicPrimaryKey>();

			// check for removals and populate a hashSet (for later use)
			for (CachedDemographic cachedDemographic : tempLinks) {
				FacilityDemographicPrimaryKey pk = new FacilityDemographicPrimaryKey(cachedDemographic.getFacilityIdIntegerCompositePk());
				currentLinks.add(pk);

				if (!linkedCaisiIds.contains(pk)) demographicWs.unLinkDemographics(demographicId, pk.getFacilityId(), pk.getDemographicId());
			}

			// process additions
			for (FacilityDemographicPrimaryKey pk : linkedCaisiIds) {
				if (!currentLinks.contains(pk)) demographicWs.linkDemographics(provider.getProviderNo(), demographicId, pk.getFacilityId(), pk.getDemographicId());
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private void saveHnrLinkedIds(Facility facility, Provider provider, Integer demographicId) {
		List<ClientLink> currentLinks = clientLinkDao.findByClientIdAndType(demographicId, true, ClientLink.Type.HNR);
		if (currentLinks.size() > 1) throw (new IllegalArgumentException("The client passed in should have had at most 1 hnr link. links.size=" + currentLinks.size()));

		boolean addNewEntry = false;
		boolean removeOldEntry = false;

		// if we never use to have an entry but we do now add entry
		if (currentLinks.size() == 0 && newHnrLinkedId != null) addNewEntry = true;

		// if we use to have an entry but we do not anymore, just remove entry
		if (currentLinks.size() == 1 && newHnrLinkedId == null) removeOldEntry = true;

		// if we use to have an entry, and we still do, but the entry is different, then update
		if (currentLinks.size() == 1 && newHnrLinkedId != null && !newHnrLinkedId.equals(currentLinks.get(0).getRemoteLinkId())) addNewEntry = removeOldEntry = true;

		// process entry removed
		if (removeOldEntry) {
			ClientLink existingLink=currentLinks.get(0);
			existingLink.setUnlinkDate(new Date());
			existingLink.setUnlinkProviderNo(provider.getProviderNo());
			clientLinkDao.merge(existingLink);
		}

		// process entry added
		if (addNewEntry) {
			ClientLink newLink = new ClientLink();
			newLink.setClientId(demographicId);
			newLink.setLinkDate(new Date());
			newLink.setLinkProviderNo(provider.getProviderNo());
			newLink.setLinkType(ClientLink.Type.HNR);
			newLink.setRemoteLinkId(newHnrLinkedId);
			clientLinkDao.persist(newLink);
		}
	}
}
