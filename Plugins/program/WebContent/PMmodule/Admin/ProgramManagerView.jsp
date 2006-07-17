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
	
		<html:form action="/PMmodule/ProgramManagerView">
			<html:hidden property="tab"/>
			<input type="hidden" name="id" value="<c:out value="${requestScope.id}"/>"/>
			<input type="hidden" name="method" value="view"/>
			<script>
				function clickTab(name) {
					document.programManagerViewForm.tab.value=name;
					document.programManagerViewForm.submit();
				}
			</script>
			
			<table width="100%">
				<tr><td  style="text-align: right;" align="right"><c:out value="${program.name }"/></td></tr>
			</table>		

		
			<div class="tabs" id="tabs">
				<%
					String selectedTab = request.getParameter("tab");
					if(selectedTab==null || selectedTab.trim().equals("")) {
						selectedTab=ProgramManagerViewFormBean.tabs[0];
					}
				%>	
				<table cellpadding="0" cellspacing="0" border="0">
					<tr>
						<% for(int x=0;x<ProgramManagerViewFormBean.tabs.length;x++) {%>
							<%if(ProgramManagerViewFormBean.tabs[x].equals(selectedTab)) { %>
								<td style="background-color: #555;"><a href="javascript:void(0)" onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[x] %>'); return false;"><%=ProgramManagerViewFormBean.tabs[x] %></a></td>
							<%} else { %>
								<td><a href="javascript:void(0)" onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[x] %>');return false;"><%=ProgramManagerViewFormBean.tabs[x] %></a></td>
							<% } %>
						<% } %>
					</tr>
				</table>
			</div>
			
			<jsp:include page="<%="/PMmodule/Admin/ProgramView/"+selectedTab.toLowerCase().replaceAll(" ","_") + ".jsp"%>"/>

		</html:form>
	
