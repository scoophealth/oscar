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
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.entities.Provider;
import oscar.oscarBilling.ca.bc.MSP.MSPReconcile;
import oscar.oscarBilling.ca.bc.data.BillRecipient;
import oscar.oscarBilling.ca.bc.data.BillingPreference;
import oscar.oscarBilling.ca.bc.data.BillingPreferencesDAO;
import oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem;
import oscar.oscarDemographic.data.DemographicData;

public final class BillingViewAction
    extends Action {
    private static Logger log = MiscUtils.getLogger();

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
      ServletException {

    Properties oscarVars = OscarProperties.getInstance();

    if (oscarVars.getProperty("billregion").equals("ON")) {
      String newURL = mapping.findForward("ON").getPath();
      newURL = newURL + "?" + request.getQueryString();
      return (new ActionForward(newURL));
    }
    else {
      BillingViewForm frm = (BillingViewForm) form;
      BillingViewBean bean = new BillingViewBean();
      bean.loadBilling(request.getParameter("billing_no"));
      BillingBillingManager bmanager = new BillingBillingManager();
      ArrayList<BillingItem> billItem = new ArrayList<BillingItem>();
      String[] billingN = request.getParameterValues("billing_no");

      for (int i = 0; i < billingN.length; i++){
          log.debug("billn "+i+" "+billingN[i]);
         ArrayList<BillingItem> tempBillItem = bmanager.getBillView(billingN[i]);
         billItem.addAll(tempBillItem);
      }

      log.debug("Calling getGrandTotal");
      bean.setBillItem(billItem);

      bean.calculateSubtotal();
      log.debug("GrandTotal" + bmanager.getGrandTotal(billItem));
      DemographicData demoData = new DemographicData();
      log.debug("Calling Demo");

      org.oscarehr.common.model.Demographic demo = demoData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), bean.getPatientNo());
      bean.setPatientLastName(demo.getLastName());
      bean.setPatientFirstName(demo.getFirstName());
      bean.setPatientDoB(demo.getDateOfBirth());
      bean.setPatientAddress1(demo.getAddress());
      bean.setPatientAddress2(demo.getCity());
      bean.setPatientPostal(demo.getPostal());
      bean.setPatientSex(demo.getSex());
      bean.setPatientPHN(demo.getHin()+demo.getVer());
      bean.setPatientHCType(demo.getHcType());
      bean.setPatientAge(demo.getAge());
      frm.setBillingNo(bean.getBillingNo());
      log.debug("End Demo Call billing No"+request.getParameter("billing_no"));
      //Loading bill Recipient Data
      List<BillRecipient> billRecipList = bean.getBillRecipient(request.getParameter("billing_no"));
      if (!billRecipList.isEmpty()) {
        log.debug("Filling recep with last details");
        BillRecipient rec = billRecipList.get(0);
        frm.setRecipientAddress(rec.getAddress());
        frm.setRecipientCity(rec.getCity());
        frm.setRecipientName(rec.getName());
        frm.setRecipientPostal(rec.getPostal());
        frm.setRecipientProvince(rec.getProvince());
        frm.setBillPatient("0");
      }else {
        log.debug("Filling recep with demo details");
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
        Provider p = getPreferredPayeeProvider(bean.getBillingProvider());
        bean.setDefaultPayeeFirstName(p.getFirstName());
        bean.setDefaultPayeeLastName(p.getLastName());
        actionForward = mapping.findForward("private");
      }


      return actionForward;
    }
  }

  /**
   * Returns a Provider instance which represents the default payee for a providers bills
   * according to the specified user preference. If the user doesn't have a default payee
   * an instance with empty fields is returned
   * @param userProviderNo String
   * @return Provider
   */
  public Provider getPreferredPayeeProvider(String userProviderNo){
    Provider p = new Provider();
    BillingPreferencesDAO dao = SpringUtils.getBean(BillingPreferencesDAO.class);
    BillingPreference pref = dao.getUserBillingPreference(userProviderNo);

    if(pref != null){
      MSPReconcile msp = new MSPReconcile();
      p = msp.getProvider(String.valueOf(pref.getDefaultPayeeNo()),0);
    }
    return p;
  }
}
