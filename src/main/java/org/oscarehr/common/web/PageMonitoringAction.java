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
package org.oscarehr.common.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.PageMonitorDao;
import org.oscarehr.common.model.PageMonitor;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class PageMonitoringAction extends DispatchAction {
	
	PageMonitorDao pageMonitorDao = SpringUtils.getBean(PageMonitorDao.class);
	
    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String pageName = request.getParameter("page");
    	String sessionId = request.getSession().getId();
    	String ip = request.getRemoteAddr();
    	String reqLock = request.getParameter("lock");
    	//get rid of stale ones
    	pageMonitorDao.updatePage(pageName);
    	
    	List<PageMonitor> otherMonitors = new ArrayList<PageMonitor>();
    	
    	synchronized(this) {
	    	//see what's left
	    	boolean locked=false;
	    	PageMonitor monitorToUpdate=null;
	    	List<PageMonitor> existing = pageMonitorDao.findByPageName(pageName);
	    	if(existing.size() > 0) {
	    		for(PageMonitor pm:existing) {
	    			
	    			if(!request.getSession().getId().equals(pm.getSession()) && pm.isLocked()) {
	    				locked=true;
	    			}
	    			
	    			if(request.getSession().getId().equals(pm.getSession())) {
	    				monitorToUpdate = pm;
	    				pm.setSelf(true);
	    				otherMonitors.add(pm);
	    			} else {
	    				otherMonitors.add(pm);
	    			}
	    		}
	    	}
	    	
	    	boolean toLock=false;
	    	
	    	if(reqLock != null && reqLock.equals("true") && !locked) {
	    		toLock=true;
	    	}
	    	
	    	if(monitorToUpdate == null) {   		
		    	PageMonitor pm = new PageMonitor();
		    	pm.setPageName(pageName);
		    	pm.setSession(sessionId);
		    	pm.setRemoteAddr(ip);
		    	if(reqLock != null)
		    		pm.setLocked(toLock);
		    	pm.setTimeout(60);
		    	pm.setUpdateDate(new Date());
		    	pm.setProviderNo(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
		    	pm.setProviderName(LoggedInInfo.loggedInInfo.get().loggedInProvider.getFormattedName());
		    	pageMonitorDao.persist(pm);
	    	} else {
	    		monitorToUpdate.setTimeout(60);
	    		monitorToUpdate.setUpdateDate(new Date());
	    		monitorToUpdate.setLocked(toLock);
	    		pageMonitorDao.merge(monitorToUpdate);
	    	}
	    	
    	} //end of synchronized block
    	
    	JSONArray jsonArray = JSONArray.fromObject( otherMonitors );
    	response.getWriter().print(jsonArray);
		return null;
	}
}
