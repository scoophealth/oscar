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

import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.ws.rest.to.model.AdmissionTo1;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class AdmissionConverter extends AbstractConverter<Admission, AdmissionTo1> {

	private boolean includeDemographic = false;
	
	private DemographicManager demographicManager;
	
	public AdmissionConverter includeDemographic(boolean val) {
		includeDemographic = val;
		return this;
	}

	@Override
	public Admission getAsDomainObject(LoggedInInfo loggedInInfo,AdmissionTo1 t) throws ConversionException {
		Admission d = new Admission();
		
		BeanUtils.copyProperties(t,d);
		
		return d;
	}

	@Override
	public AdmissionTo1 getAsTransferObject(LoggedInInfo loggedInInfo,Admission d) throws ConversionException {
		AdmissionTo1 t = new AdmissionTo1();
		
		BeanUtils.copyProperties(d, t);
		
		if(includeDemographic) {
			if(demographicManager == null) {
				demographicManager = SpringUtils.getBean(DemographicManager.class);
			}
			Demographic demo = demographicManager.getDemographic(loggedInInfo, d.getClientId());
			if(demo != null) {
				t.setDemographic(new DemographicConverter().getAsTransferObject(loggedInInfo,demo));
			}
		}
	
		return t;
	}
	

}
