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

package org.oscarehr.PMmodule.service;

import java.util.List;

import org.oscarehr.survey.model.oscar.OscarForm;
import org.oscarehr.survey.model.oscar.OscarFormData;
import org.oscarehr.survey.model.oscar.OscarFormDataTmpsave;
import org.oscarehr.survey.model.oscar.OscarFormInstance;
import org.oscarehr.survey.model.oscar.OscarFormInstanceTmpsave;
import org.oscarehr.surveymodel.SurveyDocument;

public interface SurveyManager {
	//public List getAllForms();
    //public List getAllForms(Integer facilityId);
    public List getAllForms(Integer facilityId, String providerNo);
	public OscarForm getForm(String formId);
	public void saveFormInstance(OscarFormInstance instance);
	public void saveFormInstanceTmpsave(OscarFormInstanceTmpsave instance);
	public OscarFormInstance getLatestForm(String formId, String clientId);
	public List getForms(String clientId, Integer facilityId, String providerNo);
	//public List getForms(String clientId);
	//public List getForms(String formId, String clientId);	
	//public List getFormsByFacility(String clientId, Integer facilityId);	
	public OscarFormInstance getCurrentFormById(String formInstanceId);
	public List getTmpForms(String tmpInstanceId, String formId, String clientId, String providerId);
	public List getTmpFormData(String tmpInstanceId);
	public void saveFormData(OscarFormData data);
	public void saveFormDataTmpsave(OscarFormDataTmpsave data);
	public SurveyDocument.Survey getFormModel(String formId);
	
	public List getCurrentForms(String formId, List clients);
	public void deleteTmpsave(String instanceId, String formId, String clientId, String providerId);
}
