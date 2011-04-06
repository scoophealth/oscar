<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%@page import="org.oscarehr.common.dao.ProviderPreferenceDao"%>
<%@page import="org.oscarehr.common.model.ProviderPreference"%>
<%@page import="org.oscarehr.web.admin.ProviderPreferencesUIBean"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>

<%
	//@SuppressWarnings("unchecked")
	HashMap<String,String[]> parameters=new HashMap(request.getParameterMap());
		
	String providerNo = parameters.get("provider_no")[0];
	
	ProviderPreferenceDao preferenceDao = (ProviderPreferenceDao) SpringUtils.getBean("providerPreferenceDao");
	ProviderPreference preference = ProviderPreferencesUIBean.updateOrCreateProviderPreferences(request);
	
	//preference.setDefaultDxCode(parameters.get("dxCode")[0]);
	//preference.setNew_tickler_warning_window(parameters.get("new_tickler_warning_window")[0]);
	//PreferenceAction.savePreference(preference);	
	
	//parameters.remove("dxCode");
	//parameters.remove("provider_no");		
	
	response.sendRedirect(request.getContextPath()+"/provider/providerDefaultDxCode.jsp?provider_no="+providerNo);
%>