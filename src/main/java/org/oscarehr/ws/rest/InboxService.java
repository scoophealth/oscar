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
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.oscarehr.common.dao.ProviderLabRoutingDao;
import org.oscarehr.inbox.InboxManagerQuery;
import org.oscarehr.inbox.InboxManagerResponse;
import org.oscarehr.managers.InboxManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.ws.rest.to.InboxResponse;
import org.oscarehr.ws.rest.to.model.InboxTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import oscar.oscarLab.ca.on.LabResultData;

@Path("/inbox")
@Component("inboxService")
public class InboxService extends AbstractServiceImpl {

	@Autowired
	private InboxManager inboxManager;
	
	@GET
	@Path("/mine")
	@Produces("application/json")
	public InboxResponse getMyUnacknowlegedReports(@QueryParam("limit") int limit) {
	
		LoggedInInfo loggedInInfo=getLoggedInInfo();
		String providerNo=loggedInInfo.getLoggedInProviderNo();
	
		InboxManagerQuery query = new InboxManagerQuery();
		query.setProviderNo(providerNo);
		query.setSearchProviderNo(providerNo);
		query.setStatus("N");
		query.setScannedDocStatus("I");
		query.setPage(0);
		query.setPageSize(20);
		query.setView("all");
		query.setPatientFirstName("");
		query.setPatientLastName("");
		query.setPatientHIN("");
		
		
		InboxManagerResponse response = inboxManager.getInboxResults(loggedInInfo, query);
		
		List<LabResultData> labDocs = response.getLabdocs();		
		List<InboxTo1> responseItems = new ArrayList<InboxTo1>();
	
		for(LabResultData result:labDocs) {
			InboxTo1 inboxItem = new InboxTo1();
			String segmentID        =  result.getSegmentID();
          
            
            String discipline = result.isDocument() ? result.description == null ? "" : result.description : result.getDisciplineDisplayString();
            String status = ((result.isReportCancelled())? "Cancelled" : result.isFinal() ? "Final" : "Partial");
            
            inboxItem.setId(Integer.parseInt(segmentID));
            inboxItem.setDemographicName(result.getPatientName());
            inboxItem.setDemographicNo(result.getLabPatientId()!=null?Integer.parseInt(result.getLabPatientId()):null);
            inboxItem.setDiscipline(discipline);
            inboxItem.setDateReceived(result.getDateTime() + (result.isDocument() ? " / " + result.lastUpdateDate : ""));
            inboxItem.setPriority(result.getPriority());
            inboxItem.setStatus(status);
            inboxItem.setHin(result.getHealthNumber());
            
            responseItems.add(inboxItem);
            
            if(responseItems.size() > limit) {
            	continue;
            }
            
		}
		
		InboxResponse resp = new InboxResponse();
		resp.setContent(responseItems);
		resp.setLimit(limit);
		resp.setOffset(0);
		resp.setTimestamp(new Date());
		resp.setTotal(getMyUnacknowlegedReportsCount());

		return resp;
	}
	
	@GET
	@Path("/mine/count")
	@Produces("application/json")
	public int getMyUnacknowlegedReportsCount() {
		LoggedInInfo loggedInInfo=getLoggedInInfo();
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		
		ProviderLabRoutingDao dao = SpringUtils.getBean(ProviderLabRoutingDao.class);
		int count = dao.findByProviderNo(providerNo, "N").size();
		
		return count;
	}
}
