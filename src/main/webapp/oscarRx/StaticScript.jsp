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
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.DrugDao"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.common.model.Drug"%>
<%@page import="oscar.oscarRx.data.RxPrescriptionData"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager"%>
<%@page import="org.oscarehr.caisi_integrator.ws.DemographicWs"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.oscarRx.StaticScriptBean"%>
<%@page import="java.util.ArrayList"%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_rx" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_rx");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<html:html locale="true">
<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
<title><bean:message key="StaticScript.title" /></title>

<html:base />

<logic:notPresent name="RxSessionBean" scope="session">
	<logic:redirect href="error.html" />
</logic:notPresent>
<logic:present name="RxSessionBean" scope="session">
	<bean:define id="bean" type="oscar.oscarRx.pageUtil.RxSessionBean" name="RxSessionBean" scope="session" />
	<logic:equal name="bean" property="valid" value="false">
		<logic:redirect href="error.html" />
	</logic:equal>
</logic:present>
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	oscar.oscarRx.pageUtil.RxSessionBean bean=(oscar.oscarRx.pageUtil.RxSessionBean)pageContext.findAttribute("bean");
%>


<link rel="stylesheet" type="text/css" href="styles.css">

<script language="javascript">
    function ShowDrugInfo(gn){
        window.open("drugInfo.do?GN=" + escape(gn), "_blank",
            "location=no, menubar=no, toolbar=no, scrollbars=yes, status=yes, resizable=yes");
    }
</script>

<%
	String regionalIdentifier=request.getParameter("regionalIdentifier");
	String cn=request.getParameter("cn");
        String bn=request.getParameter("bn");
	Integer currentDemographicNo=bean.getDemographicNo();

	ArrayList<StaticScriptBean.DrugDisplayData> drugs=StaticScriptBean.getDrugList(loggedInInfo, currentDemographicNo, regionalIdentifier, cn,bn);

	oscar.oscarRx.data.RxPatientData.Patient patient=oscar.oscarRx.data.RxPatientData.getPatient(loggedInInfo, currentDemographicNo);
	String annotation_display=org.oscarehr.casemgmt.model.CaseManagementNoteLink.DISP_PRESCRIP;
%>

<script language="javascript">
    function addFavorite(drugId, brandName){
        var favoriteName = window.prompt('Please enter a name for the Favorite:',
            brandName);

        if (favoriteName.length > 0){
            var s = escape('?regionalIdentifier=<%=regionalIdentifier%>&cn=<%=cn%>');

            window.location.href = 'addFavoriteStaticScript.do?drugId='
                + escape(drugId) + '&favoriteName=' + escape(favoriteName)
                + '&returnParams=' + s;
        }
    }
</script>


</head>

<body topmargin="0" leftmargin="0" vlink="#0000FF">
<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber1" height="100%">
	<%@ include file="TopLinks.jsp"%><!-- Row One included here-->
	<tr>
		<%@ include file="SideLinksNoEditFavorites.jsp"%><!-- <td></td>Side Bar File --->
		<td width="100%" style="border-left: 2px solid #A9A9A9;" height="100%" valign="top">
		<table cellpadding="0" cellspacing="2" style="border-collapse: collapse" bordercolor="#111111" width="100%" height="100%">
			<tr>
				<td width="0%" valign="top">
				<div class="DivCCBreadCrumbs"><a href="SearchDrug.jsp"> <bean:message key="SearchDrug.title" /></a> &gt; <b><bean:message key="StaticScript.title" /></b></div>
				</td>
			</tr>
			<!----Start new rows here-->
			<tr>
				<td style="font-size: small;"><br />
				<br />
				<b>Patient Name:</b> <%=patient.getFirstName()%> <%=patient.getSurname()%> <br />
				<br />
				</td>
			</tr>
			<tr>
				<td>
				<table cellspacing="10" cellpadding="0">
					<tr style="height: 20px">
						<th align="left"><b>Provider</b></th>
						<th align="left"><b>Start Date</b></th>
						<th align="left"><b>End Date</b></th>
						<th align="left"><b>Written Date</b></th>
						<th align="left"><b>Medication Details</b></th>
						<th colspan="2"></th>
					</tr>
					<%
						for (StaticScriptBean.DrugDisplayData drug : drugs)
							{
								String arch="";
								if (drug.isArchived)
								{
									arch="text-decoration: line-through;";
								}
					%>
					<tr style="height:20px;<%=arch%>">
						<td><%=drug.providerName%></td>
						<td><%=drug.startDate%></td>
						<td><%=drug.endDate%></td>
						<td><%=drug.writtenDate%></td>
						<td><%=drug.prescriptionDetails%></td>
						<td>
						<%
							if (drug.customName==null)
									{
						%> <a href="javascript:ShowDrugInfo('<%=drug.genericName%>');">Info</a> <%
							}
						%>
						</td>
						<td>
							<%
								if (drug.isLocal)
								{
									%>
										<input type="button" value="Annotation" title="Annotation" style="width: 55px" class="ControlPushButton" onclick="window.open('../annotation/annotation.jsp?display=<%=annotation_display%>&table_id=<%=drug.localDrugId%>&demo=<%=currentDemographicNo%>','anwin','width=400,height=500');">
									<%
								}
							%>
						</td>
						<td>
							<%
								if (drug.isLocal)
								{
									%>
										<html:form action="/oscarRx/rePrescribe">
											<html:hidden property="drugList" value="<%=drug.localDrugId.toString()%>" />
											<input type="hidden" name="method" value="represcribe">
											<html:submit style="width:100px" styleClass="ControlPushButton" value="Re-prescribe" />
										</html:form> <input type="button" align="top" value="Add to Favorites" style="width: 100px" class="ControlPushButton" onclick="javascript:addFavorite(<%=drug.localDrugId%>, '<%=(drug.customName!=null&&(!drug.customName.equalsIgnoreCase("null")))?drug.customName:drug.brandName%>');" />
									<%
								}
								else
								{
									%>
										<form action="<%=request.getContextPath()%>/oscarRx/searchDrug.do" method="post">
											<input type="hidden" name="demographicNo" value="<%=currentDemographicNo%>" />
											<%
												String searchString=drug.brandName;
												if (searchString==null) searchString=drug.customName;
												if (searchString==null) searchString=drug.genericName;
												if (searchString==null) searchString=drug.prescriptionDetails;
											%>
											<input type="hidden" name="searchString" value="<%=searchString%>" />
											<input type="submit" class="ControlPushButton" value="Search to Re-prescribe" />
										</form>
									<%
								}
							%>
						</td>
					</tr>
					<%
						}
					%>
				</table>
				</td>
			</tr>
			<tr>
				<td><br />
				<br />
				<input type="button" value="Back To Search Drug" class="ControlPushButton" onclick="javascript:window.location.href='SearchDrug.jsp';" /></td>
			</tr>
			<!----End new rows here-->
			<tr height="100%">
				<td></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="0%" style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
		<td height="0%" style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
	</tr>
	<tr>
		<td width="100%" height="0%" colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC" colspan="2"></td>
	</tr>
</table>

</body>
</html:html>
