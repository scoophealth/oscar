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
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarRx.data.RxDrugData;
import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.util.RxUtil;


public final class RxWriteScriptAction extends Action {
    public ActionForward execute(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response) throws IOException, ServletException, Exception {
        
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
	    rx.setLastRefillDate(RxUtil.StringToDate(frm.getLastRefillDate(),"yyyy-MM-dd"));
            rx.setNosubs(frm.getNosubs());
            rx.setPrn(frm.getPrn());
            rx.setSpecial(frm.getSpecial());
            rx.setAtcCode(frm.getAtcCode());
            rx.setRegionalIdentifier(frm.getRegionalIdentifier());
            rx.setUnit(frm.getUnit());
            rx.setUnitName(frm.getUnitName());
            rx.setMethod(frm.getMethod());
            rx.setRoute(frm.getRoute());
            rx.setCustomInstr(frm.getCustomInstr());
            rx.setDosage(frm.getDosage());
	    rx.setOutsideProvider(frm.getOutsideProvider());
	    rx.setLongTerm(frm.getLongTerm());
	    rx.setPastMed(frm.getPastMed());
	    rx.setPatientCompliance(frm.getPatientComplianceY(),frm.getPatientComplianceN());
	    rx.setDrugForm(drugData.getDrugForm(String.valueOf(frm.getGCN_SEQNO())));
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
		    
		    /* Save annotation */
		    HttpSession se = request.getSession();
		    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(se.getServletContext());
		    CaseManagementManager cmm = (CaseManagementManager) ctx.getBean("caseManagementManager");
		    
		    CaseManagementNote cmn = (CaseManagementNote)se.getAttribute(String.valueOf(rx.getDemographicNo())+"annoNote"+CaseManagementNoteLink.DRUGS);
		    if (cmn!=null) {
			cmm.saveNoteSimple(cmn);
			CaseManagementNoteLink cml = new CaseManagementNoteLink();
			cml.setTableName(cml.DRUGS);
			cml.setTableId((long)rx.getDrugId());
			cml.setNoteId(cmn.getId());
			cmm.saveNoteLink(cml);
			
			se.removeAttribute(String.valueOf(rx.getDemographicNo())+"annoNote"+cml.DRUGS);
			se.removeAttribute("anno_display");
			se.removeAttribute("anno_last_id");
		    }
                    rx = null;
                }
                                 
                fwd = "viewScript";
                String ip = request.getRemoteAddr();
                LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_PRESCRIPTION, ""+bean.getDemographicNo(), ip);
          
            }
        }        
        return mapping.findForward(fwd);
    }
}