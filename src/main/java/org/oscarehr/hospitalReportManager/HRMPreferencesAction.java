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


package org.oscarehr.hospitalReportManager;

//import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;



public class HRMPreferencesAction extends DispatchAction  {

	 @Override
	    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	     	
		 
		 	String userName = request.getParameter("userName");
	     	String location = request.getParameter("location");
	     	String interval = request.getParameter("interval");
	     	
	     	
	     	UserPropertyDAO userPropertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");
	     	
	     	try{
	     		UserProperty prop;
	     		
	     		if ((prop = userPropertyDao.getProp("hrm_username")) == null) {
	     			prop = new UserProperty();
	     		}
	     		prop.setName("hrm_username");
	     		prop.setValue(userName);
	     		userPropertyDao.saveProp(prop);
	     		
	     		if ((prop = userPropertyDao.getProp("hrm_location")) == null) {
	     			prop = new UserProperty();
	     		}
	     		prop.setName("hrm_location");
	     		prop.setValue(location);
	     		userPropertyDao.saveProp(prop);
	     		
	     		if ((prop = userPropertyDao.getProp("hrm_interval")) == null) {
	     			prop = new UserProperty();
	     		}
	     		prop.setName("hrm_interval");
	     		prop.setValue(interval);
	     		userPropertyDao.saveProp(prop);
	     		
	     		//SFTPConnector.setDownloadsDirectory(location);
	     		
	     		
	     		
	     		request.setAttribute("success", true);
	     	} catch (Exception e){
	     		MiscUtils.getLogger().error("Changing Preferences failed", e); 
				request.setAttribute("success", false);
	     	}
		 
	     	return mapping.findForward("success");
		 
	    }
	 
	 
}
