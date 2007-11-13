/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.PMmodule.service.impl;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.oscarehr.PMmodule.dao.SurveyDAO;
import org.oscarehr.PMmodule.service.SurveyManager;
import org.oscarehr.survey.model.oscar.OscarForm;
import org.oscarehr.survey.model.oscar.OscarFormData;
import org.oscarehr.survey.model.oscar.OscarFormDataTmpsave;
import org.oscarehr.survey.model.oscar.OscarFormInstance;
import org.oscarehr.survey.model.oscar.OscarFormInstanceTmpsave;
import org.oscarehr.surveymodel.SurveyDocument;

public class SurveyManagerImpl implements SurveyManager {

	private SurveyDAO surveyDAO;
	
	public void setSurveyDAO(SurveyDAO dao) {
		this.surveyDAO = dao;
	}
	
	public List getAllForms() {
		return surveyDAO.getAllForms();
	}
	public OscarForm getForm(String formId) {
		return surveyDAO.getForm(Long.valueOf(formId));
	}

	public void saveFormInstance(OscarFormInstance instance) {
		for(Iterator iter=instance.getData().iterator();iter.hasNext();) {
			OscarFormData data = (OscarFormData)iter.next();
			this.surveyDAO.saveFormData(data);
		}
		surveyDAO.saveFormInstance(instance);
	}
	
	public void deleteTmpsave(String instanceId, String formId, String clientId, String providerId) {
		List tmpInstances = getTmpForms(instanceId,formId,clientId,providerId);
		OscarFormInstanceTmpsave tmpInstance = (OscarFormInstanceTmpsave)tmpInstances.get(0);
		for(Iterator iter=tmpInstance.getData().iterator();iter.hasNext();) {
			OscarFormDataTmpsave data = (OscarFormDataTmpsave)iter.next();
			this.surveyDAO.deleteFormDataTmpsave(data);
		}
		surveyDAO.deleteFormInstanceTmpsave(tmpInstance);
	}
	
	public void saveFormInstanceTmpsave(OscarFormInstanceTmpsave instance) {
		for(Iterator iter=instance.getData().iterator();iter.hasNext();) {
			OscarFormDataTmpsave data = (OscarFormDataTmpsave)iter.next();
			this.surveyDAO.saveFormDataTmpsave(data);
		}
		surveyDAO.saveFormInstanceTmpsave(instance);
	}
	
	public OscarFormInstance getLatestForm(String formId, String clientId) {
		return surveyDAO.getLatestForm(Long.valueOf(formId),Long.valueOf(clientId));
	}

	public List getForms(String clientId) {
		return surveyDAO.getForms(Long.valueOf(clientId));
	}

	public List getForms(String formId, String clientId) {
		return surveyDAO.getForms(Long.valueOf(formId),Long.valueOf(clientId));
	}
	public OscarFormInstance getCurrentFormById(String formInstanceId) {
		return surveyDAO.getCurrentFormById(Long.valueOf(formInstanceId));
	}
	public List getTmpForms(String instanceId, String formId, String clientId, String providerId) {
		return surveyDAO.getTmpForms(Long.valueOf(instanceId),Long.valueOf(formId),Long.valueOf(clientId), Long.valueOf(providerId));
	}
	public List getTmpFormData(String tmpInstanceId) {
		return surveyDAO.getTmpFormData(Long.valueOf(tmpInstanceId));
	}
	public void saveFormData(OscarFormData data) {
		surveyDAO.saveFormData(data);
	}

	public void saveFormDataTmpsave(OscarFormDataTmpsave data) {
		surveyDAO.saveFormDataTmpsave(data);
	}
	
	public SurveyDocument.Survey getFormModel(String formId) {
		OscarForm survey = getForm(formId);
		if(survey != null) {
			try {
            	String xml = survey.getSurveyData();
            	SurveyDocument model = SurveyDocument.Factory.parse(new StringReader(xml));
            	return model.getSurvey();
            }catch(Exception e) {
            	e.printStackTrace();
            }
		}
		return null;
	}
	
	public List getCurrentForms(String formId, List clients) {
		return surveyDAO.getCurrentForms(formId,clients);
	}
}
