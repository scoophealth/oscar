<%@ include file="/taglibs.jsp" %>

<%@ taglib uri="/WEB-INF/tld/struts-nested.tld" prefix="nested" %>
<html>
<head>
	<title>Issue Search</title>
	<link rel="stylesheet" href="css/casemgmt.css" type="text/css">
</head>

<body>
<nested:form action="/CaseManagementEntry">
<c:url value="/CaseManagementEntry.jsp?demographicNo=${param.demographicNo}&providerNo=${param.providerNo}&demoName=${requestScope.demoName}" var="url" />
<script type="text/javascript">
function backToNote(from)
{
	
	if (from==null)	location.href="<c:out value='${url}' escapeXml='false'/>";
	else location.href="<c:out value='${url}' escapeXml='false'/>"+"&from="+from;
	return false;
}
</script>
<html:hidden property="demographicNo"/>
<html:hidden property="providerNo"/>
<input type="hidden" name="method" value="issueSearch"/>
<input type="hidden" name="lastPage" value="true"/>

<c:if test="${param.from=='casemgmt'||requestScope.from=='casemgmt'}">
<input type="hidden" name="from" value="casemgmt" />
</c:if>

<b>Client name: <I><c:out value="${requestScope.demoName}" /></I></b>
<br><br>

<P><b>Search the Issue </b></P>
<nested:text property="searString"></nested:text>
<nested:submit value="search" onclick="this.form.method.value='issueSearch';"/>
 
<nested:equal property="showList" value="true">
<P><b>Issue List</b></P>
<table width="100%" border="0"  cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
	<tr class="title">
		<td></td>
		<td>Issue Code</td>
		<td>Issue Description</td>
		<td>Issue Role</td>
	</tr>


<nested:iterate indexId="ind" id="newIssueCheckList" property="newIssueCheckList" type="org.caisi.casemgmt.web.CheckIssueBoxBean">
<tr bgcolor="<%= (ind.intValue()%2==0)?"#EEEEFF":"white" %>" align="center">
<td><nested:checkbox indexed="true" name="newIssueCheckList" property="checked"></nested:checkbox>
<td><nested:write name="newIssueCheckList" property="issue.code"/></td>
<td><nested:write name="newIssueCheckList" property="issue.description"/></td>
<td><nested:write name="newIssueCheckList" property="issue.role"/></td>
</tr>
</nested:iterate>
</table>
<br>
<nested:submit value="add checked issue" onclick="this.form.method.value='issueAdd';"/>

</nested:equal>

<logic:equal name="from" value="casemgmt" scope="request">
<nested:submit value="back to notes" onclick="this.form.method.value='edit';backToNote('casemgmt');return false;"/>
</logic:equal>
<logic:notEqual name="from" value="casemgmt" scope="request">
<nested:submit value="back to notes" onclick="this.form.method.value='edit';backToNote(); return false;"/>
</logic:notEqual>

</nested:form>
</body>
</html>