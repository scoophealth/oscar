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

package org.oscarehr.PMmodule.dao;

import java.util.Date;
import java.util.List;

import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.DemographicExt;
import org.oscarehr.PMmodule.web.formbean.ClientListsReportFormBean;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;

public interface ClientDao {
	
	/**
	 * Does client with given id exist
	 * 
	 * @param demographicNo id
	 * @return true if client exists
	 */
	public boolean clientExists(Integer demographicNo);
	
	public Demographic getClientByDemographicNo(Integer demographicNo);

	public List getClients();

	public List search(ClientSearchFormBean criteria, boolean returnOptinsOnly);

	public Date getMostRecentIntakeADate(Integer demographicNo);

	public Date getMostRecentIntakeCDate(Integer demographicNo);

	public String getMostRecentIntakeAProvider(Integer demographicNo);

	public String getMostRecentIntakeCProvider(Integer demographicNo);

	public void saveClient(Demographic client);

	public DemographicExt getDemographicExt(Integer id);

	public List getDemographicExtByDemographicNo(Integer demographicNo);

	public DemographicExt getDemographicExt(Integer demographicNo, String key);

	public void updateDemographicExt(DemographicExt de);

	public void saveDemographicExt(Integer demographicNo, String key, String value);

	public void removeDemographicExt(Integer id);

	public void removeDemographicExt(Integer demographicNo, String key);

	public List getProgramIdByDemoNo(String demoNo);

	public List<Demographic> findByReportCriteria(ClientListsReportFormBean x);
}
