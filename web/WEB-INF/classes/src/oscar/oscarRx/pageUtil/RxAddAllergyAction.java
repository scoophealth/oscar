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

import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarRx.data.RxAllergyData;
import oscar.oscarRx.data.RxDrugData;
import oscar.oscarRx.data.RxPatientData;
import oscar.oscarRx.data.RxPatientData.Patient.Allergy;


public final class RxAddAllergyAction extends Action {

    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {
                        
            // Extract attributes we will need
            Locale locale = getLocale(request);
            MessageResources messages = getResources(request);

            // Setup variables
            
            // Add allergy

            int id = Integer.parseInt(request.getParameter("ID"));
            
            String name = request.getParameter("name");
            String type = request.getParameter("type");
            String description = request.getParameter("reactionDescription");
            
	    String startDate = request.getParameter("startDate");
            String ageOfOnset = request.getParameter("ageOfOnset");
            String severityOfReaction = request.getParameter("severityOfReaction");
            String onSetOfReaction = request.getParameter("onSetOfReaction");
	    
            RxPatientData.Patient patient = (RxPatientData.Patient)request.getSession().getAttribute("Patient");
            RxAllergyData ald = new RxAllergyData();
            RxAllergyData.Allergy allergy = ald.getAllergy();
            allergy.setPickID(id);
            allergy.setDESCRIPTION(name);
            allergy.setTYPECODE(Integer.parseInt(type));
            allergy.setReaction(description);
	    
	    if (startDate.length()>=8 && getCharOccur(startDate,'-')==2) {
		allergy.setStartDate(oscar.oscarRx.util.RxUtil.StringToDate(startDate, "yyyy-MM-dd"));
	    } else if (startDate.length()>=6 && getCharOccur(startDate,'-')>=1) {
		allergy.setStartDate(oscar.oscarRx.util.RxUtil.StringToDate(startDate, "yyyy-MM"));
	    } else if (startDate.length()>=4) {
		allergy.setStartDate(oscar.oscarRx.util.RxUtil.StringToDate(startDate, "yyyy"));
	    }
            allergy.setAgeOfOnset(ageOfOnset);
            allergy.setSeverityOfReaction(severityOfReaction);
            allergy.setOnSetOfReaction(onSetOfReaction);
            
            if (type != null && type.equals("13")){          
                RxDrugData drugData = new RxDrugData();
                try{
                RxDrugData.DrugMonograph f = drugData.getDrug(""+id);      
                allergy.setRegionalIdentifier(f.regionalIdentifier);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            
            Allergy allerg = patient.addAllergy(oscar.oscarRx.util.RxUtil.Today(), allergy);      
            
            String ip = request.getRemoteAddr();
            LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_ALLERGY, ""+allerg.getAllergyId() , ip,""+patient.getDemographicNo(), allerg.getAllergy().getAuditString());

            return (mapping.findForward("success"));
    }
    
    private int getCharOccur(String str, char ch) {
	int occurence=0, from=0;
	while (str.indexOf(ch,from)>=0) {
	    occurence++;
	    from = str.indexOf(ch,from)+1;
	}
	return occurence;
    }
}