package org.oscarehr.PMmodule.service;

import java.util.List;

import org.oscarehr.PMmodule.model.Consent;
import org.oscarehr.PMmodule.model.ConsentInterview;


public interface ConsentManager {
	
	public Consent getConsentByDemographic(Long demographicNo);
	
	public void saveConsent(Consent consent);
	
	public Consent getMostRecentConsent(Long demographicNo); 	

	public void saveConsentInterview(ConsentInterview consent);
	
	public List getConsentInterviews();
	
	public ConsentInterview getConsentInterview(String id);
	
	public ConsentInterview getConsentInterviewByDemographicNo(String demographicNo);
}
