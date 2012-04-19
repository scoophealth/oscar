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


/*
 * BillingUpdateBillingAction.java
 *
 * Created on August 30, 2004, 1:52 PM
 */

package oscar.oscarBilling.ca.bc.pageUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.oscarBilling.ca.bc.MSP.MSPReconcile;
import oscar.oscarBilling.ca.bc.data.BillRecipient;
import oscar.oscarBilling.ca.bc.data.BillingNote;

/**
 *
 * @author  root
 */
public final class BillingUpdateBillingAction
    extends Action {
    private static Logger log = MiscUtils.getLogger();
  HttpServletRequest request;
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
      ServletException {
    BillingViewForm frm = (BillingViewForm)form;
    String creator = (String) request.getSession().getAttribute("user");

    BillRecipient recip = new BillRecipient();
    recip.setName(frm.getRecipientName());
    recip.setAddress(frm.getRecipientAddress());
    recip.setCity(frm.getRecipientCity());
    recip.setProvince(frm.getRecipientProvince());
    recip.setPostal(frm.getRecipientPostal());
    recip.setBillingNoString(frm.getBillingNo());
    log.debug("Name of recip "+recip.getName());
    MSPReconcile msprec = new MSPReconcile();
    BillingViewBean bean = new BillingViewBean();
    bean.updateBill(frm.getBillingNo(),request.getParameter("billingProvider"));
    
    msprec.saveOrUpdateBillRecipient(recip);
    BillingNote n = new BillingNote();
    try {
      n.addNoteFromBillingNo(frm.getBillingNo(), creator, frm.getMessageNotes());
    }
    catch (Exception e) {
      MiscUtils.getLogger().error("Error", e);
    }

    return mapping.findForward("success");
  }

  /** Creates a new instance of BillingUpdateBillingAction */
  public BillingUpdateBillingAction() {
  }

}
