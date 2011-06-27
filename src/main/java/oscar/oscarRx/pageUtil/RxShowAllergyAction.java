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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.MiscUtils;

import oscar.oscarRx.data.RxPatientData;
import oscar.oscarRx.data.RxPatientData.Patient.Allergy;


public final class RxShowAllergyAction extends DispatchAction {
    
	
	public ActionForward reorder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {		
		reorder(request);
		ActionForward fwd = mapping.findForward("success-redirect");
		return new ActionForward(fwd.getPath()+ "?demographicNo="+request.getParameter("demographicNo"),true );
	}
    
    public ActionForward unspecified(ActionMapping mapping,
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
        
        if(request.getParameter("method")!=null && request.getParameter("method").equals("reorder")) {
        	reorder(request);
        }
        
        RxPatientData.Patient patient = null;
        try {
            patient = RxPatientData.getPatient(bean.getDemographicNo());
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
    
    private void reorder(HttpServletRequest request) {
    	String direction = request.getParameter("direction");
    	String demographicNo = request.getParameter("demographicNo");
    	int allergyId = Integer.parseInt(request.getParameter("allergyId"));
    	try {
    		Allergy[] allergies = RxPatientData.getPatient(demographicNo).getAllergies();
    		for(int x=0;x<allergies.length;x++) {
    			if(allergies[x].getAllergyId() == allergyId) {
    				if(direction.equals("up")) {    					
    					if(x==0) { continue;}
    					//move ahead
    					int myPosition = allergies[x].getAllergy().getPosition();
    					int swapPosition = allergies[x-1].getAllergy().getPosition();
    					allergies[x].getAllergy().setPosition(swapPosition);
    					allergies[x-1].getAllergy().setPosition(myPosition);
    					allergies[x].Save();
    					allergies[x-1].Save();
    				}
    				if(direction.equals("down")) {    					
    					if(x==(allergies.length-1)) { continue;}
    					int myPosition = allergies[x].getAllergy().getPosition();
    					int swapPosition = allergies[x+1].getAllergy().getPosition();
    					allergies[x].getAllergy().setPosition(swapPosition);
    					allergies[x+1].getAllergy().setPosition(myPosition);
    					allergies[x].Save();
    					allergies[x+1].Save();
    				}
    			}
    		}

    	}catch(Exception e) {
    		MiscUtils.getLogger().error("error",e);
    	}
		
    }
}
