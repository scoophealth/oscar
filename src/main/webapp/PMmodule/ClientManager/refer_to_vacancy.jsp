
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

<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@page import="org.oscarehr.util.WebUtils"%>

<script>
	function do_referral() {
		var form = document.clientManagerForm;
		form.method.value='refer';
		var programVacancy = document.getElementsByName('selectVacancy');
		var programId,vacancyId;
		var i;
		var checked = false;
        for(i=0;i<programVacancy.length;i++){
            if(programVacancy[i].checked){
            	var arrayList = programVacancy[i].value.split("|");
               programId = arrayList[0];
               vacancyId = arrayList[1];
               checked = true;
               break;
            }
        }
        if(!checked){
            alert("you must at least choose one program");
            return;
        }
		document.clientManagerForm.elements['program.id'].value=programId;
		document.clientManagerForm.elements['program.vacancyId'].value=vacancyId;
		form.submit();
	}
</script>

<%=WebUtilsOld.popErrorMessagesAsHtml(session)%>

<html:hidden property="program.id" />
<html:hidden property="program.vacancyId" />
<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">Referrals</th>
	</tr>
</table>
</div>
<display:table class="simple" cellspacing="2" cellpadding="3"
	id="referral" name="referrals" export="false" pagesize="0"
	requestURI="/PMmodule/ClientManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	
	<display:column property="selectVacancy" sortable="true"
		title="Vacancy Name" />
	<display:column property="vacancyTemplateName" sortable="true"
		title="Vacancy Template Name" />
	<display:column property="referralDate" sortable="true"
		title="Referral Date" />
	<display:column property="programName" sortable="true"
		title="Associated Program Name" />
	<display:column property="providerFormattedName" sortable="true"
		title="Referring Provider" />
	<display:column property="status" sortable="true" title="Status" />
	<display:column sortable="true" title="Days in Queue">
		<%
		ClientReferral temp = (ClientReferral) pageContext.getAttribute("referral");
		Date referralDate = temp.getReferralDate();
		Date currentDate = new Date();
		String numDays = "";

		if (!temp.getStatus().equals("pending")) {
			long diff = currentDate.getTime() - referralDate.getTime();
			diff = diff / 1000; //seconds;
			diff = diff / 60; //minutes;
			diff = diff / 60; //hours
			diff = diff / 24; //days
			numDays = String.valueOf(diff);
		} else {
			numDays = "0";
		}
		%>
		<%=numDays%>
	</display:column>
	<!--  display:column property="notes" sortable="true" title="Notes" /> -->
	<display:column property="notes" sortable="true"
		title="Reason for referral" />
	<display:column property="presentProblems" sortable="true"
		title="Present Problems" />
</display:table>

<br />
<br />

<display:table class="simple" cellspacing="2" cellpadding="3"
	id="program" name="programs" pagesize="200"
	requestURI="/PMmodule/ClientManager.do">
	<html:hidden property="program.id" />
	<html:hidden property="program.vacancyId" />
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<%
	Program p = (Program) pageContext.getAttribute("program");
	String selectV = String.valueOf(p.getId()).concat("|").concat(String.valueOf(p.getVacancyId()));
	%>
	<display:column sortable="false" title="Select">
		<input type="radio"  name="selectVacancy"  value="<%=selectV %>">
	</display:column>	
	
	<display:column sortable="true" title="Vacancy Name">
			<a href="">
				<c:out value="${program.vacancyName}" />
			</a>
	</display:column>
	<display:column sortable="true" title="Vacancy Template Name">
			<a href="">
				<c:out value="${program.vacancyTemplateName}" />
			</a>
	</display:column>
	<display:column property="dateCreated" sortable="false" title="Created"></display:column>
	<display:column sortable="true" title="Matches">
		<c:if test="${program.matches !=0}">
			<a href="<html:rewrite action="/PMmodule/VacancyClientMatch.do"/>?vacancyId=<c:out value="${program.vacancyId}" />">
				<c:out value="${program.matches}" />
			</a>
		</c:if>
		<c:if test="${program.matches ==0}">
				<c:out value="${program.matches}" />
		</c:if>			
	</display:column>
	
	<display:column sortable="true" title="Program Name">
			<a href="<html:rewrite action="/PMmodule/ProgramManagerView.do"/>?id=<c:out value="${program.id}" />">
				<c:out value="${program.name}" /> 
			</a>		
	</display:column>	
</display:table>
<table width="100%" border="1" cellspacing="2" cellpadding="3">
		<tr class="b">
			<td colspan="2">
				<input type="button" value="Process Referral" onclick="do_referral()" /> 
				<input type="button" value="Cancel" onclick="document.clientManagerForm.submit()" />
			</td>
		</tr>
</table>
		