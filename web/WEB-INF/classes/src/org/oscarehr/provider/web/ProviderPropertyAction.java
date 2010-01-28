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
import java.util.Collection;
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
             prop.setProviderNo(provider);
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
    public ActionForward viewRxPageSize(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

         DynaActionForm frm = (DynaActionForm)actionform;
         String provider = (String) request.getSession().getAttribute("user");
         System.out.println("provider # "+provider);
         UserProperty prop = this.userPropertyDAO.getProp(provider, UserProperty.RX_PAGE_SIZE);


         if (prop == null){
             prop = new UserProperty();
         }

         //request.setAttribute("propert",propertyToSet);
         //request.setAttribute("dateProperty",prop);


         request.setAttribute("providertitle","provider.setRxPageSize.title"); //=Set Rx Script Page Size
         request.setAttribute("providermsgPrefs","provider.setRxPageSize.msgPrefs"); //=Preferences"); //
         request.setAttribute("providermsgProvider","provider.setRxPageSize.msgPageSize"); //=Rx Script Page Size
         request.setAttribute("providermsgEdit","provider.setRxPageSize.msgEdit"); //=Select your desired page size
         request.setAttribute("providerbtnSubmit","provider.setRxPageSize.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setRxPageSize.msgSuccess"); //=Rx Script Page Size saved
         request.setAttribute("method","saveRxPageSize");

         frm.set("rxPageSizeProperty", prop);System.out.println("Finish in viewRxPageSize");
         return actionmapping.findForward("genRxPageSize");
     }

   public ActionForward saveRxPageSize(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request,HttpServletResponse response){

        String provider=(String) request.getSession().getAttribute("user");

        DynaActionForm frm=(DynaActionForm)actionform;
        UserProperty UPageSize=(UserProperty)frm.get("rxPageSizeProperty");
        String rxPageSize="";
        if(UPageSize!=null)
            rxPageSize=UPageSize.getValue();
        UserProperty prop=this.userPropertyDAO.getProp(provider, UserProperty.RX_PAGE_SIZE);
        if(prop==null){
            prop=new UserProperty();
            prop.setName(UserProperty.RX_PAGE_SIZE);
            prop.setProviderNo(provider);
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
         System.out.println("Finish in saveRxPageSize");
         return actionmapping.findForward("genRxPageSize");
    }


   public ActionForward viewRxProfileView(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        // System.out.println(" in viewProfileView");
         DynaActionForm frm = (DynaActionForm)actionform;
         String provider = (String) request.getSession().getAttribute("user");
         //System.out.println("provider # "+provider);
         UserProperty prop = this.userPropertyDAO.getProp(provider, UserProperty.RX_PROFILE_VIEW);

         String propValue="";
         if (prop == null){
             prop = new UserProperty();
         }else{
            propValue=prop.getValue();
         }

         String [] propertyArray= new String[7];
         String [] va={" show_current "," show_all "," active "," inactive "," all "," longterm_acute "," longterm_acute_inactive "};

         for(int i=0;i<propertyArray.length;i++){
             System.out.println(propValue +"--"+va[i]);
             if(propValue.contains(va[i]))  { System.out.println("contains");  propertyArray[i]=va[i].trim();}//element of array has to match exactly with viewChoices values
         }
         prop.setValueArray(propertyArray);
         Collection viewChoices=new ArrayList();
         viewChoices.add(new LabelValueBean("Show Current","show_current"));
         viewChoices.add(new LabelValueBean("Show All","show_all"));
         viewChoices.add(new LabelValueBean("Show Active","active"));
         viewChoices.add(new LabelValueBean("Show Inactive","inactive"));
         viewChoices.add(new LabelValueBean("All","all"));
         viewChoices.add(new LabelValueBean("Show Longterm/Acute","longterm_acute"));
         viewChoices.add(new LabelValueBean("Show Longterm/Acute/Inactive","longterm_acute_inactive"));
         request.setAttribute("viewChoices", viewChoices);
         request.setAttribute("providertitle","provider.setRxProfileView.title"); //=Set Rx Profile View
         request.setAttribute("providermsgPrefs","provider.setRxProfileView.msgPrefs"); //=Preferences
         request.setAttribute("providermsgProvider","provider.setRxProfileView.msgProfileView"); //=Rx Profile View
         request.setAttribute("providermsgEdit","provider.setRxProfileView.msgEdit"); //=Select your desired display
         request.setAttribute("providerbtnSubmit","provider.setRxProfileView.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setRxProfileView.msgSuccess"); //=Rx Profile View saved
         request.setAttribute("method","saveRxProfileView");

         frm.set("rxProfileViewProperty", prop);
         //System.out.println("Finish in viewProfileView");
         return actionmapping.findForward("genRxProfileView");
     }

   public ActionForward saveRxProfileView(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request,HttpServletResponse response){
        //System.out.println(" in saveProfileView");
        String provider=(String) request.getSession().getAttribute("user");

        DynaActionForm frm=(DynaActionForm)actionform;
        UserProperty UProfileView=(UserProperty)frm.get("rxProfileViewProperty");
        String[] va=new String[7];
        if(UProfileView!=null)
            va=UProfileView.getValueArray();
        UserProperty prop=this.userPropertyDAO.getProp(provider, UserProperty.RX_PROFILE_VIEW);
        if(prop==null){
            prop=new UserProperty();
            prop.setName(UserProperty.RX_PROFILE_VIEW);
            prop.setProviderNo(provider);
        }

        String rxProfileView="";
        for(int i=0;i<va.length;i++){
            rxProfileView+=" "+va[i]+" ";
        }
        System.out.println("rxProfileView="+rxProfileView);
        prop.setValue(rxProfileView);
        this.userPropertyDAO.saveProp(prop);

         request.setAttribute("status", "success");
         request.setAttribute("rxProfileViewProperty",prop);
         request.setAttribute("providertitle","provider.setRxProfileView.title"); //=Set Rx Profile View
         request.setAttribute("providermsgPrefs","provider.setRxProfileView.msgPrefs"); //=Preferences
         request.setAttribute("providermsgProvider","provider.setRxProfileView.msgProfileView"); //=Rx Profile View
         request.setAttribute("providermsgEdit","provider.setRxProfileView.msgEdit"); //=Select your desired display
         request.setAttribute("providerbtnSubmit","provider.setRxProfileView.btnSubmit"); //=Save
         request.setAttribute("providermsgSuccess","provider.setRxProfileView.msgSuccess"); //=Rx Profile View saved
         request.setAttribute("method","saveRxProfileView");
         //System.out.println("Finish in saveProfileView");
         return actionmapping.findForward("genRxProfileView");
    }

      public ActionForward viewUseRx3(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {
         System.out.println(" in viewProfileView");
         DynaActionForm frm = (DynaActionForm)actionform;
         String provider = (String) request.getSession().getAttribute("user");
         UserProperty prop = this.userPropertyDAO.getProp(provider, UserProperty.RX_USE_RX3);

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
         System.out.println("Finish in viewProfileView");
         return actionmapping.findForward("genRxUseRx3");
     }

   public ActionForward saveUseRx3(ActionMapping actionmapping,ActionForm actionform,HttpServletRequest request,HttpServletResponse response){
        System.out.println(" in saveUseRx3");
        String provider=(String) request.getSession().getAttribute("user");

        DynaActionForm frm=(DynaActionForm)actionform;
        UserProperty UUseRx3=(UserProperty)frm.get("rxUseRx3Property");
        //UserProperty UUseRx3=(UserProperty)request.getAttribute("rxUseRx3Property");
        if(UUseRx3!=null)
            System.out.println("ischecked by user? "+UUseRx3.isChecked());
        else System.out.println("UUseRx3 is null ");

        boolean checked=false;
        if(UUseRx3!=null)
            checked = UUseRx3.isChecked();
        UserProperty prop=this.userPropertyDAO.getProp(provider, UserProperty.RX_USE_RX3);
        if(prop==null){
            prop=new UserProperty();
            prop.setName(UserProperty.RX_USE_RX3);
            prop.setProviderNo(provider);
        }
        String useRx3="no";
        if(checked)
            useRx3="yes";
        System.out.println("useRx3="+useRx3);
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
         System.out.println("Finish in saveUseRx3");
         return actionmapping.findForward("genRxUseRx3");
    }

       public ActionForward viewDefaultQuantity(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {


         DynaActionForm frm = (DynaActionForm)actionform;
         String provider = (String) request.getSession().getAttribute("user");
         System.out.println("provider # "+provider);
         UserProperty prop = this.userPropertyDAO.getProp(provider, UserProperty.RX_DEFAULT_QUANTITY);


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

        String provider=(String) request.getSession().getAttribute("user");

        DynaActionForm frm=(DynaActionForm)actionform;
        UserProperty UDefaultQuantity=(UserProperty)frm.get("rxDefaultQuantityProperty");
        String rxDefaultQuantity="";
        if(UDefaultQuantity!=null)
            rxDefaultQuantity=UDefaultQuantity.getValue();
        UserProperty prop=this.userPropertyDAO.getProp(provider, UserProperty.RX_DEFAULT_QUANTITY);
        if(prop==null){
            prop=new UserProperty();
            prop.setName(UserProperty.RX_DEFAULT_QUANTITY);
            prop.setProviderNo(provider);
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
         System.out.println("Finish in saveDefaultQuantity");
         return actionmapping.findForward("genRxDefaultQuantity");

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
             prop.setProviderNo(provider);
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
         String provider = (String) request.getSession().getAttribute("user");
         //System.out.println("provider # "+provider);
         UserProperty prop = this.userPropertyDAO.getProp(provider, UserProperty.ONTARIO_MD_USERNAME);
         UserProperty prop2 = this.userPropertyDAO.getProp(provider, UserProperty.ONTARIO_MD_PASSWORD);

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
         String provider = (String) request.getSession().getAttribute("user");
         //System.out.println("provider # "+provider);
         DynaActionForm frm = (DynaActionForm)actionform;
         UserProperty  UdrugrefId = (UserProperty)frm.get("dateProperty");
         String drugrefId = "";

         if (UdrugrefId != null){
             drugrefId = UdrugrefId.getValue();
         }

         UserProperty prop = this.userPropertyDAO.getProp(provider, UserProperty.ONTARIO_MD_USERNAME);

         if (prop ==null){
             prop = new UserProperty();
             prop.setName(UserProperty.ONTARIO_MD_USERNAME);
             prop.setProviderNo(provider);
         }
         prop.setValue(drugrefId);

         this.userPropertyDAO.saveProp(prop);


         UserProperty  UdrugrefId2 = (UserProperty)frm.get("dateProperty2");
         String drugrefId2 = "";

         if (UdrugrefId2 != null){
             drugrefId2 = UdrugrefId2.getValue();
         }

         UserProperty prop2 = this.userPropertyDAO.getProp(provider, UserProperty.ONTARIO_MD_PASSWORD);

         if (prop2 ==null){
             prop2 = new UserProperty();
             prop2.setName(UserProperty.ONTARIO_MD_PASSWORD);
             prop2.setProviderNo(provider);
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
             prop.setProviderNo(provider);
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
             prop.setProviderNo(provider);
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
             prop.setProviderNo(provider);
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
             saveProperty.setProviderNo(provider);
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
