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
 * Jason Gallagher
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */ 


/*
 * RxManagePharmacyAction.java
 *
 * Created on September 29, 2004, 3:20 PM
 */

package oscar.oscarRx.pageUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.oscarRx.data.RxPharmacyData;

/**
 *
 * @author  Jay Gallagher
 */
public final class RxManagePharmacyAction extends Action {

    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {           
           
           RxManagePharmacyForm frm = (RxManagePharmacyForm) form;
           
           String actionType = frm.getPharmacyAction();
           RxPharmacyData pharmacy = new RxPharmacyData();
           
           if(actionType.equals("Add")){
              pharmacy.addPharmacy(frm.getName(), frm.getAddress(), frm.getCity(), frm.getProvince(), frm.getPostalCode(), frm.getPhone1(), frm.getPhone2(), frm.getFax(), frm.getEmail(), frm.getNotes());
           }else if(actionType.equals("Edit")){
              pharmacy.updatePharmacy(frm.getID(),frm.getName(), frm.getAddress(), frm.getCity(), frm.getProvince(), frm.getPostalCode(), frm.getPhone1(), frm.getPhone2(), frm.getFax(), frm.getEmail(), frm.getNotes());
           }else if(actionType.equals("Delete")){
              pharmacy.deletePharmacy(frm.getID());
           }
           
       return mapping.findForward("success");     
    }
   
   /** Creates a new instance of RxManagePharmacyAction */
   public RxManagePharmacyAction() {
   }
   
}
