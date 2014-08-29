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

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.ConnectException_Exception;
import org.oscarehr.caisi_integrator.ws.DemographicTransfer;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.MatchingDemographicParameters;
import org.oscarehr.caisi_integrator.ws.MatchingDemographicTransferScore;
import org.oscarehr.casemgmt.dao.ClientImageDAO;
import org.oscarehr.casemgmt.model.ClientImage;
import org.oscarehr.common.dao.ClientLinkDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.ClientLink;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Facility;
import org.oscarehr.hnr.ws.MatchingClientParameters;
import org.oscarehr.hnr.ws.MatchingClientScore;
import org.oscarehr.ui.servlet.ImageRenderingServlet;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ManageLinkedClients {
	public static Logger logger = MiscUtils.getLogger();
	public static DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	public static ClientLinkDao clientLinkDao = (ClientLinkDao) SpringUtils.getBean("clientLinkDao");
	public static ClientImageDAO clientImageDAO=(ClientImageDAO)SpringUtils.getBean("clientImageDAO");

	public static class LinkedDemographicHolder {
		public int matchingScore = 0;
		public boolean linked = false;
		public String linkDestination = "";
		public Integer remoteLinkId = null;
		public String firstName = "";
		public String lastName = "";
		public String gender = "";
		public String birthDate = "";
		public String hin = "";
		public String hinType = "";
		public String imageUrl=ClientImage.imageMissingPlaceholderUrl;
		/** null means it's changeable */
		public String nonChangeableLinkStatus=null;
	}

	public static ArrayList<LinkedDemographicHolder> getDemographicsToDisplay(LoggedInInfo loggedInInfo, Facility facility, Integer demographicId) {
		// get possible matches
		// get currently linked

		try {
			Demographic demographic = demographicDao.getDemographicById(demographicId);

			// the string key is just used to prevent duplicate entries caused by matched + current links if some one is in both.
			// the string itself is arbitrary so use something like the facility+clientId or something
			HashMap<String, LinkedDemographicHolder> results = new HashMap<String, LinkedDemographicHolder>();

			addPotentialMatches(loggedInInfo, facility, results, demographic);
			addCurrentLinks(loggedInInfo, facility, results, demographic);
			addHnrMatches(loggedInInfo, facility, results, demographic);
			addHnrLinks(loggedInInfo, facility, results, demographic);
			
			ArrayList<LinkedDemographicHolder> sortedResult = getSortedResults(results);

			return (sortedResult);
		} catch (Exception e) {
			logger.error("unexpected error", e);
			return (null);
		}
	}

	private static void addHnrLinks(LoggedInInfo loggedInInfo, Facility facility, HashMap<String, LinkedDemographicHolder> results, Demographic demographic) throws MalformedURLException, ConnectException_Exception {		
		List<ClientLink> currentLinks = clientLinkDao.findByFacilityIdClientIdType(facility.getId(), demographic.getDemographicNo(), true, ClientLink.Type.HNR);
		
		// we're only dealing with 1 hnr entry even if there's multiple because there should
		// only be 1, a minor issue about some of this code not being atomic makes multiple
		// entries theoretically possible though in reality it should never happen.
		if (currentLinks.size() >0 ) {
			org.oscarehr.hnr.ws.Client hnrClient = CaisiIntegratorManager.getHnrClient(loggedInInfo, facility, currentLinks.get(0).getRemoteLinkId());

			// can be null if client revoked consent or locked data, link still exists but no records are returned.
			if (hnrClient != null) {
				String tempKey = ClientLink.Type.HNR.name() + '.' + hnrClient.getLinkingId();
				LinkedDemographicHolder integratorLinkedDemographicHolder = results.get(tempKey);

				if (integratorLinkedDemographicHolder == null) {
					integratorLinkedDemographicHolder = new LinkedDemographicHolder();
					results.put(tempKey, integratorLinkedDemographicHolder);
				}

				integratorLinkedDemographicHolder.linked = true;
				copyHnrClientDataToMatchingScorePlaceholder(integratorLinkedDemographicHolder, hnrClient);
			}
		}
	}

	private static void addHnrMatches(LoggedInInfo loggedInInfo, Facility facility, HashMap<String, LinkedDemographicHolder> results, Demographic demographic) throws MalformedURLException {
		try {
	        MatchingClientParameters matchingClientParameters = getMatchingHnrClientParameters(demographic);
	        List<MatchingClientScore> potentialMatches = CaisiIntegratorManager.searchHnrForMatchingClients(loggedInInfo, facility, matchingClientParameters);
	        
	        for (MatchingClientScore matchingClientScore : potentialMatches) {
	        	String tempKey = ClientLink.Type.HNR.name() + '.' + matchingClientScore.getClient().getLinkingId();
	        	LinkedDemographicHolder integratorLinkedDemographicHolder = results.get(tempKey);

	        	if (integratorLinkedDemographicHolder == null) {
	        		integratorLinkedDemographicHolder = new LinkedDemographicHolder();
	        		results.put(tempKey, integratorLinkedDemographicHolder);
	        	}

	        	integratorLinkedDemographicHolder.linked = false;
	        	integratorLinkedDemographicHolder.matchingScore = matchingClientScore.getScore();
	        	org.oscarehr.hnr.ws.Client hnrClient = matchingClientScore.getClient();
	        	copyHnrClientDataToMatchingScorePlaceholder(integratorLinkedDemographicHolder, hnrClient);
	        }
        } catch (ConnectException_Exception e) {
	        logger.error("Connection exception to HNR", e);
        }
	}

	private static void copyHnrClientDataToMatchingScorePlaceholder(LinkedDemographicHolder integratorLinkedDemographicHolder, org.oscarehr.hnr.ws.Client hnrClient) {
		// copy the data to holder entry
		if (hnrClient.getBirthDate() != null) integratorLinkedDemographicHolder.birthDate = DateFormatUtils.ISO_DATE_FORMAT.format(hnrClient.getBirthDate());
		integratorLinkedDemographicHolder.firstName = StringUtils.trimToEmpty(hnrClient.getFirstName());
		if (hnrClient.getGender() != null) integratorLinkedDemographicHolder.gender = hnrClient.getGender().name();
		integratorLinkedDemographicHolder.hin = StringUtils.trimToEmpty(hnrClient.getHin());
		integratorLinkedDemographicHolder.hinType = StringUtils.trimToEmpty(hnrClient.getHinType());
		integratorLinkedDemographicHolder.lastName = StringUtils.trimToEmpty(hnrClient.getLastName());
		integratorLinkedDemographicHolder.linkDestination = ClientLink.Type.HNR.name();
		integratorLinkedDemographicHolder.remoteLinkId = hnrClient.getLinkingId();
				
		if (hnrClient.getImage()!=null) integratorLinkedDemographicHolder.imageUrl="/imageRenderingServlet?source="+ImageRenderingServlet.Source.hnr_client.name()+"&linkingId=" + hnrClient.getLinkingId();
	}

	private static MatchingClientParameters getMatchingHnrClientParameters(Demographic demographic) {
		MatchingClientParameters parameters = new MatchingClientParameters();
		parameters.setMaxEntriesToReturn(20);
		parameters.setMinScore(5);

		String temp = StringUtils.trimToNull(demographic.getFirstName());
		parameters.setFirstName(temp);

		temp = StringUtils.trimToNull(demographic.getLastName());
		parameters.setLastName(temp);

		temp = StringUtils.trimToNull(demographic.getHin());
		parameters.setHin(temp);

		if (demographic.getBirthDay()!=null) parameters.setBirthDate(demographic.getBirthDay());
		
		return parameters;
	}

	public static class ScoreSorter implements Comparator<LinkedDemographicHolder> {
		// commented out until we stop supporting jdk 1.5 which has a compiler bug for inner class/annotations.
		// @Override
		public int compare(LinkedDemographicHolder o1, LinkedDemographicHolder o2) {
			return (o2.matchingScore - o1.matchingScore);
		}
	}

	private static ArrayList<LinkedDemographicHolder> getSortedResults(HashMap<String, LinkedDemographicHolder> results) {
		ArrayList<LinkedDemographicHolder> sortedResults = new ArrayList<LinkedDemographicHolder>();
		sortedResults.addAll(results.values());
		Collections.sort(sortedResults, new ScoreSorter());
		return (sortedResults);
	}

	private static void addCurrentLinks(LoggedInInfo loggedInInfo, Facility facility, HashMap<String, LinkedDemographicHolder> results, Demographic demographic) throws MalformedURLException {
		DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, facility);
		List<DemographicTransfer> directLinksTemp = demographicWs.getDirectlyLinkedDemographicsByDemographicId(demographic.getDemographicNo());
		List<DemographicTransfer> linksTemp = demographicWs.getLinkedDemographicsByDemographicId(demographic.getDemographicNo());

		for (DemographicTransfer demographicTransfer : linksTemp) {
			String tempKey = ClientLink.Type.OSCAR_CAISI.name() + '.' + demographicTransfer.getIntegratorFacilityId() + '.' + demographicTransfer.getCaisiDemographicId();

			LinkedDemographicHolder integratorLinkedDemographicHolder = results.get(tempKey);

			if (integratorLinkedDemographicHolder == null) {
				integratorLinkedDemographicHolder = new LinkedDemographicHolder();
				results.put(tempKey, integratorLinkedDemographicHolder);
			}

			integratorLinkedDemographicHolder.linked = true;
			
			DemographicTransfer directLink=exists(directLinksTemp, demographicTransfer);
			if (directLink!=null) // i.e. directly linked
			{
				// if the Health number is the same then no it's not changeable
				if (demographic.getHin()!=null && demographic.getHcType()!=null && demographic.getHin().equals(directLink.getHin()) && demographic.getHcType().equals(directLink.getHinType())) 
				{
					integratorLinkedDemographicHolder.nonChangeableLinkStatus="Implicitly Linked";
				}
			}
			else // directLink==null i.e. transitively linked
			{
				integratorLinkedDemographicHolder.nonChangeableLinkStatus="Tansitively Linked";
			}
						
			copyDemographicTransferDataToScorePlaceholder(loggedInInfo, facility, demographicTransfer, integratorLinkedDemographicHolder);
		}
	}

	private static DemographicTransfer exists(List<DemographicTransfer> directLinksTemp, DemographicTransfer demographicTransfer)
	{
		for (DemographicTransfer directLink : directLinksTemp)
		{
			if (demographicTransfer.getIntegratorFacilityId().equals(directLink.getIntegratorFacilityId()) && demographicTransfer.getCaisiDemographicId()==directLink.getCaisiDemographicId())
			{
				return(directLink);
			}
		}
		
		return(null);
	}
	
	private static void addPotentialMatches(LoggedInInfo loggedInInfo, Facility facility,HashMap<String, LinkedDemographicHolder> results, Demographic demographic) throws MalformedURLException {
		MatchingDemographicParameters parameters = getMatchingDemographicParameters(demographic);
		DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, facility);
		List<MatchingDemographicTransferScore> potentialMatches = demographicWs.getMatchingDemographics(parameters);
		
		if (potentialMatches == null) return;

		for (MatchingDemographicTransferScore matchingDemographicScore : potentialMatches) {
			String tempKey = ClientLink.Type.OSCAR_CAISI.name() + '.' + matchingDemographicScore.getDemographicTransfer().getIntegratorFacilityId() + '.'
			        + matchingDemographicScore.getDemographicTransfer().getCaisiDemographicId();
			LinkedDemographicHolder integratorLinkedDemographicHolder = results.get(tempKey);

			if (integratorLinkedDemographicHolder == null) {
				integratorLinkedDemographicHolder = new LinkedDemographicHolder();
				results.put(tempKey, integratorLinkedDemographicHolder);
			}

			integratorLinkedDemographicHolder.matchingScore = matchingDemographicScore.getScore();
			integratorLinkedDemographicHolder.linked = false;
			copyDemographicTransferDataToScorePlaceholder(loggedInInfo, facility, matchingDemographicScore.getDemographicTransfer(), integratorLinkedDemographicHolder);
		}
	}

	private static void copyDemographicTransferDataToScorePlaceholder(LoggedInInfo loggedInInfo, Facility facility,DemographicTransfer demographicTransfer, LinkedDemographicHolder integratorLinkedDemographicHolder) throws MalformedURLException {
		// copy the data to holder entry
		if (demographicTransfer.getBirthDate() != null) integratorLinkedDemographicHolder.birthDate = DateFormatUtils.ISO_DATE_FORMAT.format(demographicTransfer.getBirthDate());
		integratorLinkedDemographicHolder.firstName = StringUtils.trimToEmpty(demographicTransfer.getFirstName());
		integratorLinkedDemographicHolder.gender = "";
		if (demographicTransfer.getGender()!=null) integratorLinkedDemographicHolder.gender=demographicTransfer.getGender().name();
		integratorLinkedDemographicHolder.hin = StringUtils.trimToEmpty(demographicTransfer.getHin());
		integratorLinkedDemographicHolder.hinType = StringUtils.trimToEmpty(demographicTransfer.getHinType());
		integratorLinkedDemographicHolder.lastName = StringUtils.trimToEmpty(demographicTransfer.getLastName());

		CachedFacility tempFacility = CaisiIntegratorManager.getRemoteFacility(loggedInInfo, facility,demographicTransfer.getIntegratorFacilityId());
		integratorLinkedDemographicHolder.linkDestination = ClientLink.Type.OSCAR_CAISI.name() + '.' + tempFacility.getIntegratorFacilityId();
		integratorLinkedDemographicHolder.remoteLinkId = demographicTransfer.getCaisiDemographicId();
		
		if (demographicTransfer.getPhoto()!=null) integratorLinkedDemographicHolder.imageUrl="/imageRenderingServlet?source="+ImageRenderingServlet.Source.integrator_client.name()+"&integratorFacilityId=" + demographicTransfer.getIntegratorFacilityId()+"&caisiDemographicId=" + demographicTransfer.getCaisiDemographicId();
	}

	private static MatchingDemographicParameters getMatchingDemographicParameters(Demographic demographic) {
		MatchingDemographicParameters parameters = new MatchingDemographicParameters();
		parameters.setMaxEntriesToReturn(20);
		parameters.setMinScore(5);

		String temp = StringUtils.trimToNull(demographic.getFirstName());
		parameters.setFirstName(temp);

		temp = StringUtils.trimToNull(demographic.getLastName());
		parameters.setLastName(temp);

		temp = StringUtils.trimToNull(demographic.getHin());
		parameters.setHin(temp);

		if (demographic.getBirthDay()!=null) parameters.setBirthDate(demographic.getBirthDay());
		
		return parameters;
	}

	public static String getLocalImageUrl(int currentDemographicId)
	{
		ClientImage clientImage=clientImageDAO.getClientImage(currentDemographicId);

		if (clientImage==null)
		{
			return(ClientImage.imageMissingPlaceholderUrl);
		}
		else
		{
			String imageUrl="/imageRenderingServlet?source="+ImageRenderingServlet.Source.local_client.name()+"&clientId="+currentDemographicId;
			return(imageUrl);
		}	
	}	
}
