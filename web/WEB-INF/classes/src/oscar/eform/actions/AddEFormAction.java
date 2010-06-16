
/*
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
 * Ontario, Canada   Creates a new instance of EfmData
 *
 *
 * EfmData.java
 *
 * Created on July 28, 2005, 1:54 PM
 */

package oscar.eform.actions;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import oscar.eform.EFormUtil;
import oscar.eform.data.EForm;
import oscar.oscarEncounter.data.EctProgram;

public class AddEFormAction extends Action {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) {
         System.out.println("==================SAVING ==============");
         
         Enumeration paramNamesE = request.getParameterNames();
         //for each name="fieldname" value="myval"
         ArrayList paramNames = new ArrayList();  //holds "fieldname, ...."
         ArrayList paramValues = new ArrayList(); //holds "myval, ...."
         String fid = request.getParameter("efmfid");
         String demographic_no = request.getParameter("efmdemographic_no");
         String provider_no = request.getParameter("efmprovider_no");
         String subject = request.getParameter("subject");
         if (subject == null) subject="";
         String curField = "";
         while (paramNamesE.hasMoreElements()) {
             curField = (String) paramNamesE.nextElement();
             if( curField.equalsIgnoreCase("parentAjaxId"))
                 continue;
             paramNames.add(curField);
             paramValues.add(request.getParameter(curField));
         }
         //----names parsed
         EForm curForm = new EForm(fid, demographic_no, provider_no);
         ActionMessages errors = curForm.setMeasurements(paramNames, paramValues);
         curForm.setFormSubject(subject);
         curForm.setValues(paramNames, paramValues);
         curForm.setImagePath();
         curForm.setAction();
         curForm.setNowDateTime();
         if (!errors.isEmpty()) {
             saveErrors(request, errors);
             request.setAttribute("curform", curForm);
             request.setAttribute("page_errors", "true");
             return mapping.getInputForward();
         }
         String fdid = EFormUtil.addEForm(curForm);

         //adds parsed values
         EFormUtil.addEFormValues(paramNames, paramValues, fdid, fid, demographic_no);
         //add to oscarMeasurements

		 //write template message to echart
		 String program_no = new EctProgram(request.getSession()).getProgram(provider_no);
		 String eformLink = request.getContextPath()+"/eform/efmshowform_data.jsp?fdid="+fdid;
		 EFormUtil.writeEformTemplate(paramNames, paramValues, curForm, eformLink, program_no);
         
         return(mapping.findForward("close"));
    }
}
