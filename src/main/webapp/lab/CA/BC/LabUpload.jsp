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
<security:oscarSec roleName="<%=roleName$%>" objectName="_lab" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_lab");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
  
  //int demographic_no = Integer.parseInt(request.getParameter("demographic_no")); 
  String demographic_no = request.getParameter("demographic_no"); 
%>

<%@page import="oscar.oscarDemographic.data.*,java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>


<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<!--I18n-->
<title>Lab Upload Utility</title>
<html:base />
<link rel="stylesheet" type="text/css"
	href="../../../share/css/OscarStandardLayout.css">
<script type="text/javascript" src="../../../share/javascript/Oscar.js"></script>

<SCRIPT LANGUAGE="JavaScript">
function displayAndDisable(){
 
   if ( document.forms[0].importFile.value == "" ){
      alert("You must select a file to upload");
      return false;
   }
   document.forms[0].Submit.disabled = true;
   showHideItem('waitingMessage');
   return true;
}
</SCRIPT>


<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" width="175"><bean:message
			key="demographic.demographiceditdemographic.msgPatientDetailRecord" />
		</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Upload <!--i18n--></td>
				<td>&nbsp;</td>
				<td style="text-align: right"><a
					href="javascript:popupStart(300,400,'Help.jsp')"><bean:message
					key="global.help" /></a> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top">&nbsp;</td>
		<td valign="top" class="MainTableRightColumn"><html:form
			action="/lab/labUpload" method="POST" enctype="multipart/form-data"
			onsubmit="javascript: return displayAndDisable()">
			<input type="file" name="importFile" value="">
			<input type="submit" name="Submit" value="Import">
		</html:form> <%
            String outcome = (String) request.getAttribute("outcome");
            if(outcome != null && outcome.equals("success")){ %>
		<div>Lab File Successfully Uploaded</div>
		<%}else if(outcome != null && outcome.equals("exception")){ %>
		<div>There has been a problem Uploading this lab file</div>
		<%}else if(outcome != null && outcome.equals("uploadedPreviously")){ %>
		<div>This file has already been processed</div>
		<%}%>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>
		<td class="MainTableBottomRowRightColumn" valign="top">&nbsp;</td>
	</tr>
</table>
</body>
</html:html>
