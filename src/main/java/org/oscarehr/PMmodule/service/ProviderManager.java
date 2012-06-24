/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.service;

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.PMmodule.dao.AgencyDao;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.dao.SecUserRoleDao;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.Provider;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ProviderManager
{
	private ProviderDao providerDao;
	private AgencyDao agencyDao;
	private ProgramProviderDAO programProviderDAO;
	private SecUserRoleDao secUserRoleDao; 
	
	
	public void setProviderDao(ProviderDao providerDao)	{
		this.providerDao = providerDao;
	}
	
	public void setAgencyDao(AgencyDao agencyDao) {
		this.agencyDao = agencyDao;
	}
	
	public void setProgramProviderDAO(ProgramProviderDAO dao) {
		this.programProviderDAO = dao;
	}
	
	public void setSecUserRoleDao(SecUserRoleDao secUserRoleDao) {
		this.secUserRoleDao = secUserRoleDao;
	}
	
	public Provider getProvider(String providerNo)
	{
		return providerDao.getProvider(providerNo);
	}
	
	public String getProviderName(String providerNo)
	{
		return providerDao.getProviderName(providerNo);
	}
	
	public List<Provider> getProviders()
	{
		return providerDao.getProviders();
	}
	
	public List<Provider> getActiveProviders()
	{
		return providerDao.getActiveProviders();
	}

    public List<Provider> getActiveProviders(String facilityId, String programId) {
		return providerDao.getActiveProviders(facilityId, programId);
    }
    /* get my collegues */
    public List<Provider> getActiveProviders(String providerNo, Integer shelterId) {
		return providerDao.getActiveProviders(providerNo, shelterId);
    }
	
	public List<Provider> search(String name) {
		return providerDao.search(name);
	}

    public List<ProgramProvider> getProgramDomain(String providerNo) {
		return programProviderDAO.getProgramDomain(providerNo);
	}

    public List<ProgramProvider> getProgramDomainByFacility(String providerNo, Integer facilityId) {
		return programProviderDAO.getProgramDomainByFacility(providerNo, facilityId);
	}

    public List<Facility> getFacilitiesInProgramDomain(String providerNo) {
        return programProviderDAO.getFacilitiesInProgramDomain(providerNo);
    }
    
	public List getShelterIds(String provider_no)
	{
		return providerDao.getShelterIds(provider_no);
	}

    public List<Agency> getAgencyDomain(String providerNo) {
		Agency localAgency =  agencyDao.getLocalAgency();
		List<Agency> agencies = new ArrayList<Agency>();
		agencies.add(localAgency);
		return agencies;
	}
	
	public List<Provider> getProvidersByType(String type) {
		return providerDao.getProvidersByType(type);
	}
	
	public List<SecUserRole> getSecUserRoles(String providerNo) {
		return secUserRoleDao.getUserRoles(providerNo);
	}

	public void saveUserRole(SecUserRole sur) {
		secUserRoleDao.save(sur);
	}
	
}
