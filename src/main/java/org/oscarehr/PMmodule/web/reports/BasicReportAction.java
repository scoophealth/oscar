/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.web.reports;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;

/**
 * Will report some basic statistics out of the PMM
 * 
 * 1) # of programs 
 * 2) # of bed programs
 * 3) # of service programs
 */
public class BasicReportAction extends DispatchAction {
	
    private ProgramManager programManager;
    private ProviderManager providerManager;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return form(mapping,form,request,response);
	}
	
	public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -12);
			
		request.setAttribute("programStatistics", this.getProgramStatistics());
		request.setAttribute("providerStatistics", this.getProviderStatistics());
		
		
		return mapping.findForward("form");		
	}
	
	protected Map getProgramStatistics() {
		Map map = new LinkedHashMap();
		int total = 0, totalBed = 0, totalService = 0;
		
		List programs = programManager.getPrograms();
		
		for(Iterator iter = programs.iterator();iter.hasNext();) {
			Program p = (Program)iter.next();
			if(p.getType().equalsIgnoreCase("bed")) {
				totalBed++;
			}
			if(p.getType().equalsIgnoreCase("service")) {
				totalService++;
			}
			total++;
		}
		
		map.put("Total number of programs", new Long(total));
		map.put("Total number of bed programs", new Long(totalBed));
		map.put("Total number of service programs", new Long(totalService));
		return map;
	}
	
	protected Map getProviderStatistics() {
		Map map = new LinkedHashMap();
		
		map.put("Total number of providers",new Long(providerManager.getProviders().size()));
		/*
		List roles = roleManager.getRoles();
		for(Iterator iter=roles.iterator();iter.hasNext();) {
			Role role = (Role)iter.next();
			
		}
		*/
		return map;
	}

    public void setProgramManager(ProgramManager mgr) {
    	this.programManager = mgr;
    }

    public void setProviderManager(ProviderManager mgr) {
    	this.providerManager = mgr;
    }
}
