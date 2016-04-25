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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.OscarProperties;
import oscar.entities.PaymentType;
import oscar.entities.WCB;
import oscar.oscarBilling.ca.bc.MSP.AgeValidator;
import oscar.oscarBilling.ca.bc.MSP.ServiceCodeValidationLogic;
import oscar.oscarBilling.ca.bc.MSP.SexValidator;
import oscar.oscarBilling.ca.bc.Teleplan.WCBCodes;
import oscar.oscarBilling.ca.bc.data.BillingFormData;
import oscar.oscarBilling.ca.bc.data.BillingmasterDAO;
import oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem;
import oscar.oscarDemographic.data.DemographicData;
import oscar.util.SqlUtils;

public class BillingCreateBillingAction extends Action {
  private static final Logger log=MiscUtils.getLogger();

  private ServiceCodeValidationLogic vldt = new ServiceCodeValidationLogic();
  private ArrayList<String> patientDX = new ArrayList<String>(); //List of disease codes for current patient

  public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException {
    ActionMessages errors = new ActionMessages();
    BillingBillingManager bmanager = new BillingBillingManager();

    BillingCreateBillingForm frm = (BillingCreateBillingForm) form;
    bmanager.setBillTtype(frm.getXml_billtype());

    /**
     * This service list is not necessary
     */
    String[] service = new String[0]; //frm.getService();
    String other_service1 = frm.getXml_other1();
    String other_service2 = frm.getXml_other2();
    String other_service3 = frm.getXml_other3();
    String other_service1_unit = frm.getXml_other1_unit();
    String other_service2_unit = frm.getXml_other2_unit();
    String other_service3_unit = frm.getXml_other3_unit();

    BillingSessionBean bean = (BillingSessionBean) request.getSession().getAttribute("billingSessionBean");
    org.oscarehr.common.model.Demographic demo = new DemographicData().getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), bean.getPatientNo());
    this.patientDX = vldt.getPatientDxCodes(demo.getDemographicNo().toString());
    ArrayList<BillingItem> billItem = bmanager.getDups2(service, other_service1,
                                           other_service2, other_service3,
                                           other_service1_unit,
                                           other_service2_unit,
                                           other_service3_unit);
    BillingFormData billform = new BillingFormData();
    String payMeth = ( (BillingCreateBillingForm) form).getXml_encounter();
    bean.setGrandtotal(bmanager.getGrandTotal(billItem));
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
    bean.setBillingType(frm.getXml_billtype());
    bean.setPaymentType(payMeth);

    if (payMeth.equals("8")) {
      bean.setEncounter("E");
    }
    else {
      bean.setEncounter("O");
    }

    bean.setWcbId(request.getParameter("WCBid"));
    bean.setVisitType(frm.getXml_visittype());
    bean.setVisitLocation(frm.getXml_location());
    bean.setServiceDate(frm.getXml_appointment_date());
    bean.setStartTimeHr(frm.getXml_starttime_hr());
    bean.setStartTimeMin(frm.getXml_starttime_min());
    bean.setEndTimeHr(frm.getXml_endtime_hr());
    bean.setEndTimeMin(frm.getXml_endtime_min());

    bean.setAdmissionDate(frm.getXml_vdate());
    bean.setBillingProvider(frm.getXml_provider());
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
    ArrayList<PaymentType> lst = billform.getPaymentTypes();
    for (int i = 0; i < lst.size(); i++) {
      PaymentType tp = lst.get(i);
      if (tp.getId().equals(payMeth)) {
        bean.setPaymentTypeName(tp.getPaymentType());
        break;
      }

    }
    log.debug("Ignore warnings ? "+request.getParameter("ignoreWarn"));
    if (request.getParameter("ignoreWarn") == null){
        validateServiceCodeList(billItem, demo, errors);
        validateDxCodeList(bean, errors);
        validateServiceCodeTimes(billItem, frm, errors);

        for (Iterator<BillingItem> iter = billItem.iterator(); iter.hasNext(); ) {
          BillingItem item =  iter.next();
          validateCDMCodeConditions(errors, demo.getDemographicNo().toString(),
                                    item.getServiceCode());
        }

        if (!errors.isEmpty()) {
          validateCodeLastBilled(request, errors, demo.getDemographicNo().toString());
          return mapping.getInputForward();
        }
        validate00120(errors, demo, billItem, bean.getServiceDate());
        if (!errors.isEmpty()) {
          validateCodeLastBilled(request, errors, demo.getDemographicNo().toString());
          return mapping.getInputForward();
        }
        this.validatePatientManagementCodes(errors, demo, billItem,
                                            bean.getServiceDate());
        if (!errors.isEmpty()) {
          validateCodeLastBilled(request, errors, demo.getDemographicNo().toString());
          return mapping.getInputForward();
        }

    }

    if (request.getParameter("WCBid") != null){
            MiscUtils.getLogger().debug("WCB id is not null "+request.getParameter("WCBid"));
            List<String> errs = null;
            WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
            BillingmasterDAO billingmasterDAO = (BillingmasterDAO) ctx.getBean("BillingmasterDAO");
            WCB wcbForm = billingmasterDAO.getWCBForm(request.getParameter("WCBid"));

        for (Iterator<BillingItem> iter = billItem.iterator(); iter.hasNext(); ) {
            BillingItem item = iter.next();
            String sc = item.getServiceCode();
            boolean formNeeded = WCBCodes.getInstance().isFormNeeded(sc);
            MiscUtils.getLogger().debug("code:"+sc+" form needed "+formNeeded);
            if (formNeeded){
                MiscUtils.getLogger().debug("Setting form needed 1");
                errs = wcbForm.verifyEverythingOnForm();
                if(errs != null && errs.size() > 0){
                    request.setAttribute("WCBcode",sc);
                    request.setAttribute("WCBFormNeeds",errs);
                    return mapping.getInputForward();
                }
            }else{
                errs = wcbForm.verifyFormNotNeeded();
            }
        }
        if(errs != null && errs.size() > 0){
            MiscUtils.getLogger().debug("Setting form needed 2");
            request.setAttribute("WCBFormNeeds",errs);
            return mapping.getInputForward();
        }
    }

    //We want this alert to show up regardless
    //However we don't necessarily want it to force the user to enter a bill
    validateCodeLastBilled(request, errors, demo.getDemographicNo().toString());



    //if fromBilling is true set forward to WCB Form
////    if ("true".equals(fromBilling)) {
////      if (frm.getXml_billtype().equalsIgnoreCase("WCB")) {
////        WCBForm wcbForm = new WCBForm();
////        wcbForm.Set(bean);
////        request.setAttribute("WCBForm", wcbForm);
////        wcbForm.setFormNeeded("1");
////        wcbForm.setProviderNo(bean.getApptProviderNo());
////        wcbForm.setDoValidate(true);
////
////        return (mapping.findForward("WCB"));
////      }
////    }
    return mapping.findForward("success");
  }

  /**
   * validateServiceCodeTimes
   *
   * @param billItem ArrayList
   * @param errors ActionMessages
   */
  private void validateServiceCodeTimes(ArrayList<BillingItem> billItems,
                                        BillingCreateBillingForm frm,
                                        ActionMessages
                                        errors) {
    String qry = "select bt.billingservice_no,bt.timeRange " +
        "from billing_msp_servicecode_times bt";

    List<String[]> results = SqlUtils.getQueryResultsList(qry);

    for (int i = 0; i < billItems.size(); i++) {
      BillingItem item =  billItems.get(i);
      boolean noStartHour = frm.getXml_starttime_hr() == null ||
          "".equals(frm.getXml_starttime_hr());
      boolean noStartMinute = (frm.getXml_starttime_min() == null ||
                               "".equals(frm.getXml_starttime_min()));
      boolean noStartTime = noStartHour && noStartMinute;

      boolean noEndHour = frm.getXml_endtime_hr() == null ||
          "".equals(frm.getXml_endtime_hr());
      boolean noEndMinute = (frm.getXml_endtime_min() == null ||
                             "".equals(frm.getXml_endtime_min()));
      boolean noEndTime = noEndHour && noEndMinute;
      String svcCode = item.getServiceCode();
      for (Iterator<String[]> iter = results.iterator(); iter.hasNext(); ) {
        String[] elem = iter.next();
        String codeToCompare = elem[0];
        if (codeToCompare.equals(svcCode)) {
          //if the specified code requires a start time
          if ("0".equals(elem[1])) {
            if (noStartTime) {
              errors.add("",
                         new ActionMessage(
                             "oscar.billing.CA.BC.billingBC.error.startTimeNeeded",
                             item.getServiceCode()));

            }
          }
          else if ("1".equals(elem[1])) {
            if (noStartTime || noEndTime) {
              errors.add("",
                         new ActionMessage(
                             "oscar.billing.CA.BC.billingBC.error.startTimeandEndNeeded",
                             item.getServiceCode()));
            }
          }
        }
      }
    }
  }

  /**
   * Validates a String array of diagnostic codes and adds an ActionMessage
   * to the ActionMessages object, for any of the codes that don't validate
   * successfully
   * @param service String[]
   * @param demo Demographic
   * @param errors ActionMessages
   */

  private void validateDxCodeList(BillingSessionBean bean,
                                  ActionMessages errors) {
    BillingAssociationPersistence per = new BillingAssociationPersistence();
    String[] dxcodes = {
        bean.getDx1(), bean.getDx2(), bean.getDx3()};
    for (int i = 0; i < dxcodes.length; i++) {
      String code = dxcodes[i];
      if (code != null && !code.equals("") && !per.dxcodeExists(code)) {
        errors.add("",
                   new ActionMessage(
                       "oscar.billing.CA.BC.billingBC.error.invaliddxcode",
                       code));
      }
    }
  }

  /**
   * Validates a String array of service codes and adds and ActionMessage
   * to the ActionMessages object, for any of the codes that don't validate
   * successfully
   * @param service String[]
   * @param demo Demographic
   * @param errors ActionMessages
   */
  private void validateServiceCodeList(ArrayList<BillingItem> billItems,
                                       org.oscarehr.common.model.Demographic demo,
                                       ActionMessages errors) {
    BillingAssociationPersistence per = new BillingAssociationPersistence();
    for (int i = 0; i < billItems.size(); i++) {
      BillingItem item =  billItems.get(i);
      if (per.serviceCodeExists(item.
                                getServiceCode())) {
        AgeValidator age = (AgeValidator) vldt.getAgeValidator(item.
            getServiceCode(), demo);
        SexValidator sex = (SexValidator) vldt.getSexValidator(item.
            getServiceCode(), demo);
        if (!age.isValid()) {
          errors.add("",
                     new org.apache.struts.action.ActionMessage(
                         "oscar.billing.CA.BC.billingBC.error.invalidAge",
                         item.getServiceCode(),
                         String.valueOf(demo.getAgeInYears()),
                         age.getDescription()));
        }
        if (!sex.isValid()) {
          errors.add("",
                     new org.apache.struts.action.ActionMessage(
                         "oscar.billing.CA.BC.billingBC.error.invalidSex",
                         item.getServiceCode(), demo.getSex(), sex.getGender()));
        }

      }
      else {
        errors.add("",
                   new ActionMessage(
                       "oscar.billing.CA.BC.billingBC.error.invalidsvccode",
                       item.getServiceCode()));

      }

    }
  }

  private void validate00120(ActionMessages errors,
                             org.oscarehr.common.model.Demographic demo,
                             ArrayList<BillingItem> billItem, String serviceDate) {
    for (Iterator<BillingItem> iter = billItem.iterator(); iter.hasNext(); ) {
      BillingItem item =  iter.next();
      String[] cnlsCodes = OscarProperties.getInstance().getProperty(
          "COUNSELING_CODES","").split(",");
      Vector vCodes = new Vector(Arrays.asList(cnlsCodes));
      if (vCodes.contains(item.getServiceCode())) {
        if (!vldt.hasMore00120Codes(demo.getDemographicNo().toString(),
                                    item.getServiceCode(), serviceDate)) {
          errors.add("",
                     new ActionMessage(
                         "oscar.billing.CA.BC.billingBC.error.noMore00120"));
        }
        break;
      }
    }
  }

  /**
   * The rules for the 145015  code are as follows:
   * A maximum of 6 units may be billed per calendar year
   * A maximum of 4 units may be billed on any given day
   * @param demoNo String - The uid of the patient
   * @param code String - The service code to be evaluated
   * @param serviceDate String - The date of service
   * @return boolean -  true if the specified service is billable
   */
  private void validatePatientManagementCodes(ActionMessages errors,
                                              org.oscarehr.common.model.Demographic demo,
                                              ArrayList<BillingItem> billItem,
                                              String serviceDate) {
    HashMap<String,Double> mgmCodeCount = new HashMap<String,Double>();
    mgmCodeCount.put("14015", new Double(0));
    mgmCodeCount.put("14016", new Double(0));
    for (Iterator<BillingItem> iter = billItem.iterator(); iter.hasNext(); ) {
      BillingItem item =  iter.next();
      if (mgmCodeCount.containsKey(item.getServiceCode())) {
        //Increments the service code count by the number of units for
        //the current bill item
        Double svcCodeUnitCount = new Double(item.getUnit());
        Double unitCount = mgmCodeCount.get(item.getServiceCode());
        unitCount = new Double(unitCount.doubleValue() +
                               svcCodeUnitCount.doubleValue());
        mgmCodeCount.remove(item.getServiceCode());
        mgmCodeCount.put(item.getServiceCode(), unitCount);
      }
    }
    for (Iterator<String> iter = mgmCodeCount.keySet().iterator(); iter.hasNext(); ) {
      String key = iter.next();
      double count = ( mgmCodeCount.get(key)).doubleValue();
      if (count > 0) {
        Map<String,Double> availableUnits = vldt.getCountAvailablePatientManagementUnits(demo.
            getDemographicNo().toString(), key, serviceDate);
        double dailyAvail = ( availableUnits.get(ServiceCodeValidationLogic.
            DAILY_AVAILABLE_UNITS)).doubleValue();
        double yearAvail = (availableUnits.get(ServiceCodeValidationLogic.
            ANNUAL_AVAILABLE_UNITS)).doubleValue();

        if ( (count > dailyAvail)) {
          String dayMsg =
              "oscar.billing.CA.BC.billingBC.error.patientManagementCodesDayUsed";
          errors.add("",
                     new ActionMessage(
                         dayMsg, new String[] {key, String.valueOf(count),
                         String.valueOf(dailyAvail)}));
        }
        else if (count > yearAvail) {
          String yearMsg =
              "oscar.billing.CA.BC.billingBC.error.patientManagementCodesYearUsed";
          errors.add("",
                     new ActionMessage(yearMsg, new String[] {key,
                                       String.valueOf(count),
                                       String.valueOf(yearAvail)}));

        }
      }
    }
  }

  private void validateCDMCodeConditions(ActionMessages errors, String demoNo,
                                         String serviceCode) {
    String cdmRulesQry =
        "SELECT serviceCode,conditionCode FROM billing_service_code_conditions";
    List<String[]> cdmRules = SqlUtils.getQueryResultsList(cdmRulesQry);
    List<String[] > cdmSvcCodes = vldt.getCDMCodes();
    for (String[] item:cdmSvcCodes){
      if (patientDX.contains(item[0])) {
        if (serviceCode.equals(item[1])) {
          validateCDMCodeConditionsHlp(errors, demoNo, cdmRules, item[1]);
        }
      }
    }
  }

  private void validateCDMCodeConditionsHlp(ActionMessages errors, String demoNo,
                                            List<String[]> cdmRules, String code) {
    for (String[] item:cdmRules){
      if (code.equals(item[0])) {
        int days = vldt.daysSinceCodeLastBilled(demoNo, item[1]);
        if (days >= 0 && days < 365) {
          errors.add("",
                     new ActionMessage(
                         "oscar.billing.CA.BC.billingBC.error.codeCond",
                         new String[] {item[0], item[1]}));

        }
      }
    }
  }

  /**
   * @todo Document Me
   * @param errors ActionMessages
   * @param demo Demographic
   */
  private void validateCodeLastBilled(HttpServletRequest request,
                                      ActionMessages errors, String demoNo) {
    List<String[]> cdmSvcCodes = vldt.getCDMCodes();
    for (Iterator<String[]> iter = cdmSvcCodes.iterator(); iter.hasNext(); ) {
      String[] item = iter.next();
      if (patientDX.contains(item[0])) {
        validateCodeLastBilledHlp(errors, demoNo, item[1]);
      }
    }

    this.saveErrors(request, errors);
  }

  private void validateCodeLastBilledHlp(ActionMessages errors,
                                         String demoNo, String code) {
    int codeLastBilled = -1;
    String conditionCodeQuery = "select conditionCode from billing_service_code_conditions where serviceCode = '" +
        code + "'";
    List<String[]> conditions = SqlUtils.getQueryResultsList(conditionCodeQuery);

    for (Iterator<String[]> iter = conditions.iterator(); iter.hasNext(); ) {
      String[] row = iter.next();
      codeLastBilled = vldt.daysSinceCodeLastBilled(demoNo, row[0]);
      if (codeLastBilled < 365 && codeLastBilled > -1) {
        break;
      }
    }
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
