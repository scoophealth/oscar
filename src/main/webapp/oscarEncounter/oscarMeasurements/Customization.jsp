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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.measurements" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_admin.measurements");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="oscar.oscarEncounter.pageUtil.*"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.pageUtil.*"%>
<%@ page import="java.util.Vector"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarEncounter.Measurements.msgCustomization" /></title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<html:base />
<script type="text/javascript">
function popupOscarConS(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultChoice.oscarConS"/>", windowprops);  
}
</script>
</head>

<link rel="stylesheet" type="text/css" href="../styles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF">
<html:errors />
<table>
	<tr>
		<td class=Title colspan="2"><bean:message
			key="oscarEncounter.Measurements.msgGroup" /></td>
	</tr>
	<tr>
		<td>
		<table class=messButtonsA cellspacing=0 cellpadding=3>
			<tr>
				<td class="messengerButtonsA" width="200"><a href=#
					onClick="popupOscarConS(300,1000,'SetupStyleSheetList.do')"
					class="messengerButtons"><bean:message
					key="oscarEncounter.Index.measurements.addMeasurementGroup" /></a></td>
			</tr>
		</table>
		</td>
		<td>
		<table class=messButtonsA cellspacing=0 cellpadding=3>
			<tr>
				<td class="messengerButtonsA" width="200"><a href=#
					onClick="popupOscarConS(300,1000,'SetupGroupList.do')"
					class="messengerButtons"><bean:message
					key="oscarEncounter.Index.measurements.editMeasurementGroup" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class=Title colspan="2"><bean:message
			key="oscarEncounter.Measurements.msgType" /></td>
	</tr>
	<tr>
		<td>
		<table class=messButtonsA cellspacing=0 cellpadding=3>
			<tr>
				<td class="messengerButtonsA" width="200"><a href=#
					onClick="popupOscarConS(700,1000,'SetupDisplayMeasurementTypes.do')"
					class="messengerButtons"><bean:message
					key="oscarEncounter.Index.measurements.viewMeasurementType" /></a></td>
			</tr>
		</table>
		</td>
		<td>
		<table class=messButtonsA cellspacing=0 cellpadding=3>
			<tr>
				<td class="messengerButtonsA" width="200"><a href=#
					onClick="popupOscarConS(300,1000,'SetupAddMeasurementType.do')"
					class="messengerButtons"><bean:message
					key="oscarEncounter.Index.measurements.addMeasurementType" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class=Title colspan="2">Mappings -- 
                <a href=# onClick="popupOscarConS(300,1000,'viewMeasurementMap.jsp')" class="messengerButtons">View Mapping</a></td>
	</tr>
	<tr>
		<td>
		<table class=messButtonsA cellspacing=0 cellpadding=3>
			<tr>
				<td class="messengerButtonsA" width="200"><a href=#
					onClick="popupOscarConS(700,1000,'AddMeasurementMap.do')"
					class="messengerButtons">Add Measurement Mapping</a></td>
			</tr>
		</table>
		</td>
		<td>
		<table class=messButtonsA cellspacing=0 cellpadding=3>
			<tr>
				<td class="messengerButtonsA" width="200"><a href=#
					onClick="popupOscarConS(600,700,'RemoveMeasurementMap.do')"
					class="messengerButtons">Remove/Remap Measurement Mapping</a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class=Title colspan="2"><bean:message
			key="oscarEncounter.Measurements.msgMeasuringInstruction" /></td>
	</tr>
	<tr>
		<td>
		<table class=messButtonsA cellspacing=0 cellpadding=3>
			<tr>
				<td class="messengerButtonsA" width="200"><a href=#
					onClick="popupOscarConS(300,1000,'SetupAddMeasuringInstruction.do')"
					class="messengerButtons"><bean:message
					key="oscarEncounter.Index.measurements.addMeasuringInstruction" /></a>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class=Title colspan="2"><bean:message
			key="oscarEncounter.Measurements.msgStyleSheets" /></td>
	</tr>
	<tr>
		<td>
		<table class=messButtonsA cellspacing=0 cellpadding=3>
			<tr>
				<td class="messengerButtonsA" width="200"><a href=#
					onClick="popupOscarConS(300,1000,'SetupDisplayMeasurementStyleSheet.do')"
					class="messengerButtons"><bean:message
					key="oscarEncounter.Index.measurements.viewMeasurementStyleSheet" /></a>
				</td>
			</tr>
		</table>
		</td>
		<td>
		<table class=messButtonsA cellspacing=0 cellpadding=3>
			<tr>
				<td class="messengerButtonsA" width="200"><a href=#
					onClick="popupOscarConS(300,1000,'AddMeasurementStyleSheet.jsp')"
					class="messengerButtons"><bean:message
					key="oscarEncounter.Index.measurements.addMeasurementStyleSheet" /></a>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td></td>
	</tr>
</table>
</body>
</html:html>
