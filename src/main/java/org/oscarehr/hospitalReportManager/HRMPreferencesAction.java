/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
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
