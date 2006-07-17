<%@ include file="/taglibs.jsp" %>
<%@ page import="org.caisi.casemgmt.model.*" %>
<%@ page import="org.caisi.casemgmt.web.formbeans.*" %>

<table width="100%" border="0"  cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
<tr class="title">
	<td>Update date</td>
	<td>Allergy description</td>
	<td>Reaction</td>
</tr>
<c:forEach var="allergy" items="${Allergies}">
	<tr>
		<td bgcolor="white"><fmt:formatDate pattern="MM/dd/yy" value="${allergy.entry_date}"/></td>
		<td bgcolor="white"><c:out value="${allergy.description}"/></td>
		<td bgcolor="white"><c:out value="${allergy.reaction}"/></td>
	</tr>
</c:forEach>
</table>