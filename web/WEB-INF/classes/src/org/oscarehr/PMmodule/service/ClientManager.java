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

import java.util.List;

import org.oscarehr.PMmodule.exception.AlreadyAdmittedException;
import org.oscarehr.PMmodule.exception.AlreadyQueuedException;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.DemographicExt;
import org.oscarehr.PMmodule.web.formbean.ClientListsReportFormBean;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;


public interface ClientManager  {
	
	public Demographic getClientByDemographicNo(String demographicNo);

	public List getClients();
		
	public List search(ClientSearchFormBean criteria, boolean returnOptinsOnly);

	public java.util.Date getMostRecentIntakeADate(String demographicNo);
	
	public java.util.Date getMostRecentIntakeCDate(String demographicNo);
	
	public String getMostRecentIntakeAProvider(String demographicNo);
	public String getMostRecentIntakeCProvider(String demographicNo);

	/* V2.0 */
	public List getReferrals();	
	public List getReferrals(String clientId);
	public List getActiveReferrals(String clientId);
	public ClientReferral getReferralToRemoteAgency(long clientId, long agencyId, long programId);
	public ClientReferral getClientReferral(String id);
	public void saveClientReferral(ClientReferral referral);
	public void processReferral(ClientReferral referral) throws AlreadyAdmittedException, AlreadyQueuedException;
	public void processRemoteReferral(ClientReferral referral);
	public List searchReferrals(ClientReferral referral);
	
	public void saveClient(Demographic client);
	
	public DemographicExt getDemographicExt(String id);
	public List getDemographicExtByDemographicNo(Integer demographicNo);
	public DemographicExt getDemographicExt(Integer demographicNo, String key);
	public void updateDemographicExt(DemographicExt de);
	public void saveDemographicExt(Integer demographicNo, String key, String value);
	public void removeDemographicExt(String id);
	public void removeDemographicExt(Integer demographicNo, String key);
	
	public boolean isOutsideOfDomainEnabled();
	
	public List<Demographic> findByReportCriteria(ClientListsReportFormBean x);
}

