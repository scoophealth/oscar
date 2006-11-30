<%@ include file="/taglibs.jsp"%>

<title>MyIssues ~ Issue List</title>
 <style type="text/css">
        /* <![CDATA[ */
        @import "<html:rewrite page="/css/core.css" />";
        /*  ]]> */
 </style>

<button onclick="location.href='issueAdmin.do?method=edit'">Add Issue</button>
<table border="0"  cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
<thead>
<tr class="title">
<!--
    <th><bean:message key="issueAdmin.id"/></th>
-->
    <th><bean:message key="issueAdmin.code"/></th>
    <th><bean:message key="issueAdmin.description"/></th>
    <th><bean:message key="issueAdmin.role"/></th>
<!-- 
    <th><bean:message key="issueAdmin.update_date"/></th>
-->
</tr>
</thead>
<tbody>
<c:forEach var="issueAdmin" items="${issueAdmins}" varStatus="status">
<c:choose>
    <c:when test="${status.count % 2 == 0}"><tr class="even"></c:when>
    <c:otherwise><tr class="odd"></c:otherwise>
</c:choose>

    <td><a href="issueAdmin.do?method=edit&amp;id=<c:out value="${issueAdmin.id}"/>"><c:out value="${issueAdmin.code}"/></a></td>
    <td><c:out value="${issueAdmin.description}"/></td>
    <td><c:out value="${issueAdmin.role}"/></td>
<!--
    <td><c:out value="${issueAdmin.update_date}"/></td>
-->
</tr>
</c:forEach>
</tbody>
</table>
