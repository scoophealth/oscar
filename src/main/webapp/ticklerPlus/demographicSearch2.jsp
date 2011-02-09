<%@ include file="/taglibs.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Demographic Search</title>
<%
	String query=request.getParameter("query");
	String formName=request.getParameter("form");
	String elementName=request.getParameter("elementName");
	String elementId=request.getParameter("elementId");
	formName=(formName!=null)?formName:"ticklerForm";
	elementName=(elementName!=null)?elementName:"filter.demographic_webName";
	elementId=(elementId!=null)?elementId:"filter.demographic_no";
%>
</head>
<body onload="document.ADDAPPT.submit();">
<h2>Searching</h2>
<table>
	<form name="ADDAPPT" method="post"
		action="<c:out value="${ctx}"/>/appointment/appointmentcontrol.jsp">
	<tr>
		<td width="35%"><font color="#003366"><font
			face="Verdana, Arial, Helvetica, sans-serif" size="2"><b>Demographic
		Name: </b></font></font></td>
		<td colspan="2" width="65%">
		<div align="left"><INPUT TYPE="HIDDEN"
			value="<%=(query!=null)?query:"" %>" NAME="keyword" size="25">
		</div>

		</td>
	</tr>
	<INPUT TYPE="hidden" NAME="orderby" VALUE="last_name">
	<INPUT TYPE="hidden" NAME="search_mode" VALUE="search_name">
	<INPUT TYPE="hidden" NAME="originalpage"
		VALUE="../tickler/ticklerAdd.jsp">
	<INPUT TYPE="hidden" NAME="limit1" VALUE="0">
	<INPUT TYPE="hidden" NAME="limit2" VALUE="5">
	<!--input type="hidden" name="displaymode" value="TicklerSearch" -->
	<INPUT TYPE="hidden" NAME="displaymode" VALUE="Search ">
	<INPUT TYPE="hidden" NAME="appointment_date" VALUE="2002-10-01"
		WIDTH="25" HEIGHT="20" border="0" hspace="2">
	<INPUT TYPE="hidden" NAME="status" VALUE="t" WIDTH="25" HEIGHT="20"
		border="0" hspace="2">
	<INPUT TYPE="hidden" NAME="start_time" VALUE="10:45" WIDTH="25"
		HEIGHT="20" border="0" onChange="checkTimeTypeIn(this)">
	<INPUT TYPE="hidden" NAME="type" VALUE="" WIDTH="25" HEIGHT="20"
		border="0" hspace="2">
	<INPUT TYPE="hidden" NAME="duration" VALUE="15" WIDTH="25" HEIGHT="20"
		border="0" hspace="2">
	<INPUT TYPE="hidden" NAME="end_time" VALUE="10:59" WIDTH="25"
		HEIGHT="20" border="0" hspace="2" onChange="checkTimeTypeIn(this)">
	<input type="hidden" name="demographic_no" readonly value="" width="25"
		height="20" border="0" hspace="2">
	<input type="hidden" name="location" tabindex="4" value="" width="25"
		height="20" border="0" hspace="2">
	<input type="hidden" name="resources" tabindex="5" value="" width="25"
		height="20" border="0" hspace="2">
	<INPUT TYPE="hidden" NAME="user_id" readonly VALUE='oscardoc, doctor'
		WIDTH="25" HEIGHT="20" border="0" hspace="2">
	<INPUT TYPE="hidden" NAME="dboperation" VALUE="add_apptrecord">
	<INPUT TYPE="hidden" NAME="createdatetime" readonly
		VALUE="2002-10-1 17:53:50" WIDTH="25" HEIGHT="20" border="0"
		hspace="2">
	<INPUT TYPE="hidden" NAME="provider_no" VALUE="115">
	<INPUT TYPE="hidden" NAME="creator" VALUE="oscardoc, doctor">
	<INPUT TYPE="hidden" NAME="remarks" VALUE="">

	<INPUT TYPE="hidden" NAME="caisi" VALUE="true">
	<INPUT TYPE="hidden" NAME="formName" VALUE="<%=formName %>" />
	<INPUT TYPE="hidden" NAME="elementName" VALUE="<%=elementName %>" />
	<INPUT TYPE="hidden" NAME="elementId" VALUE="<%=elementId %>" />

	</form>
</table>
</body>
</html>
