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
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarRx.data.RxPrescriptionData;


public final class RxRePrescribeAction extends DispatchAction {
    
    public ActionForward reprint(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
        
        oscar.oscarRx.pageUtil.RxSessionBean sessionBeanRX =
        (oscar.oscarRx.pageUtil.RxSessionBean)request.getSession().getAttribute("RxSessionBean");
        if(sessionBeanRX==null) {
            response.sendRedirect("error.html");
            return null;
        }
        
        oscar.oscarRx.pageUtil.RxSessionBean beanRX =
        new oscar.oscarRx.pageUtil.RxSessionBean();
        beanRX.setDemographicNo(sessionBeanRX.getDemographicNo());
        beanRX.setProviderNo(sessionBeanRX.getProviderNo());        
        
        RxDrugListForm frm = (RxDrugListForm)form;
        String script_no = frm.getDrugList();
        
        
        String ip = request.getRemoteAddr();
        LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.REPRINT, LogConst.CON_PRESCRIPTION, script_no, ip,""+beanRX.getDemographicNo());
        
        
        RxPrescriptionData rxData = new RxPrescriptionData();
        ArrayList<RxPrescriptionData.Prescription> list = rxData.getPrescriptionsByScriptNo(Integer.parseInt(script_no), sessionBeanRX.getDemographicNo());
        RxPrescriptionData.Prescription p = null;
        
        for( int idx = 0; idx < list.size(); ++idx ) {
            p = list.get(idx);
            beanRX.setStashIndex(beanRX.addStashItem(p));
        }
        
        //save print date/time
        if( p!= null )
            p.Print();
        
        String comment = rxData.getScriptComment( script_no);
       
        
        request.getSession().setAttribute("tmpBeanRX", beanRX);
        request.setAttribute("rePrint", "true");
        request.setAttribute("comment",comment);

        return mapping.findForward("reprint");        
    }
    
    
    public ActionForward represcribe(ActionMapping mapping,
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
        RxDrugListForm frm = (RxDrugListForm)form;                                    
        
        try {
            RxPrescriptionData rxData = new RxPrescriptionData();
            
            String drugList = frm.getDrugList();
            String[] drugArr = drugList.split(",");
            
            int drugId;
            int i;
            
            for(i=0;i<drugArr.length;i++) {
                try {
                    drugId = Integer.parseInt(drugArr[i]);
                } catch (Exception e) { 
                    e.printStackTrace();
                    break; 
                }
                
                // get original drug
                RxPrescriptionData.Prescription oldRx =
                rxData.getPrescription(drugId);
                
                // create copy of Prescription                
                    RxPrescriptionData.Prescription rx =
                        rxData.newPrescription(beanRX.getProviderNo(), beanRX.getDemographicNo(), oldRx);                
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
