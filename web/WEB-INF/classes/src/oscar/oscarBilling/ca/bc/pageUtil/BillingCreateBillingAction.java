/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.oscarBilling.ca.bc.pageUtil;
import oscar.oscarDB.DBHandler;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

public class BillingCreateBillingAction extends Action {
    
    
    public ActionForward execute(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
        
        if(request.getSession().getAttribute("user") == null  ){
            return (mapping.findForward("Logout"));
        }
        
        oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean bean;
        bean = (oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean)request.getSession().getAttribute("billingSessionBean");
        
        BillingCreateBillingForm frm = (BillingCreateBillingForm) form;
        String patientNo   = bean.getPatientNo();
        String patientName = bean.getPatientName();
        String apptNo     = bean.getApptNo();
        
        String[] service  = frm.getService();
        String other_service1 = frm.getXml_other1();
        String other_service2 = frm.getXml_other2();
        String other_service3 = frm.getXml_other3();
        String other_service1_unit = frm.getXml_other1_unit();
        String other_service2_unit = frm.getXml_other2_unit();
        String other_service3_unit = frm.getXml_other3_unit();
        oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager bmanager;
        bmanager = new BillingBillingManager();
        ArrayList billItem = bmanager.getDups2(service, other_service1, other_service2, other_service3,other_service1_unit, other_service2_unit, other_service3_unit);
        System.out.println("Calling getGrandTotal");
        
        bean.setGrandtotal(bmanager.getGrandTotal(billItem));
        System.out.println("GrandTotal" +bmanager.getGrandTotal(billItem));
        oscar.oscarDemographic.data.DemographicData demoData = new oscar.oscarDemographic.data.DemographicData();
        
        oscar.oscarDemographic.data.DemographicData.Demographic demo = demoData.getDemographic(bean.getPatientNo());
        bean.setPatientLastName(demo.getLastName());
        bean.setPatientFirstName(demo.getFirstName());
        bean.setPatientDoB(demo.getDob());
        bean.setPatientAddress1(demo.getAddress());
        bean.setPatientAddress2(demo.getCity());
        bean.setPatientPostal(demo.getPostal());
        bean.setPatientSex(demo.getSex());
        bean.setPatientPHN(demo.getHIN());
        bean.setPatientHCType(demo.getHCType());
        bean.setPatientAge(demo.getAge());
        bean.setBillingType(frm.getXml_billtype());
        bean.setEncounter(((BillingCreateBillingForm) form).getXml_encounter());
		
        bean.setVisitType(frm.getXml_visittype());
        bean.setVisitLocation(frm.getXml_location());
        bean.setServiceDate(frm.getXml_appointment_date());
        bean.setStartTime(frm.getXml_starttime());
        bean.setEndTime(frm.getXml_endtime());
        bean.setAdmissionDate(frm.getXml_vdate());
        bean.setBillingProvider(frm.getXml_provider());
        
        oscar.oscarBilling.ca.bc.data.BillingFormData billform = new oscar.oscarBilling.ca.bc.data.BillingFormData();
        
        bean.setBillingPracNo(billform.getPracNo(frm.getXml_provider()));
        bean.setBillingGroupNo(billform.getGroupNo(frm.getXml_provider()));
        
        
        
        bean.setDx1(frm.getXml_diagnostic_detail1());
        bean.setDx2(frm.getXml_diagnostic_detail2());
        bean.setDx3(frm.getXml_diagnostic_detail3());
        bean.setReferral1(frm.getXml_refer1());
        bean.setReferral2(frm.getXml_refer2());
        bean.setReferType1(frm.getRefertype1());
        bean.setReferType2(frm.getRefertype2());
        bean.setBillItem(billItem);
        
        bean.setCorrespondenceCode(frm.getCorrespondenceCode());
        bean.setNotes(frm.getNotes());
        bean.setDependent(frm.getDependent());
        bean.setAfterHours(frm.getAfterHours());
        bean.setTimeCall(frm.getTimeCall());
        bean.setSubmissionCode(frm.getSubmissionCode());
        bean.setShortClaimNote(frm.getShortClaimNote());
        bean.setService_to_date(frm.getService_to_date());
	bean.setIcbc_claim_no(frm.getIcbc_claim_no());
        bean.setMessageNotes(frm.getMessageNotes());
        bean.setMva_claim_code(frm.getMva_claim_code());
        
        bean.setFacilityNum(frm.getFacilityNum());
        bean.setFacilitySubNum(frm.getFacilitySubNum());
        
        if (frm.getXml_billtype().equalsIgnoreCase("WCB")){
           WCBForm wcbForm = new WCBForm();
           wcbForm.Set(bean);
           request.getSession().putValue("WCBForm", wcbForm);
           return (mapping.findForward("WCB"));
        }               
        //      System.out.println("Service count : "+ billItem.size());        
        
        
        return mapping.findForward("success");
    }
    
}
