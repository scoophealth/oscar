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
 * ProviderPropertyAction.java
 *
 * Created on December 20, 2007, 11:44 AM
 *
 *
 *
 */

package org.oscarehr.provider.web;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserProperty;
import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil;

/**
 *
 * @author rjonasz
 */
public class ProviderPropertyAction extends DispatchAction {

    private UserPropertyDAO userPropertyDAO;
    
    public void setUserPropertyDAO(UserPropertyDAO dao) {
        this.userPropertyDAO = dao;
        
    }
    
    public ActionForward unspecified(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        
        return view(actionmapping, actionform, request, response);
    }

    public ActionForward remove(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

         DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = (UserProperty)frm.get("dateProperty"); 
         
         frm.reset(actionmapping,request);
         this.userPropertyDAO.delete(prop);
         
         request.setAttribute("status", "success");
         
         return actionmapping.findForward("success");        
    }    
    
    public ActionForward view(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

         DynaActionForm frm = (DynaActionForm)actionform;
         String provider = request.getParameter("provider_no");
         UserProperty prop = this.userPropertyDAO.getProp(provider, UserProperty.STALE_NOTEDATE);
         
         if( prop == null ) {             
             prop = new UserProperty();
             prop.setProvider_no(provider);
             prop.setName(UserProperty.STALE_NOTEDATE);
         }
         
         frm.set("dateProperty", prop);
         
         return actionmapping.findForward("success");
     }

    
    public ActionForward save(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        
         DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = (UserProperty)frm.get("dateProperty");         
         
         this.userPropertyDAO.saveProp(prop);
         
         request.setAttribute("status", "success");
         
         return actionmapping.findForward("success");
     }
    
    
    /////
    
    public ActionForward viewMyDrugrefId(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {
                               
         DynaActionForm frm = (DynaActionForm)actionform;
         String provider = (String) request.getSession().getAttribute("user");
         //System.out.println("provider # "+provider);
         UserProperty prop = this.userPropertyDAO.getProp(provider, UserProperty.MYDRUGREF_ID);
         //String propertyToSet = "";
         //if( prop != null ) {             
         //   propertyToSet = prop.getValue();
         //    System.out.println("prop was not null "+prop.getValue());
         //}else{
         //    prop = new UserProperty();
         //    System.out.println("PROP WAS NULL");
         //}
         
         if (prop == null){
             prop = new UserProperty();
         }
         
         //request.setAttribute("propert",propertyToSet);
         request.setAttribute("dateProperty",prop);
         
         
         request.setAttribute("providertitle","provider.setmyDrugrefId.title"); //=Set myDrugref ID
         request.setAttribute("providermsgPrefs","provider.setmyDrugrefId.msgPrefs"); //=Preferences"); //
         request.setAttribute("providermsgProvider","provider.setmyDrugrefId.msgProvider"); //=myDrugref ID
         request.setAttribute("providermsgEdit","provider.setmyDrugrefId.msgEdit"); //=Enter your desired login for myDrugref
         request.setAttribute("providerbtnSubmit","provider.setmyDrugrefId.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setmyDrugrefId.msgSuccess"); //=myDrugref Id saved
         request.setAttribute("method","saveMyDrugrefId");
         
         frm.set("dateProperty", prop);
         return actionmapping.findForward("gen");
     }

    
    public ActionForward saveMyDrugrefId(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {
         String provider = (String) request.getSession().getAttribute("user");
         //System.out.println("provider # "+provider);
         DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty  UdrugrefId = (UserProperty)frm.get("dateProperty");         
         String drugrefId = "";

         if (UdrugrefId != null){
             drugrefId = UdrugrefId.getValue();
         }   
         
         UserProperty prop = this.userPropertyDAO.getProp(provider, UserProperty.MYDRUGREF_ID);
         
         if (prop ==null){
             prop = new UserProperty();
             prop.setName(UserProperty.MYDRUGREF_ID);
             prop.setProvider_no(provider);
         }
         prop.setValue(drugrefId);
         
         this.userPropertyDAO.saveProp(prop);
         
         request.setAttribute("status", "success");
         request.setAttribute("dateProperty",prop);
         request.setAttribute("providertitle","provider.setmyDrugrefId.title"); //=Set myDrugref ID
         request.setAttribute("providermsgPrefs","provider.setmyDrugrefId.msgPrefs"); //=Preferences"); //
         request.setAttribute("providermsgProvider","provider.setmyDrugrefId.msgProvider"); //=myDrugref ID
         request.setAttribute("providermsgEdit","provider.setmyDrugrefId.msgEdit"); //=Enter your desired login for myDrugref
         request.setAttribute("providerbtnSubmit","provider.setmyDrugrefId.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setmyDrugrefId.msgSuccess"); //=myDrugref Id saved
         request.setAttribute("method","saveMyDrugrefId");
         return actionmapping.findForward("gen");
     }
    /////
    
    
    public ActionForward viewConsultationRequestCuffOffDate(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {
                               
         DynaActionForm frm = (DynaActionForm)actionform;
         String provider = (String) request.getSession().getAttribute("user");
         UserProperty prop = this.userPropertyDAO.getProp(provider, UserProperty.CONSULTATION_TIME_PERIOD_WARNING);
         
         if (prop == null){
             prop = new UserProperty();
         }
         
         request.setAttribute("dateProperty",prop);
         
         request.setAttribute("providertitle","provider.setConsultationCutOffDate.title"); //=Set myDrugref ID
         request.setAttribute("providermsgPrefs","provider.setConsultationCutOffDate.msgPrefs"); //=Preferences"); //
         request.setAttribute("providermsgProvider","provider.setConsultationCutOffDate.msgProvider"); //=myDrugref ID
         request.setAttribute("providermsgEdit","provider.setConsultationCutOffDate.msgEdit"); //=Enter your desired login for myDrugref
         request.setAttribute("providerbtnSubmit","provider.setConsultationCutOffDate.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setConsultationCutOffDate.msgSuccess"); //=myDrugref Id saved
         request.setAttribute("method","saveConsultationRequestCuffOffDate");
         
         frm.set("dateProperty", prop);
         return actionmapping.findForward("gen");
     }

    
    public ActionForward saveConsultationRequestCuffOffDate(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {
         String provider = (String) request.getSession().getAttribute("user");
         //System.out.println("provider # "+provider);
         DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty  UdrugrefId = (UserProperty)frm.get("dateProperty");         
         String drugrefId = "";

         if (UdrugrefId != null){
             drugrefId = UdrugrefId.getValue();
         }   
         
         UserProperty prop = this.userPropertyDAO.getProp(provider, UserProperty.CONSULTATION_TIME_PERIOD_WARNING);
         
         if (prop ==null){
             prop = new UserProperty();
             prop.setName(UserProperty.CONSULTATION_TIME_PERIOD_WARNING);
             prop.setProvider_no(provider);
         }
         prop.setValue(drugrefId);
         
         this.userPropertyDAO.saveProp(prop);
         
         request.setAttribute("status", "success");
         request.setAttribute("dateProperty",prop);
         request.setAttribute("providertitle","provider.setConsultationCutOffDate.title"); //=Set myDrugref ID
         request.setAttribute("providermsgPrefs","provider.setConsultationCutOffDate.msgPrefs"); //=Preferences"); //
         request.setAttribute("providermsgProvider","provider.setConsultationCutOffDate.msgProvider"); //=myDrugref ID
         request.setAttribute("providermsgEdit","provider.setConsultationCutOffDate.msgEdit"); //=Enter your desired login for myDrugref
         request.setAttribute("providerbtnSubmit","provider.setConsultationCutOffDate.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setConsultationCutOffDate.msgSuccess"); //=myDrugref Id saved
         request.setAttribute("method","saveConsultationRequestCuffOffDate");
         return actionmapping.findForward("gen");
     }
    
    
    
    
    //// CONSULT TEAM 
    
    public ActionForward viewConsultationRequestTeamWarning(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {
                               
         DynaActionForm frm = (DynaActionForm)actionform;
         String provider = (String) request.getSession().getAttribute("user");
         UserProperty prop = this.userPropertyDAO.getProp(provider, UserProperty.CONSULTATION_TEAM_WARNING);
         
         if (prop == null){
             prop = new UserProperty();
         }
         
         EctConsultationFormRequestUtil conUtil = new EctConsultationFormRequestUtil();
         conUtil.estTeams();
         Vector<String> vect = conUtil.teamVec;
         
         ArrayList serviceList = new ArrayList();
         serviceList.add(new  LabelValueBean("All","-1"));
         for (String s: vect ){
                 serviceList.add(new LabelValueBean(s,s));
         }   
         serviceList.add(new  LabelValueBean("None",""));
         
         
         
         //conUtil.teamVec.add("All");
         //conUtil.teamVec.add("None");
//         System.out.println("cont size "+conUtil.teamVec.size());
         request.setAttribute("dropOpts",serviceList);
         
         request.setAttribute("dateProperty",prop);
         
         request.setAttribute("providertitle","provider.setConsultationTeamWarning.title"); //=Set myDrugref ID
         request.setAttribute("providermsgPrefs","provider.setConsultationTeamWarning.msgPrefs"); //=Preferences"); //
         request.setAttribute("providermsgProvider","provider.setConsultationTeamWarning.msgProvider"); //=myDrugref ID
         request.setAttribute("providermsgEdit","provider.setConsultationTeamWarning.msgEdit"); //=Enter your desired login for myDrugref
         request.setAttribute("providerbtnSubmit","provider.setConsultationTeamWarning.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setConsultationTeamWarning.msgSuccess"); //=myDrugref Id saved
         request.setAttribute("method","saveConsultationRequestTeamWarning");
         
         frm.set("dateProperty", prop);
         return actionmapping.findForward("gen");
     }

    
    public ActionForward saveConsultationRequestTeamWarning(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {
         String provider = (String) request.getSession().getAttribute("user");
         //System.out.println("provider # "+provider);
         DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty  UdrugrefId = (UserProperty)frm.get("dateProperty");         
         String drugrefId = "";

         if (UdrugrefId != null){
             drugrefId = UdrugrefId.getValue();
         }   
         
         UserProperty prop = this.userPropertyDAO.getProp(provider, UserProperty.CONSULTATION_TEAM_WARNING);
         
         
         
         if (prop ==null){
             prop = new UserProperty();
             prop.setName(UserProperty.CONSULTATION_TEAM_WARNING);
             prop.setProvider_no(provider);
         }
         prop.setValue(drugrefId);
         
         this.userPropertyDAO.saveProp(prop);
         
         
         EctConsultationFormRequestUtil conUtil = new EctConsultationFormRequestUtil();
         conUtil.estTeams();
         Vector<String> vect = conUtil.teamVec;
         
         ArrayList serviceList = new ArrayList();
         serviceList.add(new  LabelValueBean("All","-1"));
         for (String s: vect ){
                 serviceList.add(new LabelValueBean(s,s));
         }   
         serviceList.add(new  LabelValueBean("None",""));
         request.setAttribute("dropOpts",serviceList);
         
         
         request.setAttribute("status", "success");
         request.setAttribute("dateProperty",prop);
         request.setAttribute("providertitle","provider.setConsultationTeamWarning.title"); //=Set myDrugref ID
         request.setAttribute("providermsgPrefs","provider.setConsultationTeamWarning.msgPrefs"); //=Preferences"); //
         request.setAttribute("providermsgProvider","provider.setConsultationTeamWarning.msgProvider"); //=myDrugref ID
         request.setAttribute("providermsgEdit","provider.setConsultationTeamWarning.msgEdit"); //=Enter your desired login for myDrugref
         request.setAttribute("providerbtnSubmit","provider.setConsultationTeamWarning.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setConsultationTeamWarning.msgSuccess"); //=myDrugref Id saved
         request.setAttribute("method","saveConsultationRequestTeamWarning");
         return actionmapping.findForward("gen");
     }
    
    
    //WORKLOAD MANAGEMENT SCREEN PROPERTY
    public ActionForward viewWorkLoadManagement(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {
                               
         DynaActionForm frm = (DynaActionForm)actionform;
         String provider = (String) request.getSession().getAttribute("user");
         UserProperty prop = this.userPropertyDAO.getProp(provider, UserProperty.WORKLOAD_MANAGEMENT);
         
         if (prop == null){
             prop = new UserProperty();
         }
         
         
         ArrayList serviceList = new ArrayList();
         try{
             DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
             ResultSet rs = db.GetSQL("select distinct servicetype, servicetype_name from ctl_billingservice where status='A'");
             while (rs.next()){
                 String servicetype     = rs.getString("servicetype");
                 String servicetypename = rs.getString("servicetype_name");
                 serviceList.add(new LabelValueBean(servicetypename,servicetype));
             }
         }catch(Exception e){
             e.printStackTrace();
         }
         serviceList.add(new  LabelValueBean("None",""));
         
         request.setAttribute("dropOpts",serviceList);
         
         request.setAttribute("dateProperty",prop);
         
         request.setAttribute("providertitle","provider.setWorkLoadManagement.title"); //=Set myDrugref ID
         request.setAttribute("providermsgPrefs","provider.setWorkLoadManagement.msgPrefs"); //=Preferences"); //
         request.setAttribute("providermsgProvider","provider.setWorkLoadManagement.msgProvider"); //=myDrugref ID
         request.setAttribute("providermsgEdit","provider.setWorkLoadManagement.msgEdit"); //=Enter your desired login for myDrugref
         request.setAttribute("providerbtnSubmit","provider.setWorkLoadManagement.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setWorkLoadManagement.msgSuccess"); //=myDrugref Id saved
         request.setAttribute("method","saveWorkLoadManagement");
         
         frm.set("dateProperty", prop);
         return actionmapping.findForward("gen");
     }

    
    public ActionForward saveWorkLoadManagement(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {
         String provider = (String) request.getSession().getAttribute("user");
    
         DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty  UdrugrefId = (UserProperty)frm.get("dateProperty");         
         String drugrefId = "";

         if (UdrugrefId != null){
             drugrefId = UdrugrefId.getValue();
         }   
         
         UserProperty prop = this.userPropertyDAO.getProp(provider, UserProperty.WORKLOAD_MANAGEMENT);
         
         if (prop ==null){
             prop = new UserProperty();
             prop.setName(UserProperty.WORKLOAD_MANAGEMENT);
             prop.setProvider_no(provider);
         }
         prop.setValue(drugrefId);
         
         this.userPropertyDAO.saveProp(prop);
         
         ArrayList serviceList = new ArrayList();
         try{
             DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
             ResultSet rs = db.GetSQL("select distinct servicetype, servicetype_name from ctl_billingservice where status='A'");
             while (rs.next()){
                 String servicetype     = rs.getString("servicetype");
                 String servicetypename = rs.getString("servicetype_name");
                 serviceList.add(new LabelValueBean(servicetypename,servicetype));
             }
         }catch(Exception e){
             e.printStackTrace();
         }
         
         request.setAttribute("dropOpts",serviceList);
         
         request.setAttribute("status", "success");
         request.setAttribute("dateProperty",prop);
         request.setAttribute("providertitle","provider.setWorkLoadManagement.title"); //=Set myDrugref ID
         request.setAttribute("providermsgPrefs","provider.setWorkLoadManagement.msgPrefs"); //=Preferences"); //
         request.setAttribute("providermsgProvider","provider.setWorkLoadManagement.msgProvider"); //=myDrugref ID
         request.setAttribute("providermsgEdit","provider.setWorkLoadManagement.msgEdit"); //=Enter your desired login for myDrugref
         request.setAttribute("providerbtnSubmit","provider.setWorkLoadManagement.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setWorkLoadManagement.msgSuccess"); //=myDrugref Id saved
         request.setAttribute("method","saveWorkLoadManagement");
         return actionmapping.findForward("gen");
     }
    
    //How does cpp paste into consult request property
    public ActionForward viewConsultPasteFmt(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {
                               
         DynaActionForm frm = (DynaActionForm)actionform;
         String provider = (String) request.getSession().getAttribute("user");
         UserProperty prop = this.userPropertyDAO.getProp(provider, UserProperty.CONSULTATION_REQ_PASTE_FMT);
         
         if (prop == null){
             prop = new UserProperty();
         }         
         
         ArrayList serviceList = new ArrayList();
         serviceList.add(new LabelValueBean("Single Line", "single"));
         serviceList.add(new LabelValueBean("Multi Line", "multi"));
         
         request.setAttribute("dropOpts",serviceList);
         
         request.setAttribute("dateProperty",prop);
         
         request.setAttribute("providertitle","provider.setConsulReqtPasteFmt.title");
         request.setAttribute("providermsgPrefs","provider.setConsulReqtPasteFmt.msgPrefs");
         request.setAttribute("providermsgProvider","provider.setConsulReqtPasteFmt.msgProvider");
         request.setAttribute("providermsgEdit","provider.setConsulReqtPasteFmt.msgEdit");
         request.setAttribute("providerbtnSubmit","provider.setConsulReqtPasteFmt.btnSubmit");
         request.setAttribute("providermsgSuccess","provider.setConsulReqtPasteFmt.msgSuccess");
         request.setAttribute("method","saveConsultPasteFmt");
         
         frm.set("dateProperty", prop);
         return actionmapping.findForward("gen");
     }    

    public ActionForward saveConsultPasteFmt(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        
         DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = (UserProperty)frm.get("dateProperty");        
         String fmt = prop != null ? prop.getValue() : "";
         
         String provider = (String) request.getSession().getAttribute("user");
         UserProperty saveProperty = this.userPropertyDAO.getProp(provider,UserProperty.CONSULTATION_REQ_PASTE_FMT);
         
         if( saveProperty == null ) {
             saveProperty = new UserProperty();
             saveProperty.setProvider_no(provider);
             saveProperty.setName(UserProperty.CONSULTATION_REQ_PASTE_FMT);
         }
         
         saveProperty.setValue(fmt);
         this.userPropertyDAO.saveProp(saveProperty);
         
         request.setAttribute("status", "success");
         request.setAttribute("providertitle","provider.setConsulReqtPasteFmt.title");
         request.setAttribute("providermsgPrefs","provider.setConsulReqtPasteFmt.msgPrefs");
         request.setAttribute("providermsgProvider","provider.setConsulReqtPasteFmt.msgProvider");
         request.setAttribute("providermsgEdit","provider.setConsulReqtPasteFmt.msgEdit");
         request.setAttribute("providerbtnSubmit","provider.setConsulReqtPasteFmt.btnSubmit");
         request.setAttribute("providermsgSuccess","provider.setConsulReqtPasteFmt.msgSuccess");
         request.setAttribute("method","saveConsultPasteFmt");
                  
         return actionmapping.findForward("gen");
     }        
    
    /**
     * Creates a new instance of ProviderPropertyAction
     */
    public ProviderPropertyAction() {
    }
    
}
