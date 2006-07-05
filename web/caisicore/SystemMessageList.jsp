<%@ include file="/taglibs.jsp"%>

<html>
	<head>
		<style type="text/css">
		/* <![CDATA[ */
		@import "<html:rewrite page="/css/core.css" />";
		/*  ]]> */
		</style>
	
		<title>System Messages</title>
	</head>
	
	<body>
	<table border="0" cellspacing="0" cellpadding="1" width="100%" bgcolor="#CCCCFF">
	  <tr class="subject"><th colspan="4">CAISI</th></tr>
	  	<tr>
            <td class="searchTitle" colspan="4">System Messages</td>
		</tr>
	</table>

	<br/>

		<%@ include file="messages.jsp" %>
	
	<br/>	
		<table width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
			<tr class="title">
				<td></td>
				<td>Creation Date</td>
				<td>Expiry Date</td>
				<td>Message</td>
			</tr>
			
			<c:forEach var="msg" items="${ActiveMessages}" varStatus="status">
				<%String style="color:black;"; String bgcolor="white";%>
				<c:if test="${msg.expired}">
					<%style="color:red;"; %>
				</c:if>
			
				<c:if test="${status.count % 2 != 0}">
			    	<%bgcolor="#EEEEFF";%>
			    </c:if>
			
				
				
				<tr style="<%=style %>" bgcolor="<%=bgcolor %>">
					<td valign="middle">
						<a href="SystemMessage.do?method=edit&id=<c:out value="${msg.id}"/>"><img border="0" src="images/edit.jpg"/></a>
					</td>
					<td><c:out value="${msg.formattedCreationDate}"/></td>
					<td><c:out value="${msg.formattedExpiryDate}"/></td>
					<td><c:out value="${msg.message}"/></td>
				</tr>
			</c:forEach>
		</table>
		
		<br/>
		
		<table>
		<tr>
			<td><input type="button" value="Create New Message" onclick="location.href='SystemMessage.do?method=edit'"/></td>
		</tr>
		</table>

	</body>
</html>
