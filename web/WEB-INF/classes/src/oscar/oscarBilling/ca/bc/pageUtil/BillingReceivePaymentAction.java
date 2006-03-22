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

import javax.servlet.http.*;
import org.apache.struts.action.*;
import oscar.oscarBilling.ca.bc.data.*;

public class BillingReceivePaymentAction
    extends Action {
  PrivateBillTransactionsDAO dao = new PrivateBillTransactionsDAO();
  ActionForward fwd = null;
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest request,
                               HttpServletResponse servletResponse) {
    BillingReceivePaymentActionForm frm = (BillingReceivePaymentActionForm)
        actionForm;
    String billingNo = frm.getBillingNo();
    String amountReceived = frm.getAmountReceived();
    Double dblAmt =  new Double(amountReceived);
    this.receivePayment(billingNo, dblAmt);
    fwd = actionMapping.findForward("success");
    return fwd;
  }

  public void receivePayment(String billingMasterNo, Double amount) {
    dao.savePrivateBillTransaction(billingMasterNo, amount);
  }

}
