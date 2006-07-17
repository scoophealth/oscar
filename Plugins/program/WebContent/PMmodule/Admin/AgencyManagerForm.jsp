<%@ include file="/taglibs.jsp" %>
<%@ page import="org.caisi.PMmodule.web.formbean.*" %>

<html:html>
	<head>
		<title></title>
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
	</head>

	<body>
		<html:form action="/PMmodule/AgencyManager">
			<html:hidden property="view.tab"/>
			<input type="hidden" name="id" value="<c:out value="${requestScope.id}"/>"/>
			<input type="hidden" name="method" value="edit"/>
			<html:hidden property="agency.id"/>
			<script>
				function clickTab(name) {
					document.agencyManagerForm.method.value='edit';
					document.agencyManagerForm.elements['view.tab'].value=name;
					document.agencyManagerForm.submit();
				}
			</script>
		
			<div class="tabs" id="tabs">
				<%
					String selectedTab = request.getParameter("view.tab");
					if(selectedTab==null || selectedTab.trim().equals("")) {
						selectedTab=AgencyManagerViewFormBean.tabs[0];
					}
				%>	
				<table cellpadding="0" cellspacing="0" border="0">
					<tr>
						<% for(int x=0;x<AgencyManagerViewFormBean.tabs.length;x++) {%>
							<%
							if(AgencyManagerViewFormBean.tabs[x].equalsIgnoreCase("Community")) {
								break;
							}
							%>
							<%if(AgencyManagerViewFormBean.tabs[x].equals(selectedTab)) { %>
								<td style="background-color: #555;"><a href="javascript:void(0)" onclick="javascript:clickTab('<%=AgencyManagerViewFormBean.tabs[x] %>'); return false;"><%=AgencyManagerViewFormBean.tabs[x] %></a></td>
							<%} else { %>
								<td><a href="javascript:void(0)" onclick="javascript:clickTab('<%=AgencyManagerViewFormBean.tabs[x] %>');return false;"><%=AgencyManagerViewFormBean.tabs[x] %></a></td>
							<% } %>
						<% } %>
					</tr>
				</table>
			</div>
			
			<jsp:include page="<%="/PMmodule/Admin/AgencyEdit/"+selectedTab.toLowerCase().replaceAll(" ","_") + ".jsp"%>"/>
			
		</html:form>
	</body>
</html:html>
