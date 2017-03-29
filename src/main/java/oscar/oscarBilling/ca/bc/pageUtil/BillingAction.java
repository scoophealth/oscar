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


package oscar.oscarBilling.ca.bc.pageUtil;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.billing.Clinicaid.util.ClinicaidCommunication;
import org.oscarehr.decisionSupport.model.DSConsequence;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.oscarBilling.ca.bc.MSP.ServiceCodeValidationLogic;
import oscar.oscarBilling.ca.bc.decisionSupport.BillingGuidelines;
import oscar.util.SqlUtils;
import oscar.util.SuperSiteUtil;

public final class BillingAction extends Action {
  private static Logger _log = MiscUtils.getLogger();
  private ServiceCodeValidationLogic vldt = new ServiceCodeValidationLogic();
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
      ServletException {
	  LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
    // Setup variables
    ActionMessages errors = new ActionMessages();
    oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean bean = null;
    String encounter = request.getAttribute("encounter") != null ?
        (String) request.getAttribute("encounter") : "";
    String region = request.getParameter("billRegion");
    SuperSiteUtil.getInstance().checkSuperSiteAccess(request, response, "demographic_no");
    if ("ON".equals(region)) {
      String newURL = mapping.findForward("ON").getPath();
      newURL = newURL + "?" + request.getQueryString();
      ActionForward ON = new ActionForward();
      ON.setPath(newURL);
      ON.setRedirect(true);
      return ON;
    }
	else if ("CLINICAID".equals(region)) {

	  ClinicaidCommunication clinicaid_communicator 
		  = new ClinicaidCommunication();

	  String action = "";
	  if( request.getParameter("action") != null) {
		  action = request.getParameter("action");
	  }
	  else {
		  action = "create_invoice";
	  }

	  String clinicaidURL = clinicaid_communicator.buildClinicaidURL(request, action);
      String newURL = mapping.findForward("CLINICAID").getPath();
      newURL = newURL + "?" + request.getQueryString();
      ActionForward Clinicaid = new ActionForward();
      Clinicaid.setPath(clinicaidURL);

      Clinicaid.setRedirect(true);
      return Clinicaid;
    }
    else {
      BillingCreateBillingForm frm = (BillingCreateBillingForm) form;
      if (request.getParameter("demographic_no") != null &
          request.getParameter("appointment_no") != null) {
        String newWCBClaim = request.getParameter("newWCBClaim");
        //If newWCBClaim == 1, this action was invoked from the WCB form
        //Therefore, we need to set the appropriate parameters to set up the subsequent bill
        if ("1".equals(newWCBClaim)) {
          
          frm.setXml_billtype("WCB");
          
          List l = (List) request.getAttribute("billingcodes");
          if (l != null && l.size() > 0 ){
              frm.setXml_other1(""+l.get(0));
              if (l.size() > 1){
                frm.setXml_other2(""+l.get(1));
              }
            
          }
          
          frm.setXml_diagnostic_detail1(""+request.getAttribute("icd9"));
          request.setAttribute("WCBFormId",request.getAttribute("WCBFormId"));
          request.setAttribute("newWCBClaim",request.getParameter("newWCBClaim"));
          request.setAttribute("loadFromSession", "y");
        }
        bean = new oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean();
        fillBean(request, bean);
        if(request.getAttribute("serviceDate") != null){
            MiscUtils.getLogger().debug("service Date set to the appointment Date"+(String) request.getAttribute("serviceDate"));
           bean.setApptDate((String) request.getAttribute("serviceDate"));
        }

        request.getSession().setAttribute("billingSessionBean", bean);
 //       this.validateCodeLastBilled(request, errors,
 //                                   request.getParameter("demographic_no"));
        try{
            _log.debug("Start of billing rules");
            List<DSConsequence> list = BillingGuidelines.getInstance().evaluateAndGetConsequences(loggedInInfo, request.getParameter("demographic_no"), (String) request.getSession().getAttribute("user"));
        
            for (DSConsequence dscon : list){
                _log.debug("DSTEXT "+dscon.getText());
                errors.add("",new ActionMessage("message.custom",dscon.getText()));
           }
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
      }else if ("true".equals(encounter)) {
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
   * Determines if the specified demographic number fits the following criteria and generates
   * a message if true:
   * * Has one of the predefined chronic diseases
   * * A service was performed within the last Calendar year
   *
   * @param request HttpServletRequest
   * @param errors ActionMessages
   * @param demoNo String
   */
  private void validateCodeLastBilled(HttpServletRequest request,
                                      ActionMessages errors, String demoNo) {
    List patientDX = vldt.getPatientDxCodes(demoNo);
    List<String[]> cdmSvcCodes = vldt.getCDMCodes();
    for (String[] item:cdmSvcCodes){
         if (patientDX.contains(item[0])) {
        validateCodeLastBilledHlp(errors, demoNo, item[1]);
      }
    }

    this.saveErrors(request, errors);
  }


  /*
   * Looks through the list of billing codes and if any billing code in the list has been billed within the last year then nothing happens.But if the last code in the list has either
   * never been billed OR billed over 365 days ago, a warning is added advising to do so.
  */
  private void validateCodeLastBilledHlp(ActionMessages errors,
                                         String demoNo, String code) {
    int codeLastBilled = -1;
    String conditionCodeQuery = "select conditionCode from billing_service_code_conditions where serviceCode = '" +
        code + "'";
    List<String[]> conditions = SqlUtils.getQueryResultsList(conditionCodeQuery);
 
    for(String[] row : conditions){
      codeLastBilled = vldt.daysSinceCodeLastBilled(demoNo, row[0]);
      if (codeLastBilled < 365 && codeLastBilled > -1) {
        break;
      }
    }
    if (codeLastBilled > 365) {
        MiscUtils.getLogger().debug("adding code last billed "+code);
      errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.error.codeLastBilled",new String[] {String.valueOf(codeLastBilled), code}));
    }
    else if (codeLastBilled == -1) {
        MiscUtils.getLogger().debug("adding code never billed "+code);
      errors.add("",new ActionMessage("oscar.billing.CA.BC.billingBC.error.codeNeverBilled",new String[] {code}));
    }
  }

}
