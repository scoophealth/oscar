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

public class SupServiceCodeAssocActionForm
    extends ActionForm {
  public static final String MODE_EDIT = "edit";
  public static final String MODE_DELETE = "delete";
  public static final String MODE_VIEW = "view";
  private String actionMode = MODE_VIEW;
  private String primaryCode;
  private String secondaryCode;
  private String id;
  public String getActionMode() {
    return actionMode;
  }

  public void setActionMode(String actionMode) {
    this.actionMode = actionMode;
  }

  public void setSecondaryCode(String secondaryCode) {
    this.secondaryCode = secondaryCode;
  }

  public void setPrimaryCode(String primaryCode) {
    this.primaryCode = primaryCode;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPrimaryCode() {
    return primaryCode;
  }

  public String getSecondaryCode() {
    return secondaryCode;
  }

  public String getId() {
    return id;
  }

  public ActionErrors validate(ActionMapping actionMapping,
                               HttpServletRequest httpServletRequest) {
    ActionErrors errors = new ActionErrors();
    BillingAssociationPersistence per = new BillingAssociationPersistence();
    if (SupServiceCodeAssocActionForm.MODE_EDIT.equals(this.actionMode)) {
      if (primaryCode == null || "".equals(primaryCode)){
       errors.add("",
                  new ActionMessage(
                      "oscar.billing.CA.BC.billingBC.error.nullservicecode"));

     }

     else if(!per.serviceCodeExists(primaryCode)){
       errors.add("",
                  new ActionMessage(
                      "oscar.billing.CA.BC.billingBC.error.invalidsvccode",primaryCode));


     }


      if (secondaryCode == null || "".equals(secondaryCode)){
        errors.add("",
                   new ActionMessage(
                       "oscar.billing.CA.BC.billingBC.error.nullservicecode"));

      }

      else if(!per.serviceCodeExists(secondaryCode)){
        errors.add("",
                   new ActionMessage(
                       "oscar.billing.CA.BC.billingBC.error.invalidsvccode",secondaryCode));
      }
    }
    return errors;
  }

  public void reset(ActionMapping actionMapping,
                    HttpServletRequest servletRequest) {
    this.primaryCode = "";
    this.secondaryCode = "";
  }
}
