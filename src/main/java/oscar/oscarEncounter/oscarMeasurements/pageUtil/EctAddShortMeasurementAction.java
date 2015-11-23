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


package oscar.oscarEncounter.oscarMeasurements.pageUtil; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarPrevention.reports.FollowupManagement;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author jay
 */
public class EctAddShortMeasurementAction extends DispatchAction{
    
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    /** Creates a new instance of EctAddShortMeasurementAction */
    public EctAddShortMeasurementAction() {
    }
    
    public ActionForward unspecified(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws IOException  {
     
    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_measurement", "w", null)) {
			throw new SecurityException("missing required security object (_measurement)");
		}
    	
        //MARK IN MEASUREMENTS????
       String followUpType =  request.getParameter("followupType");//"FLUF";
       String followUpValue = request.getParameter("followupValue"); //"L1";
       String[] demos = request.getParameterValues("demos");
       Integer id = Integer.parseInt(request.getParameter("id"));
       String providerNo = (String) request.getSession().getAttribute("user");
  
       String comment = request.getParameter("message");
       if ( followUpType != null && followUpValue != null){   
           FollowupManagement fup = new FollowupManagement();
           MiscUtils.getLogger().debug("followUpType:"+followUpType+" followUpValue: "+followUpValue+" demos:"+demos+" providerNo:"+providerNo+" comment:"+comment);
           fup.markFollowupProcedure(followUpType,followUpValue,demos,providerNo,new Date(),comment);
           response.getWriter().print("id="+id+"&followupValue="+StringEscapeUtils.escapeJavaScript(followUpValue)+"&Date="+UtilDateUtilities.DateToString(new Date()));        
       }
       return null;
    }
    
    /*
     * Add Measurements from prevention report.  Allow multiple values with multiple demos
     */
    public ActionForward addMeasurements(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) {
    	
    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_measurement", "w", null)) {
			throw new SecurityException("missing required security object (_measurement)");
		}
    	
    	String followUpType = request.getParameter("followUpType");
    	String[] arrDemoContactMethods = request.getParameterValues("nsp");
    	String providerNo = (String) request.getSession().getAttribute("user");
    	Map<String,List<String>> nextContactMethods = new HashMap<String,List<String>>();
    	String[] arrDemoMethod = null;
    	List<String> demos;
    	
    	if( arrDemoContactMethods != null ) {    		
	    	for( int idx = 0; idx < arrDemoContactMethods.length; ++idx ) {
	    		arrDemoMethod = arrDemoContactMethods[idx].split(",");	    		
	    		if( arrDemoMethod.length != 2 ) {
	    			continue;
	    		}
	    	
	    		demos = nextContactMethods.get(arrDemoMethod[1]);
	    		if( demos == null ) {
	    			demos = new ArrayList<String>();    			
	    			nextContactMethods.put(arrDemoMethod[1], demos);
	    		}
	    		
	    		demos.add(arrDemoMethod[0]);
	    	}
    	
    	}
    	
    	if( followUpType != null && !nextContactMethods.isEmpty() ) {
    		String comment = null;
    		FollowupManagement fup = new FollowupManagement();
    		Set<String> keys = nextContactMethods.keySet();
    		
    		for( String key : keys ) {
    			arrDemoMethod = nextContactMethods.get(key).toArray(new String[nextContactMethods.get(key).size()]);
    			fup.markFollowupProcedure(followUpType,key,arrDemoMethod,providerNo,new Date(),comment);
    		}
    	}
    	
    	return null;
    }
    
}
