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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.oscarRx.pageUtil;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import oscar.OscarProperties;
import oscar.oscarRx.data.RxPatientData;


public final class RxChoosePatientAction extends Action {
    
    public void p(String s){
        System.out.println(s);
    }
    public void p(String s,String s2){
        System.out.println(s+"="+s2);
    }
    public ActionForward execute(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
     //   System.out.println("***IN RxChoosePatientAction.java");
        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources(request);
     //   p("locale",locale.toString());
   //     p("messages",messages.toString());
        // Setup variables       
        
        if(request.getSession().getAttribute("user") == null  ){
           return (mapping.findForward("Logout"));
        }
        
        String user_no;
        user_no = (String) request.getSession().getAttribute("user");
      //    p("user_no",user_no);
        RxChoosePatientForm frm = (RxChoosePatientForm)form;
   //     p("frm",frm.toString());
        // Setup bean
        RxSessionBean bean;
        
        if(request.getSession().getAttribute("RxSessionBean")!=null) {
            bean = (oscar.oscarRx.pageUtil.RxSessionBean)request.getSession().getAttribute("RxSessionBean");
            
            if((bean.getProviderNo() != frm.getProviderNo())
            || (bean.getDemographicNo() != Integer.parseInt(frm.getDemographicNo()))) {
                bean = new RxSessionBean();
            }
        }
        else {
            bean = new RxSessionBean();
        }
        
        
        bean.setProviderNo(user_no);
        bean.setDemographicNo(Integer.parseInt(frm.getDemographicNo()));
        
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
            if(OscarProperties.getInstance().getBooleanProperty("RX3", "yes")){//if rx3 is set to yes.
                System.out.println("successRX3");
                    return (mapping.findForward("successRX3"));
            }else   return (mapping.findForward("success"));
            
        } else //no records found
        {
            response.sendRedirect("error.html");
            return null;
        }
    }
}