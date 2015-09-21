<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
if(request.getParameter("submit")!=null && request.getParameter("submit").equals("Report in CSV")) {
  if(true) {
    out.clear();
    pageContext.forward("reportDownload"); //forward request&response to the target page
    return;
  }
}
%>
<%@ page errorPage="../appointment/errorpage.jsp"
	import="java.util.*, oscar.oscarReport.data.*"%>
<%@ page import="oscar.oscarReport.pageUtil.*"%>
<%@ page import="oscar.login.*"%>
<%
String VALUE = "value_";
String DATE_FORMAT = "dateFormat_";
String SAVE_AS = "default";
String reportId = request.getParameter("id")!=null ? request.getParameter("id") : "0";
// get form name
String reportName = (new RptReportItem()).getReportName(reportId);

RptFormQuery formQuery = new RptFormQuery();
String reportSql = formQuery.getQueryStr(reportId, request);

RptReportConfigData formConfig = new RptReportConfigData();
Vector[] vecField = formConfig.getAllFieldNameValue(SAVE_AS, reportId);
Vector vecFieldCaption = vecField[1];
Vector vecFieldName = vecField[0];
Vector vecFieldValue = (new RptReportCreator()).query(reportSql, vecFieldCaption);

%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Report List</title>
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
		<th><%=reportName%></th>
	</tr>
</table>
</center>
<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr BGCOLOR="#CCCCFF">
		<td></td>
		<td width="10%" align="right" nowrap><a
			href="reportFilter.jsp?id=<%=reportId%>">Back to Report Filter</a></td>
	</tr>
</table>


<hr>
<table BORDER="0" CELLPADDING="1" CELLSPACING="1" WIDTH="100%"
	class="sortable tabular_list">
	<thead>
		<tr BGCOLOR="#66CCCC">
			<% for(int i=0; i<vecFieldCaption.size(); i++) { %>
			<th><%=(String) vecFieldCaption.get(i)%></th>
			<% } %>
		</tr>
	</thead>
	<% for(int i=0; i<vecFieldValue.size(); i++) {
	String color = i%2==0? "#EEEEFF" : "#DDDDFF";
	Properties prop = (Properties) vecFieldValue.get(i);
%>
	<tr BGCOLOR="<%=color%>">
		<% for(int j=0; j<vecFieldCaption.size(); j++) { %>
		<td><%=prop.getProperty((String) vecFieldCaption.get(j), "")%>&nbsp;</td>
		<% } %>
	</tr>
	<% } %>
</table>

<script language="javascript" src="../commons/scripts/sort_table/css.js">
      <script language="javascript" src="../commons/scripts/sort_table/common.js">
      <script language="javascript" src="../commons/scripts/sort_table/standardista-table-sorting.js">
    </body>
  </html:html>
