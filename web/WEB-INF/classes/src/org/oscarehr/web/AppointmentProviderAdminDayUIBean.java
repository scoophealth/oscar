package org.oscarehr.web;

import org.oscarehr.common.dao.EFormDao;
import org.oscarehr.common.model.EForm;
import org.oscarehr.util.SpringUtils;

public final class AppointmentProviderAdminDayUIBean {
	private static final int MAX_FORM_NAME_LENGTH=6;
	
	private static EFormDao eFormDao=(EFormDao)SpringUtils.getBean("EFormDao");
	
	public static String getLengthLimitedFormName(String formName) {
		if (formName.length()<MAX_FORM_NAME_LENGTH) return(formName);
		else return(formName.substring(0, MAX_FORM_NAME_LENGTH-1)+".");
	}
	
	public static EForm getEForms(Integer eformId)
	{
		return(eFormDao.find(eformId));
	}
}
