package org.oscarehr.managers;

import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;


@Service
public class DemographicManager
{
	@Autowired
	private DemographicDao demographicDao;
	
	public Demographic getDemographic(Integer demographicId)
	{
		Demographic result=demographicDao.getDemographicById(demographicId);
		
		//--- log action ---
		LogAction.addLogSynchronous("DemographicManager.getDemographic", "demographicId="+demographicId);
		
		return(result);
	}
	
	public Demographic getDemographicByMyOscarUserName(String myOscarUserName)
	{
		Demographic result=demographicDao.getDemographicByMyOscarUserName(myOscarUserName);
		
		//--- log action ---
		LogAction.addLogSynchronous("DemographicManager.getDemographic", "demographicId="+result.getDemographicNo());
		
		return(result);
	}
}
