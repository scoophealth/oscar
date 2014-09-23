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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import net.sf.json.JSONObject;

import org.oscarehr.common.model.DemographicSets;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.DemographicSetsManager;
import org.oscarehr.web.PatientListApptBean;
import org.oscarehr.web.PatientListApptItemBean;
import org.oscarehr.ws.rest.to.AbstractSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/reporting/")
@Component
public class ReportingService extends AbstractServiceImpl {
	
	//private static final Logger logger = MiscUtils.getLogger();

	@Autowired
	DemographicSetsManager demographicSetsManager;
	
	@Autowired
	DemographicManager demographicManager;
	
	
	@GET
	@Path("/demographicSets/list")
	@Produces("application/json")
	public AbstractSearchResponse<String> listDemographicSets(){
		AbstractSearchResponse<String> response = new AbstractSearchResponse<String>();
		
		response.setContent(demographicSetsManager.getNames(getLoggedInInfo()));
		response.setTotal(response.getContent().size());
		
		return (response);
	}
	
	@GET
	@Path("/demographicSets/demographicSet/{name}")
	@Produces("application/json")
	public AbstractSearchResponse<DemographicSets> getDemographicSetByName(@PathParam("name") String name){
		AbstractSearchResponse<DemographicSets> response = new AbstractSearchResponse<DemographicSets>();
		
		response.setContent(demographicSetsManager.getByName(getLoggedInInfo(), name));
		response.setTotal(response.getContent().size());
		
		return (response);
	}
	
	@POST
	@Path("/demographicSets/patientList")
	@Produces("application/json")
	@Consumes("application/json")
	public PatientListApptBean getAsPatientList(JSONObject json){
		
		PatientListApptBean response = new PatientListApptBean();
		
		if(json.containsKey("name") && json.getString("name").length()>0) {
		
			for(DemographicSets demographicSet : demographicSetsManager.getByName(getLoggedInInfo(), json.getString("name"))) {
				PatientListApptItemBean item = new PatientListApptItemBean();
				item.setDemographicNo(demographicSet.getDemographicNo());
				item.setName(demographicManager.getDemographicFormattedName(getLoggedInInfo(), item.getDemographicNo()));
				response.getPatients().add(item);
			}
		} 

		return (response);
	}
		
}
