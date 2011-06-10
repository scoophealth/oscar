package org.oscarehr.web;

import java.net.MalformedURLException;
import java.util.List;

import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.MatchingDemographicParameters;
import org.oscarehr.caisi_integrator.ws.MatchingDemographicTransferScore;
import org.oscarehr.util.LoggedInInfo;

public final class DemographicSearchHelper {
	
	public static List<MatchingDemographicTransferScore> getIntegratedSearchResults(MatchingDemographicParameters matchingDemographicParameters) throws MalformedURLException
	{
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		if (!loggedInInfo.currentFacility.isIntegratorEnabled()) return(null);
		
		DemographicWs demographicWs=CaisiIntegratorManager.getDemographicWs();
		demographicWs.getMatchingDemographics(matchingDemographicParameters);
		
		List<MatchingDemographicTransferScore> integratedMatches = demographicWs.getMatchingDemographics(matchingDemographicParameters);
		return(integratedMatches);
	}
}