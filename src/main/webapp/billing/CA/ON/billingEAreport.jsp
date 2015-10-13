<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting,_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_report&type=_admin.reporting&type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="java.util.*,oscar.oscarReport.pageUtil.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Reconcilliation</title>
<link rel="stylesheet" href="../billing.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td height="40" width="10%" class="Header"><input type='button'
			name='print' value='<bean:message key="global.btnPrint"/>'
			onClick='window.print()'></td>
		<td width="90%" align="left" class="Header">oscar<font size="3">Billing</font>
		</td>
	</tr>
</table>

<table width="100%">
	<tr>
		<td class="Header1"><bean:write name="ReportName" /></td>
	</tr>
</table>
<table width="100%">
	<tr>
		<td><logic:present name="claimsErrors">
			<logic:iterate id="claimsError" name="claimsErrors"
				property="claimsErrorReportBeanVector">
				<logic:present name="claimsError" property="techSpec">
					<table width="100%" border="0" cellspacing="2" cellpadding="2"
						bgcolor="#CCCCFF">
						<tr>
							<td width="15%"><b>MOH Office: <bean:write
								name="claimsError" property="MOHoffice" /></b></td>
							<td width="15%"><b>Provider #: <bean:write
								name="claimsError" property="providerNumber" /> </b></td>
							<td width="11%"><b>Group #: <bean:write
								name="claimsError" property="groupNumber" /> </b></td>
							<td width="11%"><b>Opr.#: <bean:write name="claimsError"
								property="operatorNumber" /></b></td>
							<td width="11%"><b>Sp. Code: <bean:write
								name="claimsError" property="specialtyCode" /></b></td>
							<td width="11%"><b>Spec.#: <bean:write
								name="claimsError" property="techSpec" /> </b></td>
							<td width="11%"><b>Station #: <bean:write
								name="claimsError" property="stationNumber" /> </b></td>
							<td width="15%"><b>Clm Date: <bean:write
								name="claimsError" property="claimProcessDate" /></b></td>

						</tr>
					</table>
					<table width="100%" border="0" cellspacing="2" cellpadding="2"
						bgcolor="#F1E9FE">
						<tr>
							<td width="10%">Health#</td>
							<td width="6%">D.O.B</td>
							<td width="7%">Invoice #</td>
							<td width="3%">Type</td>
							<td width="9%">Ref Phy#</td>
							<td width="7%">Hosp #</td>
							<td width="9%">Admitted</td>
							<td width="5%">Claim Errors</td>
							<td width="5%">Code</td>
							<td width="6%">Fee Unit</td>
							<td width="4%">Unit</td>

							<td width="7%">Date</td>
							<td width="4%">Diag</td>
							<td width="2%">Exp.</td>
							<td width="12%">Code Error</td>
							</logic:present>
							<logic:present name="claimsError" property="patient_last">
								<table width="100%" border="0" cellspacing="2" cellpadding="2"
									bgcolor="#F1E9FE">
									<tr bgcolor="#F9F1FE">
										<td width="23%" colspan="3"><bean:write
											name="claimsError" property="patient_last" />, &nbsp;<bean:write
											name="claimsError" property="patient_first" /></td>
										<td width="3%"><bean:write name="claimsError"
											property="patient_sex" /></td>
										<td width="9%"><bean:write name="claimsError"
											property="province_code" /></td>
										<td width="65%" colspan="10"><bean:write
											name="claimsError" property="reCode1" /> &nbsp;<bean:write
											name="claimsError" property="reCode2" /> &nbsp;<bean:write
											name="claimsError" property="reCode3" /> &nbsp;<bean:write
											name="claimsError" property="reCode4" /> &nbsp;<bean:write
											name="claimsError" property="reCode5" />&nbsp;</td>
									</tr>
								</table>
							</logic:present>
							<logic:present name="claimsError" property="servicecode">
								<table width="100%" border="0" cellspacing="2" cellpadding="2"
									bgcolor="#F1E9FE">
									<tr bgcolor="#F9F1FE">
										<td width="10%"><bean:write name="claimsError"
											property="hin" /> &nbsp; <bean:write name="claimsError"
											property="ver" /></td>
										<td width="6%"><bean:write name="claimsError"
											property="dob" /></td>
										<td width="7%"><bean:write name="claimsError"
											property="account" /></td>
										<td width="3%"><bean:write name="claimsError"
											property="payee" /></td>
										<td width="9%"><bean:write name="claimsError"
											property="referNumber" /></td>
										<td width="7%"><bean:write name="claimsError"
											property="facilityNumber" /></td>
										<td width="9%"><bean:write name="claimsError"
											property="admitDate" /></td>
										<td width="5%"><bean:write name="claimsError"
											property="heCode1" /> &nbsp;<bean:write name="claimsError"
											property="heCode2" /> &nbsp;<bean:write name="claimsError"
											property="heCode3" /> &nbsp;<bean:write name="claimsError"
											property="heCode4" /> &nbsp;<bean:write name="claimsError"
											property="heCode5" />&nbsp;</td>
										<td width="5%"><bean:write name="claimsError"
											property="servicecode" /></td>
										<td width="6%"><bean:write name="claimsError"
											property="amountsubmit" /></td>
										<td width="4%"><bean:write name="claimsError"
											property="serviceno" /></td>
										<td width="7%"><bean:write name="claimsError"
											property="servicedate" /></td>
										<td width="4%"><bean:write name="claimsError"
											property="dxcode" /></td>
										<td width="2%"></td>
										<td width="12%"><bean:write name="claimsError"
											property="code1" /> &nbsp;<bean:write name="claimsError"
											property="code2" /> &nbsp;<bean:write name="claimsError"
											property="code3" /> &nbsp;<bean:write name="claimsError"
											property="code4" /> &nbsp;<bean:write name="claimsError"
											property="code5" />&nbsp;</td>
									</tr>
								</table>
							</logic:present>
							<logic:present name="claimsError" property="explain">
								<table width="100%" border="0" cellspacing="2" cellpadding="2"
									bgcolor="#F1E9FE">
									<tr>
										<td width="20%"><b>Error/Description</b></td>
										<td width="20%"><bean:write name="claimsError"
											property="explain" /></td>
										<td width="60%"><bean:write name="claimsError"
											property="error" /></td>
									</tr>
									</logic:present>
									<logic:present name="claimsError" property="header1Count">
										</tr>
								</table>
								<table width="100%" border="0" cellspacing="2" cellpadding="2"
									bgcolor="#CCCCFF">
									<tr>
										<td width="20%"><b>Record Counts: [ </b></td>
										<td width="20% "><b>Header 1: <bean:write
											name="claimsError" property="header1Count" /> </b></td>
										<td width="20%"><b>Header 2: <bean:write
											name="claimsError" property="header2Count" /></b></td>
										<td width="20%"><b>Item: <bean:write
											name="claimsError" property="itemCount" /> </b></td>
										<td width="20%"><b>Message: <bean:write
											name="claimsError" property="messageCount" /> ]</b></td>
									</tr>
								</table>
							</logic:present>
							</logic:iterate>
							</logic:present>


							<logic:present name="batchAcks">
								<tr>
									<td class="fieldName" width="5%">Batch #</td>
									<td class="fieldName" width="5%">Oper.#</td>
									<td class="fieldName" width="7%">Provider #</td>
									<td class="fieldName" width="4%">Group#</td>
									<td class="fieldName" width="7%">Create Date</td>
									<td class="fieldName" width="5%">Seq#</td>
									<td class="fieldName" width="7%">Rec Start</td>
									<td class="fieldName" width="5%">Rec End</td>
									<td class="fieldName" width="7%">Rec Type</td>
									<td class="fieldName" width="5%">Claims</td>
									<td class="fieldName" width="5%">Records</td>
									<td class="fieldName" width="12%">Batch Process Date</td>
									<td class="fieldName" width="15%">Reject Reason</td>
								</tr>
								<logic:iterate id="batchAck" name="batchAcks"
									property="batchAckReportBeanVector">
									<tr>
										<td class="dataTable" width="5%"><bean:write
											name="batchAck" property="batchNumber" /></td>
										<td class="dataTable" width="5%"><bean:write
											name="batchAck" property="operatorNumber" /></td>
										<td class="dataTable" width="7%"><bean:write
											name="batchAck" property="providerNumber" /></td>
										<td class="dataTable" width="4%"><bean:write
											name="batchAck" property="groupNumber" /></td>
										<td class="dataTable" width="7%"><bean:write
											name="batchAck" property="batchCreateDate" /></td>
										<td class="dataTable" width="5%"><bean:write
											name="batchAck" property="batchSequenceNumber" /></td>
										<td class="dataTable" width="7%"><bean:write
											name="batchAck" property="microStart" /></td>
										<td class="dataTable" width="5%"><bean:write
											name="batchAck" property="microEnd" /></td>
										<td class="dataTable" width="7%"><bean:write
											name="batchAck" property="microType" /></td>
										<td class="dataTable" width="5%"><bean:write
											name="batchAck" property="claimNumber" /></td>
										<td class="dataTable" width="5%"><bean:write
											name="batchAck" property="recordNumber" /></td>
										<td class="dataTable" width="12%"><bean:write
											name="batchAck" property="batchProcessDate" /></td>
										<td class="dataTable" width="15%"><bean:write
											name="batchAck" property="explain" /></td>
									</tr>
								</logic:iterate>
							</logic:present>


							<logic:present name="messages">
								<logic:iterate id="msg" name="messages">
									<tr>
										<td><pre><bean:write name="msg" /></pre></td>
									</tr>
								</logic:iterate>
							</logic:present>


							<logic:present name="outputSpecs">
								<tr>
									<td class="fieldName" width="8%">Health #</td>
									<td class="fieldName" width="3%">Ver</td>
									<td class="fieldName" width="10%">Response Code</td>
									<td class="fieldName" width="10%">Identifier</td>
									<td class="fieldName" width="3%">Sex</td>
									<td class="fieldName" width="10%">DOB</td>
									<td class="fieldName" width="10%">Expiry</td>
									<td class="fieldName" width="10%">Last Name</td>
									<td class="fieldName" width="10%">First Name</td>
									<td class="fieldName" width="10%">Second Name</td>
									<td class="fieldName" width="16%">Reserved for MOH</td>
								</tr>
								<logic:iterate id="outputSpec" name="outputSpecs"
									property="EDTOBECOutputSecifiationBeanVector">
									<tr>
										<td class="dataTable" width="8%"><bean:write
											name="outputSpec" property="healthNo" /></td>
										<td class="dataTable" width="3%"><bean:write
											name="outputSpec" property="version" /></td>
										<td class="dataTable" width="10%"><bean:write
											name="outputSpec" property="responseCode" /></td>
										<td class="dataTable" width="10%"><bean:write
											name="outputSpec" property="identifier" /></td>
										<td class="dataTable" width="3%"><bean:write
											name="outputSpec" property="sex" /></td>
										<td class="dataTable" width="10%"><bean:write
											name="outputSpec" property="DOB" /></td>
										<td class="dataTable" width="10%"><bean:write
											name="outputSpec" property="expiry" /></td>
										<td class="dataTable" width="10%"><bean:write
											name="outputSpec" property="lastName" /></td>
										<td class="dataTable" width="10%"><bean:write
											name="outputSpec" property="firstName" /></td>
										<td class="dataTable" width="10%"><bean:write
											name="outputSpec" property="secondName" /></td>
										<td class="dataTable" width="16%"><bean:write
											name="outputSpec" property="MOH" /></td>
									</tr>
								</logic:iterate>
							</logic:present>
						<tr>
							<td><input type="button" name="Button"
								value="<bean:message key="global.btnClose"/>"
								onClick="window.close()"></td>
						</tr>

						</td>
						</tr>
					</table>
</body>
</html:html>
