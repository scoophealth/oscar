/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
import org.oscarehr.olis.dao.OLISSystemPreferencesDao;
import org.oscarehr.olis.model.OLISSystemPreferences;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.scheduling.timer.ScheduledTimerTask;

public class OLISPreferencesAction extends DispatchAction  {

	 @Override
	    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
