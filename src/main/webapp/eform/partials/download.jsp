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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed2=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_admin.eform" rights="w" reverse="<%=true%>">
	<%authed2=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.eform");%>
</security:oscarSec>
<%
	if(!authed2) {
		return;
	}
%>

<!DOCTYPE html>
<%@page import="java.io.*,java.util.*,java.net.*,org.oscarehr.common.dao.*,org.oscarehr.common.model.*,org.oscarehr.util.*"%>
<%@page import="org.codehaus.jettison.json.*"%>
<%@page import="org.joda.time.*"%>
<%@page import="org.oscarehr.ws.rest.FormsService"%>
<%@page import="org.oscarehr.app.AppOAuth1Config"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">

<%@ page session="true" %>
<head>
<link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath()%>/css/DT_bootstrap.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.dataTables.js"></script>
</head>

<style>
	body { background-color:#f5f5f5; }
</style>

<body>
	<%
	AppDefinitionDao appDefinitionDao = SpringUtils.getBean(AppDefinitionDao.class);
	AppUserDao appUserDao = SpringUtils.getBean(AppUserDao.class);
	
	AppDefinition k2aApp = appDefinitionDao.findByName("K2A");
	
	if(k2aApp == null) { %>
		<div>
			<p>A K2A instance is unavailable for this OSCAR instance. Please authenticate a K2A instance or contact an administrator for support.</p>
		</div>
	<% } else { 
			LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		
			String k2aURL = AppOAuth1Config.fromDocument(k2aApp.getConfig()).getBaseURL();
			AppUser k2aUser = appUserDao.findForProvider(k2aApp.getId(),loggedInInfo.getLoggedInProvider().getProviderNo());
			
			if(k2aUser == null) { %>
				<div>Please authenticate with K2A using your username and password <a href="" onclick="window.open('<%=request.getContextPath()%>/web/#/settings');">here</a> under the integration tab. Once completed please refresh the page.</div>
			<% } else {
				String jsonString = FormsService.getK2AEFormsList(loggedInInfo,k2aApp, k2aUser);
				JSONArray jsonArray = new JSONArray();
				if(jsonString == null || jsonString.isEmpty()) { %>
					<p>An error occurred while retrieving data from K2A. Please contact an administrator for support.</p>
			<%  } else {
					jsonArray = new JSONArray(jsonString);
			%>
			<script language="javascript">
				function downloadAllK2AEForms() {
					var xhr = new XMLHttpRequest();
					xhr.onreadystatechange = function() {
					    if (xhr.readyState == 4) {
					    	var jsonObject = JSON.parse(xhr.responseText);
					    	document.getElementById("total_downloaded").innerHTML = "";
			   				document.getElementById("errors").innerHTML = "";
			   				
				    		document.getElementById("total_downloaded").innerHTML = 'Total EForms Processed: ' + jsonObject.total;
				    		document.getElementById("errors").innerHTML = jsonObject.content;
				    		document.getElementById("download_all_k2a_eforms").disabled = false;
				    		document.getElementById("download_all_k2a_eforms").value = '<bean:message key="eform.download.msgDownloadEform" />';
					    }
					}
					xhr.open("POST", "<%=request.getContextPath()%>/ws/rs/forms/getAllK2AEForms");
					xhr.setRequestHeader('Content-Type', 'application/json');
				    xhr.send(JSON.stringify(<%=jsonArray%>));
				}
				
				function downloadK2AEForm(id) {
					var xhr = new XMLHttpRequest();
					xhr.onreadystatechange = function() {
					    if (xhr.readyState == 4) {
					    	var jsonObject = JSON.parse(xhr.responseText);
					    	document.getElementById("total_downloaded").innerHTML = "";
			   				document.getElementById("errors").innerHTML = "";
			   				
				    		document.getElementById("total_downloaded").innerHTML = 'Total EForms Processed: ' + jsonObject.total;
				    		document.getElementById("errors").innerHTML = jsonObject.content;
					    }
					}
					xhr.open("POST", "<%=request.getContextPath()%>/ws/rs/forms/getK2AEForm");
					xhr.setRequestHeader('Content-Type', 'application/json');
					xhr.send(id);
				}
				
				$(document).ready(function(){
					$('#k2aEFormTbl').dataTable({
						"bPaginate": false,
					    "aoColumnDefs": [{"bSortable":false, "aTargets":[0]}]
					});
				});

			</script>
			<div>
				<h5 id="total_downloaded"></h5>
				<p id="errors"></p>
			</div>
			<h4><bean:message key="eform.download.msgK2AResources" /></h4>
			<input type="button" id="download_all_k2a_eforms" value="<bean:message key="eform.download.msgDownloadEform" />" class="btn btn-primary upload" onclick="this.value = 'Downloading...'; this.disabled = true;downloadAllK2AEForms();" />
			<input type="button" value="<bean:message key="eform.download.msgK2ABrowse" />" class="btn btn-primary upload" onclick="window.open('<%=k2aURL%>/#/ws/rs/posts/browse/EForm');" />
			<input type="button" value="<bean:message key="eform.download.msgRefresh" />" class="btn btn-primary upload" onclick="location.reload();" />
			
			<table class="table table-condensed table-striped" id="k2aEFormTbl">
				<thead>
		            <tr>
		                <th>&nbsp;</th>
		                <th><bean:message key="eform.download.msgName" /></th>
		                <th><bean:message key="eform.download.msgCreator" /></th>
		                <th><bean:message key="eform.download.msgCategory" /></th>
		                <th><bean:message key="eform.download.msgCreated" /></th>
		            </tr>
		        </thead>
		        <tbody>
		            <%
		            for (int i = 0; i < jsonArray.length(); i++) {
			        	JSONObject eform = jsonArray.getJSONObject(i);
		            %>
		            <tr>
		                <td>
		                	<a href="#" onclick="downloadK2AEForm(<%=eform.getString("id")%>);"><i class="icon-download-alt" title="<bean:message key="eform.download.btnLoadEform"/>"></i></a>
		                </td>
		                <td><%=eform.getString("name")%></td>
		                <td><%=eform.getString("creator")%></td>
		                <td><%=eform.getString("category")%></td>
		                <td><% String pattern = "E MMM dd HH:mm:ss z yyyy";
		                	   String createdAtString = eform.getString("createdAt");
		                	   DateTime createdAt = new DateTime(createdAtString); %>
		                	<%=createdAt.toString(pattern)%>
		                </td>
		            </tr>
		            <%}%>
		       	</tbody>
			</table>
			<% } %>
		<% } %>
	<% } %>
	<div>&nbsp;</div>
</body>
</html:html>

<%!
	String stripDrugref(Object obj){
		String s = "";
	    if (obj !=null){
	    	s = (String) obj;
	        return s.substring(26);
	    }
	    return "";
	}
%>
