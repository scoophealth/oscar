<%@ include file="/taglibs.jsp" %>

<html:html>
	<head>
		<title>Staff Manager</title>
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
			function ConfirmDelete(name)
			{
				if(confirm("Are you sure you want to delete " + name + " ?")) {
					return true;
				}
				return false;
			}

			function sort() {
			
			}
		</script>

	</head>

	<body marginwidth="0" marginheight="0">

		<br/>
		<%@ include file="/common/messages.jsp"%>
		<br/>			
			
		<div class="tabs" id="tabs">
			<table cellpadding="3" cellspacing="0" border="0">
		
				<tr>
					<th title="Programs">Staff List</th>
		
				</tr>
			</table>
		</div>

		<display:table class="simple" cellspacing="2" cellpadding="3" id="provider" name="providers" export="false" pagesize="0" requestURI="/PMmodule/StaffManager.do">
		  <display:setProperty name="paging.banner.placement" value="bottom"/>
		  <display:setProperty name="paging.banner.item_name" value="provider"/>
		  <display:setProperty name="paging.banner.items_name" value="providers"/>
		  <display:setProperty name="basic.msg.empty_list" value="No providers found."/>
		  <display:column sortable="false" title="">  
			<a href="<html:rewrite action="/PMmodule/StaffManager.do"/>?method=edit&id=<c:out value="${provider.providerNo}" />">
				Edit
			</a>
		  </display:column>
		  <display:column property="formattedName" sortable="true" title="Name"/>
		  <display:column property="phone"  sortable="true" title="Phone" />
		  <display:column property="workPhone"  sortable="true" title="Work Phone" />
		  <display:column property="providerType"  sortable="true" title="OSCAR Type" />
		</display:table>

	</body>
</html:html>
