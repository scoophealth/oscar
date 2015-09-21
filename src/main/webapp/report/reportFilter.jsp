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
<%
String reportId = request.getParameter("id")!=null ? request.getParameter("id") : "0";
// get form name
String reportName = (new RptReportItem()).getReportName(reportId);


boolean bDeletedList = false;
String msg = "Limit To";
Properties	prop  = new Properties();
RptReportFilter reportFilter = new RptReportFilter();

// delete/undelete list
if(request.getParameter("undelete")!=null && "true".equals(request.getParameter("undelete"))) {
	bDeletedList = true;
}
// delete action
if (request.getParameter("submit") != null && request.getParameter("submit").equals("Delete")) {
	// check the input data
	String id = request.getParameter("id");
	int nId = id!=null ? Integer.parseInt(id) : 0 ;
}

// search the list
int n = bDeletedList? 0 : 1;
String link = bDeletedList? "<a href='reportFormRecord.jsp'>Report list</a>" : "<a href='reportFormRecord.jsp?undelete=true'>Deleted report list</a>";
Vector vec = reportFilter.getNameList(reportId, n);
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><%=bDeletedList? "Deleted" : ""%> Report List</title>
<LINK REL="StyleSheet" HREF="../web.css" TYPE="text/css">
<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />
<!-- main calendar program -->
<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript"
	src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
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
		<td><%=msg%></td>
		<td width="10%" align="right" nowrap><a
			href="reportFormRecord.jsp">Back to Report List</a> | <a
			href="reportFormConfig.jsp?id=<%=reportId%>">Configuration</a></td>
	</tr>
</table>
<table width="100%" border="0" cellspacing="2" cellpadding="2">
	<form method="post" name="baseurl" action="reportResult.jsp">
	<%
Vector vecJS = new Vector();
for(int i=0; i<vec.size(); i++) {
	String color = i%2==0? "#EEEEFF" : "";
	String [] strElt = (String[]) vec.get(i);
	String itemId = strElt[3];
	vecJS.add(strElt[4]);
%>
	
	<tr bgcolor="<%=color%>">
		<td align="right" width="20%"><b><input type="checkbox"
			name="<%="filter_" + itemId%>" <%="1".equals(itemId)?"checked":""%>></b>
		</td>
		<td><%=strElt[0]%></td>
		<td width="5%" align="right"><input type="hidden"
			name="<%="value_" + itemId%>" value="<%=strElt[1]%>"> <input
			type="hidden" name="<%="position_" + itemId%>" value="<%=strElt[2]%>">
		<input type="hidden" name="<%="dateFormat_" + itemId%>"
			value="<%=strElt[5]%>"></td>
	</tr>
	<% } %>
	<tr bgcolor="silver">
		<td colspan="2" align="center"><input type="hidden" name="id"
			value="<%=reportId%>"> <input type="submit" name="submit"
			value="Report in HTML"> | <input type="submit" name="submit"
			value="Report in CSV"></td>
		<td align='right'></td>
	</tr>
	</form>
</table>


</body>
<script type="text/javascript">
<%
for(int i=0; i<vecJS.size(); i++) {
	out.print(vecJS.get(i));
}
%>
      </script>
</html:html>
