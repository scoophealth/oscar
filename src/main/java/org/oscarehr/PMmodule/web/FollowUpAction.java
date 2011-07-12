package org.oscarehr.PMmodule.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.DemographicTransfer;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.ProviderCommunicationTransfer;
import org.oscarehr.caisi_integrator.ws.ProviderWs;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.XmlUtils;
import org.w3c.dom.Document;

/**
 * No one should ever use this class except during conformance testing.
 */
public class FollowUpAction extends DispatchAction {
	private static Logger logger=MiscUtils.getLogger();
	
	@Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		try {
			String demographicId = request.getParameter("demographicId");
			String remoteProviderNo = request.getParameter("remoteProviderId");
			String remoteFacilityId = request.getParameter("remoteFacilityId");
			String note = request.getParameter("followUpNote");

			logger.debug("Create follow up : "+demographicId+", "+remoteProviderNo+", "+remoteFacilityId+", "+note);
			
			DemographicWs demographicWs=CaisiIntegratorManager.getDemographicWs();
			List<DemographicTransfer> linkedDemographics=demographicWs.getDirectlyLinkedDemographicsByDemographicId(Integer.parseInt(demographicId));
			DemographicTransfer remoteDemographic=linkedDemographics.get(0);

			logger.debug("Create follow up : remote demographicId="+remoteDemographic.getCaisiDemographicId());
			
			LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();

			ProviderCommunicationTransfer followUp = new ProviderCommunicationTransfer();
			followUp.setDestinationIntegratorFacilityId(Integer.parseInt(remoteFacilityId));
			followUp.setDestinationProviderId(remoteProviderNo);
			followUp.setSourceProviderId(loggedInInfo.loggedInProvider.getProviderNo());
			followUp.setType("FOLLOWUP");

			Document doc = XmlUtils.newDocument("followup");
			XmlUtils.appendChildToRoot(doc, "destinationDemographicId", ""+remoteDemographic.getCaisiDemographicId());
			XmlUtils.appendChildToRoot(doc, "note", note);
			byte[] data = XmlUtils.toBytes(doc,false);
			followUp.setData(data);
		
			ProviderWs providerWs = CaisiIntegratorManager.getProviderWs();
			providerWs.addProviderComunication(followUp);
			
		} catch (Exception e) {
			MiscUtils.getLogger().error("Can't push follow up request.", e);
		}
		
		return mapping.findForward("success");
	}
	
}