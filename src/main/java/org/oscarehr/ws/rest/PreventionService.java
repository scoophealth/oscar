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

import org.oscarehr.managers.PreventionManager;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.ws.rest.conversion.PreventionConverter;
import org.oscarehr.ws.rest.to.PreventionResponse;
import org.oscarehr.ws.rest.to.model.PreventionTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Path("/preventions")
@Component("preventionService")
public class PreventionService extends AbstractServiceImpl {

	@Autowired
	private PreventionManager preventionManager;

	@GET
	@Path("/active")
	@Produces("application/json")
	public PreventionResponse getCurrentPreventions(@QueryParam("demographicNo") Integer demographicNo) {
		List<Prevention> preventions = preventionManager.getPreventionsByDemographicNo(getLoggedInInfo(), demographicNo);
		
		List<PreventionTo1> preventionsT = new PreventionConverter().getAllAsTransferObjects(getLoggedInInfo(), preventions);
		
		PreventionResponse response = new PreventionResponse();
		response.setPreventions(preventionsT);
		
		return response;
	}

}
