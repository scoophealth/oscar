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

package org.oscarehr.survey.service;

import java.io.StringReader;
import java.util.List;

import org.oscarehr.common.dao.SurveyDao;
import org.oscarehr.common.model.Survey;
import org.oscarehr.surveymodel.SurveyDocument;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SurveyManager {
	@Autowired
	private SurveyDao surveyDAO;
	
	
	public void saveSurvey(Survey survey) {
		if(survey.getId()==null)
			surveyDAO.persist(survey);
		else
			surveyDAO.merge(survey);
	}

	public Survey getSurvey(String surveyId) {
		return surveyDAO.find(Integer.valueOf(surveyId));
	}

	public List<Survey> getSurveys() {
		return surveyDAO.findAll();
	}

	public void deleteSurvey(String surveyId) {
		surveyDAO.remove(Integer.valueOf(surveyId));
	}

	public Survey getSurveyByName(String name) {
		return surveyDAO.findByName(name);
	}
	
	public Survey updateStatus(String surveyId, short status) {
		Survey survey = getSurvey(surveyId);
		if(survey != null && survey.getStatus() != new Short(status)) {
			survey.setStatus(new Short(status));
		}
		saveSurvey(survey);
		return survey;
	}
	
	public org.oscarehr.surveymodel.SurveyDocument.Survey getSurveyModel(String surveyId) {
		Survey survey = getSurvey(surveyId);
		if(survey != null) {
			try {
            	String xml = survey.getSurveyData();
            	SurveyDocument model = SurveyDocument.Factory.parse(new StringReader(xml));
            	return model.getSurvey();
            }catch(Exception e) {
            	MiscUtils.getLogger().error("Error", e);
            }
		}
		return null;
	}
	
	public org.oscarehr.surveymodel.SurveyDocument getSurveyDocument(String surveyId) {
		Survey survey = getSurvey(surveyId);
		if(survey != null) {
			try {
            	String xml = survey.getSurveyData();
            	SurveyDocument model = SurveyDocument.Factory.parse(new StringReader(xml));
            	return model;
            }catch(Exception e) {
            	MiscUtils.getLogger().error("Error", e);
            }
		}
		return null;
	}
	
}
