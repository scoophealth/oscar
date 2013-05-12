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

package org.oscarehr.PMmodule.web.forms;

import java.io.File;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.oscarehr.PMmodule.dao.SurveySecurityDao;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.SurveyManager;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.CaisiForm;
import org.oscarehr.common.model.CaisiFormData;
import org.oscarehr.common.model.CaisiFormDataTmpSave;
import org.oscarehr.common.model.CaisiFormInstance;
import org.oscarehr.common.model.CaisiFormInstanceTmpSave;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Provider;
import org.oscarehr.survey.service.SurveyModelManager;
import org.oscarehr.survey.web.formbean.SurveyExecuteDataBean;
import org.oscarehr.survey.web.formbean.SurveyExecuteFormBean;
import org.oscarehr.surveymodel.Page;
import org.oscarehr.surveymodel.Question;
import org.oscarehr.surveymodel.SurveyDocument;
import org.oscarehr.surveymodel.SurveyDocument.Survey;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SessionConstants;
import org.oscarehr.util.SpringUtils;

public class SurveyExecuteAction extends DispatchAction {
    private static Logger log = MiscUtils.getLogger();

    private SurveyManager surveyManager = (SurveyManager)SpringUtils.getBean("surveyManager2");
    private CaseManagementManager caseManagementManager=(CaseManagementManager)SpringUtils.getBean("caseManagementManager");
    private ClientManager clientManager = (ClientManager)SpringUtils.getBean("clientManager");
    private AdmissionManager admissionManager = (AdmissionManager)SpringUtils.getBean("admissionManager");

   
    
    protected String getProviderNo(HttpServletRequest request) {
        return getProvider(request).getProviderNo();
    }

    protected Provider getProvider(HttpServletRequest request) {
        return (Provider) request.getSession().getAttribute("provider");
    }

    public ActionForward forwardToClientManager(HttpServletRequest request, ActionMapping mapping, ActionForm form, String clientId) {
        /*
          ActionForward forward =  mapping.findForward("client_manager");
          String path = forward.getPath();

          //return forward;
          ActionForward af =  new ActionForward(path + "&view.tab=Forms&id=" + clientId);
          af.setRedirect(true);
          return af;
          */
        request.setAttribute("demographicNo", clientId);
        request.setAttribute("clientId", clientId);
        request.setAttribute("tab.override", "Forms");
        ((DynaActionForm) form).reset(mapping, request);

        return mapping.findForward("success");
    }

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String clientId = request.getParameter("clientId");

        return forwardToClientManager(request, mapping, form, clientId);
    }

    protected void postMessage(HttpServletRequest request, String key, String val) {
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key, val));
        saveMessages(request, messages);
    }

    protected void postMessage(HttpServletRequest request, String key) {
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key));
        saveMessages(request, messages);
    }

    protected long getUserId(HttpServletRequest request) {
        String value = (String) request.getSession().getAttribute("user");
        return Integer.parseInt(value);
    }

    protected String getUsername(HttpServletRequest request) {
        Provider provider = (Provider) request.getSession().getAttribute("provider");
        return provider.getFormattedName();
    }

    public ActionForward survey(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
    	DynaActionForm form = (DynaActionForm) af;
    	String clientId = request.getParameter("clientId");
    	
    	SurveyExecuteFormBean formBean = (SurveyExecuteFormBean)form.get("view");
        SurveySecurityDao securityDao = new SurveySecurityDao();
        boolean hasAccess = false; 
        try {
        	hasAccess = securityDao.checkPrivilege(formBean.getDescription(),(String)request.getSession().getAttribute("user"));
        }catch(Exception e) {
        	hasAccess=false;
        }
        if(!hasAccess) {
        	String type = request.getParameter("type");
        	if (type != null && type.equals("provider")) {
        		return mapping.findForward("close");	
        	} else {
        		return forwardToClientManager(request, mapping, form, clientId);
        	}
        }
        
    	String forwardString = survey_view(mapping, af, request, response);
    	if("close".equals(forwardString))  {
        	return mapping.findForward("close");	
        }else if("clientManager".equals(forwardString)) {
        	return forwardToClientManager(request, mapping, form, clientId);
        } else {
        	return refresh(mapping, form, request, response);
        }
        
    }
    
    private String survey_view(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm form = (DynaActionForm) af;
        SurveyExecuteFormBean formBean = (SurveyExecuteFormBean) form.get("view");
        SurveyExecuteDataBean data = (SurveyExecuteDataBean) form.get("data");

        formBean.setTab("");
        data.reset();
        
        String formInstanceId = request.getParameter("formInstanceId");
        String surveyId = request.getParameter("formId");
        String clientId = request.getParameter("clientId");
        String type = request.getParameter("type");
        request.setAttribute("type", type);
        request.getSession().setAttribute("formInstanceId",formInstanceId);
        
        if (surveyId == null) {
            surveyId = String.valueOf(formBean.getId());
            if (surveyId == null || surveyId.equals("0")) {
                postMessage(request, "survey.missing");
                if (type != null && type.equals("provider")) {
                    //return mapping.findForward("close");
                	return "close";
                }
                //return forwardToClientManager(request, mapping, form, clientId);
                return "clientManager";
            }
        }
        if (clientId == null) {
            clientId = String.valueOf(formBean.getClientId());
        }

        formBean.setId(Integer.parseInt(surveyId));
        formBean.setClientId(Integer.parseInt(clientId));
        CaisiForm surveyObj = surveyManager.getForm(surveyId);
        if (surveyObj == null) {
            postMessage(request, "survey.missing");
            if (type != null && type.equals("provider")) {
                //return mapping.findForward("close");
            	return "close";
            }
            //return forwardToClientManager(request, mapping, form, clientId);
            return "clientManager";
        }

        formBean.setDescription(surveyObj.getDescription());
        String descr = surveyObj.getDescription();
        descr = descr.replaceAll(" ", "_");
        descr = descr.toLowerCase();
        String path = request.getSession().getServletContext().getRealPath("/");
        path = path + "survey" + File.separator + "scripts" + File.separator + descr + ".js";

        if (new File(path).exists()) {
            request.getSession().setAttribute("validation_file", descr);
        } else {
            request.getSession().setAttribute("validation_file", null);
        }

        log.debug("executing survey " + surveyObj.getId());

        SurveyDocument model = null;
        try {
            String xml = surveyObj.getSurveyData();
            model = SurveyDocument.Factory.parse(new StringReader(xml));
            request.getSession().setAttribute("model", model);
        } catch (Exception e) {
            log.error("Error", e);
            postMessage(request, "");
            if (type != null && type.equals("provider")) {
                //return mapping.findForward("close");
            	return "close";
            }
            //return forwardToClientManager(request, mapping, form, clientId);
            return "clientManager";
        }

        /* load test/tmpsave data - if exists */
        CaisiFormInstance instance = null;
        if (!clientId.equals("0")) {
            //instance = surveyManager.getLatestForm(surveyId, clientId);
        	List tmpInstances = surveyManager.getTmpForms(formInstanceId, surveyId, clientId, String.valueOf(getUserId(request)));
        	if(tmpInstances.size()==0 || tmpInstances == null) {
        		instance = surveyManager.getCurrentFormById(formInstanceId);
        	} else {
        		CaisiFormInstanceTmpSave tmpsave = (CaisiFormInstanceTmpSave)tmpInstances.get(0);
            	instance = new CaisiFormInstance();
        		instance.setClientId(tmpsave.getClientId());
                instance.setDateCreated(new Date());
                instance.setFormId(tmpsave.getFormId());
                instance.setUserId(tmpsave.getUserId());
                instance.setDescription(tmpsave.getDescription());
                instance.setUsername(tmpsave.getUsername());
                
                //instance.setData(tmpsave.getData()); //CaisiFormDataTmpSave???
                org.oscarehr.surveymodel.SurveyDocument.Survey surveyModel = surveyManager.getFormModel(String.valueOf(formBean.getId()));
                List qids = SurveyModelManager.getAllQuestionIds(surveyModel);
            	for (Iterator iter = qids.iterator(); iter.hasNext();) {
            		String key = (String) iter.next();            		
            		String[] parsed = key.split("_");
            		String pageNumber = parsed[0];
            		String sectionId = parsed[1];
            		String questionId = parsed[2];              
          
            		CaisiFormData dataItem = new CaisiFormData();
            		dataItem.setPageNumber(Integer.parseInt(pageNumber));
            		dataItem.setSectionId(Integer.parseInt(sectionId));
            		dataItem.setQuestionId(Integer.parseInt(questionId));
            		//dataItem.setValue((String)data.getValue(key));//?????
            		Set data_tmp = tmpsave.getData();
            		for(Iterator it = data_tmp.iterator(); it.hasNext();) {
            			CaisiFormDataTmpSave oneItem = (CaisiFormDataTmpSave)it.next();
            			if(key.equals(oneItem.getDataKey())) {
            				dataItem.setValue(oneItem.getValue());
            			}
            		}  		
            		dataItem.setDataKey(key);            		
            		instance.getData().add(dataItem);
            	} 
                
        	}       	
        }
        if (instance != null) {
            log.debug("loading up existing data");
            for (Iterator iter = instance.getData().iterator(); iter.hasNext();) {
                CaisiFormData dataItem = (CaisiFormData) iter.next();
                String key = dataItem.getDataKey();
                if (SurveyModelManager.isCheckbox(model.getSurvey(), key)) {
                    String value = dataItem.getValue();
                    if (value != null) {
                        data.getValues().put("checkbox_" + key, "checked");
                    } else {
                        data.getValues().put("checkbox_" + key, "");
                    }
                }
                data.getValues().put(key, dataItem.getValue());
            }
        }

        //find caisi objects & data links
        for (int x = 0; x < model.getSurvey().getBody().getPageArray().length; x++) {
            Page page = model.getSurvey().getBody().getPageArray(x);
            int sectionNum = 0;
            int pageNum = x + 1;
            int questionNum = 0;

            for (Page.QContainer container : page.getQContainerArray()) {
                if (container.isSetSection()) {
                    sectionNum++;
                    int questionNum2 = 0;
                    for (Question question : container.getSection().getQuestionArray()) {
                        questionNum2++;
                        if (question.getCaisiObject() != null && question.getCaisiObject().length() > 0) {
                            String caisiObject = question.getCaisiObject();
                            log.debug("FOUND CAISI-OBJECT: " + pageNum + "_" + sectionNum + "_" + questionNum2 + " " + caisiObject);
                            populateWithCaisiObject(request, data, pageNum + "_" + sectionNum + "_" + questionNum2, caisiObject, clientId);
                        }

                        if (question.getDataLink() != null && question.getDataLink().length() > 0) {
                            //load data
                            String format = null;
                            if (question.getType().isSetDate()) {
                                format = question.getType().getDate().toString();
                            }
                            populateWithDataLink(data, pageNum + "_" + sectionNum + "_" + questionNum2, question.getDataLink(), clientId, format, false, 0);
                        }
                    }
                } else if (container.isSetQuestion()) {
                    Question question = container.getQuestion();
                    questionNum++;
                    if (question.getCaisiObject() != null && question.getCaisiObject().length() > 0) {
                        String caisiObject = question.getCaisiObject();
                        log.debug("FOUND CAISI-OBJECT: " + pageNum + "_" + 0 + "_" + questionNum + " " + caisiObject);
                        populateWithCaisiObject(request, data, pageNum + "_" + 0 + "_" + questionNum, caisiObject, clientId);
                    }

                    if (question.getDataLink() != null && question.getDataLink().length() > 0) {
                        String format = null;
                        if (question.getType().isSetDate()) {
                            format = question.getType().getDate().toString();
                        }

                        populateWithDataLink(data, pageNum + "_" + 0 + "_" + questionNum, question.getDataLink(), clientId, format, false, 0);
                    }
                }
            }
        }
        //return refresh(mapping, form, request, response);
        return "success";
    }
    
    public ActionForward refresh(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
    	refresh_survey(mapping,  af, request, response);
    	return mapping.findForward("execute");
    }
    
    public void refresh_survey(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm form = (DynaActionForm) af;
        SurveyExecuteFormBean formBean = (SurveyExecuteFormBean) form.get("view");
        SurveyExecuteDataBean data = (SurveyExecuteDataBean) form.get("data");

        SurveyDocument model = (SurveyDocument) request.getSession().getAttribute("model");

        String type = request.getParameter("type");
        request.setAttribute("type", type);


        Survey survey = model.getSurvey();
        Page[] pages = survey.getBody().getPageArray();
        List pageNames = new ArrayList();

        if (formBean.getAdmissionId() > 0) {
            log.debug("ADMISSION ID=" + formBean.getAdmissionId());
        }

        for (int x = 0; x < model.getSurvey().getBody().getPageArray().length; x++) {
            Page page = model.getSurvey().getBody().getPageArray(x);
            int sectionNum = 0;
            int pageNum = x + 1;
            int questionNum = 0;

            for (Page.QContainer container : page.getQContainerArray()) {
                if (container.isSetSection()) {
                    sectionNum++;
                    int questionNum2 = 0;
                    for (Question question : container.getSection().getQuestionArray()) {
                        questionNum2++;
                        if (question.getDataLink() != null && question.getDataLink().length() > 0) {
                            //load data
                            String format = null;
                            if (question.getType().isSetDate()) {
                                format = question.getType().getDate().toString();
                            }
                            populateWithDataLink(data, pageNum + "_" + sectionNum + "_" + questionNum2, question.getDataLink(), String.valueOf(formBean.getClientId()), format, true, formBean.getAdmissionId());
                        }
                    }
                } else if (container.isSetQuestion()) {
                    Question question = container.getQuestion();
                    questionNum++;
                    if (question.getDataLink() != null && question.getDataLink().length() > 0) {
                        String format = null;
                        if (question.getType().isSetDate()) {
                            format = question.getType().getDate().toString();
                        }

                        populateWithDataLink(data, pageNum + "_" + 0 + "_" + questionNum, question.getDataLink(), String.valueOf(formBean.getClientId()), format, true, formBean.getAdmissionId());
                    }
                }
            }
        }

        if (survey.getIntroduction() != null && !survey.getIntroduction().getIncludeOnFirstPage()) {
            pageNames.add("Introduction");
            if (formBean.getTab() == null || formBean.getTab().length() == 0) {
                //default first page
                request.setAttribute("introduction", survey.getIntroduction());
                request.setAttribute("currentTab", "Introduction");
                log.debug("showing page: introduction");
            }

            if (formBean.getTab() != null && formBean.getTab().equals("Introduction")) {
                request.setAttribute("introduction", survey.getIntroduction());
                request.setAttribute("currentTab", "Introduction");

                log.debug("showing page: introduction");
            }
        } else {
            //default first page is page1
            if (formBean.getTab() == null || formBean.getTab().length() == 0) {
                formBean.setTab(pages[0].getDescription());
                request.setAttribute("currentTab", pages[0].getDescription());

            }
        }

        for (int x = 0; x < pages.length; x++) {
            Page tmp = pages[x];
            pageNames.add(tmp.getDescription());
            if (formBean.getTab() != null && tmp.getDescription().equals(formBean.getTab())) {
                request.setAttribute("page", tmp);
                request.setAttribute("pageNumber", String.valueOf(x + 1));
                request.setAttribute("currentTab", tmp.getDescription());
                if (survey.getIntroduction() != null && survey.getIntroduction().getIncludeOnFirstPage() && x == 0) {
                    request.setAttribute("introduction", survey.getIntroduction());
                }
                if (survey.getClosing() != null && survey.getClosing().getIncludeOnLastPage() && x == (pages.length - 1)) {
                    request.setAttribute("closing", survey.getClosing());
                }
                log.debug("showing page: " + tmp.getDescription());
            }
        }

        if (survey.getClosing() != null && !survey.getClosing().getIncludeOnLastPage()) {
            pageNames.add("Closing");
            if (formBean.getTab() != null && formBean.getTab().equals("Closing")) {
                request.setAttribute("closing", survey.getClosing());
                request.setAttribute("currentTab", "Closing");

                log.debug("showing page: closing");
            }
        }


        request.setAttribute("tabs", pageNames);

        if (formBean.getClientId() > 0) {
            List admissions = admissionManager.getCurrentAdmissions(new Integer((int) formBean.getClientId()));
            request.setAttribute("admissions", admissions);
        }

        //return mapping.findForward("execute");
    }

    public ActionForward save_survey(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm form = (DynaActionForm) af;
        SurveyExecuteFormBean formBean = (SurveyExecuteFormBean) form.get("view");

        String type = request.getParameter("type");        
        String formInstanceId = (String)request.getSession().getAttribute("formInstanceId");
        
        if (this.isCancelled(request)) {
            if (type != null && type.equals("provider")) {
                return mapping.findForward("close");
            }
            return forwardToClientManager(request, mapping, form, String.valueOf(formBean.getClientId()));
        }

        log.debug("calling save() on action");
        SurveyExecuteDataBean data = (SurveyExecuteDataBean) form.get("data");
        org.oscarehr.surveymodel.SurveyDocument.Survey surveyModel = surveyManager.getFormModel(String.valueOf(formBean.getId()));

        CaisiFormInstance instance = new CaisiFormInstance();
        instance.setClientId((int)formBean.getClientId());
        instance.setDateCreated(new Date());
        instance.setFormId((int)formBean.getId());
        instance.setUserId((int)getUserId(request));
        instance.setDescription(formBean.getDescription());
        instance.setUsername(getUsername(request));

        /* fix the checkboxes */
        Map test = new HashMap();

        for (Iterator iter = data.getValues().keySet().iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            if (key.startsWith("checkbox_")) {
                //found a hidden element related to a checkbox
                String realKey = key.substring(9);
                String value = (String) data.getValues().get(key);
                if (value.equals("checked")) {

                } else {
                    test.put(realKey, null);
                    //data.getValues().put(realKey,null);
                }
            }


        }
        data.getValues().putAll(test);

        /* convert the data form bean */
        List qids = SurveyModelManager.getAllQuestionIds(surveyModel);
        for (Iterator iter = qids.iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            //log.debug("key=" + key + ",value=" + (String)data.getValue(key));
            String[] parsed = key.split("_");
            String pageNumber = parsed[0];
            String sectionId = parsed[1];
            String questionId = parsed[2];

            CaisiFormData dataItem = new CaisiFormData();
            dataItem.setPageNumber(Integer.parseInt(pageNumber));
            dataItem.setSectionId(Integer.parseInt(sectionId));
            dataItem.setQuestionId(Integer.parseInt(questionId));
            dataItem.setValue((String) data.getValue(key));
            dataItem.setDataKey(key);
            instance.getData().add(dataItem);
        }


        SurveyDocument model = (SurveyDocument) request.getSession().getAttribute("model");
        //find caisi objects & data links
        for (int x = 0; x < model.getSurvey().getBody().getPageArray().length; x++) {
            Page page = model.getSurvey().getBody().getPageArray(x);
            int sectionNum = 0;
            int pageNum = x + 1;
            int questionNum = 0;

            for (Page.QContainer container : page.getQContainerArray()) {
                if (container.isSetSection()) {
                    sectionNum++;
                    int questionNum2 = 0;
                    for (Question question : container.getSection().getQuestionArray()) {
                        questionNum2++;
                        if (question.getDataLink() != null && question.getDataLink().length() > 0) {
                            //load data
                            String format = null;
                            if (question.getType().isSetDate()) {
                                format = question.getType().getDate().toString();
                            }
                            saveDataLink(data, pageNum + "_" + sectionNum + "_" + questionNum2, question.getDataLink(), instance.getClientId(), format);
                        }
                    }
                } else if (container.isSetQuestion()) {
                    Question question = container.getQuestion();
                    questionNum++;
                    if (question.getDataLink() != null && question.getDataLink().length() > 0) {
                        String format = null;
                        if (question.getType().isSetDate()) {
                            format = question.getType().getDate().toString();
                        }

                        saveDataLink(data, pageNum + "_" + 0 + "_" + questionNum, question.getDataLink(), instance.getClientId(), format);
                    }
                }
            }
        }

        // if the form was saved in the context of a program
        String programIdStr = (String) request.getSession().getAttribute(SessionConstants.CURRENT_PROGRAM_ID);
        if (programIdStr!=null) instance.setProgramId(Integer.valueOf(programIdStr));        
        
        surveyManager.saveFormInstance(instance);
        
        //If saving survey succeed, then delete tmpsave record.
        //List tmpInstanceForms = surveyManager.getTmpForms(formInstanceId, String.valueOf(formBean.getId()),String.valueOf(formBean.getClientId()), String.valueOf(getUserId(request)));
        surveyManager.deleteTmpsave(formInstanceId, String.valueOf(formBean.getId()),String.valueOf(formBean.getClientId()), String.valueOf(getUserId(request)));
        
        form.set("data", new SurveyExecuteDataBean());
        form.set("view", new SurveyExecuteFormBean());

        if (type != null && type.equals("provider")) {
            return mapping.findForward("close");
        }
        return forwardToClientManager(request, mapping, form, String.valueOf(formBean.getClientId()));
        //request.setAttribute("survey_saved",new Boolean(true));
        //request.setAttribute("clientId",String.valueOf(formBean.getDemographicId()));
        //return mapping.findForward("client_manager");
    }

    public static String getCalendarFormat(String val) {
        if (val.equals("yyyy-mm-dd")) {
            return "%Y-%m-%d";
        }
        if (val.equals("yyyy/mm/dd")) {
            return "%Y/%m/%d";
        }
        if (val.equals("dd/mm/yy")) {
            return "%d/%m/%y";
        }
        if (val.equals("mm/dd/yy")) {
            return "%m/%d/%y";
        }

        return "%Y-%m-%d";
    }

    private void populateWithCaisiObject(HttpServletRequest request, SurveyExecuteDataBean data, String key, String caisiObject, String demographic_no) {
        log.debug("CaisObject=" + caisiObject);

        if (caisiObject.equals("Current Medications")) {
            List<Drug> meds = caseManagementManager.getPrescriptions(demographic_no, true);
            StringBuilder str = new StringBuilder();
            for (Drug med : meds) {
                str.append(med.getSpecial().replaceAll("\r\n", " ")).append("\r\n");
            }
            data.getValues().put(key, str.toString());
        } else if (caisiObject.equals("Current Issues")) {
            List issues = caseManagementManager.getIssues(Integer.parseInt(demographic_no), false);
            StringBuilder str = new StringBuilder();
            for (Iterator iter = issues.iterator(); iter.hasNext();) {
                CaseManagementIssue issue = (CaseManagementIssue) iter.next();
                str.append(issue.getIssue().getDescription()).append("\r\n");
            }
            data.getValues().put(key, str.toString());
        } else if (caisiObject.equals("Allergies")) {
            List allergies = caseManagementManager.getAllergies(demographic_no);
            StringBuilder str = new StringBuilder();
            for (Iterator iter = allergies.iterator(); iter.hasNext();) {
                Allergy med = (Allergy) iter.next();
                str.append(med.getDescription()).append(" ").append(med.getReaction()).append("\r\n");
            }
            data.getValues().put(key, str.toString());
        } else if (caisiObject.equals("Program Selector")) {
            log.debug("SETTING CURRENT ADMISSIONS");
            List admissions = admissionManager.getCurrentAdmissions(new Integer(demographic_no));
            request.setAttribute("admissions", admissions);
        }
    }

    public void populateWithDataLink(SurveyExecuteDataBean data, String key, String dataLink, String demographic_no, String format, boolean refresh, long admissionId) {
        //will make more dynamic in the future
        Demographic client = this.clientManager.getClientByDemographicNo(demographic_no);

        if (!refresh) {
            if (dataLink.equals("Demographic/FirstName")) {
                data.getValues().put(key, client.getFirstName());
            } else if (dataLink.equals("Demographic/LastName")) {
                data.getValues().put(key, client.getLastName());
            } else if (dataLink.equals("Demographic/birthDate")) {
                format = format.replaceAll("mm", "MM");
        		SimpleDateFormat formatter = new SimpleDateFormat(format);
        		Calendar cal = client.getBirthDay();
        		String temp=formatter.format(cal.getTime());
                data.getValues().put(key, temp);
            }
        } else {
            if (admissionId > 0) {
                if (dataLink.equals("Program/admissionDate")) {
                    Admission admission = admissionManager.getAdmission(admissionId);
                    format = format.replaceAll("mm", "MM");
                    data.getValues().put(key, admission.getAdmissionDate(format));
                }
                if (dataLink.equals("Program/admissionNotes")) {
                    if (admissionId > 0) {
                        Admission admission = admissionManager.getAdmission(admissionId);
                        data.getValues().put(key, admission.getAdmissionNotes());
                    }
                }
            }
        }
    }

    public void saveDataLink(SurveyExecuteDataBean data, String key, String dataLink, long demographic_no, String format) {
        Demographic client = this.clientManager.getClientByDemographicNo(String.valueOf(demographic_no));

        if (dataLink.equals("Demographic/FirstName")) {
            String value = (String) data.getValues().get(key);
            client.setFirstName(value);
        } else if (dataLink.equals("Demographic/LastName")) {
            String value = (String) data.getValues().get(key);
            client.setLastName(value);
        } else if (dataLink.equals("Demographic/birthDate")) {
            try {
	            format = format.replaceAll("mm", "MM");
	            SimpleDateFormat formatter = new SimpleDateFormat(format);

	            String value = (String) data.getValues().get(key);
	            Date d = formatter.parse(value);
	            Calendar cal = Calendar.getInstance();
	            cal.setTime(d);
	            
	            client.setBirthDay(cal);
            } catch (ParseException e) {
	            log.error("Error", e);
            }
        }
        clientManager.saveClient(client);
    }
    
    public ActionForward tmpsave_survey(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm form = (DynaActionForm) af;
        SurveyExecuteFormBean formBean = (SurveyExecuteFormBean) form.get("view");

        String type = request.getParameter("type");
        String formInstanceId = (String)request.getSession().getAttribute("formInstanceId");
        
        if (this.isCancelled(request)) {
            if (type != null && type.equals("provider")) {
                return mapping.findForward("close");
            }
            return forwardToClientManager(request, mapping, form, String.valueOf(formBean.getClientId()));
        }

        log.debug("calling save() on action");
        SurveyExecuteDataBean data = (SurveyExecuteDataBean) form.get("data");
        org.oscarehr.surveymodel.SurveyDocument.Survey surveyModel = surveyManager.getFormModel(String.valueOf(formBean.getId()));

        //check instanceId,formId,clientId, and userId to see if tmp form instance exists :
        CaisiFormInstanceTmpSave instance = new CaisiFormInstanceTmpSave();
        boolean newForm;
        List tmpInstanceForms = surveyManager.getTmpForms(formInstanceId, String.valueOf(formBean.getId()),String.valueOf(formBean.getClientId()), String.valueOf(getUserId(request)));
        if(tmpInstanceForms.size()==0 || tmpInstanceForms==null ) {
        	newForm = true;
        	instance.setInstanceId(Integer.valueOf(formInstanceId));
        	instance.setClientId((int)formBean.getClientId());
            instance.setDateCreated(new Date());
            instance.setFormId((int)formBean.getId());
            instance.setUserId((int)getUserId(request));
            instance.setDescription(formBean.getDescription());
            instance.setUsername(getUsername(request));
        } else {        	
        	newForm = false;
        	instance = (CaisiFormInstanceTmpSave)tmpInstanceForms.get(0);
              	
        }        

        /* fix the checkboxes */
        Map test = new HashMap();

        for (Iterator iter = data.getValues().keySet().iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            if (key.startsWith("checkbox_")) {
                //found a hidden element related to a checkbox
                String realKey = key.substring(9);
                String value = (String) data.getValues().get(key);
                if (value.equals("checked")) {

                } else {
                    test.put(realKey, null);
                    //data.getValues().put(realKey,null);
                }
            }


        }
        data.getValues().putAll(test);     
        
        if(newForm) {
        	/* convert the data form bean */
        	List qids = SurveyModelManager.getAllQuestionIds(surveyModel);
        	for (Iterator iter = qids.iterator(); iter.hasNext();) {
        		String key = (String) iter.next();
        		//log.debug("key=" + key + ",value=" + (String)data.getValue(key));
        		String[] parsed = key.split("_");
        		String pageNumber = parsed[0];
        		String sectionId = parsed[1];
        		String questionId = parsed[2];              
      
        		CaisiFormDataTmpSave dataItem = new CaisiFormDataTmpSave();
        		dataItem.setPageNumber(Integer.parseInt(pageNumber));
        		dataItem.setSectionId(Integer.parseInt(sectionId));
        		dataItem.setQuestionId(Integer.parseInt(questionId));
        		dataItem.setValue((String) data.getValue(key));
        		dataItem.setDataKey(key);
        		instance.getData().add(dataItem);
        	} 
        } else {
        		instance.getData().clear();
            	List tmpFormData = surveyManager.getTmpFormData(String.valueOf(instance.getId()));
            	for(Iterator it = tmpFormData.iterator(); it.hasNext();) {
            		CaisiFormDataTmpSave formData = (CaisiFormDataTmpSave)it.next();
            		formData.setValue((String)data.getValue(formData.getDataKey()));
            		instance.getData().add(formData);
            	}
        }	        

        SurveyDocument model = (SurveyDocument) request.getSession().getAttribute("model");
        //find caisi objects & data links
        for (int x = 0; x < model.getSurvey().getBody().getPageArray().length; x++) {
            Page page = model.getSurvey().getBody().getPageArray(x);
            int sectionNum = 0;
            int pageNum = x + 1;
            int questionNum = 0;

            for (Page.QContainer container : page.getQContainerArray()) {
                if (container.isSetSection()) {
                    sectionNum++;
                    int questionNum2 = 0;
                    for (Question question : container.getSection().getQuestionArray()) {
                        questionNum2++;
                        if (question.getDataLink() != null && question.getDataLink().length() > 0) {
                            //load data
                            String format = null;
                            if (question.getType().isSetDate()) {
                                format = question.getType().getDate().toString();
                            }
                            saveDataLink(data, pageNum + "_" + sectionNum + "_" + questionNum2, question.getDataLink(), instance.getClientId(), format);
                        }
                    }
                } else if (container.isSetQuestion()) {
                    Question question = container.getQuestion();
                    questionNum++;
                    if (question.getDataLink() != null && question.getDataLink().length() > 0) {
                        String format = null;
                        if (question.getType().isSetDate()) {
                            format = question.getType().getDate().toString();
                        }

                        saveDataLink(data, pageNum + "_" + 0 + "_" + questionNum, question.getDataLink(), instance.getClientId(), format);
                    }
                }
            }
        }

        // if the form was saved in the context of a program
        String programIdStr = (String) request.getSession().getAttribute(SessionConstants.CURRENT_PROGRAM_ID);
        if (programIdStr!=null) instance.setProgramId(Integer.valueOf(programIdStr));        

        surveyManager.saveFormInstanceTmpsave(instance);

        if (type != null && type.equals("provider")) {
            return mapping.findForward("close");
        }
        
        return refresh(mapping, form, request, response);
    }

    public ActionForward printPreview_survey(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
        
    	DynaActionForm form = (DynaActionForm) af;
    	String clientId = request.getParameter("clientId");
    	String forwardString = survey_view(mapping, af, request, response);
    	if("close".equals(forwardString))  {
        	return mapping.findForward("close");	
        }else if("clientManager".equals(forwardString)) {
        	return forwardToClientManager(request, mapping, form, clientId);
        } else {
        	refresh_survey(mapping,  af, request, response);        	
        	return mapping.findForward("printPreview");
        }
    }
    
    public ActionForward printPreview_refresh(ActionMapping mapping, ActionForm af, HttpServletRequest request, HttpServletResponse response) {
    	refresh_survey(mapping,  af, request, response);
    	return mapping.findForward("printPreview");
    }
}
