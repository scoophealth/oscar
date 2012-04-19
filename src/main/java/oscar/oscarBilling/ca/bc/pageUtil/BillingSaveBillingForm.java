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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public final class BillingSaveBillingForm extends ActionForm {
   	
  String submit;   
   
  /**
   * Used to reset everything to a null value
   * @param mapping
   * @param request
   */
  public void reset(ActionMapping mapping, HttpServletRequest request){
  //  this.service = null;
   // this.message = null;
   // this.subject = null;

  }

  /**
   * Getter for property submit.
   * @return Value of property submit.
   */
  public java.lang.String getSubmit() {
     return submit;
  }  
 
  /**
   * Setter for property submit.
   * @param submit New value of property submit.
   */
  public void setSubmit(java.lang.String submit) {
     this.submit = submit;
  }  



  /**
     * Validate the properties that have been set from this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     * @return fill in later
     */
  //public ActionErrors validate(ActionMapping mapping,
  //                               HttpServletRequest request) {

  //   ActionErrors errors = new ActionErrors();

  //   if (message == null || message.length() == 0){
  //      errors.add("message", new ActionError("error.message.missing"));
  //   }

  //   if (provider == null || provider.length == 0){
  //      errors.add(ActionErrors.GLOBAL_ERROR,
  //              new ActionError("error.provider.missing"));
  //   }

  //   return errors;

  //}

}//CreateMessageForm
