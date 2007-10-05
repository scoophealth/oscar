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

import java.util.Collection;
import java.util.List;

public interface IntegratorManager {

	public static final short DATATYPE_CLIENT = 	1;
	public static final short DATATYPE_PROVIDER = 	2;

    public boolean isActive();
	public boolean isEnabled();
	public boolean isRegistered();

	public void refresh();

    /**
     * @return the id of the local agency
     */
    public long getLocalAgencyId();

    /**
     * Register an agency (presumably the local agency) with the integrator
     *
     * @param agencyInfo the agency to register
     * @return the agency's id
     */
    public Long register(Agency agencyInfo);

    /**
     * @return a list of all agencies
     */
    public List getAgencies();

	/**
     * Synchronize the program registry
     *
     * @param programs of type List<Program>
     */
    public void updateProgramData(List<Program> programs);
	/**
     * Search for a program in the program registry
     *
     * @param criteria a program object filled out with the critera to search with
     * @return programs which satisfy the critera
     */
    public List<Program> searchPrograms(Program criteria);

    /**
     * Get information on a specific program
     *
     * @param agencyId the id of the agency the program belongs to
     * @param programId the id of the program
     * @return the program if found, null otherwise
     * @throws IntegratorNotEnabledException when
     */
    public Program getProgram(Long agencyId, Long programId) throws IntegratorNotEnabledException;

	/* asynchronous messaging for referrals */
	public void sendReferral(Long agencyId, ClientReferral referral);

	/* client linking */
	public Demographic getDemographic(long agencyId, long demographicNo) throws IntegratorException;
	public Collection<Demographic> matchClient(Demographic client) throws IntegratorException;
	public void saveClient(Demographic client) throws IntegratorException;
	public void mergeClient(Demographic localClient, long remoteAgency, long remoteClientId) throws IntegratorException;

	/* refreshing */
	public void refreshPrograms(List<Program> programs) throws IntegratorException;
	public void refreshAdmissions(List<Admission> admissions) throws IntegratorException;
	public void refreshProviders(List<Provider> providers) throws IntegratorException;
	public void refreshReferrals(List<ClientReferral> referrals) throws IntegratorException;
	public void refreshClients(List<Demographic> clients) throws IntegratorException;

	public List getCurrentAdmissions(long clientId) throws IntegratorException;
	public List getCurrentReferrals(long clientId) throws IntegratorException;

	public boolean notifyUpdate(short dataType, long id);
	public Demographic getClient(long demographicNo);

	public String getIntegratorVersion();
}
