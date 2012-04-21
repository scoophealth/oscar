<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="org.oscarehr.casemgmt.model.*"%>
<%@page import="java.util.ArrayList"%>

<ul>
	<c:forEach var="issue" items="${issueList}">
		<li id="<c:out value="${issue.id}"/>"><c:out
			value="${issue.code} - ${issue.description}" /></li>
	</c:forEach>
</ul>
