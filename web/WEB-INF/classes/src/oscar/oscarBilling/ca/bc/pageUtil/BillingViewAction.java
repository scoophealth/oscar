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

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.util.*;
import oscar.oscarBilling.ca.bc.data.*;
import oscar.oscarDemographic.data.*;

public final class BillingViewAction
    extends Action {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
      ServletException {

    if (request.getSession().getAttribute("user") == null) {
      return (mapping.findForward("Logout"));
    }
    // Extract attributes we will need
    Locale locale = getLocale(request);
    MessageResources messages = getResources(request);

    // Setup variables
    ActionErrors errors = new ActionErrors();
    Properties oscarVars = (Properties) request.getSession().getAttribute(
        "oscarVariables");

    if (oscarVars.getProperty("billregion").equals("ON")) {
      String newURL = mapping.findForward("ON").getPath();
      newURL = newURL + "?" + request.getQueryString();
      return (new ActionForward(newURL));
    }
    else {
      BillingViewForm frm = (BillingViewForm) form;
      oscar.oscarBilling.ca.bc.pageUtil.BillingViewBean bean = new oscar.
          oscarBilling.ca.bc.pageUtil.BillingViewBean();
      bean.loadBilling(request.getParameter("billing_no"));
      oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager bmanager = new
          BillingBillingManager();
      ArrayList billItem = bmanager.getBillView(request.getParameter(
          "billing_no"));
      System.out.println("Calling getGrandTotal");
      bean.setBillItem(billItem);

      // bean.setSubTotal(bmanager.getSubTotal(billItem));
      bean.setGrandtotal(bmanager.getGrandTotal(billItem));
      System.out.println("GrandTotal" + bmanager.getGrandTotal(billItem));
      oscar.oscarDemographic.data.DemographicData demoData = new oscar.
          oscarDemographic.data.DemographicData();
      System.out.println("Calling Demo");

      oscar.oscarDemographic.data.DemographicData.Demographic demo = demoData.
          getDemographic(bean.getPatientNo());
      bean.setPatientLastName(demo.getLastName());
      bean.setPatientFirstName(demo.getFirstName());
      bean.setPatientDoB(demo.getDateOfBirth());
      bean.setPatientAddress1(demo.getAddress());
      bean.setPatientAddress2(demo.getCity());
      bean.setPatientPostal(demo.getPostal());
      bean.setPatientSex(demo.getSex());
      bean.setPatientPHN(demo.getHIN());
      bean.setPatientHCType(demo.getHCType());
      bean.setPatientAge(demo.getAge());
      frm.setBillingNo(bean.getBillingNo());
      System.out.println("End Demo Call");
      //Loading bill Recipient Data
      List billRecipList = bean.getBillRecipient(request.getParameter(
          "billing_no"));
      if (!billRecipList.isEmpty()) {
        BillRecipient rec = (BillRecipient) billRecipList.get(0);
        frm.setRecipientAddress(rec.getAddress());
        frm.setRecipientCity(rec.getCity());
        frm.setRecipientName(rec.getName());
        frm.setRecipientPostal(rec.getPostal());
        frm.setRecipientProvince(rec.getProvince());
        frm.setBillPatient("0");
      }
      else {
        frm.setRecipientName(demo.getFirstName() + " " + demo.getLastName());
        frm.setRecipientCity(demo.getCity());
        frm.setRecipientAddress(demo.getAddress());
        frm.setRecipientPostal(demo.getPostal());
        frm.setRecipientProvince(demo.getProvince());
        frm.setBillPatient("1");

      }
      frm.setMessageNotes(bean.getMessageNotes());
      frm.setBillStatus(bean.getBillingType());
      frm.setPaymentMethod(bean.getPaymentMethod());
      request.getSession().setAttribute("billingViewBean", bean);
      ActionForward actionForward = mapping.findForward("success");

      String receipt = request.getParameter("receipt");
      if (receipt != null && receipt.equals("yes")) {
        System.out.println("forwarding to receipt");
        actionForward = mapping.findForward("private");
      }

      return actionForward;
    }
  }

}
