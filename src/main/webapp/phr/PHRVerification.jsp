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
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_phr" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_phr");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.common.dao.PHRVerificationDao,org.oscarehr.common.model.PHRVerification,org.oscarehr.util.SpringUtils,java.util.*" %>

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



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%-- This JSP is the first page you see when you enter 'report by template' --%>
<%
String demographicNo = request.getParameter("demographic_no");
if (demographicNo == null) demographicNo = request.getParameter("demographicNo");
if (demographicNo == null) demographicNo = (String) request.getAttribute("demographicNo");
Integer demoNo = Integer.parseInt(demographicNo);

PHRVerificationDao phrVerificationDao = (PHRVerificationDao)SpringUtils.getBean("PHRVerificationDao");
ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");

List<PHRVerification> phrVerifications = phrVerificationDao.findByDemographic(demoNo, true);

org.oscarehr.common.model.Demographic demo = new DemographicData().getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demographicNo); 
String myOscarUserName = demo.getMyOscarUserName();

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
					<%}%>
					</script>    
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableRightColumn" valign="top">
			<%if(request.getAttribute("forwardToOnSuccess") != null ){ %>
			<span style="color:red">Patient Has not been verified in person.  Verify and Continue</span>
			<%}%>	
			<fieldset>
		    	<legend><bean:message key="phr.verification.add.fieldset.legend"/></legend>
		    	<html:form action="/demographic/viewPhrRecord" onsubmit="return checkLevel(verificationLevel.value);" >
			    	<input type="hidden" name="method" value="saveNewVerification"/>
			    	<input type="hidden" name="demographic_no" value="<%=demographicNo%>"/>
			    	<%if(request.getAttribute("forwardToOnSuccess") != null ){ %>
			    	<input type="hidden" name="forwardToOnSuccess" value="<%=request.getAttribute("forwardToOnSuccess")%>" />
			    	<%}%>
			    	<label><bean:message key="phr.verification.add.fieldset.method"/>:</label> 
				    	<select name="verificationLevel">
				    		<option value="">--</option>
				    		<option value="<%=PHRVerification.VERIFICATION_METHOD_FAX%>"><bean:message key="phr.verification.add.fieldset.method.option.fax"/></option>
				    		<option value="<%=PHRVerification.VERIFICATION_METHOD_MAIL%>"><bean:message key="phr.verification.add.fieldset.method.option.mail"/></option>
				    		<option value="<%=PHRVerification.VERIFICATION_METHOD_EMAIL%>"><bean:message key="phr.verification.add.fieldset.method.option.email"/></option>
				    		<option value="<%=PHRVerification.VERIFICATION_METHOD_TEL%>"><bean:message key="phr.verification.add.fieldset.method.option.tel"/></option>
				    		<option value="<%=PHRVerification.VERIFICATION_METHOD_VIDEOPHONE%>"><bean:message key="phr.verification.add.fieldset.method.option.videophone"/></option>
				    		<option value="<%=PHRVerification.VERIFICATION_METHOD_INPERSON%>"><bean:message key="phr.verification.add.fieldset.method.option.inperson"/></option>
				    	</select>
		    	
			    	<label><bean:message key="phr.verification.add.date"/>:</label><input type="text" name="verificationDate"  value="<%=UtilDateUtilities.getToday("yyyy-MM-dd")%>" size="10"/>  
			    	<label><bean:message key="phr.verification.add.photoId"/>:</label> <input type="checkbox" name="photoId" value="true"/> <label><bean:message key="phr.verification.add.parentGuardian"/>:</label> <input type="checkbox" name="parentGuardian" value="true"/> <br>
					<label><bean:message key="phr.verification.add.comments"/>:</label> <br>
					<textarea name="comments" cols="40" rows="5"></textarea>	
					<br>
					<input type="submit" value="<bean:message key="global.btnSave"/>"/>
		    	</html:form>
    		</fieldset>
    
    		<h3><bean:message key="phr.verification.table.heading"/></h3><br>
    
		    <table width="100%" border=1>
		      <tr>
		      	<th width="10%"><bean:message key="phr.verification.table.username"/></th>
		      	<th width="10%"><bean:message key="phr.verification.table.method"/></th>
		      	<th width="10%"><bean:message key="phr.verification.table.date"/></th>
		      	<th width="10%"><bean:message key="phr.verification.table.by"/></th>
		      	<th width="10%"><bean:message key="phr.verification.table.photoId"/></th>
		      	<th width="10%"><bean:message key="phr.verification.table.parentGuardian"/></th>
		      	<th width="50%"><bean:message key="phr.verification.table.comments"/></th>
		      </tr>
		      <%for(PHRVerification phrVerification:phrVerifications ){%>
		      <tr>
		      	<td><%=phrVerification.getPhrUserName()%></td>
		      	<td><%=phrVerification.getVerificationLevel() %></td>
		      	<td title="<%=DateUtils.formatDate(phrVerification.getVerificationDate(), request.getLocale())%>.<%=DateUtils.formatTime(phrVerification.getVerificationDate(), request.getLocale())%>"><%=DateUtils.formatDate(phrVerification.getVerificationDate(), request.getLocale())%></td>
		      	<td><%=providerDao.getProviderName(phrVerification.getVerificationBy()) %></td>
		      	<td align="center">
		      		<%if(phrVerification.getPhotoId()){ %>
		      			<bean:message key="phr.verification.table.Yes"/>
		      		<%}%>
		      	</td>
		      	<td align="center">
		      		<%if(phrVerification.getParentGuardian()){ %>
		      			<bean:message key="phr.verification.table.Yes"/>
		      		<%}%>
		      	</td>
		      	<td><%=phrVerification.getComments() %></td>
		      </tr>
		      <%} %>
		    </table>		
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>
</html:html>
