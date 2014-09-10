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

import org.oscarehr.common.model.CustomFilter;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.managers.TicklerManager;
import org.oscarehr.ticklers.service.TicklerService;
import org.oscarehr.ws.rest.conversion.TicklerConverter;
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
	

	@GET
	@Path("/mine")
	@Produces("application/json")
	public TicklerResponse getMyTicklers(@QueryParam("limit") int limit) {
		CustomFilter cf = new CustomFilter(true);
		cf.setAssignee(getLoggedInInfo().getLoggedInProviderNo());
		cf.setStatus("A");
		
		List<Tickler> ticklers = ticklerManager.getTicklers(getLoggedInInfo(),cf,0,limit);

		TicklerResponse result = new TicklerResponse();
		result.setTotal(ticklers.size());
		result.getContent().addAll(ticklerConverter.getAllAsTransferObjects(ticklers)); 
		
		
		return result;
	}
}
