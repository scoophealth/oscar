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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
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
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.xmlbeans.XmlOptions;
import org.oscarehr.common.dao.CaisiFormDao;
import org.oscarehr.common.model.Survey;
import org.oscarehr.survey.model.QuestionTypes;
import org.oscarehr.survey.service.OscarFormManager;
import org.oscarehr.survey.service.SurveyLaunchManager;
import org.oscarehr.survey.service.SurveyManager;
import org.oscarehr.survey.service.SurveyModelManager;
import org.oscarehr.survey.service.SurveyTestManager;
import org.oscarehr.survey.service.UserManager;
import org.oscarehr.survey.web.formbean.PageNavEntry;
import org.oscarehr.survey.web.formbean.SurveyManagerFormBean;
import org.oscarehr.surveymodel.DateDocument;
import org.oscarehr.surveymodel.DateDocument.Date.Enum;
import org.oscarehr.surveymodel.Page;
import org.oscarehr.surveymodel.PossibleAnswersDocument.PossibleAnswers;
import org.oscarehr.surveymodel.Question;
import org.oscarehr.surveymodel.Section;
import org.oscarehr.surveymodel.SelectDocument.Select;
import org.oscarehr.surveymodel.SurveyDocument;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;



public class SurveyManagerAction extends AbstractSurveyAction {
	private static Logger log = MiscUtils.getLogger();

	private SurveyManager surveyManager = (SurveyManager)SpringUtils.getBean("surveyManager");
	private SurveyTestManager surveyTestManager = (SurveyTestManager)SpringUtils.getBean("surveyTestManager");
	private SurveyLaunchManager surveyLaunchManager = (SurveyLaunchManager)SpringUtils.getBean("surveyLaunchManager");
	private QuestionTypes questionTypes = (QuestionTypes)SpringUtils.getBean("QuestionTypes");
	private CaisiFormDao caisiFormDao = SpringUtils.getBean(CaisiFormDao.class);
	private OscarFormManager oscarFormManager = (OscarFormManager)SpringUtils.getBean("oscarFormManager");
	private UserManager surveyUserManager = (UserManager)SpringUtils.getBean("surveyUserManager");
	
	public void setSurveyManager(SurveyManager mgr) {
		this.surveyManager = mgr;
	}
	
	public void setSurveyTestManager(SurveyTestManager mgr) {
		this.surveyTestManager = mgr;
	}
    
	
	
	public void setQuestionTypes(QuestionTypes qt) {
		this.questionTypes = qt;
	}
	
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return list(mapping,form,request,response);
	}
	
    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
    	
        request.setAttribute("surveys",surveyManager.getSurveys());
        
        request.setAttribute("released_forms", caisiFormDao.getCaisiForms());
        return mapping.findForward("list");
    }
    
    public ActionForward reportForm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	        
        return mapping.findForward("reportForm");
    }
    
    
    public ActionForward test(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
    	
    	String surveyId = request.getParameter("id");
    	if(surveyId != null) {
    		surveyManager.updateStatus(surveyId,Survey.STATUS_TEST);
    	}
    	request.setAttribute("id",surveyId);
    	return mapping.findForward("execute");
    }
    
    public ActionForward new_survey(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
    	
    	DynaActionForm surveyForm = (DynaActionForm)form;
    	surveyForm.set("survey",new Survey());
    	surveyForm.set("web",new SurveyManagerFormBean());
    	surveyForm.set("model",null);
    	surveyForm.set("pageModel",null);
    	
    	request.setAttribute("templates",surveyManager.getSurveys());
    	
    	return mapping.findForward("new_survey");
    }
    
    public ActionForward create_survey(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
    	
    	DynaActionForm surveyForm = (DynaActionForm)form;
    	Survey survey = (Survey)surveyForm.get("survey");
    	SurveyManagerFormBean formBean = (SurveyManagerFormBean)surveyForm.get("web");
		
    	//make sure there's a name
    	if(survey.getDescription() != null && survey.getDescription().equals("")) {
    		postMessage(request,"survey.noname");
    		request.setAttribute("templates",surveyManager.getSurveys());
    		return mapping.findForward("new_survey");
    	}
    	
    	//check to see if name exists
    	if(surveyManager.getSurveyByName(survey.getDescription()) != null) {
    		this.postMessage(request,"name.exists");
    		request.setAttribute("templates",surveyManager.getSurveys());
    		return mapping.findForward("new_survey");
    	}
    
    	SurveyDocument model = null;
    	
    	//if using a template, load model into memory
    	if(formBean.getTemplateId()>0) {
    		Survey template = surveyManager.getSurvey(String.valueOf(formBean.getTemplateId()));
    		String xml = template.getSurveyData();
    		try {
    			model = SurveyDocument.Factory.parse(new StringReader(xml));
    			model.getSurvey().setName(survey.getDescription());
    			model.getSurvey().setVersion(0);
    		}catch(Exception e) {
    			this.postMessage(request,"survey.create_error","Error loading template model");
        		request.setAttribute("templates",surveyManager.getSurveys());
        		return mapping.findForward("new_survey");
    		}		
    	} else {
	    	//build a new model
	    	model = SurveyDocument.Factory.newInstance();
	    	SurveyDocument.Survey surveyData = model.addNewSurvey();
	    	surveyData.setName(survey.getDescription());
	    	surveyData.setBody(surveyData.addNewBody());
	    	Page page = surveyData.getBody().addNewPage();
	    	page.setDescription("Page 1");
    	}
    	
    	formBean.setPage("1");
    	surveyForm.set("model",model);
    	
    	return form(mapping,form,request,response);
    }
    
	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		DynaActionForm surveyForm = (DynaActionForm)form;
		SurveyManagerFormBean formBean = (SurveyManagerFormBean)surveyForm.get("web");
		
		String id = request.getParameter("id");
		if(id==null)
			id = (String)request.getAttribute("id");
		
		if(id == null) {
			this.postMessage(request,"survey.noid");
			return list(mapping,form,request,response);
		}
		
		
		Survey survey = surveyManager.getSurvey(id);

        if(survey == null) {
        	this.postMessage(request,"survey.edit_error","Survey not found");
            return list(mapping,form,request,response);
        }
        
        if(survey.getStatus() == Survey.STATUS_TEST) {
        	surveyTestManager.clearTestData(id);
        }
        
        surveyForm.set("survey",survey);
        
        try {
        	String xml = survey.getSurveyData();
        	SurveyDocument model = SurveyDocument.Factory.parse(new StringReader(xml));
        	surveyForm.set("model",model);
        }catch(Exception e) {
        	log.error("Error", e);
        	postMessage(request,"survey.edit_error",e.getMessage());
        	return list(mapping,form,request,response);
        }
        
        formBean.setPage("1");
		
        return form(mapping,form,request,response);
	}
	
	public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		DynaActionForm surveyForm = (DynaActionForm)form;
		SurveyDocument model = (SurveyDocument)surveyForm.get("model");
		SurveyManagerFormBean formBean = (SurveyManagerFormBean)surveyForm.get("web");
		
		SurveyDocument.Survey survey = model.getSurvey();
		
		List pages = new ArrayList();
		if(survey.getIntroduction() != null) {
			pages.add(new PageNavEntry("Introduction","Introduction"));
		}
		
		for(int x=0;x<survey.getBody().getPageArray().length;x++) {
			pages.add(new PageNavEntry(String.valueOf(x+1),survey.getBody().getPageArray(x).getDescription()));
		}
		
		if(survey.getClosing() != null) {
			pages.add(new PageNavEntry("Closing","Closing"));
		}
		
		request.setAttribute("pages",pages);
					
		String pageName = formBean.getPage();
		
		if(pageName.equalsIgnoreCase("Introduction")) {
			// do nothing
		}
		else if(pageName.equals("Closing")) {
			// do nothing
		} else {	
			
			if(pageName == null || pageName.length()==0) {
				pageName = "1";
	    	    }
	    	    
			if(pageName.startsWith("Page")) {
				pageName = pageName.substring(5);
			}
			
			int pn = Integer.parseInt(pageName);
			
			Page p = survey.getBody().getPageArray(pn-1);
			
			surveyForm.set("pageModel",p);
			request.setAttribute("page_number",pageName);
		}
		
		request.setAttribute("QuestionTypes", questionTypes);
		
        setSectionProperties(request,survey,formBean);
        
        List<String> colorList =  new ArrayList<String>();
        colorList.add("red");
        colorList.add("green");
        colorList.add("blue");
        colorList.add("yellow");
        colorList.add("beige");
        colorList.add("brown");
        colorList.add("cyan");
        colorList.add("grey");
        colorList.add("magenta");
        colorList.add("orange");
        colorList.add("pink");
        colorList.add("purple");        
        request.setAttribute("colors", colorList);
        
		return mapping.findForward("edit");
	}
		
	public ActionForward navigate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		//DynaActionForm surveyForm = (DynaActionForm)form;		
		return form(mapping,form,request,response);
		
	}
	
	public ActionForward add_introduction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		DynaActionForm surveyForm = (DynaActionForm)form;
		SurveyManagerFormBean formBean = (SurveyManagerFormBean)surveyForm.get("web");
		SurveyDocument model = (SurveyDocument)surveyForm.get("model");
		SurveyDocument.Survey surveyModel = model.getSurvey();
		
		if(surveyModel.getIntroduction() == null) {
			surveyModel.setIntroduction(surveyModel.addNewIntroduction());
			surveyModel.getIntroduction().setIncludeOnFirstPage(true);
		}
		formBean.setPage("Introduction");
		return form(mapping,form,request,response);
	}
	
	public ActionForward remove_introduction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		DynaActionForm surveyForm = (DynaActionForm)form;
		SurveyManagerFormBean formBean = (SurveyManagerFormBean)surveyForm.get("web");
		SurveyDocument model = (SurveyDocument)surveyForm.get("model");
		SurveyDocument.Survey surveyModel = model.getSurvey();
		
		if(surveyModel.getIntroduction() != null) {
			surveyModel.unsetIntroduction();
		}
		
		formBean.setPage("1");
		return form(mapping,form,request,response);
	}
	
	public ActionForward add_page(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		DynaActionForm surveyForm = (DynaActionForm)form;
		SurveyManagerFormBean formBean = (SurveyManagerFormBean)surveyForm.get("web");
		SurveyDocument model = (SurveyDocument)surveyForm.get("model");
		
		SurveyDocument.Survey survey = model.getSurvey();
		Page p = survey.getBody().addNewPage();
		p.setDescription(String.valueOf("Page " + survey.getBody().getPageArray().length));
		formBean.setPage(String.valueOf(survey.getBody().getPageArray().length));
		return form(mapping,form,request,response);
	}
	
	public ActionForward remove_page(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		DynaActionForm surveyForm = (DynaActionForm)form;
		SurveyDocument model = (SurveyDocument)surveyForm.get("model");
		SurveyManagerFormBean formBean = (SurveyManagerFormBean)surveyForm.get("web");

                
		SurveyDocument.Survey survey = model.getSurvey();
		int pageNumber = Integer.parseInt(request.getParameter("id"));
		
		log.debug("removing page number " + pageNumber);
		//remove the page, only if it's not the only page
		if(survey.getBody().getPageArray().length == 1) {
			postMessage(request,"survey_1page");
			return form(mapping,form,request,response);
		}
		
		Page p = survey.getBody().getPageArray(pageNumber-1);
		if(p != null) {
			survey.getBody().removePage(pageNumber-1);
		}
		
		formBean.setPage("1");
		
		return form(mapping,form,request,response);
	}

	public ActionForward add_closing(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		DynaActionForm surveyForm = (DynaActionForm)form;
		SurveyManagerFormBean formBean = (SurveyManagerFormBean)surveyForm.get("web");
		SurveyDocument model = (SurveyDocument)surveyForm.get("model");
		SurveyDocument.Survey surveyModel = model.getSurvey();
		
		if(surveyModel.getClosing() == null) {
			surveyModel.setClosing(surveyModel.addNewClosing());
			surveyModel.getClosing().setIncludeOnLastPage(true);
		}
		
		formBean.setPage("Closing");
		return form(mapping,form,request,response);
	}
	
	public ActionForward remove_closing(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		DynaActionForm surveyForm = (DynaActionForm)form;
		SurveyManagerFormBean formBean = (SurveyManagerFormBean)surveyForm.get("web");
		SurveyDocument model = (SurveyDocument)surveyForm.get("model");
		SurveyDocument.Survey surveyModel = model.getSurvey();
		
		if(surveyModel.getClosing() != null) {
			surveyModel.unsetClosing();
		}
		
		formBean.setPage("1");
		return form(mapping,form,request,response);
	}
	
	public ActionForward update_section(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("updateSection", "true");
		return form(mapping,form,request,response);
	}
	
	public ActionForward add_section(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		DynaActionForm surveyForm = (DynaActionForm)form;
		SurveyManagerFormBean formBean = (SurveyManagerFormBean)surveyForm.get("web");
		SurveyDocument model = (SurveyDocument)surveyForm.get("model");
		SurveyDocument.Survey surveyModel = model.getSurvey();
		
		String pageNumber = formBean.getPage();
		Page page = surveyModel.getBody().getPageArray(Integer.parseInt(pageNumber)-1);
		
		Section section = page.addNewQContainer().addNewSection();
		section.setDescription("");
		section.setId(SurveyManagerAction.getUnusedSectionId(page));
		
		return form(mapping,form,request,response);
	}
	
	public ActionForward remove_section(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		DynaActionForm surveyForm = (DynaActionForm)form;
		SurveyManagerFormBean formBean = (SurveyManagerFormBean)surveyForm.get("web");
		SurveyDocument model = (SurveyDocument)surveyForm.get("model");
		SurveyDocument.Survey surveyModel = model.getSurvey();
		
		int id = Integer.parseInt(request.getParameter("id"));
		String pageNumber = formBean.getPage();
		Page page = surveyModel.getBody().getPageArray(Integer.parseInt(pageNumber)-1);
		
		
		for(int x=0;x<page.getQContainerArray().length;x++) {
			if(page.getQContainerArray(x).isSetSection()) {
				Section section = page.getQContainerArray(x).getSection();
				if(section.getId() == id) {
					//found it
					page.removeQContainer(x);
				}
			}
		}
		
		return form(mapping,form,request,response);
	}
	
	public ActionForward add_question(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		DynaActionForm surveyForm = (DynaActionForm)form;
		SurveyManagerFormBean formBean = (SurveyManagerFormBean)surveyForm.get("web");
		SurveyDocument model = (SurveyDocument)surveyForm.get("model");
		SurveyDocument.Survey surveyModel = model.getSurvey();
	
		String pageNumber = formBean.getPage();
		int sectionId = formBean.getSection();
		String questionType = formBean.getQuestionTypeData();
		
		//log.debug("add_question: page=" + pageNumber);
		//log.debug("add_question: section=" + sectionId);
		//log.debug("add_question: questionType=" + questionType);
		
		/* find right place */
		Page page = surveyModel.getBody().getPageArray(Integer.parseInt(pageNumber)-1);
		if(sectionId == 0) {
			//no section
			SurveyModelManager.createQuestion(surveyModel,pageNumber,questionType);
		} else {
			//find section
			Section section = SurveyModelManager.findSection(surveyModel,pageNumber,sectionId);
			if(section != null) {
				SurveyModelManager.createQuestion(surveyModel,page,section,questionType);
			}
		}
		
		formBean.setSection(0);
		formBean.setQuestionType("");
		formBean.setQuestionTypeData("");
		
		return form(mapping,form,request,response);
	}
	
	public ActionForward edit_question(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		DynaActionForm surveyForm = (DynaActionForm)form;
		SurveyManagerFormBean formBean = (SurveyManagerFormBean)surveyForm.get("web");
		SurveyDocument model = (SurveyDocument)surveyForm.get("model");
		SurveyDocument.Survey surveyModel = model.getSurvey();
		
		String questionId = request.getParameter("id");
		int qid = Integer.parseInt(questionId);
		String sectionId = request.getParameter("section");
		String pageNumber = request.getParameter("page");
		
		Question question = SurveyModelManager.findQuestion(surveyModel,pageNumber,Integer.parseInt(sectionId),qid);
		
		List<LabelValueBean> oscarVars = new ArrayList<LabelValueBean>();
		List<String> objects = new ArrayList<String>();
		
		if(question != null && question.getType().isSetSelect()) {
			Select select = question.getType().getSelect();
			PossibleAnswers pa = select.getPossibleAnswers();
			formBean.setNumAnswers(pa.getAnswerArray().length);
		}
		if(question != null && question.getType().isSetDate()) {
			DateDocument.Date.Enum enum1 = question.getType().getDate();
			List dateFormats = new ArrayList();
			for(int x=1;x<=Enum.table.lastInt();x++) {
				LabelValueBean bean = new LabelValueBean();
				bean.setValue(String.valueOf(Enum.table.forInt(x).intValue()));
				bean.setLabel(Enum.table.forInt(x).toString());
				dateFormats.add(bean);
			}
			formBean.setDateFormat(String.valueOf(enum1.intValue()));
			request.setAttribute("dateFormats",dateFormats);
			
			oscarVars.add(new LabelValueBean("Date of Birth","Demographic/birthDate"));
			oscarVars.add(new LabelValueBean("Program Admission","Program/admissionDate"));		
		}
		if(question != null && question.getType().isSetOpenEnded()) {
			//objects			
			objects.add("Current Issues");
			objects.add("Current Medications");
			objects.add("Allergies");
			
			oscarVars.add(new LabelValueBean("First Name","Demographic/FirstName"));
			oscarVars.add(new LabelValueBean("Last Name","Demographic/LastName"));
			oscarVars.add(new LabelValueBean("Program Admission Notes","Program/admissionNotes"));
		}
		
		if(question != null && question.getType().isSetSelect()) {
			//oscarVars.add(new LabelValueBean("Program Selection","Program"));
			objects.add("Program Selector");
		}
				
		
		//request.setAttribute("caisiobjects", objects);
		//request.setAttribute("oscarVars", oscarVars);
		request.getSession().setAttribute("caisiobjects", objects);		
		request.getSession().setAttribute("oscarVars", oscarVars);
		surveyForm.set("questionModel",question);

	       List<String> colorList =  new ArrayList<String>();
	        colorList.add("red");
	        colorList.add("green");
	        colorList.add("blue");
	        colorList.add("yellow");
	        colorList.add("beige");
	        colorList.add("brown");
	        colorList.add("cyan");
	        colorList.add("grey");
	        colorList.add("magenta");
	        colorList.add("orange");
	        colorList.add("pink");
	        colorList.add("purple");        
	        request.getSession().setAttribute("colors", colorList);
	        
		return mapping.findForward("question_editor");
	}
	
	public ActionForward question_adjust_possible_answers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		
		DynaActionForm surveyForm = (DynaActionForm)form;
		Question question = (Question)surveyForm.get("questionModel");
		SurveyManagerFormBean formBean = (SurveyManagerFormBean)surveyForm.get("web");
		
		
		
		if(question.getType().isSetSelect()) {
			Select select = question.getType().getSelect();
			PossibleAnswers pa = select.getPossibleAnswers();
			if(formBean.getNumAnswers() > pa.getAnswerArray().length) {
				//adding more
				for(int x=0;x<(formBean.getNumAnswers()-pa.getAnswerArray().length+1);x++) {
					pa.addNewAnswer();
				}
			} else if(formBean.getNumAnswers() < pa.getAnswerArray().length) {
				//remove some
				int index = pa.getAnswerArray().length-1;
				int numToRemove = pa.getAnswerArray().length-formBean.getNumAnswers();
				log.debug("index=" + index);
				log.debug("numtoRemove=" + numToRemove);
				while(numToRemove-- > 0) {
					pa.removeAnswer(index--);
				}
			}
			formBean.setNumAnswers(pa.getAnswerArray().length);
		}
		
		return mapping.findForward("question_editor");
	}
	
	public ActionForward save_question(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		DynaActionForm surveyForm = (DynaActionForm)form;
		Question question = (Question)surveyForm.get("questionModel");
		SurveyManagerFormBean formBean = (SurveyManagerFormBean)surveyForm.get("web");
		
		log.info("saving question: " + question.getDescription());
		
		if(question.getType().isSetSelect()) {
			Select select = question.getType().getSelect();
			PossibleAnswers pa = select.getPossibleAnswers();
			int numAnswers = formBean.getNumAnswers();
			for(int x=0;x<numAnswers;x++) {
				String answer = request.getParameter("answer_" + (x+1));
				pa.setAnswerArray(x,answer);
			}
		}
		if(question.getType().isSetDate()) {
			int dateFormat = Integer.parseInt(formBean.getDateFormat());
			question.getType().setDate(DateDocument.Date.Enum.forInt(dateFormat));
		}
		
		if(request.getParameter("questionModel.bold") == null) {
			question.setBold("");
		}
		if(request.getParameter("questionModel.underline") == null) {
			question.setUnderline("");
		}
		if(request.getParameter("questionModel.italics") == null) {
			question.setItalics("");
		}
		//return null;
		return form(mapping,form,request,response);
		
		
	}

	public ActionForward remove_question(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		DynaActionForm surveyForm = (DynaActionForm)form;
		SurveyManagerFormBean formBean = (SurveyManagerFormBean)surveyForm.get("web");
		SurveyDocument model = (SurveyDocument)surveyForm.get("model");
		SurveyDocument.Survey surveyModel = model.getSurvey();

		String id = request.getParameter("id");
		int sectionId = formBean.getSection();
		String pageNumber = formBean.getPage();
		
		SurveyModelManager.removeQuestion(surveyModel,pageNumber,sectionId,Integer.parseInt(id));
		
		return form(mapping,form,request,response);
	}

	
	public ActionForward clear_test_data(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		String surveyId = request.getParameter("id");
		if(surveyId != null) {
			surveyTestManager.clearTestData(surveyId);
		}
		return list(mapping,form,request,response);
	}
	
	public ActionForward launch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		String surveyId = request.getParameter("id");
		if(surveyId != null) {
			Survey survey = surveyManager.getSurvey(surveyId);
			if(survey.getStatus()==1) {
			long instanceId = surveyLaunchManager.launch(survey);
			survey.setLaunchedInstanceId((int)instanceId);
			surveyManager.saveSurvey(survey);
			surveyManager.updateStatus(surveyId,Survey.STATUS_LAUNCHED);
			}
		}
		return list(mapping,form,request,response);
	}
	
	public ActionForward close(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		String surveyId = request.getParameter("id");
		if(surveyId != null) {
			surveyLaunchManager.close(surveyManager.getSurvey(surveyId).getLaunchedInstanceId());
			surveyManager.updateStatus(surveyId,Survey.STATUS_CLOSED);
		}
		return list(mapping,form,request,response);
	}
	
	public ActionForward reopen(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		String surveyId = request.getParameter("id");
		if(surveyId != null) {
			surveyLaunchManager.reopen(surveyManager.getSurvey(surveyId).getLaunchedInstanceId());
			surveyManager.updateStatus(surveyId,Survey.STATUS_LAUNCHED);
		}
		return list(mapping,form,request,response);
	}
	
	
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
	
		DynaActionForm surveyForm = (DynaActionForm)form;
		Survey survey = (Survey)surveyForm.get("survey");
		SurveyManagerFormBean formBean = (SurveyManagerFormBean)surveyForm.get("web");
		SurveyDocument model = (SurveyDocument)surveyForm.get("model");
		SurveyDocument.Survey surveyModel = model.getSurvey();
		
		if(this.isCancelled(request)) {
			return list(mapping,form,request,response);
		}
		
		setSectionProperties(request,surveyModel,formBean);
        
		surveyModel.setVersion(surveyModel.getVersion()+1);
		survey.setVersion(surveyModel.getVersion());
		
        //Add creation date
        if(survey.getDateCreated()==null) {
            survey.setDateCreated(new Date());
        }
    
        survey.setFacilityId(loggedInInfo.getCurrentFacility().getId());
        
        survey.setUserId(Integer.valueOf((String)request.getSession().getAttribute("user")));
        
        try {
        	StringWriter sw = new StringWriter();
        	model.save(sw);
        	String xml = sw.toString();
        	survey.setSurveyData(xml);
        }catch(Exception e) {
        	this.postMessage(request,"survey.saved.error","Unabled to create XML :" + e.getMessage());
        	return form(mapping,form,request,response);
        }
        
        survey.setStatus(new Short(Survey.STATUS_IN_REVIEW));
        
		surveyManager.saveSurvey(survey);
		
		postMessage(request,"survey.saved");
		
		return mapping.findForward("redirect");
	}
	
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		String id = request.getParameter("id");
		
		surveyManager.deleteSurvey(id);
		
		postMessage(request,"survey.deleted");
	
		//return list(mapping,form,request,response);
		return mapping.findForward("redirect");
	}

	public ActionForward show_import_form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		return mapping.findForward("import");
	}	
	
	public ActionForward import_survey(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		DynaActionForm surveyForm = (DynaActionForm)form;
		SurveyManagerFormBean formBean = (SurveyManagerFormBean)surveyForm.get("web");
		
		SurveyDocument surveyDocument = null;
		try {
			InputStream is = formBean.getImportFile().getInputStream();
			surveyDocument = SurveyDocument.Factory.parse(is);
		}catch(Exception e) {
			postMessage(request,"execute.parse_error",e.getMessage());
			return list(mapping,form,request,response);
		}
		
		//check name
		if(surveyManager.getSurveyByName(surveyDocument.getSurvey().getName()) != null) {
			this.postMessage(request,"name.exists");
    		return list(mapping,form,request,response);
		}
		
		try {	
			Survey survey = new Survey();
			survey.setDateCreated(new Date());
			survey.setDescription(surveyDocument.getSurvey().getName());
			survey.setStatus(new Short(Survey.STATUS_TEST));
			//survey.setSurveyData()
			survey.setUserId(new Long(surveyUserManager.getUserId(request)).intValue());
			survey.setVersion(surveyDocument.getSurvey().getVersion());
			
			//save data
			StringWriter sw = new StringWriter();
        	surveyDocument.save(sw);
        	String xml = sw.toString();
        	survey.setSurveyData(xml);
			
        	surveyManager.saveSurvey(survey);
		}catch(Exception e) {
			postMessage(request,"survey.import_error",e.getMessage());
			log.error("Error", e);
		}
		return list(mapping,form,request,response);
	}
	
	
	public ActionForward export(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		String id = request.getParameter("id");
		
		SurveyDocument survey = surveyManager.getSurveyDocument(id);
		if(survey == null) {
			postMessage(request,"survey.missing");
			return list(mapping,form,request,response);
		}
		
		response.setContentType("text/xml");
		response.setHeader("Content-disposition","attachement;filename=" + survey.getSurvey().getName() + ".xml");
		try {
			XmlOptions options = new XmlOptions();
			options.setSavePrettyPrint();
			options.setSavePrettyPrintIndent(4);
			survey.save(response.getWriter(),options);
		}catch(Exception e) {
			log.error("Error", e);
		}
		
		return null;
	}
	
	public ActionForward copy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(!surveyUserManager.isAdmin(request)) {
    		postMessage(request,"survey.auth");
    		return mapping.findForward("auth");
    	}
		
		String id = request.getParameter("id");
		
		log.debug("copying a survey");
		
		Survey survey = surveyManager.getSurvey(id);
		if(survey == null) {
			postMessage(request,"survey.missing");
			return list(mapping,form,request,response);
		}
		
		Survey newSurvey = new Survey();
		newSurvey.setDateCreated(new Date());
		newSurvey.setDescription(survey.getDescription());
		newSurvey.setSurveyData(survey.getSurveyData());
		newSurvey.setUserId(new Long(surveyUserManager.getUserId(request)).intValue());
		newSurvey.setVersion(survey.getVersion());
		newSurvey.setStatus(new Short(Survey.STATUS_IN_REVIEW));
		
		surveyManager.saveSurvey(newSurvey);
		
		return list(mapping,form,request,response);
	}

	public ActionForward export_csv(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String id = request.getParameter("id");
		try {
			response.setContentType("APPLICATION/OCTET-STREAM");
			String strProjectInfoPageHeader = "Attachment;Filename=" + id + ".csv";
			response.setHeader("Content-Disposition", URLEncoder.encode(strProjectInfoPageHeader,"UTF-8") );
			this.oscarFormManager.generateCSV(Integer.valueOf(id), response.getOutputStream());
		}catch(IOException e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return null;
	}
	
	public ActionForward export_inverse_csv(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String id = request.getParameter("id");
		try {
			response.setContentType("APPLICATION/OCTET-STREAM");
			String strProjectInfoPageHeader = "Attachment;Filename=" + id + ".csv";
			response.setHeader("Content-Disposition", URLEncoder.encode(strProjectInfoPageHeader,"UTF-8"));
			this.oscarFormManager.generateInverseCSV(Integer.valueOf(id), response.getOutputStream());
		}catch(IOException e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return null;
	}
	
	public ActionForward export_to_db(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String id = request.getParameter("id");
		this.oscarFormManager.convertFormXMLToDb(Integer.valueOf(id));
		
		return list(mapping,form,request,response);
	}
	
	protected static int getUnusedSectionId(Page page) {
		int id  = 1;
		for(int x=0;x<page.getQContainerArray().length;x++) {
			if(page.getQContainerArray(x).isSetSection()) {
				if(page.getQContainerArray(x).getSection().getId() == id) {
					id++;
				} else if(page.getQContainerArray(x).getSection().getId()> id) {
					id = page.getQContainerArray(x).getSection().getId()+1;
				}
			}
		}
		return id;
	}
	
	protected void setSectionProperties(HttpServletRequest request,SurveyDocument.Survey survey, SurveyManagerFormBean formBean) {
		Enumeration e = request.getParameterNames();
		//create a uniqeu list of sectionids;
		Map<String,Boolean> ids = new HashMap<String,Boolean>();
		
        while(e.hasMoreElements()) {
        	String name = (String)e.nextElement();
        	String pageName = formBean.getPage();
        	if(name.startsWith("section_description_")) {
        		String sectionId = name.substring(name.lastIndexOf("_")+1);
        		ids.put(sectionId,true);
        		String description =request.getParameter(name);
        		
        		if(!(pageName.equalsIgnoreCase("Introduction")) && !(pageName.equalsIgnoreCase("Closing"))) {
        		Section section = SurveyModelManager.findSection(survey,formBean.getPage(),Integer.valueOf(sectionId).intValue());
        		if( section != null && request.getAttribute("updateSection")=="true") {
        			section.setDescription(description);
        			//log.debug("setting description for section " + sectionId);
        		}
        		
        		}
        	}
        	
        	if(name.startsWith("section_bold_")) {
        		String sectionId = name.substring(name.lastIndexOf("_")+1);
        		String description =request.getParameter(name);
        		
        		if(!(pageName.equalsIgnoreCase("Introduction")) && !(pageName.equalsIgnoreCase("Closing"))) {
        		Section section = SurveyModelManager.findSection(survey,formBean.getPage(),Integer.valueOf(sectionId).intValue());
        		if( section != null && request.getAttribute("updateSection")=="true") {
        			section.setBold(description);
        			//log.debug("setting description for section " + sectionId);
        		}
        		
        		}
        	}
        	
        	if(name.startsWith("section_underline_")) {
        		String sectionId = name.substring(name.lastIndexOf("_")+1);
        		String description =request.getParameter(name);
        		
        		if(!(pageName.equalsIgnoreCase("Introduction")) && !(pageName.equalsIgnoreCase("Closing"))) {
        		Section section = SurveyModelManager.findSection(survey,formBean.getPage(),Integer.valueOf(sectionId).intValue());
        		if( section != null && request.getAttribute("updateSection")=="true") {
        			section.setUnderline(description);
        			//log.debug("setting description for section " + sectionId);
        		}
        		
        		}
        	}
        	
        	if(name.startsWith("section_italics_")) {
        		String sectionId = name.substring(name.lastIndexOf("_")+1);
        		String description =request.getParameter(name);
        		
        		if(!(pageName.equalsIgnoreCase("Introduction")) && !(pageName.equalsIgnoreCase("Closing"))) {
        		Section section = SurveyModelManager.findSection(survey,formBean.getPage(),Integer.valueOf(sectionId).intValue());
        		if( section != null && request.getAttribute("updateSection")=="true") {
        			section.setItalics(description);
        			//log.debug("setting description for section " + sectionId);
        		}
        		
        		}
        	}
        	
        	if(name.startsWith("section_color_")) {
        		String sectionId = name.substring(name.lastIndexOf("_")+1);
        		String description =request.getParameter(name);
        		
        		if(!(pageName.equalsIgnoreCase("Introduction")) && !(pageName.equalsIgnoreCase("Closing"))) {
        		Section section = SurveyModelManager.findSection(survey,formBean.getPage(),Integer.valueOf(sectionId).intValue());
        		if( section != null && request.getAttribute("updateSection")=="true") {
        			section.setColor(description);
        			//log.debug("setting description for section " + sectionId);
        		}
        		
        		}
        	}
        }
        
        for(Iterator<String> iter=ids.keySet().iterator();iter.hasNext();) {
        	String key = iter.next();
        	//unset the checkboxes if necessary
        	if(request.getParameter("section_bold_" + key) == null) {
        		Section section = SurveyModelManager.findSection(survey,formBean.getPage(),Integer.valueOf(key).intValue());
        		if( section != null && request.getAttribute("updateSection")=="true") {
        			section.setBold("");
        		}
        	}
        	if(request.getParameter("section_underline_" + key) == null) {
        		Section section = SurveyModelManager.findSection(survey,formBean.getPage(),Integer.valueOf(key).intValue());
        		if( section != null && request.getAttribute("updateSection")=="true") {
        			section.setUnderline("");
        		}
        	}
        	if(request.getParameter("section_italics_" + key) == null) {
        		Section section = SurveyModelManager.findSection(survey,formBean.getPage(),Integer.valueOf(key).intValue());
        		if( section != null && request.getAttribute("updateSection")=="true") {
        			section.setItalics("");
        		}
        	}
        }
	}
	
@Deprecated	
public ActionForward getUcfReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		Long id = Long.valueOf(request.getParameter("formId"));
		
		this.oscarFormManager.convertFormXMLToDb(Long.valueOf(id).intValue());
		//request.setAttribute("ucfReports", oscarFormManager.getFormReport(id, startDate, endDate));
		
		return mapping.findForward("ucfReport");
		
	}
	
}
