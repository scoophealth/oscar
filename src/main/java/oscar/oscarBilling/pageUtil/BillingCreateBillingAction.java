/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.oscarBilling.pageUtil;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.oscarBilling.pageUtil.BillingBillingManager.BillingItem;
import oscar.oscarDemographic.data.DemographicData;

public class BillingCreateBillingAction extends Action {


    public ActionForward execute(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {

        if(request.getSession().getAttribute("user") == null  ){
            return (mapping.findForward("Logout"));
        }

        oscar.oscarBilling.pageUtil.BillingSessionBean bean;
        bean = (oscar.oscarBilling.pageUtil.BillingSessionBean)request.getSession().getAttribute("billingSessionBean");


        String[] service  = ((BillingCreateBillingForm)form).getService();
        String other_service1 = ((BillingCreateBillingForm)form).getXml_other1();
        String other_service2 = ((BillingCreateBillingForm)form).getXml_other2();
        String other_service3 = ((BillingCreateBillingForm)form).getXml_other3();
        String other_service1_unit = ((BillingCreateBillingForm)form).getXml_other1_unit();
        String other_service2_unit = ((BillingCreateBillingForm)form).getXml_other2_unit();
        String other_service3_unit = ((BillingCreateBillingForm)form).getXml_other3_unit();
        oscar.oscarBilling.pageUtil.BillingBillingManager bmanager;
        bmanager = new BillingBillingManager();
        ArrayList<BillingItem> billItem = bmanager.getDups2(service, other_service1, other_service2, other_service3,other_service1_unit, other_service2_unit, other_service3_unit);
        MiscUtils.getLogger().debug("Calling getGrandTotal");

        bean.setGrandtotal(bmanager.getGrandTotal(billItem));
        MiscUtils.getLogger().debug("GrandTotal" +bmanager.getGrandTotal(billItem));
        oscar.oscarDemographic.data.DemographicData demoData = new oscar.oscarDemographic.data.DemographicData();

        org.oscarehr.common.model.Demographic demo = demoData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), bean.getPatientNo());
        bean.setPatientLastName(demo.getLastName());
        bean.setPatientFirstName(demo.getFirstName());
        bean.setPatientDoB(DemographicData.getDob(demo));
        bean.setPatientAddress1(demo.getAddress());
        bean.setPatientAddress2(demo.getCity());
        bean.setPatientPostal(demo.getPostal());
        bean.setPatientSex(demo.getSex());
        bean.setPatientPHN(demo.getHin()+demo.getVer());
        bean.setPatientHCType(demo.getHcType());
        bean.setPatientAge(demo.getAge());
        bean.setBillingType(((BillingCreateBillingForm)form).getXml_billtype());
        bean.setVisitType(((BillingCreateBillingForm)form).getXml_visittype());
        bean.setVisitLocation(((BillingCreateBillingForm)form).getXml_location());
        bean.setServiceDate(((BillingCreateBillingForm)form).getXml_appointment_date());
        bean.setStartTime(((BillingCreateBillingForm)form).getXml_starttime());
        bean.setEndTime(((BillingCreateBillingForm)form).getXml_endtime());
        bean.setAdmissionDate(((BillingCreateBillingForm)form).getXml_vdate());
        bean.setBillingProvider(((BillingCreateBillingForm)form).getXml_provider());

        oscar.oscarBilling.data.BillingFormData billform = new oscar.oscarBilling.data.BillingFormData();

        bean.setBillingPracNo(billform.getPracNo(((BillingCreateBillingForm)form).getXml_provider()));
        bean.setBillingGroupNo(billform.getGroupNo(((BillingCreateBillingForm)form).getXml_provider()));



        bean.setDx1(((BillingCreateBillingForm)form).getXml_diagnostic_detail1());
        bean.setDx2(((BillingCreateBillingForm)form).getXml_diagnostic_detail2());
        bean.setDx3(((BillingCreateBillingForm)form).getXml_diagnostic_detail3());
        bean.setReferral1(((BillingCreateBillingForm)form).getXml_refer1());
        bean.setReferral2(((BillingCreateBillingForm)form).getXml_refer2());
        bean.setReferType1(((BillingCreateBillingForm)form).getRefertype1());
        bean.setReferType2(((BillingCreateBillingForm)form).getRefertype2());
        bean.setBillItem(billItem);

        //bean.setApptProviderNo(request.getParameter("apptProvider_no"));
        //bean.setPatientName(request.getParameter("demographic_name"));
        //bean.setProviderView(request.getParameter("providerview"));
        //bean.setBillRegion(request.getParameter("billRegion"));
        //bean.setBillForm(request.getParameter("billForm"));
        //bean.setCreator(request.getParameter("user_no"));
        //bean.setPatientNo(request.getParameter("demographic_no"));
        //bean.setApptNo(request.getParameter("appointment_no"));
        //bean.setApptDate(request.getParameter("appointment_date"));

        return (mapping.findForward("success"));
    }

}
