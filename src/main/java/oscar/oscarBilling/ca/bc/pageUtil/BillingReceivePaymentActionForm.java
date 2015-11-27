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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import oscar.util.StringUtils;

public class BillingReceivePaymentActionForm
    extends ActionForm {
  private String amountReceived="";
  private String billingNo ="";
  public String getAmountReceived() {
    return amountReceived;
  }

  public void setAmountReceived(String amountReceived) {
    this.amountReceived = amountReceived;
  }

  public void setBillingNo(String billingNo) {

    this.billingNo = billingNo;
  }

  public String getBillingNo() {

    return billingNo;
  }

  public ActionErrors validate(ActionMapping actionMapping,
                               HttpServletRequest httpServletRequest) {
    ActionErrors errors = new ActionErrors();
    if (!StringUtils.isNumeric(amountReceived)) {
      errors.add("",
                 new ActionMessage(
                     "oscar.billing.CA.BC.billingBC.error.nonNumericAmount",
                     ""));
    }
    else {
      double dblAmount = new Double(amountReceived).doubleValue();
      if ((int)dblAmount == 0) {
        errors.add("",
                 new ActionMessage(
                     "oscar.billing.CA.BC.billingBC.error.zeroAmount",
                     ""));

      }
    }

    return errors;
  }


}
