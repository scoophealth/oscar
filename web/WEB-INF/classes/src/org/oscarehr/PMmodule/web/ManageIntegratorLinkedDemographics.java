package org.oscarehr.PMmodule.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.caisi.dao.DemographicDao;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.caisi_integrator.ws.client.CachedDemographic;
import org.oscarehr.caisi_integrator.ws.client.DemographicInfoWs;
import org.oscarehr.caisi_integrator.ws.client.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.ws.client.MatchingDemographicInfoParameters;
import org.oscarehr.caisi_integrator.ws.client.MatchingDemographicInfoScore;
import org.oscarehr.util.SpringUtils;

public class ManageIntegratorLinkedDemographics {
	public static Logger logger = LogManager.getLogger(ManageIntegratorLinkedDemographics.class);
	public static CaisiIntegratorManager caisiIntegratorManager = (CaisiIntegratorManager) SpringUtils.getBean("caisiIntegratorManager");
	public static DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");

	public static class IntegratorLinkedDemographicHolder {
		private CachedDemographic cachedDemographic = null;
		private int matchingScore = 0;
		private boolean linked = false;

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

	}

	public static ArrayList<IntegratorLinkedDemographicHolder> getDemographicsToDisplay(Integer facilityId, Integer demographicId) {
		// get possible matches
		// get currently linked

		try {
			Demographic demographic = demographicDao.getDemographicById(demographicId);
			DemographicInfoWs demographicInfoWs = caisiIntegratorManager.getDemographicInfoWs(facilityId);

			HashMap<FacilityIdIntegerCompositePk,IntegratorLinkedDemographicHolder> results=new HashMap<FacilityIdIntegerCompositePk,IntegratorLinkedDemographicHolder>();
			
			MatchingDemographicInfoParameters parameters = getMatchingDemographicInfoParameters(demographic);
			List<MatchingDemographicInfoScore> potentialMatches = demographicInfoWs.getMatchingDemographicInfos(parameters);
			addPotentialMatches(results, potentialMatches);
			
			List<CachedDemographic> currentLinks = demographicInfoWs.getLinkedCachedDemographicByDemographicId(demographicId);
			addCurrentLinks(results, currentLinks);
			
			ArrayList<IntegratorLinkedDemographicHolder> sortedResult=getSortedResults(results);
			
			return(sortedResult);
		}
		catch (Exception e) {
			logger.error(e);
			return(null);
		}
	}

	public static class ScoreSorter implements Comparator<IntegratorLinkedDemographicHolder>
	{
// commented out until we stop supporting jdk 1.5 which has a compiler bug for inner class/annotations.
//		@Override
		public int compare(IntegratorLinkedDemographicHolder o1, IntegratorLinkedDemographicHolder o2) {
			return(o1.getMatchingScore()-o2.getMatchingScore());
		}
	}
	
	private static ArrayList<IntegratorLinkedDemographicHolder> getSortedResults(HashMap<FacilityIdIntegerCompositePk, IntegratorLinkedDemographicHolder> results) {
		ArrayList<IntegratorLinkedDemographicHolder> sortedResults=new ArrayList<IntegratorLinkedDemographicHolder>();
		sortedResults.addAll(results.values());
		Collections.sort(sortedResults, new ScoreSorter());
		return(sortedResults);
	}

	private static void addCurrentLinks(HashMap<FacilityIdIntegerCompositePk, IntegratorLinkedDemographicHolder> results, List<CachedDemographic> currentLinks) {
		if (currentLinks==null) return;
		
		for (CachedDemographic cachedDemographic : currentLinks)
		{
			IntegratorLinkedDemographicHolder integratorLinkedDemographicHolder=results.get(cachedDemographic.getFacilityIdIntegerCompositePk());

			if (integratorLinkedDemographicHolder==null) 
			{
				integratorLinkedDemographicHolder=new IntegratorLinkedDemographicHolder();
				results.put(cachedDemographic.getFacilityIdIntegerCompositePk(), integratorLinkedDemographicHolder);
			}
			
			integratorLinkedDemographicHolder.setCachedDemographic(cachedDemographic);
			integratorLinkedDemographicHolder.setLinked(true);
		}
	}

	private static void addPotentialMatches(HashMap<FacilityIdIntegerCompositePk, IntegratorLinkedDemographicHolder> results, List<MatchingDemographicInfoScore> potentialMatches) {
		if (potentialMatches==null) return;
		
		for (MatchingDemographicInfoScore matchingDemographicInfoScore : potentialMatches)
		{
			IntegratorLinkedDemographicHolder integratorLinkedDemographicHolder=results.get(matchingDemographicInfoScore.getCachedDemographic().getFacilityIdIntegerCompositePk());

			if (integratorLinkedDemographicHolder==null) 
			{
				integratorLinkedDemographicHolder=new IntegratorLinkedDemographicHolder();
				results.put(matchingDemographicInfoScore.getCachedDemographic().getFacilityIdIntegerCompositePk(), integratorLinkedDemographicHolder);
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
}
