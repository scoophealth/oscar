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


package oscar.oscarBilling.pageUtil;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

public final class BillingAction extends Action {
    
    public ActionForward execute(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
        
        Locale locale = getLocale(request);
        
        if(request.getSession().getAttribute("user") == null  ){
            return (mapping.findForward("Logout"));
        }
        
        // Setup variables
        oscar.oscarBilling.pageUtil.BillingSessionBean bean = null;
        if(request.getParameter("billRegion").equals("ON")){                        
            String newURL = mapping.findForward("ON").getPath();
            newURL = newURL + "?"+request.getQueryString();                            
            
            ActionForward ON = new ActionForward();
            ON.setPath(newURL);
            ON.setRedirect(true);
            return ON;
        }else{
                
            if(request.getParameter("demographic_no")!=null & request.getParameter("appointment_no")!=null) {

                // ((BillingForm)form).setXml_location("P1|PEMBERTON");
                bean = new oscar.oscarBilling.pageUtil.BillingSessionBean();

                bean.setApptProviderNo(request.getParameter("apptProvider_no"));
                bean.setPatientName(request.getParameter("demographic_name"));
                bean.setProviderView(request.getParameter("providerview"));
                bean.setBillRegion(request.getParameter("billRegion"));
                bean.setBillForm(request.getParameter("billForm"));
                bean.setCreator(request.getParameter("user_no"));
                bean.setPatientNo(request.getParameter("demographic_no"));
                bean.setApptNo(request.getParameter("appointment_no"));
                bean.setApptDate(request.getParameter("appointment_date"));
                bean.setApptStart(request.getParameter("start_time"));
                bean.setApptStatus(request.getParameter("status"));
                request.getSession().setAttribute("billingSessionBean", bean);

                MiscUtils.getLogger().debug("PatientName is:" + bean.getPatientName());
            }else{
                bean = (oscar.oscarBilling.pageUtil.BillingSessionBean)request.getSession().getAttribute("billingSessionBean");
            }
        
        }
        
        return (mapping.findForward(request.getParameter("billRegion")));
    }
    
}
