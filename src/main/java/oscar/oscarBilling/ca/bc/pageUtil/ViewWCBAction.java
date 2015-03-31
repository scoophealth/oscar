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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.Misc;
import oscar.oscarBilling.ca.bc.data.BillingFormData;
import oscar.oscarBilling.ca.bc.data.BillingmasterDAO;
import oscar.util.SqlUtils;

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
public class ViewWCBAction extends Action {


	DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
	
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse servletResponse) {
    WCBForm frm = (WCBForm) form;
    request.setAttribute("WCBForm", frm);
    String demoNo = request.getParameter("demographic_no");
    String providerNo = request.getParameter("provNo");
    BillingFormData data = new BillingFormData();
    String formId = request.getParameter("formId");
    frm.setWcbFormId(formId);
    //if the formId is zero, this is a new form

    if ("0".equals(formId)) {
      frm.setFormNeeded("1");
      Demographic demographic = demographicManager.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demoNo);

      if (demographic != null) {
        frm.setDemographic(demographic.getDemographicNo().toString());
        frm.setW_fname(demographic.getFirstName());
        frm.setW_lname(demographic.getLastName());
        frm.setW_gender(demographic.getSex());
        frm.setW_phone(demographic.getPhone());
        frm.setW_area(Misc.areaCode(demographic.getPhone()));
        String[] pc = demographic.getPostal().split(" ");

        String postal = "";
        for (int i = 0; i < pc.length; i++) {  // DOES THIS JUST REMOVE SPACES???
          postal += pc[i];
        }
        frm.setW_postal(postal);

        frm.setW_phn(demographic.getHin());
        String seperator = "-";
        String dob = demographic.getYearOfBirth() + seperator +
        		demographic.getMonthOfBirth() + seperator + demographic.getDateOfBirth();
        frm.setW_dob(dob);
        frm.setW_address(demographic.getAddress());
        frm.setW_opcity(demographic.getCity());
        frm.setW_city(demographic.getCity());
        frm.setInjuryLocations(data.getInjuryLocationList());

        //Retrieve provider ohip number and payee number

        List lstResults = SqlUtils.getQueryResultsList("select ohip_no,billing_no from provider where provider_no = " + providerNo);
        if(lstResults!=null){
          String[] providerData = (String[])lstResults.get(0);
          frm.setW_pracno(providerData[0]);
          frm.setW_payeeno(providerData[1]);
        }

        frm.setProviderNo(providerNo);

        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        String fmtStrDate = fm.format(new Date());
        frm.setW_servicedate(fmtStrDate);
      }
    }
    //If the incoming request is for an existing form, retrieve the WCB form data
    //for readonly viewing on the WCB Form Screen
    else {
      request.setAttribute("readonly", "true");
       WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
       BillingmasterDAO billingmasterDAO = (BillingmasterDAO) ctx.getBean("BillingmasterDAO");

       frm.setWCBForms(billingmasterDAO.getWCBForm(formId));
    }
    return (mapping.findForward("success"));
  }
}
