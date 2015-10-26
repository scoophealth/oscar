
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
<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@page import="org.oscarehr.util.WebUtilsOld"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.oscarehr.common.model.RemoteReferral"%>
<%@ include file="/taglibs.jsp"%>
<%@ page import="org.oscarehr.PMmodule.model.*"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>

<%
String vacancyId = request.getParameter("vacancyId");
%>

<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@page import="org.oscarehr.util.WebUtils"%><script>
	

	function do_referral() {
		var form = document.clientManagerForm;
		form.method.value='refer';
		form.submit();
	}
	
	function do_cancel() {
		var form = document.clientManagerForm;
		form.action='./VacancyClientMatch.do?vacancyId=<%=vacancyId%>';
		form.submit();
	}
</script>

<%=WebUtilsOld.popErrorMessagesAsHtml(session)%>

<c:if test="${remoteReferrals!=null}">
	<br />
	<br />
	<div class="tabs" id="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Programs">Remote Referrals</th>
		</tr>
	</table>
	</div>
	<display:table class="simple" cellspacing="2" cellpadding="3"
		id="remoteReferral" name="remoteReferrals" export="false" pagesize="0"
		requestURI="/PMmodule/ClientManager.do">
		<%
			RemoteReferral temp = (RemoteReferral) pageContext.getAttribute("remoteReferral");
		%>
		<display:setProperty name="paging.banner.placement" value="bottom" />
		<display:column	title="Facility / Program">
			<%=temp.getReferredToFacilityName()%> / <%=temp.getReferredToProgramName()%>
		</display:column>
		<display:column title="Referral Date">
			<%
				String date=DateFormatUtils.ISO_DATETIME_FORMAT.format(temp.getReferalDate());
				date=date.replace("T", " ");
			%>
			<%=date%>
		</display:column>
		<display:column title="Referring Provider">
			<%
				String providerName=temp.getReferringProviderNo();
				ProviderDao providerDao=(ProviderDao)SpringUtils.getBean("providerDao");
				Provider provider=providerDao.getProvider(temp.getReferringProviderNo());
				if (provider!=null) providerName=provider.getFormattedName();
			%>
			<%=providerName%>
		</display:column>
		<display:column property="reasonForReferral" title="Reason for referral" />
		<display:column property="presentingProblem" title="Present Problems" />
	</display:table>
</c:if>
<br />
<br />

<html:hidden property="program.id" />
<html:hidden property="referral.remoteFacilityId" />
<html:hidden property="referral.remoteProgramId" />
<html:hidden property="program.name" />
<html:hidden property="vacancyId"  value="<%=vacancyId%>"/>
<br />
<c:if test="${requestScope.do_refer != null}">
	<table class="b" border="0" width="100%">
		<tr>
			<th style="color: black">Program Name</th>
			<th style="color: black">Type</th>
			<th style="color: black">Participation</th>
			<th style="color: black">Phone</th>
			<th style="color: black">Email</th>
		</tr>
		<tr>
			<td><c:out value="${program.name }" /></td>
			<td><c:out value="${program.type }" /></td>
			<td><c:out value="${program.numOfMembers}" />/<c:out
				value="${program.maxAllowed}" /> (<c:out
				value="${program.queueSize}" /> waiting)</td>
			<td><c:out value="${program.phone }" /></td>
			<td><c:out value="${program.email }" /></td>
		</tr>
	</table>
</c:if>
<br />
<c:if test="${requestScope.do_refer != null}">
	<table width="100%" border="1" cellspacing="2" cellpadding="3">
		<tr class="b">
			<td width="20%">Reason for referral:</td>
			<td><html:textarea cols="50" rows="7" property="referral.notes" /></td>
		</tr>
		<tr class="b">
			<td width="20%">Presenting Problems:</td>
			<td><html:textarea cols="50" rows="7"
				property="referral.presentProblems" /></td>
		</tr>
			
		<c:if test="${program.type eq 'Bed' }">
			<!-- <c:if test="${requestScope.temporaryAdmission == true}"> -->
			<caisi:isModuleLoad moduleName="pmm.refer.temporaryAdmission.enabled">
				<tr class="b">
					<td width="20%">Request Temporary Admission:</td>
					<td><html:checkbox property="referral.temporaryAdmission" /></td>
				</tr>
			</caisi:isModuleLoad>
			<!-- </c:if> -->
		</c:if>
		<tr class="b">
			<td colspan="2"><input type="button" value="Process Referral"
				onclick="do_referral()" /> 
				
				<input type="button" value="Cancel"
				onclick="do_cancel()" /></td>
		</tr>
	</table>
</c:if>
