package org.oscarehr.PMmodule.web;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.casemgmt.dao.ClientImageDAO;
import org.oscarehr.casemgmt.model.ClientImage;
import org.oscarehr.common.dao.ClientLinkDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.ClientLink;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

public class ManageHnrClient {
	private static Logger logger = LogManager.getLogger(ManageHnrClient.class);
	private static CaisiIntegratorManager caisiIntegratorManager = (CaisiIntegratorManager) SpringUtils.getBean("caisiIntegratorManager");
	private static DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	private static ClientLinkDao clientLinkDao = (ClientLinkDao) SpringUtils.getBean("clientLinkDao");
	private static ClientImageDAO clientImageDAO = (ClientImageDAO) SpringUtils.getBean("clientImageDAO");

	private Demographic demographic = null;
	private ClientImage clientImage = null;
	private ClientLink clientLink = null;
	private org.oscarehr.hnr.ws.client.Client hnrClient = null;

	public ManageHnrClient(Facility currentFacility, Provider currentProvider, Integer demographicId) {
		demographic = demographicDao.getDemographicById(demographicId);
		clientImage = clientImageDAO.getClientImage(demographicId);

		// we're only dealing with 1 hnr entry even if there's multiple because there should
		// only be 1, a minor issue about some of this code not being atomic makes multiple
		// entries theoretically possible though in reality it should never happen.
		List<ClientLink> temp = clientLinkDao.findByClientIdAndType(demographicId, true, ClientLink.Type.HNR);
		if (temp.size() > 0) clientLink = temp.get(0);

		if (caisiIntegratorManager.isEnableHealthNumberRegistry(currentFacility.getId()) && clientLink != null) {
			try {
				hnrClient = caisiIntegratorManager.getHnrClient(currentFacility, currentProvider, clientLink.getRemoteLinkId());
			} catch (Exception e) {
				logger.error("Unexpected error", e);
			}
		}
	}

	public Demographic getDemographic() {
		return demographic;
	}

	
	public org.oscarehr.hnr.ws.client.Client getHnrClient() {
    	return hnrClient;
    }

	public String getLocalClientImageUrl() {
		if (clientImage == null) return ("/images/defaultR_img.jpg");
		else return ("/imageRenderingServlet?source=local_client&clientId=" + demographic.getDemographicNo());
	}

	public String getHnrClientImageUrl() {
		if (hnrClient == null || hnrClient.getImage()==null) return ("/images/defaultR_img.jpg");
		else return ("/imageRenderingServlet?source=hnr_client&linkingId=" + clientLink.getRemoteLinkId());
	}
}
