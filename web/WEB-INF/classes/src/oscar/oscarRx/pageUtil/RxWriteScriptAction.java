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

import oscar.form.study.HSFO.HSFODAO;
import oscar.oscarRx.data.*;
import oscar.oscarRx.util.*;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;


public final class RxWriteScriptAction extends Action {
    public ActionForward execute(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response) throws IOException, ServletException {
        
        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources(request);
        
        // Setup variables
        RxWriteScriptForm frm = (RxWriteScriptForm)form;        
        
        String fwd = "refresh";
        
        oscar.oscarRx.pageUtil.RxSessionBean bean =
        (oscar.oscarRx.pageUtil.RxSessionBean)request.getSession().getAttribute("RxSessionBean");
        if(bean==null) {
            response.sendRedirect("error.html");
            return null;
        }

        if(frm.getAction().startsWith("update")){            
            
            RxDrugData drugData = new RxDrugData();
            RxPrescriptionData.Prescription rx = bean.getStashItem(bean.getStashIndex());
            RxPrescriptionData prescription = new RxPrescriptionData();
            
            if(frm.getGCN_SEQNO() != 0){ // not custom            
                if(frm.getBrandName().equals(rx.getBrandName())==false){           
                    rx.setBrandName(frm.getBrandName());                    
                }else{            
                    rx.setGCN_SEQNO(frm.getGCN_SEQNO());
                }
            }
            else{ // custom            
                rx.setBrandName(null);
                rx.setGCN_SEQNO(0);
                rx.setCustomName(frm.getCustomName());
            }
            
            rx.setRxDate(RxUtil.StringToDate(frm.getRxDate(),"yyyy-MM-dd"));
            
            rx.setTakeMin(frm.getTakeMinFloat());
            rx.setTakeMax(frm.getTakeMaxFloat());
            rx.setFrequencyCode(frm.getFrequencyCode());
            rx.setDuration(frm.getDuration());
            rx.setDurationUnit(frm.getDurationUnit());
            rx.setQuantity(frm.getQuantity());
            rx.setRepeat(frm.getRepeat());
            rx.setNosubs(frm.getNosubs());
            rx.setPrn(frm.getPrn());
            rx.setSpecial(frm.getSpecial());
            rx.setAtcCode(frm.getAtcCode());
            rx.setRegionalIdentifier(frm.getRegionalIdentifier());
            rx.setUnit(frm.getUnit());
            rx.setMethod(frm.getMethod());
            rx.setRoute(frm.getRoute());
            rx.setCustomInstr(frm.getCustomInstr());
            System.out.println("SAVING STASH " + rx.getCustomInstr());
                     
            bean.setStashItem(bean.getStashIndex(), rx);
         
            rx=null;
         
            
            if(frm.getAction().equals("update")){                
                fwd = "refresh";
            }
            if(frm.getAction().equals("updateAddAnother")){
                fwd = "addAnother";
            }
            if(frm.getAction().equals("updateAndPrint")){
                int i;
                String scriptId = prescription.saveScript(bean);
                
                for(i=0; i<bean.getStashSize(); i++){
                    rx = bean.getStashItem(i);
                    rx.Save(scriptId);
                    rx = null;
                }
                
                // added by vic, hsfo
                if (request.getParameter("hsfo_initDx")==null || Integer.parseInt(request.getParameter("hsfo_initDx")) <0){
                	request.getSession().setAttribute("hsfo_RxDx", -1);
                }
                else{ 
                	// hsfo_initDx < 0 means patient is not enrolled.
                    String[] rxDxes = request.getParameterValues("hsfo_dx");
                    int hsfoRxDx = 0;
                	for (int j=0; j<rxDxes.length; j++)
                		hsfoRxDx += Integer.parseInt(rxDxes[j],10);

                	System.out.println("hsfo_RxDx = "+hsfoRxDx);
               	
                	HSFODAO hsfoDao = new HSFODAO();
                	hsfoDao.updatePatientDx(String.valueOf(bean.getDemographicNo()), hsfoRxDx);

                	request.getSession().setAttribute("hsfo_RxDx", hsfoRxDx);
                	                   
                }
                 
                fwd = "viewScript";
            }
        }        
        return mapping.findForward(fwd);
    }
}