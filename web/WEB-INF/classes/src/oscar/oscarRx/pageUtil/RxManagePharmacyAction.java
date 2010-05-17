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

import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.struts.actions.DispatchAction;
import oscar.oscarRx.data.RxPharmacyData;
import oscar.oscarRx.data.RxPharmacyData.Pharmacy;

/**
 *
 * @author  Jay Gallagher & Jackson Bi
 */
public final class RxManagePharmacyAction extends DispatchAction {

    public ActionForward unspecified(ActionMapping mapping,
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

    public ActionForward getPharmacyInfo(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {
        String pharmacyId=request.getParameter("pharmacyId");
        System.out.println("pharmacyId="+pharmacyId);
        if(pharmacyId==null) return null;
        RxPharmacyData pharmacyData = new RxPharmacyData();
        Pharmacy pharmacy=pharmacyData.getPharmacy(pharmacyId);
        HashMap hm=new HashMap();
       if(pharmacy!=null){
           hm.put("address", pharmacy.address);
            hm.put("city", pharmacy.city);
            hm.put("email", pharmacy.email);
            hm.put("fax", pharmacy.fax);
            hm.put("name", pharmacy.name);
            hm.put("phone1", pharmacy.phone1);
            hm.put("phone2", pharmacy.phone2);
            hm.put("postalCode", pharmacy.postalCode);
            hm.put("province", pharmacy.province);
            hm.put("notes", pharmacy.notes);
            System.out.println("in getPharmacyInfo,hm="+hm);
            JSONObject jsonObject = JSONObject.fromObject(hm);
            response.getOutputStream().write(jsonObject.toString().getBytes());
       }
        return null;
    }
   /** Creates a new instance of RxManagePharmacyAction */
   public RxManagePharmacyAction() {
   }

}
