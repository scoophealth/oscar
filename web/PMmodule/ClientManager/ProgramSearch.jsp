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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
	<head>
		<title>Program Search</title>
		<link href="<html:rewrite page='/css/tigris.css'/>" rel="stylesheet" type="text/css" />
		<link href="<html:rewrite page='/css/displaytag.css'/>" rel="stylesheet" type="text/css" />
	</head>
	
	<script type="text/javascript">
		var gender='<%=request.getSession().getAttribute("clientGender")%>';
		
		var programMaleOnly=<%=session.getAttribute("programMaleOnly")%>;
        var programFemaleOnly=<%=session.getAttribute("programFemaleOnly")%>;
        var programTransgenderOnly=<%=session.getAttribute("programTransgenderOnly")%>;

		function error(msg) {
			alert(msg);
			return false;
		}

		function selectProgram(agencyId,id,type) {
		
			var programId=Number(id);
			if (gender == 'M')
			{
				if (programFemaleOnly.indexOf(programId)>=0 ||  programTransgenderOnly.indexOf(programId)>=0)
				{
					return error("This gender not allowed in selected program.");
				}
			}
			if (gender == 'F')
			{
				if (programMaleOnly.indexOf(programId)>=0 ||  programTransgenderOnly.indexOf(programId)>=0)
				{
					return error("This gender not allowed in selected program.");
				}
			}
			if (gender == 'T')
			{
				if (programFemaleOnly.indexOf(programId)>=0 ||  programMaleOnly.indexOf(programId)>=0)
				{
					return error("This gender not allowed in selected program.");
				}
			}		
		
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
		
		<display:table class="simple" cellspacing="2" cellpadding="3" id="program" name="programs" pagesize="200" requestURI="/PMmodule/ClientManager.do">
			<display:setProperty name="paging.banner.placement" value="bottom" />
			<display:column sortable="true" title="Name">
				<a href="#javascript:void(0);" onclick="selectProgram('<c:out value="${program.agencyId}" />','<c:out value="${program.id}" />','<c:out value="${program.type}" />');"><c:out value="${program.name}" /></a>
			</display:column>
			<display:column property="type" sortable="true" title="Type"></display:column>
			<display:column sortable="false" title="Participation">
				<c:out value="${program.numOfMembers}" />/<c:out value="${program.maxAllowed}" />&nbsp;(<c:out value="${program.queueSize}" /> waiting)
			</display:column>
			<display:column property="descr" sortable="false" title="Description"></display:column>
		</display:table>
	</body>
</html:html>
