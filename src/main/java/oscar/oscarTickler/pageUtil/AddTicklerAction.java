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


package oscar.oscarTickler.pageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.managers.TicklerManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.oscarTickler.TicklerData;

/**
 *
 * @author Jay Gallagher
 */
public class AddTicklerAction extends Action {
   
	private TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
   public AddTicklerAction() {
   }
                   
   public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_tickler", "w", null)) {
			throw new RuntimeException("missing required security object (_tickler)");
		}
	   
      String[] demos = request.getParameterValues("demo");                           
      String message          = request.getParameter("message");
      String status           = request.getParameter("status");
      String service_date     = request.getParameter("date");
      String creator          = (String) request.getSession().getAttribute("user");
      String priority         = request.getParameter("priority");
      String task_assigned_to = request.getParameter("assignedTo");
      
      if (status == null){ status = TicklerData.ACTIVE; }
      if (service_date == null) { service_date = "now()"; }
      if (priority == null) { priority = TicklerData.NORMAL;}
      if (task_assigned_to == null){ task_assigned_to = creator; }
            
      Tickler.STATUS tStatus = Tickler.STATUS.A;
      if(status.equals(TicklerData.COMPLETED)) {
    	  tStatus = Tickler.STATUS.C;
      }
      if(status.equals(TicklerData.DELETED)) {
    	  tStatus = Tickler.STATUS.D;
      }
      
      Tickler.PRIORITY tPriority = Tickler.PRIORITY.Normal;
      if(priority.equals(TicklerData.HIGH)) {
    	  tPriority = Tickler.PRIORITY.High;
      }
      if(priority.equals(TicklerData.LOW)) {
    	  tPriority = Tickler.PRIORITY.Low;
      }
      
      if (demos != null){
         for ( int i = 0; i < demos.length ; i++){

        	 ticklerManager.addTickler(demos[i],message,tStatus,service_date,creator,tPriority,task_assigned_to);
         }
      }         
      return mapping.findForward("close");                                    
   }
}
