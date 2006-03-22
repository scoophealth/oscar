package oscar.oscarBilling.ca.bc.pageUtil;

import java.io.IOException;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;

import org.apache.struts.action.ActionForm;

import org.apache.struts.action.ActionForward;

import org.apache.struts.action.ActionMapping;
import java.util.List;
import oscar.util.StringUtils;
import oscar.util.SqlUtils;
import oscar.entities.Demographic;
import oscar.Misc;
import oscar.oscarBilling.ca.bc.data.BillingFormData;

/*
 * Copyright (c) 2001-2002. Andromedia. All Rights Reserved. *

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

 * This software was written for

 * Andromedia, to be provided as

 * part of the OSCAR McMaster

 * EMR System

 *

 * @author Jef King

 * For The Oscar McMaster Project

 * Developed By Andromedia

 * www.andromedia.ca

 */

public final class WCBAction
    extends Action {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response)

      throws IOException, ServletException {
    WCBForm frm = (WCBForm) form;
    request.setAttribute("WCBForm", frm);
    String demoNo = request.getParameter("demographic_no");
    if (null != form) {
      if ("1".equals(request.getParameter("newform"))) {
        List lst = SqlUtils.getBeanList(
            "select * from demographic where demographic_no = " + demoNo,
            Demographic.class);
        if (!lst.isEmpty()) {
          Demographic demo = (Demographic) lst.get(0);
          BillingFormData data = new BillingFormData();

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
        }
      }
      else {
        request.setAttribute("GOBACKWCB", "true");
        request.getSession().setAttribute("WCBForm",frm);
      }
      return (mapping.findForward("success"));
    }
    else {
      return (mapping.findForward("failure"));
    }
  }
}
