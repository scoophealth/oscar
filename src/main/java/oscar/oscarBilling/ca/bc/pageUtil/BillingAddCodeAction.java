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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.oscarBilling.ca.bc.data.BillingCodeData;

public final class BillingAddCodeAction
    extends Action {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
      ServletException {

    

    if (request.getSession().getAttribute("user") == null) {
      return (mapping.findForward("Logout"));
    }

    BillingAddCodeForm frm = (BillingAddCodeForm) form;

    
    String code = frm.getCode();
    String pCode = code;
    String desc = frm.getDesc();
    String value = frm.getValue();
    String whereTo = frm.getWhereTo();
    String returnMessage = null;
    boolean added = true;

    if (whereTo != null && whereTo.equals("private")) {
      code = "A" + code;
    }

    BillingCodeData bcd = new BillingCodeData();
    List list = bcd.findBillingCodesByCode(code,1);

    if (list.size() == 0) {
      bcd.addBillingCode(code, desc, value);
      returnMessage = "Code Added Successfully";
    }else{
      added = false;
      returnMessage = "Code Already in use";
      request.setAttribute("code", pCode);
      request.setAttribute("desc", desc);
      request.setAttribute("value", value);

    }

    request.setAttribute("returnMessage", returnMessage);
    ActionForward retval;
    if (whereTo == null || whereTo.equals("")) {
      if (added) {
        retval = mapping.findForward("success");
        MiscUtils.getLogger().debug("success");
      }
      else {
        retval = mapping.findForward("normalCodeError");
        MiscUtils.getLogger().debug("nCE");
      }
    }
    else {
      if (added) {
        retval = mapping.findForward("private");
        MiscUtils.getLogger().debug("pri");
      }
      else {
        retval = mapping.findForward("privateCodeError");
        MiscUtils.getLogger().debug("privCodErr");
      }
    }

    return retval;
  }

}
