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
 * McMaster Unviersity
 * Hamilton
 * Ontario, Canada
 */

package oscar.oscarBilling.ca.bc.pageUtil;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.Action;
import oscar.oscarBilling.ca.bc.data.BillingPreferencesDAO;
import oscar.oscarBilling.ca.bc.data.BillingPreference;

/**
 * Forwards flow of control to Billing Preferences Screen
 * @version 1.0
 */
public class ViewBillingPreferencesAction
    extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {
    String providerNo = servletRequest.getParameter("provider_no");
    BillingPreferencesActionForm frm = (
        BillingPreferencesActionForm) actionForm;
    BillingPreferencesDAO dao = new BillingPreferencesDAO();
    BillingPreference pref = dao.getUserBillingPreference(providerNo);
    //If the user doesn't have a BillingPreference record create one
    if(pref==null){
      pref = new BillingPreference();
      pref.setProviderNo(new Integer(providerNo).intValue());
      dao.saveUserPreferences(pref);
    }
    frm.setReferral(String.valueOf(pref.getReferral()));
    frm.setProviderNo(providerNo);
    return actionMapping.findForward("success");
  }
}
