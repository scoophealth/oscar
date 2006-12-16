package org.oscarehr.PMmodule.web.reports;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.web.BaseAction;

/**
 * Will report some basic statistics out of the PMM
 * 
 * 1) # of programs 
 * 2) # of bed programs
 * 3) # of service programs
 * 
 * 
 * @author marc
 *
 */
public class BasicReportAction extends BaseAction {
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return form(mapping,form,request,response);
	}
	
	public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		Date endDate= new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -12);
		Date startDate = cal.getTime();
		
		request.setAttribute("programStatistics", this.getProgramStatistics());
		request.setAttribute("providerStatistics", this.getProviderStatistics());
		
		
		return mapping.findForward("form");		
	}
	
	protected Map getProgramStatistics() {
		Map map = new LinkedHashMap();
		int total = 0, totalBed = 0, totalService = 0;
		
		List programs = programManager.getProgramsByAgencyId("0");
		
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
}
