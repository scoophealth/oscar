<%@ include file="/taglibs.jsp" %>
<%@ page import="org.caisi.casemgmt.model.*" %>
<%@ page import="org.caisi.casemgmt.web.formbeans.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>


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
		<a <%= styleColor%> target="_blank" href="../../oscarRx/StaticScript.jsp?gcn=<c:out value="${prescription.GCN_SEQNO}"/>&cn=<c:out value="${prescription.customName}"/>" >
		<c:out value="${prescription.drug_special}"/>
		</a>
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