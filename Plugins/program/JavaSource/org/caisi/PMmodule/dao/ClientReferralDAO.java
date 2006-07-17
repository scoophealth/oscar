package org.caisi.PMmodule.dao;

import java.util.List;

import org.caisi.PMmodule.model.ClientReferral;

public interface ClientReferralDAO {
	List getReferrals(Long clientId);
	public List getActiveReferrals(Long clientId);
	ClientReferral getClientReferral(Long id);
	void saveClientReferral(ClientReferral referral);
}
