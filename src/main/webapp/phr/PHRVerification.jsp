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
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.common.dao.ConsentDao,org.oscarehr.common.model.Consent,org.oscarehr.util.SpringUtils,java.util.*,org.oscarehr.managers.AppManager,org.oscarehr.common.model.AppDefinition,org.oscarehr.common.model.Provider" %>

<%@ page import="oscar.oscarDemographic.data.DemographicData"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="oscar.util.DateUtils"%>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao,oscar.util.UtilDateUtilities" %>
<%@ page import="org.oscarehr.phr.util.MyOscarServerRelationManager,org.oscarehr.phr.util.MyOscarUtils" %>

<%@ taglib uri="/WEB-INF/phr-tag.tld" prefix="phr"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html-el" prefix="html-el" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<%-- security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
	<%response.sendRedirect("../logout.jsp");%>
</security:oscarSec --%>




<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%-- This JSP is the first page you see when you enter 'report by template' --%>
<%
String demographicNo = request.getParameter("demographic_no");
if (demographicNo == null) demographicNo = request.getParameter("demographicNo");
if (demographicNo == null) demographicNo = (String) request.getAttribute("demographicNo");
Integer demoNo = Integer.parseInt(demographicNo);

ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");

AppManager appManager = SpringUtils.getBean(AppManager.class);
ConsentDao consentDao = SpringUtils.getBean(ConsentDao.class);

org.oscarehr.common.model.Demographic demo = new DemographicData().getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demographicNo); 
String myOscarUserName = demo.getMyOscarUserName();
String providerName = "N/A";
AppDefinition appDef = appManager.getAppDefinition(LoggedInInfo.getLoggedInInfoFromSession(request), "PHR");
Consent consent = null;
if(appDef != null && appDef.getConsentTypeId() != null) {
	consent = consentDao.findByDemographicAndConsentTypeId( demoNo,  appDef.getConsentTypeId()  ) ;
	if(consent != null){
		Provider provider = providerDao.getProvider(consent.getLastEnteredBy());
		providerName = provider.getFormattedName();
	}
}

%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<c:out value="${ctx}"/>/js/jquery.js"></script>
<script type="text/javascript">
	function checkLevel(level) {
		if (level=="") {
			alert("Please select a verification method");
			return false;
		}
		return true;
	}
</script>
<title><bean:message key="phr.verification.title"/></title>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">

<script type="text/javascript" language="JavaScript" src="../share/javascript/Oscar.js"></script>

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
	//float: left;
	width: 120px;
	//font-weight: bold;
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
</head>

<body vlink="#0000FF" class="BodyStyle">

<table class="MainTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar" style="width: 100%;">
			<tr>
				<td>
					<bean:message key="phr.verification.title"/> &nbsp; <oscar:nameage demographicNo="<%=demographicNo%>"/> &nbsp; <oscar:phrverification demographicNo="<%=demographicNo%>"><bean:message key="phr.verification.link"/></oscar:phrverification> 
					&nbsp;
					
					<%if(demographicNo!=null){%>
					<div id="relationshipMessage"></div>    
					<script type="text/javascript">
					$.ajax({
					    url: '<c:out value="${ctx}"/>/phr/PatientRelationship.jsp?demoNo=<%=demographicNo%>&myOscarUserName=<%=myOscarUserName%>',
					    dataType: 'html',
					    timeout: 5000,
					    cache: false,
					    error: function() { alert("Error talking to server."); },
					    success: function(data) {
					      $("#relationshipMessage").html(data);
					    }
					  });
					
					
					
					jQuery(document).ready(function(){
						//Check if PHR is active and if patient has consented	
						/*
						PHR inactive                    FALSE      INACTIVE
							PHR active & Consent Needed     TRUE       NEED_CONSENT
							PHR Active & Consent exists.    TRUE       CONSENTED
							*/
						    jQuery.ajax({
						        url: "<%=request.getContextPath()%>/ws/rs/app/PHRActive/consentGiven/<%=demographicNo%>",
						        dataType: 'json',
						        success: function (data) {
						       		console.log("PHR CONSENT",data);
						       		if(data.success && data.message === "NEED_CONSENT"){
						       			jQuery("#phrConsent").show();
						       		}else{
						       			jQuery("#phrConsent").hide();
						       		}
						    		}
							});
							
						jQuery("#phrConsent").click(function() {
					  		jQuery.ajax({
					  			type: "POST",
						        url: "<%=request.getContextPath()%>/ws/rs/app/PHRActive/consentGiven/<%=demographicNo%>",
						        dataType: 'json',
						        success: function (data) {
						       		console.log("PHR CONSENT POST",data);
						       		if(data.success && data.message === "NEED_CONSENT"){
						       			jQuery("#phrConsent").show();
						       		}else{
						       			alert("Successfully confirmed");
						       			jQuery("#phrConsent").hide();
						       			location.reload(); 
						       		}
						    		}
							});
						});
						
					});
					<%}%>
					</script>    
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableRightColumn" valign="top">
			<fieldset>
		    	<legend><bean:message key="phr.verification.add.fieldset.legend"/></legend>
		    		<%if(consent != null && consent.getPatientConsented()){ %>
		    			<h2>This person (PHR username: <span style="color:blue;"><%=myOscarUserName%></span>) was confirmed on <%=consent.getConsentDate()%> by <%=providerName%> </h2>
		    		<%} else { %>
		    			<h2>Confirm this person is using PHR username: <span style="color:blue;"><%=myOscarUserName%></span></h2>
		    			<input type="button" id="phrConsent"  value="Confirm" />
		    		<%} %>
		    	
    			</fieldset>
    
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>
</html:html>
