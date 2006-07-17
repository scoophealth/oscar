<%@ include file="/taglibs.jsp" %>
<%@ page import="org.caisi.PMmodule.web.formbean.*" %>
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
		
		<html:form action="/PMmodule/ClientManager.do">
		
			<html:hidden property="view.tab"/>
			<input type="hidden" name="id" value="<c:out value="${requestScope.id}"/>"/>
			<input type="hidden" name="method" value="edit"/>
			<html:hidden property="client.demographicNo"/>

			<script>
				function clickTab(name) {
					document.clientManagerForm.method.value='edit';
					document.clientManagerForm.elements['view.tab'].value=name;
					document.clientManagerForm.submit();
				}
			</script>
	
			<table width="100%">
				<tr><td  style="text-align: right;" align="right"><c:out value="${client.formattedName }"/></td></tr>
			</table>		
			
			<div class="tabs" id="tabs">
				<%
					String selectedTab = request.getParameter("view.tab");
					if(selectedTab==null || selectedTab.trim().equals("")) {
						selectedTab=ClientManagerFormBean.tabs[0];
					}
				%>	
				<table cellpadding="0" cellspacing="0" border="0">
					<tr>
					<% 
						boolean admin = false;
						if(session.getAttribute("userrole") != null && 
								((String)session.getAttribute("userrole")).indexOf("admin") != -1) {
							admin=true;
						}%>
						<% for(int x=0;x<ClientManagerFormBean.tabs.length;x++) {%>
							<%if(!admin && ClientManagerFormBean.tabs[x].equalsIgnoreCase("refer")) { 
								Boolean b =(Boolean) request.getAttribute("isInProgramDomain");
								if(b == null) {
									continue;
								}
							} %>
							<%if(ClientManagerFormBean.tabs[x].equals(selectedTab)) { %>
								<td style="background-color: #555;"><a href="javascript:void(0)" onclick="javascript:clickTab('<%=ClientManagerFormBean.tabs[x] %>'); return false;"><%=ClientManagerFormBean.tabs[x] %></a></td>
							<%} else { %>
								<td><a href="javascript:void(0)" onclick="javascript:clickTab('<%=ClientManagerFormBean.tabs[x] %>');return false;"><%=ClientManagerFormBean.tabs[x] %></a></td>
							<% } %>
						<% } %>
					</tr>
				</table>
			</div>
		
		<br/>
		<%@ include file="/messages.jsp"%>
		<br/>
				
		<jsp:include page="<%="/PMmodule/ClientManager/"+selectedTab.toLowerCase().replaceAll(" ","_") + ".jsp"%>"/>
		
		</html:form>

