package org.oscarehr.PMmodule.dao;

import java.util.List;

import org.oscarehr.PMmodule.model.Consent;
import org.oscarehr.PMmodule.model.ConsentInterview;

public interface ConsentDAO {
	
	public List getConsents();
	
	public Consent getConsent(Long id);
	
	public Consent getConsentByDemographic(Long demographicNo);
	
	public void saveConsent(Consent consent);
		
	public Consent getMostRecentConsent(Long demographicNo);
	
	public void saveConsentInterview(ConsentInterview consent);
	
	public List getConsentInterviews();
	
	public ConsentInterview getConsentInterview(Long id);
	
	public ConsentInterview getConsentInterviewByDemographicNo(Long demographicNo);
}
