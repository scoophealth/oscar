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
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.oscarehr.common.model.DemographicMerged;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.ws.rest.conversion.DemographicMergedConverter;
import org.oscarehr.ws.rest.to.OscarSearchResponse;
import org.oscarehr.ws.rest.to.model.DemographicMergedTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Defines a service contract for main operations on demographic. 
 */
@Path("/demographics/merge")
@Component("demographicMergeService")
public class DemographicMergeService extends AbstractServiceImpl {


	@Autowired
	private DemographicManager demographicManager;
	
	/**
	 * Gets child records IDs for the specified parent record  
	 * 
	 * @param parentId
	 * 		Parent demographic record ID to merge children to
	 */
	@GET
	@Path("/{parentId}")
	public OscarSearchResponse<DemographicMergedTo1> getMergedDemographicIds(@PathParam("parentId") Integer parentId) {
		DemographicMergedConverter converter = new DemographicMergedConverter();
		List<DemographicMerged> children = demographicManager.getMergedDemographics(getLoggedInInfo(),parentId);
		OscarSearchResponse<DemographicMergedTo1> response = new OscarSearchResponse<DemographicMergedTo1>();
		for (DemographicMerged dm : children) {
			response.getContent().add(converter.getAsTransferObject(getLoggedInInfo(),dm));
		}
		return response;
	}

	/**
	 * Merges demographic records with the specified children IDs to the 
	 * demographic record with the provided parent id. 
	 * 
	 * @param parentId
	 * 		Parent demographic record ID to merge children to
	 * @param childId
	 * 		Id of the demographic record to be merged to the parent.
	 */
	@PUT
	@Path("/")
	public void mergeDemographic(@QueryParam("parentId") Integer parentId, @QueryParam("childId") Integer childId) {
		List<Integer> children = new ArrayList<Integer>();
		children.add(childId);
		demographicManager.mergeDemographics(getLoggedInInfo(),parentId, children);
	}

	/**
	 * Unmerges demographic records with the specified children IDs from the 
	 * demographic record with the provided parent id. 
	 * 
	 * @param parentId
	 * 		Parent demographic record ID to unmerge children from
	 * @param childId
	 * 		Id of the demographic record to be unmerged from the parent.
	 */
	@DELETE
	@Path("/")
	public void unmergeDemographic(@QueryParam("parentId") Integer parentId, @QueryParam("childsId") Integer childId) {
		List<Integer> children = new ArrayList<Integer>();
		children.add(childId);
		demographicManager.unmergeDemographics(getLoggedInInfo(),parentId, children);
	}

}
