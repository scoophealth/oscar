<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>
<%@page import="org.oscarehr.myoscar.utils.MyOscarLoggedInInfo"%>
<%@page import="org.oscarehr.common.model.Security"%>
<%@page import="org.oscarehr.ws.WsUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="org.bouncycastle.util.encoders.UrlBase64"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="oscar.OscarProperties"%>
<%
	String clinicConfigUrl=StringUtils.trimToNull(OscarProperties.getInstance().getProperty("oscar_myoscar_clinic_component_url"));
	String oscarWsUrl=StringUtils.trimToNull(OscarProperties.getInstance().getProperty("ws_endpoint_url_base"));
   	LoggedInInfo oscarLoggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
   	Security loggedInSecurity=oscarLoggedInInfo.getLoggedInSecurity();
   	MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(session);
	
	if (clinicConfigUrl==null || oscarWsUrl==null)
	{
		String errorMsg="Some one is running the clinic config admin page but the urls are not set. clinicConfigUrl="+clinicConfigUrl+", oscarWsUrl="+oscarWsUrl;
		MiscUtils.getLogger().error(errorMsg);
		%>
			<%=errorMsg%>
			return;
		<%
	}

	StringBuilder sb=new StringBuilder();
	sb.append(clinicConfigUrl);
	
	if (clinicConfigUrl.indexOf('?')==-1) sb.append('?');
	else sb.append('&');
	
	sb.append("oscarUserName=");
	String temp=loggedInSecurity.getUserName();
	sb.append(URLEncoder.encode(temp, "UTF-8"));
	
	sb.append("&oscarToken=");
	temp=WsUtils.generateSecurityToken(loggedInSecurity);
	sb.append(URLEncoder.encode(temp, "UTF-8"));
		
	sb.append("&myOscarUserName=");
	temp=myOscarLoggedInInfo.getLoggedInPerson().getUserName();
	sb.append(URLEncoder.encode(temp, "UTF-8"));
	
	sb.append("&myOscarToken=");
	temp=myOscarLoggedInInfo.getLoggedInPersonSecurityToken();
	sb.append(URLEncoder.encode(temp, "UTF-8"));
		
	String resultUrl=sb.toString();
	
	MiscUtils.getLogger().debug("clinic config url : "+resultUrl);
	
	// redirect
	response.sendRedirect(resultUrl);
%>