/**
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
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of ScheduleOfBenefitsUpdateAction
 *
 *
 * ScheduleOfBenefitsUpdateAction.java
 *
 * Created on October 7, 2005, 5:29 PM
 */

package oscar.oscarBilling.ca.on.OHIP;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.oscarBilling.ca.on.data.BillingCodeData;

/**
 *
 * @author Jay Gallagher
 */
public class ScheduleOfBenefitsUpdateAction extends Action {
      
   public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
      String [] changes = request.getParameterValues("change");            
      if ( changes != null ){
         BillingCodeData bc = new BillingCodeData();
         ArrayList list = new ArrayList();
         System.out.println("changes #"+changes.length);
         
         for ( int i = 0 ; i < changes.length; i++){
            System.out.println(changes[i]);            
            String[] change = changes[i].split("\\|");
            if (change != null && change.length == 4){
               //change[0] // billing code
               //change[1] // value
               //change[2] //effectiveDate     
               //change[3] //terminactionDate
               bc.editBillingCodeByServiceCode(change[1], change[0]);
               Hashtable h = new Hashtable();
               h.put("code",change[0]);
               h.put("value",change[1]);
               list.add(h);
               //System.out.println(change.length);
               //for ( int j = 0; j < change.length; j++){
               //   System.out.println(j+" "+change[j]);
               //}
               request.setAttribute("changes",list);
            }else{
               System.out.println("test was null");
            }
         
         }
      }
      return mapping.findForward("success");
   }
   
   public ScheduleOfBenefitsUpdateAction() {
   }
   
}
