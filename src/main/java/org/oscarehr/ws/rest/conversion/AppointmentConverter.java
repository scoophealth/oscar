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

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.ws.rest.to.model.AppointmentTo1;
import org.springframework.beans.BeanUtils;

public class AppointmentConverter extends AbstractConverter<Appointment, AppointmentTo1> {

	private boolean includeDemographic;
	private boolean includeProvider;
	
	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	private ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	
	public AppointmentConverter() {
		
	}
	
	public AppointmentConverter(boolean includeDemographic, boolean includeProvider) {
		this.includeDemographic = includeDemographic;
		this.includeProvider = includeProvider;
	}
	
	@Override
    public Appointment getAsDomainObject(LoggedInInfo loggedInInfo, AppointmentTo1 t) throws ConversionException {
	    return null;
    }

	@Override
    public AppointmentTo1 getAsTransferObject(LoggedInInfo loggedInInfo, Appointment d) throws ConversionException {
	   AppointmentTo1 t = new AppointmentTo1();
	   
	   BeanUtils.copyProperties(d, t);
	   
	   if(includeDemographic && t.getDemographicNo() > 0) {
		   t.setDemographic(demographicDao.getDemographicById(t.getDemographicNo()));
	   }
	   
	   if(includeProvider && t.getProviderNo() != null) {
		   t.setProvider(providerDao.getProvider(t.getProviderNo()));
	   }
	  
	   return t;
    }

	
}
