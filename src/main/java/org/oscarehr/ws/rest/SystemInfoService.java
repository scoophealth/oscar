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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.oscarehr.ws.rest.to.model.OSPInfoTo1;

import oscar.OscarProperties;

@Path("/systemInfo")
public class SystemInfoService extends AbstractServiceImpl{

	@GET
	@Path("/osp")
	@Produces("application/json")
	public OSPInfoTo1 getOSP() {
		OscarProperties props = OscarProperties.getInstance();
		
		OSPInfoTo1 ospInfo = new OSPInfoTo1();
		ospInfo.setOspContactEmail(props.getProperty("osp.contactEmail", ""));
		ospInfo.setOspContactName(props.getProperty("osp.contactName", ""));
		ospInfo.setOspName(props.getProperty("osp.name", ""));
		ospInfo.setOspPhone(props.getProperty("osp.phone", ""));
		ospInfo.setOspUrl(props.getProperty("osp.url", ""));
		
		return ospInfo;
	}
	
}
