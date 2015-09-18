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

<%@ page errorPage="../appointment/errorpage.jsp"
	import="java.util.*, oscar.oscarReport.data.*"%>
<%@ page import="oscar.login.*"%>
<%@ page import="org.apache.commons.lang.*"%>
<%
String reportId = request.getParameter("id")!=null ? request.getParameter("id") : "0";
String SAVE_AS = "default";
// get form name
String reportName = (new RptReportItem()).getReportName(reportId);

// get form parameters
RptReportConfigData confObj = new RptReportConfigData();
RptTableFieldNameCaption tableObj = new RptTableFieldNameCaption();
Vector vecTableName = new Vector();
if(confObj.getReportTableNameList(reportId)!=null) vecTableName = confObj.getReportTableNameList(reportId);
String tableName = request.getParameter("tableName");
if(tableName==null) tableName = vecTableName.size() >= 1? (String)vecTableName.get(0) : "";

// add/delete action 
if (request.getParameter("submit") != null && request.getParameter("submit").equals(" Add ")) {
	String strCapName = request.getParameter("selField") != null ? request.getParameter("selField") : "";
	String[] strTemp = strCapName.split("\\|");
	if(strTemp.length>1) {
	    String fieldName = strTemp[1];
	    String fieldCaption = strTemp[0];
		confObj.setReport_id(Integer.parseInt(reportId));
		confObj.setTable_name(tableName);
		confObj.setName(fieldName);
		confObj.setCaption(fieldCaption);
		confObj.setSave(SAVE_AS);
		confObj.insertRecordWithOrder();
	}
}
if (request.getParameter("submit") != null && request.getParameter("submit").equals("Delete")) {
	String strCapName = request.getParameter("selConfig") != null ? request.getParameter("selConfig") : "";
	String[] strTemp = strCapName.split("\\|");
	if(strTemp.length>1) {
	    String fieldName = strTemp[1];
	    String fieldCaption = strTemp[0];
		confObj.setReport_id(Integer.parseInt(reportId));
		confObj.setTable_name(tableName);
		confObj.setName(fieldName);
		confObj.setCaption(fieldCaption);
		confObj.setSave(SAVE_AS);
		confObj.deleteRecord();
	}
}
if (request.getParameter("submit") != null && request.getParameter("submit").equals(" Go ")) {
	tableName = request.getParameter("selTable") != null ? request.getParameter("selTable") : "";
}

// get display data
Vector vecConfigField = new Vector();
Vector vecTableField = new Vector();
Vector vecFormTable = new Vector();
if("".equals(tableName)) {
	// get form table list to choose: name/tablename
	vecFormTable = tableObj.getFormTableNameList();
} else {
	// standard
	vecConfigField = confObj.getConfigNameList(SAVE_AS, reportId);
	vecTableField = tableObj.getTableNameCaption(tableName);
}

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
		<td><%=reportName%> Configuration</td>
		<td width="10%" align="right" nowrap><a
			href="reportFilter.jsp?id=<%=reportId%>">Back to the Report</a></td>
	</tr>
</table>

<table width="100%" border="1" cellspacing="0" cellpadding="2">
	<form method="post" name="baseurl0" action="reportFormConfig.jsp">
	<% if(vecFormTable.size()>0) { %>
	
	<tr>
		<td colspan="3" align="center"><font color="red">Please
		select a form name first </font> <select name="selTable">
			<%
	for(int i=0; i<vecFormTable.size(); i=i+2) {
		String formName = (String)vecFormTable.get(i);
		String formTable = (String)vecFormTable.get(i+1);
%>
			<option value="<%=formTable%>"><%=formName%></option>
			<% 	} %>
		</select> <input type="submit" name="submit" value=" Go " /></td>
	</tr>
	<% } %>
	<tr bgcolor="<%="#EEEEFF"%>">
		<td align="center" width="45%">Form | <a
			href="reportFormDemoConfig.jsp?id=<%=reportId%>&tableName=<%="demographic"%>&formTableName=<%=tableName%>&configTableName=<%=tableName%>">Patient
		Profile</a> <br />
		<select size=28 name="selField" ondblclick="javascript:onSelField();">
			<%
String strMatchConfig = "";
for(int i=0; i<vecConfigField.size(); i++) {
	strMatchConfig += StringUtils.replace((String)vecConfigField.get(i), "|", "\\|") + "|";
}
for(int i=0; i<vecTableField.size(); i++) {
	String color = i%2==0? "#EEEEFF" : "";
	String captionName = (String)vecTableField.get(i);
	if(captionName.matches(strMatchConfig)) continue;
	captionName = StringEscapeUtils.escapeHtml(captionName);
%>
			<option value="<%=captionName%>"><%=captionName%></option>
			<% } %>
		</select> <br>
		<a
			href="reportFormCaption.jsp?id=<%=reportId%>&tableName=<%=tableName%>">Add
		Caption</a></td>

		<td align="center" width="20%" nowrap valign="top">
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td colspan="2">Fields | Selected <br>
				<br>
				==<input type="submit" name="submit" value=" Add " />=&gt;&gt; <br>
				<br>
				&lt;&lt;=<input type="submit" name="submit" value="Delete" />==
			</tr>
		</table>
		</td>

		<td width="45%" align="center"><select size=28 name="selConfig"
			ondblclick="javascript:onSelField();">
			<% for(int i=0; i<vecConfigField.size(); i++) {
	String captionName = (String)vecConfigField.get(i);
	captionName = StringEscapeUtils.escapeHtml(captionName);
%>
			<option value="<%=captionName%>"><%=captionName%></option>
			<% } %>
		</select> <br>
		<a
			href="reportFormOrder.jsp?id=<%=reportId%>&save=<%=SAVE_AS%>&tableName=<%=tableName%>">Change
		Order</a> <input type="hidden" name="id" value="<%=reportId%>"> <input
			type="hidden" name="tableName" value="<%=tableName%>"> <input
			type="hidden" name="configTableName" value="<%=tableName%>">
		</td>
	</tr>
	</form>
</table>


</body>
</html:html>
