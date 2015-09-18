<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/oscarProperties-tag.tld" prefix="oscarProp"%>
<%@ page import="oscar.oscarRx.data.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_rx" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_rx");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<logic:notPresent name="RxSessionBean" scope="session">
	<logic:redirect href="error.html" />
</logic:notPresent>
<logic:present name="RxSessionBean" scope="session">
	<bean:define id="bean" type="oscar.oscarRx.pageUtil.RxSessionBean"
		name="RxSessionBean" scope="session" />
	<logic:equal name="bean" property="valid" value="false">
		<logic:redirect href="error.html" />
	</logic:equal>
</logic:present>
<%
oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean)pageContext.findAttribute("bean");
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Prescription Print History</title>
<link rel="stylesheet" type="text/css" href="styles.css">

<html:base />

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

</head>

<bean:define id="patient" type="oscar.oscarRx.data.RxPatientData.Patient" name="Patient" />
<%
	String scriptNo = request.getParameter("scriptNo");
	//load prescription
	oscar.oscarRx.data.RxPrescriptionData.Prescription[] prescribedDrugs = patient.getPrescribedDrugScripts();
	oscar.oscarRx.data.RxPrescriptionData.Prescription prescription = null;
	for(int x=0;x<prescribedDrugs.length;x++) {
		if(prescribedDrugs[x].getScript_no() != null && prescribedDrugs[x].getScript_no().equals(scriptNo)) {
			prescription = prescribedDrugs[x];
		}
	}
	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");

%>

<body topmargin="0" leftmargin="0" vlink="#0000FF">
<table border="0" cellpadding="0" cellspacing="0"
	style="border-collapse: collapse" bordercolor="#111111" width="100%"
	id="AutoNumber1" height="100%">
	<tr>
		<td width="100%" height="100%" valign="top"><!--Column Two Row Two-->
		<table cellpadding="0" cellspacing="2"
			style="border-collapse: collapse" bordercolor="#111111" width="100%"
			height="100%">
			<!----Start new rows here-->
			<tr>
				<td align="right"><span><input type="button"
					onclick="window.print();" value="Print" class="printCell"></span>
				</td>
			</tr>
			<tr>
				<td>
				<div class="DivContentSectionHead">Prescription Print History</div>
				</td>
			</tr>
					
			<tr>
				<td>
				<table>
					<tr>
						<td width="100%"><!--<div class="Step1Text" style="width:100%">-->
						<table width="100%" cellpadding="3">
							<tr>
								<th align="left" width="50%" nowrap="nowrap"><b>Print Date</b></th>
								<th align="left" width="50%" nowrap="nowrap"><b>Provider</b></th>								
							</tr>
							<%
								java.util.Date originalPrintDate = prescription.getPrintDate();
								String originalProviderNo = prescription.getProviderNo();
							%>
							<tr>
								<td width="50%" valign="top" nowrap="nowrap"><%=oscar.util.DateUtils.formatDate(originalPrintDate,request.getLocale()) %></td>
								<td width="50%" valign="top" nowrap="nowrap"><%=providerDao.getProvider(originalProviderNo).getFormattedName() %></td>								
							</tr>
										
							<%
								String datesReprinted = prescription.getDatesReprinted();
								if(datesReprinted == null) datesReprinted="";
								String datesReprintedArr[] = datesReprinted.split(",");
								for(int x=0;x<datesReprintedArr.length;x++) {									
										String drp = null;
										String pn = null;
										String providerName="";
										if(datesReprintedArr[x].indexOf(";")!=-1) {
											String t[] = datesReprintedArr[x].split(";");
											drp = t[0];
											pn = t[1];
											providerName = providerDao.getProvider(pn).getFormattedName();
										} else {
											drp = datesReprintedArr[x];
										}
										%>
											<tr>
												<td width="50%" valign="top" nowrap="nowrap"><%=drp%></td>
												<td width="50%" valign="top" nowrap="nowrap"><%=providerName %></td>
											</tr>
										<%
								}
							%>						
						</table>

						</div>
						<div style="margin-top: 10px; margin-left: 20px; width: 100%">
						<table width="100%" cellspacing=0 cellpadding=0>
							<tr>
								
							</tr>
						</table>
						<!--</div>-->
						</td>
					</tr>
				</table>
				</td>
			</tr>





			<!----End new rows here-->
			<tr height="100%">
				<td></td>
			</tr>
		</table>
		</td>
	</tr>





</table>





</body>
</html:html>
