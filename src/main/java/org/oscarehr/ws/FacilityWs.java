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

package org.oscarehr.ws;

import java.util.List;

import javax.jws.WebService;

import org.apache.cxf.annotations.GZIP;
import org.oscarehr.common.model.Facility;
import org.oscarehr.managers.FacilityManager;
import org.oscarehr.ws.transfer_objects.FacilityTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
@GZIP(threshold = AbstractWs.GZIP_THRESHOLD)
public class FacilityWs extends AbstractWs {
	@Autowired
	private FacilityManager facilityManager;

	/**
	 * @deprecated 2013-03-19 grammatical mistaken in name, this only returns 1 default facility, use getDefaultFacility() instead. 
	 */
	public FacilityTransfer getDefaultFacilities() {
		return (FacilityTransfer.toTransfer(facilityManager.getDefaultFacility(getLoggedInInfo())));
	}

	public FacilityTransfer getDefaultFacility() {
		return (FacilityTransfer.toTransfer(facilityManager.getDefaultFacility(getLoggedInInfo())));
	}

	public FacilityTransfer[] getAllFacilities(Boolean active) {
		List<Facility> results = facilityManager.getAllFacilities(getLoggedInInfo(),active);
		return (FacilityTransfer.toTransfers(results));
	}
}
