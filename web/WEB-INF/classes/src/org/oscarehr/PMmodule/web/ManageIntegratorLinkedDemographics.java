package org.oscarehr.PMmodule.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.caisi_integrator.ws.client.CachedDemographic;
import org.oscarehr.caisi_integrator.ws.client.DemographicInfoWs;
import org.oscarehr.caisi_integrator.ws.client.MatchingDemographicInfoParameters;
import org.oscarehr.caisi_integrator.ws.client.MatchingDemographicInfoScore;
import org.oscarehr.common.model.FacilityDemographicPrimaryKey;
import org.oscarehr.util.SpringUtils;

public class ManageIntegratorLinkedDemographics {
	public static Logger logger = LogManager.getLogger(ManageIntegratorLinkedDemographics.class);
	public static CaisiIntegratorManager caisiIntegratorManager = (CaisiIntegratorManager) SpringUtils.getBean("caisiIntegratorManager");
	public static DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");

	public static class IntegratorLinkedDemographicHolder {
		private CachedDemographic cachedDemographic = null;
		private int matchingScore = 0;
		private boolean linked = false;
		private boolean directlyLinked=false; 

		public CachedDemographic getCachedDemographic() {
			return cachedDemographic;
		}

		public void setCachedDemographic(CachedDemographic cachedDemographic) {
			this.cachedDemographic = cachedDemographic;
		}

		public int getMatchingScore() {
			return matchingScore;
		}

		public void setMatchingScore(int matchingScore) {
			this.matchingScore = matchingScore;
		}

		public boolean isLinked() {
			return linked;
		}

		public void setLinked(boolean linked) {
			this.linked = linked;
		}

		public boolean isDirectlyLinked() {
			return directlyLinked;
		}

		public void setDirectlyLinked(boolean directlyLinked) {
			this.directlyLinked = directlyLinked;
		}

	}

	public static ArrayList<IntegratorLinkedDemographicHolder> getDemographicsToDisplay(Integer facilityId, Integer demographicId) {
		// get possible matches
		// get currently linked

		try {
			Demographic demographic = demographicDao.getDemographicById(demographicId);
			DemographicInfoWs demographicInfoWs = caisiIntegratorManager.getDemographicInfoWs(facilityId);

			HashMap<FacilityDemographicPrimaryKey, IntegratorLinkedDemographicHolder> results = new HashMap<FacilityDemographicPrimaryKey, IntegratorLinkedDemographicHolder>();

			addPotentialMatches(results, demographic, demographicInfoWs);
			addCurrentLinks(results, demographic, demographicInfoWs);

			ArrayList<IntegratorLinkedDemographicHolder> sortedResult = getSortedResults(results);

			return (sortedResult);
		}
		catch (Exception e) {
			logger.error(e);
			return (null);
		}
	}

	public static class ScoreSorter implements Comparator<IntegratorLinkedDemographicHolder> {
		// commented out until we stop supporting jdk 1.5 which has a compiler bug for inner class/annotations.
		// @Override
		public int compare(IntegratorLinkedDemographicHolder o1, IntegratorLinkedDemographicHolder o2) {
			return (o1.getMatchingScore() - o2.getMatchingScore());
		}
	}

	private static ArrayList<IntegratorLinkedDemographicHolder> getSortedResults(HashMap<FacilityDemographicPrimaryKey, IntegratorLinkedDemographicHolder> results) {
		ArrayList<IntegratorLinkedDemographicHolder> sortedResults = new ArrayList<IntegratorLinkedDemographicHolder>();
		sortedResults.addAll(results.values());
		Collections.sort(sortedResults, new ScoreSorter());
		return (sortedResults);
	}

	private static void addCurrentLinks(HashMap<FacilityDemographicPrimaryKey, IntegratorLinkedDemographicHolder> results, Demographic demographic, DemographicInfoWs demographicInfoWs) {
		List<CachedDemographic> currentLinks = demographicInfoWs.getAllLinkedCachedDemographicByDemographicId(demographic.getDemographicNo());

		List<CachedDemographic> directLinksTemp = demographicInfoWs.getDirectlyLinkedCachedDemographicByDemographicId(demographic.getDemographicNo());
		HashSet<FacilityDemographicPrimaryKey> directLinks=new HashSet<FacilityDemographicPrimaryKey>();
		for(CachedDemographic cachedDemographic : directLinksTemp) directLinks.add(new FacilityDemographicPrimaryKey(cachedDemographic.getFacilityIdIntegerCompositePk()));		
		
		if (currentLinks == null) return;

		for (CachedDemographic cachedDemographic : currentLinks) {
			FacilityDemographicPrimaryKey facilityDemographicPrimaryKey = new FacilityDemographicPrimaryKey(cachedDemographic.getFacilityIdIntegerCompositePk());
			IntegratorLinkedDemographicHolder integratorLinkedDemographicHolder = results.get(facilityDemographicPrimaryKey);

			if (integratorLinkedDemographicHolder == null) {
				integratorLinkedDemographicHolder = new IntegratorLinkedDemographicHolder();
				results.put(facilityDemographicPrimaryKey, integratorLinkedDemographicHolder);
			}

			integratorLinkedDemographicHolder.setCachedDemographic(cachedDemographic);
			integratorLinkedDemographicHolder.setLinked(true);
			
			if (directLinks.contains(facilityDemographicPrimaryKey)) integratorLinkedDemographicHolder.setDirectlyLinked(true);
		}
	}

	private static void addPotentialMatches(HashMap<FacilityDemographicPrimaryKey, IntegratorLinkedDemographicHolder> results, Demographic demographic, DemographicInfoWs demographicInfoWs) throws DatatypeConfigurationException {
		MatchingDemographicInfoParameters parameters = getMatchingDemographicInfoParameters(demographic);
		List<MatchingDemographicInfoScore> potentialMatches = demographicInfoWs.getMatchingDemographicInfos(parameters);

		if (potentialMatches == null) return;

		for (MatchingDemographicInfoScore matchingDemographicInfoScore : potentialMatches) {
			FacilityDemographicPrimaryKey facilityDemographicPrimaryKey = new FacilityDemographicPrimaryKey(matchingDemographicInfoScore.getCachedDemographic()
					.getFacilityIdIntegerCompositePk());
			IntegratorLinkedDemographicHolder integratorLinkedDemographicHolder = results.get(facilityDemographicPrimaryKey);

			if (integratorLinkedDemographicHolder == null) {
				integratorLinkedDemographicHolder = new IntegratorLinkedDemographicHolder();
				results.put(facilityDemographicPrimaryKey, integratorLinkedDemographicHolder);
			}

			integratorLinkedDemographicHolder.setCachedDemographic(matchingDemographicInfoScore.getCachedDemographic());
			integratorLinkedDemographicHolder.setMatchingScore(matchingDemographicInfoScore.getScore());
		}
	}

	private static MatchingDemographicInfoParameters getMatchingDemographicInfoParameters(Demographic demographic) throws DatatypeConfigurationException {
		MatchingDemographicInfoParameters parameters = new MatchingDemographicInfoParameters();
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

	public static void saveLinkedClients(Integer facilityId, Integer demographicId, HashSet<FacilityDemographicPrimaryKey> linkedIds) {
		try {
//			DemographicInfoWs demographicInfoWs = caisiIntegratorManager.getDemographicInfoWs(facilityId);
//			List<CachedDemographic> tempLinks = demographicInfoWs.getLinkedCachedDemographicByDemographicId(demographicId);
//			HashSet<FacilityDemographicPrimaryKey> currentLinks=new HashSet<FacilityDemographicPrimaryKey>();
//			
//			// check for removals and populate a hashSet (for later use)
//			for (CachedDemographic cachedDemographic : tempLinks)
//			{
//				FacilityDemographicPrimaryKey pk=new FacilityDemographicPrimaryKey(cachedDemographic.getFacilityIdIntegerCompositePk());
//				currentLinks.add(pk);
//				
//				if (!linkedIds.contains(pk))
//				{
//					demographicInfoWs.unLinkDemographics(arg0, arg1, arg2, arg3)
//				}
//			}
			
		}
		catch (Exception e) {
			logger.error(e);
		}
	}
}
