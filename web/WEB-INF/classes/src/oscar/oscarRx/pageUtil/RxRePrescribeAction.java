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

import oscar.oscarRx.data.*;

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


public final class RxRePrescribeAction extends Action {
    
    
    public ActionForward execute(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
        
        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources(request);
        
        oscar.oscarRx.pageUtil.RxSessionBean beanRX =
        (oscar.oscarRx.pageUtil.RxSessionBean)request.getSession().getAttribute("RxSessionBean");
        if(beanRX==null) {
            response.sendRedirect("error.html");
            return null;
        }
        
        try {
            RxPrescriptionData rxData = new RxPrescriptionData();
            
            String drugList = ((RxDrugListForm)form).getDrugList();
            String[] drugArr = drugList.split(",");
            
            int drugId;
            int i;
            
            for(i=0;i<drugArr.length;i++) {
                try {
                    drugId = Integer.parseInt(drugArr[i]);
                } catch (Exception e) { break; }
                
                // get original drug
                RxPrescriptionData.Prescription oldRx =
                rxData.getPrescription(drugId);
                
                // create copy of Prescription
                RxPrescriptionData.Prescription rx =
                rxData.newPrescription(beanRX.getProviderNo(), beanRX.getDemographicNo(), oldRx);
                //beanRX.clearStash();
                beanRX.setStashIndex(beanRX.addStashItem(rx));
                request.setAttribute("BoxNoFillFirstLoad", "true");
                
            }
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
        
        return (mapping.findForward("success"));
    }
}
