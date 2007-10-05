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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.xfire.XFire;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.client.XFireProxy;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import org.codehaus.xfire.transport.http.SoapHttpTransport;
import org.oscarehr.PMmodule.dao.AgencyDao;
import org.oscarehr.PMmodule.exception.IntegratorException;
import org.oscarehr.PMmodule.exception.IntegratorNotEnabledException;
import org.oscarehr.PMmodule.integrator.message.OutgoingReferralBean;
import org.oscarehr.PMmodule.integrator.xfire.OutgoingAuthenticationHandler;
import org.oscarehr.PMmodule.model.*;
import org.oscarehr.PMmodule.service.IntegratorManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.integrator.ws.AgencyService;
import org.oscarehr.integrator.ws.ProgramService;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;

import java.lang.reflect.Proxy;
import java.util.*;

public class IntegratorManagerImpl implements IntegratorManager {

    private static Log log = LogFactory.getLog(IntegratorManagerImpl.class);

    private static Agency localAgency;
    private static Map<Long, Agency> agencyMap;

    private boolean initCalled = false;

    private AgencyDao agencyDAO;
    private JmsTemplate jmsTemplate;
    private OutgoingReferralBean outgoingReferralBean;/* services provided by integrator */
    protected AgencyService agencyService= null;
    protected ProgramService programService = null;/* data managers */
    protected ClientManager clientManager;

    public static Agency getLocalAgency() {
        return localAgency;
    }

    public static Map<Long, Agency> getAgencyMap() {
        return agencyMap;
    }

    public void setAgencyDao(AgencyDao dao) {
        this.agencyDAO = dao;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setOutgoingReferralBean(OutgoingReferralBean bean) {
        this.outgoingReferralBean = bean;
    }

    public boolean isActive() {
        return isEnabled() && isRegistered();
    }

    public boolean isEnabled() {
        if (!initCalled) {
            init();
        }

        return localAgency != null && localAgency.isIntegratorEnabled();
    }

    public boolean isRegistered() {
        if (!isEnabled()) {
            log.warn("integrator not enabled");
            return false;
        }

        return localAgency != null && localAgency.getId() > 0;

    }

    public void init() {
        if (initCalled) {
            return;
        }

        initCalled = true;

        try {
            localAgency = agencyDAO.getLocalAgency();
        } catch (Throwable e) {
            log.error("could not get local agency", e);
            return;
        }

        if (localAgency == null) {
            log.warn("local agency information not in database");
            return;
        }

        if (isActive()) {
            try {
                XFire xfire = XFireFactory.newInstance().getXFire();
                XFireProxyFactory factory = new XFireProxyFactory(xfire);
                Service agencyServiceModel = new ObjectServiceFactory().create(AgencyService.class);
                Service programServiceModel = new ObjectServiceFactory().create(ProgramService.class);

                agencyService = (AgencyService) factory.create(agencyServiceModel, localAgency.getIntegratorUrl() + "/service/AgencyService");

                XFireProxy proxy = (XFireProxy) Proxy.getInvocationHandler(agencyService);
                proxy.getClient().addOutHandler(new OutgoingAuthenticationHandler(localAgency.getIntegratorUsername(), localAgency.getIntegratorPassword()));
                proxy.getClient().setTransport(new SoapHttpTransport());

                programService = (ProgramService) factory.create(programServiceModel, localAgency.getIntegratorUrl() + "/service/ProgramService");

                proxy = (XFireProxy) Proxy.getInvocationHandler(programService);
                proxy.getClient().addOutHandler(new OutgoingAuthenticationHandler(localAgency.getIntegratorUsername(), localAgency.getIntegratorPassword()));
                proxy.getClient().setTransport(new SoapHttpTransport());

                setupAgencyMap();
            } catch (XFireRuntimeException e) {
                log.error(e);
                localAgency.setIntegratorEnabled(false);
            } catch (Throwable e) {
                log.error(e);
                localAgency.setIntegratorEnabled(false);
            } finally {
                if (agencyMap == null && localAgency != null) {
                    setupLocalAgencyMap();
                }
            }
        } else {
            if (agencyMap == null && localAgency != null) {
                setupLocalAgencyMap();
            }
        }
    }

    public void setupAgencyMap() {
        if (agencyService != null) {
            Agency[] agencies = agencyService.getAgencies();
            agencyMap = new HashMap<Long, Agency>();
            for (Agency agency : agencies) {
                agencyMap.put(agency.getId(), agency);
            }
        }
        agencyMap.put((long) 0, localAgency);
        Agency.setAgencyMap(agencyMap);
        log.info("Agency Map is setup");
    }

    public void setupLocalAgencyMap() {
        agencyMap = new HashMap<Long, Agency>();
        agencyMap.put((long) 0, localAgency);
        Agency.setAgencyMap(agencyMap);
        log.info("Agency Map is setup");
    }

    public void refresh() {
        init();
    }

    public long getLocalAgencyId() {
        return localAgency.getId();
    }

    public String register(Agency agencyInfo, String key) {
        if (!isEnabled()) {
            return null;
        }

        if (agencyService == null) {
            log.warn("integrator not enabled, or not inited");
            return null;
        }
        return agencyService.register(agencyInfo, key);
    }

    public List getAgencies() {
        if (!isEnabled() || agencyService == null) {
            log.warn("integrator not enabled, or not inited");
            return null;
        }
        try {
            Agency[] agencies = agencyService.getAgencies();
            List<Agency> result = new ArrayList<Agency>();
            result.addAll(Arrays.asList(agencies));
            return result;
        } catch (XFireRuntimeException e) {
            log.error(e);
            return null;
        }
    }

    public Agency getAgency(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    public void updateProgramData(List<Program> programs) {
        if (!isEnabled() || programService == null) {
            log.warn("integrator not enabled, or not inited");
            return;
        }
        Program programArray[] = programs.toArray(new Program[programs.size()]);

        programService.updateProgramData(localAgency.getId(), programArray);
    }

    public List searchPrograms(Program criteria) {
        if (!isEnabled() || programService == null) {
            log.warn("integrator not enabled, or not inited");
            return null;
        }
        Program[] programs = programService.searchPrograms(criteria);
        List<Program> results = new ArrayList<Program>();
        results.addAll(Arrays.asList(programs));
        return results;
    }

    public Program getProgram(Long agencyId, Long programId) throws IntegratorNotEnabledException {
        if (!isEnabled() || programService == null) {
            log.warn("integrator not enabled, or not inited");
            throw new IntegratorNotEnabledException();
        }
        return programService.getProgram(agencyId, programId);
    }

    public void sendReferral(Long agencyId, ClientReferral referral) {
        if (!isEnabled()) {
            return;
        }

        this.outgoingReferralBean.setReferral(referral);

        jmsTemplate.setDefaultDestinationName(agencyId + ".refer");
        try {
            jmsTemplate.send(this.outgoingReferralBean);
        } catch (JmsException e) {
            log.error(e);
        }
    }

    public Demographic[] matchClient(Demographic client) throws IntegratorException {
        if (!isEnabled() || agencyService == null) {
            throw new IntegratorNotEnabledException();
        }
        try {
            return agencyService.matchClient(localAgency.getId(), client);
        } catch (XFireRuntimeException e) {
            throw new IntegratorException(e.getMessage());
        }
    }

    public Demographic getDemographic(long agencyId, long demographicNo) throws IntegratorException {
        if (!isEnabled() || agencyService == null) {
            throw new IntegratorNotEnabledException();
        }
        try {
            return agencyService.getDemographic(agencyId, demographicNo);
        } catch (XFireRuntimeException e) {
            throw new IntegratorException(e.getMessage());
        }
    }

    public void saveClient(Demographic client) throws IntegratorException {
        if (!isEnabled() || agencyService == null) {
            throw new IntegratorNotEnabledException();
        }
        try {
            agencyService.saveClient(localAgency.getId(), client);
        } catch (XFireRuntimeException e) {
            throw new IntegratorException(e.getMessage());
        }
    }

    public void mergeClient(Demographic localClient, long remoteAgency, long remoteClientId) throws IntegratorException {
        if (!isEnabled() || agencyService == null) {
            throw new IntegratorNotEnabledException();
        }
        try {
            agencyService.mergeClients(getLocalAgencyId(), String.valueOf(localClient.getDemographicNo()), remoteAgency, String.valueOf(remoteClientId));
        } catch (XFireRuntimeException e) {
            throw new IntegratorException(e.getMessage());
        }
    }

    public void refreshPrograms(List<Program> programs) throws IntegratorException {
        if (!isEnabled() || programService == null) {
            throw new IntegratorNotEnabledException();
        }
        Program[] programArray = programs.toArray(new Program[programs.size()]);

        programService.updateProgramData(getLocalAgencyId(), programArray);
    }

    public void refreshAdmissions(List<Admission> admissions) throws IntegratorException {
        if (!isEnabled() || programService == null) {
            throw new IntegratorNotEnabledException();
        }
        Admission[] admissionArray = admissions.toArray(new Admission[admissions.size()]);

        programService.updateAdmissionData(getLocalAgencyId(), admissionArray);
    }

    public void refreshProviders(List<Provider> providers) throws IntegratorException {
        if (!isEnabled() || agencyService == null) {
            throw new IntegratorNotEnabledException();
        }
        Provider[] providerArray = providers.toArray(new Provider[providers.size()]);

        agencyService.updateProviderData(getLocalAgencyId(), providerArray);
    }

    public void refreshReferrals(List<ClientReferral> referrals) throws IntegratorException {
        if (!isEnabled() || programService == null) {
            throw new IntegratorNotEnabledException();
        }
        ClientReferral[] referralArray = referrals.toArray(new ClientReferral[referrals.size()]);

        programService.updateReferralData(getLocalAgencyId(), referralArray);
    }

    public void refreshClients(List<Demographic> clients) throws IntegratorException {
        if (!isEnabled() || agencyService == null) {
            throw new IntegratorNotEnabledException();
        }
        Demographic[] clientArray = clients.toArray(new Demographic[clients.size()]);
        for (Demographic aClientArray : clientArray) {
            List<DemographicExt> extras = clientManager.getDemographicExtByDemographicNo(aClientArray.getDemographicNo());
            aClientArray.setExtras(extras.toArray(new DemographicExt[extras.size()]));
        }
        agencyService.updateDemographicData(getLocalAgencyId(), clientArray);
    }

    public List getCurrentAdmissions(long clientId) throws IntegratorException {
        if (!isEnabled() || programService == null) {
            throw new IntegratorNotEnabledException();
        }

        Admission[] admissions = programService.getCurrentAdmissions(localAgency.getId(), clientId);
        List<Admission> results = new ArrayList<Admission>();
        results.addAll(Arrays.asList(admissions));
        return results;
    }

    public List getCurrentReferrals(long clientId) throws IntegratorException {
        if (!isEnabled() || programService == null) {
            throw new IntegratorNotEnabledException();
        }

        ClientReferral[] referrals = programService.getCurrentReferrals(localAgency.getId(), clientId);
        List<ClientReferral> results = new ArrayList<ClientReferral>();
        results.addAll(Arrays.asList(referrals));
        return results;
    }

    public long getLocalClientId(long agencyId, long demographicNo) throws IntegratorException {
        if (!isEnabled() || agencyService == null) {
            throw new IntegratorNotEnabledException();
        }

        return agencyService.getLocalClientId(localAgency.getId(), agencyId, demographicNo);
    }

    public boolean notifyUpdate(short dataType, String id) {
        if (!isEnabled() || agencyService == null) {
            return false;
        }

        switch (dataType) {
            case IntegratorManager.DATATYPE_CLIENT:
                return updateClient(id);
        }

        return true;
    }

    public Demographic getClient(String demographicNo) {
        if (!isEnabled() || agencyService == null) {
            return null;
        }

        return agencyService.getDemographic(localAgency.getId(), Long.parseLong(demographicNo));
    }

    public String getIntegratorVersion() {
        if (!isEnabled() || agencyService == null) {
            return null;
        }

        return agencyService.getVersion();

    }

    public void setClientManager(ClientManager mgr) {
        this.clientManager = mgr;
    }

    public ClientManager getClientManager() {
        return this.clientManager;
    }

    protected boolean updateClient(String id) {
        if(!isEnabled() || agencyService == null) {
            return false;
        }

        Demographic demographic = clientManager.getClientByDemographicNo(id);
        List extras = clientManager.getDemographicExtByDemographicNo(Integer.valueOf(id));

        demographic.setExtras((DemographicExt[])extras.toArray(new DemographicExt[extras.size()]));

        agencyService.updateClient(getLocalAgency().getId(), demographic.getDemographicNo(),demographic);

        return true;
    }
}
