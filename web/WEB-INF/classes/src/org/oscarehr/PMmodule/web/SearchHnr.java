package org.oscarehr.PMmodule.web;

import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.Provider;
import org.oscarehr.hnr.ws.client.HnrWs;
import org.oscarehr.hnr.ws.client.MatchingClientParameters;
import org.oscarehr.hnr.ws.client.MatchingClientScore;
import org.oscarehr.util.SpringUtils;

public class SearchHnr {
	public static Logger logger = LogManager.getLogger(SearchHnr.class);
	public static CaisiIntegratorManager caisiIntegratorManager = (CaisiIntegratorManager) SpringUtils.getBean("caisiIntegratorManager");
	public static DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");

	public static List<MatchingClientScore> getSearchResults(Facility facility, Provider provider, Integer demographicId) throws DatatypeConfigurationException {
		try {
	        Demographic demographic = demographicDao.getDemographicById(demographicId);
	        HnrWs hnrWs = caisiIntegratorManager.getHnrWs(facility.getId());

	        MatchingClientParameters parameters = getMatchingClientParameters(demographic);
	        List<MatchingClientScore> potentialMatches = hnrWs.getMatchingClients2("facility="+facility.getName()+", provider="+provider.getFormattedName(), parameters);

	        return(potentialMatches);
        } catch (Exception e) {
        	logger.error("Unexpected error", e);
        }
        
        return(null);
	}

	private static MatchingClientParameters getMatchingClientParameters(Demographic demographic) throws DatatypeConfigurationException {
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
}
