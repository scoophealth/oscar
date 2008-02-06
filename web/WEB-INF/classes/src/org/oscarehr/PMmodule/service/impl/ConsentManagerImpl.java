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
