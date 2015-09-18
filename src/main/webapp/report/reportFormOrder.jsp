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
String tableName = request.getParameter("tableName")!=null ? request.getParameter("tableName") : "";
String formTableName = request.getParameter("formTableName")!=null ? request.getParameter("formTableName") : tableName;
String configTableName = request.getParameter("configTableName")!=null ? request.getParameter("configTableName") : formTableName;
String SAVE_AS = request.getParameter("save")!=null ? request.getParameter("save") : "default";

// get form name
String reportName = (new RptReportItem()).getReportName(reportId);

// get form parameters
RptReportConfigData tableObj = new RptReportConfigData();

// change order action 
if (request.getParameter("submit") != null && request.getParameter("submit").equals("Move here")) {
	String itemId = request.getParameter("nameSelected") != null ? request.getParameter("nameSelected") : "";
	String newPos = request.getParameter("position") != null ? request.getParameter("position") : "";
	tableObj.updateRecordOrder(SAVE_AS, reportId, itemId, newPos);
}
// get display data

Vector vecConfigObj = tableObj.getConfigObj(SAVE_AS, reportId);
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Report Field Order</title>
<LINK REL="StyleSheet" HREF="../web.css" TYPE="text/css">
<script language="JavaScript">

		<!--
		var n = 0;
		function setfocus() {
		  this.focus();
		  //document.forms[0].service_code.focus();
		}
	    function onButMove(pos) {
			document.forms[0].position.value = pos;
	    }
	    function checkscript() {
			if (n!=1) {
				// something is wrong
				alert('You need ONLY one selection!');
				return false;
			}
			return true;
	    }
	    function onCheckbox(param, num) {
	    	var oTR = param.parentNode.parentNode;
			if(param.checked) {
				n++;
				if(n>1) {
					alert("You can not have more than one selection.");
					n--;
					param.checked = false;
				} else {
					oTR.className = 'trSelected';
				}
			} else {
				n--;
				oTR.className = num%2==0?'trOdd':'trEven';
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
<center></center>
<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr BGCOLOR="#CCCCFF">
		<td><%=reportName%> Order</td>
		<td width="10%" align="right" nowrap><a
			href="reportFormConfig.jsp?id=<%=reportId%>&tableName=<%=tableName%>">Back
		to the Configuration</a></td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="2" cellpadding="2">
	<tr>
		<td width="70%">

		<table width="100%" border="0" cellspacing="1" cellpadding="2">
			<form method="post" name="baseurl" action="reportFormOrder.jsp"
				onsubmit="return checkscript()">
			<%
for(int i=0; i<vecConfigObj.size(); i++) {
	String color = i%2==0? "trOdd":"trEven"; //"#EEEEFF" : "";
	Properties prop = (Properties)vecConfigObj.get(i);
    String fieldName = StringEscapeUtils.escapeHtml(prop.getProperty("name",""));
    String fieldCaption = StringEscapeUtils.escapeHtml(prop.getProperty("caption",""));
    String fieldId = prop.getProperty("id","");
    String fieldPosition = prop.getProperty("order_no","");
	String action = " Add ";
%>
			
			<tr class=<%=color%>>
				<td width="20%" align="right"><input type="checkbox"
					name="nameSelected" value="<%=fieldId%>"
					onClick="onCheckbox(this, <%=i%>);" /></td>
				<td width="30%" nowrap><span title="<%=fieldName%>"><%=fieldCaption%></span></td>
				<td align="center"><input type="submit" name="submit"
					value="Move here" onClick="onButMove(<%=fieldPosition%>)" /></td>
			</tr>
			<% } %>
			<input type="hidden" name="position" />
			<input type="hidden" name="id" value="<%=reportId%>" />
			<input type="hidden" name="save" value="<%=SAVE_AS%>">
			<input type="hidden" name="tableName" value="<%=tableName%>" />
			<input type="hidden" name="formTableName" value="<%=formTableName%>" />
			<input type="hidden" name="configTableName"
				value="<%=configTableName%>" />
			</form>
		</table>
		</td>
		<td></td>
	</tr>
</table>


</body>
</html:html>
