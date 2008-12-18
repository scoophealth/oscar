<!--
/*
 *
 * Copyright (c) 2005- www.oscarservice.com . All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * Yi Li
 */
-->
<%
  if (session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
%>
<%@ page errorPage="../appointment/errorpage.jsp"
	import="java.util.*, oscar.oscarReport.data.*"%>
<%@ page import="oscar.login.*"%>
<%@ page import="org.apache.commons.lang.*"%>
<%
String reportId = request.getParameter("id")!=null ? request.getParameter("id") : "0";
String tableName = request.getParameter("tableName")!=null ? request.getParameter("tableName") : "";
String formTableName = request.getParameter("formTableName")!=null ? request.getParameter("formTableName") : tableName;
String configTableName = request.getParameter("configTableName")!=null ? request.getParameter("configTableName") : formTableName;

// get form name
String reportName = (new RptReportItem()).getReportName(reportId);

// get form parameters
RptTableFieldNameCaption tableObj = new RptTableFieldNameCaption();

// add/delete action //System.out.println(request.getParameter("submit"));
if (request.getParameter("submit") != null && request.getParameter("submit").equals(" Add ")) {
	String strName = request.getParameter("name") != null ? request.getParameter("name") : "";
	String strCaption = request.getParameter("caption") != null ? request.getParameter("caption") : "";
	tableObj.setTable_name(tableName);
	tableObj.setName(strName);
	tableObj.setCaption(strCaption);
	tableObj.insertRecord();
}
if (request.getParameter("submit") != null && request.getParameter("submit").equals("Update")) {
	String strName = request.getParameter("name") != null ? request.getParameter("name") : "";
	String strCaption = request.getParameter("caption") != null ? request.getParameter("caption") : "";
	tableObj.setTable_name(tableName);
	tableObj.setName(strName);
	tableObj.setCaption(strCaption);
	tableObj.updateRecord();
}

// get display data
Vector vecTableField = new Vector();
vecTableField = tableObj.getTableNameCaption(tableName);
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Report List</title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<LINK REL="StyleSheet" HREF="../web.css" TYPE="text/css">
<script language="JavaScript">

		<!--
		function setfocus() {
		  this.focus();
		  //document.forms[0].service_code.focus();
		}
	    function onDelete() {
			ret = confirm("Are you sure you want to delete it?");
	        return ret;
	    }
	    function onRestore() {
			ret = confirm("Are you sure you want to restore it?");
	        return ret;
	    }
	    function goCaption() {
			//self.location.href = "reportFormCaption.jsp?id=<%=reportId%>&tableName=<%=tableName%>";
	    }

	    function goPage(id) {
			self.location.href = "reportFilter.jsp?id=" + id;
	    }
		//-->

      </script>
</head>
<body bgcolor="ivory" onLoad="setfocus()" topmargin="0" leftmargin="0"
	rightmargin="0">
<center></center>
<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr BGCOLOR="#CCCCFF">
		<td><%=reportName%> Caption</td>
		<td width="10%" align="right" nowrap>
		<% if("demographic".equals(tableName) ) {%> <a
			href="reportFormDemoConfig.jsp?id=<%=reportId%>&tableName=<%=tableName%>&formTableName=<%=formTableName%>&configTableName=<%=configTableName%>">Back
		to the Configuration</a> <% } else {%> <a
			href="reportFormConfig.jsp?id=<%=reportId%>&tableName=<%=tableName%>">Back
		to the Configuration</a> <% }%>
		</td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="2" cellpadding="2">
	<tr>
		<td width="70%">

		<table width="100%" border="0" cellspacing="1" cellpadding="2">
			<%
for(int i=0; i<vecTableField.size(); i++) {
	String color = i%2==0? "#EEEEFF" : "";
	String captionName = (String)vecTableField.get(i);
	String[] strTemp = captionName.split("\\|");
    String fieldName = "";
    String fieldCaption = "";
	String action = " Add ";
	if(strTemp.length>1) {
	    fieldName = StringEscapeUtils.escapeHtml(strTemp[1]);
	    fieldCaption = StringEscapeUtils.escapeHtml(strTemp[0].trim());
	}
	if(fieldCaption.length()>1) {color="gold";action = "Update";}
%>
			<form method="post" name="baseurl<%=i%>"
				action="reportFormCaption.jsp">
			<tr bgcolor="<%=color%>">
				<td width="50%"><input type="text" name="caption"
					value="<%=fieldCaption%>" size="36" /></td>
				<td width="30%" nowrap><%=fieldName%></td>
				<td align="center"><input type="submit" name="submit"
					value="<%=action%>" /></td>
				<input type="hidden" name="name" value="<%=fieldName%>">
				<input type="hidden" name="id" value="<%=reportId%>">
				<input type="hidden" name="tableName" value="<%=tableName%>">
				<input type="hidden" name="formTableName" value="<%=formTableName%>">
				<input type="hidden" name="configTableName"
					value="<%=configTableName%>">
			</tr>
			</form>
			<% } %>
		</table>
		</td>
		<td></td>
	</tr>
</table>


</body>
</html:html>
