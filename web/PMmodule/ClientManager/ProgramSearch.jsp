<%@ include file="/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
	<head>
		<title>Program Search</title>
		<link href="<html:rewrite page='/css/tigris.css'/>" rel="stylesheet" type="text/css" />
		<link href="<html:rewrite page='/css/displaytag.css'/>" rel="stylesheet" type="text/css" />
	</head>
	
	<script type="text/javascript">
		function selectProgram(agencyId,id,type) {
			opener.document.<%=request.getParameter("formName")%>.elements['<%=request.getParameter("formElementId")%>'].value=id;
			opener.document.<%=request.getParameter("formName")%>.elements['<%=request.getParameter("formElementAgencyId")%>'].value=agencyId;
			
			<% if(request.getParameter("submit") != null && request.getParameter("submit").equals("true")) { %>
				opener.document.<%=request.getParameter("formName")%>.submit();
			<% } %>
			
			self.close();
		}
	</script>
	
	<body marginwidth="0" marginheight="0">
		<%@ include file="/common/messages.jsp"%>
		<div class="tabs" id="tabs">
			<table cellpadding="3" cellspacing="0" border="0">
				<tr>
					<th title="Programs">Search Results</th>
				</tr>
			</table>
		</div>
		<display:table class="simple" cellspacing="2" cellpadding="3" id="program" name="programs" pagesize="10" requestURI="/PMmodule/ClientManager.do">
			<display:setProperty name="paging.banner.placement" value="bottom" />
			
			<display:column sortable="true" title="Name">
				<a href="#javascript:void(0);" onclick="selectProgram('<c:out value="${program.agencyId}" />','<c:out value="${program.id}" />','<c:out value="${program.type}" />');"><c:out value="${program.name}" /></a>
			</display:column>
			<display:column property="type" sortable="true" title="Type" />
			<display:column sortable="false" title="Participation">
				<c:out value="${program.numOfMembers}" />/<c:out value="${program.maxAllowed}" />&nbsp;(<c:out value="${program.queueSize}" /> waiting)
			</display:column>
		</display:table>
	</body>
</html:html>
