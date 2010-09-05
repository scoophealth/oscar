package org.oscarehr.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.dao.ProviderPreferenceDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ProviderPreference;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public final class ProviderPreferencesUIBean {
	
	private static final ProviderPreferenceDao providerPreferenceDao=(ProviderPreferenceDao) SpringUtils.getBean("providerPreferenceDao");

	public static final ProviderPreference updateOrCreateProviderPreferences(HttpServletRequest request)
	{
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		Provider provider=loggedInInfo.loggedInProvider;
		
		// get or create new preferences entry
		ProviderPreference providerPreference=providerPreferenceDao.find(provider.getProviderNo());
		if (providerPreference==null)
		{
			providerPreference=new ProviderPreference();
			providerPreference.setProviderNo(provider.getProviderNo());
			providerPreferenceDao.persist(providerPreference);
		}
		
		// update preferences based on request parameters
		String temp;
		HttpSession session=request.getSession();
		
		// new tickler window
		temp=StringUtils.trimToNull(request.getParameter("new_tickler_warning_window"));
		if (temp!=null)
		{
			providerPreference.setNewTicklerWarningWindow(temp);
		}
		else
		{
			temp=StringUtils.trimToNull((String)session.getAttribute("newticklerwarningwindow"));
			if (temp!=null) providerPreference.setNewTicklerWarningWindow(temp);
		}

		// default pmm
		temp=StringUtils.trimToNull(request.getParameter("default_pmm"));
		if (temp!=null)
		{
			providerPreference.setDefaultCaisiPmm(temp);
		}
		else
		{
			temp=StringUtils.trimToNull((String)session.getAttribute("default_pmm"));
			if (temp==null) providerPreference.setDefaultCaisiPmm("disabled");
			else providerPreference.setDefaultCaisiPmm(temp);
		}
		
		// rest
		temp=StringUtils.trimToNull(request.getParameter("start_hour"));
		if (temp!=null)	providerPreference.setStartHour(Integer.parseInt(temp));
		
		temp=StringUtils.trimToNull(request.getParameter("end_hour"));
		if (temp!=null)	providerPreference.setEndHour(Integer.parseInt(temp));
		
		temp=StringUtils.trimToNull(request.getParameter("every_min"));
		if (temp!=null)	providerPreference.setEveryMin(Integer.parseInt(temp));
		
		temp=StringUtils.trimToNull(request.getParameter("mygroup_no"));
		if (temp!=null)	providerPreference.setMyGroupNo(temp);
		
		temp=StringUtils.trimToNull(request.getParameter("default_servicetype"));
		if (temp!=null)	providerPreference.setDefaultServiceType(temp);
		
		temp=StringUtils.trimToNull(request.getParameter("color_template"));
		if (temp!=null)	providerPreference.setColourTemplate(temp);
		
		providerPreferenceDao.merge(providerPreference);
		
		return(providerPreference);
	}
	
	public static ProviderPreference getLoggedInProviderPreference()
	{
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		return(providerPreferenceDao.find(loggedInInfo.loggedInProvider.getProviderNo()));
	}
}
