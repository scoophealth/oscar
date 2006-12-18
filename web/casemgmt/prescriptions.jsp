<%@ include file="/casemgmt/taglibs.jsp" %>
<%@ page import="org.oscarehr.casemgmt.model.*" %>
<%@ page import="org.oscarehr.casemgmt.web.formbeans.*" %>


Prescriptions
<table width="100%" border="0"  cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
<tr class="title">
	<td>Start Date</td>
	<td>Prescription Details</td>
</tr>
<c:forEach var="prescription" items="${Prescriptions}">
	<tr>
		<td bgcolor="white" ><fmt:formatDate pattern="MM/dd/yy" value="${prescription.date_prescribed}"/></td>
		
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
		
		<caisirole:SecurityAccess accessName="prescription Write" accessType="access" providerNo="<%=request.getParameter("providerNo")%>" demoNo="<%=request.getParameter("demographicNo")%>" programId="<%=(String)session.getAttribute("case_program_id")%>">
			<a <%= styleColor%> target="_blank" href="../oscarRx/StaticScript.jsp?gcn=<c:out value="${prescription.GCN_SEQNO}"/>&cn=<c:out value="${prescription.customName}"/>" >
				<c:out value="${prescription.drug_special}"/>
			</a>
		</caisirole:SecurityAccess>
		
		<caisirole:SecurityAccess accessName="prescription Write" accessType="access" providerNo="<%=request.getParameter("providerNo")%>" demoNo="<%=request.getParameter("demographicNo")%>" programId="<%=(String)session.getAttribute("case_program_id")%>" reverse="true">
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
<span> *expired medications in black,</span><span style="color:red">current medications in red </span> 