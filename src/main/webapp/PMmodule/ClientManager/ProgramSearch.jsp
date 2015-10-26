
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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%-- Updated by Eugene Petruhin on 10 dec 2008 while fixing #2389527 --%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="org.oscarehr.util.SpringUtils"%>

<%@page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager"%>
<%@page import="org.oscarehr.caisi_integrator.ws.CachedFacility"%>
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Program Search</title>
<link href="<html:rewrite page='/css/tigris.css'/>" rel="stylesheet"
	type="text/css" />
<link href="<html:rewrite page='/css/displaytag.css'/>" rel="stylesheet"
	type="text/css" />
</head>

<script type="text/javascript">
		var gender='<%=request.getSession().getAttribute("clientGender")%>';
		var age=<%=request.getSession().getAttribute("clientAge")%>;
		
		var programMaleOnly=<%=session.getAttribute("programMaleOnly")%>;
        var programFemaleOnly=<%=session.getAttribute("programFemaleOnly")%>;
        var programTransgenderOnly=<%=session.getAttribute("programTransgenderOnly")%>;

		<%=session.getAttribute("programAgeValidationMethod")%>

		function error(msg) {
			alert(msg);
			return false;
		}

if (!Array.prototype.indexOf)
{
  Array.prototype.indexOf = function(elt /*, from*/)
  {
    var len = this.length;

    var from = Number(arguments[1]) || 0;
    from = (from < 0)
         ? Math.ceil(from)
         : Math.floor(from);
    if (from < 0)
      from += len;

    for (; from < len; from++)
    {
      if (from in this &&
          this[from] === elt)
        return from;
    }
    return -1;
  };
}


		function selectProgram(id) {
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
		
			if (!validAgeRangeForProgram(programId,age))
			{
				return error("This client does not meet the age range requirements for this program.");
			}
		
			opener.document.<%=request.getParameter("formName")%>.elements['<%=request.getParameter("formElementId")%>'].value=id;
			var facilityId = opener.document.<%=request.getParameter("formName")%>.elements['referral.remoteFacilityId'];
			if (facilityId) {
				facilityId.value=null;
			}
			var programId = opener.document.<%=request.getParameter("formName")%>.elements['referral.remoteProgramId'];
			if (programId) {
				programId.value=null;
			}
			
			<% if(request.getParameter("submit") != null && request.getParameter("submit").equals("true")) { %>
				opener.document.<%=request.getParameter("formName")%>.submit();
			<% } %>
			
			self.close();
		}
		
		function selectRemoteProgram(facilityId, facilityProgramId) {
			
			opener.document.<%=request.getParameter("formName")%>.elements['<%=request.getParameter("formElementId")%>'].value=0;
			opener.document.<%=request.getParameter("formName")%>.elements['referral.remoteFacilityId'].value=facilityId;
			opener.document.<%=request.getParameter("formName")%>.elements['referral.remoteProgramId'].value=facilityProgramId;
			
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
		<th title="Programs">Local Facility Search Results</th>
	</tr>
</table>
</div>

<display:table class="simple" cellspacing="2" cellpadding="3"
	id="program" name="programs" pagesize="200"
	requestURI="/PMmodule/ClientManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:column sortable="true" title="Name">
		<a href="#javascript:void(0);"
			onclick="selectProgram('<c:out value="${program.id}" />');"><c:out
			value="${program.name}" /></a>
	</display:column>
	<display:column property="type" sortable="true" title="Type"></display:column>
	<display:column sortable="false" title="Participation">
		<c:out value="${program.numOfMembers}" />/<c:out
			value="${program.maxAllowed}" />&nbsp;(<c:out
			value="${program.queueSize}" /> waiting)
			</display:column>
	<display:column property="description" sortable="false"
		title="Description"></display:column>
</display:table>

<c:if test="${remotePrograms!=null}">
	<br />
	<br />
	<div class="tabs" id="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Programs">Remote Search Results</th>
		</tr>
	</table>
	</div>

	<display:table class="simple" cellspacing="2" cellpadding="3"
		id="program" name="remotePrograms" pagesize="200"
		requestURI="/PMmodule/ClientManager.do">
		<display:column sortable="true" title="Name">
			<a href="#javascript:void(0);"
				onclick="selectRemoteProgram('<c:out value="${program.facilityIdIntegerCompositePk.integratorFacilityId}" />','<c:out value="${program.facilityIdIntegerCompositePk.caisiItemId}" />');"><c:out
				value="${program.name}" /></a>
		</display:column>
		<display:column title="Facility Name">
			<c:set var="integratorFacilityId" value="${program.facilityIdIntegerCompositePk.integratorFacilityId}" scope="request" />
			<%
				Integer integratorFacilityId=(Integer)request.getAttribute("integratorFacilityId");
				CachedFacility cachedFacility=CaisiIntegratorManager.getRemoteFacility(loggedInInfo, loggedInInfo.getCurrentFacility(),integratorFacilityId);
			%>
			<%=cachedFacility.getName()%>
		</display:column>
		<display:column property="type" sortable="true" title="Type"></display:column>
		<display:column property="description" sortable="false"
			title="Description"></display:column>
	</display:table>
</c:if>

</body>
</html:html>
