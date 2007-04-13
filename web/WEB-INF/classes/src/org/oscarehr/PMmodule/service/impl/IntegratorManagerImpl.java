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

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.DemographicExt;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.PMmodule.service.IntegratorManager;
import org.oscarehr.integrator.ws.AgencyService;
import org.oscarehr.integrator.ws.ProgramService;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;

public class IntegratorManagerImpl extends BaseIntegratorManager implements IntegratorManager {

	private static Log log = LogFactory.getLog(IntegratorManagerImpl.class);

	private static Agency localAgency;
	private static Map agencyMap;

	private boolean initCalled = false;

	private AgencyDao agencyDAO;
	private JmsTemplate jmsTemplate;
	private OutgoingReferralBean outgoingReferralBean;

	public static Agency getLocalAgency() {
		return localAgency;
	}

	public static Map getAgencyMap() {
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

		if (localAgency != null) {
			return localAgency.isIntegratorEnabled();
		} else {
			return false;
		}
	}

	public boolean isRegistered() {
		if (!isEnabled()) {
			log.warn("integrator not enabled");
			return false;
		}

		if (localAgency != null && localAgency.getId().longValue() > 0) {
			return true;
		}

		return false;
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
				return;
			} catch (Throwable e) {
				log.error(e);
				localAgency.setIntegratorEnabled(false);
				return;
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
			agencyMap = new HashMap();
			for (int x = 0; x < agencies.length; x++) {
				agencyMap.put(agencies[x].getId(), agencies[x]);
			}
		}
		agencyMap.put(new Long(0), localAgency);
		Agency.setAgencyMap(agencyMap);
		log.info("Agency Map is setup");
	}

	public void setupLocalAgencyMap() {
		agencyMap = new HashMap();
		agencyMap.put(new Long(0), localAgency);
		Agency.setAgencyMap(agencyMap);
		log.info("Agency Map is setup");
	}

	public void refresh() {
		init();
	}

	public long getLocalAgencyId() {
		return localAgency.getId().longValue();
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
			List result = new ArrayList();
			for (int x = 0; x < agencies.length; x++) {
				result.add(agencies[x]);
			}
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

	public void updateProgramData(List programs) {
		if (!isEnabled() || programService == null) {
			log.warn("integrator not enabled, or not inited");
			return;
		}
		Program programArray[] = (Program[]) programs.toArray(new Program[programs.size()]);

		programService.updateProgramData(localAgency.getId(), programArray);
	}

	public List searchPrograms(Program criteria) {
		if (!isEnabled() || programService == null) {
			log.warn("integrator not enabled, or not inited");
			return null;
		}
		Program[] programs = programService.searchPrograms(criteria);
		List results = new ArrayList();
		for (int x = 0; x < programs.length; x++) {
			results.add(programs[x]);
		}
		return results;
	}

	public Program getProgram(Long agencyId, Long programId) throws IntegratorNotEnabledException {
		if (!isEnabled() || programService == null) {
			log.warn("integrator not enabled, or not inited");
			throw new IntegratorNotEnabledException();
		}
		Program program = programService.getProgram(agencyId, programId);
		return program;
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
			return agencyService.matchClient(localAgency.getId().longValue(), client);
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
			agencyService.saveClient(localAgency.getId().longValue(), client);
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

	public void refreshPrograms(List programs) throws IntegratorException {
		if (!isEnabled() || programService == null) {
			throw new IntegratorNotEnabledException();
		}
		Program[] programArray = (Program[]) programs.toArray(new Program[programs.size()]);

		programService.updateProgramData(new Long(getLocalAgencyId()), programArray);
	}

	public void refreshAdmissions(List admissions) throws IntegratorException {
		if (!isEnabled() || programService == null) {
			throw new IntegratorNotEnabledException();
		}
		Admission[] admissionArray = (Admission[]) admissions.toArray(new Admission[admissions.size()]);

		programService.updateAdmissionData(new Long(getLocalAgencyId()), admissionArray);
	}

	public void refreshProviders(List providers) throws IntegratorException {
		if (!isEnabled() || agencyService == null) {
			throw new IntegratorNotEnabledException();
		}
		Provider[] providerArray = (Provider[]) providers.toArray(new Provider[providers.size()]);

		agencyService.updateProviderData(getLocalAgencyId(), providerArray);
	}

	public void refreshReferrals(List referrals) throws IntegratorException {
		if (!isEnabled() || programService == null) {
			throw new IntegratorNotEnabledException();
		}
		ClientReferral[] referralArray = (ClientReferral[]) referrals.toArray(new ClientReferral[referrals.size()]);

		programService.updateReferralData(new Long(getLocalAgencyId()), referralArray);
	}

	public void refreshClients(List clients) throws IntegratorException {
		if (!isEnabled() || agencyService == null) {
			throw new IntegratorNotEnabledException();
		}
		Demographic[] clientArray = (Demographic[]) clients.toArray(new Demographic[clients.size()]);
		for (int x = 0; x < clientArray.length; x++) {
			List extras = clientManager.getDemographicExtByDemographicNo(clientArray[x].getDemographicNo());
			clientArray[x].setExtras((DemographicExt[]) extras.toArray(new DemographicExt[extras.size()]));
		}
		agencyService.updateDemographicData(getLocalAgencyId(), clientArray);
	}

	public List getCurrentAdmissions(long clientId) throws IntegratorException {
		if (!isEnabled() || programService == null) {
			throw new IntegratorNotEnabledException();
		}

		Admission[] admissions = programService.getCurrentAdmissions(localAgency.getId().longValue(), clientId);
		List results = new ArrayList();
		for (int x = 0; x < admissions.length; x++) {
			results.add(admissions[x]);
		}
		return results;
	}

	public List getCurrentReferrals(long clientId) throws IntegratorException {
		if (!isEnabled() || programService == null) {
			throw new IntegratorNotEnabledException();
		}

		ClientReferral[] referrals = programService.getCurrentReferrals(localAgency.getId().longValue(), clientId);
		List results = new ArrayList();
		for (int x = 0; x < referrals.length; x++) {
			results.add(referrals[x]);
		}
		return results;
	}

	public long getLocalClientId(long agencyId, long demographicNo) throws IntegratorException {
		if (!isEnabled() || agencyService == null) {
			throw new IntegratorNotEnabledException();
		}

		return agencyService.getLocalClientId(localAgency.getId().longValue(), agencyId, demographicNo);
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

		return agencyService.getDemographic(localAgency.getId().longValue(), Long.parseLong(demographicNo));
	}

	public String getIntegratorVersion() {
		if (!isEnabled() || agencyService == null) {
			return null;
		}

		return agencyService.getVersion();

	}
}
