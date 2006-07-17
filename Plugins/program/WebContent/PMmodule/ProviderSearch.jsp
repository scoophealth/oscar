<%@ include file="/taglibs.jsp" %>

<html:html>
	<head>
		<title>provider search</title>
		<style>
			.sortable {
				background-color: #555;
				color: #555;
			}
			.b th{
				border-right: 1px solid #333;
				background-color: #ddd;
				color: #ddd;
				border-left: 1px solid #fff;
			}
			.message {
				color: red;
				background-color: white;
			}
			.error {
				color: red;
				background-color: white;
			}
		</style>
		<script>
			function selectProvider(id,name) {
				opener.document.<%=request.getParameter("formName")%>.elements['<%=request.getParameter("formElementName")%>'].value=name;
				opener.document.<%=request.getParameter("formName")%>.elements['<%=request.getParameter("formElementId")%>'].value=id;
				self.close();
			}

		</script>

	</head>

	<body marginwidth="0" marginheight="0">

		<br/>
		<%@ include file="/messages.jsp"%>
		<br/>
				
		<div class="tabs" id="tabs">
			<table cellpadding="3" cellspacing="0" border="0">
		
				<tr>
					<th title="Programs">Search Results</th>
		
				</tr>
			</table>
		</div>

		<display:table class="b" border="1" cellspacing="2" cellpadding="3" width="100%" id="provider" name="providers" export="false" pagesize="10" requestURI="/PMmodule/ProviderSearch.do">
		  <display:setProperty name="paging.banner.placement" value="bottom"/>
		  <display:column sortable="true" title="Name">
		  	<a href="#javascript:void(0);" onclick="selectProvider('<c:out value="${provider.providerNo}"/>','<c:out value="${provider.formattedName}"/>');">
				<c:out value="${provider.providerNo}"/>
			</a>
		  </display:column>
		  <display:column property="lastName"  sortable="true" title="Last Name" />
		  <display:column property="firstName"  sortable="true" title="First Name" />
		  <display:column property="providerType"  sortable="true" title="Type" />
		</display:table>

	</body>
</html:html>