<%@ include file="/taglibs.jsp" %>
<%@ page import="org.caisi.PMmodule.web.formbean.*" %>
<html:html>
 <head>
    <html:base />
    <title>Program Management Module</title>
   
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
		<link rel="stylesheet" type="text/css"  href='<html:rewrite page="/css/intakeC.css" />' >
		<script language="JavaScript" src='<html:rewrite page="/js/IntakeC.js" />'  ></script>
  </head> 
  <body>		
		<html:form action="/PMmodule/IntakeC.do">
		
			<html:hidden property="view.tab"/>
			<input type="hidden" name="id" value="<c:out value="${requestScope.id}"/>"/>
			<input type="hidden" name="method" value="refresh"/>
			<html:hidden property="intake.demographicNo"/>
			<input type="hidden" name="demographicNo" value="<c:out value="${requestScope.demographicNo}"/>"/>
			<script>
				function clickTab(name) {
					document.intakeCForm.method.value='refresh';
					document.intakeCForm.elements['view.tab'].value=name;
					document.intakeCForm.submit();
				}
			</script>
	
			<table width="100%">
				<tr><td  style="text-align: right;" align="right"><c:out value="${client.formattedName }"/></td></tr>
			</table>		
			
			<div class="tabs" id="tabs">
				<%
					String selectedTab = request.getParameter("view.tab");
					if(selectedTab==null || selectedTab.trim().equals("")) {
						selectedTab=IntakeCFormBean.tabs[0];
					}
				%>	
				<table cellpadding="0" cellspacing="0" border="0">
					<tr>
						<% for(int x=0;x<IntakeCFormBean.tabs.length;x++) {%>
							<%if(IntakeCFormBean.tabs[x].equals(selectedTab)) { %>
								<td style="background-color: #555;"><a href="javascript:void(0)" onclick="javascript:clickTab('<%=IntakeCFormBean.tabs[x] %>'); return false;"><%=IntakeCFormBean.tabs[x] %><br/><%=IntakeCFormBean.getTabDescr()[x]%></a></td>
							<%} else { %>
								<td><a href="javascript:void(0)" onclick="javascript:clickTab('<%=IntakeCFormBean.tabs[x] %>');return false;"><%=IntakeCFormBean.tabs[x] %><br/><%=IntakeCFormBean.getTabDescr()[x]%></a></td>
							<% } %>
						<% } %>
					</tr>
				</table>
			</div>
		
		<br/>
		<%@ include file="/messages.jsp"%>
		<br/>
				
		<jsp:include page="<%="/PMmodule/IntakeC/"+selectedTab.toLowerCase().replaceAll(" ","_") + ".jsp"%>"/>
		
		</html:form>
	</body>
</html:html>
