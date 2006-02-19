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
import oscar.entities.*;
import oscar.oscarBilling.ca.bc.MSP.*;
import oscar.oscarDemographic.data.*;
import oscar.oscarDemographic.data.DemographicData.*;
import oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem;
import oscar.oscarBilling.ca.bc.data.BillingFormData;
import oscar.OscarProperties;

public class BillingCreateBillingAction
    extends Action {
  private ServiceCodeValidationLogic vldt = new ServiceCodeValidationLogic();
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
      ServletException {
    ActionErrors errors = new ActionErrors();
    BillingBillingManager bmanager = new BillingBillingManager();
    BillingCreateBillingForm frm = (BillingCreateBillingForm) form;

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

    BillingSessionBean bean = (BillingSessionBean) request.getSession().
        getAttribute("billingSessionBean");
    Demographic demo = new DemographicData().getDemographic(bean.getPatientNo());

    ArrayList billItem = bmanager.getDups2(service, other_service1,
                                           other_service2, other_service3,
                                           other_service1_unit,
                                           other_service2_unit,
                                           other_service3_unit);
    BillingFormData billform = new BillingFormData();
    String payMeth = ( (BillingCreateBillingForm) form).getXml_encounter();
    bean.setGrandtotal(bmanager.getGrandTotal(billItem));
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
    bean.setPaymentType(payMeth);

    if (payMeth.equals("8")) {
      bean.setEncounter("E");
    }
    else {
      bean.setEncounter("O");
    }

    bean.setVisitType(frm.getXml_visittype());
    bean.setVisitLocation(frm.getXml_location());
    bean.setServiceDate(frm.getXml_appointment_date());
    bean.setStartTime(frm.getXml_starttime_hr() + frm.getXml_starttime_min());
    bean.setEndTime(frm.getXml_endtime_hr() + frm.getXml_endtime_min());
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
    ArrayList lst = billform.getPaymentTypes();
    for (int i = 0; i < lst.size(); i++) {
      PaymentType tp = (PaymentType) lst.get(i);
      if (tp.getId().equals(payMeth)) {
        bean.setPaymentTypeName(tp.getPaymentType());
        break;
      }

    }
    validateServiceCodeList(billItem, demo, errors);
    validateDxCodeList(bean, errors);

    if (!errors.isEmpty()) {
      checkCDMStatus(request, errors, demo);
      return mapping.getInputForward();
    }
    validate00120(errors, demo, billItem,bean.getServiceDate());
    if (!errors.isEmpty()) {
      checkCDMStatus(request, errors, demo);
      return mapping.getInputForward();
    }
    //We want this alert to show up regardless
    //However we don't necessarily want it to force the user to enter a bill
    checkCDMStatus(request, errors, demo);
    if (frm.getXml_billtype().equalsIgnoreCase("WCB")) {
      WCBForm wcbForm = new WCBForm();
      wcbForm.Set(bean);
      request.getSession().putValue("WCBForm", wcbForm);
      return (mapping.findForward("WCB"));
    }
    //      System.out.println("Service count : "+ billItem.size());
    return mapping.findForward("success");
  }

  private void checkCDMStatus(HttpServletRequest request, ActionErrors errors,
                              Demographic demo) {
    String[] cnlsCodes = OscarProperties.getInstance().getProperty(
        "COUNSELING_CODES").split(",");
    if (vldt.needsCDMCounselling(demo.getDemographicNo(), cnlsCodes)) {
      verifyLast13050(errors, demo);
    }
    this.saveErrors(request, errors);
  }

  /**
   * Validates a String array of diagnostic codes and adds an ActionMessage
   * to the ActionMessages object, for any of the codes that don't validate
   * successfully
   * @param service String[]
   * @param demo Demographic
   * @param errors ActionErrors
   */

  private void validateDxCodeList(BillingSessionBean bean,
                                  ActionErrors errors) {
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
   * to the ActionErrors object, for any of the codes that don't validate
   * successfully
   * @param service String[]
   * @param demo Demographic
   * @param errors ActionErrors
   */
  private void validateServiceCodeList(ArrayList billItems, Demographic demo,
                                       ActionErrors errors) {
    BillingAssociationPersistence per = new BillingAssociationPersistence();
    for (int i = 0; i < billItems.size(); i++) {
      BillingItem item = (BillingItem) billItems.get(i);
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

  private void validate00120(ActionErrors errors, Demographic demo,
                             ArrayList billItem,String serviceDate) {
    for (Iterator iter = billItem.iterator(); iter.hasNext(); ) {
      BillingItem item = (BillingItem) iter.next();
      String[] cnlsCodes = OscarProperties.getInstance().getProperty(
          "COUNSELING_CODES").split(",");
      Vector vCodes = new Vector(Arrays.asList(cnlsCodes));
      if (vCodes.contains(item.getServiceCode())) {
        if (!vldt.hasMore00120Codes(demo.getDemographicNo(),
                                    item.getServiceCode(),serviceDate)) {
          errors.add("",
                     new ActionMessage(
                         "oscar.billing.CA.BC.billingBC.error.noMore00120"));
        }
        break;
      }
    }

  }

  private void verifyLast13050(ActionErrors errors, Demographic demo) {
    int last13050 = vldt.daysSinceLast13050(demo.getDemographicNo());
    if (last13050 > 365) {
      errors.add("",
                 new ActionMessage(
                     "oscar.billing.CA.BC.billingBC.error.last13050",
                     String.valueOf(last13050)));
    }
    else if (last13050 == -1) {
      errors.add("",
                 new ActionMessage(
                     "oscar.billing.CA.BC.billingBC.error.neverBilled13050"));
    }

  }
}
