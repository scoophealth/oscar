<%@ include file="/taglibs.jsp"%>
<%@ page import="java.util.List"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Program History</title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>
<body>

<display:table cellspacing="2" cellpadding="9" id="ps"
	name="programSignatures" export="false" pagesize="0"
	requestURI="/PMmodule/ProgramManager.do">

	<display:column property="providerName" style="white-space: nowrap;"
		sortable="true" title="Provider Name"></display:column>
	<display:column property="caisiRoleName" style="white-space: nowrap;"
		sortable="true" title="Role"></display:column>
	<display:column property="updateDate" style="white-space: nowrap;"
		sortable="true" title="Date"></display:column>
</display:table>

</body>
</html:html>
<!--  
<input type="button" value="Back" onClick="history.go(-1)"/>
-->
</br>
<input type="button" value="Close" onClick="self.close()" />
<!--
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td>&nbsp;</td>
		<td>Provider Name</td>
		<td>Role</td>
		<td>Date</td>
	</tr>
	
	
	<c:forEach var="ps" items="${programSignatures}">
	<tr class="b">
		<td><c:out value="${ps.providerName}"/></td>
		<td><c:out value="${ps.caisiRoleName}" /></td>
		<td><c:out value="${ps.updateDate}" /></td>	
	</tr>
	</c:forEach>
</table>
-->