package org.oscarehr.PMmodule.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.ClientDao;
import org.oscarehr.PMmodule.dao.ConsentDAO;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.Consent;
import org.oscarehr.PMmodule.model.ConsentInterview;
import org.oscarehr.PMmodule.service.ConsentManager;

public class ConsentManagerImpl implements ConsentManager
{
	private static Log log = LogFactory.getLog(ConsentManagerImpl.class);
	private ConsentDAO dao;
	private ClientDao clientDao;
	
	public void setConsentDao(ConsentDAO dao) {
		this.dao = dao;
	}
	
	public void setClientDao(ClientDao dao) {
		this.clientDao = dao;
	}
	
	public Consent getConsentByDemographic(Long demographicNo){
		return dao.getConsentByDemographic(demographicNo);
	}
	
	public void saveConsent(Consent consent){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm");

		clientDao.saveDemographicExt(consent.getDemographicNo().intValue(), "consent_st",consent.getStatus());
		clientDao.saveDemographicExt(consent.getDemographicNo().intValue(), "consent_ex",consent.getExclusionString());
		clientDao.saveDemographicExt(consent.getDemographicNo().intValue(), "consent_ag",String.valueOf(Agency.getLocalAgency().getId()));
		clientDao.saveDemographicExt(consent.getDemographicNo().intValue(), "consent_dt",formatter.format(new Date()));
				
		dao.saveConsent(consent);
	}
	
	public Consent getMostRecentConsent(Long demographicNo){
		return dao.getMostRecentConsent(demographicNo);
	}
	
	public void saveConsentInterview(ConsentInterview consent) {
		dao.saveConsentInterview(consent);
	}
	
	public List getConsentInterviews() {
		return dao.getConsentInterviews();
	}
	
	public ConsentInterview getConsentInterview(String id) {
		return dao.getConsentInterview(Long.valueOf(id));
	}
	
	public ConsentInterview getConsentInterviewByDemographicNo(String demographicNo) {
		return dao.getConsentInterviewByDemographicNo(new Long(demographicNo));
	}
}
