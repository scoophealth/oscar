/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.PMmodule.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.AgencyDao;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.service.AgencyManager;

public class AgencyManager {
	
	private static Log log = LogFactory.getLog(AgencyManager.class);

	private AgencyDao dao;

	public void setAgencyDao(AgencyDao dao) {
		this.dao = dao;
	}

	public Agency getAgency(String agencyId) {
		return dao.getAgency(Long.valueOf(agencyId));
	}

	public Agency getLocalAgency() {
		Agency agency = dao.getLocalAgency();

		if (agency == null) {
			log.warn("No local agency has been saved.");
			return new Agency(new Long(0));
		}
		
		return agency;
	}

	public void saveLocalAgency(Agency agency) {
		log.debug("Saving agency information");

		agency.setLocal(true);
		dao.saveAgency(agency);
	}

	public void saveAgency(Agency agency) {
		dao.saveAgency(agency);
	}
}