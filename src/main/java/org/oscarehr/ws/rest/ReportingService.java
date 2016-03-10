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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.dao.EFormReportToolDao;
import org.oscarehr.common.model.DemographicSets;
import org.oscarehr.common.model.EFormReportTool;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.DemographicSetsManager;
import org.oscarehr.managers.EFormReportToolManager;
import org.oscarehr.web.PatientListApptBean;
import org.oscarehr.web.PatientListApptItemBean;
import org.oscarehr.ws.rest.conversion.EFormReportToolConverter;
import org.oscarehr.ws.rest.to.AbstractSearchResponse;
import org.oscarehr.ws.rest.to.GenericRESTResponse;
import org.oscarehr.ws.rest.to.model.EFormReportToolTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

@Path("/reporting/")
@Component
public class ReportingService extends AbstractServiceImpl {
	
	//private static final Logger logger = MiscUtils.getLogger();

	@Autowired
	DemographicSetsManager demographicSetsManager;
	
	@Autowired
	DemographicManager demographicManager;
	
	@Autowired
	EFormReportToolManager eformReportToolManager;
	
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
		
	/**
	 * EFromReportTool is a utility for taking a snapshot of key-value pair data from saved eforms
	 * to a new table for easier querying. need _admin.eformreporttool security object.
	 * @return
	 */
	@GET
	@Path("/eformReportTool/list")
	@Produces("application/json")
	public AbstractSearchResponse<EFormReportToolTo1> eformReportToolList(){
		
		List<EFormReportTool> results = eformReportToolManager.findAll(getLoggedInInfo(), 0, EFormReportToolDao.MAX_LIST_RETURN_SIZE);
		
		EFormReportToolConverter converter = new EFormReportToolConverter(true, true);
		
		AbstractSearchResponse<EFormReportToolTo1> response = new AbstractSearchResponse<EFormReportToolTo1>();
		
		response.setContent(converter.getAllAsTransferObjects(getLoggedInInfo(), results));
		response.setTotal(response.getContent().size());
		
		return (response);
	}
	
	
	@POST
	@Path("/eformReportTool/add")
	@Produces("application/json")
	@Consumes("application/json")
	public GenericRESTResponse addEFormReportTool(EFormReportToolTo1 json){
		
		GenericRESTResponse response = new GenericRESTResponse();
		
		if(StringUtils.isEmpty(json.getName()) || json.getEformId() == 0) {
			response.setSuccess(false);
			response.setMessage("Need required fields");
			return response;
		}
		
		EFormReportToolConverter converter = new EFormReportToolConverter();
		
		eformReportToolManager.addNew(getLoggedInInfo(),converter.getAsDomainObject(getLoggedInInfo(),json), json.isUseNameAsTableName());
		
		return (response);
	}
	
	@POST
	@Path("/eformReportTool/populate")
	@Produces("application/json")
	@Consumes("application/json")
	public GenericRESTResponse populateEFormReportTool(EFormReportToolTo1 json){
		
		GenericRESTResponse response = new GenericRESTResponse();
		
		eformReportToolManager.populateReportTable(getLoggedInInfo(), json.getId());
		
		return (response);
	}
	
	
	@POST
	@Path("/eformReportTool/remove")
	@Produces("application/json")
	@Consumes("application/json")
	public GenericRESTResponse removeEFormReportTool(EFormReportToolTo1 json){
		
		GenericRESTResponse response = new GenericRESTResponse();
		
		eformReportToolManager.remove(getLoggedInInfo(), json.getId());
		
		return (response);
	}
	
	@POST
	@Path("/eformReportTool/markLatest")
	@Produces("application/json")
	@Consumes("application/json")
	public GenericRESTResponse markLatestEFormReportTool(EFormReportToolTo1 json){
		
		GenericRESTResponse response = new GenericRESTResponse();
		
		eformReportToolManager.markLatest(getLoggedInInfo(), json.getId());
		
		return (response);
	}
	
	
}
