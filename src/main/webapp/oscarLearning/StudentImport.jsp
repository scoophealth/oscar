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

<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="java.util.*,org.oscarehr.learning.web.CourseManagerAction,org.oscarehr.common.model.SecRole"%>



<html:html locale="true">



<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery.js"></script>
<html:base />
<title><bean:message key="oscarLearning.studentImport.title" />
</title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">



<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />


</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message
			key="oscarLearning.studentImport.msgManager" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td></td>
				<td>&nbsp;</td>
				<td style="text-align: right"><oscar:help keywords="course" key="app.top1"/> | <a
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
		<td class="MainTableRightColumn">
	
	<%
		String totalImported = request.getParameter("r");
		if(totalImported != null && totalImported.length()>0) {
	%>				
	
		<h4 style="color:red;"><%=totalImported%> Records Successfully Imported.</h4>
	<%
		}
	%>
				<div id="upload_form">
					<br/>
					<h2>Student Importer</h2>
					<p>Batch upload student data into oscarLearning system. Format should be as follows (per line)</p>
					<p>lastname,firstname,username,password,pin,student#</p>
					<h4>Upload CSV file:</h4>
					<br/>
					<html:form action="/oscarLearning/StudentImport.do?method=uploadFile" method="post" enctype="multipart/form-data">
						<html:file property="file"></html:file>						
						<br/>
						<html:submit/>
					</html:form>
				</div>
	
	
		</td>
	</tr>	
	
	
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>


</html:html>
