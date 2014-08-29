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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%-- This JSP is the first page you see when you enter 'report by template' --%>
<%@page import="java.lang.reflect.Field"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.*" %>
<%@ page import="org.oscarehr.common.dao.ServiceRequestTokenDao" %>
<%@ page import="org.oscarehr.common.dao.ServiceAccessTokenDao" %>
<%@ page import="org.oscarehr.common.dao.ServiceClientDao" %>
<%@ page import="org.oscarehr.common.model.ServiceClient" %>
<%@ page import="org.oscarehr.common.model.ServiceRequestToken" %>
<%@ page import="org.oscarehr.common.model.ServiceAccessToken" %>
<%
	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	String providerNo=loggedInInfo.getLoggedInProviderNo();

	ServiceRequestTokenDao serviceRequestTokenDao = SpringUtils.getBean(ServiceRequestTokenDao.class);
	ServiceAccessTokenDao serviceAccessTokenDao = SpringUtils.getBean(ServiceAccessTokenDao.class);
	ServiceClientDao serviceClientDao = SpringUtils.getBean(ServiceClientDao.class);
	
	List<ServiceRequestToken> requestTokens = new ArrayList<ServiceRequestToken>();
	List<ServiceAccessToken> accessTokens = new ArrayList<ServiceAccessToken>();
	
	//find all the tokens/clients associated with this provider
	for(ServiceRequestToken t: serviceRequestTokenDao.findAll()) {
		if(t.getProviderNo() != null && t.getProviderNo().equals(providerNo)){
			requestTokens.add(t);
		}
	}
	for(ServiceAccessToken t: serviceAccessTokenDao.findAll()) {
		if(t.getProviderNo() != null && t.getProviderNo().equals(providerNo)){
			accessTokens.add(t);
		}
	}
	
	Map<Integer,ServiceClient> clientMap = new HashMap<Integer,ServiceClient>();
	for(ServiceClient c:serviceClientDao.findAll()) {
		clientMap.put(c.getId(),c);	
	}
	
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Manage API Clients</title>

<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/share/css/OscarStandardLayout.css">
<script type="text/javascript" language="JavaScript" src="<%= request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" language="JavaScript" src="<%= request.getContextPath() %>/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" language="JavaScript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"></script>

<style type="text/css">
table.outline {
	margin-top: 50px;
	border-bottom: 1pt solid #888888;
	border-left: 1pt solid #888888;
	border-top: 1pt solid #888888;
	border-right: 1pt solid #888888;
}

table.grid {
	border-bottom: 1pt solid #888888;
	border-left: 1pt solid #888888;
	border-top: 1pt solid #888888;
	border-right: 1pt solid #888888;
}

td.gridTitles {
	border-bottom: 2pt solid #888888;
	font-weight: bold;
	text-align: center;
}

td.gridTitlesWOBottom {
	font-weight: bold;
	text-align: center;
}

td.middleGrid {
	border-left: 1pt solid #888888;
	border-right: 1pt solid #888888;
	text-align: center;
}

label {
	float: left;
	width: 120px;
	font-weight: bold;
}

label.checkbox {
	float: left;
	width: 116px;
	font-weight: bold;
}

label.fields {
	float: left;
	width: 80px;
	font-weight: bold;
}

span.labelLook {
	font-weight: bold;
}

input,textarea,select { //
	margin-bottom: 5px;
}

textarea {
	width: 450px;
	height: 100px;
}

.boxes {
	width: 1em;
}

#submitbutton {
	margin-left: 120px;
	margin-top: 5px;
	width: 90px;
}

br {
	clear: left;
}
</style>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/cupertino/jquery-ui-1.8.18.custom.css">

<script>
	function deleteAccessToken(id) {
		jQuery.getJSON("tokenManage.json",
                {
                        method: "deleteAccessToken",
                        id: id
                },
                function(xml){
                	if(xml.success)
                		window.location = 'clients.jsp';
                	else
                		alert(xml.error);
                });	
	}
	
	function deleteRequestToken(id) {
		jQuery.getJSON("tokenManage.json",
                {
                        method: "deleteRequestToken",
                        id: id
                },
                function(xml){
                	if(xml.success)
                		window.location = 'clients.jsp';
                	else
                		alert(xml.error);
                });			
	}
	
</script>
</head>

<body vlink="#0000FF" class="BodyStyle">

<table class="MainTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">Provider</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar" style="width: 100%;">
			<tr>
				<td>Manage API Client/Tokens</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top" width="160px;">
		&nbsp;</td>
		<td class="MainTableRightColumn" valign="top">
		
			
			<h3>Request Tokens</h3>
			<br/>
			<%if(requestTokens.size()>0) { %>
			<table width="80%" id="requestTokenTable" name="requestTokenTable">
				<thead>
					<tr>
						<td><b>Client Name</b></td>
						<td><b>Date Created</B></td>
						<td><b>Verified</B></td>
						<td><b>Actions</b></td>
					</tr>
				</thead>
				<tbody>
					<%for(ServiceRequestToken srt: requestTokens) { %>
					<tr>
						<td><%=clientMap.get(srt.getClientId()).getName() %></td>
						<td><%=dateFormatter.format(srt.getDateCreated()) %></td>
						<td><%=srt.getVerifier()%></td>
						<td><a href="javascript:void(0);" onclick="deleteRequestToken('<%=srt.getId()%>');"><img border="0" title="delete" src="<%=request.getContextPath() %>/images/Delete16.gif"/></a></td>
					</tr>
					<% } %>
				</tbody>
			</table>
			<% } else {%>
				<h4>No Request Tokens found.</h4>
			<% } %>
			<br/><br/>
			<h3>Access Tokens</h3>
			<br/>
			<%if(accessTokens.size()>0) { %>
			<table width="80%" id="accessTokenTable" name="accessTokenTable">
				<thead>
					<tr>
						<td><b>Client Name</b></td>
						<td><b>Date Created</B></td>
						<td><b>Expires</B></td>
						<td><b>Actions</b></td>
					</tr>
				</thead>
				<tbody>
					<%for(ServiceAccessToken sat: accessTokens) { %>
					<tr>
						<td><%=clientMap.get(sat.getClientId()).getName() %></td>
						<td><%=sat.getDateCreated() %></td>
						<td>
						<%
							Date d = new Date();
							d.setTime(sat.getIssued()*1000);
							Calendar c = Calendar.getInstance();
							c.setTime(d);
							c.add(Calendar.SECOND, (int)sat.getLifetime());
						%>
						<%=dateFormatter.format(c.getTime()) %>
						</td>
						<td><a href="javascript:void(0);" onclick="deleteAccessToken('<%=sat.getId()%>');"><img border="0" title="delete" src="<%=request.getContextPath() %>/images/Delete16.gif"/></a></td>
					</tr>
					<% } %>
				</tbody>
			</table>
			<% } else {%>
				<h4>No Access Tokens found.</h4>
			<% } %>
					
			
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>


</body>


</html:html>
