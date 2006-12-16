package org.oscarehr.PMmodule.dao;

import java.util.List;

import org.oscarehr.PMmodule.model.ClientReferral;

public interface ClientReferralDAO {
	
	public List getReferrals();
	public List getReferrals(Long clientId);
	public List getActiveReferrals(Long clientId);
	public ClientReferral getClientReferral(Long id);
	public void saveClientReferral(ClientReferral referral);
	public ClientReferral getReferralToRemoteAgency(long clientId, long agencyId, long programId);
	public List search(ClientReferral referral);
}
