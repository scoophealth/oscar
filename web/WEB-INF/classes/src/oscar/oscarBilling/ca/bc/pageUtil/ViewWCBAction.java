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

import java.util.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import oscar.*;
import oscar.entities.*;
import oscar.oscarBilling.ca.bc.data.*;
import oscar.util.*;
import java.text.SimpleDateFormat;

/**
 *
 * <p>Title:ViewWCBAction </p>
 *
 * <p>Description: Coordinates data retrieval and configuration parameters for rendering either</p>
 * <p>a new or existing WCB form
 *
 * @author Joel Legris
 * @version 1.0
 */
public class ViewWCBAction
    extends Action {
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse servletResponse) {
    WCBForm frm = (WCBForm) form;
    request.setAttribute("WCBForm", frm);
    String demoNo = request.getParameter("demographic_no");
    BillingFormData data = new BillingFormData();
    String formId = request.getParameter("formId");
    //if the formId is zero, this is a new form
    if ("0".equals(formId)) {
      frm.setFormNeeded("1");
      List lst = SqlUtils.getBeanList(
          "select * from demographic where demographic_no = " + demoNo,
          Demographic.class);
      if (!lst.isEmpty()) {
        Demographic demo = (Demographic) lst.get(0);
        frm.setDemographic(demo.getDemographic_no().toString());
        frm.setW_fname(demo.getFirst_name());
        frm.setW_lname(demo.getLast_name());
        frm.setW_gender(demo.getSex());
        frm.setW_phone(demo.getPhone());
        frm.setW_area(Misc.areaCode(demo.getPhone()));
        String[] pc = demo.getPostal().split(" ");

        String postal = "";
        for (int i = 0; i < pc.length; i++) {
          postal += pc[i];
        }
        frm.setW_postal(postal);

        frm.setW_phn(demo.getHin());
        String seperator = "-";
        String dob = demo.getYear_of_birth() + seperator +
            demo.getMonth_of_birth() + seperator + demo.getDate_of_birth();
        frm.setW_dob(dob);
        frm.setW_address(demo.getAddress());
        frm.setW_opcity(demo.getCity());
        frm.setW_city(demo.getCity());
        frm.setInjuryLocations(data.getInjuryLocationList());
        frm.setW_pracno(request.getParameter("provNo"));

        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        String fmtStrDate = fm.format(new Date());
        frm.setW_servicedate(fmtStrDate);
      }
    }
    //If the incoming request is for an existing form, retrieve the WCB form data
    //for readonly viewing on the WCB Form Screen
    else {
      request.setAttribute("readonly", "true");
      frm.setWCBForms(data.getWCBFromID(formId));
    }
    return (mapping.findForward("success"));
  }
}
