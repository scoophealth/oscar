<!--
/*
 *
 * Copyright (c) 2005- OSCAR SERVICE. All Rights Reserved. *
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
  if (session.getAttribute("user") == null) {
    response.sendRedirect("../logout.jsp");
  }
%>
<%@ page errorPage="../appointment/errorpage.jsp"
	import="java.util.*, oscar.oscarReport.data.*"%>
<%@ page import="oscar.login.*"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%
boolean bDeletedList = false;
String msg = "Report List";
Properties	prop  = new Properties();
RptReportItem reportItem = new RptReportItem();

// delete/undelete list
if(request.getParameter("undelete")!=null && "true".equals(request.getParameter("undelete"))) {
	bDeletedList = true;
}
// delete action
if (request.getParameter("submit") != null && request.getParameter("submit").equals("Delete")) {
	// check the input data
	String id = request.getParameter("id");
	int nId = id!=null ? Integer.parseInt(id) : 0 ;
	if(!reportItem.deleteRecord(nId) ) {
		msg = "The report is NOT deleted by some reasons. Please check your action.";
	}
}
// undelete action
if (request.getParameter("submit") != null && request.getParameter("submit").equals("Restore")) {
	// check the input data
	String id = request.getParameter("id");
	int nId = id!=null ? Integer.parseInt(id) : 0 ;
	if(!reportItem.unDeleteRecord(nId) ) {
		msg = "The report is NOT undeleted by some reasons. Please check your action.";
	}
}
// add action
if (request.getParameter("submit") != null && request.getParameter("submit").equals("Add")) {
	// check the input data
	String report_name = request.getParameter("name");
	reportItem.setReport_name(report_name) ;
	if(!reportItem.insertRecord() ) {
		msg = "The report is NOT added by some reasons. Please check your action.";
	}
}

// search the list
int n = bDeletedList? 0 : 1;
String link = bDeletedList? "<a href='reportFormRecord.jsp'>Report list</a>" : "<a href='reportFormRecord.jsp?undelete=true'>Deleted report list</a>";
Vector vec = reportItem.getNameList(n);
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><%=bDeletedList? "Deleted" : ""%> Report List</title>
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
	    function onAdd() {
			if(document.baseurl.name.value.length < 2) {
				alert("Please type in a valid name!");
				return false;
			} else {
	        	return true;
	        }
	    }

	    function goPage(id) {
			self.location.href = "reportFilter.jsp?id=" + id;
	    }
		//-->

      </script>
</head>
<body bgcolor="ivory" onLoad="setfocus()" topmargin="0" leftmargin="0"
	rightmargin="0">
<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr>
		<td align="left">&nbsp;</td>
	</tr>
</table>

<center>
<table BORDER="1" CELLPADDING="0" CELLSPACING="0" WIDTH="80%">
	<tr BGCOLOR="#CCFFFF">
		<th><%=msg%></th>
	</tr>
</table>
</center>
<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr BGCOLOR="#CCCCFF">
		<td align="right"><%=link%></td>
	</tr>
</table>
<table width="100%" border="0" cellspacing="2" cellpadding="2">
	<% for(int i=0; i<vec.size(); i++) {
		String color = i%2==0? "#EEEEFF" : "";
		prop = (Properties) vec.get(i);
		String itemId = prop.getProperty("id");
%>
	<form method="post" name="baseurl<%=i+1%>"
		action="reportFormRecord.jsp">
	<tr bgcolor="<%=color%>">
		<td align="right"><b><%=i+1%></b></td>
		<td
			onMouseOver="this.style.cursor='hand';this.style.backgroundColor='pink';"
			onMouseout="this.style.backgroundColor='<%=color%>';"
			onClick="goPage(<%=itemId%>)"><%=prop.getProperty(itemId)%></td>
		<td width="5%" align="right"><input type="hidden" name="id"
			value="<%=itemId%>"> <% if(!bDeletedList) { %> <input
			type="submit" name="submit" value="Delete"
			onclick="javascript:return onDelete();"> <% } else { %> <input
			type="submit" name="submit" value="Restore"
			onclick="javascript:return onRestore();"> <% } %>
		</td>
	</tr>
	</form>
	<% } %>
</table>

<hr>
<table width="60%" border="0" cellspacing="2" cellpadding="2">
	<tr>
		<td>Add a new report</td>
	</tr>
	<tr>
		<td align="center">
		<form method="post" name="baseurl" action="reportFormRecord.jsp">
		<input type="text" name="name" value="" size="60" /> <input
			type="submit" name="submit" value="Add"
			onclick="javascript:return onAdd();" /></form>
		</td>
	</tr>
</table>

</body>
</html:html>
