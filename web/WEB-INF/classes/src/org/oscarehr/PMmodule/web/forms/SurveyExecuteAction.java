package org.oscarehr.PMmodule.web.forms;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.PMmodule.service.SurveyManager;
import org.oscarehr.survey.model.oscar.OscarForm;
import org.oscarehr.survey.model.oscar.OscarFormData;
import org.oscarehr.survey.model.oscar.OscarFormInstance;
import org.oscarehr.survey.service.SurveyModelManager;
import org.oscarehr.survey.web.formbean.SurveyExecuteDataBean;
import org.oscarehr.survey.web.formbean.SurveyExecuteFormBean;
import org.oscarehr.surveymodel.Page;
import org.oscarehr.surveymodel.SurveyDocument;
import org.oscarehr.surveymodel.SurveyDocument.Survey;

public class SurveyExecuteAction extends DispatchAction {
	private Log log = LogFactory.getLog(getClass());

	private SurveyManager surveyManager;

	public void setSurveyManager(SurveyManager mgr) {
		this.surveyManager = mgr;
	}

	public ActionForward forwardToClientManager(HttpServletRequest request, ActionMapping mapping,ActionForm form, String clientId) {
		/*
		ActionForward forward =  mapping.findForward("client_manager");
		String path = forward.getPath();
		
		//return forward;
		ActionForward af =  new ActionForward(path + "&view.tab=Forms&id=" + clientId);
		af.setRedirect(true);
		return af;
		*/
		request.setAttribute("demographicNo",clientId);
		request.setAttribute("clientId",clientId);
		request.setAttribute("tab.override","Forms");
		((DynaActionForm)form).reset(mapping,request);
		
		return mapping.findForward("success");
	}
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String clientId = request.getParameter("clientId");
		
		return forwardToClientManager(request,mapping,form, clientId);
	}
	
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
	
	protected long getUserId(HttpServletRequest request) {
		String value = (String)request.getSession().getAttribute("user");
		return Long.parseLong(value);
	}
	
	protected String getUsername(HttpServletRequest request) {
		Provider provider = (Provider)request.getSession().getAttribute("provider");
		return provider.getFormattedName();
	}
	
	public ActionForward survey(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form = (DynaActionForm)af;
		SurveyExecuteFormBean formBean = (SurveyExecuteFormBean)form.get("view");
		SurveyExecuteDataBean data = (SurveyExecuteDataBean)form.get("data");
		
		formBean.setTab("");
		data.reset();
		
		String surveyId = request.getParameter("formId");
		String clientId = request.getParameter("clientId");
		
		if(surveyId == null) {
			surveyId = String.valueOf(formBean.getId());
			if(surveyId == null || surveyId.equals("0")) {
				postMessage(request,"survey.missing");
				return forwardToClientManager(request,mapping,form, clientId);
			}
		}
		if(clientId == null) {
			clientId = String.valueOf(formBean.getClientId());
		}
		
		formBean.setId(Long.parseLong(surveyId));
		formBean.setClientId(Long.parseLong(clientId));
		OscarForm surveyObj = surveyManager.getForm(surveyId);
		if(surveyObj == null)  {
			postMessage(request,"survey.missing");
			return forwardToClientManager(request,mapping,form, clientId);
		}
		
		formBean.setDescription(surveyObj.getDescription());
		
		
		log.debug("executing survey " + surveyObj.getFormId());
		
		SurveyDocument model = null;
		try {
			String xml = surveyObj.getSurveyData();
        	model = SurveyDocument.Factory.parse(new StringReader(xml));
        	request.getSession().setAttribute("model",model);
        }catch(Exception e) {
        	log.error(e);
        	postMessage(request,"");
        	return forwardToClientManager(request,mapping,form, clientId);
        }
        
        /* load test data - if exists */
        OscarFormInstance instance = surveyManager.getLatestForm(surveyId,clientId);
        if(instance != null) {
        	log.debug("loading up existing data");
        	for(Iterator iter=instance.getData().iterator();iter.hasNext();) {
        		OscarFormData dataItem = (OscarFormData)iter.next();
        		String key = dataItem.getKey();
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
	
	public ActionForward save_survey(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form = (DynaActionForm)af;
		SurveyExecuteFormBean formBean = (SurveyExecuteFormBean)form.get("view");
		
		if(this.isCancelled(request)) {
			return forwardToClientManager(request,mapping,form,String.valueOf(formBean.getClientId()));
		}
		
		log.debug("calling save() on action");
		SurveyExecuteDataBean data = (SurveyExecuteDataBean)form.get("data");
		org.oscarehr.surveymodel.SurveyDocument.Survey surveyModel = surveyManager.getFormModel(String.valueOf(formBean.getId()));
		
		OscarFormInstance instance = new OscarFormInstance();
		instance.setClientId(formBean.getClientId());
		instance.setDateCreated(new Date());
		instance.setFormId(formBean.getId());
		instance.setUserId(getUserId(request));
		instance.setDescription(formBean.getDescription());
		instance.setUsername(getUsername(request));
		
		/* fix the checkboxes */
		Map test = new HashMap();
		
		for(Iterator iter = data.getValues().keySet().iterator();iter.hasNext();) {
			String key = (String)iter.next();
			if(key.startsWith("checkbox_")) {
				//found a hidden element related to a checkbox
				String realKey = key.substring(9);
				String value = (String)data.getValues().get(key);
				if(value.equals("checked")) {
					
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
	    	
			OscarFormData dataItem = new OscarFormData();
			dataItem.setPageNumber(Long.parseLong(pageNumber));
			dataItem.setSectionId(Long.parseLong(sectionId));
			dataItem.setQuestionId(Long.parseLong(questionId));
			dataItem.setValue((String)data.getValue(key));
			dataItem.setKey(key);
			instance.getData().add(dataItem);
		}
		
		surveyManager.saveFormInstance(instance);
		
		form.set("data",new SurveyExecuteDataBean());
		form.set("view",new SurveyExecuteFormBean());
		
		return forwardToClientManager(request,mapping,form, String.valueOf(formBean.getClientId()));
		//request.setAttribute("survey_saved",new Boolean(true));
		//request.setAttribute("clientId",String.valueOf(formBean.getClientId()));
		//return mapping.findForward("client_manager");
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
