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
package org.oscarehr.ws.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.oscarehr.common.model.CustomFilter;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.managers.TicklerManager;
import org.oscarehr.ticklers.service.TicklerService;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.conversion.TicklerConverter;
import org.oscarehr.ws.rest.to.GenericRESTResponse;
import org.oscarehr.ws.rest.to.TicklerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/tickler")
@Component("ticklerWebService")
public class TicklerWebService extends AbstractServiceImpl {
	
	@Autowired
	private TicklerManager ticklerManager; 
	@Autowired
	private TicklerService ticklerService;
	
	private TicklerConverter ticklerConverter = new TicklerConverter();
	
	@Autowired
	private SecurityInfoManager securityInfoManager;
	

	@POST
	@Path("/search")
	@Produces("application/json")
	@Consumes("application/json")
	public TicklerResponse search(JSONObject json, @QueryParam("startIndex") int startIndex, @QueryParam("limit") int limit) {
		
		if(!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_tickler", "r", null)) {
			throw new RuntimeException("Access Denied");
		}
		
		CustomFilter cf = new CustomFilter(true);
		
		if(json.containsKey("status")) {
			cf.setStatus(json.getString("status"));
		}
		if(json.containsKey("priority")) {
			cf.setPriority(json.getString("priority"));
		}
		if(json.containsKey("assignee")) {
			cf.setAssignee(json.getString("assignee"));
		}
		
		List<Tickler> ticklers = ticklerManager.getTicklers(getLoggedInInfo(),cf,startIndex,limit);

		TicklerResponse result = new TicklerResponse();
		
		if(ticklers.size()==limit) {
			result.setTotal(ticklerManager.getNumTicklers(getLoggedInInfo(), cf));
		} else {
			result.setTotal(ticklers.size());
		}
		
		result.getContent().addAll(ticklerConverter.getAllAsTransferObjects(ticklers)); 
		
		
		return result;
	}
	
	@GET
	@Path("/mine")
	@Produces("application/json")
	public TicklerResponse getMyTicklers(@QueryParam("limit") int limit) {
		
		if(!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_tickler", "r", null)) {
			throw new RuntimeException("Access Denied");
		}
		
		CustomFilter cf = new CustomFilter(true);
		cf.setAssignee(getLoggedInInfo().getLoggedInProviderNo());
		cf.setStatus("A");
		
		List<Tickler> ticklers = ticklerManager.getTicklers(getLoggedInInfo(),cf,0,limit);

		TicklerResponse result = new TicklerResponse();
		result.setTotal(ticklers.size());
		result.getContent().addAll(ticklerConverter.getAllAsTransferObjects(ticklers)); 
		
		
		return result;
	}
	
	@GET
	@Path("/ticklers")
	@Produces("application/json")
	public TicklerResponse getTicklerList() {
		
		if(!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_tickler", "r", null)) {
			throw new RuntimeException("Access Denied");
		}
	
		HttpServletRequest req = this.getHttpServletRequest();
	        
	    String strCount = req.getParameter("count");
	    String strPage = req.getParameter("page");
	    
	    String serviceStartDate = req.getParameter("serviceStartDate");
	    String serviceEndDate = req.getParameter("serviceEndDate");
	    String status = req.getParameter("status");
	    String taskAssignedTo = req.getParameter("taskAssignedTo");
	    String mrp = req.getParameter("mrp");
	    String creator = req.getParameter("creator");
	    String priority = req.getParameter("priority");
	    
	    boolean includeLinks = Boolean.valueOf(req.getParameter("includeLinks"));
	    boolean includeComments = Boolean.valueOf(req.getParameter("includeComments"));
	    boolean includeUpdates = Boolean.valueOf(req.getParameter("includeUpdates"));
	    
	    int count = Integer.parseInt(strCount);
	    int page = Integer.parseInt(strPage);
	    
	    
		CustomFilter cf = new CustomFilter(true);
		
		if(serviceStartDate != null && !"".equals(serviceStartDate)) {
			serviceStartDate = serviceStartDate.startsWith("\"")?serviceStartDate.substring(1,serviceStartDate.length()-1):serviceStartDate;
			try {
				cf.setStartDate(javax.xml.bind.DatatypeConverter.parseDateTime(serviceStartDate).getTime());
			}catch(Exception e) {
				MiscUtils.getLogger().warn("Error parsing start date - " + serviceStartDate);
			}
	    }
		if(serviceEndDate != null && !"".equals(serviceEndDate)) {
			serviceEndDate = serviceEndDate.startsWith("\"")?serviceEndDate.substring(1,serviceEndDate.length()-1):serviceEndDate;
			try {
				cf.setEndDate(javax.xml.bind.DatatypeConverter.parseDateTime(serviceEndDate).getTime());
			}catch(Exception e) {
				MiscUtils.getLogger().warn("Error parsing end date - " + serviceEndDate);
			}
	    }
		
		cf.setStatus(status);
		
		cf.setPriority(priority);
		
		
		if(taskAssignedTo != null && !"".equals(taskAssignedTo)) {
			cf.setAssignee(taskAssignedTo);
		}
		
		if(creator != null && !"".equals(creator)) {
			cf.setProviderNo(creator);
		}
		
		if(mrp != null && !"".equals(mrp)) {
			cf.setMrp(mrp);
		}
		
		TicklerResponse result = new TicklerResponse();
		

		int total = ticklerManager.getNumTicklers(getLoggedInInfo(),cf);
		result.setTotal(total);
		
		
		List<Tickler> ticklers = ticklerManager.getTicklers(getLoggedInInfo(),cf,((page-1)*count),count);
		
		if(includeLinks) {
			ticklerConverter.setIncludeLinks(true);
		}
		if(includeComments) {
			ticklerConverter.setIncludeComments(true);
		}
		if(includeUpdates) {
			ticklerConverter.setIncludeUpdates(true);
		}
		
		result.getContent().addAll(ticklerConverter.getAllAsTransferObjects(ticklers)); 
		
		return result;
	}

	@POST
	@Path("/complete")
	@Produces("application/json")
	@Consumes("application/json")
	public GenericRESTResponse completeTicklers(JSONObject json){
		GenericRESTResponse response = new GenericRESTResponse();
		
		if(!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_tickler", "u", null)) {
			throw new RuntimeException("Access Denied");
		}
		
		
		MiscUtils.getLogger().info(json.toString());
		
		JSONArray ticklerIds = json.getJSONArray("ticklers");
		
		for(Object id : ticklerIds) {
			int ticklerNo = (Integer)id;
			ticklerManager.completeTickler(getLoggedInInfo(), ticklerNo, getLoggedInInfo().getLoggedInProviderNo());
		}
		
		return response;
	}
	
	@POST
	@Path("/delete")
	@Produces("application/json")
	@Consumes("application/json")
	public GenericRESTResponse deleteTicklers(JSONObject json){
		GenericRESTResponse response = new GenericRESTResponse();
		
		if(!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_tickler", "u", null)) {
			throw new RuntimeException("Access Denied");
		}
		
		MiscUtils.getLogger().info(json.toString());
		
		JSONArray ticklerIds = json.getJSONArray("ticklers");
		
		for(Object id : ticklerIds) {
			int ticklerNo = (Integer)id;
			ticklerManager.deleteTickler(getLoggedInInfo(), ticklerNo, getLoggedInInfo().getLoggedInProviderNo());
		}
		
		return response;
	}
}
