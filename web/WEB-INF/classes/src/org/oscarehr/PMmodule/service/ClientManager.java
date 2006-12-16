package org.oscarehr.PMmodule.service;

import java.util.List;

import org.oscarehr.PMmodule.exception.AlreadyAdmittedException;
import org.oscarehr.PMmodule.exception.AlreadyQueuedException;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.DemographicExt;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;


public interface ClientManager  {
	
	public Demographic getClientByDemographicNo(String demographicNo);

	public List getClients();
		
	public List search(ClientSearchFormBean criteria);

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
}

