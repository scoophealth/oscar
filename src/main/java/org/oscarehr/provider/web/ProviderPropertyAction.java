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


package org.oscarehr.provider.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.oscarehr.common.dao.CtlBillingServiceDao;
import org.oscarehr.common.dao.QueueDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.eform.EFormUtil;
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

    public ActionForward OscarMsgRecvd(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        
        
        userPropertyDAO.saveProp(request.getParameter("provider_no"), UserProperty.OSCAR_MSG_RECVD, request.getParameter("value"));
        
        return null;
    }
            
    public ActionForward remove(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

         DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = (UserProperty)frm.get("dateProperty");
         UserProperty prop2 = (UserProperty)frm.get("singleViewProperty");

         frm.reset(actionmapping,request);
         this.userPropertyDAO.delete(prop);
         this.userPropertyDAO.delete(prop2);

         request.setAttribute("status", "success");

         return actionmapping.findForward("success");
    }

    public ActionForward view(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

         DynaActionForm frm = (DynaActionForm)actionform;
         String provider = LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo();
         UserProperty prop = this.userPropertyDAO.getProp(provider, UserProperty.STALE_NOTEDATE);

         if( prop == null ) {
             prop = new UserProperty();
             prop.setProviderNo(provider);
             prop.setName(UserProperty.STALE_NOTEDATE);
         }

         frm.set("dateProperty", prop);

         UserProperty prop2 = this.userPropertyDAO.getProp(provider, UserProperty.STALE_FORMAT);

         if( prop2 == null ) {
             prop2 = new UserProperty();
             prop2.setProviderNo(provider);
             prop2.setName(UserProperty.STALE_FORMAT);
         }

         frm.set("singleViewProperty", prop2);

         return actionmapping.findForward("success");
     }


    public ActionForward save(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

         DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = (UserProperty)frm.get("dateProperty");

         this.userPropertyDAO.saveProp(prop);

         UserProperty prop2 = (UserProperty)frm.get("singleViewProperty");

         this.userPropertyDAO.saveProp(prop2);

         request.setAttribute("status", "success");
         return actionmapping.findForward("success");
    }
    public ActionForward viewDefaultSex(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.DEFAULT_SEX);

         if (prop == null){
             prop = new UserProperty();
         }

         ArrayList<LabelValueBean> serviceList = new ArrayList<LabelValueBean>();
         serviceList.add(new LabelValueBean("M", "M"));
         serviceList.add(new LabelValueBean("F", "F"));

         request.setAttribute("dropOpts",serviceList);

         request.setAttribute("dateProperty",prop);

         request.setAttribute("providertitle","provider.setDefaultSex.title");
         request.setAttribute("providermsgPrefs","provider.setDefaultSex.msgPrefs");
         request.setAttribute("providermsgProvider","provider.setDefaultSex.msgDefaultSex");
         request.setAttribute("providermsgEdit","provider.setDefaultSex.msgEdit");
         request.setAttribute("providerbtnSubmit","provider.setDefaultSex.btnSubmit");
         request.setAttribute("providermsgSuccess","provider.setDefaultSex.msgSuccess");
         request.setAttribute("method","saveDefaultSex");

         frm.set("dateProperty", prop);
         return actionmapping.findForward("gen");
     }

    public ActionForward saveDefaultSex(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = (UserProperty)frm.get("dateProperty");
         String fmt = prop != null ? prop.getValue() : "";
         UserProperty saveProperty = this.userPropertyDAO.getProp(providerNo,UserProperty.DEFAULT_SEX);

         if( saveProperty == null ) {
             saveProperty = new UserProperty();
             saveProperty.setProviderNo(providerNo);
             saveProperty.setName(UserProperty.DEFAULT_SEX);
         }

         saveProperty.setValue(fmt);
         this.userPropertyDAO.saveProp(saveProperty);

         request.setAttribute("status", "success");
         request.setAttribute("providertitle","provider.setDefaultSex.title");
         request.setAttribute("providermsgPrefs","provider.setDefaultSex.msgPrefs");
         request.setAttribute("providermsgProvider","provider.setDefaultSex.msgDefaultSex");
         request.setAttribute("providermsgEdit","provider.setDefaultSex.msgEdit");
         request.setAttribute("providerbtnSubmit","provider.btnSubmit");
         request.setAttribute("providermsgSuccess","provider.setDefaultSex.msgSuccess");
         request.setAttribute("method","saveDefaultSex");

         return actionmapping.findForward("gen");
    }
    /////

    public ActionForward viewHCType(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.HC_TYPE);

         if (prop == null){
             prop = new UserProperty();
         }

         // Add all provinces / states to serviceList
         ArrayList<LabelValueBean> serviceList = constructProvinceList();

         request.setAttribute("dropOpts",serviceList);

         request.setAttribute("dateProperty",prop);

         request.setAttribute("providertitle","provider.setHCType.title");
         request.setAttribute("providermsgPrefs","provider.setHCType.msgPrefs");
         request.setAttribute("providermsgProvider","provider.setHCType.msgHCType");
         request.setAttribute("providermsgEdit","provider.setHCType.msgEdit");
         request.setAttribute("providerbtnSubmit","provider.setHCType.btnSubmit");
         request.setAttribute("providermsgSuccess","provider.setHCType.msgSuccess");
         request.setAttribute("method","saveHCType");

         frm.set("dateProperty", prop);
         return actionmapping.findForward("gen");
     }

    public ActionForward saveHCType(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

         DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = (UserProperty)frm.get("dateProperty");
         String fmt = prop != null ? prop.getValue() : "";
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

        UserProperty saveProperty = this.userPropertyDAO.getProp(providerNo,UserProperty.HC_TYPE);

         if( saveProperty == null ) {
             saveProperty = new UserProperty();
             saveProperty.setProviderNo(providerNo);
             saveProperty.setName(UserProperty.HC_TYPE);
         }

         saveProperty.setValue(fmt);
         this.userPropertyDAO.saveProp(saveProperty);

         request.setAttribute("status", "success");
         request.setAttribute("providertitle","provider.setHCType.title");
         request.setAttribute("providermsgPrefs","provider.setHCType.msgPrefs");
         request.setAttribute("providermsgProvider","provider.setHCType.msgHCType");
         request.setAttribute("providermsgEdit","provider.setHCType.msgEdit");
         request.setAttribute("providerbtnSubmit","provider.setHCType.btnSubmit");
         request.setAttribute("providermsgSuccess","provider.setHCType.msgSuccess");
         request.setAttribute("method","saveHCType");

         return actionmapping.findForward("gen");
    }



    /////

    public ActionForward viewMyDrugrefId(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

         DynaActionForm frm = (DynaActionForm)actionform;
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

         UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.MYDRUGREF_ID);
         

         if (prop == null){
             prop = new UserProperty();
         }

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
    public ActionForward viewRxPageSize(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

         DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.RX_PAGE_SIZE);


         if (prop == null){
             prop = new UserProperty();
         }

         request.setAttribute("providertitle","provider.setRxPageSize.title"); //=Set Rx Script Page Size
         request.setAttribute("providermsgPrefs","provider.setRxPageSize.msgPrefs"); //=Preferences"); //
         request.setAttribute("providermsgProvider","provider.setRxPageSize.msgPageSize"); //=Rx Script Page Size
         request.setAttribute("providermsgEdit","provider.setRxPageSize.msgEdit"); //=Select your desired page size
         request.setAttribute("providerbtnSubmit","provider.setRxPageSize.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setRxPageSize.msgSuccess"); //=Rx Script Page Size saved
         request.setAttribute("method","saveRxPageSize");

         frm.set("rxPageSizeProperty", prop);
         return actionmapping.findForward("genRxPageSize");
     }

   public ActionForward saveRxPageSize(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request,HttpServletResponse response){

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm=(DynaActionForm)actionform;
        UserProperty UPageSize=(UserProperty)frm.get("rxPageSizeProperty");
        String rxPageSize="";
        if(UPageSize!=null)
            rxPageSize=UPageSize.getValue();
        UserProperty prop=this.userPropertyDAO.getProp(providerNo, UserProperty.RX_PAGE_SIZE);
        if(prop==null){
            prop=new UserProperty();
            prop.setName(UserProperty.RX_PAGE_SIZE);
            prop.setProviderNo(providerNo);
        }
        prop.setValue(rxPageSize);
        this.userPropertyDAO.saveProp(prop);

         request.setAttribute("status", "success");
         request.setAttribute("rxPageSizeProperty",prop);
         request.setAttribute("providertitle","provider.setRxPageSize.title"); //=Set Rx Script Page Size
         request.setAttribute("providermsgPrefs","provider.setRxPageSize.msgPrefs"); //=Preferences"); //
         request.setAttribute("providermsgProvider","provider.setRxPageSize.msgPageSize"); //=Rx Script Page Size
         request.setAttribute("providermsgEdit","provider.setRxPageSize.msgEdit"); //=Select your desired page size
         request.setAttribute("providerbtnSubmit","provider.setRxPageSize.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setRxPageSize.msgSuccess"); //=Rx Script Page Size saved
         request.setAttribute("method","saveRxPageSize");
         return actionmapping.findForward("genRxPageSize");
    }

      public ActionForward saveDefaultDocQueue(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request,HttpServletResponse response){
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm=(DynaActionForm)actionform;
        UserProperty existingQ=(UserProperty)frm.get("existingDefaultDocQueueProperty");
        UserProperty newQ=(UserProperty)frm.get("newDefaultDocQueueProperty");
        String mode=request.getParameter("chooseMode");
        String defaultQ="";
        if(mode.equalsIgnoreCase("new")&&newQ!=null)
            defaultQ=newQ.getValue();
        else if(mode.equalsIgnoreCase("existing")&&existingQ!=null)
            defaultQ=existingQ.getValue();
        else{
                 request.setAttribute("status", "success");
                 request.setAttribute("providertitle","provider.setDefaultDocumentQueue.title"); //=Set Default Document Queue
                 request.setAttribute("providermsgPrefs","provider.setDefaultDocumentQueue.msgPrefs"); //=Preferences
                 request.setAttribute("providermsgProvider","provider.setDefaultDocumentQueue.msgProfileView"); //=Default Document Queue
                 request.setAttribute("providermsgSuccess","provider.setDefaultDocumentQueue.msgNotSaved"); //=Default Document Queue has NOT been saved
                 request.setAttribute("method","saveDefaultDocQueue");
                 return actionmapping.findForward("genDefaultDocQueue");
        }
        UserProperty prop=this.userPropertyDAO.getProp(providerNo, UserProperty.DOC_DEFAULT_QUEUE);
        if(prop==null){
            prop=new UserProperty();
            prop.setName(UserProperty.DOC_DEFAULT_QUEUE);
            prop.setProviderNo(providerNo);
        }
        if(mode.equals("new")){
            //save and get most recent id
            QueueDao queueDao = (QueueDao) SpringUtils.getBean("queueDao");
            queueDao.addNewQueue(defaultQ);
            String lastId=queueDao.getLastId();
            prop.setValue(lastId);
            this.userPropertyDAO.saveProp(prop);
        }else{
            prop.setValue(defaultQ);
            this.userPropertyDAO.saveProp(prop);
        }
         request.setAttribute("status", "success");
         request.setAttribute("defaultDocQueueProperty",prop);
         request.setAttribute("providertitle","provider.setDefaultDocumentQueue.title"); //=Set Default Document Queue
         request.setAttribute("providermsgPrefs","provider.setDefaultDocumentQueue.msgPrefs"); //=Preferences
         request.setAttribute("providermsgProvider","provider.setDefaultDocumentQueue.msgProfileView"); //=Default Document Queue
         request.setAttribute("providermsgSuccess","provider.setDefaultDocumentQueue.msgSuccess"); //=Default Document Queue saved
         request.setAttribute("method","saveDefaultDocQueue");
         return actionmapping.findForward("genDefaultDocQueue");
    }
    //public ActionForward viewDefaultDocQueue(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request,HttpServletResponse response){
    //    return actionmapping.findForward("genDefaultDocQueue");
    //}
    public ActionForward viewDefaultDocQueue(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request,HttpServletResponse response){
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm=(DynaActionForm)actionform;
        UserProperty prop=this.userPropertyDAO.getProp(providerNo, UserProperty.DOC_DEFAULT_QUEUE);
        UserProperty propNew=new UserProperty();

        if(prop==null){
            prop=new UserProperty();
        }
        QueueDao queueDao = (QueueDao) SpringUtils.getBean("queueDao");
        List<Hashtable> queues= queueDao.getQueues();
        Collection<LabelValueBean> viewChoices=new ArrayList<LabelValueBean>();
        viewChoices.add(new LabelValueBean("None","-1"));
        for(Hashtable ht:queues){
            viewChoices.add(new LabelValueBean((String)ht.get("queue"),(String)ht.get("id")));
        }
         request.setAttribute("viewChoices", viewChoices);
         request.setAttribute("providertitle","provider.setDefaultDocumentQueue.title"); //=Set Default Document Queue
         request.setAttribute("providermsgPrefs","provider.setDefaultDocumentQueue.msgPrefs"); //=Preferences
         request.setAttribute("providermsgProvider","provider.setDefaultDocumentQueue.msgProfileView"); //=Default Document Queue
         request.setAttribute("providermsgEditFromExisting","provider.setDefaultDocumentQueue.msgEditFromExisting"); //=Choose a default queue from existing queues
         request.setAttribute("providermsgEditSaveNew","provider.setDefaultDocumentQueue.msgEditSaveNew"); //=Save a new default queue
         request.setAttribute("providerbtnSubmit","provider.setDefaultDocumentQueue.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setDefaultDocumentQueue.msgSuccess"); //=Default Document Queue saved
         request.setAttribute("method","saveDefaultDocQueue");
         frm.set("existingDefaultDocQueueProperty", prop);
         frm.set("newDefaultDocQueueProperty", propNew);
        return actionmapping.findForward("genDefaultDocQueue");
    }

   public ActionForward viewRxProfileView(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.RX_PROFILE_VIEW);

         String propValue="";
         if (prop == null){
             prop = new UserProperty();
         }else{
            propValue=prop.getValue();
         }

         String [] propertyArray= new String[7];
         String [] va={" show_current "," show_all "," active "," inactive "," all "," longterm_acute "," longterm_acute_inactive_external "};

         for(int i=0;i<propertyArray.length;i++){
             if(propValue.contains(va[i]))  {
                 propertyArray[i]=va[i].trim();
             }//element of array has to match exactly with viewChoices values
         }
         prop.setValueArray(propertyArray);
         Collection<LabelValueBean> viewChoices=new ArrayList<LabelValueBean>();
         viewChoices.add(new LabelValueBean("Current","show_current"));
         viewChoices.add(new LabelValueBean("All","show_all"));
         viewChoices.add(new LabelValueBean("Active","active"));
         viewChoices.add(new LabelValueBean("Expired","inactive"));
         viewChoices.add(new LabelValueBean("Longterm/Acute","longterm_acute"));
         viewChoices.add(new LabelValueBean("Longterm/Acute/Inactive/External","longterm_acute_inactive_external"));
         request.setAttribute("viewChoices", viewChoices);
         request.setAttribute("providertitle","provider.setRxProfileView.title"); //=Set Rx Profile View
         request.setAttribute("providermsgPrefs","provider.setRxProfileView.msgPrefs"); //=Preferences
         request.setAttribute("providermsgProvider","provider.setRxProfileView.msgProfileView"); //=Rx Profile View
         request.setAttribute("providermsgEdit","provider.setRxProfileView.msgEdit"); //=Select your desired display
         request.setAttribute("providerbtnSubmit","provider.setRxProfileView.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setRxProfileView.msgSuccess"); //=Rx Profile View saved
         request.setAttribute("method","saveRxProfileView");

         frm.set("rxProfileViewProperty", prop);

         return actionmapping.findForward("genRxProfileView");
     }

   public ActionForward saveRxProfileView(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request,HttpServletResponse response){

       try{
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm=(DynaActionForm)actionform;
        UserProperty UProfileView=(UserProperty)frm.get("rxProfileViewProperty");
        String[] va=null;
        if(UProfileView!=null)
            va=UProfileView.getValueArray();
        UserProperty prop=this.userPropertyDAO.getProp(providerNo, UserProperty.RX_PROFILE_VIEW);
        if(prop==null){
            prop=new UserProperty();
            prop.setName(UserProperty.RX_PROFILE_VIEW);
            prop.setProviderNo(providerNo);
        }

        String rxProfileView="";
        if(va!=null){
            for(int i=0;i<va.length;i++){
                rxProfileView+=" "+va[i]+" ";
            }
        }
        prop.setValue(rxProfileView);
        this.userPropertyDAO.saveProp(prop);

         request.setAttribute("status", "success");
         request.setAttribute("defaultDocQueueProperty",prop);
         request.setAttribute("providertitle","provider.setRxProfileView.title"); //=Set Rx Profile View
         request.setAttribute("providermsgPrefs","provider.setRxProfileView.msgPrefs"); //=Preferences
         request.setAttribute("providermsgProvider","provider.setRxProfileView.msgProfileView"); //=Rx Profile View
         request.setAttribute("providermsgEdit","provider.setRxProfileView.msgEdit"); //=Select your desired display
         request.setAttribute("providerbtnSubmit","provider.setRxProfileView.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setRxProfileView.msgSuccess"); //=Rx Profile View saved
         request.setAttribute("method","saveRxProfileView");
       }catch(Exception e){
           MiscUtils.getLogger().error("Error", e);
       }

         return actionmapping.findForward("genRxProfileView");
    }

      public ActionForward viewShowPatientDOB(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.RX_SHOW_PATIENT_DOB);

         String propValue="";
         if (prop == null){
             prop = new UserProperty();
         }else{
            propValue=prop.getValue();
         }

         //String [] propertyArray= new String[7];
         boolean checked;
         if(propValue.equalsIgnoreCase("yes"))
             checked=true;
         else
             checked=false;

         prop.setChecked(checked);
         request.setAttribute("rxShowPatientDOBProperty", prop);
         request.setAttribute("providertitle","provider.setShowPatientDOB.title"); //=Select if you want to use Rx3
         request.setAttribute("providermsgPrefs","provider.setShowPatientDOB.msgPrefs"); //=Preferences
         request.setAttribute("providermsgProvider","provider.setShowPatientDOB.msgProfileView"); //=Use Rx3
         request.setAttribute("providermsgEdit","provider.setShowPatientDOB.msgEdit"); //=Do you want to use Rx3?
         request.setAttribute("providerbtnSubmit","provider.setShowPatientDOB.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setShowPatientDOB.msgSuccess"); //=Rx3 Selection saved
         request.setAttribute("method","saveShowPatientDOB");

         frm.set("rxShowPatientDOBProperty", prop);
         return actionmapping.findForward("genShowPatientDOB");
     }

   public ActionForward saveShowPatientDOB(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request,HttpServletResponse response){
	    DynaActionForm frm=(DynaActionForm)actionform;
        UserProperty UShowPatientDOB=(UserProperty)frm.get("rxShowPatientDOBProperty");

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		boolean checked=false;
        if(UShowPatientDOB!=null)
            checked = UShowPatientDOB.isChecked();
        UserProperty prop=this.userPropertyDAO.getProp(providerNo, UserProperty.RX_SHOW_PATIENT_DOB);
        if(prop==null){
            prop=new UserProperty();
            prop.setName(UserProperty.RX_SHOW_PATIENT_DOB);
            prop.setProviderNo(providerNo);
        }
        String showPatientDOB="no";
        if(checked)
            showPatientDOB="yes";
        prop.setValue(showPatientDOB);
        this.userPropertyDAO.saveProp(prop);

         request.setAttribute("status", "success");
         request.setAttribute("rxShowPatientDOBProperty",prop);
         request.setAttribute("providertitle","provider.setShowPatientDOB.title"); //=Select if you want to use Rx3
         request.setAttribute("providermsgPrefs","provider.setShowPatientDOB.msgPrefs"); //=Preferences
         request.setAttribute("providermsgProvider","provider.setShowPatientDOB.msgProfileView"); //=Use Rx3
         request.setAttribute("providermsgEdit","provider.setShowPatientDOB.msgEdit"); //=Do you want to use Rx3?
         request.setAttribute("providerbtnSubmit","provider.setShowPatientDOB.btnSubmit"); //=Save
         if(checked)
            request.setAttribute("providermsgSuccess","provider.setShowPatientDOB.msgSuccess_selected"); //=Rx3 is selected
         else
            request.setAttribute("providermsgSuccess","provider.setShowPatientDOB.msgSuccess_unselected"); //=Rx3 is unselected
         request.setAttribute("method","saveShowPatientDOB");
         return actionmapping.findForward("genShowPatientDOB");
    }

   public ActionForward viewUseMyMeds(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.USE_MYMEDS);
         if (prop == null) prop = new UserProperty();

         String propValue=prop.getValue();
         boolean checked=Boolean.parseBoolean(propValue);

         prop.setChecked(checked);
         request.setAttribute("useMyMedsProperty", prop);
         request.setAttribute("providertitle","provider.setUseMyMeds.title"); //=Select if you want to use MyMeds
         request.setAttribute("providermsgPrefs","provider.setUseMyMeds.msgPrefs"); //=Preferences
         request.setAttribute("providermsgProvider","provider.setUseMyMeds.msgProfileView"); //=Use MyMeds
         request.setAttribute("providermsgEdit","provider.setUseMyMeds.msgEdit"); //=Do you want to use MyMeds?
         request.setAttribute("providerbtnSubmit","provider.setUseMyMeds.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setUseMyMeds.msgSuccess"); //=MyMeds Selection saved
         request.setAttribute("method","saveUseMyMeds");

         frm.set("useMyMedsProperty", prop);
         return actionmapping.findForward("genUseMyMeds");
     }

      public ActionForward saveUseMyMeds(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request,HttpServletResponse response){
        DynaActionForm frm=(DynaActionForm)actionform;
        UserProperty UUseMyMeds=(UserProperty)frm.get("useMyMedsProperty");
        //UserProperty UUseRx3=(UserProperty)request.getAttribute("rxUseRx3Property");

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		boolean checked=false;
        if(UUseMyMeds!=null)
            checked = UUseMyMeds.isChecked();
        UserProperty prop=this.userPropertyDAO.getProp(providerNo, UserProperty.USE_MYMEDS);
        if(prop==null){
            prop=new UserProperty();
            prop.setName(UserProperty.USE_MYMEDS);
            prop.setProviderNo(providerNo);
        }
        prop.setValue(String.valueOf(checked));
        this.userPropertyDAO.saveProp(prop);

         request.setAttribute("status", "success");
         request.setAttribute("useMyMedsProperty",prop);
         request.setAttribute("providertitle","provider.setUseMyMeds.title"); //=Select if you want to use Rx3
         request.setAttribute("providermsgPrefs","provider.setUseMyMeds.msgPrefs"); //=Preferences
         request.setAttribute("providermsgProvider","provider.setUseMyMeds.msgProfileView"); //=Use Rx3
         request.setAttribute("providermsgEdit","provider.setUseMyMeds.msgEdit"); //=Check if you want to use Rx3
         request.setAttribute("providerbtnSubmit","provider.setUseMyMeds.btnSubmit"); //=Save
         if(checked)
            request.setAttribute("providermsgSuccess","provider.setUseMyMeds.msgSuccess_selected"); //=Rx3 is selected
         else
            request.setAttribute("providermsgSuccess","provider.setUseMyMeds.msgSuccess_unselected"); //=Rx3 is unselected
         request.setAttribute("method","saveUseMyMeds");
         return actionmapping.findForward("genUseMyMeds");
    }

      public ActionForward viewUseRx3(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {
    	  
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.RX_USE_RX3);

         String propValue="";
         if (prop == null){
             prop = new UserProperty();
         }else{
            propValue=prop.getValue();
         }

         //String [] propertyArray= new String[7];
         boolean checked;
         if(propValue.equalsIgnoreCase("yes"))
             checked=true;
         else
             checked=false;

         prop.setChecked(checked);
         request.setAttribute("rxUseRx3Property", prop);
         request.setAttribute("providertitle","provider.setRxRxUseRx3.title"); //=Select if you want to use Rx3
         request.setAttribute("providermsgPrefs","provider.setRxRxUseRx3.msgPrefs"); //=Preferences
         request.setAttribute("providermsgProvider","provider.setRxRxUseRx3.msgProfileView"); //=Use Rx3
         request.setAttribute("providermsgEdit","provider.setRxUseRx3.msgEdit"); //=Do you want to use Rx3?
         request.setAttribute("providerbtnSubmit","provider.setRxUseRx3.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setRxUseRx3.msgSuccess"); //=Rx3 Selection saved
         request.setAttribute("method","saveUseRx3");

         frm.set("rxUseRx3Property", prop);
         return actionmapping.findForward("genRxUseRx3");
     }

   public ActionForward saveUseRx3(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request,HttpServletResponse response){
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm=(DynaActionForm)actionform;
        UserProperty UUseRx3=(UserProperty)frm.get("rxUseRx3Property");
      
        boolean checked=false;
        if(UUseRx3!=null)
            checked = UUseRx3.isChecked();
        UserProperty prop=this.userPropertyDAO.getProp(providerNo, UserProperty.RX_USE_RX3);
        if(prop==null){
            prop=new UserProperty();
            prop.setName(UserProperty.RX_USE_RX3);
            prop.setProviderNo(providerNo);
        }
        String useRx3="no";
        if(checked)
            useRx3="yes";
        prop.setValue(useRx3);
        this.userPropertyDAO.saveProp(prop);

         request.setAttribute("status", "success");
         request.setAttribute("rxUseRx3Property",prop);
         request.setAttribute("providertitle","provider.setRxRxUseRx3.title"); //=Select if you want to use Rx3
         request.setAttribute("providermsgPrefs","provider.setRxRxUseRx3.msgPrefs"); //=Preferences
         request.setAttribute("providermsgProvider","provider.setRxRxUseRx3.msgProfileView"); //=Use Rx3
         request.setAttribute("providermsgEdit","provider.setRxUseRx3.msgEdit"); //=Check if you want to use Rx3
         request.setAttribute("providerbtnSubmit","provider.setRxUseRx3.btnSubmit"); //=Save
         if(checked)
            request.setAttribute("providermsgSuccess","provider.setRxUseRx3.msgSuccess_selected"); //=Rx3 is selected
         else
            request.setAttribute("providermsgSuccess","provider.setRxUseRx3.msgSuccess_unselected"); //=Rx3 is unselected
         request.setAttribute("method","saveUseRx3");
         return actionmapping.findForward("genRxUseRx3");
    }

       public ActionForward viewDefaultQuantity(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

         DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.RX_DEFAULT_QUANTITY);


         if (prop == null){
             prop = new UserProperty();
         }

         //request.setAttribute("propert",propertyToSet);
         request.setAttribute("rxDefaultQuantityProperty",prop);
         request.setAttribute("providertitle","provider.setRxDefaultQuantity.title"); //=Set Rx Default Quantity
         request.setAttribute("providermsgPrefs","provider.setRxDefaultQuantity.msgPrefs"); //=Preferences"); //
         request.setAttribute("providermsgProvider","provider.setRxDefaultQuantity.msgDefaultQuantity"); //=Rx Default Quantity
         request.setAttribute("providermsgEdit","provider.setRxDefaultQuantity.msgEdit"); //=Enter your desired quantity
         request.setAttribute("providerbtnSubmit","provider.setRxDefaultQuantity.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setRxDefaultQuantity.msgSuccess"); //=Rx Default Quantity saved
         request.setAttribute("method","saveDefaultQuantity");

         frm.set("rxDefaultQuantityProperty", prop);

         return actionmapping.findForward("genRxDefaultQuantity");
     }

   public ActionForward saveDefaultQuantity(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request,HttpServletResponse response){
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		
		DynaActionForm frm=(DynaActionForm)actionform;
        UserProperty UDefaultQuantity=(UserProperty)frm.get("rxDefaultQuantityProperty");
        String rxDefaultQuantity="";
        if(UDefaultQuantity!=null)
            rxDefaultQuantity=UDefaultQuantity.getValue();
        UserProperty prop=this.userPropertyDAO.getProp(providerNo, UserProperty.RX_DEFAULT_QUANTITY);
        if(prop==null){
            prop=new UserProperty();
            prop.setName(UserProperty.RX_DEFAULT_QUANTITY);
            prop.setProviderNo(providerNo);
        }
        prop.setValue(rxDefaultQuantity);
        this.userPropertyDAO.saveProp(prop);

         request.setAttribute("status", "success");
         request.setAttribute("rxDefaultQuantityProperty",prop);
         request.setAttribute("providertitle","provider.setRxDefaultQuantity.title"); //=Set Rx Default Quantity
         request.setAttribute("providermsgPrefs","provider.setRxDefaultQuantity.msgPrefs"); //=Preferences"); //
         request.setAttribute("providermsgProvider","provider.setRxDefaultQuantity.msgDefaultQuantity"); //=Rx Default Quantity
         request.setAttribute("providermsgEdit","provider.setRxDefaultQuantity.msgEdit"); //=Enter your desired quantity
         request.setAttribute("providerbtnSubmit","provider.setRxDefaultQuantity.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setRxDefaultQuantity.msgSuccess"); //=Rx Default Quantity saved
         request.setAttribute("method","saveDefaultQuantity");
         return actionmapping.findForward("genRxDefaultQuantity");

    }
    public ActionForward saveMyDrugrefId(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
   	
    	DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty  UdrugrefId = (UserProperty)frm.get("dateProperty");
         String drugrefId = "";

         if (UdrugrefId != null){
             drugrefId = UdrugrefId.getValue();
         }

         UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.MYDRUGREF_ID);

         if (prop ==null){
             prop = new UserProperty();
             prop.setName(UserProperty.MYDRUGREF_ID);
             prop.setProviderNo(providerNo);
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



    /*ontario md*/
    public ActionForward viewOntarioMDId(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

         DynaActionForm frm = (DynaActionForm)actionform;
         
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.ONTARIO_MD_USERNAME);
         UserProperty prop2 = this.userPropertyDAO.getProp(providerNo, UserProperty.ONTARIO_MD_PASSWORD);

         if (prop == null){
             prop = new UserProperty();
         }

         if (prop2 == null){
             prop2 = new UserProperty();
         }

         //request.setAttribute("propert",propertyToSet);
         request.setAttribute("dateProperty",prop);
         request.setAttribute("dateProperty2",prop2);


         request.setAttribute("providertitle","provider.setOntarioMD.title"); //=Set myDrugref ID
         request.setAttribute("providermsgPrefs","provider.setOntarioMD.msgPrefs"); //=Preferences"); //
         request.setAttribute("providermsgProvider","provider.setOntarioMD.msgProvider"); //=myDrugref ID
         request.setAttribute("providermsgEdit","provider.setOntarioMD.msgEdit"); //=Enter your desired login for myDrugref
         request.setAttribute("providerbtnSubmit","provider.setOntarioMD.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setOntarioMD.msgSuccess"); //=myDrugref Id saved
         request.setAttribute("method","saveOntarioMDId");

         frm.set("dateProperty", prop);
         return actionmapping.findForward("gen");
     }


    public ActionForward saveOntarioMDId(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
    	
    	DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty  UdrugrefId = (UserProperty)frm.get("dateProperty");
         String drugrefId = "";

         if (UdrugrefId != null){
             drugrefId = UdrugrefId.getValue();
         }

         UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.ONTARIO_MD_USERNAME);

         if (prop ==null){
             prop = new UserProperty();
             prop.setName(UserProperty.ONTARIO_MD_USERNAME);
             prop.setProviderNo(providerNo);
         }
         prop.setValue(drugrefId);

         this.userPropertyDAO.saveProp(prop);


         UserProperty  UdrugrefId2 = (UserProperty)frm.get("dateProperty2");
         String drugrefId2 = "";

         if (UdrugrefId2 != null){
             drugrefId2 = UdrugrefId2.getValue();
         }

         UserProperty prop2 = this.userPropertyDAO.getProp(providerNo, UserProperty.ONTARIO_MD_PASSWORD);

         if (prop2 ==null){
             prop2 = new UserProperty();
             prop2.setName(UserProperty.ONTARIO_MD_PASSWORD);
             prop2.setProviderNo(providerNo);
         }
         prop2.setValue(drugrefId2);

         this.userPropertyDAO.saveProp(prop2);


         request.setAttribute("status", "success");
         request.setAttribute("dateProperty",prop);
         request.setAttribute("providertitle","provider.setOntarioMD.title"); //=Set myDrugref ID
         request.setAttribute("providermsgPrefs","provider.setOntarioMD.msgPrefs"); //=Preferences"); //
         request.setAttribute("providermsgProvider","provider.setOntarioMD.msgProvider"); //=myDrugref ID
         request.setAttribute("providermsgEdit","provider.setOntarioMD.msgEdit"); //=Enter your desired login for myDrugref
         request.setAttribute("providerbtnSubmit","provider.setOntarioMD.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setOntarioMD.msgSuccess"); //=myDrugref Id saved
         request.setAttribute("method","saveOntarioMDId");
         return actionmapping.findForward("gen");
     }
    /*ontario md*/

    public ActionForward viewConsultationRequestCuffOffDate(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.CONSULTATION_TIME_PERIOD_WARNING);

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
		
    	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty  UdrugrefId = (UserProperty)frm.get("dateProperty");
         String drugrefId = "";

         if (UdrugrefId != null){
             drugrefId = UdrugrefId.getValue();
         }

         UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.CONSULTATION_TIME_PERIOD_WARNING);

         if (prop ==null){
             prop = new UserProperty();
             prop.setName(UserProperty.CONSULTATION_TIME_PERIOD_WARNING);
             prop.setProviderNo(providerNo);
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

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.CONSULTATION_TEAM_WARNING);

         if (prop == null){
             prop = new UserProperty();
         }

         EctConsultationFormRequestUtil conUtil = new EctConsultationFormRequestUtil();
         conUtil.estTeams();
         Vector<String> vect = conUtil.teamVec;

         ArrayList<LabelValueBean> serviceList = new ArrayList<LabelValueBean>();
         serviceList.add(new  LabelValueBean("All","-1"));
         for (String s: vect ){
                 serviceList.add(new LabelValueBean(s,s));
         }
         serviceList.add(new  LabelValueBean("None",""));



         //conUtil.teamVec.add("All");
         //conUtil.teamVec.add("None");

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
		
    	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty  UdrugrefId = (UserProperty)frm.get("dateProperty");
         String drugrefId = "";

         if (UdrugrefId != null){
             drugrefId = UdrugrefId.getValue();
         }

         UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.CONSULTATION_TEAM_WARNING);

         if (prop ==null){
             prop = new UserProperty();
             prop.setName(UserProperty.CONSULTATION_TEAM_WARNING);
             prop.setProviderNo(providerNo);
         }
         prop.setValue(drugrefId);

         this.userPropertyDAO.saveProp(prop);


         EctConsultationFormRequestUtil conUtil = new EctConsultationFormRequestUtil();
         conUtil.estTeams();
         Vector<String> vect = conUtil.teamVec;

         ArrayList<LabelValueBean> serviceList = new ArrayList<LabelValueBean>();
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

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.WORKLOAD_MANAGEMENT);

         if (prop == null)
             prop = new UserProperty();

         ArrayList<LabelValueBean> serviceList = new ArrayList<LabelValueBean>();         
		 CtlBillingServiceDao dao = SpringUtils.getBean(CtlBillingServiceDao.class);
		 for (Object[] service : dao.getUniqueServiceTypes())
			serviceList.add(new LabelValueBean(String.valueOf(service[0]), String.valueOf(service[1])));
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
		
    	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty  UdrugrefId = (UserProperty)frm.get("dateProperty");
         String drugrefId = "";

         if (UdrugrefId != null){
             drugrefId = UdrugrefId.getValue();
         }

         UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.WORKLOAD_MANAGEMENT);

         if (prop ==null){
             prop = new UserProperty();
             prop.setName(UserProperty.WORKLOAD_MANAGEMENT);
             prop.setProviderNo(providerNo);
         }
         prop.setValue(drugrefId);

         this.userPropertyDAO.saveProp(prop);

         ArrayList<LabelValueBean> serviceList = new ArrayList<LabelValueBean>();
         CtlBillingServiceDao dao = SpringUtils.getBean(CtlBillingServiceDao.class);
         for(Object[] service : dao.getUniqueServiceTypes())
        	 serviceList.add(new LabelValueBean(String.valueOf(service[0]), String.valueOf(service[1])));

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

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.CONSULTATION_REQ_PASTE_FMT);

         if (prop == null){
             prop = new UserProperty();
         }

         ArrayList<LabelValueBean> serviceList = new ArrayList<LabelValueBean>();
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

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = (UserProperty)frm.get("dateProperty");
         String fmt = prop != null ? prop.getValue() : "";

         UserProperty saveProperty = this.userPropertyDAO.getProp(providerNo,UserProperty.CONSULTATION_REQ_PASTE_FMT);

         if( saveProperty == null ) {
             saveProperty = new UserProperty();
             saveProperty.setProviderNo(providerNo);
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
    // Constructs a list of LabelValueBeans, to be used as the dropdown list
    // when viewing a HCType preference
    public ArrayList<LabelValueBean> constructProvinceList() {

         ArrayList<LabelValueBean> provinces = new ArrayList<LabelValueBean>();

         provinces.add(new LabelValueBean("AB-Alberta", "AB"));
         provinces.add(new LabelValueBean("BC-British Columbia", "BC"));
         provinces.add(new LabelValueBean("MB-Manitoba", "MB"));
         provinces.add(new LabelValueBean("NB-New Brunswick", "NB"));
         provinces.add(new LabelValueBean("NL-Newfoundland", "NL"));
         provinces.add(new LabelValueBean("NT-Northwest Territory", "NT"));
         provinces.add(new LabelValueBean("NS-Nova Scotia", "NS"));
         provinces.add(new LabelValueBean("NU-Nunavut", "NU"));
         provinces.add(new LabelValueBean("ON-Ontario", "ON"));
         provinces.add(new LabelValueBean("PE-Prince Edward Island", "PE"));
         provinces.add(new LabelValueBean("QC-Quebec", "QC"));
         provinces.add(new LabelValueBean("SK-Saskatchewan", "SK"));
         provinces.add(new LabelValueBean("YT-Yukon", "YK"));
         provinces.add(new LabelValueBean("US resident", "US"));
         provinces.add(new LabelValueBean("US-AK-Alaska", "US-AK"));
         provinces.add(new LabelValueBean("US-AL-Alabama","US-AL"));
         provinces.add(new LabelValueBean("US-AR-Arkansas","US-AR"));
         provinces.add(new LabelValueBean("US-AZ-Arizona","US-AZ"));
         provinces.add(new LabelValueBean("US-CA-California","US-CA"));
         provinces.add(new LabelValueBean("US-CO-Colorado","US-CO"));
         provinces.add(new LabelValueBean("US-CT-Connecticut","US-CT"));
         provinces.add(new LabelValueBean("US-CZ-Canal Zone","US-CZ"));
         provinces.add(new LabelValueBean("US-DC-District of Columbia","US-DC"));
         provinces.add(new LabelValueBean("US-DE-Delaware","US-DE"));
         provinces.add(new LabelValueBean("US-FL-Florida","US-FL"));
         provinces.add(new LabelValueBean("US-GA-Georgia","US-GA"));
         provinces.add(new LabelValueBean("US-GU-Guam","US-GU"));
         provinces.add(new LabelValueBean("US-HI-Hawaii","US-HI"));
         provinces.add(new LabelValueBean("US-IA-Iowa","US-IA"));
         provinces.add(new LabelValueBean("US-ID-Idaho","US-ID"));
         provinces.add(new LabelValueBean("US-IL-Illinois","US-IL"));
         provinces.add(new LabelValueBean("US-IN-Indiana","US-IN"));
         provinces.add(new LabelValueBean("US-KS-Kansas","US-KS"));
         provinces.add(new LabelValueBean("US-KY-Kentucky","US-KY"));
         provinces.add(new LabelValueBean("US-LA-Louisiana","US-LA"));
         provinces.add(new LabelValueBean("US-MA-Massachusetts","US-MA"));
         provinces.add(new LabelValueBean("US-MD-Maryland","US-MD"));
         provinces.add(new LabelValueBean("US-ME-Maine","US-ME"));
         provinces.add(new LabelValueBean("US-MI-Michigan","US-MI"));
         provinces.add(new LabelValueBean("US-MN-Minnesota","US-MN"));
         provinces.add(new LabelValueBean("US-MO-Missouri","US-MO"));
         provinces.add(new LabelValueBean("US-MS-Mississippi","US-MS"));
         provinces.add(new LabelValueBean("US-MT-Montana","US-MT"));
         provinces.add(new LabelValueBean("US-NC-North Carolina","US-NC"));
         provinces.add(new LabelValueBean("US-ND-North Dakota","US-ND"));
         provinces.add(new LabelValueBean("US-NE-Nebraska","US-NE"));
         provinces.add(new LabelValueBean("US-NH-New Hampshire","US-NH"));
         provinces.add(new LabelValueBean("US-NJ-New Jersey","US-NJ"));
         provinces.add(new LabelValueBean("US-NM-New Mexico","US-NM"));
         provinces.add(new LabelValueBean("US-NU-Nunavut","US-NU"));
         provinces.add(new LabelValueBean("US-NV-Nevada","US-NV"));
         provinces.add(new LabelValueBean("US-NY-New York","US-NY"));
         provinces.add(new LabelValueBean("US-OH-Ohio","US-OH"));
         provinces.add(new LabelValueBean("US-OK-Oklahoma","US-OK"));
         provinces.add(new LabelValueBean("US-OR-Oregon","US-OR"));
         provinces.add(new LabelValueBean("US-PA-Pennsylvania","US-PA"));
         provinces.add(new LabelValueBean("US-PR-Puerto Rico","US-PR"));
         provinces.add(new LabelValueBean("US-RI-Rhode Island","US-RI"));
         provinces.add(new LabelValueBean("US-SC-South Carolina","US-SC"));
         provinces.add(new LabelValueBean("US-SD-South Dakota","US-SD"));
         provinces.add(new LabelValueBean("US-TN-Tennessee","US-TN"));
         provinces.add(new LabelValueBean("US-TX-Texas","US-TX"));
         provinces.add(new LabelValueBean("US-UT-Utah","US-UT"));
         provinces.add(new LabelValueBean("US-VA-Virginia","US-VA"));
         provinces.add(new LabelValueBean("US-VI-Virgin Islands","US-VI"));
         provinces.add(new LabelValueBean("US-VT-Vermont","US-VT"));
         provinces.add(new LabelValueBean("US-WA-Washington","US-WA"));
         provinces.add(new LabelValueBean("US-WI-Wisconsin","US-WI"));
         provinces.add(new LabelValueBean("US-WV-West Virginia","US-WV"));
         provinces.add(new LabelValueBean("US-WY-Wyoming","US-WY"));

         return provinces;
    }



    public ActionForward viewFavouriteEformGroup(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {
		
    	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
        UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.EFORM_FAVOURITE_GROUP);

        if (prop == null){
         prop = new UserProperty();
        }

        frm.set("dateProperty", prop);
        ArrayList<HashMap<String,String>> groups = EFormUtil.getEFormGroups();
        ArrayList<LabelValueBean> groupList = new ArrayList<LabelValueBean>();
        String name;
        groupList.add(new LabelValueBean("None",""));
         for (HashMap<String,String> h: groups ){
             name = h.get("groupName");
             groupList.add(new LabelValueBean(name,name));
         }

         request.setAttribute("dropOpts",groupList);

         request.setAttribute("dateProperty",prop);

         request.setAttribute("providertitle","provider.setFavEfrmGrp.title"); //=Set Favourite Eform Group
         request.setAttribute("providermsgPrefs","provider.setFavEfrmGrp.msgPrefs"); //=Preferences"); //
         request.setAttribute("providermsgProvider","provider.setFavEfrmGrp.msgProvider"); //=Default Eform Group
         request.setAttribute("providermsgEdit","provider.setFavEfrmGrp.msgEdit"); //=Select your favourite Eform Group
         request.setAttribute("providerbtnSubmit","provider.setFavEfrmGrp.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setFavEfrmGrp.msgSuccess"); //=Favourite Eform Group saved
         request.setAttribute("method","saveFavouriteEformGroup");
        return actionmapping.findForward("gen");
    }

    public ActionForward saveFavouriteEformGroup(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = (UserProperty)frm.get("dateProperty");
         String group = prop != null ? prop.getValue() : "";

         UserProperty saveProperty = this.userPropertyDAO.getProp(providerNo,UserProperty.EFORM_FAVOURITE_GROUP);

         if( saveProperty == null ) {
             saveProperty = new UserProperty();
             saveProperty.setProviderNo(providerNo);
             saveProperty.setName(UserProperty.EFORM_FAVOURITE_GROUP);
         }

         if( group.equalsIgnoreCase("")) {
             this.userPropertyDAO.delete(saveProperty);
         }
         else {
            saveProperty.setValue(group);
            this.userPropertyDAO.saveProp(saveProperty);
         }

         request.setAttribute("status", "success");
         request.setAttribute("providertitle","provider.setFavEfrmGrp.title"); //=Set Favourite Eform Group
         request.setAttribute("providermsgPrefs","provider.setFavEfrmGrp.msgPrefs"); //=Preferences"); //
         request.setAttribute("providermsgProvider","provider.setFavEfrmGrp.msgProvider"); //=Default Eform Group
         request.setAttribute("providermsgEdit","provider.setFavEfrmGrp.msgEdit"); //=Select your favourite Eform Group
         request.setAttribute("providerbtnSubmit","provider.setFavEfrmGrp.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setFavEfrmGrp.msgSuccess"); //=Favourite Eform Group saved
         request.setAttribute("method","saveFavouriteEformGroup");

         return actionmapping.findForward("gen");
    }

    public ActionForward viewCppSingleLine(ActionMapping actionmapping,
            ActionForm actionform,
            HttpServletRequest request,
            HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
		UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.CPP_SINGLE_LINE);

		String propValue="";
		if (prop == null){
			prop = new UserProperty();
		}else{
			propValue=prop.getValue();
		}

		boolean checked;
		if(propValue.equalsIgnoreCase("yes"))
			checked=true;
		else
			checked=false;

		prop.setChecked(checked);
		request.setAttribute("cppSingleLineProperty", prop);
		request.setAttribute("providertitle","provider.setCppSingleLine.title"); //=Select if you want to use Rx3
		request.setAttribute("providermsgPrefs","provider.setCppSingleLine.msgPrefs"); //=Preferences
		request.setAttribute("providermsgProvider","provider.setCppSingleLine.msgProfileView"); //=Use Rx3
		request.setAttribute("providermsgEdit","provider.setCppSingleLine.msgEdit"); //=Do you want to use Rx3?
		request.setAttribute("providerbtnSubmit","provider.setCppSingleLine.btnSubmit"); //=Save
		request.setAttribute("providermsgSuccess","provider.setCppSingleLine.msgSuccess"); //=Rx3 Selection saved
		request.setAttribute("method","saveUseCppSingleLine");

		frm.set("cppSingleLineProperty", prop);

		return actionmapping.findForward("genCppSingleLine");
	}


    public ActionForward saveUseCppSingleLine(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request,HttpServletResponse response){
    	DynaActionForm frm=(DynaActionForm)actionform;
    	UserProperty UUseRx3=(UserProperty)frm.get("cppSingleLineProperty");

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		boolean checked=false;
		if(UUseRx3!=null)
			checked = UUseRx3.isChecked();
		UserProperty prop=this.userPropertyDAO.getProp(providerNo, UserProperty.CPP_SINGLE_LINE);
		if(prop==null){
			prop=new UserProperty();
			prop.setName(UserProperty.CPP_SINGLE_LINE);
			prop.setProviderNo(providerNo);
		}
		String useRx3="no";
		if(checked)
			useRx3="yes";

		prop.setValue(useRx3);
		this.userPropertyDAO.saveProp(prop);

		request.setAttribute("status", "success");
		request.setAttribute("cppSingleLineProperty",prop);
		request.setAttribute("providertitle","provider.setCppSingleLine.title"); //=Select if you want to use Rx3
		request.setAttribute("providermsgPrefs","provider.setCppSingleLine.msgPrefs"); //=Preferences
		request.setAttribute("providermsgProvider","provider.setCppSingleLine.msgProfileView"); //=Use Rx3
		request.setAttribute("providermsgEdit","provider.setCppSingleLine.msgEdit"); //=Check if you want to use Rx3
		request.setAttribute("providerbtnSubmit","provider.setCppSingleLine.btnSubmit"); //=Save
		if(checked)
			request.setAttribute("providermsgSuccess","provider.setCppSingleLine.msgSuccess_selected"); //=Rx3 is selected
		else
			request.setAttribute("providermsgSuccess","provider.setCppSingleLine.msgSuccess_unselected"); //=Rx3 is unselected
		request.setAttribute("method","saveUseCppSingleLine");

		return actionmapping.findForward("genCppSingleLine");
	}
    
public ActionForward viewEDocBrowserInDocumentReport(ActionMapping actionmapping,
            ActionForm actionform,
            HttpServletRequest request,
            HttpServletResponse response) {

	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	String providerNo=loggedInInfo.getLoggedInProviderNo();

	DynaActionForm frm = (DynaActionForm)actionform;
		UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.EDOC_BROWSER_IN_DOCUMENT_REPORT);

		String propValue="";
		if (prop == null){
			prop = new UserProperty();
		}else{
			propValue=prop.getValue();
		}

		boolean checked;
		if(propValue.equalsIgnoreCase("yes"))
			checked=true;
		else
			checked=false;

		prop.setChecked(checked);
		request.setAttribute("eDocBrowserInDocumentReportProperty", prop);
		request.setAttribute("providertitle","provider.setEDocBrowserInDocumentReport.title"); 
		request.setAttribute("providermsgPrefs","provider.setEDocBrowserInDocumentReport.msgPrefs");
		request.setAttribute("providermsgProvider","provider.setEDocBrowserInDocumentReport.msgProfileView");
		request.setAttribute("providermsgEdit","provider.setEDocBrowserInDocumentReport.msgEdit");
		request.setAttribute("providerbtnSubmit","provider.setEDocBrowserInDocumentReport.btnSubmit");
		request.setAttribute("providermsgSuccess","provider.setEDocBrowserInDocumentReport.msgSuccess");
		request.setAttribute("method","saveEDocBrowserInDocumentReport");

		frm.set("eDocBrowserInDocumentReportProperty", prop);

		return actionmapping.findForward("genEDocBrowserInDocumentReport");
	}


    public ActionForward saveEDocBrowserInDocumentReport(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request,HttpServletResponse response){
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm=(DynaActionForm)actionform;
    	UserProperty Uprop=(UserProperty)frm.get("eDocBrowserInDocumentReportProperty");

		boolean checked=false;
		if(Uprop!=null)
			checked = Uprop.isChecked();
		UserProperty prop=this.userPropertyDAO.getProp(providerNo, UserProperty.EDOC_BROWSER_IN_DOCUMENT_REPORT);
		if(prop==null){
			prop=new UserProperty();
			prop.setName(UserProperty.EDOC_BROWSER_IN_DOCUMENT_REPORT);
			prop.setProviderNo(providerNo);
		}
		String propValue="no";
		if(checked)
			propValue="yes";

		prop.setValue(propValue);
		this.userPropertyDAO.saveProp(prop);

		request.setAttribute("status", "success");
		request.setAttribute("eDocBrowserInDocumentReportProperty",prop);
		request.setAttribute("providertitle","provider.setEDocBrowserInDocumentReport.title");
		request.setAttribute("providermsgPrefs","provider.setEDocBrowserInDocumentReport.msgPrefs");
		request.setAttribute("providermsgProvider","provider.setEDocBrowserInDocumentReport.msgProfileView");
		request.setAttribute("providermsgEdit","provider.setEDocBrowserInDocumentReport.msgEdit");
		request.setAttribute("providerbtnSubmit","provider.setEDocBrowserInDocumentReport.btnSubmit");
		if(checked)
			request.setAttribute("providermsgSuccess","provider.setEDocBrowserInDocumentReport.msgSuccess_selected");
		else
			request.setAttribute("providermsgSuccess","provider.setEDocBrowserInDocumentReport.msgSuccess_unselected");
		request.setAttribute("method","saveEDocBrowserInDocumentReport");

		return actionmapping.findForward("genEDocBrowserInDocumentReport");
	}

    public ActionForward viewEDocBrowserInMasterFile(ActionMapping actionmapping,
            ActionForm actionform,
            HttpServletRequest request,
            HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
		UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.EDOC_BROWSER_IN_MASTER_FILE);

		String propValue="";
		if (prop == null){
			prop = new UserProperty();
		}else{
			propValue=prop.getValue();
		}

		boolean checked;
		if(propValue.equalsIgnoreCase("yes"))
			checked=true;
		else
			checked=false;

		prop.setChecked(checked);
		request.setAttribute("eDocBrowserInMasterFileProperty", prop);
		request.setAttribute("providertitle","provider.setEDocBrowserInMasterFile.title"); //=Select if you want to use Rx3
		request.setAttribute("providermsgPrefs","provider.setEDocBrowserInMasterFile.msgPrefs"); //=Preferences
		request.setAttribute("providermsgProvider","provider.setEDocBrowserInMasterFile.msgProfileView"); //=Use Rx3
		request.setAttribute("providermsgEdit","provider.setEDocBrowserInMasterFile.msgEdit"); //=Do you want to use Rx3?
		request.setAttribute("providerbtnSubmit","provider.setEDocBrowserInMasterFile.btnSubmit"); //=Save
		request.setAttribute("providermsgSuccess","provider.setEDocBrowserInMasterFile.msgSuccess"); //=Rx3 Selection saved
		request.setAttribute("method","saveEDocBrowserInMasterFile");

		frm.set("eDocBrowserInMasterFileProperty", prop);

		return actionmapping.findForward("genEDocBrowserInMasterFile");
	}


    public ActionForward saveEDocBrowserInMasterFile(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request,HttpServletResponse response){
    	DynaActionForm frm=(DynaActionForm)actionform;
    	UserProperty Uprop=(UserProperty)frm.get("eDocBrowserInMasterFileProperty");

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		boolean checked=false;
		if(Uprop!=null)
			checked = Uprop.isChecked();
		UserProperty prop=this.userPropertyDAO.getProp(providerNo, UserProperty.EDOC_BROWSER_IN_MASTER_FILE);
		if(prop==null){
			prop=new UserProperty();
			prop.setName(UserProperty.EDOC_BROWSER_IN_MASTER_FILE);
			prop.setProviderNo(providerNo);
		}
		String propValue="no";
		if(checked)
			propValue="yes";

		prop.setValue(propValue);
		this.userPropertyDAO.saveProp(prop);

		request.setAttribute("status", "success");
		request.setAttribute("eDocBrowserInMasterFileProperty",prop);
		request.setAttribute("providertitle","provider.setEDocBrowserInMasterFile.title");
		request.setAttribute("providermsgPrefs","provider.setEDocBrowserInMasterFile.msgPrefs");
		request.setAttribute("providermsgProvider","provider.setEDocBrowserInMasterFile.msgProfileView");
		request.setAttribute("providermsgEdit","provider.setEDocBrowserInMasterFile.msgEdit");
		request.setAttribute("providerbtnSubmit","provider.setEDocBrowserInMasterFile.btnSubmit");
		if(checked)
			request.setAttribute("providermsgSuccess","provider.setEDocBrowserInMasterFile.msgSuccess_selected");
		else
			request.setAttribute("providermsgSuccess","provider.setEDocBrowserInMasterFile.msgSuccess_unselected");
		request.setAttribute("method","saveEDocBrowserInMasterFile");

		return actionmapping.findForward("genEDocBrowserInMasterFile");
	}

    public ActionForward viewCommentLab(ActionMapping actionmapping,
            ActionForm actionform,
            HttpServletRequest request,
            HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
		UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.LAB_ACK_COMMENT);

		String propValue="";
		if (prop == null){
			prop = new UserProperty();
		}else{
			propValue=prop.getValue();
		}

		boolean checked;
		if(propValue.equalsIgnoreCase("yes"))
			checked=true;
		else
			checked=false;

		prop.setChecked(checked);
		request.setAttribute("labAckComment", prop);
		request.setAttribute("providertitle","provider.setAckComment.title");
		request.setAttribute("providermsgPrefs","provider.setAckComment.msgPrefs");
		request.setAttribute("providermsgProvider","provider.setAckComment.msgProfileView");
		request.setAttribute("providermsgEdit","provider.setAckComment.msgEdit");
		request.setAttribute("providerbtnSubmit","provider.setAckComment.btnSubmit");
		request.setAttribute("providermsgSuccess","provider.setAckComment.msgSuccess");
		request.setAttribute("method","saveCommentLab");

		frm.set("labAckCommentProperty", prop);

		return actionmapping.findForward("genAckCommentLab");
	}

    public ActionForward saveCommentLab(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request,HttpServletResponse response){
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm=(DynaActionForm)actionform;
    	UserProperty Uprop=(UserProperty)frm.get("labAckCommentProperty");

		boolean checked=false;
		if(Uprop!=null)
			checked = Uprop.isChecked();
		UserProperty prop=this.userPropertyDAO.getProp(providerNo, UserProperty.LAB_ACK_COMMENT);
		if(prop==null){
			prop=new UserProperty();
			prop.setName(UserProperty.LAB_ACK_COMMENT);
			prop.setProviderNo(providerNo);
		}
		String disableComment="no";
		if(checked)
			disableComment="yes";

		prop.setValue(disableComment);
		this.userPropertyDAO.saveProp(prop);

		request.setAttribute("status", "success");
		request.setAttribute("labAckComment", prop);
		request.setAttribute("providertitle","provider.setAckComment.title");
		request.setAttribute("providermsgPrefs","provider.setAckComment.msgPrefs");
		request.setAttribute("providermsgProvider","provider.setAckComment.msgProfileView");
		request.setAttribute("providermsgEdit","provider.setAckComment.msgEdit");
		request.setAttribute("providerbtnSubmit","provider.setAckComment.btnSubmit");
		request.setAttribute("providermsgSuccess","provider.setAckComment.msgSuccess");
		request.setAttribute("method","saveCommentLab");

		if(checked)
			request.setAttribute("providermsgSuccess","provider.setAckComment.msgSuccess_selected");
		else
			request.setAttribute("providermsgSuccess","provider.setAckComment.msgSuccess_unselected");


		return actionmapping.findForward("genAckCommentLab");
	}


    public ActionForward viewEncounterWindowSize(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request, HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
		UserProperty width = this.userPropertyDAO.getProp(providerNo, "encounterWindowWidth");
		UserProperty height = this.userPropertyDAO.getProp(providerNo, "encounterWindowHeight");
		UserProperty maximize = this.userPropertyDAO.getProp(providerNo, "encounterWindowMaximize");

		if (width == null){
			width = new UserProperty();
		}
		if (height == null){
			height = new UserProperty();
		}
		if (maximize == null){
			maximize = new UserProperty();
		}
		if(maximize.getValue()!=null) {
			maximize.setChecked(maximize.getValue().equals("yes")?true:false);
		}

		request.setAttribute("width",width);
		request.setAttribute("height",height);
		request.setAttribute("maximize",maximize);


		request.setAttribute("providertitle","provider.encounterWindowSize.title"); //=Set myDrugref ID
		request.setAttribute("providermsgPrefs","provider.encounterWindowSize.msgPrefs"); //=Preferences"); //
		request.setAttribute("providermsgProvider","provider.encounterWindowSize.msgProvider"); //=myDrugref ID
		request.setAttribute("providermsgEdit","provider.encounterWindowSize.msgEdit"); //=Enter your desired login for myDrugref
		request.setAttribute("providerbtnSubmit","provider.encounterWindowSize.btnSubmit"); //=Save
		request.setAttribute("providermsgSuccess","provider.encounterWindowSize.msgSuccess"); //=myDrugref Id saved
		request.setAttribute("method","saveEncounterWindowSize");

		frm.set("encounterWindowWidth", width);
		frm.set("encounterWindowHeight", height);
		frm.set("encounterWindowMaximize", maximize);

		return actionmapping.findForward("genEncounterWindowSize");
    }

    public ActionForward saveEncounterWindowSize(ActionMapping actionmapping,ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
		UserProperty w = (UserProperty)frm.get("encounterWindowWidth");
		UserProperty h = (UserProperty)frm.get("encounterWindowHeight");
		UserProperty m = (UserProperty)frm.get("encounterWindowMaximize");

		String width = w != null ? w.getValue() : "";
		String height = h != null ? h.getValue() : "";
		boolean maximize = m != null ? m.isChecked() : false;

		UserProperty wProperty = this.userPropertyDAO.getProp(providerNo,"encounterWindowWidth");
		if( wProperty == null ) {
			wProperty = new UserProperty();
			wProperty.setProviderNo(providerNo);
			wProperty.setName("encounterWindowWidth");
		}
		wProperty.setValue(width);
		userPropertyDAO.saveProp(wProperty);

		UserProperty hProperty = this.userPropertyDAO.getProp(providerNo,"encounterWindowHeight");
		if( hProperty == null ) {
			hProperty = new UserProperty();
			hProperty.setProviderNo(providerNo);
			hProperty.setName("encounterWindowHeight");
		}
		hProperty.setValue(height);
		userPropertyDAO.saveProp(hProperty);

		UserProperty mProperty = this.userPropertyDAO.getProp(providerNo,"encounterWindowMaximize");
		if( mProperty == null ) {
			mProperty = new UserProperty();
			mProperty.setProviderNo(providerNo);
			mProperty.setName("encounterWindowMaximize");
		}
		mProperty.setValue(maximize?"yes":"no");
		userPropertyDAO.saveProp(mProperty);

		request.setAttribute("status", "success");
		request.setAttribute("providertitle","provider.encounterWindowSize.title"); //=Set myDrugref ID
		request.setAttribute("providermsgPrefs","provider.encounterWindowSize.msgPrefs"); //=Preferences"); //
		request.setAttribute("providermsgProvider","provider.encounterWindowSize.msgProvider"); //=myDrugref ID
		request.setAttribute("providermsgEdit","provider.encounterWindowSize.msgEdit"); //=Enter your desired login for myDrugref
		request.setAttribute("providerbtnSubmit","provider.encounterWindowSize.btnSubmit"); //=Save
		request.setAttribute("providermsgSuccess","provider.encounterWindowSize.msgSuccess"); //=myDrugref Id saved
		request.setAttribute("method","saveEncounterWindowSize");

		return actionmapping.findForward("genEncounterWindowSize");
	}

    public ActionForward viewQuickChartSize(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request, HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
				UserProperty size = this.userPropertyDAO.getProp(providerNo, "quickChartSize");

		if (size == null){
			size = new UserProperty();
		}


		request.setAttribute("size",size);


		request.setAttribute("providertitle","provider.quickChartSize.title"); //=Set myDrugref ID
		request.setAttribute("providermsgPrefs","provider.quickChartSize.msgPrefs"); //=Preferences"); //
		request.setAttribute("providermsgProvider","provider.quickChartSize.msgProvider"); //=myDrugref ID
		request.setAttribute("providermsgEdit","provider.quickChartSize.msgEdit"); //=Enter your desired login for myDrugref
		request.setAttribute("providerbtnSubmit","provider.quickChartSize.btnSubmit"); //=Save
		request.setAttribute("providermsgSuccess","provider.quickChartSize.msgSuccess"); //=myDrugref Id saved
		request.setAttribute("method","saveQuickChartSize");

		frm.set("quickChartSize", size);

		return actionmapping.findForward("genQuickChartSize");
    }

    public ActionForward saveQuickChartSize(ActionMapping actionmapping,ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
		UserProperty s = (UserProperty)frm.get("quickChartSize");

		String size = s != null ? s.getValue() : "";

		UserProperty wProperty = this.userPropertyDAO.getProp(providerNo,"quickChartSize");
		if( wProperty == null ) {
			wProperty = new UserProperty();
			wProperty.setProviderNo(providerNo);
			wProperty.setName("quickChartsize");
		}
		wProperty.setValue(size);
		userPropertyDAO.saveProp(wProperty);


		request.setAttribute("status", "success");
		request.setAttribute("providertitle","provider.quickChartSize.title"); //=Set myDrugref ID
		request.setAttribute("providermsgPrefs","provider.quickChartSize.msgPrefs"); //=Preferences"); //
		request.setAttribute("providermsgProvider","provider.quickChartSize.msgProvider"); //=myDrugref ID
		request.setAttribute("providermsgEdit","provider.quickChartSize.msgEdit"); //=Enter your desired login for myDrugref
		request.setAttribute("providerbtnSubmit","provider.quickChartSize.btnSubmit"); //=Save
		request.setAttribute("providermsgSuccess","provider.quickChartSize.msgSuccess"); //=myDrugref Id saved
		request.setAttribute("method","saveQuickChartSize");

		return actionmapping.findForward("genQuickChartSize");
	}

    public ActionForward viewIntegratorProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
    	Facility facility = loggedInInfo.getCurrentFacility();
        UserProperty[] integratorProperties = new UserProperty[21];

        integratorProperties[0] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_SYNC);
        integratorProperties[1] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_ADMISSIONS);
        integratorProperties[2] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_ALLERGIES);
        integratorProperties[3] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_APPOINTMENTS);
        integratorProperties[4] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_BILLING);
        integratorProperties[5] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_CONSENT);
        integratorProperties[6] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_DOCUMENTS);
        integratorProperties[7] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_DRUGS);
        integratorProperties[8] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_DXRESEARCH);
        integratorProperties[9] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_EFORMS);
        integratorProperties[10] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_ISSUES);
        integratorProperties[11] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_LABREQ);
        integratorProperties[12] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_MEASUREMENTS);
        integratorProperties[13] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_NOTES);
        integratorProperties[14] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_PREVENTIONS);
        integratorProperties[15] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_FACILITY);
        integratorProperties[16] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_PROGRAMS);
        integratorProperties[17] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_PROVIDERS);
        integratorProperties[18] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_FULL_PUSH+facility.getId());
        integratorProperties[19] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_LAST_PUSH);
        integratorProperties[20] = this.userPropertyDAO.getProp(UserProperty.INTEGRATOR_PATIENT_CONSENT);
        
        request.setAttribute("integratorProperties", integratorProperties);
        return mapping.findForward("genIntegrator");
    }

    public ActionForward saveIntegratorProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
    	Facility facility = loggedInInfo.getCurrentFacility();
        this.userPropertyDAO.saveProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_ADMISSIONS, request.getParameter("integrator_demographic_admissions"));
        this.userPropertyDAO.saveProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_ALLERGIES, request.getParameter("integrator_demographic_allergies"));
        this.userPropertyDAO.saveProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_APPOINTMENTS, request.getParameter("integrator_demographic_appointments"));
        this.userPropertyDAO.saveProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_BILLING, request.getParameter("integrator_demographic_billing"));
        this.userPropertyDAO.saveProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_CONSENT, request.getParameter("integrator_demographic_consent"));
        this.userPropertyDAO.saveProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_DOCUMENTS, request.getParameter("integrator_demographic_documents"));
        this.userPropertyDAO.saveProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_DRUGS, request.getParameter("integrator_demographic_drugs"));
        this.userPropertyDAO.saveProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_DXRESEARCH, request.getParameter("integrator_demographic_dxresearch"));
        this.userPropertyDAO.saveProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_EFORMS, request.getParameter("integrator_demographic_eforms"));
        this.userPropertyDAO.saveProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_ISSUES, request.getParameter("integrator_demographic_issues"));
        this.userPropertyDAO.saveProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_LABREQ, request.getParameter("integrator_demographic_labreq"));
        this.userPropertyDAO.saveProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_MEASUREMENTS, request.getParameter("integrator_demographic_measurements"));
        this.userPropertyDAO.saveProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_NOTES, request.getParameter("integrator_demographic_notes"));
        this.userPropertyDAO.saveProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_PREVENTIONS, request.getParameter("integrator_demographic_preventions"));
        this.userPropertyDAO.saveProp(UserProperty.INTEGRATOR_DEMOGRAPHIC_SYNC, request.getParameter("integrator_demographic_sync"));               
        this.userPropertyDAO.saveProp(UserProperty.INTEGRATOR_FACILITY, request.getParameter("integrator_facility"));
        this.userPropertyDAO.saveProp(UserProperty.INTEGRATOR_PROGRAMS, request.getParameter("integrator_programs"));
        this.userPropertyDAO.saveProp(UserProperty.INTEGRATOR_PROVIDERS, request.getParameter("integrator_providers"));
        
        this.userPropertyDAO.saveProp(UserProperty.INTEGRATOR_PATIENT_CONSENT, 
        		( request.getParameter(UserProperty.INTEGRATOR_PATIENT_CONSENT) != null ) ? request.getParameter(UserProperty.INTEGRATOR_PATIENT_CONSENT) : "0" );

        this.userPropertyDAO.saveProp(UserProperty.INTEGRATOR_FULL_PUSH+facility.getId(), request.getParameter("integrator_full_push"));

        request.setAttribute("saved", true);

        return viewIntegratorProperties(mapping, form, request, response);
    }
 public ActionForward viewPatientNameLength(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request, HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		DynaActionForm frm = (DynaActionForm)actionform;
		UserProperty length = this.userPropertyDAO.getProp(providerNo, UserProperty.PATIENT_NAME_LENGTH);

		if (length == null){
			length = new UserProperty();
		}


		request.setAttribute("patientnameLength",length);


		request.setAttribute("providertitle","provider.patientNameLength.title"); 
		request.setAttribute("providermsgPrefs","provider.patientNameLength.msgPrefs"); 
		request.setAttribute("providermsgProvider","provider.patientNameLength.msgProvider"); 
		request.setAttribute("providermsgEdit","provider.patientNameLength.msgEdit"); 
		request.setAttribute("providerbtnSubmit","provider.patientNameLength.btnSubmit"); 
		request.setAttribute("providermsgSuccess","provider.patientNameLength.msgSuccess"); 
		request.setAttribute("method","savePatientNameLength");

		frm.set("patientNameLength", length);

		return actionmapping.findForward("genPatientNameLength");
    }

    public ActionForward savePatientNameLength(ActionMapping actionmapping,ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		DynaActionForm frm = (DynaActionForm)actionform;
		UserProperty s = (UserProperty)frm.get("patientNameLength");

		String length = s != null ? s.getValue() : "";

		UserProperty wProperty = this.userPropertyDAO.getProp(providerNo,UserProperty.PATIENT_NAME_LENGTH);
		if( wProperty == null ) {
			wProperty = new UserProperty();
			wProperty.setProviderNo(providerNo);
			wProperty.setName(UserProperty.PATIENT_NAME_LENGTH);
		}
		wProperty.setValue(length);
		userPropertyDAO.saveProp(wProperty);


		request.setAttribute("status", "success");
		request.setAttribute("providertitle","provider.patientNameLength.title"); //=Set myDrugref ID
		request.setAttribute("providermsgPrefs","provider.patientNameLength.msgPrefs"); //=Preferences"); //
		request.setAttribute("providermsgProvider","provider.patientNameLength.msgProvider"); //=myDrugref ID
		request.setAttribute("providermsgEdit","provider.patientNameLength.msgEdit"); //=Enter your desired login for myDrugref
		request.setAttribute("providerbtnSubmit","provider.patientNameLength.btnSubmit"); //=Save
		request.setAttribute("providermsgSuccess","provider.patientNameLength.msgSuccess"); //=myDrugref Id saved
		request.setAttribute("method","savePatientNameLength");

		return actionmapping.findForward("genPatientNameLength");
	}
    
     public ActionForward viewDisplayDocumentAs(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

         DynaActionForm frm = (DynaActionForm)actionform;
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
         UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.DISPLAY_DOCUMENT_AS);

         if (prop == null){
             prop = new UserProperty();
         }

         ArrayList<LabelValueBean> serviceList = new ArrayList<LabelValueBean>();
         serviceList.add(new LabelValueBean(UserProperty.PDF, UserProperty.PDF));
         serviceList.add(new LabelValueBean(UserProperty.IMAGE, UserProperty.IMAGE));

         request.setAttribute("dropOpts",serviceList);

         request.setAttribute("displayDocumentAsProperty",prop);

         request.setAttribute("providertitle","provider.displayDocumentAs.title");
         request.setAttribute("providermsgPrefs","provider.displayDocumentAs.msgPrefs");
         request.setAttribute("providermsgProvider","provider.displayDocumentAs.msgProvider");
         request.setAttribute("providermsgEdit","provider.displayDocumentAs.msgEdit");
         request.setAttribute("providerbtnSubmit","provider.displayDocumentAs.btnSubmit");
         request.setAttribute("providermsgSuccess","provider.displayDocumentAs.msgSuccess");
         request.setAttribute("method","saveDisplayDocumentAs");

         frm.set("displayDocumentAsProperty", prop);
         return actionmapping.findForward("genDisplayDocumentAs");
     }

    public ActionForward saveDisplayDocumentAs(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		
		DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty prop = (UserProperty)frm.get("displayDocumentAsProperty");
         String fmt = prop != null ? prop.getValue() : "";
         UserProperty saveProperty = this.userPropertyDAO.getProp(providerNo,UserProperty.DISPLAY_DOCUMENT_AS);

         if( saveProperty == null ) {
             saveProperty = new UserProperty();
             saveProperty.setProviderNo(providerNo);
             saveProperty.setName(UserProperty.DISPLAY_DOCUMENT_AS);
         }

         saveProperty.setValue(fmt);
         this.userPropertyDAO.saveProp(saveProperty);

         request.setAttribute("status", "success");
         request.setAttribute("providertitle","provider.displayDocumentAs.title");
         request.setAttribute("providermsgPrefs","provider.displayDocumentAs.msgPrefs");
         request.setAttribute("providermsgProvider","provider.displayDocumentAs.msgProvider");
         request.setAttribute("providermsgEdit","provider.displayDocumentAs.msgEdit");
         request.setAttribute("providerbtnSubmit","provider.btnSubmit");
         request.setAttribute("providermsgSuccess","provider.displayDocumentAs.msgSuccess");
         request.setAttribute("method","saveDisplayDocumentAs");

         return actionmapping.findForward("genDisplayDocumentAs");
    }
    
    

    public ActionForward viewCobalt(ActionMapping actionmapping, ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm frm = (DynaActionForm)actionform;
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.COBALT);

		String propValue="";
		if (prop == null){
			prop = new UserProperty();
		}else{
			propValue=prop.getValue();
		}

		boolean checked;
		if(propValue.equalsIgnoreCase("yes"))
			checked=true;
		else
			checked=false;

		prop.setChecked(checked);
		request.setAttribute("cobaltProperty", prop);
		request.setAttribute("providertitle","provider.cobalt.title"); //=Select if you want to use Rx3
		request.setAttribute("providermsgPrefs","provider.cobalt.msgPrefs"); //=Preferences
		request.setAttribute("providermsgProvider","provider.cobalt.msgProfileView"); //=Use Rx3
		request.setAttribute("providermsgEdit","provider.cobalt.msgEdit"); //=Do you want to use Rx3?
		request.setAttribute("providerbtnSubmit","provider.cobalt.btnSubmit"); //=Save
		request.setAttribute("providermsgSuccess","provider.cobalt.msgSuccess"); //=Rx3 Selection saved
		request.setAttribute("method","saveCobalt");

		frm.set("cobaltProperty", prop);

		return actionmapping.findForward("genCobalt");
    }
    

    public ActionForward saveCobalt(ActionMapping actionmapping, ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
    	DynaActionForm frm=(DynaActionForm)actionform;
    	UserProperty Uprop=(UserProperty)frm.get("cobaltProperty");

		boolean checked=false;
		if(Uprop!=null)
			checked = Uprop.isChecked();
		UserProperty prop=this.userPropertyDAO.getProp(providerNo, UserProperty.COBALT);
		if(prop==null){
			prop=new UserProperty();
			prop.setName(UserProperty.COBALT);
			prop.setProviderNo(providerNo);
		}
		String propValue="no";
		if(checked)
			propValue="yes";

		prop.setValue(propValue);
		this.userPropertyDAO.saveProp(prop);

		request.setAttribute("status", "success");
		request.setAttribute("cobaltProperty",prop);
		request.setAttribute("providertitle","provider.cobalt.title");
		request.setAttribute("providermsgPrefs","provider.cobalt.msgPrefs");
		request.setAttribute("providermsgProvider","provider.cobalt.msgProfileView");
		request.setAttribute("providermsgEdit","provider.cobalt.msgEdit");
		request.setAttribute("providerbtnSubmit","provider.cobalt.btnSubmit");
		if(checked)
			request.setAttribute("providermsgSuccess","provider.cobalt.msgSuccess_selected");
		else
			request.setAttribute("providermsgSuccess","provider.cobalt.msgSuccess_unselected");
		request.setAttribute("method","saveCobalt");

		return actionmapping.findForward("genCobalt");
    }
    
    
    public ActionForward viewHideOldEchartLinkInAppt(ActionMapping actionmapping, ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm frm = (DynaActionForm)actionform;
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.HIDE_OLD_ECHART_LINK_IN_APPT);

		String propValue="";
		if (prop == null){
			prop = new UserProperty();
		}else{
			propValue=prop.getValue();
		}

		boolean checked;
		if(propValue.equals("Y"))
			checked=true;
		else
			checked=false;

		prop.setChecked(checked);
		request.setAttribute("hideOldEchartLinkInApptProperty", prop);
		request.setAttribute("providertitle","provider.hideOldEchartLinkInAppt.title"); //=Hide Old Echart Link in Appointment
		request.setAttribute("providermsgPrefs","provider.hideOldEchartLinkInAppt.msgPrefs"); //=Preferences
		request.setAttribute("providerbtnSubmit","provider.hideOldEchartLinkInAppt.btnSubmit"); //=Save
		request.setAttribute("providerbtnCancel","provider.hideOldEchartLinkInAppt.btnCancel"); //=Cancel
		request.setAttribute("method","saveHideOldEchartLinkInAppt");

		frm.set("hideOldEchartLinkInApptProperty", prop);

		return actionmapping.findForward("genHideOldEchartLinkInAppt");
    }
    

    public ActionForward saveHideOldEchartLinkInAppt(ActionMapping actionmapping, ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
    	DynaActionForm frm=(DynaActionForm)actionform;
    	UserProperty Uprop=(UserProperty)frm.get("hideOldEchartLinkInApptProperty");

		boolean checked=false;
		if(Uprop!=null)
			checked = Uprop.isChecked();
		UserProperty prop=this.userPropertyDAO.getProp(providerNo, UserProperty.HIDE_OLD_ECHART_LINK_IN_APPT);
		if(prop==null){
			prop=new UserProperty();
			prop.setName(UserProperty.HIDE_OLD_ECHART_LINK_IN_APPT);
			prop.setProviderNo(providerNo);
		}
		String propValue="N";
		if(checked) propValue="Y";

		prop.setValue(propValue);
		this.userPropertyDAO.saveProp(prop);

		request.setAttribute("status", "success");
		request.setAttribute("hideOldEchartLinkInApptProperty",prop);
		request.setAttribute("providertitle","provider.hideOldEchartLinkInAppt.title"); //=Hide Old Echart Link in Appointment
		request.setAttribute("providermsgPrefs","provider.hideOldEchartLinkInAppt.msgPrefs"); //=Preferences
		request.setAttribute("providerbtnClose","provider.hideOldEchartLinkInAppt.btnClose"); //=Close
		if(checked)
			request.setAttribute("providermsgSuccess","provider.hideOldEchartLinkInAppt.msgSuccess_selected"); //=Old Echart Link Hidden in Appointment
		else
			request.setAttribute("providermsgSuccess","provider.hideOldEchartLinkInAppt.msgSuccess_unselected"); //Old Echart Link Shown in Appointment
		request.setAttribute("method","saveHideOldEchartLinkInAppt");

		return actionmapping.findForward("genHideOldEchartLinkInAppt");
    }

    
    public ActionForward viewDashboardPrefs(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request, HttpServletResponse response) {
    	DynaActionForm frm = (DynaActionForm)actionform;
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.DASHBOARD_SHARE);

		String propValue="";
		if (prop == null){
			prop = new UserProperty();
		}else{
			propValue=prop.getValue();
		}

		boolean checked;
		if(propValue.equals("true"))
			checked=true;
		else
			checked=false;

		prop.setChecked(checked);
		request.setAttribute("dashboardShareProperty", prop);
		request.setAttribute("providertitle","provider.dashboardPrefs.title"); 
		request.setAttribute("providermsgPrefs","provider.dashboardPrefs.msgPrefs"); //=Preferences
		request.setAttribute("providerbtnSubmit","provider.dashboardPrefs.btnSubmit"); //=Save
		request.setAttribute("providerbtnCancel","provider.dashboardPrefs.btnCancel"); //=Cancel
		request.setAttribute("method","saveDashboardPrefs");

		frm.set("dashboardShareProperty", prop);

		return actionmapping.findForward("genDashboardPrefs");
    }

    public ActionForward saveDashboardPrefs(ActionMapping actionmapping,ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {
    	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
    	DynaActionForm frm=(DynaActionForm)actionform;
    	UserProperty Uprop=(UserProperty)frm.get("dashboardShareProperty");

		boolean checked=false;
		if(Uprop!=null)
			checked = Uprop.isChecked();
		UserProperty prop=this.userPropertyDAO.getProp(providerNo, UserProperty.DASHBOARD_SHARE);
		if(prop==null){
			prop=new UserProperty();
			prop.setName(UserProperty.DASHBOARD_SHARE);
			prop.setProviderNo(providerNo);
		}
		String propValue="false";
		if(checked) propValue="true";

		prop.setValue(propValue);
		this.userPropertyDAO.saveProp(prop);

		request.setAttribute("status", "success");
		request.setAttribute("dashboardShareProperty",prop);
		request.setAttribute("providertitle","provider.dashboardPrefs.title"); 
		request.setAttribute("providermsgPrefs","provider.dashboardPrefs.msgPrefs"); //=Preferences
		request.setAttribute("providerbtnClose","provider.dashboardPrefs.btnClose"); //=Close
		if(checked)
			request.setAttribute("providermsgSuccess","provider.dashboardPrefs.msgSuccess_selected"); 
		else
			request.setAttribute("providermsgSuccess","provider.dashboardPrefs.msgSuccess_unselected"); 
		request.setAttribute("method","saveDashboardPrefs");

		return actionmapping.findForward("genDashboardPrefs");
	}

    public ActionForward viewAppointmentCardPrefs(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request, HttpServletResponse response) {

		DynaActionForm frm = (DynaActionForm)actionform;
		String provider = (String) request.getSession().getAttribute("user");

		UserProperty name = this.userPropertyDAO.getProp(provider, "APPT_CARD_NAME");
		UserProperty phone = this.userPropertyDAO.getProp(provider, "APPT_CARD_PHONE");
		UserProperty fax = this.userPropertyDAO.getProp(provider, "APPT_CARD_FAX");

		if (name == null){
			name = new UserProperty();
		}
		if (phone == null){
			phone = new UserProperty();
		}
		if (fax == null){
			fax = new UserProperty();
		}
		
		request.setAttribute("appointmentCardName",name);
		request.setAttribute("appointmentCardPhone",phone);
		request.setAttribute("appointmentCardFax",fax);


		request.setAttribute("providertitle","provider.appointmentCardPrefs.title"); //=Set myDrugref ID
		request.setAttribute("providermsgPrefs","provider.appointmentCardPrefs.msgPrefs"); //=Preferences"); //
		request.setAttribute("providermsgProvider","provider.appointmentCardPrefs.msgProvider"); //=myDrugref ID
		request.setAttribute("providermsgEdit","provider.appointmentCardPrefs.msgEdit"); //=Enter your desired login for myDrugref
		request.setAttribute("providerbtnSubmit","provider.appointmentCardPrefs.btnSubmit"); //=Save
		request.setAttribute("providermsgSuccess","provider.appointmentCardPrefs.msgSuccess"); //=myDrugref Id saved
		request.setAttribute("method","saveAppointmentCardPrefs");

		frm.set("appointmentCardName", name);
		frm.set("appointmentCardPhone", phone);
		frm.set("appointmentCardFax", fax);

		return actionmapping.findForward("genAppointmentCardPrefs");
    }

    public ActionForward saveAppointmentCardPrefs(ActionMapping actionmapping,ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {

		DynaActionForm frm = (DynaActionForm)actionform;
		UserProperty n = (UserProperty)frm.get("appointmentCardName");
		UserProperty p = (UserProperty)frm.get("appointmentCardPhone");
		UserProperty f = (UserProperty)frm.get("appointmentCardFax");

		String name = n != null ? n.getValue() : "";
		String phone = p != null ? p.getValue() : "";
		String fax = f != null ? f.getValue() : "";
		
		String provider = (String) request.getSession().getAttribute("user");

		UserProperty wProperty = this.userPropertyDAO.getProp(provider,"APPT_CARD_NAME");
		if( wProperty == null ) {
			wProperty = new UserProperty();
			wProperty.setProviderNo(provider);
			wProperty.setName("APPT_CARD_NAME");
		}
		wProperty.setValue(name);

		userPropertyDAO.saveProp(wProperty);

		UserProperty hProperty = this.userPropertyDAO.getProp(provider,"APPT_CARD_PHONE");
		if( hProperty == null ) {
			hProperty = new UserProperty();
			hProperty.setProviderNo(provider);
			hProperty.setName("APPT_CARD_PHONE");
		}
		hProperty.setValue(phone);
		userPropertyDAO.saveProp(hProperty);

		UserProperty mProperty = this.userPropertyDAO.getProp(provider,"APPT_CARD_FAX");
		if( mProperty == null ) {
			mProperty = new UserProperty();
			mProperty.setProviderNo(provider);
			mProperty.setName("APPT_CARD_FAX");
		}
		mProperty.setValue(fax);
		userPropertyDAO.saveProp(mProperty);

		request.setAttribute("status", "success");
		request.setAttribute("providertitle","provider.appointmentCardPrefs.title"); //=Set myDrugref ID
		request.setAttribute("providermsgPrefs","provider.appointmentCardPrefs.msgPrefs"); //=Preferences"); //
		request.setAttribute("providermsgProvider","provider.appointmentCardPrefs.msgProvider"); //=myDrugref ID
		request.setAttribute("providermsgEdit","provider.appointmentCardPrefs.msgEdit"); //=Enter your desired login for myDrugref
		request.setAttribute("providerbtnSubmit","provider.appointmentCardPrefs.btnSubmit"); //=Save
		request.setAttribute("providermsgSuccess","provider.appointmentCardPrefs.msgSuccess"); //=myDrugref Id saved
		request.setAttribute("method","saveAppointmentCardPrefs");

		return actionmapping.findForward("genAppointmentCardPrefs");
	}
    
    public ActionForward viewBornPrefs(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request, HttpServletResponse response) {
    	DynaActionForm frm = (DynaActionForm)actionform;
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.DISABLE_BORN_PROMPTS);

		String propValue="";
		if (prop == null){
			prop = new UserProperty();
		}else{
			propValue=prop.getValue();
		}

		boolean checked;
		if(propValue.equals("Y"))
			checked=true;
		else
			checked=false;

		prop.setChecked(checked);
		request.setAttribute("bornPromptsProperty", prop);
		request.setAttribute("providertitle","provider.bornPrefs.title"); 
		request.setAttribute("providermsgPrefs","provider.bornPrefs.msgPrefs"); //=Preferences
		request.setAttribute("providerbtnSubmit","provider.bornPrefs.btnSubmit"); //=Save
		request.setAttribute("providerbtnCancel","provider.bornPrefs.btnCancel"); //=Cancel
		request.setAttribute("method","saveBornPrefs");

		frm.set("bornPromptsProperty", prop);

		return actionmapping.findForward("genBornPrefs");
    }

    public ActionForward saveBornPrefs(ActionMapping actionmapping,ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {
    	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
    	DynaActionForm frm=(DynaActionForm)actionform;
    	UserProperty Uprop=(UserProperty)frm.get("bornPromptsProperty");

		boolean checked=false;
		if(Uprop!=null)
			checked = Uprop.isChecked();
		UserProperty prop=this.userPropertyDAO.getProp(providerNo, UserProperty.DISABLE_BORN_PROMPTS);
		if(prop==null){
			prop=new UserProperty();
			prop.setName(UserProperty.DISABLE_BORN_PROMPTS);
			prop.setProviderNo(providerNo);
		}
		String propValue="N";
		if(checked) propValue="Y";

		prop.setValue(propValue);
		this.userPropertyDAO.saveProp(prop);

		request.setAttribute("status", "success");
		request.setAttribute("bornPromptsProperty",prop);
		request.setAttribute("providertitle","provider.bornPrefs.title"); 
		request.setAttribute("providermsgPrefs","provider.bornPrefs.msgPrefs"); //=Preferences
		request.setAttribute("providerbtnClose","provider.bornPrefs.btnClose"); //=Close
		if(checked)
			request.setAttribute("providermsgSuccess","provider.bornPrefs.msgSuccess_selected"); 
		else
			request.setAttribute("providermsgSuccess","provider.bornPrefs.msgSuccess_unselected"); 
		request.setAttribute("method","saveBornPrefs");

		return actionmapping.findForward("genBornPrefs");
	}
    
    
    public ActionForward viewTicklerProviderEmailPrefs(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request, HttpServletResponse response) {
    	DynaActionForm frm = (DynaActionForm)actionform;
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		UserProperty prop = this.userPropertyDAO.getProp(providerNo, UserProperty.TICKLER_EMAIL_PROVIDER);

		String propValue="";
		if (prop == null){
			prop = new UserProperty();
		}else{
			propValue=prop.getValue();
		}

		boolean checked;
		if(propValue.equals("Y"))
			checked=true;
		else
			checked=false;

		prop.setChecked(checked);
		request.setAttribute("ticklerEmailProviderProperty", prop);
		request.setAttribute("providertitle","provider.ticklerEmailProviderPrefs.title"); 
		request.setAttribute("providermsgPrefs","provider.ticklerEmailProviderPrefs.msgPrefs"); //=Preferences
		request.setAttribute("providerbtnSubmit","provider.ticklerEmailProviderPrefs.btnSubmit"); //=Save
		request.setAttribute("providerbtnCancel","provider.ticklerEmailProviderPrefs.btnCancel"); //=Cancel
		request.setAttribute("method","saveTicklerEmailProviderPrefs");

		frm.set("ticklerEmailProviderProperty", prop);

		return actionmapping.findForward("genTicklerEmailProviderPrefs");
    }

    public ActionForward saveTicklerEmailProviderPrefs(ActionMapping actionmapping,ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {
    	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
    	DynaActionForm frm=(DynaActionForm)actionform;
    	UserProperty Uprop=(UserProperty)frm.get("ticklerEmailProviderProperty");

		boolean checked=false;
		if(Uprop!=null)
			checked = Uprop.isChecked();
		UserProperty prop=this.userPropertyDAO.getProp(providerNo, UserProperty.TICKLER_EMAIL_PROVIDER);
		if(prop==null){
			prop=new UserProperty();
			prop.setName(UserProperty.TICKLER_EMAIL_PROVIDER);
			prop.setProviderNo(providerNo);
		}
		String propValue="N";
		if(checked) propValue="Y";

		prop.setValue(propValue);
		this.userPropertyDAO.saveProp(prop);

		request.setAttribute("status", "success");
		request.setAttribute("ticklerEmailProviderProperty",prop);
		request.setAttribute("providertitle","provider.ticklerEmailProviderPrefs.title"); 
		request.setAttribute("providermsgPrefs","provider.ticklerEmailProviderPrefs.msgPrefs"); //=Preferences
		request.setAttribute("providerbtnClose","provider.ticklerEmailProviderPrefs.btnClose"); //=Close
		if(checked)
			request.setAttribute("providermsgSuccess","provider.ticklerEmailProviderPrefs.msgSuccess_selected"); 
		else
			request.setAttribute("providermsgSuccess","provider.ticklerEmailProviderPrefs.msgSuccess_unselected"); 
		request.setAttribute("method","saveTicklerEmailProviderPrefs");

		return actionmapping.findForward("genTicklerEmailProviderPrefs");
	}
    
    /**
     * Creates a new instance of ProviderPropertyAction
     */
    public ProviderPropertyAction() {
    }
}
