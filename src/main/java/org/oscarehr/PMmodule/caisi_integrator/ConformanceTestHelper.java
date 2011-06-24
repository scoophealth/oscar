package org.oscarehr.PMmodule.caisi_integrator;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.ws.DemographicTransfer;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.ProviderCommunicationTransfer;
import org.oscarehr.caisi_integrator.ws.ProviderWs;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import oscar.oscarTickler.TicklerCreator;

/**
 * This class should not be used by anyone other than for conformance test only features. i.e. features that no one should ever use and should never be enabled except during conformance testing.
 */
public final class ConformanceTestHelper {
	private static Logger logger = MiscUtils.getLogger();

	public static void populateLocalTicklerWithRemoteProviderMessageFollowUps() {
		try {
			LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();

			ProviderWs providerWs = CaisiIntegratorManager.getProviderWs();
			List<ProviderCommunicationTransfer> followUps = providerWs.getProviderCommunications(loggedInInfo.loggedInProvider.getProviderNo(), "FOLLOWUP", true);

			if (followUps == null) return;

			logger.debug("Folowups found : " + followUps.size());

			for (ProviderCommunicationTransfer providerCommunication : followUps) {
				Document doc = XmlUtils.toDocument(providerCommunication.getData());
				Node root = doc.getFirstChild();
				String demographicId = XmlUtils.getChildNodeTextContents(root, "destinationDemographicId");
				String note = XmlUtils.getChildNodeTextContents(root, "note");

				TicklerCreator t = new TicklerCreator();

				logger.debug("Create tickler : " + demographicId + ", " + providerCommunication.getDestinationProviderId() + ", " + note);
				t.createTickler(demographicId, providerCommunication.getDestinationProviderId(), note);

				providerWs.deactivateProviderCommunication(providerCommunication.getId());
			}
		} catch (Exception e) {
			logger.error("Error", e);
		}
	}

	public static void copyLinkedDemographicsPropertiesToLocal(Integer localDemographicId) {
		try {
			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
			List<DemographicTransfer> directLinks=demographicWs.getDirectlyLinkedDemographicsByDemographicId(localDemographicId);
			
			logger.debug("found linked demographics size:"+directLinks.size());
			
			if (directLinks.size()>0)
			{
				DemographicTransfer demographicTransfer=directLinks.get(0);
				
				logger.debug("remoteDemographic:"+ReflectionToStringBuilder.toString(demographicTransfer));
				
				DemographicDao demographicDao=(DemographicDao) SpringUtils.getBean("demographicDao");
				Demographic demographic=demographicDao.getDemographicById(localDemographicId);
				
				CaisiIntegratorManager.copyDemographicFieldsIfNotNull(demographicTransfer, demographic);
				
				demographic.setRosterDate(new Date());
				
				demographicDao.save(demographic);				
			}
		} catch (Exception e) {
			logger.error("Error", e);
		}
	}
}

