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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.QuickListDao;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.QuickList;
import org.oscarehr.ws.rest.to.model.DiagnosisTo1;
import org.oscarehr.ws.rest.to.model.DxQuickList;
import org.oscarehr.ws.rest.to.model.IssueTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import oscar.log.LogAction;

@Path("/dxRegisty")
public class DiseaseRegistryService extends AbstractServiceImpl {
	
	@Autowired
	QuickListDao quickListDao;
	
	@Autowired
    @Qualifier("DxresearchDAO")
    protected DxresearchDAO dxresearchDao;
	
	@Autowired
	@Qualifier("IssueDAO")
	private IssueDAO issueDao; 
	
	@GET
	@Path("/quickLists")
	@Produces("application/json")
	public List<DxQuickList> getQuickLists(){
		
		Map<String,DxQuickList> quickListMap = new HashMap<String,DxQuickList>();
		
		List<QuickList> quicklists =quickListDao.findAll();
		
		for(QuickList item : quicklists){
			DxQuickList dxList = quickListMap.get(item.getQuickListName());
			if(dxList == null){
				dxList = new DxQuickList();
				dxList.setLabel(item.getQuickListName());
				quickListMap.put(item.getQuickListName(),dxList);
			}
			
			String desc = dxresearchDao.getDescription(item.getCodingSystem(),item.getDxResearchCode());
			if(desc != null){
				DiagnosisTo1 dx = new DiagnosisTo1();
				dx.setCode(item.getDxResearchCode());
				dx.setCodingSystem(item.getCodingSystem());
				dx.setDescription(desc);
				dxList.getDxList().add(dx);
			}			
		}
		
		
		List<DxQuickList> returnQuickLists = new ArrayList<DxQuickList>(quickListMap.values());
		return returnQuickLists;
	}
	
	@POST
	@Path("/findLikeIssue")
	@Produces("application/json")
	@Consumes("application/json")
	public Response findLikeIssues(DiagnosisTo1 dx){
		Issue issue = issueDao.findIssueByTypeAndCode(dx.getCodingSystem(), dx.getCode());
		IssueTo1 returnIssue = new IssueTo1();
		returnIssue.setCode(issue.getCode());
		returnIssue.setDescription(issue.getDescription());
		returnIssue.setId(issue.getId());
		returnIssue.setType(issue.getType());
		returnIssue.setPriority(issue.getPriority());
		returnIssue.setRole(issue.getRole());
		returnIssue.setUpdate_date(issue.getUpdate_date());
		returnIssue.setSortOrderId(issue.getSortOrderId());
		return Response.ok(returnIssue).build();
	}
	
	@POST
	@Path("/{demographicNo}/add")
	@Produces("application/json")
	@Consumes("application/json")
	public Response addToDiseaseRegistry(@PathParam("demographicNo") Integer demographicNo,IssueTo1 issue){		
		boolean activeEntryExists = dxresearchDao.activeEntryExists(demographicNo, issue.getType(), issue.getCode());
		
		if(!activeEntryExists){
			Dxresearch dx = new Dxresearch();
			dx.setStartDate(new Date());
			dx.setCodingSystem(issue.getType());
			dx.setDemographicNo(demographicNo);
			dx.setDxresearchCode(issue.getCode());
			dx.setStatus('A');
			dx.setProviderNo(getCurrentProvider().getProviderNo());
			dxresearchDao.persist(dx);
			LogAction.addLog(getLoggedInInfo(), "Dxresearch.add", "dxresearch", ""+dx.getId(), ""+demographicNo, dx.toString());
		}
		
		return Response.ok().build();
	}
	
}
