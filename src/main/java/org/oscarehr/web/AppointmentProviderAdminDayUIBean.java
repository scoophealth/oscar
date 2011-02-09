package org.oscarehr.web;

import org.oscarehr.common.dao.EFormDao;
import org.oscarehr.common.dao.ProviderPreferenceDao;
import org.oscarehr.common.model.EForm;
import org.oscarehr.common.model.ProviderPreference;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public final class AppointmentProviderAdminDayUIBean {
	private static EFormDao eFormDao=(EFormDao)SpringUtils.getBean("EFormDao");
	private static ProviderPreferenceDao providerPreferenceDao=(ProviderPreferenceDao)SpringUtils.getBean("providerPreferenceDao");
	
	public static String getLengthLimitedFormName(String formName) {
		int maxLength=3;

		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		String providerId=loggedInInfo.loggedInProvider.getProviderNo();
		ProviderPreference providerPreference=providerPreferenceDao.find(providerId);
		if (providerPreference!=null) maxLength=providerPreference.getAppointmentScreenFormNameDisplayLength();
		
		
		if (formName.length()<=maxLength) return(formName);
		else return(formName.substring(0, maxLength-1)+".");
	}
	
	public static EForm getEForms(Integer eformId)
	{
		return(eFormDao.find(eformId));
	}
}
