<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
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
