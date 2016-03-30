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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.oscarehr.common.model.Hl7TextMessage;
import org.oscarehr.managers.LabManager;
import org.oscarehr.ws.rest.conversion.Hl7TextMessageConverter;
import org.oscarehr.ws.rest.to.LabResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/labs")
@Component("labService")
public class LabService extends AbstractServiceImpl {

	@Autowired
	LabManager labManager;
	
	@GET
	@Path("/hl7LabsByDemographicNo")
	@Produces("application/json")
	public LabResponse getHl7LabsByDemographicNo(@QueryParam("demographicNo") int demographicNo, @QueryParam("offset") int offset, @QueryParam("limit") int limit) {
//	public LabResponse getHl7LabsByDemographicNo() {
		
		LabResponse response = new LabResponse();
		
		List<Hl7TextMessage> hl7TextMessages = labManager.getHl7Messages(getLoggedInInfo(), demographicNo,offset,limit);
	
		Hl7TextMessageConverter converter = new Hl7TextMessageConverter();
		response.setMessages(converter.getAllAsTransferObjects(getLoggedInInfo(), hl7TextMessages));
		
		return response;
	}
	
}
