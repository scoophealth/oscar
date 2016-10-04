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


<%@ page import="java.util.*,oscar.oscarReport.pageUtil.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarEncounter.Measurements.msgAddMeasurementType" /></title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<script type="text/javascript">
    function set(target) {
     document.forms[0].forward.value=target;
};
</script>

</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<html:errors />
<html:form 	action="/oscarEncounter/oscarMeasurements/AddMeasurementType.do" onsubmit="return validateForm()">
	<table class="MainTable" id="scrollNumber1" name="encounterTable">
		<tr class="MainTableTopRow">
			<td class="MainTableTopRowLeftColumn"><bean:message
				key="oscarEncounter.Measurements.msgMeasurements" /></td>
			<td class="MainTableTopRowRightColumn">
			<table class="TopStatusBar">
				<tr>
					<td><bean:message
						key="oscarEncounter.Measurements.msgAddMeasurementType" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="MainTableLeftColumn"></td>
			<td class="MainTableRightColumn">
			<table border=0 cellspacing=4 width=400>
				<tr>
					<td>
					<table>
						<tr>
							<td>
						<tr>
							<td colspan="2"><logic:present name="messages">
								<logic:iterate id="msg" name="messages">
									<bean:write name="msg" />
									<br>
								</logic:iterate>
							</logic:present></td>
						</tr>
						<tr>
							<th align="left" class="td.tite" width="5"><bean:message
								key="oscarEncounter.oscarMeasurements.Measurements.headingType" />
							
							</th>
							<td><html:text property="type" /></td>
						</tr>
						<tr>
							<th align="left" class="td.tite"><bean:message
								key="oscarEncounter.oscarMeasurements.Measurements.headingTypeDesc" />
							</th>
							<td><html:text property="typeDesc" /></td>
						</tr>
						<tr>
							<th align="left" class="td.tite" width="50"><bean:message
								key="oscarEncounter.oscarMeasurements.Measurements.headingDisplayName" />
							</th>
							<td><html:text property="typeDisplayName" /></td>
						</tr>
						<tr>
							<th align="left" class="td.tite"><bean:message
								key="oscarEncounter.oscarMeasurements.Measurements.headingMeasuringInstrc" />
							</th>
							<td><html:text property="measuringInstrc" /></td>
						</tr>
						<tr>
							<th align="left" class="td.tite"><bean:message
								key="oscarEncounter.oscarMeasurements.Measurements.headingValidation" />
							</th>
							<td><html:select property="validation">
								<html:options collection="validations" property="id"
									labelProperty="name" />
							</html:select></td>
						</tr>
						<tr>
							<td>
							<table>
								<tr>
									<td><input type="button" name="Button"
										value="<bean:message key="global.btnClose"/>"
										onClick="window.close()"></td>
									<td><input type="submit" name="submit"
										value="<bean:message key="oscarEncounter.oscarMeasurements.MeasurementsAction.addBtn"/>"/></td>
								</tr>
							</table>
							</td>
						</tr>
						<input type="hidden" name="msgBetween"
							value="<bean:message key="oscarEncounter.oscarMeasurements.AddMeasurementType.duplicateType"/>" />
						<input type="hidden" name="msgBetween"
							value="<bean:message key="oscarEncounter.oscarMeasurements.AddMeasurementType.successful"/>" />
						</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="MainTableBottomRowLeftColumn"></td>
			<td class="MainTableBottomRowRightColumn"></td>
		</tr>
	</table>
</html:form>

<script type="text/javascript">
function validateForm()
{
  var a=document.forms[0]["type"].value;
  var b=document.forms[0]["typeDesc"].value;
  var c=document.forms[0]["typeDisplayName"].value;
  
  if (a==null || a==""){	
  	alert("Please enter a type");
  	return false;
  }else if(b==null || b==""){
  	alert("Please enter a type description");
  	return false;	  
  }else if(c==null || c==""){
  	alert("Please enter a display name");
  	return false;	  
  }
}
</script>
</body>
</html:html>
