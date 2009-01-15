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

<%-- Updated by Eugene Petruhin on 16 dec 2008 while fixing #2434234 --%>

<%@ include file="/casemgmt/taglibs.jsp" %>
<%@ page import="org.oscarehr.casemgmt.model.*" %>
<%@ page import="org.oscarehr.casemgmt.web.formbeans.*" %>
<% response.setHeader("Cache-Control","no-cache");%>

Prescriptions
<table width="100%" border="0"  cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
<tr class="title">
	<td>Start Date</td>
	<td>Prescription Details</td>
</tr>
<c:forEach var="prescription" items="${Prescriptions}">
	<tr>
		<td bgcolor="white" >
			<c:if test="${prescription.expired}">
			*
			</c:if>
			<fmt:formatDate pattern="MM/dd/yy" value="${prescription.date_prescribed}"/>
		</td>
		
		<%String styleColor=""; %>
		<c:if test="${!prescription.expired && prescription.drug_achived}">
		<%styleColor="style=\"color:red;text-decoration: line-through;\"";%>
		</c:if>
		<c:if test="${prescription.expired && prescription.drug_achived}">
		<%styleColor="style=\"text-decoration: line-through;\"";%>
		</c:if>
		<c:if test="${!prescription.expired && !prescription.drug_achived}">
		<%styleColor="style=\"color:red;\"";%>
		</c:if>
		<td bgcolor="white" >
		
		<caisirole:SecurityAccess accessName="prescription Write" accessType="access" providerNo='<%=request.getParameter("providerNo")%>' demoNo='<%=request.getParameter("demographicNo")%>' programId='<%=(String)session.getAttribute("case_program_id")%>'>
			<a <%= styleColor%> target="_blank" href="../oscarRx/StaticScript.jsp?gcn=<c:out value="${prescription.GCN_SEQNO}"/>&cn=<c:out value="${prescription.customName}"/>" >
				<c:out value="${prescription.drug_special}"/>
			</a>
		</caisirole:SecurityAccess>
		
		<caisirole:SecurityAccess accessName="prescription Write" accessType="access" providerNo='<%=request.getParameter("providerNo")%>' demoNo='<%=request.getParameter("demographicNo")%>' programId='<%=(String)session.getAttribute("case_program_id")%>' reverse="true">
			<span <%= styleColor%> ><c:out value="${prescription.drug_special}"/></span>
		</caisirole:SecurityAccess>
		
		
		</td>
	</tr>
</c:forEach>
</table>
<c:if test="${sessionScope.caseManagementViewForm.prescipt_view!='all'}">
<span style="text-decoration: underline;cursor:pointer;color: blue" onclick="document.caseManagementViewForm.prescipt_view.value='all';document.caseManagementViewForm.method.value='setPrescriptViewType';document.caseManagementViewForm.submit(); return false;" >show all</span>
</c:if>
<c:if test="${sessionScope.caseManagementViewForm.prescipt_view=='all'}">
<span style="text-decoration: underline;cursor:pointer;color: blue" onclick="document.caseManagementViewForm.prescipt_view.value='current';document.caseManagementViewForm.method.value='setPrescriptViewType';document.caseManagementViewForm.submit(); return false;" >show current</span>
</c:if>
<br>
<span> *expired medications in blue,</span><span style="color:red">current medications in red </span> 