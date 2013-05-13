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

package org.oscarehr.survey.web;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.model.SurveyTestData;
import org.oscarehr.common.model.SurveyTestInstance;
import org.oscarehr.survey.service.SurveyManager;
import org.oscarehr.survey.service.SurveyModelManager;
import org.oscarehr.survey.service.SurveyTestManager;
import org.oscarehr.survey.service.UserManager;
import org.oscarehr.survey.web.formbean.SurveyExecuteDataBean;
import org.oscarehr.survey.web.formbean.SurveyExecuteFormBean;
import org.oscarehr.surveymodel.Page;
import org.oscarehr.surveymodel.SurveyDocument;
import org.oscarehr.surveymodel.SurveyDocument.Survey;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;


public class SurveyTestAction extends DispatchAction {

	private static Logger log = MiscUtils.getLogger();

	private SurveyManager surveyManager = (SurveyManager)SpringUtils.getBean("surveyManager");
	private SurveyTestManager surveyTestManager = (SurveyTestManager)SpringUtils.getBean("surveyTestManager");
	private UserManager userManager = (UserManager)SpringUtils.getBean("surveyUserManager");
	
	
	protected void postMessage(HttpServletRequest request, String key, String val) {
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(key,val));
		saveMessages(request,messages);
	}
	
	protected void postMessage(HttpServletRequest request, String key) {
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(key));
		saveMessages(request,messages);
	}
	
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return test(mapping,form,request,response);
	}
	
	public ActionForward test(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		if(!userManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		DynaActionForm form = (DynaActionForm)af;
		SurveyExecuteFormBean formBean = (SurveyExecuteFormBean)form.get("view");
		SurveyExecuteDataBean data = (SurveyExecuteDataBean)form.get("data");
		
		formBean.setTab("");
		data.reset();
		
		String surveyId = request.getParameter("id");
		
		if(surveyId == null) {
			surveyId = String.valueOf(formBean.getId());
			if(surveyId == null || surveyId.equals("0")) {
				postMessage(request,"survey.missing");
				return mapping.findForward("manager");
			}
		}
		formBean.setId(Long.parseLong(surveyId));
		
		org.oscarehr.common.model.Survey surveyObj = surveyManager.getSurvey(String.valueOf(formBean.getId()));
		String descr = surveyObj.getDescription();
		descr = descr.replaceAll(" ","_");
		descr = descr.toLowerCase();				
		String path = request.getSession().getServletContext().getRealPath("/");
		path = path + "survey" + File.separator + "scripts" + File.separator + descr + ".js";
						
		if(new File(path).exists()) {
			request.getSession().setAttribute("validation_file",descr);
		} else {
			request.getSession().setAttribute("validation_file",null);
		}
		
		
		if(surveyObj == null)  {
			postMessage(request,"survey.missing");
			return mapping.findForward("manager");
		}
		
		log.debug("running test on survey " + surveyObj.getId());
		
		SurveyDocument model = null;
		try {
			String xml = surveyObj.getSurveyData();
        	model = SurveyDocument.Factory.parse(new StringReader(xml));
        	request.getSession().setAttribute("model",model);
        }catch(Exception e) {
        	log.error("Error", e);
        	//postMessage(request,"");
        	return mapping.findForward("manager");
        }
        
        /* load test data - if exists */
        SurveyTestInstance instance = surveyTestManager.getSurveyInstance(surveyId,"1");
        if(instance != null) {
        	log.debug("loading up test data");
        	for(Iterator iter=instance.getData().iterator();iter.hasNext();) {
        		SurveyTestData dataItem = (SurveyTestData)iter.next();
        		String key = dataItem.getDataKey();
        		if(SurveyModelManager.isCheckbox(model.getSurvey(),key)) {
        			String value = dataItem.getValue();
        			if(value != null) {
        				data.getValues().put("checkbox_" + key,"checked");
        			} else {
        				data.getValues().put("checkbox_" + key,"");
        			}
        		}
        		data.getValues().put(key,dataItem.getValue());
        	}
        }
        
        return refresh(mapping,form,request,response);
	}
	
	public ActionForward refresh(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		if(!userManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		DynaActionForm form = (DynaActionForm)af;
		SurveyExecuteFormBean formBean = (SurveyExecuteFormBean)form.get("view");
		
		SurveyDocument model = (SurveyDocument)request.getSession().getAttribute("model");
		
        Survey survey = model.getSurvey();
        Page[] pages = survey.getBody().getPageArray();
        List pageNames = new ArrayList();
        
        if(survey.getIntroduction() != null && !survey.getIntroduction().getIncludeOnFirstPage()) {
        	pageNames.add("Introduction");
        	if(formBean.getTab() == null || formBean.getTab().length()==0) {
        		//default first page
        		request.setAttribute("introduction",survey.getIntroduction());
        		request.setAttribute("currentTab","Introduction");
        		log.debug("showing page: introduction");
        	}
        
        	if(formBean.getTab() != null && formBean.getTab().equals("Introduction")) {
        		request.setAttribute("introduction",survey.getIntroduction());
        		request.setAttribute("currentTab","Introduction");
        		
            	log.debug("showing page: introduction");
        	}
        } else {
        	//default first page is page1
        	if(formBean.getTab()== null || formBean.getTab().length()==0) {
        		formBean.setTab(pages[0].getDescription());
        		request.setAttribute("currentTab",pages[0].getDescription());
        		
        	}
        }
        
        for(int x=0;x<pages.length;x++) {
        	Page tmp = pages[x];
        	pageNames.add(tmp.getDescription());
        	if(formBean.getTab() != null && tmp.getDescription().equals(formBean.getTab())) {
        		request.setAttribute("page",tmp);
        		request.setAttribute("pageNumber",String.valueOf(x+1));
        		request.setAttribute("currentTab",tmp.getDescription());
        		if(survey.getIntroduction()!=null && survey.getIntroduction().getIncludeOnFirstPage() && x==0) {
            		request.setAttribute("introduction",survey.getIntroduction());
            	}
        		if(survey.getClosing()!=null && survey.getClosing().getIncludeOnLastPage() && x == (pages.length-1)) {
            		request.setAttribute("closing",survey.getClosing());
            	}
        		log.debug("showing page: " + tmp.getDescription());
        	 }
        }
        
        if(survey.getClosing() != null && !survey.getClosing().getIncludeOnLastPage()) {
        	pageNames.add("Closing");
        	if(formBean.getTab() != null && formBean.getTab().equals("Closing")) {
            	request.setAttribute("closing",survey.getClosing());
            	request.setAttribute("currentTab","Closing");
        		
            	log.debug("showing page: closing");
        	}
        }
        
 
        request.setAttribute("tabs",pageNames);
 
        return mapping.findForward("execute");
	}
		
	public ActionForward save(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		if(!userManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		if(this.isCancelled(request)) {
			return mapping.findForward("manager");
		}
		
		log.debug("calling save() on action");
		DynaActionForm form = (DynaActionForm)af;
		SurveyExecuteFormBean formBean = (SurveyExecuteFormBean)form.get("view");
		SurveyExecuteDataBean data = (SurveyExecuteDataBean)form.get("data");
		org.oscarehr.surveymodel.SurveyDocument.Survey surveyModel = surveyManager.getSurveyModel(String.valueOf(formBean.getId()));
		
		SurveyTestInstance instance = new SurveyTestInstance();
		instance.setClientId(1);
		instance.setDateCreated(new Date());
		instance.setSurveyId((int)formBean.getId());
		instance.setUserId((int)userManager.getUserId(request));
		
		/* fix the checkboxes */
		Map test = new HashMap();
		
		for(Iterator iter = data.getValues().keySet().iterator();iter.hasNext();) {
			String key = (String)iter.next();
			if(key.startsWith("checkbox_")) {
				//found a hidden element related to a checkbox
				String realKey = key.substring(9);
				String value = (String)data.getValues().get(key);
				if(value.equals("checked")) {
					// do nothing
				} else {
					test.put(realKey,null);
					//data.getValues().put(realKey,null);
				}
			}
			
			
		}
		data.getValues().putAll(test);
		
		/* convert the data form bean */
		List qids = SurveyModelManager.getAllQuestionIds(surveyModel);
		for(Iterator iter = qids.iterator();iter.hasNext();) {
			String key = (String)iter.next();
			//log.debug("key=" + key + ",value=" + (String)data.getValue(key));
			String[] parsed = key.split("_");
	    	String pageNumber = parsed[0];
	    	String sectionId = parsed[1];
	    	String questionId = parsed[2];
	    	
			SurveyTestData dataItem = new SurveyTestData();
			dataItem.setPageNumber(Integer.parseInt(pageNumber));
			dataItem.setSectionId(Integer.parseInt(sectionId));
			dataItem.setQuestionId(Integer.parseInt(questionId));
			dataItem.setValue((String)data.getValue(key));
			dataItem.setDataKey(key);
			instance.getData().add(dataItem);
		}
		
		surveyTestManager.saveSurveyInstance(instance);
		
		return mapping.findForward("manager");
	}
	
	public static String getCalendarFormat(String val) {
		if(val.equals("yyyy-mm-dd")) {
			return "%Y-%m-%d";
		}
		if(val.equals("yyyy/mm/dd")) {
			return "%Y/%m/%d";
		}
		if(val.equals("dd/mm/yy")) {
			return "%d/%m/%y";
		}
		if(val.equals("mm/dd/yy")) {
			return "%m/%d/%y";
		}
		
		return "%Y-%m-%d";
	}
}
