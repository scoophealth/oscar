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
<%@ page import="org.oscarehr.common.dao.StudyDetailsDao, org.oscarehr.common.model.StudyDetails" %>
<%@ page import="org.oscarehr.common.model.Demographic, org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="java.util.Set" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.reporting");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>



<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Demographic Study Listing</title>
<style type="text/css">
BODY {
	font-family: Arial, Verdana, Tahoma, Helvetica, sans-serif;
	background-color: #EEEEFF;
}


table {
	border: 1pt 1pt 1pt 1pt;
	border-style:solid;
	text-align:center;
	margin-top: 20pt;
}

th {
	font-size: 15pt;
	font-weight: bold;
	text-align: center;
	background-color: #003399;;
	color: #FFFFFF;	
}

td {
	font-size: 10pt;	
	border-bottom: 1pt dotted blue;
}

a:link {
	text-decoration: none;
	color: #003399;
}

a:active {
	text-decoration: none;
	color: #003399;
}

a:visited {
	text-decoration: none;
	color: #003399;
}

a:hover {
	text-decoration: none;
	color: #003399;
}

.smallButton { font-size: 8pt; }

</style>
<script type="text/javascript">

var studyMembersChecked = false;
var providerMembersChecked = false;

function selectAll(type) {
	var elements = new Array();
	var check;
	
	if( type == "Demographic" ) {
		elements = document.getElementsByName("demographicNo");
		check = studyMembersChecked;
		studyMembersChecked = !studyMembersChecked;
	}
	else if( type == "Provider") {		
		elements = document.getElementsByName("providerNo");
		check = providerMembersChecked;
		providerMembersChecked = !providerMembersChecked;
	}
	
	for( var idx = 0; idx < elements.length; ++idx ) {
		if( check ) {
			elements[idx].checked = false;
		}
		else {
			elements[idx].checked = true;
		}
	}
	
}

function checkForm(type) {
	
	var elements = new Array();
	var ret = false;
	if( type == "Demographic" ) {
		elements = document.getElementsByName("demographicNo");	
	}
	else if( type == "Provider") {
		elements = document.getElementsByName("providerNo");
	}
	
	for( var idx = 0; idx < elements.length; ++idx ) {
		if( elements[idx].checked ) {
			ret = true;
			break;
		}	
	}
	
	if( !ret ) {
		alert("Please select at least one " + type + " to remove.");
	}
	
	return ret;
	
}
</script>
</head>
<body>
<%
	String error = (String)request.getAttribute("error");
	if( error != null ) {		
%>
	<h3><%=error%></h3>
<%
	}
	else {
	    String studyId = request.getParameter("studyId");
	    if( studyId == null ) {
			studyId = (String)request.getAttribute("studyId");
	    }
	    
	    StudyDetailsDao studyDetailsDao = (StudyDetailsDao)SpringUtils.getBean(StudyDetailsDao.class);
	    StudyDetails studyDetails = studyDetailsDao.find(Integer.parseInt(studyId));
	    Set<Demographic> setDemographics = studyDetails.getDemographics();
	    Set<Provider> setProviders = studyDetails.getProviders();	    
%>
<form method="post" action="../study/ManageStudy.do">
<input type="hidden" name="method" value="RemoveFromStudy"/>
<input type="hidden" name="studyId" value="<%=studyId%>"/>
<div style="float:right; text-align:center;">
<h4>Patients enrolled in <%=studyDetails.getStudyName()%></h4>
<input type="submit" name="submit" value="Remove Demographic" onclick="return checkForm('Demographic');"/>
<table>
<tr>
	<th>Remove &nbsp; <input type="checkbox" onclick="selectAll('Demographic')"/></th>
	<th>Num</th>
	<th>Name</th>
	<th>DOB</th>
	<th>Address</th>
	<th>MRP</th>
</tr>
<%
		for( Demographic demo : setDemographics ) {
%>
			<tr>
				<td><input type="checkbox" id="d_<%=demo.getDemographicNo()%>" name="demographicNo" value="<%=demo.getDemographicNo()%>"/></td>
				<td><%=demo.getDemographicNo()%></td>
				<td><%=demo.getDisplayName()%></td>
				<td><%=demo.getBirthDayAsString()%></td>
				<td><%=demo.getAddress() != null ? demo.getAddress() : "N/A"%></td>
				<td><%=demo.getProvider() != null ? demo.getProvider().getFormattedName() : "N/A"%></td>
			</tr>
<%
		}
%>
</table>
</div>
<div style="float:left; text-align:center;">
<h4>Providers enrolled in <%=studyDetails.getStudyName()%></h4>
<input type="submit" name="submit" value="Remove Provider" onclick="return checkForm('Provider');"/>
<table>
<tr>
	<th>Remove &nbsp; <input type="checkbox" onclick="selectAll('Provider')"/></th>
	<th>Num</th>
	<th>Name</th>	
</tr>
<%
		for( Provider provider : setProviders ) {
%>
			<tr>
				<td><input type="checkbox" id="p_<%=provider.getProviderNo()%>" name="providerNo" value="<%=provider.getProviderNo()%>"/></td>
				<td><%=provider.getProviderNo()%></td>
				<td><%=provider.getFormattedName()%></td>
			</tr>
<%
		}
%>
</table>
</div>
</form>
<%}%>
</body>
</html>