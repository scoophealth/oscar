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

import org.oscarehr.PMmodule.exception.IntegratorException;
import org.oscarehr.PMmodule.exception.IntegratorNotEnabledException;
import org.oscarehr.PMmodule.model.*;

import java.util.List;

public interface IntegratorManager {
	
	public static final short DATATYPE_CLIENT = 	1;
	public static final short DATATYPE_PROVIDER = 	2;

    public boolean isActive();
	public boolean isEnabled();
	public boolean isRegistered();

	public void refresh();

    /* agency */
    public long getLocalAgencyId();

    public String register(Agency agencyInfo, String key);
	public List getAgencies();
	public Agency getAgency(String id);

    /* program */
	public void updateProgramData(List<Program> programs);
	public List searchPrograms(Program criteria);
	public Program getProgram(Long agencyId, Long programId) throws IntegratorNotEnabledException;
	
	/* asynchronous messaging for referrals */
	public void sendReferral(Long agencyId, ClientReferral referral);
	
	/* client linking */
	public Demographic getDemographic(long agencyId, long demographicNo) throws IntegratorException;
	public Demographic[] matchClient(Demographic client) throws IntegratorException;
	public void saveClient(Demographic client) throws IntegratorException;
	public void mergeClient(Demographic localClient, long remoteAgency, long remoteClientId) throws IntegratorException;
	public long getLocalClientId(long agencyId, long demographicNo) throws IntegratorException;
	
	/* refreshing */
	public void refreshPrograms(List<Program> programs) throws IntegratorException;
	public void refreshAdmissions(List<Admission> admissions) throws IntegratorException;
	public void refreshProviders(List<Provider> providers) throws IntegratorException;
	public void refreshReferrals(List<ClientReferral> referrals) throws IntegratorException;
	public void refreshClients(List<Demographic> clients) throws IntegratorException;
	
	public List getCurrentAdmissions(long clientId) throws IntegratorException;
	public List getCurrentReferrals(long clientId) throws IntegratorException;
	
	public boolean notifyUpdate(short dataType, String id);
	public Demographic getClient(String demographicNo);
	
	public String getIntegratorVersion();
}
