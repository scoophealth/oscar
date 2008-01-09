/*
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
 * TODO Integrator todo list: - clean up use of local agency across application; application should not assume '0' as local agency Id, and should use the string (non-numerical) identifier for an agency. disjunction between local agency Ids and what they
 * are assigned on the server is a source of confusion. - implement unimplemented methods - implement Ext handling - implement program registry - implement notes
 */

import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.integrator.message.AuthenticationToken;
import org.caisi.integrator.message.GetIntegratorInformationRequest;
import org.caisi.integrator.message.GetIntegratorInformationResponse;
import org.caisi.integrator.message.MessageAck;
import org.caisi.integrator.message.agencies.GetAgenciesResponse;
import org.caisi.integrator.message.clientGroup.DemographicKey;
import org.caisi.integrator.message.clientGroup.JoinClientRequest;
import org.caisi.integrator.message.demographics.AddUpdateDemographicRequest;
import org.caisi.integrator.message.demographics.GetDemographicRequest;
import org.caisi.integrator.message.demographics.GetDemographicResponse;
import org.caisi.integrator.message.demographics.SynchronizeAgencyDemographicsRequest;
import org.caisi.integrator.message.program.GetProgramsResponse;
import org.caisi.integrator.message.program.GetReferralsResponse;
import org.caisi.integrator.message.program.MakeReferralRequest;
import org.caisi.integrator.message.program.MakeReferralResponse;
import org.caisi.integrator.message.program.PublishProgramRequest;
import org.caisi.integrator.message.program.PublishProgramResponse;
import org.caisi.integrator.message.program.RemoveReferralRequest;
import org.caisi.integrator.message.program.RemoveReferralResponse;
import org.caisi.integrator.message.search.SearchCandidateDemographicRequest;
import org.caisi.integrator.message.search.SearchCandidateDemographicResponse;
import org.caisi.integrator.model.Client;
import org.caisi.integrator.model.transfer.AgencyTransfer;
import org.caisi.integrator.model.transfer.ClientTransfer;
import org.caisi.integrator.model.transfer.DemographicTransfer;
import org.caisi.integrator.model.transfer.GetReferralResponseTransfer;
import org.caisi.integrator.model.transfer.ProgramTransfer;
import org.caisi.integrator.service.IntegratorService;
import org.codehaus.xfire.annotations.AnnotationServiceFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.oscarehr.PMmodule.dao.AgencyDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.exception.IntegratorException;
import org.oscarehr.PMmodule.exception.OperationNotImplementedException;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.util.TimeClearedHashMap;
import org.springframework.beans.factory.annotation.Required;

public class IntegratorManager {

    private static Log log = LogFactory.getLog(IntegratorManager.class);

    /**
     * This is a simple cache mechanism which removes objects based on time. The expectation is that anyon who puts items into this will identify themselves uniquely via the map key.
     */
    private static TimeClearedHashMap<String, Object> simpleTimeCache = new TimeClearedHashMap<String, Object>(DateUtils.MILLIS_PER_HOUR, DateUtils.MILLIS_PER_HOUR);

    protected AgencyDao agencyDao;
    protected ClientManager clientManager;
    protected ProviderDao providerDao;

    public void setProviderDao(ProviderDao providerDao) {
        this.providerDao = providerDao;
    }

    public Agency getLocalAgency() {
        return(agencyDao.getLocalAgency());
    }

    public boolean isEnabled() {
        return(getLocalAgency().isIntegratorEnabled());
    }

    /**
     * @return an instance of the integrator service or null on error.
     */
    public IntegratorService getIntegratorService() {
        try {
            Service serviceModel = new AnnotationServiceFactory().create(IntegratorService.class);
            IntegratorService integratorService = (IntegratorService) new XFireProxyFactory().create(serviceModel, getLocalAgency().getIntegratorUrl());
            return(integratorService);
        }
        catch (MalformedURLException e) {
            log.error("Error with integrator.", e);
            return(null);
        }
    }

    public AuthenticationToken getAuthenticationToken() {
        return(new AuthenticationToken(getLocalAgency().getIntegratorUsername(), getLocalAgency().getIntegratorPassword()));
    }

    public AgencyTransfer[] getAgencies() {
        try {
            if (!isEnabled()) return(null);

            String CACHE_KEY = "ALL_AGENCIES";

            AgencyTransfer[] results = (AgencyTransfer[]) simpleTimeCache.get(CACHE_KEY);

            if (results == null) {
                GetAgenciesResponse response = getIntegratorService().getAgencies(getAuthenticationToken());

                if (!response.getAck().equals(MessageAck.OK)) {
                    log.error("Error getting agency list. " + response.getAck());
                    return(null);
                }

                results = response.getAgencyTransfers();
                simpleTimeCache.put(CACHE_KEY, results);
            }

            return results;
        }
        catch (Exception e) {
            log.error("Unexpected error.", e);
            return(null);
        }
    }

    /**
     * @param agencyId
     * @return the agency or null upon either error or not found or integrator not enabled.
     */
    public AgencyTransfer getAgencyById(long agencyId) {
        try {
            if (!isEnabled()) return(null);

            String CACHE_KEY = "ALL_AGENCIES_MAPPED_BY_ID";

            @SuppressWarnings("unchecked")
            Map<Long, AgencyTransfer> results = (Map<Long, AgencyTransfer>) simpleTimeCache.get(CACHE_KEY);

            // if no map, then make one
            if (results == null) {
                results = new HashMap<Long, AgencyTransfer>();
                for (AgencyTransfer agency : getAgencies()) {
                    results.put(agency.getId(), agency);
                }
            }

            return(results.get(agencyId));
        }
        catch (Exception e) {
            log.error("Unexpected error.", e);
            return(null);
        }
    }

    public Collection<ClientTransfer> matchClient(Demographic client) throws IntegratorException {
        try {
            if (!isEnabled()) return(null);

            DemographicTransfer searchDemographic = caisiDemographicToIntegratorDemographic(client);
            SearchCandidateDemographicResponse response = getIntegratorService().searchCandidateDemographic(new SearchCandidateDemographicRequest(new Date(), searchDemographic), getAuthenticationToken());

            Collection<ClientTransfer> clients = response.getCandidateClients();

            return clients;
        }
        catch (Exception e) {
            log.error("Unexpected error.", e);
            return(null);
        }
    }

    public Client getClient(String agencyUsername, long demographicNo) throws IntegratorException {
        GetDemographicResponse response = getIntegratorService().getDemographic(new GetDemographicRequest(new Date(), agencyUsername, (int) demographicNo), getAuthenticationToken());

        return response.getClient();
    }

    public Demographic getDemographic(String agencyUsername, long demographicNo) throws IntegratorException {
        try {
            if (!isEnabled()) return(null);

            GetDemographicResponse response = getIntegratorService().getDemographic(new GetDemographicRequest(new Date(), agencyUsername, (int) demographicNo), getAuthenticationToken());

            if (!response.getAck().equals(MessageAck.DEMOGRAPHIC_NOT_FOUND)) return(null);

            if (!response.getAck().equals(MessageAck.OK)) {
                log.error("Error getting demographic information. " + response.getAck());
                return(null);
            }

            Demographic demographic = integratorDemographicToCaisiDemographic(response.getDemographic());

            return demographic;
        }
        catch (Exception e) {
            log.error("Unexpected error.", e);
            return(null);
        }
    }

    public void refreshClients(List<Demographic> clients) throws IntegratorException {
        List<DemographicTransfer> demographics = new ArrayList<DemographicTransfer>();
        for (Demographic client : clients) {
            demographics.add(caisiDemographicToIntegratorDemographic(client));
        }
        getIntegratorService().synchronizeAgencyDemographics(new SynchronizeAgencyDemographicsRequest(new Date(), demographics), getAuthenticationToken());
    }

    protected boolean updateClient(long id) {

        Demographic demographic = clientManager.getClientByDemographicNo("" + id);
        saveClient(demographic);

        return true;
    }

    public void saveClient(Demographic client) throws IntegratorException {
        getIntegratorService().addUpdateDemographic(new AddUpdateDemographicRequest(new Date(), caisiDemographicToIntegratorDemographic(client)), getAuthenticationToken());
    }

    public void saveClient(Demographic client, String remoteAgency, Long remoteDemographicNo) throws IntegratorException {
        AddUpdateDemographicRequest demographicRequest = new AddUpdateDemographicRequest(new Date(), caisiDemographicToIntegratorDemographic(client));
        if (remoteAgency != null && remoteDemographicNo != null) demographicRequest.setSameClientAsDemographic(new DemographicKey(remoteAgency, remoteDemographicNo));
        getIntegratorService().addUpdateDemographic(demographicRequest, getAuthenticationToken());
    }

    public void mergeClient(long demographicNo, String remoteAgency, long remoteDemographicNo) throws IntegratorException {
        JoinClientRequest request = new JoinClientRequest(new Date(), Arrays.asList(new DemographicKey(remoteAgency, remoteDemographicNo), new DemographicKey(getLocalAgency().getIntegratorUsername(), demographicNo)));

        getIntegratorService().joinClient(request, getAuthenticationToken());
    }

    /**
     * @return a String denoting the integrator version or null upon error or integrator not enabled.
     */
    public String getIntegratorVersion() {
        try {
            if (!isEnabled()) return(null);

            String CACHE_KEY = "INTEGRATOR_VERSION";
            String result = (String) simpleTimeCache.get(CACHE_KEY);

            if (result == null) {
                GetIntegratorInformationResponse response = getIntegratorService().getIntegratorInformation(new GetIntegratorInformationRequest(new Date()), getAuthenticationToken());

                if (!response.getAck().equals(MessageAck.OK)) {
                    log.error("Error getting agency information. " + response.getAck());
                    return(null);
                }
                result = response.getVersion();
                simpleTimeCache.put(CACHE_KEY, result);
            }

            return(result);
        }
        catch (Exception e) {
            log.error("Unexpected error.", e);
            return(null);
        }
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

    public DemographicTransfer caisiDemographicToIntegratorDemographic(Demographic demographicInfo) {
        DemographicTransfer demographicTransfer = new DemographicTransfer();

        demographicTransfer.setAddress(demographicInfo.getAddress());
        if (demographicInfo.getDemographicNo() != null) demographicTransfer.setAgencyDemographicNo(demographicInfo.getDemographicNo().longValue());
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

        demographicInfo.setAgencyId("" + demographicTransfer.getAgencyId());
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

    public void refreshAdmissions(List<Admission> admissions) throws IntegratorException {
        throw new OperationNotImplementedException("admission registrations not yet implemented in integrator");

    }

    /**
     * This method will publish the provided program list to the integrator. The program list should not contain caisi standard programs, just user generated ones.
     */
    public void publishPrograms(ProgramTransfer[] programTransfers) {
        try {
            if (!isEnabled()) return;

            PublishProgramRequest request = new PublishProgramRequest(new Date(), programTransfers);
            PublishProgramResponse response = getIntegratorService().publishProgram(request, getAuthenticationToken());

            if (!response.getAck().equals(MessageAck.OK)) {
                log.error("Error publishing programs. " + response.getAck());
            }
        }
        catch (Exception e) {
            log.error("Unexpected error.", e);
        }
    }

    public ProgramTransfer[] getOtherAgenciesPrograms() {
        try {
            if (!isEnabled()) return(null);

            String CACHE_KEY = "OTHER_AGENCIES_PROGRAMS";
            ProgramTransfer[] results = (ProgramTransfer[]) simpleTimeCache.get(CACHE_KEY);

            if (results == null) {
                GetProgramsResponse response = getIntegratorService().getPrograms(getAuthenticationToken());

                if (!response.getAck().equals(MessageAck.OK)) {
                    log.error("Error retrieving programs. " + response.getAck());
                    return(null);
                }

                results = response.getProgramTransfers();
                simpleTimeCache.put(CACHE_KEY, results);
            }

            return(results);
        }
        catch (Exception e) {
            log.error("Unexpected error.", e);
            return(null);
        }
    }

    /**
     * @param agencyId
     * @param remoteProgramId
     * @return the programTransfer with the given agencyId and remoteProgramId or null if non exists or integrator is not enabled.
     */
    public ProgramTransfer getProgramByAgencyAndId(long agencyId, long remoteProgramId) {
        if (!isEnabled()) return(null);

        // meh, I'll just brute force it right now, not like this is called a lot or anything.
        ProgramTransfer[] programTransfers = getOtherAgenciesPrograms();
        if (programTransfers != null) for (ProgramTransfer programTransfer : programTransfers)
            if (programTransfer.getAgencyId() == agencyId && programTransfer.getRemoteProgramId() == remoteProgramId) return(programTransfer);

        return(null);
    }

    @Required
    public void setClientManager(ClientManager mgr) {
        this.clientManager = mgr;
    }

    @Required
    public void setAgencyDao(AgencyDao dao) {
        this.agencyDao = dao;
    }

    /**
     * @param referral
     * @return true if successful false otherwise
     */
    public boolean makeReferral(ClientReferral referral) {
        try {
            if (!isEnabled()) return(false);

            MakeReferralRequest request = new MakeReferralRequest();
            request.setDestinationAgencyId(referral.getAgencyId());
            request.setDestinationRemoteProgramId(referral.getProgramId().intValue());
            request.setPresentingProblem(referral.getPresentProblems());
            request.setReasonForReferral(referral.getNotes());
            request.setSourceDemographicNo(referral.getClientId());

            Provider provider = providerDao.getProvider(String.valueOf(referral.getProviderNo()));
            StringBuilder providerInfo = new StringBuilder();
            providerInfo.append(provider.getFullName());
            providerInfo.append(" (");
            providerInfo.append(provider.getProviderType());
            providerInfo.append(") ");
            providerInfo.append(provider.getWorkPhone());
            request.setSourceProviderInfo(providerInfo.toString());

            MakeReferralResponse response = getIntegratorService().makeReferral(request, getAuthenticationToken());

            if (!response.getAck().equals(MessageAck.OK)) {
                log.error("Error making referral. " + response.getAck());
                return(false);
            }

            return(true);
        }
        catch (Exception e) {
            log.error("Unexpected error.", e);
        }

        return(false);
    }

    /**
     */
    public GetReferralResponseTransfer[] getRemoteReferrals() {
        try {
            if (!isEnabled()) return(null);

            GetReferralsResponse response = getIntegratorService().getReferralsByAgency(getAuthenticationToken());

            if (!response.getAck().equals(MessageAck.OK)) {
                log.error("Error getting referrals. " + response.getAck());
                return(null);
            }

            return(response.getReferrals());
        }
        catch (Exception e) {
            log.error("Unexpected error.", e);
        }

        return(null);
    }
    
    public boolean removeReferral(int referralId) {
        try {
            if (!isEnabled()) return(false);

            RemoveReferralRequest request = new RemoveReferralRequest();
            request.setReferralId(referralId);

            RemoveReferralResponse response = getIntegratorService().removeReferral(request, getAuthenticationToken());

            if (!response.getAck().equals(MessageAck.OK)) {
                log.error("Error making referral. " + response.getAck());
                return(false);
            }

            return(true);
        }
        catch (Exception e) {
            log.error("Unexpected error.", e);
        }

        return(false);
    }

}
