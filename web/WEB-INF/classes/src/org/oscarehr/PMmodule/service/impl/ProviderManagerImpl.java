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

package org.oscarehr.PMmodule.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.PMmodule.dao.AgencyDao;
import org.oscarehr.PMmodule.dao.OscarSecurityDAO;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.PMmodule.service.ProviderManager;


public class ProviderManagerImpl implements ProviderManager
{
//	private static Log log = LogFactory.getLog(ProviderManagerImpl.class);
	private ProviderDao dao;
	private AgencyDao agencyDAO;
	private ProgramProviderDAO programProviderDAO;
	private OscarSecurityDAO oscarSecurityDAO; 
	
	
	public void setProviderDao(ProviderDao dao)	{
		this.dao = dao;
	}
	
	public void setAgencyDAO(AgencyDao dao) {
		this.agencyDAO = dao;
	}
	
	public void setProgramProviderDAO(ProgramProviderDAO dao) {
		this.programProviderDAO = dao;
	}
	
	public void setOscarSecurityDAO(OscarSecurityDAO oscarSecurityDAO) {
		this.oscarSecurityDAO = oscarSecurityDAO;
	}
	
	public Provider getProvider(String providerNo)
	{
		return dao.getProvider(providerNo);
	}
	
	public String getProviderName(String providerNo)
	{
		return dao.getProviderName(providerNo);
	}
	
	public List<Provider> getProviders()
	{
		return dao.getProviders();
	}
	
	public List<Provider> search(String name) {
		return dao.search(name);
	}
	
	public List getProgramDomain(String providerNo) {
		return programProviderDAO.getProgramDomain(Long.valueOf(providerNo));
	}
	
	public List<Agency> getAgencyDomain(String providerNo) {
		Agency localAgency =  agencyDAO.getLocalAgency();
		List<Agency> agencies = new ArrayList<Agency>();
		agencies.add(localAgency);
		return agencies;
	}
	
	public List<Provider> getProvidersByType(String type) {
		return dao.getProvidersByType(type);
	}
	
	public List<SecUserRole> getSecUserRoles(String providerNo) {
		return oscarSecurityDAO.getUserRoles(providerNo);
	}

	
}
