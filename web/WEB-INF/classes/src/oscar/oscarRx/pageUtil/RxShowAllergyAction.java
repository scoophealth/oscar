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
 * Jay Gallagher
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
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

import oscar.oscarRx.data.RxPatientData;


public final class RxShowAllergyAction extends Action {
    
    
    public ActionForward execute(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
                                 
        if(request.getSession().getAttribute("user") == null  || !( ((String) request.getSession().getAttribute("userprofession")).equalsIgnoreCase("doctor") )  ){
            return (mapping.findForward("Logout"));
        }
        
        String user_no = (String) request.getSession().getAttribute("user");
        String demo_no = (String) request.getParameter("demographicNo");
               
        
        if(demo_no == null){
           return mapping.findForward("failure");
        }
        // Setup bean
        RxSessionBean bean;
        
        if(request.getSession().getAttribute("RxSessionBean")!=null) {
            bean = (oscar.oscarRx.pageUtil.RxSessionBean)request.getSession().getAttribute("RxSessionBean");            
            if( (bean.getProviderNo() != user_no) || (bean.getDemographicNo() != Integer.parseInt(demo_no) ) ) {
                bean = new RxSessionBean();
            }
            
        } else {
            bean = new RxSessionBean();
        }
        
        
        bean.setProviderNo(user_no);
        bean.setDemographicNo(Integer.parseInt(demo_no));
        
        request.getSession().setAttribute("RxSessionBean", bean);
        
        RxPatientData rx = null;
        RxPatientData.Patient patient = null;
        try {
            rx = new RxPatientData();
            patient = rx.getPatient(bean.getDemographicNo());
        }
        catch (java.sql.SQLException ex) {
            throw new ServletException(ex);
        }
        
        if(patient!=null) {
            request.getSession().setAttribute("Patient", patient);
            return (mapping.findForward("success"));            
        } else {//no records found        
            response.sendRedirect("error.html");
            return null;
        }
    }
}
