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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.LabelValueBean;
import org.oscarehr.PMmodule.dao.SurveyDAO;
import org.oscarehr.PMmodule.dao.SurveySecurityDao;
import org.oscarehr.PMmodule.service.SurveyManager;
import org.oscarehr.PMmodule.web.reports.custom.CustomReportDataSource;
import org.oscarehr.PMmodule.web.reports.custom.Item;
import org.oscarehr.survey.model.oscar.OscarForm;
import org.oscarehr.survey.model.oscar.OscarFormData;
import org.oscarehr.survey.model.oscar.OscarFormDataTmpsave;
import org.oscarehr.survey.model.oscar.OscarFormInstance;
import org.oscarehr.survey.model.oscar.OscarFormInstanceTmpsave;
import org.oscarehr.surveymodel.Page;
import org.oscarehr.surveymodel.Question;
import org.oscarehr.surveymodel.Section;
import org.oscarehr.surveymodel.SurveyDocument;

public class SurveyManagerImpl implements SurveyManager, CustomReportDataSource {

	Log log = LogFactory.getLog(SurveyManagerImpl.class);
	
	private SurveyDAO surveyDAO;
	
	public void setSurveyDAO(SurveyDAO dao) {
		this.surveyDAO = dao;
	}

	public List getAllForms(Integer facilityId, String providerNo) {
		List<OscarForm> allForms = getAllForms(facilityId);
		List<OscarForm> results = new ArrayList<OscarForm>();
		SurveySecurityDao securityDao = new SurveySecurityDao();
		//filter out the ones due to security
		for(OscarForm form:allForms) {
			//String name = form.getDescription().toLowerCase().replaceAll(" ","_");
			String name = form.getDescription();
			try {
				if(securityDao.checkPrivilege(name,providerNo)) {
					results.add(form);
				}
			} catch(SQLException e) {
				log.error(e);
			}
			
		}
		return results;
	}
	

	
    private List<OscarForm> getAllForms(Integer facilityId) {
        return surveyDAO.getAllForms(facilityId);
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
		if(tmpInstances.size()> 0 && tmpInstances!=null) {
			OscarFormInstanceTmpsave tmpInstance = (OscarFormInstanceTmpsave)tmpInstances.get(0);
			for(Iterator iter=tmpInstance.getData().iterator();iter.hasNext();) {
				OscarFormDataTmpsave data = (OscarFormDataTmpsave)iter.next();
				this.surveyDAO.deleteFormDataTmpsave(data);
			}		
			surveyDAO.deleteFormInstanceTmpsave(tmpInstance);
		}
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

	public List getForms(String clientId, Integer facilityId, String providerNo){
		List<OscarFormInstance> forms = surveyDAO.getForms(Long.valueOf(clientId), facilityId);
		List<OscarFormInstance> results = new ArrayList<OscarFormInstance>();
		SurveySecurityDao securityDao = new SurveySecurityDao();
		
		for(OscarFormInstance form:forms) {
			String name = form.getDescription();
			try {
				if(securityDao.checkPrivilege(name,providerNo)) {
					results.add(form);
				}
			} catch(SQLException e) {
				log.error(e);
			}
		}
		return results;
	}
	
	public List getForms(String clientId) {
		return surveyDAO.getForms(Long.valueOf(clientId));
	}

	public List getForms(String formId, String clientId) {
		return surveyDAO.getForms(Long.valueOf(formId),Long.valueOf(clientId));
	}

	public List getFormsByFacility(String clientId, Integer facilityId){
		return surveyDAO.getForms(Long.valueOf(clientId), facilityId);
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
	
	public LabelValueBean[] getFormNames() {
		List<OscarForm> forms = surveyDAO.getAllForms();
		LabelValueBean[] results = new LabelValueBean[forms.size()];
		int x=0;
		for(OscarForm form: forms) {
			results[x] = new LabelValueBean(form.getDescription(),String.valueOf(form.getFormId()));
			x++;
		}
		return results;
	}
	
	
	public LabelValueBean[] getItems(String formId) {
		List<LabelValueBean> results = new ArrayList<LabelValueBean>();
		SurveyDocument.Survey survey = getFormModel(formId);
		if(survey == null) {
			return (LabelValueBean[])results.toArray(new LabelValueBean[results.size()]);
		}
		
		int pageId=1;
		int sectionId=0;
		for(Page page:survey.getBody().getPageArray()) {
			for(Page.QContainer qcontainer:page.getQContainerArray()) {
				sectionId=0;
				if(qcontainer.isSetQuestion()) {
					Question question = qcontainer.getQuestion();
					String id = pageId + "_" + sectionId + "_" + question.getId();
					results.add(new LabelValueBean(question.getDescription(),id));
				} else {
					Section section = qcontainer.getSection();
					sectionId = section.getId();
					for(Question question: section.getQuestionArray()) {
						String id = pageId + "_" + sectionId + "_" + question.getId();
						results.add(new LabelValueBean(question.getDescription(),id));
					}
				}
			}
		}
		return (LabelValueBean[])results.toArray(new LabelValueBean[results.size()]);		
	}
	
	public Item getItem(String formId, String id) {
		SurveyDocument.Survey survey = getFormModel(formId);
		if(survey == null) {
			return null;
		}
		
		int pageId=1;
		int sectionId=0;
		for(Page page:survey.getBody().getPageArray()) {
			for(Page.QContainer qcontainer:page.getQContainerArray()) {
				sectionId=0;
				if(qcontainer.isSetQuestion()) {
					Question question = qcontainer.getQuestion();
					String tmpId = pageId + "_" + sectionId + "_" + question.getId();
					if(id.equals(tmpId)) {
						return createItem(question,id);
					}
				} else {
					Section section = qcontainer.getSection();
					sectionId = section.getId();
					for(Question question: section.getQuestionArray()) {
						String tmpId = pageId + "_" + sectionId + "_" + question.getId();
						if(id.equals(tmpId)) {
							return createItem(question,id);
						}
					}
				}
			}
		}
		return null;
	}
	
	private Item createItem(Question question,String id) {
		Item item = new Item();
		item.setId(id);
		item.setName(question.getDescription());
		
		return item;
	}
}
