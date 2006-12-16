package org.oscarehr.PMmodule.service.impl;

import java.util.List;

import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.DemographicExt;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.integrator.ws.AgencyService;
import org.oscarehr.integrator.ws.ProgramService;

public abstract class BaseIntegratorManager {

	/* services provided by integrator */
	protected AgencyService agencyService= null;
    protected ProgramService programService = null;

	
	/* data managers */
    protected ClientManager clientManager;
    
    
    public abstract boolean isEnabled();
    
    
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
    	
    	agencyService.updateClient(IntegratorManagerImpl.getLocalAgency().getId().longValue(),demographic.getDemographicNo().intValue(),demographic);
    	
    	return true;
    }
}
