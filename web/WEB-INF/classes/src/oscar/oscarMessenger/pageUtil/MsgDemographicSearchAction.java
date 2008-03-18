// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster University 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarMessenger.pageUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class MsgDemographicSearchAction extends Action {


    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {

    java.util.Vector searchVector;

    MsgDemographicSearchForm frm = ( MsgDemographicSearchForm) form;

    String fName        = frm.getFirstName();
    String lName        = frm.getLastName();
    String phone        = frm.getPhone();
    String hin          = frm.getHin();
    String yearOfBirth  = frm.getYearOfBirth();
    String sex          = frm.getSex();
    String address      = frm.getAddress();
    String chartNumber  = frm.getChartNumber();
    String monthOfBirth = frm.getMonthOfBirth();
    String dayOfBirth   = frm.getDayOfBirth();
    String city         = frm.getCity();
    String id           = frm.getId();


   if ( (dayOfBirth != "") && (dayOfBirth.length() == 1)){
       dayOfBirth = "0"+dayOfBirth;
   }


    oscar.oscarEncounter.search.data.EctSearchDemographicData searchDemographicData = new oscar.oscarEncounter.search.data.EctSearchDemographicData();

    searchVector = searchDemographicData.doSearch(fName,lName,phone,hin,yearOfBirth,sex,address,chartNumber,monthOfBirth,dayOfBirth,city);

    request.setAttribute("searchVector",searchVector);
    request.setAttribute("id",id);

    return (mapping.findForward("success"));
    }
}
