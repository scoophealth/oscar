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

	private ArrayList<Integer> linkedHnrIds=new ArrayList<Integer>();
	private ArrayList<FacilityDemographicPrimaryKey> linkedCaisiIds=new ArrayList<FacilityDemographicPrimaryKey>();
	
	/**
	 * @param linkString should be of the format linked.<ClientLink.Type.name()>.<type specific identifier>
	 */
	public void addLinkedId(String linkString)
	{
		String[] linkStringSplit=linkString.split("\\.");

		if (ClientLink.Type.HNR.name().equals(linkStringSplit[1]))
		{
			linkedHnrIds.add(Integer.parseInt(linkStringSplit[2]));
		}
		else if (ClientLink.Type.OSCAR_CAISI.name().equals(linkStringSplit[1]))
		{
			FacilityDemographicPrimaryKey pk=new FacilityDemographicPrimaryKey();
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
	    List<ClientLink> currentLinks=clientLinkDao.findByClientIdAndType(demographicId, true, ClientLink.Type.HNR);
	    HashSet<Integer> currentLinkIds=new HashSet<Integer>();
	    
	    // process entries removed and populate hashset for additions lookup
	    for (ClientLink clientLink : currentLinks)
	    {
	    	currentLinkIds.add(clientLink.getRemoteLinkId());
	    	
	    	if (!linkedHnrIds.contains(clientLink.getRemoteLinkId()))
	    	{
	    		clientLink.setUnlinkDate(new Date());
	    		clientLink.setUnlinkProviderNo(provider.getProviderNo());
	    		clientLinkDao.merge(clientLink);
	    	}
	    }
	    
	    // process entries added
	    for (Integer tempId : linkedHnrIds)
	    {
	    	if (!currentLinkIds.contains(tempId))
	    	{
	    		ClientLink clientLink=new ClientLink();
	    		clientLink.setClientId(demographicId);
	    		clientLink.setLinkDate(new Date());
	    		clientLink.setLinkProviderNo(provider.getProviderNo());
	    		clientLink.setLinkType(ClientLink.Type.HNR);
	    		clientLink.setRemoteLinkId(tempId);
	    		clientLinkDao.persist(clientLink);
	    	}
	    }
    }
}
