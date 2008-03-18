/**
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 * 
 *  Jason Gallagher
 * 
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada   Creates a new instance of AddReferralDocAction
 * 
 * AddReferralDocAction.java
 *
 * Created on November 7, 2005, 11:09 PM
 */

package oscar.oscarBilling.ca.bc.pageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.oscarBilling.ca.bc.data.ReferralBillingData;

/**
 *
 * @author Jay Gallagher
 */
public class AddReferralDocAction extends Action {

  public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) {
     ActionForward af = mapping.findForward("success");
     ReferralBillingData bd = new ReferralBillingData();
     
     AddReferralDocForm f = (AddReferralDocForm) form;
     
     String id = request.getParameter("id");
     if (id == null ){
        if (bd.getNumberOfRecordsUsingBillingNumber(f.getReferral_no()) == 0 ){
           bd.insertIntoBillingReferral(f.getReferral_no(),f.getLast_name(),f.getFirst_name(),f.getSpecialty(),f.getAddress1(),f.getAddress2(),f.getCity(),f.getProvince(),f.getPostal(),f.getPhone(),f.getFax());
        }else{           
           request.setAttribute("Error", "Billing # already in use");
           af = mapping.findForward("error");
        }
     }else{
        bd.updateBillingReferral(id,f.getReferral_no(),f.getLast_name(),f.getFirst_name(),f.getSpecialty(),f.getAddress1(),f.getAddress2(),f.getCity(),f.getProvince(),f.getPostal(),f.getPhone(),f.getFax());
     }                        
     
     //af.setRedirect(true);
     return af;
  }
   public AddReferralDocAction() {
   }
   
}
