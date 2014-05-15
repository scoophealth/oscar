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

import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.TicklerDao;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.ws.rest.to.model.TicklerTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//this isn't working - not being injected
@Component
public class TicklerConverter extends AbstractConverter<Tickler, TicklerTo1> {

	@Autowired
	private TicklerDao dao;
	
	
	@Override
	public Tickler getAsDomainObject(TicklerTo1 t) throws ConversionException {
		Tickler d = new Tickler();
		d.setCreator(t.getCreator());
		d.setDemographicNo(t.getDemographicNo());
		d.setId(t.getId());
		d.setMessage(t.getMessage());
		d.setPriority(t.getPriority());
		d.setProgramId(t.getProgramId());
		d.setServiceDate(t.getServiceDate());
		d.setStatus(t.getStatus());
		d.setTaskAssignedTo(t.getTaskAssignedTo());
		d.setUpdateDate(t.getUpdateDate());
		
		return d;
	}

	@Override
	public TicklerTo1 getAsTransferObject(Tickler t) throws ConversionException {
		TicklerTo1 d = new TicklerTo1();
		
		d.setCreator(t.getCreator());
		d.setDemographicNo(t.getDemographicNo());
		d.setId(t.getId());
		d.setMessage(t.getMessage());
		d.setPriority(t.getPriority());
		d.setProgramId(t.getProgramId());
		d.setServiceDate(t.getServiceDate());
		d.setStatus(t.getStatus());
		d.setTaskAssignedTo(t.getTaskAssignedTo());
		d.setUpdateDate(t.getUpdateDate());
		
		//want responses to include patient name - be nice to have this configurable by a parameter map.
		DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
		d.setDemographicName(demographicDao.getDemographicById(t.getDemographicNo()).getFormattedName());
		return d;
	}

}
