/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package org.oscarehr.web.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.myoscar_server.ws.AccountWs;
import org.oscarehr.myoscar_server.ws.NoSuchItemException_Exception;
import org.oscarehr.myoscar_server.ws.NotAuthorisedException_Exception;
import org.oscarehr.myoscar_server.ws.PersonTransfer;
import org.oscarehr.myoscar_server.ws.SurveyResultTransfer;
import org.oscarehr.myoscar_server.ws.SurveyTemplateTransfer;
import org.oscarehr.myoscar_server.ws.SurveyWs;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.util.MumpsResultWrapper.Entry;
import org.oscarehr.phr.util.MyOscarServerWebServicesManager;
import org.oscarehr.phr.util.MyOscarUtils;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.xml.sax.SAXException;

import oscar.OscarProperties;

public class SymptomChecklistReportUIBean {

	private static final Logger logger = MiscUtils.getLogger();
	private static final String MYOSCAR_SYMPTOM_CHECKLIST_REPORT_TEMPLATE_NAME_KEY = "MYOSCAR_SYMPTOM_CHECKLIST_REPORT_TEMPLATE_NAME";

	private static DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");

	private static Long symptomChecklistTemplateId = null;

	private static Long getSymptomChecklistTemplateId(PHRAuthentication auth) {
		if (symptomChecklistTemplateId == null) {
			SurveyWs surveyWs = MyOscarServerWebServicesManager.getSurveyWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());

			OscarProperties p = OscarProperties.getInstance();
			String templateName = p.getProperty(MYOSCAR_SYMPTOM_CHECKLIST_REPORT_TEMPLATE_NAME_KEY);

			SurveyTemplateTransfer result = surveyWs.getSurveyTemplateByName(templateName, true);
			if (result == null) {
				logger.warn("No template matching " + MYOSCAR_SYMPTOM_CHECKLIST_REPORT_TEMPLATE_NAME_KEY + "=" + templateName);
			} else {
				symptomChecklistTemplateId = result.getId();
			}
		}

		return (symptomChecklistTemplateId);
	}

	public static ArrayList<MumpsSurveyResultsDisplayObject> getSymptomChecklistReportsResultList(HttpSession session, PHRAuthentication auth, int demographicId, int startIndex, int itemsToReturn)  {
		String sessionKey = "SymptomChecklistReportsResultList:" + demographicId + ":" + startIndex + ":" + itemsToReturn;

		@SuppressWarnings("unchecked")
		ArrayList<MumpsSurveyResultsDisplayObject> results = (ArrayList<MumpsSurveyResultsDisplayObject>) session.getAttribute(sessionKey);

		if (results == null) {
			results = getSymptomChecklistReportsResultListNoCache(auth, demographicId, startIndex, itemsToReturn);
			session.setAttribute(sessionKey, results);
		}

		return (results);
	}

	private static ArrayList<MumpsSurveyResultsDisplayObject> getSymptomChecklistReportsResultListNoCache(PHRAuthentication auth, int demographicId, int startIndex, int itemsToReturn) {

		Demographic demographic = demographicDao.getDemographicById(demographicId);

		SurveyWs surveyWs = MyOscarServerWebServicesManager.getSurveyWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());
		Long myOscarUserId=MyOscarUtils.getMyOscarUserId(auth, demographic.getMyOscarUserName());
		List<SurveyResultTransfer> surveyResultTransfers = surveyWs.getSurveyResults(myOscarUserId, getSymptomChecklistTemplateId(auth), true, true, startIndex, itemsToReturn);

		ArrayList<MumpsSurveyResultsDisplayObject> results = new ArrayList<MumpsSurveyResultsDisplayObject>();

		for (SurveyResultTransfer surveyResultTransfer : surveyResultTransfers) {
			try {
				results.add(new MumpsSurveyResultsDisplayObject(surveyResultTransfer));
			} catch (Exception e) {
				logger.error("Error", e);
			}
		}

		return (results);
	}

	public static SymptomChecklistCompareDisplayObject getCompareDisplayObject(PHRAuthentication auth, String[] resultIds) throws IOException, SAXException, ParserConfigurationException, NumberFormatException, NotAuthorisedException_Exception, NoSuchItemException_Exception {
		SymptomChecklistCompareDisplayObject symptomChecklistCompareDisplayObject = new SymptomChecklistCompareDisplayObject();

		if (resultIds != null && resultIds.length > 0) {
			// --- get survey results in question ---
			SurveyWs surveyWs = MyOscarServerWebServicesManager.getSurveyWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());
			for (String tempResultId : resultIds) {
				SurveyResultTransfer surveyResultTransfer = surveyWs.getSurveyResult(Long.parseLong(tempResultId));
				MumpsSurveyResultsDisplayObject mumpsSurveyResultsDisplayObject = new MumpsSurveyResultsDisplayObject(surveyResultTransfer);
				symptomChecklistCompareDisplayObject.getResultsToCompare().add(mumpsSurveyResultsDisplayObject);

				int largestQuestion = 0;
				// --- generate question list, it should be all the same across results but just in case the first result only had a sub set of answers or bad xml for some reason ---
				for (Entry entry : mumpsSurveyResultsDisplayObject.getMumpsResultWrapper().getEntries()) {
					String questionId = entry.questionId;
					String questionText = entry.questionText;

					// surveyId / surveyHash / intro entries should be ignored
					if (questionId == null || !questionId.startsWith("E")) continue;
					else largestQuestion = Math.max(largestQuestion, Integer.parseInt(questionId.substring(1)));

					symptomChecklistCompareDisplayObject.getQuestionsList().put(questionId, questionText);
				}
				symptomChecklistCompareDisplayObject.setLastQuestion(largestQuestion);
			}

			if (symptomChecklistCompareDisplayObject.getResultsToCompare().size() > 0) {
				// --- get persons name ---
				// we should get the username from the myoscar server and not the local demographics record.
				// this will help prevent accidents when a person maybe linked to the wrong account.
				MumpsSurveyResultsDisplayObject mumpsSurveyResultsDisplayObject = symptomChecklistCompareDisplayObject.getResultsToCompare().get(0);

				Long myOscarPersonId = mumpsSurveyResultsDisplayObject.getSurveyResultTransfer().getOwningPersonId();
				AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());
				PersonTransfer personTransfer = accountWs.getPerson(myOscarPersonId);
				symptomChecklistCompareDisplayObject.setPatientName(personTransfer.getFirstName() + " " + personTransfer.getLastName());
			}
		}

		return (symptomChecklistCompareDisplayObject);
	}
}
