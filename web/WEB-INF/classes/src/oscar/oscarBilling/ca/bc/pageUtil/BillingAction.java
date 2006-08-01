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
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import oscar.*;
import oscar.oscarBilling.ca.bc.MSP.*;
import oscar.util.SqlUtils;
import java.util.List;
import java.util.ArrayList;

public final class BillingAction
    extends Action {
  private ServiceCodeValidationLogic vldt = new ServiceCodeValidationLogic();
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
      ServletException {
    // Setup variables
    ActionErrors errors = new ActionErrors();
    oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean bean = null;
    String encounter = request.getAttribute("encounter") != null ?
        (String) request.getAttribute("encounter") : "";
 String region = request.getParameter("billRegion");
    if ("ON".equals(region)) {
      String newURL = mapping.findForward("ON").getPath();
      newURL = newURL + "?" + request.getQueryString();
      ActionForward ON = new ActionForward();
      ON.setPath(newURL);
      ON.setRedirect(true);
      return ON;
    }
    else {
      BillingCreateBillingForm frm = (BillingCreateBillingForm) form;
      if (request.getParameter("demographic_no") != null &
          request.getParameter("appointment_no") != null) {
        String newWCBClaim = request.getParameter("newWCBClaim");
        //If newWCBClaim == 1, this action was invoked from the WCB form
        //Therefore, we need to set the appropriate parameters to set up the subsequent bill
        if ("1".equals(newWCBClaim)) {
          WCBForm wcbForm = (WCBForm) request.getSession().getAttribute(
              "WCBForm");
          frm.setXml_billtype("WCB");
          frm.setXml_other1(wcbForm.getW_extrafeeitem());
          frm.setXml_diagnostic_detail1(wcbForm.getW_icd9());
          request.setAttribute("newWCBClaim",
                               request.getParameter("newWCBClaim"));
          request.setAttribute("loadFromSession", "y");
        }
        bean = new oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean();
        fillBean(request, bean);

        request.getSession().setAttribute("billingSessionBean", bean);
        this.validateCodeLastBilled(request,errors, request.getParameter("demographic_no"));
      }
      else if ("true".equals(encounter)) {
        bean = (oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean) request.
            getSession().getAttribute("billingSessionBean");
        frm.setXml_provider(request.getParameter("user_no"));
        region = bean.getBillRegion();
      }
      /**
             * @todo Test this, it looks unnecessary
       */
      else {
        bean = (oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean) request.
            getSession().getAttribute("billingSessionBean");
      }
    }
    this.saveErrors(request, errors);
    return (mapping.findForward(region));
  }

  private void fillBean(HttpServletRequest request, BillingSessionBean bean) {
    bean.setApptProviderNo(request.getParameter("apptProvider_no"));
    bean.setPatientName(request.getParameter("demographic_name"));
    bean.setProviderView(request.getParameter("providerview"));
    bean.setBillRegion(request.getParameter("billRegion"));
    bean.setBillForm(request.getParameter("billForm"));
    bean.setCreator(request.getParameter("user_no"));
    bean.setPatientNo(request.getParameter("demographic_no"));
    bean.setApptNo(request.getParameter("appointment_no"));
    bean.setApptDate(request.getParameter("appointment_date"));
    bean.setApptStart(request.getParameter("start_time"));
    bean.setApptStatus(request.getParameter("status"));
  }

  /**
    * @todo Document Me
    * @param errors ActionErrors
    * @param demo Demographic
    */
   private void validateCodeLastBilled(HttpServletRequest request,ActionErrors errors, String demoNo) {
     ArrayList patientDX = vldt.getPatientDxCodes(demoNo);
     if (patientDX.contains("250")) {
       validateCodeLastBilledHlp(errors, demoNo, "14050");
     }
     //if patient has HT
     if (patientDX.contains("428")) {
       validateCodeLastBilledHlp(errors, demoNo, "14051");

     }
     //if patient has chf
     if (patientDX.contains("401")) {
       validateCodeLastBilledHlp(errors, demoNo, "14052");
     }
     this.saveErrors(request,errors);
   }
   private void validateCodeLastBilledHlp(ActionErrors errors,
                                         String demoNo, String code) {
     int codeLastBilled = vldt.daysSinceCodeLastBilled(demoNo, code);
     if (codeLastBilled > 365) {
       errors.add("",
                  new ActionMessage(
                      "oscar.billing.CA.BC.billingBC.error.codeLastBilled",
                      new String[] {String.valueOf(codeLastBilled), code}));
     }
     else if (codeLastBilled == -1) {
       errors.add("",
                  new ActionMessage(
                      "oscar.billing.CA.BC.billingBC.error.codeNeverBilled",
                      new String[] {code}));
     }
   }


}
