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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class PageMonitoringAction extends DispatchAction {
	
	PageMonitorDao pageMonitorDao = SpringUtils.getBean(PageMonitorDao.class);
	
    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String pageName = request.getParameter("page");
        String pageId = request.getParameter("pageId");
    	String sessionId = request.getSession().getId();
    	String ip = request.getRemoteAddr();

    	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
    	String providerNo=loggedInInfo.getLoggedInProviderNo();
        
        String cleanupExisting = request.getParameter("cleanupExisting");
        boolean cleanupPriorPageAccess = false;
                                                
        if (cleanupExisting != null) {
            cleanupPriorPageAccess = Boolean.parseBoolean(cleanupExisting);
        }
                        
    	String reqLock = request.getParameter("lock");
    	    	
    	List<PageMonitor> otherMonitors = new ArrayList<PageMonitor>();

    	synchronized(this) {
                //get rid of stale ones
                if (cleanupPriorPageAccess) {
                    //they've already opened another appointment window, meaning they already destroyed the last one 
                    //and should therefore also destroy any lock/views on previous instances of the page 
                    pageMonitorDao.removePageNameKeepPageIdForProvider(pageName, pageId, providerNo); 
                }
                
                pageMonitorDao.updatePage(pageName,pageId);
        
	    	//see what's left
	    	boolean locked=false;
	    	PageMonitor monitorToUpdate=null;
                
                List<PageMonitor> existing = null;
                
                if (pageName.equals("addappointment")) {     
                    List <PageMonitor> existingParsed = pageMonitorDao.findByPageName(pageName);
                    try {
                        existing = findByPageName(existingParsed, pageId, request.getLocale());
                    } catch (java.text.ParseException e) {
                        MiscUtils.getLogger().error("Invalid dates",e);
                    }
                } else {
                    existing = pageMonitorDao.findByPage(pageName, pageId);
                }
                
	    	if(existing != null && existing.size() > 0) {
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
                String timeoutStr = request.getParameter("timeout");
                int timeout = 60;
                if (timeoutStr != null) {
                    try {                
                        timeout = Integer.parseInt(timeoutStr);
                    } catch (NumberFormatException e) {}
                }
                
	    	boolean toLock=false;
	    	
	    	if(reqLock != null && reqLock.equals("true") && !locked) {
	    		toLock=true;
	    	}
	    	
                //if existing is null, likely an invalid date was passed, and we shouldn't be updating.
                if (existing != null) {
                    if(monitorToUpdate == null) {   		
                            PageMonitor pm = new PageMonitor();
                            pm.setPageName(pageName);
                            pm.setPageId(pageId);
                            pm.setSession(sessionId);
                            pm.setRemoteAddr(ip);
                            if(reqLock != null)
                                    pm.setLocked(toLock);
                            pm.setTimeout(timeout);
                            pm.setUpdateDate(new Date());
                            pm.setProviderNo(providerNo);
                            pm.setProviderName(loggedInInfo.getLoggedInProvider().getFormattedName());
                            pageMonitorDao.persist(pm);
                    } else {
                            monitorToUpdate.setTimeout(timeout);
                            monitorToUpdate.setUpdateDate(new Date());
                            monitorToUpdate.setLocked(toLock);
                            pageMonitorDao.merge(monitorToUpdate);
                    }
                }
	    	
    	} //end of synchronized block
    	
    	JSONArray jsonArray = JSONArray.fromObject( otherMonitors );
    	response.getWriter().print(jsonArray);       
		return null;
    }
    
    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
        String pageName = request.getParameter("page");
        String pageId = request.getParameter("pageId");    	

        LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
        String providerNo=loggedInInfo.getLoggedInProviderNo();
        
        //User hit cancel, so they actively want to release their (potential) lock on the page
        pageMonitorDao.cancelPageIdForProvider(pageName, pageId, providerNo); 
                
        return null;
    }
    
    private List<PageMonitor> findByPageName(List<PageMonitor> existingParsed, String pageId, Locale locale) throws ParseException{
       
        List<PageMonitor> existing = new ArrayList<PageMonitor>();

        String[] pageIdElements = pageId.split("\\|");
        String apptProviderNo = pageIdElements[0];
        String apptDate = pageIdElements[1];
        String startTime = pageIdElements[2];
        String endTime = pageIdElements[3];
       
        if (existingParsed.size() > 0) {
            for (PageMonitor pm:existingParsed) {

                String[] existingPageIdElements = pm.getPageId().split("\\|");                                
                String existingApptProviderNo = existingPageIdElements[0];

                if (apptProviderNo.equals(existingApptProviderNo)) { 

                    String existingApptDate = existingPageIdElements[1];
                    String existingStartTime = existingPageIdElements[2];
                    String existingEndTime = existingPageIdElements[3];

                    Date startDT = null;
                    Date existingStartDT = null;
                    Date endDT = null;
                    Date existingEndDT = null;

                    startDT = oscar.util.DateUtils.parseDateTime(apptDate + " " + startTime +":00", locale);                                
                    existingStartDT = oscar.util.DateUtils.parseDateTime(existingApptDate + " " + existingStartTime + ":00", locale);
                    endDT = oscar.util.DateUtils.parseDateTime(apptDate + " " + endTime + ":00", locale);                                
                    existingEndDT = oscar.util.DateUtils.parseDateTime(existingApptDate + " " + existingEndTime + ":00", locale);
                    
                    if (startDT.before(existingEndDT) && existingStartDT.before(endDT)) {                        
                        //overlapping date ranges
                        existing.add(pm);
                    }                                                                        
                }
            }
        }
       
        return existing;
    }
}
