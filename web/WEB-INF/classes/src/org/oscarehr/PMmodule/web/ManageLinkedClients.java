package org.oscarehr.PMmodule.web;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.client.CachedDemographic;
import org.oscarehr.caisi_integrator.ws.client.CachedFacility;
import org.oscarehr.caisi_integrator.ws.client.DemographicWs;
import org.oscarehr.caisi_integrator.ws.client.MatchingDemographicParameters;
import org.oscarehr.caisi_integrator.ws.client.MatchingDemographicScore;
import org.oscarehr.common.dao.ClientLinkDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.ClientLink;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.Provider;
import org.oscarehr.hnr.ws.client.MatchingClientParameters;
import org.oscarehr.hnr.ws.client.MatchingClientScore;
import org.oscarehr.util.SpringUtils;

public class ManageLinkedClients {
	public static Logger logger = LogManager.getLogger(ManageLinkedClients.class);
	public static CaisiIntegratorManager caisiIntegratorManager = (CaisiIntegratorManager) SpringUtils.getBean("caisiIntegratorManager");
	public static DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	public static ClientLinkDao clientLinkDao = (ClientLinkDao) SpringUtils.getBean("clientLinkDao");

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
	}

	public static ArrayList<LinkedDemographicHolder> getDemographicsToDisplay(Facility currentFacility, Provider currentProvider, Integer demographicId) {
		// get possible matches
		// get currently linked

		try {
			Demographic demographic = demographicDao.getDemographicById(demographicId);

			// the string key is just used to prevent duplicate entries caused by matched + current links if some one is in both.
			// the string itself is arbitrary so use something like the facility+clientId or something
			HashMap<String, LinkedDemographicHolder> results = new HashMap<String, LinkedDemographicHolder>();

			addPotentialMatches(results, demographic, currentFacility);
			addCurrentLinks(results, demographic, currentFacility);
			addHnrMatches(results, demographic, currentFacility, currentProvider);
			addHnrLinks(results, demographic, currentFacility, currentProvider);

			ArrayList<LinkedDemographicHolder> sortedResult = getSortedResults(results);

			return (sortedResult);
		} catch (Exception e) {
			logger.error(e);
			return (null);
		}
	}

	private static void addHnrLinks(HashMap<String, LinkedDemographicHolder> results, Demographic demographic, Facility currentFacility, Provider currentProvider) throws MalformedURLException {
		List<ClientLink> currentLinks = clientLinkDao.findByFacilityIdClientIdType(currentFacility.getId(), demographic.getDemographicNo(), true, ClientLink.Type.HNR);

		// we're only dealing with 1 hnr entry even if there's multiple because there should
		// only be 1, a minor issue about some of this code not being atomic makes multiple
		// entries theoretically possible though in reality it should never happen.
		if (currentLinks.size() >0 ) {
			org.oscarehr.hnr.ws.client.Client hnrClient = caisiIntegratorManager.getHnrClient(currentFacility, currentProvider, currentLinks.get(0).getRemoteLinkId());

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

	private static void addHnrMatches(HashMap<String, LinkedDemographicHolder> results, Demographic demographic, Facility currentFacility, Provider currentProvider) throws MalformedURLException, DatatypeConfigurationException {
		MatchingClientParameters matchingClientParameters = getMatchingHnrClientParameters(demographic);
		List<MatchingClientScore> potentialMatches = caisiIntegratorManager.searchHnrForMatchingClients(currentFacility, currentProvider, matchingClientParameters);

		for (MatchingClientScore matchingClientScore : potentialMatches) {
			String tempKey = ClientLink.Type.HNR.name() + '.' + matchingClientScore.getClient().getLinkingId();
			LinkedDemographicHolder integratorLinkedDemographicHolder = results.get(tempKey);

			if (integratorLinkedDemographicHolder == null) {
				integratorLinkedDemographicHolder = new LinkedDemographicHolder();
				results.put(tempKey, integratorLinkedDemographicHolder);
			}

			integratorLinkedDemographicHolder.linked = false;
			integratorLinkedDemographicHolder.matchingScore = matchingClientScore.getScore();
			org.oscarehr.hnr.ws.client.Client hnrClient = matchingClientScore.getClient();
			copyHnrClientDataToMatchingScorePlaceholder(integratorLinkedDemographicHolder, hnrClient);
		}
	}

	private static void copyHnrClientDataToMatchingScorePlaceholder(LinkedDemographicHolder integratorLinkedDemographicHolder, org.oscarehr.hnr.ws.client.Client hnrClient) {
		// copy the data to holder entry
		if (hnrClient.getBirthDate() != null) integratorLinkedDemographicHolder.birthDate = DateFormatUtils.ISO_DATE_FORMAT.format(hnrClient.getBirthDate().toGregorianCalendar());
		integratorLinkedDemographicHolder.firstName = StringUtils.trimToEmpty(hnrClient.getFirstName());
		if (hnrClient.getGender() != null) integratorLinkedDemographicHolder.gender = hnrClient.getGender().name();
		integratorLinkedDemographicHolder.hin = StringUtils.trimToEmpty(hnrClient.getHin());
		integratorLinkedDemographicHolder.lastName = StringUtils.trimToEmpty(hnrClient.getLastName());
		integratorLinkedDemographicHolder.linkDestination = ClientLink.Type.HNR.name();
		integratorLinkedDemographicHolder.remoteLinkId = hnrClient.getLinkingId();
	}

	private static MatchingClientParameters getMatchingHnrClientParameters(Demographic demographic) throws DatatypeConfigurationException {
		MatchingClientParameters parameters = new MatchingClientParameters();
		parameters.setMaxEntriesToReturn(20);
		parameters.setMinScore(5);

		String temp = StringUtils.trimToNull(demographic.getFirstName());
		parameters.setFirstName(temp);

		temp = StringUtils.trimToNull(demographic.getLastName());
		parameters.setLastName(temp);

		temp = StringUtils.trimToNull(demographic.getHin());
		parameters.setHin(temp);

		GregorianCalendar cal=demographic.getBirthDay();
		if (cal!=null)
		{
			XMLGregorianCalendar soapCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
			parameters.setBirthDate(soapCal);
		}
		
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

	private static void addCurrentLinks(HashMap<String, LinkedDemographicHolder> results, Demographic demographic, Facility currentFacility) throws MalformedURLException {
		DemographicWs demographicWs = caisiIntegratorManager.getDemographicWs(currentFacility.getId());
		List<CachedDemographic> directLinksTemp = demographicWs.getDirectlyLinkedCachedDemographicsByDemographicId(demographic.getDemographicNo());

		for (CachedDemographic cachedDemographic : directLinksTemp) {
			String tempKey = ClientLink.Type.OSCAR_CAISI.name() + '.' + cachedDemographic.getFacilityIdIntegerCompositePk().getIntegratorFacilityId() + '.' + cachedDemographic.getFacilityIdIntegerCompositePk().getCaisiItemId();

			LinkedDemographicHolder integratorLinkedDemographicHolder = results.get(tempKey);

			if (integratorLinkedDemographicHolder == null) {
				integratorLinkedDemographicHolder = new LinkedDemographicHolder();
				results.put(tempKey, integratorLinkedDemographicHolder);
			}

			integratorLinkedDemographicHolder.linked = true;
			copyCachedDemographicDataToScorePlaceholder(currentFacility, cachedDemographic, integratorLinkedDemographicHolder);
		}
	}

	private static void addPotentialMatches(HashMap<String, LinkedDemographicHolder> results, Demographic demographic, Facility currentFacility) throws DatatypeConfigurationException, MalformedURLException {
		MatchingDemographicParameters parameters = getMatchingDemographicParameters(demographic);
		DemographicWs demographicWs = caisiIntegratorManager.getDemographicWs(currentFacility.getId());
		List<MatchingDemographicScore> potentialMatches = demographicWs.getMatchingDemographics(parameters);

		if (potentialMatches == null) return;

		for (MatchingDemographicScore matchingDemographicScore : potentialMatches) {
			String tempKey = ClientLink.Type.OSCAR_CAISI.name() + '.' + matchingDemographicScore.getCachedDemographic().getFacilityIdIntegerCompositePk().getIntegratorFacilityId() + '.'
			        + matchingDemographicScore.getCachedDemographic().getFacilityIdIntegerCompositePk().getCaisiItemId();
			LinkedDemographicHolder integratorLinkedDemographicHolder = results.get(tempKey);

			if (integratorLinkedDemographicHolder == null) {
				integratorLinkedDemographicHolder = new LinkedDemographicHolder();
				results.put(tempKey, integratorLinkedDemographicHolder);
			}

			integratorLinkedDemographicHolder.matchingScore = matchingDemographicScore.getScore();
			integratorLinkedDemographicHolder.linked = false;
			copyCachedDemographicDataToScorePlaceholder(currentFacility, matchingDemographicScore.getCachedDemographic(), integratorLinkedDemographicHolder);
		}
	}

	private static void copyCachedDemographicDataToScorePlaceholder(Facility currentFacility, CachedDemographic cachedDemographic, LinkedDemographicHolder integratorLinkedDemographicHolder) throws MalformedURLException {
		// copy the data to holder entry
		if (cachedDemographic.getBirthDate() != null) integratorLinkedDemographicHolder.birthDate = DateFormatUtils.ISO_DATE_FORMAT.format(cachedDemographic.getBirthDate().toGregorianCalendar());
		integratorLinkedDemographicHolder.firstName = StringUtils.trimToEmpty(cachedDemographic.getFirstName());
		integratorLinkedDemographicHolder.gender = StringUtils.trimToEmpty(cachedDemographic.getGender());
		integratorLinkedDemographicHolder.hin = StringUtils.trimToEmpty(cachedDemographic.getHin());
		integratorLinkedDemographicHolder.lastName = StringUtils.trimToEmpty(cachedDemographic.getLastName());

		CachedFacility tempFacility = caisiIntegratorManager.getRemoteFacility(currentFacility.getId(), cachedDemographic.getFacilityIdIntegerCompositePk().getIntegratorFacilityId());
		integratorLinkedDemographicHolder.linkDestination = ClientLink.Type.OSCAR_CAISI.name() + '.' + tempFacility.getIntegratorFacilityId();
		integratorLinkedDemographicHolder.remoteLinkId = cachedDemographic.getFacilityIdIntegerCompositePk().getCaisiItemId();
	}

	private static MatchingDemographicParameters getMatchingDemographicParameters(Demographic demographic) throws DatatypeConfigurationException {
		MatchingDemographicParameters parameters = new MatchingDemographicParameters();
		parameters.setMaxEntriesToReturn(20);
		parameters.setMinScore(5);

		String temp = StringUtils.trimToNull(demographic.getFirstName());
		parameters.setFirstName(temp);

		temp = StringUtils.trimToNull(demographic.getLastName());
		parameters.setLastName(temp);

		temp = StringUtils.trimToNull(demographic.getHin());
		parameters.setHin(temp);

		GregorianCalendar cal=demographic.getBirthDay();
		if (cal!=null)
		{
			XMLGregorianCalendar soapCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
			parameters.setBirthDate(soapCal);
		}
		
		return parameters;
	}

}
