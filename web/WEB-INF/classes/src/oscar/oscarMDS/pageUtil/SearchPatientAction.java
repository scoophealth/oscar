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
package oscar.oscarMDS.pageUtil;

import java.io.IOException;
import java.io.PrintStream;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import oscar.oscarMDS.data.MDSResultsData;

public class SearchPatientAction extends Action {
   
   public SearchPatientAction() {
   }
   
   public ActionForward perform(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
   throws ServletException, IOException {
      //System.out.println("inside ReportStatusUpdateAction");
      
      String labNo = request.getParameter("segmentID");
      String name = request.getParameter("name");
      String newURL = "";
      
      try {
         String demographicNo = MDSResultsData.searchPatient(labNo);
         if ( ! demographicNo.equals("0") ) {
            newURL = mapping.findForward("success").getPath();
            newURL = newURL + "?demographicNo="+demographicNo;
         } else {
            newURL = mapping.findForward("failure").getPath();
         }
      } catch (Exception e) {
         System.out.println("exception in SearchPatientAction:"+e);
         newURL = mapping.findForward("failure").getPath();
      }
      newURL = newURL + "&labNo="+labNo+"&keyword="+java.net.URLEncoder.encode(name);
      //System.out.println("url "+newURL);
      return new ActionForward(newURL);
   }
}