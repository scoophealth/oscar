
<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>



<%@ include file="/taglibs.jsp"%>
<%@ page import="org.oscarehr.PMmodule.web.formbean.*"%>
<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<title>Street Health Mental Health Intake - <c:out
	value="${intakeCForm.map.view2.version}" /></title>
<style>
.sortable {
	background-color: #555;
	color: #555;
}

.b th {
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
<link rel="stylesheet" type="text/css"
	href='<html:rewrite page="/css/intakeC.css" />'>
<script language="JavaScript"
	src='<html:rewrite page="/js/IntakeC.js" />'></script>
</head>
<body>
<html:form action="/PMmodule/IntakeC.do">
	<html:hidden property="bean.clientId" />
	<html:hidden property="view2.tab" />
	<input type="hidden" name="id"
		value="<c:out value="${requestScope.id}"/>" />
	<input type="hidden" name="method" value="refresh" />
	<html:hidden property="intake.demographicNo" />
	<input type="hidden" name="demographicNo"
		value="<c:out value="${requestScope.demographicNo}"/>" />
	<script>
		function clickTab(name) {
			document.intakeCForm.method.value='refresh';
			document.intakeCForm.elements['view2.tab'].value=name;
			document.intakeCForm.submit();
		}
	</script>
	<table width="100%">
		<tr>
			<td style="text-align: right;" align="right"><c:out
				value="${client.formattedName}" /></td>
		</tr>
	</table>
	<center class="style51">Street Health Mental Health Intake - <c:out
		value="${intakeCForm.map.view2.version}" /></center>
	<div class="tabs" id="tabs">
	<%
		String selectedTab = request.getParameter("view2.tab");
		if (selectedTab == null || selectedTab.trim().equals("")) {
			selectedTab = IntakeCFormBean.tabs[0];
		}
	%>
	<table cellpadding="0" cellspacing="0" border="0">
		<tr>
			<%
			for (int x = 0; x < IntakeCFormBean.tabs.length; x++) {
			%>
			<%
			if (IntakeCFormBean.tabs[x].equals(selectedTab)) {
			%>
			<td style="background-color: #555;"><a href="javascript:void(0)"
				onclick="javascript:clickTab('<%=IntakeCFormBean.tabs[x] %>'); return false;"><%=IntakeCFormBean.tabs[x]%><br />
			<%=IntakeCFormBean.getTabDescr()[x]%></a></td>
			<%
			} else {
			%>
			<td><a href="javascript:void(0)"
				onclick="javascript:clickTab('<%=IntakeCFormBean.tabs[x] %>');return false;"><%=IntakeCFormBean.tabs[x]%><br />
			<%=IntakeCFormBean.getTabDescr()[x]%></a></td>
			<%
			}
			%>
			<%
			}
			%>
		</tr>
	</table>
	</div>
	<%@ include file="/common/messages.jsp"%>
	<jsp:include
		page='<%="/PMmodule/IntakeC/"+selectedTab.toLowerCase().replaceAll(" ","_") + ".jsp"%>' />
</html:form>
</body>
</html:html>
