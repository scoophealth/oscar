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

/**
 * TODO Integrator todo list: - clean up use of local agency across application; application should not assume '0' as local agency Id, and should use the string
 * (non-numerical) identifier for an agency. disjunction between local agency Ids and what they are assigned on the server is a source of confusion.
 *  - implement unimplemented methods
 *  - implement Ext handling
 *  - implement program registry
 *  - implement notes
 */

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.integrator.message.AuthenticationToken;
import org.caisi.integrator.message.GetIntegratorInformationRequest;
import org.caisi.integrator.message.agencies.FindAgenciesRequest;
import org.caisi.integrator.message.agencies.FindAgenciesResponse;
import org.caisi.integrator.message.agencies.RegisterAgencyRequest;
import org.caisi.integrator.message.agencies.RegisterAgencyResponse;
import org.caisi.integrator.message.clientGroup.JoinClientRequest;
import org.caisi.integrator.message.clientGroup.JoinClientResponse;
import org.caisi.integrator.message.demographics.AddUpdateDemographicRequest;
import org.caisi.integrator.message.demographics.GetDemographicRequest;
import org.caisi.integrator.message.demographics.GetDemographicResponse;
import org.caisi.integrator.message.demographics.SynchronizeAgencyDemographicsRequest;
import org.caisi.integrator.message.demographics.SynchronizeAgencyDemographicsResponse;
import org.caisi.integrator.message.search.SearchCandidateDemographicRequest;
import org.caisi.integrator.message.search.SearchCandidateDemographicResponse;
import org.caisi.integrator.model.Client;
import org.caisi.integrator.model.transfer.AgencyTransfer;
import org.caisi.integrator.model.transfer.DemographicTransfer;
import org.caisi.integrator.service.IntegratorService;
import org.codehaus.xfire.annotations.AnnotationServiceFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.oscarehr.PMmodule.dao.AgencyDao;
import org.oscarehr.PMmodule.exception.IntegratorException;
import org.oscarehr.PMmodule.exception.IntegratorNotEnabledException;
import org.oscarehr.PMmodule.exception.IntegratorNotInitializedException;
import org.oscarehr.PMmodule.exception.OperationNotImplementedException;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.Provider;
import org.springframework.beans.factory.annotation.Required;

public class IntegratorManager {

    public static final short DATATYPE_CLIENT = 1;
    public static final short DATATYPE_PROVIDER = 2;

    private static Log log = LogFactory.getLog(IntegratorManager.class);

    private static Agency localAgency;
    private static Map<Long, Agency> agencyMap;

    private boolean initCalled = false;

    // injected
    protected AgencyDao agencyDAO;
    protected ClientManager clientManager;

    // set up during initialization
    private IntegratorService integratorService;
    private AuthenticationToken authenticationToken;

    public static Agency getLocalAgency() {
        return localAgency;
    }

    public static Map<Long, Agency> getAgencyMap() {
        return agencyMap;
    }

    public boolean isActive() {
        return isEnabled();
    }

    public boolean isEnabled() {
        if (!initCalled) {
            init();
        }

        return localAgency != null && localAgency.isIntegratorEnabled();
    }

    public void init() {
        if (initCalled) {
            return;
        }

        initCalled = true;

        try {
            localAgency = agencyDAO.getLocalAgency();
        }
        catch (Throwable e) {
            log.error("could not get local agency", e);
            return;
        }

        if (localAgency == null) {
            log.warn("local agency information not in database");
            return;
        }

        if (localAgency.isIntegratorEnabled()) {
            try {
                // prepare the service;
                Service serviceModel = new AnnotationServiceFactory().create(IntegratorService.class);
                integratorService = (IntegratorService) new XFireProxyFactory().create(serviceModel, localAgency.getIntegratorUrl());

                // set up the auth token
                authenticationToken = new AuthenticationToken(localAgency.getIntegratorUsername(), localAgency.getIntegratorPassword());

                // set up the map of other agencies
                setupAgencyMap();

                localAgency.setIntegratorEnabled(true);
            }
            catch (Throwable e) {
                localAgency.setIntegratorEnabled(false);

                throw new IntegratorNotInitializedException(e);
            }
            finally {
                if (agencyMap == null && localAgency != null) {
                    setupLocalAgencyMap();
                }
            }
        }
        else {
            if (agencyMap == null && localAgency != null) {
                setupLocalAgencyMap();
            }
        }
    }

    /**
     * Build the map of agencies available via the integrator.
     */
    protected void setupAgencyMap() {
        if (integratorService != null) {
            FindAgenciesResponse response = getIntegratorService().findAgencies(new FindAgenciesRequest(new Date()), authenticationToken);
            agencyMap = new HashMap<Long, Agency>();
            for (org.caisi.integrator.model.Agency agency : response.getAgencies()) {
                // skip the local agency... we already have the information we need about ourselves
                if (!agency.getName().equals(localAgency.getIntegratorUsername())) agencyMap.put(agency.getId(), integratorAgencyToCAISIAgency(agency));
            }
        }
        agencyMap.put((long) 0, localAgency);
        Agency.setAgencyMap(agencyMap);
        log.info("Agency Map is setup");
    }

    /**
     * Build a map of agencies available, but populate it only with the local agency
     */
    protected void setupLocalAgencyMap() {
        agencyMap = new HashMap<Long, Agency>();
        agencyMap.put((long) 0, localAgency);
        Agency.setAgencyMap(agencyMap);
        log.info("Agency Map is setup");
    }

    protected IntegratorService getIntegratorService() {
        if (!isEnabled()) throw new IntegratorNotEnabledException("request made for integrator service, but integrator is not enabled");
        else if (integratorService == null) throw new IntegratorNotInitializedException(
                "request made for integrator service, which did not complete initialization");
        else return integratorService;
    }

    /**
     * @return a list of all agencies
     */
    public List<Agency> getAgencies() {
        FindAgenciesResponse response = getIntegratorService().findAgencies(new FindAgenciesRequest(new Date()), authenticationToken);
        List<Agency> agencies = new ArrayList<Agency>();
        for (org.caisi.integrator.model.Agency agency : response.getAgencies()) {
            agencies.add(integratorAgencyToCAISIAgency(agency));
        }

        return agencies;
    }

    public void refresh() {
        init();
    }

    /**
     * @return the id of the local agency
     */
    public long getLocalAgencyId() {
        return localAgency.getId();
    }

    /**
     * Register an agency (presumably the local agency) with the integrator
     * 
     * @param agencyInfo
     *            the agency to register
     * @return the agency's id
     */
    public Long register(Agency agencyInfo) {
        RegisterAgencyResponse response = getIntegratorService().registerAgency(new RegisterAgencyRequest(new Date(), caisiAgencyToIntegratorAgency(agencyInfo)

        ), authenticationToken);

        return response.getAgency().getId();
    }

    public Collection<Client> matchClient(Demographic client) throws IntegratorException {

        org.caisi.integrator.model.Demographic searchDemographic = caisiDemographicToIntegratorDemographic(client);
        SearchCandidateDemographicResponse response = getIntegratorService().searchCandidateDemographic(
                new SearchCandidateDemographicRequest(new Date(), searchDemographic), authenticationToken);

        Collection<Client> clients = response.getCandidateClients();

        return clients;
    }

    public Demographic getDemographic(String agencyUsername, long demographicNo) throws IntegratorException {
        GetDemographicResponse response = getIntegratorService().getDemographic(new GetDemographicRequest(new Date(), agencyUsername, (int) demographicNo),
                authenticationToken);

        Demographic demographic = integratorDemographicToCaisiDemographic(response.getDemographic());

        return demographic;
    }

    // TODO is this used? and why this name?
    public Demographic getClient(long demographicNo) {
        GetDemographicResponse response = getIntegratorService().getDemographic(
                new GetDemographicRequest(new Date(), localAgency.getIntegratorUsername(), (int) demographicNo), authenticationToken);

        Demographic demographic = integratorDemographicToCaisiDemographic(response.getDemographic());

        return demographic;
    }

    public void refreshClients(List<Demographic> clients) throws IntegratorException {
        List<org.caisi.integrator.model.Demographic> demographics = new ArrayList<org.caisi.integrator.model.Demographic>();
        for (Demographic client : clients) {
            demographics.add(caisiDemographicToIntegratorDemographic(client));
        }
        SynchronizeAgencyDemographicsResponse response = getIntegratorService().synchronizeAgencyDemographics(
                new SynchronizeAgencyDemographicsRequest(new Date(), demographics), authenticationToken);
    }

    protected boolean updateClient(long id) {
        // TODO stubbed for now, must implement
        // if(!isEnabled() || agencyService == null) {
        // return false;
        // }
        //
        // Demographic demographic = clientManager.getClientByDemographicNo(id);
        // List<DemographicExt> extras = clientManager.getDemographicExtByDemographicNo((int)id);
        //
        // demographic.setExtras(extras.toArray(new DemographicExt[extras.size()]));
        //
        // agencyService.updateClient(getLocalAgency().getId().longValue(),demographic.getDemographicNo().intValue(),demographic);

        return true;
    }

    public void saveClient(Demographic client) throws IntegratorException {
        getIntegratorService().addUpdateDemographic(new AddUpdateDemographicRequest(new Date(), caisiDemographicToIntegratorDemographic(client)),
                authenticationToken);
    }

    public void mergeClient(long demographicNo, String remoteAgency, long remoteDemographicNo) throws IntegratorException {
        JoinClientRequest request = new JoinClientRequest(new Date(), Arrays.asList(new JoinClientRequest.DemographicKey(remoteAgency, remoteDemographicNo),
                new JoinClientRequest.DemographicKey(localAgency.getIntegratorUsername(), demographicNo)));

        JoinClientResponse response = integratorService.joinClient(request, authenticationToken);
    }

    public String getIntegratorVersion() {
        return getIntegratorService().getIntegratorInformation(new GetIntegratorInformationRequest(new Date()), authenticationToken).getVersion();
    }

    public boolean notifyUpdate(short dataType, long id) {
        if (!isEnabled()) {
            return false;
        }

        switch (dataType) {
            case IntegratorManager.DATATYPE_CLIENT:
                return updateClient(id);
        }

        return true;
    }

    private org.caisi.integrator.model.Agency caisiAgencyToIntegratorAgency(Agency agencyInfo) {
        AgencyTransfer agencyTransfer = new AgencyTransfer();

        agencyTransfer.setHic(agencyInfo.isHic());
        agencyTransfer.setLocal(agencyInfo.isLocal());
        agencyTransfer.setName(agencyInfo.getName());
        agencyTransfer.setUsername(agencyInfo.getIntegratorUsername());
        agencyTransfer.setDescription(agencyInfo.getDescription());
        agencyTransfer.setContactPhone(agencyInfo.getContactPhone());
        agencyTransfer.setContactName(agencyInfo.getContactName());
        agencyTransfer.setContactEmail(agencyInfo.getContactEmail());

        return agencyTransfer;
    }

    private Agency integratorAgencyToCAISIAgency(org.caisi.integrator.model.Agency agencyTransfer) {
        Agency agency = new Agency();

        agency.setLocal(agencyTransfer.isLocal());
        agency.setName(agencyTransfer.getName());
        agency.setIntegratorUsername(agencyTransfer.getUsername());
        agency.setDescription(agencyTransfer.getDescription());
        agency.setContactPhone(agencyTransfer.getContactPhone());
        agency.setContactName(agencyTransfer.getContactName());
        agency.setContactEmail(agencyTransfer.getContactEmail());

        return agency;
    }

    public org.caisi.integrator.model.Demographic caisiDemographicToIntegratorDemographic(Demographic demographicInfo) {
        DemographicTransfer demographicTransfer = new DemographicTransfer();

        demographicTransfer.setAddress(demographicInfo.getAddress());
        if (demographicInfo.getDemographicNo() != null) demographicTransfer.setAgencyDemographicNo(demographicInfo.getDemographicNo());
        demographicTransfer.setChartNo(demographicInfo.getChartNo());
        demographicTransfer.setCity(demographicInfo.getCity());
        demographicTransfer.setDateJoined(demographicInfo.getDateJoined());
        demographicTransfer.setDateOfBirth(strToIntegerOrNull(demographicInfo.getDateOfBirth()));
        demographicTransfer.setEffDate(demographicInfo.getEffDate());
        demographicTransfer.setEmail(demographicInfo.getEmail());
        demographicTransfer.setEndDate(demographicInfo.getEndDate());
        demographicTransfer.setFamilyDoctor(demographicInfo.getFamilyDoctor());
        demographicTransfer.setFirstName(demographicInfo.getFirstName());
        demographicTransfer.setHcRenewDate(demographicInfo.getHcRenewDate());
        demographicTransfer.setHcType(demographicInfo.getHcType());
        demographicTransfer.setHin(demographicInfo.getHin());
        demographicTransfer.setLastName(demographicInfo.getLastName());
        demographicTransfer.setMonthOfBirth(strToIntegerOrNull(demographicInfo.getMonthOfBirth()));
        demographicTransfer.setPatientStatus(demographicInfo.getPatientStatus());
        demographicTransfer.setPcnIndicator(demographicInfo.getPcnIndicator());
        demographicTransfer.setPhone(demographicInfo.getPhone());
        demographicTransfer.setPhone2(demographicInfo.getPhone2());
        demographicTransfer.setPin(demographicInfo.getPin());
        demographicTransfer.setPostal(demographicInfo.getPostal());
        // TODO provider conversion here
        demographicTransfer.setProvince(demographicInfo.getProvince());
        demographicTransfer.setRosterStatus(demographicInfo.getRosterStatus());
        demographicTransfer.setSex(demographicInfo.getSex());
        demographicTransfer.setVer(demographicInfo.getVer());
        demographicTransfer.setYearOfBirth(strToIntegerOrNull(demographicInfo.getYearOfBirth()));

        return demographicTransfer;
    }

    private String padOrNull(Integer n, int zeroes) {
        if (n == null) return null;
        else {
            StringBuffer zeroesBuff = new StringBuffer();
            while (zeroes-- != 0)
                zeroesBuff.append('0');
            DecimalFormat df = new DecimalFormat(zeroesBuff.toString());
            return df.format(n);
        }
    }

    public Demographic integratorDemographicToCaisiDemographic(org.caisi.integrator.model.Demographic demographicTransfer) {
        Demographic demographicInfo = new Demographic();

        demographicInfo.setAgencyId(demographicTransfer.getAgency().getUsername());
        demographicInfo.setAddress(demographicTransfer.getAddress());
        demographicInfo.setChartNo(demographicTransfer.getChartNo());
        demographicInfo.setCity(demographicTransfer.getCity());
        demographicInfo.setDateJoined(demographicTransfer.getDateJoined());
        demographicInfo.setDateOfBirth(padOrNull(demographicTransfer.getDateOfBirth(), 2));
        demographicInfo.setEffDate(demographicTransfer.getEffDate());
        demographicInfo.setEmail(demographicTransfer.getEmail());
        demographicInfo.setEndDate(demographicTransfer.getEndDate());
        demographicInfo.setFamilyDoctor(demographicTransfer.getFamilyDoctor());
        demographicInfo.setFirstName(demographicTransfer.getFirstName());
        demographicInfo.setHcRenewDate(demographicTransfer.getHcRenewDate());
        demographicInfo.setHcType(demographicTransfer.getHcType());
        demographicInfo.setHin(demographicTransfer.getHin());
        demographicInfo.setLastName(demographicTransfer.getLastName());
        demographicInfo.setMonthOfBirth(padOrNull(demographicTransfer.getMonthOfBirth(), 2));
        demographicInfo.setPatientStatus(demographicTransfer.getPatientStatus());
        demographicInfo.setPcnIndicator(demographicTransfer.getPcnIndicator());
        demographicInfo.setPhone(demographicTransfer.getPhone());
        demographicInfo.setPhone2(demographicTransfer.getPhone2());
        demographicInfo.setPin(demographicTransfer.getPin());
        demographicInfo.setPostal(demographicTransfer.getPostal());
        // TODO provider conversion here
        demographicInfo.setProvince(demographicTransfer.getProvince());
        demographicInfo.setRosterStatus(demographicTransfer.getRosterStatus());
        demographicInfo.setSex(demographicTransfer.getSex());
        demographicInfo.setVer(demographicTransfer.getVer());
        demographicInfo.setYearOfBirth(padOrNull(demographicTransfer.getYearOfBirth(), 4));

        return demographicInfo;
    }

    public Integer strToIntegerOrNull(String value) {
        try {
            return Integer.valueOf(value);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    public void refreshPrograms(List<Program> programs) throws IntegratorException {
        throw new OperationNotImplementedException("program registrations not yet implemented in integrator");
    }

    public void refreshAdmissions(List<Admission> admissions) throws IntegratorException {
        throw new OperationNotImplementedException("admission registrations not yet implemented in integrator");

    }

    public void refreshProviders(List<Provider> providers) throws IntegratorException {
        throw new OperationNotImplementedException("provider registrations not yet implemented in integrator");
    }

    public void refreshReferrals(List<ClientReferral> referrals) throws IntegratorException {
        throw new OperationNotImplementedException("referral registrations not yet implemented in integrator");
    }

    public void updateProgramData(List<Program> programs) {
        throw new OperationNotImplementedException("program registry not yet implemented");
    }

    public List<Program> searchPrograms(Program criteria) {
        throw new OperationNotImplementedException("program registry not yet implemented");
    }

    public Program getProgram(Long agencyId, Long programId) throws IntegratorNotEnabledException {
        throw new OperationNotImplementedException("program registry not yet implemented");
    }

    public void sendReferral(Long agencyId, ClientReferral referral) {
        throw new OperationNotImplementedException("referral registrations not yet implemented in integrator");
    }

    public List getCurrentAdmissions(long clientId) throws IntegratorException {
        throw new OperationNotImplementedException("admission registrations not yet implemented in integrator");
    }

    public List getCurrentReferrals(long clientId) throws IntegratorException {
        throw new OperationNotImplementedException("referral registrations not yet implemented in integrator");
    }

    @Required
    public void setClientManager(ClientManager mgr) {
        this.clientManager = mgr;
    }

    @Required
    public void setAgencyDao(AgencyDao dao) {
        this.agencyDAO = dao;
    }

}
