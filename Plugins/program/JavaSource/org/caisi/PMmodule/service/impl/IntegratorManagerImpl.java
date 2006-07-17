package org.caisi.PMmodule.service.impl;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.PMmodule.dao.AgencyDao;
import org.caisi.PMmodule.integrator.xfire.OutgoingAuthenticationHandler;
import org.caisi.PMmodule.model.Agency;
import org.caisi.PMmodule.service.IntegratorManager;
import org.caisi.integrator.ws.AgencyService;
import org.codehaus.xfire.XFire;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxy;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import org.codehaus.xfire.transport.http.SoapHttpTransport;

public class IntegratorManagerImpl implements IntegratorManager {
	private static Log log = LogFactory.getLog(IntegratorManagerImpl.class);

	private AgencyDao agencyDAO;
	protected boolean enabled;
	
	private Agency localAgency;
	
	AgencyService agencyService= null;
    

	public void setAgencyDao(AgencyDao dao) {
		this.agencyDAO = dao;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public boolean isRegistered() {
		if(!isEnabled()) {
			log.warn("integrator not enabled");
			return false;
		}
		
		if(localAgency.getId().longValue() > 0) {
			return true;
		}
		return false;
	}
	
	public void init() {
		localAgency = agencyDAO.getLocalAgency();
		if(localAgency == null) {
			log.warn("local agency information not in database");
			enabled = false;
			return;
		}
		if(!localAgency.isIntegratorEnabled()) {
			enabled = false;
			return;
		}
		enabled = true;
		log.info("integrated enabled: " + enabled);
		
        XFire xfire = XFireFactory.newInstance().getXFire();
        XFireProxyFactory factory = new XFireProxyFactory(xfire);
        Service serviceModel = new ObjectServiceFactory().create(AgencyService.class);
        try {
        	agencyService = (AgencyService) factory.create(serviceModel,this.localAgency.getIntegratorURL() + "/service/AgencyService");
        }catch(Exception e) {
                e.printStackTrace();
        }
        XFireProxy proxy =  (XFireProxy)Proxy.getInvocationHandler(agencyService);
        proxy.getClient().addOutHandler(new OutgoingAuthenticationHandler(localAgency.getIntegratorUserName(),localAgency.getIntegratorPassword()));
        proxy.getClient().setTransport(new SoapHttpTransport());
	}
	
	public void refresh() {
		init();
	}
	
	public String register(Agency agencyInfo, String key) {
		if(!isEnabled() || agencyService == null) {
			log.warn("integrator not enabled, or not inited");
			return null;
		}
		return agencyService.register(agencyInfo,key);
	}

	public List getAgencies() {
		if(!isEnabled() || agencyService == null) {
			log.warn("integrator not enabled, or not inited");
			return null;
		}
		Agency[] agencies = agencyService.getAgencies();
		List result = new ArrayList();
		for(int x=0;x<agencies.length;x++) {
			result.add(agencies[x]);
		}
		return result;
	}

	public Agency getAgency(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
