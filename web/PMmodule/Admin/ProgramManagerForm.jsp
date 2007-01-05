<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->

<%@ include file="/taglibs.jsp"%>
<%@ page import="org.oscarehr.PMmodule.web.formbean.*"%>
<%@ page import="org.oscarehr.PMmodule.model.Program"%>

<%@page import="org.apache.struts.validator.DynaValidatorForm"%>
<html:form action="/PMmodule/ProgramManager">

	<html:hidden property="view.tab" />
	<input type="hidden" name="id" value="<c:out value="${requestScope.id}"/>" />
	<input type="hidden" name="method" value="edit" />
	<html:hidden property="program.id" />
	
	<c:choose>
		<c:when test="${id != null && id gt 0}">
			<script>
				function clickTab(name) {
					document.programManagerForm.method.value='edit';
					document.programManagerForm.elements['view.tab'].value=name;
					document.programManagerForm.submit();
				}
			</script>
			
			<table width="100%">
				<tr>
					<td style="text-align:right;" align="right"><c:out value="${programName}" /></td>
				</tr>
			</table>
			
			<div class="tabs">
				<%
					String selectedTab = request.getParameter("view.tab");
				
					if (selectedTab == null || selectedTab.trim().equals("")) {
						selectedTab = ProgramManagerViewFormBean.tabs[0];
					}
				%>
				<table cellpadding="0" cellspacing="0" border="0">
					<tr>
						<%
							DynaValidatorForm form = (DynaValidatorForm) session.getAttribute("programManagerForm");
							Program program = (Program) form.get("program");
							
							for (int i = 0; i < ProgramManagerViewFormBean.tabs.length; i++) {
								if (ProgramManagerViewFormBean.tabs[i].equalsIgnoreCase("Bed Check") && program.isService()) {
									break;
								}
								
								if (ProgramManagerViewFormBean.tabs[i].equals(selectedTab)) {
						%>
						<td style="background-color: #555;">
							<a href="javascript:void(0)" onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[i] %>');return false;"><%=ProgramManagerViewFormBean.tabs[i]%></a>
						</td>
						<%
								} else {
						%>
						<td>
							<a href="javascript:void(0)" onclick="javascript:clickTab('<%=ProgramManagerViewFormBean.tabs[i] %>');return false;"><%=ProgramManagerViewFormBean.tabs[i]%></a>
						</td>
						<%
								}
							}
						%>
					</tr>
				</table>
			</div>
			<%@ include file="/common/messages.jsp"%>
			<jsp:include page="<%="/PMmodule/Admin/ProgramEdit/" + selectedTab.toLowerCase().replaceAll(" ","_") + ".jsp"%>" />
		</c:when>
		<c:otherwise>
			<%@ include file="/common/messages.jsp"%>
			<jsp:include page="/PMmodule/Admin/ProgramEdit/general.jsp" />
		</c:otherwise>
	</c:choose>
</html:form>