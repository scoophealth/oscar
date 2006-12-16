<%@ include file="/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
	<head>
		<title>Provider Search</title>
		<link href="<html:rewrite page='/css/tigris.css'/>" rel="stylesheet" type="text/css" />
		<link href="<html:rewrite page='/css/displaytag.css'/>" rel="stylesheet" type="text/css" />
	</head>
	
	<script type="text/javascript">
		function selectProvider(id,name) {
			opener.document.<%=request.getParameter("formName")%>.elements['<%=request.getParameter("formElementName")%>'].value=name;
			opener.document.<%=request.getParameter("formName")%>.elements['<%=request.getParameter("formElementId")%>'].value=id;
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
		<display:table class="simple" cellspacing="2" cellpadding="3" id="provider" name="providers" export="false" pagesize="10" requestURI="/PMmodule/ProviderSearch.do">
			<display:setProperty name="paging.banner.placement" value="bottom" />
			<display:column sortable="true" title="Name">
				<a href="#javascript:void(0);" onclick="selectProvider('<c:out value="${provider.providerNo}"/>','<c:out value="${provider.formattedName}"/>');"> <c:out value="${provider.providerNo}" /> </a>
			</display:column>
			<display:column property="lastName" sortable="true" title="Last Name" />
			<display:column property="firstName" sortable="true" title="First Name" />
			<display:column property="providerType" sortable="true" title="Type" />
		</display:table>
	</body>
</html:html>