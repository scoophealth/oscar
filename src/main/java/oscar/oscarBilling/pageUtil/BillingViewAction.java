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
import java.util.Locale;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.oscarBilling.pageUtil.BillingBillingManager.BillingItem;

public final class BillingViewAction extends Action {

    public ActionForward execute(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {


        if(request.getSession().getAttribute("user") == null  ){
            return (mapping.findForward("Logout"));
        }
        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources(request);

        // Setup variables
        Properties oscarVars = OscarProperties.getInstance();

        if (oscarVars.getProperty("billregion").equals("ON")){
            String newURL = mapping.findForward("ON").getPath();
            newURL = newURL + "?"+request.getQueryString();
            return (new ActionForward(newURL));
        }else{
            oscar.oscarBilling.pageUtil.BillingViewBean bean = new oscar.oscarBilling.pageUtil.BillingViewBean();
            bean.loadBilling(request.getParameter("billing_no"));
            oscar.oscarBilling.pageUtil.BillingBillingManager bmanager;
            bmanager = new BillingBillingManager();
            ArrayList<BillingItem> billItem = bmanager.getBillView(request.getParameter("billing_no"));
            MiscUtils.getLogger().debug("Calling getGrandTotal");
            bean.setBillItem(billItem);
            bean.setGrandtotal(bmanager.getGrandTotal(billItem));
            MiscUtils.getLogger().debug("GrandTotal" +bmanager.getGrandTotal(billItem));
            oscar.oscarDemographic.data.DemographicData demoData = new oscar.oscarDemographic.data.DemographicData();
            MiscUtils.getLogger().debug("Calling Demo");

            org.oscarehr.common.model.Demographic demo = demoData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), bean.getPatientNo());
            bean.setPatientLastName(demo.getLastName());
            bean.setPatientFirstName(demo.getFirstName());
            bean.setPatientDoB(demo.getFirstName());
            bean.setPatientAddress1(demo.getAddress());
            bean.setPatientAddress2(demo.getCity());
            bean.setPatientPostal(demo.getPostal());
            bean.setPatientSex(demo.getSex());
            bean.setPatientPHN(demo.getHin()+demo.getVer());
            bean.setPatientHCType(demo.getHcType());
            bean.setPatientAge(demo.getAge());
            MiscUtils.getLogger().debug("End Demo Call");

            //if(request.getParameter("demographic_no")!=null & request.getParameter("appointment_no")!=null)
            //{
            //    bean = new oscar.oscarBilling.pageUtil.BillingSessionBean();

            //	 bean.setApptProviderNo(request.getParameter("apptProvider_no"));
            //    bean.setPatientName(request.getParameter("demographic_name"));
            //    bean.setProviderView(request.getParameter("providerview"));
            //    bean.setBillRegion(request.getParameter("billRegion"));
            //    bean.setBillForm(request.getParameter("billForm"));
            //    bean.setCreator(request.getParameter("user_no"));
            //    bean.setPatientNo(request.getParameter("demographic_no"));
            //    bean.setApptNo(request.getParameter("appointment_no"));
            //    bean.setApptDate(request.getParameter("appointment_date"));
            //    bean.setApptStart(request.getParameter("start_time"));
            //    bean.setApptStatus(request.getParameter("status"));
            request.getSession().setAttribute("billingViewBean", bean);

            //            }//if
            //else
            //{
            //    bean = (oscar.oscarBilling.pageUtil.BillingSessionBean)request.getSession().getAttribute("billingSessionBean");
            //}



            return (mapping.findForward("success"));
        }
    }

}
