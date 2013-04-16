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
<%@page import="java.net.URLEncoder"%>
<%@page import="org.bouncycastle.util.encoders.UrlBase64"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="oscar.OscarProperties"%>
<%
	String syncConfigUrl=OscarProperties.getInstance().getProperty("oscar_myoscar_sync_component_url");
	String oscarWsUrl=OscarProperties.getInstance().getProperty("ws_endpoint_url_base");
	LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
	
	if (syncConfigUrl==null || oscarWsUrl==null) MiscUtils.getLogger().error("Some one is running the sync config admin page but the urls are not set. syncConfigUrl="+syncConfigUrl+", oscarWsUrl="+oscarWsUrl);

	StringBuilder sb=new StringBuilder();
	sb.append(syncConfigUrl);
	
	if (syncConfigUrl.indexOf('?')==-1) sb.append('?');
	else sb.append('&');
	
	sb.append("userName=");
	String temp=loggedInInfo.loggedInSecurity.getUserName();
	sb.append(URLEncoder.encode(temp, "UTF-8"));
	
	sb.append("&password=");
	temp=loggedInInfo.loggedInSecurity.getPassword();
	sb.append(URLEncoder.encode(temp, "UTF-8"));
	
	sb.append("&oscarWsUrl=");
	sb.append(URLEncoder.encode(oscarWsUrl, "UTF-8"));
	
	// redirect
	response.sendRedirect(sb.toString());
%>