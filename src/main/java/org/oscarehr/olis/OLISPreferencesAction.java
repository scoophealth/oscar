/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package org.oscarehr.olis;

import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.olis.dao.OLISSystemPreferencesDao;
import org.oscarehr.olis.model.OLISSystemPreferences;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.scheduling.timer.ScheduledTimerTask;

public class OLISPreferencesAction extends DispatchAction  {

	 private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	 
	 @Override
	    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		 	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "r", null)) {
	        	throw new SecurityException("missing required security object (_admin)");
	        }
		 
	     	DateTimeFormatter input = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss Z");
	     	DateTimeFormatter output = DateTimeFormat.forPattern("YYYYMMddHHmmssZ");
	     	DateTime date;
	     	String startTime = oscar.Misc.getStr(request.getParameter("startTime"), "").trim();
	     	if (!startTime.equals("")) {
		     	date = input.parseDateTime(startTime);
		     	startTime = date.toString(output);
	     	}
	     	String endTime = oscar.Misc.getStr(request.getParameter("endTime"), "").trim();
	     	if (!endTime.equals("")) {
		     	date = input.parseDateTime(endTime);
		     	endTime = date.toString(output);
	     	}
	     
	     	Integer pollFrequency = oscar.Misc.getInt(request.getParameter("pollFrequency"), 30);
	     	String filterPatients = request.getParameter("filter_patients");
	     	OLISSystemPreferencesDao olisPrefDao = (OLISSystemPreferencesDao)SpringUtils.getBean("OLISSystemPreferencesDao");
	        OLISSystemPreferences olisPrefs =  olisPrefDao.getPreferences();
	     	
	     	try{
	     		olisPrefs.setStartTime(startTime);
	     		olisPrefs.setEndTime(endTime);
	     		olisPrefs.setFilterPatients((filterPatients!=null)?true:false);
	     		boolean restartTimer = olisPrefs.getPollFrequency() != pollFrequency;	     		
	     		olisPrefs.setPollFrequency(pollFrequency);
	     		olisPrefDao.save(olisPrefs);
	     		request.setAttribute("success", true);
	     		
	     		if (restartTimer) {
	     			ScheduledTimerTask task = (ScheduledTimerTask)SpringUtils.getBean("olisScheduledPullTask");		
	     			TimerTask tt = task.getTimerTask();
	     			Thread t = new Thread(tt);
	     			t.start();
		     	}
	     		
	     	} catch (Exception e){
	     		MiscUtils.getLogger().error("Changing Preferences failed", e); 
				request.setAttribute("success", false);
	     	}
		 
	     	return mapping.findForward("success");
		 
	    }
}
