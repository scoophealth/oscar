package org.oscarehr.PMmodule.web;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

	public static class LinkedDemographicHolder {
		public int matchingScore = 0;
		public boolean linked = false;
		public String linkDestination="";
		public Integer remoteLinkId=null;
		public String firstName="";
		public String lastName="";
		public String gender="";
		public String birthDate="";
		public String hin="";
	}

	public static ArrayList<LinkedDemographicHolder> getDemographicsToDisplay(Facility currentFacility, Provider currentProvider, Integer demographicId) {
		// get possible matches
		// get currently linked

		try {
			Demographic demographic = demographicDao.getDemographicById(demographicId);

			// the string key is just used to prevent duplicte entries caused by matched + current links if some one is in both.
			// the string itself is arbitrary so use something like the facility+clientId or something
			HashMap<String, LinkedDemographicHolder> results = new HashMap<String, LinkedDemographicHolder>();

			addPotentialMatches(results, demographic, currentFacility);
//			addCurrentLinks(results, demographic, demographicWs);
			addHnrMatches(results, demographic, currentFacility, currentProvider);
//			addHnrLinks(results, demographic);

			ArrayList<LinkedDemographicHolder> sortedResult = getSortedResults(results);

			return (sortedResult);
		} catch (Exception e) {
			logger.error(e);
			return (null);
		}
	}

	private static void addHnrMatches(HashMap<String, LinkedDemographicHolder> results, Demographic demographic, Facility currentFacility, Provider currentProvider) throws MalformedURLException, DatatypeConfigurationException {
		MatchingClientParameters matchingClientParameters=getMatchingHnrClientParameters(demographic);
		List<MatchingClientScore> potentialMatches=caisiIntegratorManager.searchHnrForMatchingClients(currentFacility, currentProvider, matchingClientParameters);
		
		for (MatchingClientScore matchingClientScore : potentialMatches) {
			String tempKey = "hnr:"+matchingClientScore.getClient().getLinkingId();
			LinkedDemographicHolder integratorLinkedDemographicHolder = results.get(tempKey);

			if (integratorLinkedDemographicHolder == null) {
				integratorLinkedDemographicHolder = new LinkedDemographicHolder();
				results.put(tempKey, integratorLinkedDemographicHolder);
			}

			// copy the data to holder entry
			org.oscarehr.hnr.ws.client.Client hnrClient=matchingClientScore.getClient();
			if (hnrClient.getBirthDate()!=null) integratorLinkedDemographicHolder.birthDate=DateFormatUtils.ISO_DATE_FORMAT.format(hnrClient.getBirthDate().toGregorianCalendar()); 
			integratorLinkedDemographicHolder.firstName=StringUtils.trimToEmpty(hnrClient.getFirstName()); 
			if (hnrClient.getGender()!=null) integratorLinkedDemographicHolder.gender=hnrClient.getGender().name(); 
			integratorLinkedDemographicHolder.hin=StringUtils.trimToEmpty(hnrClient.getHin()); 
			integratorLinkedDemographicHolder.lastName=StringUtils.trimToEmpty(hnrClient.getLastName()); 
			integratorLinkedDemographicHolder.linkDestination=ClientLink.Type.HNR.name(); 
			integratorLinkedDemographicHolder.linked=false; 
			integratorLinkedDemographicHolder.matchingScore=matchingClientScore.getScore(); 
			integratorLinkedDemographicHolder.remoteLinkId=hnrClient.getLinkingId(); 
		}
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

		XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		{
			temp = StringUtils.trimToNull(demographic.getYearOfBirth());
			if (temp != null) cal.setYear(Integer.parseInt(temp));

			temp = StringUtils.trimToNull(demographic.getMonthOfBirth());
			if (temp != null) cal.setMonth(Integer.parseInt(temp));

			temp = StringUtils.trimToNull(demographic.getDateOfBirth());
			if (temp != null) cal.setDay(Integer.parseInt(temp));

			cal.setTime(0, 0, 0);
		}
		parameters.setBirthDate(cal);
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

//	private static void addCurrentLinks(HashMap<FacilityDemographicPrimaryKey, LinkedDemographicHolder> results, Demographic demographic, DemographicWs demographicWs) {
//		List<CachedDemographic> currentLinks = demographicWs.getAllLinkedCachedDemographicsByDemographicId(demographic.getDemographicNo());
//
//		List<CachedDemographic> directLinksTemp = demographicWs.getDirectlyLinkedCachedDemographicsByDemographicId(demographic.getDemographicNo());
//		HashSet<FacilityDemographicPrimaryKey> directLinks = new HashSet<FacilityDemographicPrimaryKey>();
//		for (CachedDemographic cachedDemographic : directLinksTemp)
//			directLinks.add(new FacilityDemographicPrimaryKey(cachedDemographic.getFacilityIdIntegerCompositePk()));
//
//		if (currentLinks == null) return;
//
//		for (CachedDemographic cachedDemographic : currentLinks) {
//			FacilityDemographicPrimaryKey facilityDemographicPrimaryKey = new FacilityDemographicPrimaryKey(cachedDemographic.getFacilityIdIntegerCompositePk());
//			LinkedDemographicHolder integratorLinkedDemographicHolder = results.get(facilityDemographicPrimaryKey);
//
//			if (integratorLinkedDemographicHolder == null) {
//				integratorLinkedDemographicHolder = new LinkedDemographicHolder();
//				results.put(facilityDemographicPrimaryKey, integratorLinkedDemographicHolder);
//			}
//
//			integratorLinkedDemographicHolder.setCachedDemographic(cachedDemographic);
//			integratorLinkedDemographicHolder.setLinked(true);
//
//			if (directLinks.contains(facilityDemographicPrimaryKey)) integratorLinkedDemographicHolder.setDirectlyLinked(true);
//		}
//	}

	private static void addPotentialMatches(HashMap<String, LinkedDemographicHolder> results, Demographic demographic, Facility currentFacility) throws DatatypeConfigurationException, MalformedURLException {
		MatchingDemographicParameters parameters = getMatchingDemographicParameters(demographic);
		DemographicWs demographicWs = caisiIntegratorManager.getDemographicWs(currentFacility.getId());
		List<MatchingDemographicScore> potentialMatches = demographicWs.getMatchingDemographics(parameters);

		if (potentialMatches == null) return;

		for (MatchingDemographicScore matchingDemographicScore : potentialMatches) {
			String tempKey = ""+matchingDemographicScore.getCachedDemographic().getFacilityIdIntegerCompositePk().getIntegratorFacilityId()+':'+matchingDemographicScore.getCachedDemographic().getFacilityIdIntegerCompositePk().getCaisiItemId();
			LinkedDemographicHolder integratorLinkedDemographicHolder = results.get(tempKey);

			if (integratorLinkedDemographicHolder == null) {
				integratorLinkedDemographicHolder = new LinkedDemographicHolder();
				results.put(tempKey, integratorLinkedDemographicHolder);
			}

			// copy the data to holder entry
			CachedDemographic cachedDemographic=matchingDemographicScore.getCachedDemographic();
			if (cachedDemographic.getBirthDate()!=null) integratorLinkedDemographicHolder.birthDate=DateFormatUtils.ISO_DATE_FORMAT.format(cachedDemographic.getBirthDate().toGregorianCalendar()); 
			integratorLinkedDemographicHolder.firstName=StringUtils.trimToEmpty(cachedDemographic.getFirstName()); 
			integratorLinkedDemographicHolder.gender=StringUtils.trimToEmpty(cachedDemographic.getGender()); 
			integratorLinkedDemographicHolder.hin=StringUtils.trimToEmpty(cachedDemographic.getHin()); 
			integratorLinkedDemographicHolder.lastName=StringUtils.trimToEmpty(cachedDemographic.getLastName()); 
			
			CachedFacility tempFacility=caisiIntegratorManager.getRemoteFacility(currentFacility.getId(), cachedDemographic.getFacilityIdIntegerCompositePk().getIntegratorFacilityId());
			integratorLinkedDemographicHolder.linkDestination=ClientLink.Type.OSCAR_CAISI.name()+'.'+tempFacility.getIntegratorFacilityId(); 

			integratorLinkedDemographicHolder.linked=false; 
			integratorLinkedDemographicHolder.matchingScore=matchingDemographicScore.getScore(); 
			integratorLinkedDemographicHolder.remoteLinkId=cachedDemographic.getFacilityIdIntegerCompositePk().getCaisiItemId(); 
		}
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

		XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		{
			temp = StringUtils.trimToNull(demographic.getYearOfBirth());
			if (temp != null) cal.setYear(Integer.parseInt(temp));

			temp = StringUtils.trimToNull(demographic.getMonthOfBirth());
			if (temp != null) cal.setMonth(Integer.parseInt(temp));

			temp = StringUtils.trimToNull(demographic.getDateOfBirth());
			if (temp != null) cal.setDay(Integer.parseInt(temp));

			cal.setTime(0, 0, 0);
		}
		parameters.setBirthDate(cal);
		return parameters;
	}

}
