
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
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.eform.EFormUtil;
import oscar.eform.data.EForm;
import oscar.oscarEncounter.data.EctProgram;
import oscar.util.StringUtils;

public class AddEFormAction extends Action {
    
	private static final Logger logger=MiscUtils.getLogger();
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) {
         logger.debug("==================SAVING ==============");
         
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

         //Check if eform same as previous, if same -> not saved
         String prev_fdid = (String)request.getSession().getAttribute("eform_data_id");
         request.getSession().removeAttribute("eform_data_id");
         boolean sameform = false;
         if (StringUtils.filled(prev_fdid)) {
             EForm prevForm = new EForm(prev_fdid);
             if (prevForm!=null) {
                 sameform = curForm.getFormHtml().equals(prevForm.getFormHtml());
             }
         }
         if (!sameform) {
        	 EFormDataDao eFormDataDao=(EFormDataDao)SpringUtils.getBean("EFormDataDao");
        	 EFormData eFormData=toEFormData(curForm);
        	 eFormDataDao.persist(eFormData);
             String fdid = eFormData.getId().toString();
             
             EFormUtil.addEFormValues(paramNames, paramValues, new Integer(fdid), new Integer(fid), new Integer(demographic_no)); //adds parsed values
             
             //write template message to echart
             String program_no = new EctProgram(request.getSession()).getProgram(provider_no);
             String path = request.getRequestURL().toString();
             String uri = request.getRequestURI();
             path = path.substring(0, path.indexOf(uri));
             path += request.getContextPath();

             EFormUtil.writeEformTemplate(paramNames, paramValues, curForm, fdid, program_no, path);
         }
         else {
             logger.debug("Warning! Form HTML exactly the same, new form data not saved.");
         }

         return(mapping.findForward("close"));
    }

	private EFormData toEFormData(EForm eForm) {
		EFormData eFormData=new EFormData();
		eFormData.setFormId(Integer.parseInt(eForm.getFid()));
		eFormData.setFormName(eForm.getFormName());
		eFormData.setSubject(eForm.getFormSubject());
		eFormData.setDemographicId(Integer.parseInt(eForm.getDemographicNo()));
		eFormData.setCurrent(true);
		eFormData.setFormDate(new Date());
		eFormData.setFormTime(eFormData.getFormDate());
		eFormData.setProviderNo(eForm.getProviderNo());
		eFormData.setFormData(eForm.getFormHtml());
		eFormData.setPatientIndependent(eForm.getPatientIndependent());
		eFormData.setRoleType(eForm.getRoleType());
		
	    return(eFormData);
    }
}
