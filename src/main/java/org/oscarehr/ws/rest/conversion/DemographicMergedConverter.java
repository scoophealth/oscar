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
package org.oscarehr.ws.rest.conversion;

import org.oscarehr.common.model.DemographicMerged;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.rest.to.model.DemographicMergedTo1;

public class DemographicMergedConverter extends AbstractConverter<DemographicMerged, DemographicMergedTo1> {

	@Override
	public DemographicMerged getAsDomainObject(LoggedInInfo loggedInInfo,DemographicMergedTo1 t) throws ConversionException {
		DemographicMerged d = new DemographicMerged();
		
		d.setId(t.getId());
		d.setDemographicNo(t.getDemographicNo());
		d.setMergedTo(t.getMergedTo());
		d.setDeleted(t.getDeleted());
		d.setLastUpdateUser(t.getLastUpdateUser());
		d.setLastUpdateDate(t.getLastUpdateDate());
		return d;
	}

	@Override
	public DemographicMergedTo1 getAsTransferObject(LoggedInInfo loggedInInfo,DemographicMerged d) throws ConversionException {
		DemographicMergedTo1 t = new DemographicMergedTo1();
		
		t.setId(d.getId());
		t.setDemographicNo(d.getDemographicNo());
		t.setMergedTo(d.getMergedTo());
		t.setDeleted(d.getDeleted());
		t.setLastUpdateUser(d.getLastUpdateUser());
		t.setLastUpdateDate(d.getLastUpdateDate());
		return t;
	}

}
